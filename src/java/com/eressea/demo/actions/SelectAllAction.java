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

package com.eressea.demo.actions;

import java.util.Iterator;
import java.util.Map;

import com.eressea.Coordinate;
import com.eressea.Region;

import com.eressea.demo.Client;

import com.eressea.event.GameDataEvent;
import com.eressea.event.GameDataListener;
import com.eressea.event.SelectionEvent;
import com.eressea.event.SelectionListener;

import com.eressea.util.CollectionFactory;

/**
 * DOCUMENT ME!
 *
 * @author Ulrich K�ster
 */
public class SelectAllAction extends MenuAction implements SelectionListener, GameDataListener {
	private Client client;
	private Map selectedRegions = CollectionFactory.createHashtable();

	/**
	 * Creates a new SelectAllAction object.
	 *
	 * @param parent TODO: DOCUMENT ME!
	 */
	public SelectAllAction(Client parent) {
		client = parent;
		client.getDispatcher().addSelectionListener(this);
		client.getDispatcher().addGameDataListener(this);
	}

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @param e TODO: DOCUMENT ME!
	 */
	public void selectionChanged(SelectionEvent e) {
		if(e.getSource() == this) {
			return;
		}

		if((e.getSelectedObjects() != null) && (e.getSelectionType() == SelectionEvent.ST_REGIONS)) {
			selectedRegions.clear();

			for(Iterator iter = e.getSelectedObjects().iterator(); iter.hasNext();) {
				Object o = iter.next();

				if(o instanceof Region) {
					Region r = (Region) o;
					selectedRegions.put(r.getID(), r);
				}
			}
		}
	}

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @param e TODO: DOCUMENT ME!
	 */
	public void gameDataChanged(GameDataEvent e) {
		selectedRegions.clear();
	}

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @param e TODO: DOCUMENT ME!
	 */
	public void actionPerformed(java.awt.event.ActionEvent e) {
		for(Iterator iter = client.getData().regions().keySet().iterator(); iter.hasNext();) {
			Coordinate c = (Coordinate) iter.next();

			if(c.z == client.getLevel()) {
				selectedRegions.put(c, client.getData().regions().get(c));
			}
		}

		client.getDispatcher().fire(new SelectionEvent(this, selectedRegions.values(), null,
													   SelectionEvent.ST_REGIONS));
	}

	// pavkovic 2003.01.28: this is a Map of the default Translations mapped to this class
	// it is called by reflection (we could force the implementation of an interface,
	// this way it is more flexible.)
	// Pls use this mechanism, so the translation files can be created automagically
	// by inspecting all classes.
	private static Map defaultTranslations;

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @return TODO: DOCUMENT ME!
	 */
	public static synchronized Map getDefaultTranslations() {
		if(defaultTranslations == null) {
			defaultTranslations = CollectionFactory.createHashtable();
			defaultTranslations.put("name", "Select all");
			defaultTranslations.put("mnemonic", "a");
			defaultTranslations.put("accelerator", "");
			defaultTranslations.put("tooltip", "");
		}

		return defaultTranslations;
	}
}
