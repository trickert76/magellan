/*
 *  Copyright (C) 2000-2003 Roger Butenuth, Andreas Gampe,
 *                          Stefan Goetz, Sebastian Pappert,
 *                          Klaas Prause, Enno Rehling,
 *                          Sebastian Tusk, Ulrich Kuester,
 *                          Ilja Pavkovic
 *
 * This file is part of the Eressea Java Code Base, see the
 * file LICENSING for the licensing information applying to
 * this file.
 *
 */

package com.eressea.util.replacers;

/**
 * DOCUMENT ME!
 *
 * @author Andreas
 * @version
 */
public interface EnvironmentDependent {
	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @param env TODO: DOCUMENT ME!
	 */
	public void setEnvironment(ReplacerEnvironment env);
}
