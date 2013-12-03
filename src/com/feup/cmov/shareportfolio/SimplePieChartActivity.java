package com.feup.cmov.shareportfolio;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;

import entities.SimpleQuoteDB;

public class SimplePieChartActivity extends Activity {
	private TextView donutSizeTextView;
	private SeekBar donutSizeSeekBar;
	private PieChart pie;
	private Bundle bundle;

	private ArrayList<Segment> sectors;
	private ArrayList<SimpleQuoteDB> quotes;

	private static final int ADD_SHARE = 10;
	private static final int REMOVE_SHARE = 20;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.simple_pie_chart, menu);
		return true;
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_share_dialog:
			showDialog(ADD_SHARE);
			return true;
		case R.id.remove_share_dialog:
			showDialog(REMOVE_SHARE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id){
		Builder ntBuilder = new AlertDialog.Builder(this);
		LayoutInflater ntInflater = getLayoutInflater();
		switch (id) {
		case ADD_SHARE:
			ntBuilder = new AlertDialog.Builder(this);
			ntBuilder.setTitle(R.string.title_add_share);
			final View layoutView = ntInflater.inflate(R.layout.add_share_dialog, null);
			NumberPicker np = (NumberPicker) layoutView.findViewById(R.id.number_picker);
			np.setMinValue(0);
			np.setMaxValue(100);
			ntBuilder.setView(layoutView);
			ntBuilder.setPositiveButton(R.string.positive_add_button_label,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id){
							//TODO:
							class AddShare implements Runnable{
								private String companyName;
								private int shareNumber;
								public AddShare(String cn, int sn){
									this.companyName = cn;
									this.shareNumber = sn;
								}
								@Override
								public void run() {
									//TODO: get the current value of this shares
									//TODO: add the shares to the database
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
										}
									});
								}
								
							}
						}
					});
			ntBuilder.setNegativeButton(R.string.negative_button_label,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dismissDialog(ADD_SHARE);
						}
					});
			return ntBuilder.create();
		case REMOVE_SHARE:
			ntBuilder = new AlertDialog.Builder(this);
			ntBuilder.setTitle(R.string.title_remove_share);
			final View layoutView2 = ntInflater.inflate(R.layout.remove_share_dialog, null);
			ntBuilder.setView(layoutView2);
			ntBuilder.setPositiveButton(R.string.positive_remove_button_label,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							//TODO:
						}
					});
			ntBuilder.setNegativeButton(R.string.negative_button_label,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dismissDialog(REMOVE_SHARE);
						}
					});
			return ntBuilder.create();
			
		}
		return super.onCreateDialog(id);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.pie_chart);
		
		// loading extras from the previous activity
		bundle = this.getIntent().getExtras();		
		quotes = (ArrayList<SimpleQuoteDB>) bundle.getSerializable("quotes");

		// initialize our XYPlot reference:
		pie = (PieChart) findViewById(R.id.mySimplePieChart);
		donutSizeSeekBar = (SeekBar) findViewById(R.id.donutSizeSeekBar);

		donutSizeSeekBar
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar, int i,
							boolean b) {
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						pie.getRenderer(PieRenderer.class).setDonutSize(
								seekBar.getProgress() / 100f,
								PieRenderer.DonutMode.PERCENT);
						pie.redraw();
						updateDonutText();
					}
				});
		donutSizeTextView = (TextView) findViewById(R.id.donutSizeTextView);
		updateDonutText();
		//pie.getRenderer(PieRenderer.class).setDonutSize(0.0f, PieRenderer.DonutMode.PERCENT);
		//pie.
		//pie.redraw();
		sectors = new ArrayList<Segment>();
		EmbossMaskFilter emf = new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 10, 8.2f);
		for(int i = 0; i < quotes.size(); i++){
			String name = quotes.get(i).getCompanyName();
			int number = quotes.get(i).getShareNumber();
			double value = quotes.get(i).getCurrentValue();
			sectors.add(new Segment(name + "\t" + (number * value) + " $", number));
			Segment s = sectors.get(i);
			SegmentFormatter sf = new SegmentFormatter();
			sf.configure(getApplicationContext(), R.xml.pie_segment_formatter1);
			sf.getFillPaint().setMaskFilter(emf);
			Random r = new Random();
			sf.getFillPaint().setColor(r.nextInt());//Selecting a random color
			pie.addSeries(s, sf);
		}
		pie.getBorderPaint().setColor(Color.LTGRAY);
		pie.getBackgroundPaint().setColor(Color.LTGRAY);
	}
	protected void updateDonutText() {
		donutSizeTextView.setText(donutSizeSeekBar.getProgress() + "%");
	}
}
