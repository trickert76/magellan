// ===
// Copyright (C) 2000, 2001 Roger Butenuth, Andreas Gampe, Stefan G�tz, Sebastian Pappert, Klaas Prause, Enno Rehling, Sebastian Tusk
// ---
// This file is part of the Eressea Java Code Base, see the file LICENSING for the licensing information applying to this file
// ---
// $Id$
// ===

package com.eressea.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;

import com.eressea.util.file.FileType;

public class Log {
	private File baseDir = null;

	/**
	 * Creates a new Log object copying the output onto the exported
	 * print stream to a file in the specified directory.
	 * 
	 * @param baseDir name of the directory for logging output.
	 */
	public Log(File baseDir) {
		this.baseDir = baseDir;
	}
	
	/**
	 * Create a stream for writing errors to the log.
	 *  
	 * @return output stream to the error log.
	 */
	public PrintStream getPrintStream() {
		Writer out = null;
		try {
			out = FileType.createEncodingWriter(new FileOutputStream(new File(baseDir, "errors.txt").getAbsolutePath(), true));
			out = new BufferedWriter(out);
		} catch (IOException e) {
		}
		return new PrintStream(new StreamWrapper(out));
	}
	
	/**
	 * Wrapper for the logging stream for adding timestamp and linebreaks to output.
 	 */
	private class StreamWrapper extends OutputStream {
		Writer out = null;
		public StreamWrapper(Writer out) {
			super();
			this.out = out;
			Thread timeStamper = new Thread() {
				public void run() {
					while (true) {
						try {
							// 2002.05.05 pavkovic: Synchronization needed because of multithreading
							synchronized(StreamWrapper.this.out) {
								StreamWrapper.this.out.write((new java.util.Date(System.currentTimeMillis())).toString());
								StreamWrapper.this.out.write("\n\r");
								StreamWrapper.this.out.flush();
							}
						} catch (IOException e) {
						}
						try {
							Thread.sleep(600000);
						} catch (InterruptedException e) {
						}
					}
				}
			};
			timeStamper.start();

			Thread flusher = new Thread() {
				public void run() {
					while (true) {
						try {
							StreamWrapper.this.out.flush();
						} catch (IOException e) {
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
				}
			};
			flusher.start();
		}

		/**
		 * 
		 * Write value to the stream and to the console. 
		 */	
		public void write(int b) throws IOException {
			System.out.write(b);
			out.write(b);
		}
	}
}
