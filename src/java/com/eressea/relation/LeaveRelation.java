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

package com.eressea.relation;

import com.eressea.Unit;
import com.eressea.UnitContainer;

/**
 * A relation indicating that a unit leaves a unit container.
 */
public class LeaveRelation extends UnitContainerRelation {
	/**
	 * Creates a new LeaveRelation object.
	 *
	 * @param s The source unit
	 * @param t The target container
	 * @param line The line in the source's orders
	 */
	public LeaveRelation(Unit s, UnitContainer t, int line) {
		super(s, t, line);
	}
}
