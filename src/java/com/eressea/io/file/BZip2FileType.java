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

package com.eressea.io.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.bzip2.CBZip2OutputStream;

/**
 * TODO: DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class BZip2FileType extends FileType {
	BZip2FileType(String aFile) throws IOException {
		super(aFile);
	}

	protected InputStream createInputStream() throws IOException {
		// normally the following lines would be ok. But somehow it does not work, so we copy the content of the 
		// bzip2file into a tmpfile for reading with deleteonexit set.
		//return new CBZip2InputStream(new FileInputStream(fileName));
		File	    tmpfile = CopyFile.createTempFile();
		InputStream fis    = new FileInputStream(new File(filename));
		int		    magic3 = fis.read();
		int		    magic4 = fis.read();

		if((magic3 != 'B') || (magic4 != 'Z')) {
			throw new IOException("File " + filename +
								  " is missing bzip2 header BZ.");
		}

		CopyFile.copyStreams(new CBZip2InputStream(fis),
							 new FileOutputStream(tmpfile));

		return new FileInputStream(tmpfile);
	}

	protected OutputStream createOutputStream() throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filename)));
		bos.write('B');
		bos.write('Z');

		return new CBZip2OutputStream(bos);
	}
}
