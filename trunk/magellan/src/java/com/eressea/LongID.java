// ===
// Copyright (C) 2000, 2001 Roger Butenuth, Andreas Gampe, Stefan G�tz, Sebastian Pappert, Klaas Prause, Enno Rehling, Sebastian Tusk
// ---
// This file is part of the Eressea Java Code Base, see the file LICENSING for the licensing information applying to this file
// ---
// $Id$
// ===

package com.eressea;

import java.util.Map;

import com.eressea.util.CollectionFactory;

/**
 * A class establishing the uniqueness property through a long.
 * This class assumes the representation of integers to be decimal in
 * all cases.
 */
public class LongID implements ID {
	/**
	 * The Long object this id is based on.
	 */
	// pavkovic 2003.09.18: changed to primitive type to avoid memory overhead
	protected final long id;
	
	/**
	 * Constructs a new LongID object from the specified integer.
	 */
	protected LongID(Long l) {
		this(l.longValue());
	}
	
	/**
	 * Constructs a new LongID object based on an Long object
	 * created from the specified long.
	 */
	protected LongID(long l) {
		this.id = l;
	}
	
	/**
	 * Creates a new LongID object by parsing the specified string
	 * for a decimal integer.
	 */
	protected LongID(String s) {
		this(Long.valueOf(s));
	}
	

	/** a static cache to use this class as flyweight factory */
	private static Map idMap = CollectionFactory.createHashMap();
	
	
	/** 
	 * Returns a (possibly) new StringID object.
	 * 
	 */
	public static LongID create(Long o) {
		if(o == null) throw new NullPointerException();
		LongID id = (LongID) idMap.get(o);
		if(id == null) {
			id = new LongID(o);
			idMap.put(o, id);
		}
		return id;
	}
	public static LongID create(String s) {
		return create(Long.valueOf(s));
	}
	public static LongID create(int i) {
		return create(new Long(i));
	}

	/**
	 * Returns a string representation of the underlying integer.
	 */
	public String toString() {
		return Long.toString(id);
	}

	/**
	 * Returns a string representation of the underlying integer.
	 */
	public String toString(String delim) {
		return toString();
	}
	
	/**
	 * Returns the value of this LongID as an int.
	 */
	public long longValue() {
		return longValue();
	}
	
	/**
	 * Indicates whether this LongID object is equal to some other
	 * object.
	 *
	 * @returns true, if o is an instance of class LongID and the
	 * numerical values of this and the specified object are equal.
	 */
	public boolean equals(Object o) {
		return this == o || 
			(o instanceof LongID && id == ((LongID)o).id);
	}
	
	/**
	 * Imposes a natural ordering on LongID objects which is based
	 * on the natural ordering of the underlying integers.
	 */
	public int compareTo(Object o) {
		long anotherId = ((LongID) o).id;
		return id<anotherId ? -1 : (id==anotherId ? 0 : 1);
	}
	
	/**
	 * Returns a hash code for this object.
	 *
	 * @returns a hash code value based on the hash code returned by
	 * the underlying Long object.
	 */
	public int hashCode() {
		return (int)(id ^ (id >>> 32));
	}
	
	/**
	 * Returns a copy of this LongID object.
	 */
	public Object clone() throws CloneNotSupportedException {
		// pavkovic 2003.07.08: we dont really clone this object as LongID is unchangeable after creation
		return this;
	}
}
