// ===
// Copyright (C) 2000, 2001 Roger Butenuth, Andreas Gampe, Stefan G�tz, Sebastian Pappert, Klaas Prause, Enno Rehling, Sebastian Tusk
// ---
// This file is part of the Eressea Java Code Base, see the file LICENSING for the licensing information applying to this file
// ---
// $Id$
// ===

package com.eressea.event;

import com.eressea.GameData;

/**
 * An event indicating that the previous game data object is no longer
 * valid e.g. after the user loaded a report.
 *
 * @see GameDataListener
 * @see EventDispatcher
 */
public class GameDataEvent extends TimeStampedEvent {
	private GameData data;

	/**
	 * Creates an event object.
	 *
	 * @param source the object that originated the event.
	 * @param data the new game data object.
	 */
	public GameDataEvent(Object source, GameData data) {
		super(source);
		this.data = data;
	}

	/**
	 * Returns the new valid game data object.
	 */
	public GameData getGameData() {
		return data;
	}
}
