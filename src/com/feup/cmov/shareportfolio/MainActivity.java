package com.feup.cmov.shareportfolio;

import java.util.ArrayList;

import database.SimpleQuoteDB;
import database.StockDataSource;

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

public class MainActivity extends Activity implements OnClickListener{
	
	private Button mySharesButton, searchButton, shareEvolutionButton;
	private Intent newIntent;
	private Bundle bundle;
	private String username = "teste3";
	private ArrayList<SimpleQuoteDB> quotes;
	private StockDataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//loading view objects
		mySharesButton = (Button) findViewById(R.id.my_shares_button);
		searchButton = (Button) findViewById(R.id.search_button);
		shareEvolutionButton = (Button) findViewById(R.id.shares_evolution);
		
		//myQuotes = new ArrayList<SimpleQuoteDB>();
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
			ArrayList<SimpleQuoteDB> quotes;
			public LoadCurrentValues(ArrayList<SimpleQuoteDB> quotes){
				this.quotes = quotes;
			}
			@Override
			public void run() {
				String[] companyNames = new String[quotes.size()];
				for(int i = 0; i < quotes.size(); i++){
					companyNames[i] = quotes.get(i).getCompanyName();
				}
				final ArrayList<Quote> quoteValues = RestApi.getCurrentValue(companyNames);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(!quoteValues.isEmpty()){
							Toast.makeText(getApplicationContext(), "Share current values loaded successfully", Toast.LENGTH_LONG).show();
							for(int i = 0; i < quotes.size(); i++){
								SimpleQuoteDB sq = new SimpleQuoteDB(quotes.get(i).getCompanyName(),quotes.get(i).getShareNumber(), quoteValues.get(i).getCloseValue());
								datasource.updateShare(sq, username);
							}
							newIntent.putExtras(bundle);
							startActivity(newIntent);	
							
						}else{
							Toast.makeText(getApplicationContext(), "Unable to load current values", Toast.LENGTH_LONG).show();
							startActivity(newIntent);
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
			datasource = new StockDataSource(this);
		    datasource.open();
			quotes = datasource.getAllShares(username);
			System.out.println("*******"+quotes.size());
			new Thread(new LoadCurrentValues(quotes)).start();		
		}
		else if(v.getId() == searchButton.getId()){
			
		}
	}

}
