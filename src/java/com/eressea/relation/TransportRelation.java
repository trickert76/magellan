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
 * $Id$
 */

package com.eressea.relation;

import com.eressea.Unit;

/**
 * This relation indicates that the source unit is transporting the the target
 * unit. Although the order semantics require a TRANSPORTIERE and FAHRE order
 * for the carrier and each passenger, this relation does not enforce these
 * semantics and is established by the TRANSPORTIERE order only.
 */
public class TransportRelation extends InterUnitRelation {
	/**
	 * Creates a new relation indicating that unit s transports unit t
	 *
	 * @param s TODO: DOCUMENT ME!
	 * @param t TODO: DOCUMENT ME!
	 * @param line TODO: DOCUMENT ME!
	 */
	public TransportRelation(Unit s, Unit t, int line) {
		super(s, t, line);
	}
}
