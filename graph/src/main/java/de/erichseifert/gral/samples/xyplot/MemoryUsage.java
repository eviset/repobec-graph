/*
 * GRAL: GRAphing Library for Java(R)
 *
 * (C) Copyright 2009-2015 Erich Seifert <dev[at]erichseifert.de>,
 * Michael Seifert <mseifert[at]error-reports.org>
 *
 * This file is part of GRAL.
 *
 * GRAL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GRAL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GRAL.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.erichseifert.gral.samples.xyplot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;

import javax.swing.*;

import de.erichseifert.gral.data.*;
import de.erichseifert.gral.data.statistics.Statistics;
import de.erichseifert.gral.samples.ExamplePanel;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.graphics.Orientation;
import de.erichseifert.gral.plots.Plot;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.XYPlot.XYPlotArea2D;
import de.erichseifert.gral.plots.areas.AreaRenderer;
import de.erichseifert.gral.plots.areas.DefaultAreaRenderer2D;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.GraphicsUtils;

final class UpdateTask implements ActionListener {
	private final DataTable data;
	private final Plot plot;
	private final JComponent component;
	private Method getTotalPhysicalMemorySize;
	private Method getFreePhysicalMemorySize;


	public UpdateTask(DataTable data, XYPlot plot, JComponent comp) {
		this.data = data;
		this.plot = plot;
		this.component = comp;

		// Check for VM specific methods getTotalPhysicalMemorySize() and
		// getFreePhysicalMemorySize()
		OperatingSystemMXBean osBean =
			ManagementFactory.getOperatingSystemMXBean();
		try {
			getTotalPhysicalMemorySize = osBean.getClass()
				.getMethod("getTotalPhysicalMemorySize");
			getTotalPhysicalMemorySize.setAccessible(true);
			getFreePhysicalMemorySize = osBean.getClass()
				.getMethod("getFreePhysicalMemorySize");
			getFreePhysicalMemorySize.setAccessible(true);
		} catch (SecurityException ex) {
		} catch (NoSuchMethodException ex) {
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (!component.isVisible()) {
			return;
		}
		double time = System.currentTimeMillis();

		// Physical system memory
		long memSysTotal = 0L;
		long memSysFree = 0L;
		long memSysUsed = 0L;

		// We can only display system memory if there are the corresponding
		// methods
		if ((getTotalPhysicalMemorySize != null) &&
				(getFreePhysicalMemorySize != null)) {
			OperatingSystemMXBean osBean =
				ManagementFactory.getOperatingSystemMXBean();
			try {
				memSysTotal = (Long) getTotalPhysicalMemorySize.invoke(osBean);
				memSysFree = (Long) getFreePhysicalMemorySize.invoke(osBean);
				memSysUsed = memSysTotal - memSysFree;
			} catch (IllegalArgumentException ex) {
			} catch (IllegalAccessException ex) {
			} catch (InvocationTargetException ex) {
			}
		}

		// JVM memory
		long memVmTotal = Runtime.getRuntime().totalMemory();
		long memVmFree = Runtime.getRuntime().freeMemory();
		long memVmUsed = memVmTotal - memVmFree;

		data.add(time,  memSysUsed/1024L/1024L);
		data.remove(0);

		Column col1 = data.getColumn(0);
		plot.getAxis(XYPlot.AXIS_X).setRange(
			col1.getStatistics(Statistics.MIN),
			col1.getStatistics(Statistics.MAX)
		);

		Column col3 = data.getColumn(1);
		plot.getAxis(XYPlot.AXIS_Y).setRange(
			0, Math.max(
				memSysUsed/1024L/1024L,
				col3.getStatistics(Statistics.MAX)
			)
		);

		component.repaint();
	}
}

public class MemoryUsage extends ExamplePanel {
	/** Version id for serialization. */
	private static final long serialVersionUID = 5914124874301980251L;

	/** Size of the data buffer in no. of element. */
	private static final int BUFFER_SIZE = 1000;
	/** Update interval in milliseconds */
	private static final int INTERVAL = 100;

	private JButton runStopButton;
	private final Timer updateTimer;
	private  Boolean runStopState;


	@SuppressWarnings("unchecked")
	public MemoryUsage() {
		DataTable data = new DataTable(Double.class, Long.class);
		double time = System.currentTimeMillis();
		for (int i=BUFFER_SIZE - 1; i>=0; i--) {
			data.add(time - i*INTERVAL, 0L);
		}

		// Use columns 0 and 1 for physical system memory
		DataSource memSysUsage = new DataSeries("Curlic", data, 0, 1);



		// Create new xy-plot
		XYPlot plot = new XYPlot(memSysUsage);


		// Format  plot
		plot.setInsets(new Insets2D.Double(20.0, 90.0, 40.0, 20.0));
		plot.getTitle().setText("Memory Usage");
		plot.setLegendVisible(true);

		// Format legend
		plot.getLegend().setOrientation(Orientation.HORIZONTAL);

		// Format plot area
		((XYPlotArea2D) plot.getPlotArea()).setMajorGridX(false);
		((XYPlotArea2D) plot.getPlotArea()).setMinorGridY(true);

		// Format axes (set scale and spacings)
		plot.getAxis(XYPlot.AXIS_Y).setRange(0.0, 1.0);
		AxisRenderer axisRendererX = plot.getAxisRenderer(XYPlot.AXIS_X);
		axisRendererX.setTickSpacing(BUFFER_SIZE*INTERVAL/10.0);
		axisRendererX.setTickLabelFormat(DateFormat.getTimeInstance());
		AxisRenderer axisRendererY = plot.getAxisRenderer(XYPlot.AXIS_Y);
		axisRendererY.setMinorTicksCount(4);
		axisRendererY.setTickLabelFormat(new DecimalFormat("0 MiB"));

		Color color1Dark = GraphicsUtils.deriveDarker(COLOR1);


		// Format first data series
		plot.setPointRenderers(memSysUsage, null);
		AreaRenderer area1 = new DefaultAreaRenderer2D();
		area1.setColor(new LinearGradientPaint(
			0f, 0f, 0f, 1f,
			new float[] {0f, 1f},
			new Color[] {
				GraphicsUtils.deriveWithAlpha(COLOR1, 128),
				GraphicsUtils.deriveWithAlpha(COLOR1, 24)
			}
		));
		plot.setAreaRenderers(memSysUsage, area1);


		// Add plot to frame
		InteractivePanel plotPanel = new InteractivePanel(plot);
		//plotPanel.setPannable(false);
		//plotPanel.setZoomable(false);
		add(plotPanel, BorderLayout.CENTER);


		runStopState = true;
		runStopButton= new JButton("Stop");
		runStopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(runStopState) {
					updateTimer.stop();
					runStopButton.setText("Run");
				}else {
					updateTimer.start();
					runStopButton.setText("Stop");
				}
				runStopState = !runStopState;

			}
		});
		add(runStopButton, BorderLayout.SOUTH);

		// Start watching memory
		UpdateTask updateTask = new UpdateTask(data, plot, plotPanel);
		updateTimer = new Timer(INTERVAL, updateTask);
		updateTimer.setCoalesce(false);
		updateTimer.start();
	}

	@Override
	public String getTitle() {
		return "Memory usage";
	}

	@Override
	public String getDescription() {
		return "Area plot of the system's current memory usage. This example " +
			"works best with Oracle VM, but it can show VM memory usage on " +
			"other VMs too.";
	}

	public static void main(String[] args) {
		new MemoryUsage().showInFrame();
	}
}
