package com.feup.cmov.shareportfolio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import entities.Quote;

public class XYPlotActivity extends Activity {

	private XYPlot plot;
	private ArrayList<Quote> array;
	private String tickName;
	private Date date;
	private char periodicitySelected;
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//loading extras(busList) from previous activity
        Bundle bundle = this.getIntent().getExtras();
        array = (ArrayList<Quote>) bundle.getSerializable("array");   
        tickName = (String) bundle.getSerializable("tickName");     
        date = (Date) bundle.getSerializable("date");   
        periodicitySelected = bundle.getChar("type");
        
		//Snippet that prevents users from taking screenshots on ICS+ devices 
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
		plot.setTitle(tickName + " share evolution since " + (1900 + date.getYear()) + ":" + date.getMonth() + ":" + date.getDate());
		String periodicity = "";
		switch (periodicitySelected){
			case 'd':
				periodicity = "days";
				break;
			case 'w':
				periodicity = "weeks";
				break;
			default:
				periodicity = "months";
				break;				
		}
		plot.setDomainLabel("Time ( " + periodicity + " )");
		plot.setRangeLabel("Value per share ( $ )");
		plot.setDomainStepValue(1);
		XYSeries series = new SimpleXYSeries(Arrays.asList(seriesNumbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, tickName + " value");
		LineAndPointFormatter seriesFormat = new LineAndPointFormatter();
		seriesFormat.setPointLabelFormatter(new PointLabelFormatter());
		seriesFormat.configure(getApplicationContext(), R.xml.line_point_formatter_with_plf1);
		plot.addSeries(series, seriesFormat);
		
		// reduce the number of range labels
		plot.setTicksPerRangeLabel(3);
		plot.getGraphWidget().setDomainLabelOrientation(-45);

	}
}