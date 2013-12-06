package com.feup.cmov.shareportfolio;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Spinner;
import android.widget.Toast;
import entities.Quote;

public class SelectQuotesEvolution extends Activity implements OnValueChangeListener{
	public static final int ADD_SHARE = 10;
	public static final int REMOVE_SHARE = 20;
	
	private Spinner tickSpinner, periodicitySpinner;
	private Button button;
	private DatePicker datepicker;
	
	private String[] tickNames, periodicityNames;
	private String tickSelected;
	private char[] periodicityChars;
	private char periodicitySelected;
	private Date dateSelected = null;
 	
	private Bundle bundle;
	private Intent newIntent;
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_quotes_evolution, menu);
		return true;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_quotes_evolution);
		
		tickSpinner = (Spinner) findViewById(R.id.tickNameSpinner);
		periodicitySpinner = (Spinner) findViewById(R.id.PeriodicitySpinner);
		datepicker = (DatePicker) findViewById(R.id.datePicker);
		button = (Button) findViewById(R.id.ButtonSeeEvolution);
		
		//Loading the items for the Spinner
		tickNames = new String [] {"IBM", "MSFT", "DELL", "CSCO", "AMZN", "HPQ", "GOOG", "AAPL", "ORCL"};
		periodicityNames = new String [] {"Daily", "Weekly", "Monthly"};
		periodicityChars = new char []{'d', 'w', 'm'};
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, tickNames);
        tickSpinner.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, periodicityNames);
        periodicitySpinner.setAdapter(adapter2);
        

		bundle = new Bundle();
		newIntent = new Intent(this.getApplicationContext(), SimpleXYPlotActivity.class);
        
        //Setting the listeners
        tickSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if(arg0.getId() == tickSpinner.getId()){
					tickSelected = tickNames[arg2];
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
        periodicitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if(arg0.getId() == periodicitySpinner.getId()){
					periodicitySelected = periodicityChars[arg2];
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
        button.setOnClickListener(new OnClickListener() {			
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View v) {
				class LoadShareHistory implements Runnable{
					private Date date;
					public LoadShareHistory(Date date) {
						this.date = date;
					}
					@Override
					public void run() {
						final ArrayList<Quote> array = RestApi.getQuotesHistory(tickSelected, date, new Date(), periodicitySelected);
						Log.i("DEBUG", "" + array);
						runOnUiThread(new Runnable() {							
							@Override
							public void run() {
								if(array != null){
									Toast.makeText(getApplicationContext(), "Share history loaded!!!", Toast.LENGTH_LONG).show();
									//passing the busses to next activity
									bundle.putSerializable("array", array);
									bundle.putSerializable("tickName", tickSelected);
									newIntent.putExtras(bundle);
									startActivity(newIntent);
								}
							}
						});
					}		
				}
				if(v.getId() == button.getId()){
					try {
						int day = datepicker.getDayOfMonth();
						int month = datepicker.getMonth();
						int year = datepicker.getYear();
						String dateString = "" + year + "/" + (month + 1) + "/" + day;
						DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
						dateSelected = df.parse(dateString);
						Log.i("DEBUG", "dateString: " + dateString);
						Log.i("DEBUG", "date: " + dateSelected);
						new Thread(new LoadShareHistory(dateSelected)).start();						
					} catch (ParseException e) {
						e.printStackTrace();
					} 
				}
			}
		});        
	}
	@Override
	public void onValueChange(NumberPicker piker, int oldVal, int newVal) {
		 Log.i("value is","" + newVal);		
	}
}
