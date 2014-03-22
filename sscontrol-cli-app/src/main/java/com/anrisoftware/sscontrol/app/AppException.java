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
package com.anrisoftware.sscontrol.app;

import java.util.Map;

import com.anrisoftware.globalpom.exceptions.Context;

/**
 * Exception that causes the application to terminate.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class AppException extends Exception {

	private final Context<AppException> context;

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public AppException(String message, Throwable cause) {
		super(message, cause);
		this.context = new Context<AppException>(this);
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public AppException(String message) {
		super(message);
		this.context = new Context<AppException>(this);
	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public AppException(Object message, Throwable cause) {
		super(message.toString(), cause);
		this.context = new Context<AppException>(this);
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public AppException(Object message) {
		super(message.toString());
		this.context = new Context<AppException>(this);
	}

	/**
	 * @see Context#addContext(String, Object)
	 */
	public AppException add(String name, Object value) {
		context.addContext(name, value);
		return this;
	}

	/**
	 * @see Context#addContext(String, Object)
	 */
	public AppException add(Object name, Object value) {
		context.addContext(name.toString(), value);
		return this;
	}

	/**
	 * @see Context#getContext()
	 */
	public Map<String, Object> getContext() {
		return context.getContext();
	}

	@Override
	public String toString() {
		return context.toString();
	}
}
