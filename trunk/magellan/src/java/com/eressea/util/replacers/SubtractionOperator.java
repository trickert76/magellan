// ===
// Copyright (C) 2000, 2001 Roger Butenuth, Andreas Gampe, Stefan G�tz, Sebastian Pappert, Klaas Prause, Enno Rehling, Sebastian Tusk
// ---
// This file is part of the Eressea Java Code Base, see the file LICENSING for the licensing information applying to this file
// ===

package com.eressea.util.replacers;

import java.util.Map;

import com.eressea.util.CollectionFactory;
/**
 * An subtraction operator.
 *
 * @author  Andreas
 * @version 
 */
public class SubtractionOperator extends AbstractOperator {

	public SubtractionOperator() {
		super(2);
	}
	
	public Object compute(Object[] numbers) {
		return new Float(((Number)numbers[0]).floatValue()-((Number)numbers[1]).floatValue());
	}
	
	// pavkovic 2003.01.28: this is a Map of the default Translations mapped to this class
	// it is called by reflection (we could force the implementation of an interface,
	// this way it is more flexible.)
	// Pls use this mechanism, so the translation files can be created automagically
	// by inspecting all classes.
	private static Map defaultTranslations;
	public synchronized static Map getDefaultTranslations() {
		if(defaultTranslations == null) {
			defaultTranslations = CollectionFactory.createHashtable();
			// FIXME(pavkovic)
			defaultTranslations.put("description","Subtrahiert die zwei folgenden Definitionselemente(entwickelt dabei andere Operatoren).");
		}
		return defaultTranslations;
	}

}
