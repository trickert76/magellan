// ===
// Copyright (C) 2000, 2001 Roger Butenuth, Andreas Gampe, Stefan G�tz, Sebastian Pappert, Klaas Prause, Enno Rehling, Sebastian Tusk
// ---
// This file is part of the Eressea Java Code Base, see the file LICENSING for the licensing information applying to this file
// ---
// $Id$
// ===

package com.eressea.swing;


import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.eressea.resource.ResourcePathClassLoader;
import com.eressea.util.CollectionFactory;
import com.eressea.util.VersionInfo;

/**
 *
 */
public class InfoDlg extends com.eressea.swing.InternationalizedDialog {
	private ResourceBundle componentDictionary = null;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JButton btn_OK;
	private javax.swing.JButton btn_Version;
	private javax.swing.JLabel magallanImage;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextArea jTextArea1;

	public InfoDlg(javax.swing.JFrame parent) {
		super(parent, true);
		initComponents ();

		magallanImage.setText("");
		java.net.URL url = ResourcePathClassLoader.getResourceStatically("about/magellan.gif");
		javax.swing.Icon icon = null;
		if (url != null) {
			java.awt.Image image = getToolkit().createImage( url );
			icon = new javax.swing.ImageIcon(image);
			magallanImage.setIcon(icon);
		}
		pack ();

		// center
		this.setLocation( (getToolkit().getScreenSize().width - this.getWidth())/2,
		(getToolkit().getScreenSize().height - this.getHeight())/2 );
	}

	private void initComponents() {
		jPanel1 = new javax.swing.JPanel();
		btn_OK = new javax.swing.JButton();
		btn_Version = new javax.swing.JButton();
		magallanImage = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTextArea1 = new javax.swing.JTextArea();
		getContentPane().setLayout(new java.awt.GridBagLayout());
		java.awt.GridBagConstraints gridBagConstraints1;
		setModal(true);
		setTitle(getString("window.title"));
		setBackground(new java.awt.Color (213, 169, 131));

		jPanel1.setLayout(new java.awt.GridBagLayout());
		java.awt.GridBagConstraints gridBagConstraints2;
		java.awt.GridBagConstraints gridBagConstraints3;
		jPanel1.setBackground(new java.awt.Color (213, 169, 131));

		btn_Version.setText(getString("btn.checkfornew.caption"));
		btn_Version.setBackground(new java.awt.Color (213, 169, 131));
		btn_Version.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				checkVersion(evt);
			}
		});
		gridBagConstraints3 = new java.awt.GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.gridy = 2;
		gridBagConstraints3.insets = new java.awt.Insets(5, 5, 5, 5);
		gridBagConstraints3.anchor = java.awt.GridBagConstraints.SOUTHWEST;
		jPanel1.add(btn_Version, gridBagConstraints3);


		btn_OK.setText(getString("btn.close.caption"));
		btn_OK.setBackground(new java.awt.Color (213, 169, 131));
		btn_OK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				quit();
			}
		});
		gridBagConstraints2 = new java.awt.GridBagConstraints();
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.gridy = 2;
		gridBagConstraints2.insets = new java.awt.Insets(0, 5, 5, 5);
		gridBagConstraints2.anchor = java.awt.GridBagConstraints.SOUTHEAST;
		jPanel1.add(btn_OK, gridBagConstraints2);


		magallanImage.setPreferredSize(new java.awt.Dimension(400, 200));
		magallanImage.setMinimumSize(new java.awt.Dimension(400, 200));
		magallanImage.setText("jLabel1");
		magallanImage.setBackground(new java.awt.Color (213, 169, 131));
		magallanImage.setMaximumSize(new java.awt.Dimension(400, 200));
		gridBagConstraints2 = new java.awt.GridBagConstraints();
		gridBagConstraints2.insets = new java.awt.Insets(5, 5, 0, 5);
		jPanel1.add(magallanImage, gridBagConstraints2);


		jScrollPane1.setBackground(new java.awt.Color (213, 169, 131));

		String text = getString("infotext");
		Date buildDate = VersionInfo.getBuildDate();
		if (buildDate != null) {
			Object[] msgArgs = {
				DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(buildDate)
			};
			text += (new java.text.MessageFormat(getString("versiontext"))).format(msgArgs);
		}

		jTextArea1.setWrapStyleWord(true);
		jTextArea1.setPreferredSize(new java.awt.Dimension(160, 250));
		jTextArea1.setLineWrap(true);
		jTextArea1.setEditable(false);
		jTextArea1.setText(text);
		jTextArea1.setBackground(new java.awt.Color (213, 169, 131));
		jTextArea1.setMinimumSize(new java.awt.Dimension(400, 200));
		jScrollPane1.setViewportView(jTextArea1);

		gridBagConstraints2 = new java.awt.GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.gridwidth = 2;
		gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints2.insets = new java.awt.Insets(5, 5, 5, 5);
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.weighty = 1.0;
		jPanel1.add(jScrollPane1, gridBagConstraints2);


		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.weighty = 1.0;
		getContentPane().add(jPanel1, gridBagConstraints1);

	}

	private void checkVersion(java.awt.event.ActionEvent evt) {
		Date myDate = VersionInfo.getBuildDate();
		Date serverDate = VersionInfo.getServerBuildDate();

		if (myDate != null && serverDate != null) {
			if (!myDate.equals(serverDate)) {
				Object[] msgArgs = {
					DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(serverDate)
				};
				JOptionPane.showMessageDialog(this, (new java.text.MessageFormat(getString("msg.newversion.text"))).format(msgArgs), getString("msg.newversion.title"), JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, getString("msg.versionuptodate.text"), getString("msg.versionuptodate.title"), JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, getString("msg.versionunknown.text"), getString("msg.versionunknown.title"), JOptionPane.ERROR_MESSAGE);
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
			defaultTranslations.put("window.title" , "About Magellan");
			
			defaultTranslations.put("btn.checkfornew.caption" , "Check for new version");
			defaultTranslations.put("btn.close.caption" , "Close");
			
			defaultTranslations.put("infotext" , "You can find the official Magellan homepage at http://eressea.upb.de/magellan/. Check out that site if you need a new version, support, or if you want to contribute to Magellan.\n\nCredits:\nRoger Butenuth, Enno Rehling, Stefan G\u00f6tz, Klaas Prause, Sebastian Tusk, Andreas Gampe, Roland Behme, Michael Schmidt, Henning Zahn, Oliver Hertel, Guenter Grossberger, S�ren Bendig, Marc Geerligs, Matthias M�ller, Ulrich K�ster, Jake Hofer, Ilja Pavkovic...\n(drop us a note if somebody is missing here).\n\nLast but not least we want to mention Ferdinand Magellan, daring explorer and first circumnavigator of the globe of a time long ago (http://www.mariner.org/age/magellan.html)\n\nThis product includes software developed by the Apache Software Foundation (http://www.apache.org/).\n\n");
			defaultTranslations.put("versiontext" , "Version of {0}\n");
			
			defaultTranslations.put("msg.newversion.text" , "There is a new version of Magellan since {0} (http://eressea.upb.de/magellan/downloads/magellan.jar)");
			defaultTranslations.put("msg.newversion.title" , "New version");
			defaultTranslations.put("msg.versionuptodate.text" , "This version of Magellan is up to date.");
			defaultTranslations.put("msg.versionuptodate.title" , "Latest version");
			defaultTranslations.put("msg.versionunknown.text" , "Unable to retrieve the version information from the Magellan server.");
			defaultTranslations.put("msg.versionunknown.title" , "Error");
		}
		return defaultTranslations;
	}
	

}
