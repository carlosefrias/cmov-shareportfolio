package com.feup.cmov.shareportfolio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;

import entities.Quote;
import entities.SimpleQuoteDB;

public class SimplePieChartActivity extends Activity {
	private TextView donutSizeTextView;
	private SeekBar donutSizeSeekBar;
	private PieChart pie;
	private Bundle bundle;
	private Intent newIntent;

	private ArrayList<Segment> sectors;
	private ArrayList<SimpleQuoteDB> quotes;
	//TODO: Put here all company names...
	private final static String[] companyNames = new String [] {"FOXA","ATVI","ADBE","AKAM","ALXN","ALTR","AMZN","MGN","ADI","AAPL",
		"AMAT","ADSK","ADP","AVGO","BIDU","BBBY","BIIB","BRCM","CHRW","CA","CTRX","CELG","CERN","CHTR","CHKP","CSCO","CTXS","CTSH",
		"CMCSA","COST","DELL","XRAY","DTV","DISCA","DLTR","EBAY","EQIX","EXPE","EXPD","ESRX","FFIV","FB","FAST","FISV","FOSL","GRMN",
		"GILD","GOOG","GMCR","HSIC","INTC","INTU","ISRG","KLAC","KRFT","LBTYA","LINTA","LMCA","LLTC","MAT","MXIM","MCHP","MU","MSFT",
		"MDLZ","MNST","MYL","NTAP","NFLX","NUAN","NVDA","ORLY","PCAR","PAYX","PCLN","QCOM","GOLD","REGN","ROST","SNDK","SBAC","STX",
		"SHLD","SIAL","SIRI","SPLS","SBUX","SRCL","SYMC","TXN","TSLA","VRSK","VRTX","VIAB","VOD","WDC","WFM","WYNN","XLNX","YHOO"};
	
	private ArrayAdapter<CharSequence> adapter, adapter2;

	private static final int ADD_SHARE = 10;
	private static final int REMOVE_SHARE = 20;
	
	private int numSharesSelected;
	private String companyNameSelected;
	private String[] myCompanyShares;
		
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
			final NumberPicker np = (NumberPicker) layoutView.findViewById(R.id.number_picker);
			final Spinner spinner = (Spinner) layoutView.findViewById(R.id.spinner_companys_ticks);
			spinner.setAdapter(adapter);
			AutoCompleteTextView textView = (AutoCompleteTextView) layoutView.findViewById(R.id.share_edit_text);
	        textView.setAdapter(adapter);
			np.setMinValue(1);
			np.setMaxValue(100);
			ntBuilder.setView(layoutView);
			ntBuilder.setPositiveButton(R.string.positive_add_button_label,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id){
							class AddShare implements Runnable{
								private String companyName;
								private int shareNumber;
								public AddShare(String cn, int sn){
									this.companyName = cn;
									this.shareNumber = sn;
								}
								@Override
								public void run() {
									final ArrayList<Quote> quote = RestApi.getCurrentValue(new String[] {companyName});
									//TODO: add the shares to the database
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											if(!quote.isEmpty()){
												Quote q = quote.get(0);
												SimpleQuoteDB sq = new SimpleQuoteDB(companyName, shareNumber, q.getCloseValue());
												quotes = addNewQuote(quotes, sq);
												//TODO:ADD TO DATABASE
												Toast.makeText(getApplicationContext(), "" 
														+ shareNumber
														+ " shares of the "
														+ companyName
														+ " company added successfully!", Toast.LENGTH_LONG)
												.show();
												bundle.putSerializable("quotes", quotes);
												newIntent.putExtras(bundle);
												startActivity(newIntent);
											}
										}
										private ArrayList<SimpleQuoteDB> addNewQuote(ArrayList<SimpleQuoteDB> quotes, SimpleQuoteDB sq) {
											boolean exists = false;
											for(int i = 0; i < quotes.size(); i++){
												if(quotes.get(i).getCompanyName().equals(sq.getCompanyName())){
													exists = true;
													int numberShares = sq.getShareNumber() + quotes.get(i).getShareNumber();
													quotes.get(i).setShareNumber(numberShares);
													break;
												}
											}
											if(!exists) quotes.add(sq);
											return quotes;
										}
									});
								}
							}
							companyNameSelected = (String) spinner.getSelectedItem();
							numSharesSelected = np.getValue();
							new Thread(new AddShare(companyNameSelected, numSharesSelected)).start();
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
			final NumberPicker np_rem = (NumberPicker) layoutView2.findViewById(R.id.numberPicker_remove_share);
			final Spinner spinner_rem = (Spinner) layoutView2.findViewById(R.id.spinner_rem_companys_ticks);
			spinner_rem.setAdapter(adapter2);
			AutoCompleteTextView textView2 = (AutoCompleteTextView) layoutView2.findViewById(R.id.autoCompleteTextView1);
	        textView2.setAdapter(adapter2);
	        spinner_rem.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			        companyNameSelected = (String) spinner_rem.getSelectedItem();
			        for(int i = 0; i < quotes.size(); i++){
			        	if(quotes.get(i).getCompanyName().equals(companyNameSelected)){
			        		np_rem.setMaxValue(quotes.get(i).getShareNumber());
			        		np_rem.setMinValue(1);
			        	}
			        }					
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
			ntBuilder.setView(layoutView2);
			ntBuilder.setPositiveButton(R.string.positive_remove_button_label,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							numSharesSelected = np_rem.getValue();
							System.out.println(numSharesSelected);
							ArrayList<SimpleQuoteDB> quotes_aux = new ArrayList<SimpleQuoteDB>();
							for(int i = 0; i < quotes.size(); i++){
								if(quotes.get(i).getCompanyName().equals(companyNameSelected)){
									//TODO: Update the database...
									int numberShares = quotes.get(i).getShareNumber() - numSharesSelected;
									double currentvalue = quotes.get(i).getCurrentValue();
									if(numberShares > 0) quotes_aux.add(new SimpleQuoteDB(companyNameSelected, numberShares, currentvalue));
								}
								else
									quotes_aux.add(quotes.get(i));
							}
							Toast.makeText(getApplicationContext(), "" 
									+ numSharesSelected
									+ " shares of the "
									+ companyNameSelected
									+ " company removed successfully!", Toast.LENGTH_LONG)
							.show();
							bundle.putSerializable("quotes", quotes_aux);
							newIntent.putExtras(bundle);
							startActivity(newIntent);
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
		
		// loading extras from the previous activity
		bundle = this.getIntent().getExtras();
		newIntent = new Intent(this.getApplicationContext(), SimplePieChartActivity.class);
		//Loading the items for the Spinner
		adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, companyNames);
		
		// initialize our XYPlot reference:
		pie = (PieChart) findViewById(R.id.mySimplePieChart);
		donutSizeSeekBar = (SeekBar) findViewById(R.id.donutSizeSeekBar);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onStart(){
		super.onStart();
		quotes = (ArrayList<SimpleQuoteDB>) bundle.getSerializable("quotes");
		
		myCompanyShares = new String[quotes.size()];
		//Get the company Names
		for(int i = 0; i < quotes.size(); i++)
			myCompanyShares[i] = quotes.get(i).getCompanyName();
		adapter2 = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, myCompanyShares);
		
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
		sectors = new ArrayList<Segment>();
		EmbossMaskFilter emf = new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 10, 8.2f);
		DecimalFormat df = new DecimalFormat("#.##");
		Segment s;
		for(int i = 0; i < quotes.size(); i++){
			String name = quotes.get(i).getCompanyName();
			int number = quotes.get(i).getShareNumber();
			double valuePerShare = quotes.get(i).getCurrentValue();
			double value = valuePerShare * number;
			sectors.add(new Segment(number + ":" + name + ":" + df.format(value) + " $", number));
			s = sectors.get(i);
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
