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

package com.eressea.demo;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import com.eressea.util.logging.Logger;


/**
 * TODO: undo/redo seems broken
 *
 * @author Andreas
 */
public class MagellanUndoManager extends UndoManager {
	private final static Logger log = Logger.getInstance(MagellanUndoManager.class);

	/** TODO: DOCUMENT ME! */
	public static final String UNDO = "Undo_Changed";

	/** TODO: DOCUMENT ME! */
	public static final String REDO = "Redo_Changed";

	// this is basicall needed to attach RedoAction and UndoAction to this UndoManager
	private PropertyChangeSupport list;

	/**
	 * Creates new MagellanUndoManager
	 */
	public MagellanUndoManager() {
		list = new PropertyChangeSupport(this);
	}

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @param l TODO: DOCUMENT ME!
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		list.addPropertyChangeListener(l);
	}

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @param property TODO: DOCUMENT ME!
	 * @param l TODO: DOCUMENT ME!
	 */
	public void addPropertyChangeListener(String property, PropertyChangeListener l) {
		list.addPropertyChangeListener(property, l);
	}

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @param l TODO: DOCUMENT ME!
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		list.removePropertyChangeListener(l);
	}

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @param property TODO: DOCUMENT ME!
	 * @param l TODO: DOCUMENT ME!
	 */
	public void removePropertyChangeListener(String property, PropertyChangeListener l) {
		list.removePropertyChangeListener(property, l);
	}

	/*
	 This methods must be changed to throw the events.
	 */
	public synchronized void undo() {
		// TODO: implement undo history?
		// String oldUndo=getUndoPresentationName(),oldRedo=getRedoPresentationName();
		try {
			super.undo();
			list.firePropertyChange(REDO, false, canRedo());
			list.firePropertyChange(UNDO, false, canUndo());
		} catch (CannotUndoException e) {
			log.error("MagellanUndoManager.undo: catched exception",e);
		}
	}

	/**
	 * TODO: DOCUMENT ME!
	 */
	public synchronized void redo() {
		try {
			// TODO: implement redo history?
			// String oldUndo=getUndoPresentationName(),oldRedo=getRedoPresentationName();
			super.redo();
			list.firePropertyChange(REDO, false, canRedo());
			list.firePropertyChange(UNDO, false, canUndo());
		} catch (CannotRedoException e) {
			log.error("MagellanUndoManager.redo: catched exception",e);
		}
	}

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @param e TODO: DOCUMENT ME!
	 *
	 * @return TODO: DOCUMENT ME!
	 */
	public synchronized boolean addEdit(UndoableEdit e) {
		// TODO: implement undo/redo history?
		// String oldUndo=getUndoPresentationName(),oldRedo=getRedoPresentationName();
		boolean b = super.addEdit(e);

		if(b) {
			list.firePropertyChange(UNDO, false, canUndo());
			list.firePropertyChange(REDO, false, canRedo());
		}

		return b;
	}
}
