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

package com.eressea.io.file;

import java.io.IOException;

/**
 * This FileType represent an not specified FileType. Right now it will be
 * treated equal to cr files.
 */
public class UnknownFileType extends FileType {
	UnknownFileType(String aFile) throws IOException {
		super(aFile);
	}
}
