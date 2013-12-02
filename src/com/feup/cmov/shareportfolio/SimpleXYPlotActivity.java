package com.feup.cmov.shareportfolio;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import entities.Quote;

/**
 * A straightforward example of using AndroidPlot to plot some data.
 */
public class SimpleXYPlotActivity extends Activity {

	private XYPlot plot;
	private ArrayList<Quote> array;
	private String tickName;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//loading extras(busList) from previous activity
        Bundle bundle = this.getIntent().getExtras();
        array = (ArrayList<Quote>) bundle.getSerializable("array");        
        tickName = (String) bundle.getSerializable("tickName");
        
		// fun little snippet that prevents users from taking screenshots
		// on ICS+ devices :-)
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
				WindowManager.LayoutParams.FLAG_SECURE);
		
		setContentView(R.layout.simple_xy_plot_example);

		// initialize our XYPlot reference:
		plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
		Quote quote;
		Number[] seriesNumbers = new Number[array.size()];
		for(int i = 0; i < array.size(); i++){
			quote = array.get(i);
			seriesNumbers[i] = quote.getCloseValue();
		}
		XYSeries series = new SimpleXYSeries(Arrays.asList(seriesNumbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, tickName + " lower");
		LineAndPointFormatter seriesFormat = new LineAndPointFormatter();
		seriesFormat.setPointLabelFormatter(new PointLabelFormatter());
		seriesFormat.configure(getApplicationContext(), R.xml.line_point_formatter_with_plf1);
		//LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.RED, Color.GREEN, null, null);
		plot.addSeries(series, seriesFormat);
		
		// reduce the number of range labels
		plot.setTicksPerRangeLabel(3);
		plot.getGraphWidget().setDomainLabelOrientation(-45);

	}
}