package com.eressea.skillchart;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ToolTipManager;

import com.jrefinery.chart.JFreeChart;
import com.jrefinery.chart.JFreeChartPanel;

/**
 * @author Ulrich K�ster
 * extended for use with tooltips
 */

public class SkillChartJFreeChartPanel extends JFreeChartPanel {

	// the SkillChartPanel object this JFreeChartPanel is inside
	private SkillChartPanel skillChartPanel;

  /**
   * Full constructor: returns a panel containing the specified chart.
   * @param chart The chart to display in the panel;
   */
  public SkillChartJFreeChartPanel(JFreeChart chart, SkillChartPanel skillChartPanel) {
	super(chart);
	this.chart = chart;
	this.skillChartPanel = skillChartPanel;
	this.chart.addChangeListener(this);
	setPreferredSize(new Dimension(480, 320));
	ToolTipManager.sharedInstance().registerComponent(this);
  }

	public String getToolTipText(MouseEvent e) {
		Vector barAreas = ((VerticalBarPlot)chart.getPlot()).getBarAreas();
		if (barAreas.size() == 0) {
			return "";
		} else {
			Point p = e.getPoint();
			int i = barAreas.size() - 1;
			for (Iterator iter = barAreas.iterator(); iter.hasNext(); i--) {
				Rectangle2D rec = (Rectangle2D)iter.next();
				if (rec.contains(p)) {
					return skillChartPanel.getToolTip(i);
				}
			}
			return "";
		}
	}

}