// ===
// Copyright (C) 2000, 2001 Roger Butenuth, Andreas Gampe, Stefan G�tz, Ulrich K�ster, Sebastian Pappert, Klaas Prause, Enno Rehling, Sebastian Tusk
// ---
// This file is part of the Eressea Java Code Base, see the file LICENSING for the licensing information applying to this file
// ---
// $Id$
// ===

package com.eressea.demo.actions;


import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

import java.util.Map;
import com.eressea.util.CollectionFactory;

import javax.swing.JFileChooser;

import com.eressea.GameData;
import com.eressea.demo.Client;
import com.eressea.swing.EresseaFileFilter;
import com.eressea.swing.HistoryAccessory;
import com.eressea.util.ReportMerger;

/**
 * @author  Andreas, Ulrich K�ster
 */
public class AddCRAction extends MenuAction {
	private Client client;

	/** Creates new AddCRAction */
	public AddCRAction(Client parent) {
		client=parent;
	}

	/**
	* Called when the file->add menu is selected in order to add
	* a certain cr file to current game data. Displays a file
	* chooser and adds the selected cr file to the current game data.
	*/
	public void actionPerformed(ActionEvent e) {
		final Client theclient=client;
		Properties settings = client.getSettings();
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		EresseaFileFilter gz = new EresseaFileFilter(EresseaFileFilter.GZ_FILTER);
		EresseaFileFilter zip = new EresseaFileFilter(EresseaFileFilter.ZIP_FILTER);
		EresseaFileFilter cr = new EresseaFileFilter(EresseaFileFilter.CR_FILTER);
		fc.addChoosableFileFilter(gz);
		fc.addChoosableFileFilter(zip);
		fc.addChoosableFileFilter(cr);
		int lastFileFilter = Integer.parseInt(settings.getProperty("Client.lastSelectedAddCRFileFilter", "3"));
		fc.setFileFilter(fc.getChoosableFileFilters()[lastFileFilter]);
		File file = new File(settings.getProperty("Client.lastCRAdded", ""));
		fc.setSelectedFile(file);
		if (file.exists()) {
			fc.setCurrentDirectory(file.getParentFile());
		}
		HistoryAccessory acc = new HistoryAccessory(settings, fc);
		fc.setAccessory(acc);
		fc.setDialogTitle(getString("title"));
		if (fc.showOpenDialog(client) == JFileChooser.APPROVE_OPTION) {
			// find selected FileFilter
			int i = 0;
			while (!fc.getChoosableFileFilters()[i].equals(fc.getFileFilter())) {
				i++;
			}
			settings.setProperty("Client.lastSelectedAddCRFileFilter", String.valueOf(i));
			// force user to choose a file on save
			//client.setDataFile(null);

			ReportMerger merger = null;
			File files[] = fc.getSelectedFiles();
			if (files.length == 0) {
				merger = new ReportMerger(client.getData(), fc.getSelectedFile(),
					new ReportMerger.Loader() {
						// pavkovic 2002.11.05: prevent name clash with variable "file"
						public GameData load( File aFile ) {
							return theclient.loadCR(aFile.getAbsolutePath());
						}
					},
					new ReportMerger.AssignData() {
						public void assign( GameData _data ) {
							theclient.setData(_data);
						}
					}
				);
				settings.setProperty("Client.lastCRAdded", fc.getSelectedFile().getAbsolutePath());
			} else {
				merger = new ReportMerger(client.getData(), files,
					new ReportMerger.Loader() {
						// pavkovic 2002.11.05: prevent name clash with variable "file"
						public GameData load( File aFile ) {
							return theclient.loadCR(aFile.getAbsolutePath());
						}
					},
					new ReportMerger.AssignData() {
						public void assign( GameData _data ) {
							theclient.setData(_data);
						}
					}
				);
				settings.setProperty("Client.lastCRAdded", files[files.length-1].getAbsolutePath());
			}

			merger.merge(client);
		}
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
			defaultTranslations.put("name"       , "Add...");
			defaultTranslations.put("mnemonic"   , "d");
			defaultTranslations.put("accelerator", "ctrl D");
			defaultTranslations.put("tooltip"    , "");

			defaultTranslations.put("title"    , "Add cr file(s)");

		}
		return defaultTranslations;
	}

}
