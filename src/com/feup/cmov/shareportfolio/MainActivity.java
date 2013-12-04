package com.feup.cmov.shareportfolio;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import entities.Quote;
import entities.SimpleQuoteDB;

public class MainActivity extends Activity implements OnClickListener{
	
	private Button mySharesButton, searchButton, shareEvolutionButton;
	private Intent newIntent;
	private Bundle bundle;
	private ArrayList<SimpleQuoteDB> myQuotes;
	//FOR NOW
	int[] array = {12, 8, 4, 15, 5};
	String[] ticks = {"GOOG", "IBM", "DELL", "AMZN", "AAPL"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//loading view objects
		mySharesButton = (Button) findViewById(R.id.my_shares_button);
		searchButton = (Button) findViewById(R.id.search_button);
		shareEvolutionButton = (Button) findViewById(R.id.shares_evolution);
		
		myQuotes = new ArrayList<SimpleQuoteDB>();
		newIntent = new Intent(this.getApplicationContext(), SimplePieChartActivity.class);
		bundle = new Bundle();
		
		
		shareEvolutionButton.setOnClickListener(this);	
		mySharesButton.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		class LoadCurrentValues implements Runnable{
			String[] companyNames;
			public LoadCurrentValues(String[] cnames){
				this.companyNames = cnames;
			}
			@Override
			public void run() {
				final ArrayList<Quote> quoteValues = RestApi.getCurrentValue(companyNames);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(!quoteValues.isEmpty()){
							Toast.makeText(getApplicationContext(), "Share current values loaded successfully", Toast.LENGTH_LONG).show();
							for(int i = 0; i < array.length; i++){
								SimpleQuoteDB quote = new SimpleQuoteDB(ticks[i], array[i], quoteValues.get(i).getCloseValue());
								myQuotes.add(quote);
							}
							bundle.putSerializable("quotes", myQuotes);
							newIntent.putExtras(bundle);
							startActivity(newIntent);	
							
						}else{
							Toast.makeText(getApplicationContext(), "Unable to load current values", Toast.LENGTH_LONG).show();
						}
					}
				});
			}
		}
		if(v.getId() == shareEvolutionButton.getId()){
			Intent newIntent = new Intent(this.getApplicationContext(), SelectQuotesEvolution.class);
			startActivity(newIntent);
		}
		else if(v.getId()== mySharesButton.getId()){
			ArrayList<SimpleQuoteDB> quotes = new ArrayList<SimpleQuoteDB>();
			//TODO: Load quotes from database
			//Loading current value from webservice
			new Thread(new LoadCurrentValues(ticks)).start();		
		}
		else if(v.getId() == searchButton.getId()){
			
		}
	}

}
