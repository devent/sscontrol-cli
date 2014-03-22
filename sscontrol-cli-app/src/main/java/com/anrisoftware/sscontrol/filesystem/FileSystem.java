/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-cli-app.
 *
 * sscontrol-cli-app is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-cli-app is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-cli-app. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.filesystem;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;
import org.apache.commons.vfs2.FileSystemManager;

import com.anrisoftware.propertiesutils.ContextProperties;

import eu.medsea.mimeutil.MimeException;
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil2;

/**
 * Search abstract locations for specified files. The locations can be
 * directories or archives.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class FileSystem {

	private static final String FILESYSTEM_KEY = "com.anrisoftware.sscontrol.filesystem.";

	private final FileSystemManager manager;

	private final Set<FileObject> locations;

	private final MimeUtil2 mime;

	private final FileSystemLogger log;

	private final ContextProperties fileSystemsProperties;

	@Inject
	FileSystem(FileSystemLogger logger, MimeUtil2 mime,
			FileSystemManager manager,
			@Named("filesystems-properties") ContextProperties p) {
		this.log = logger;
		this.mime = mime;
		this.manager = manager;
		this.locations = createFileSet();
		this.fileSystemsProperties = p;
	}

	/**
	 * Adds the specified location.
	 * 
	 * @param url
	 *            the {@link URL}.
	 * 
	 * @throws FileSystemException
	 *             if there was an error get the MIME type for the location; if
	 *             there was an error resolve the location to a file.
	 */
	public void addLocation(URL url) throws FileSystemException {
		try {
			String path = filePath(url, mimeType(url));
			FileObject file = manager.resolveFile(path);
			locations.add(file);
		} catch (org.apache.commons.vfs2.FileSystemException e) {
			throw log.resolveFileError(url, e);
		} catch (MimeException e) {
			throw log.getMimeError(url, e);
		}
	}

	private String filePath(URL url, MimeType mimeType) {
		for (String key : fileSystemsProperties.stringPropertyNames()) {
			key = key.replace(FILESYSTEM_KEY, "");
			if (key.equals(mimeType.toString())) {
				String value = fileSystemsProperties.getProperty(key);
				return format(value, url.toString());
			}
		}
		return url.toString();
	}

	@SuppressWarnings("unchecked")
	private MimeType mimeType(URL url) throws MimeException,
			FileSystemException {
		Collection<MimeType> type;
		if (isFileScheme(url)) {
			type = mime.getMimeTypes(new File(url.getPath()));
		} else {
			type = mime.getMimeTypes(url);
		}
		Iterator<MimeType> it = type.iterator();
		if (it.hasNext()) {
			return it.next();
		}
		throw log.noMimeTypeReturned(url);
	}

	private boolean isFileScheme(URL url) {
		return url.getProtocol().equalsIgnoreCase("file");
	}

	/**
	 * Search the locations for files that are matching the specified pattern.
	 * 
	 * @param pattern
	 *            the regular expression {@link Pattern}.
	 * 
	 * @return the {@link Set} of the found files as {@link URL}.
	 * 
	 * @throws FileSystemException
	 *             if there was an error list the files of one of the locations.
	 */
	public Set<URL> findFiles(Pattern pattern) throws FileSystemException {
		Set<FileObject> set = createFileSet();
		for (FileObject location : locations) {
			set = findFiles(set, location, pattern);
		}
		return toURLs(set);
	}

	private Set<URL> toURLs(Set<FileObject> set) throws FileSystemException {
		Set<URL> urlset = new HashSet<URL>();
		for (FileObject file : set) {
			try {
				urlset.add(file.getURL());
			} catch (org.apache.commons.vfs2.FileSystemException e) {
				throw log.getURLError(file, e);
			}
		}
		return urlset;
	}

	private Set<FileObject> createFileSet() {
		return new TreeSet<FileObject>(new Comparator<FileObject>() {

			@Override
			public int compare(FileObject o1, FileObject o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	private Set<FileObject> findFiles(Set<FileObject> set, FileObject location,
			Pattern pattern) throws FileSystemException {
		try {
			FileObject[] files = listFiles(location, pattern);
			log.checkFiles(files, location);
			set.addAll(asList(files));
			return set;
		} catch (org.apache.commons.vfs2.FileSystemException e) {
			throw log.errorListFiles(location, e);
		}
	}

	private FileObject[] listFiles(FileObject location, final Pattern pattern)
			throws org.apache.commons.vfs2.FileSystemException {
		return location.findFiles(new FileSelector() {

			@Override
			public boolean traverseDescendents(FileSelectInfo fileInfo)
					throws Exception {
				return true;
			}

			@Override
			public boolean includeFile(FileSelectInfo fileInfo)
					throws Exception {
				String name = fileInfo.getFile().getName().getBaseName();
				Matcher matcher = pattern.matcher(name);
				return matcher.matches();
			}
		});
	}
}
