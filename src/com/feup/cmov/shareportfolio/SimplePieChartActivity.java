package com.feup.cmov.shareportfolio;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

public class SimplePieChartActivity extends Activity {
	private TextView donutSizeTextView;
	private SeekBar donutSizeSeekBar;
	private PieChart pie;

	private Segment s1;
	private Segment s2;
	private Segment s3;
	private Segment s4;

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
						public void onClick(DialogInterface dialog, int id) {
							//TODO:
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
	

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.pie_chart);

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
		s1 = new Segment("s1", 5);
		s2 = new Segment("s2", 1);
		s3 = new Segment("s3", 12);
		s4 = new Segment("s4", 7);

		EmbossMaskFilter emf = new EmbossMaskFilter(new float[] { 1, 1, 1 },
				0.4f, 10, 8.2f);

		SegmentFormatter sf1 = new SegmentFormatter();
		sf1.configure(getApplicationContext(), R.xml.pie_segment_formatter1);

		sf1.getFillPaint().setMaskFilter(emf);

		SegmentFormatter sf2 = new SegmentFormatter();
		sf2.configure(getApplicationContext(), R.xml.pie_segment_formatter2);

		sf2.getFillPaint().setMaskFilter(emf);

		SegmentFormatter sf3 = new SegmentFormatter();
		sf3.configure(getApplicationContext(), R.xml.pie_segment_formatter3);

		sf3.getFillPaint().setMaskFilter(emf);

		SegmentFormatter sf4 = new SegmentFormatter();
		sf4.configure(getApplicationContext(), R.xml.pie_segment_formatter4);

		sf4.getFillPaint().setMaskFilter(emf);

		pie.addSeries(s1, sf1);
		pie.addSeries(s2, sf2);
		pie.addSeries(s3, sf3);
		pie.addSeries(s4, sf4);

		pie.getBorderPaint().setColor(Color.LTGRAY);
		pie.getBackgroundPaint().setColor(Color.LTGRAY);
	}
	protected void updateDonutText() {
		donutSizeTextView.setText(donutSizeSeekBar.getProgress() + "%");
	}
}
