// ===
// Copyright (C) 2000, 2001 Roger Butenuth, Andreas Gampe, Stefan G�tz, Ulrich K�ster, Sebastian Pappert, Klaas Prause, Enno Rehling, Sebastian Tusk
// ---
// This file is part of the Eressea Java Code Base, see the file LICENSING for the licensing information applying to this file
// ---
// $Id$
// ===

package com.eressea.util.comparator;


import java.util.Comparator;

import com.eressea.util.Sorted;

/**
 * A comparator imposing an ordering on objects implementing the
 * Sorted interface by comparing their sorting indices.
 * <p>Note: this comparator can impose orderings that are inconsistent with
 * equals.</p>
 * <p>In order to overcome the inconsistency with equals this comparator
 * allows the introduction of a sub-comparator which is applied in cases
 * of equality. I.e. if the two compared objects have the same sorting index
 * and they would be regarded as equal by this comparator, instead of 0
 * the result of the sub-comparator's comparison is returned.</p>
 */
public class SortIndexComparator implements Comparator {
	protected Comparator sameIndexSubCmp = null;

	/**
	 * Creates a new SortIndexComparator object.
	 * @param sameIndexSubComparator if two objects with the same
	 * sort index are compared, the given sub-comparator is applied
	 * (if not <tt>null</tt>).
	 */
	public SortIndexComparator (Comparator sameIndexSubComparator) {
		sameIndexSubCmp = sameIndexSubComparator;
	}

	/**
	 * Compares its two arguments for order according to their sort indices.
	 *
	 * @return the numerical difference of <tt>o1</tt>'s and <tt>o2</tt>'s
	 * sort indices. If the sort indices are
	 * equal and a sub-comparator was specified, the result of that
	 * sub-comparator's comparison is returned.
	 */
	public int compare(Object o1, Object o2) {
		int retVal = ((Sorted)o1).getSortIndex() - ((Sorted)o2).getSortIndex();
		if (retVal == 0 && sameIndexSubCmp != null) {
			retVal = sameIndexSubCmp.compare(o1, o2);
		}
		return retVal;
	}

	/**
	 * Checks the Object <tt>o</tt> for equality.
	 * @returns <tt>false</tt>
	 */
	public boolean equals(Object o1) {
		return false;
	}
}
