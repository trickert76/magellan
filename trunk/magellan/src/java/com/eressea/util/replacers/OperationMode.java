/*
 *  Copyright (C) 2000-2004 Roger Butenuth, Andreas Gampe,
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
public class OperationMode extends EnvironmentPart {
	protected boolean nullEqualsZero = false;

	/**
	 * TODO: DOCUMENT ME!
	 */
	public void reset() {
		nullEqualsZero = false;
	}

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @return TODO: DOCUMENT ME!
	 */
	public boolean isNullEqualsZero() {
		return nullEqualsZero;
	}

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @param bool TODO: DOCUMENT ME!
	 */
	public void setNullEqualsZero(boolean bool) {
		nullEqualsZero = bool;
	}
}
