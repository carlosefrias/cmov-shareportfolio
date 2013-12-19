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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;
import entities.Quote;

public class SelectQuotesEvolution extends Activity{
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_quotes_evolution);
		
		tickSpinner = (Spinner) findViewById(R.id.tickNameSpinner);
		periodicitySpinner = (Spinner) findViewById(R.id.PeriodicitySpinner);
		datepicker = (DatePicker) findViewById(R.id.datePicker);
		button = (Button) findViewById(R.id.ButtonSeeEvolution);
		
		//Loading the items for the Spinner
		tickNames = PieChartActivity.COMPANY_NAMES;
		periodicityNames = new String [] {"Daily", "Weekly", "Monthly"};
		periodicityChars = new char []{'d', 'w', 'm'};
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, tickNames);
        tickSpinner.setAdapter(adapter);
		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.text_view_auto_complete);
        textView.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, periodicityNames);
        periodicitySpinner.setAdapter(adapter2);
        

		bundle = new Bundle();
		newIntent = new Intent(this.getApplicationContext(), XYPlotActivity.class);
        
        //Setting the listeners
        tickSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if(arg0.getId() == tickSpinner.getId()){
					tickSelected = tickSpinner.getSelectedItem().toString();
					Log.v("DEBUG",""+tickSelected); 
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				try{
					tickSelected = tickSpinner.getSelectedItem().toString();
				}catch(NullPointerException e){
					
				}
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
						Date date_now = new Date();
						tickSelected = tickSpinner.getSelectedItem().toString();
						final ArrayList<Quote> array = RestApi.getQuotesHistory(tickSelected, date, date_now, periodicitySelected);
						if(date_now.getTime() > date.getTime()){
							runOnUiThread(new Runnable() {							
								@Override
								public void run() {
									if(array != null){
										Toast.makeText(getApplicationContext(), "Share history loaded!!!", Toast.LENGTH_LONG).show();
										bundle.putSerializable("array", array);
										bundle.putSerializable("tickName", tickSelected);
										bundle.putChar("type", periodicitySelected);
										bundle.putSerializable("date", date);
										newIntent.putExtras(bundle);
										startActivity(newIntent);
									}
								}
							});
						}else
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(getApplicationContext(), "Select a previous date!", Toast.LENGTH_LONG).show();
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
						new Thread(new LoadShareHistory(dateSelected)).start();						
					} catch (ParseException e) {
						e.printStackTrace();
					} 
				}
			}
		});        
	}
}
