/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.appmodel;

import java.io.IOException;
import java.util.Map;

import com.anrisoftware.globalpom.exceptions.Context;

/**
 * Application model exception.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ModelException extends IOException {

	private final Context<ModelException> context;

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public ModelException(String message, Throwable cause) {
		super(message, cause);
		this.context = new Context<ModelException>(this);
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public ModelException(String message) {
		super(message);
		this.context = new Context<ModelException>(this);
	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public ModelException(Object message, Throwable cause) {
		super(message.toString(), cause);
		this.context = new Context<ModelException>(this);
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public ModelException(Object message) {
		super(message.toString());
		this.context = new Context<ModelException>(this);
	}

	/**
	 * @see Context#addContext(String, Object)
	 */
	public ModelException add(String name, Object value) {
		context.addContext(name, value);
		return this;
	}

	/**
	 * @see Context#addContext(String, Object)
	 */
	public ModelException add(Object name, Object value) {
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
