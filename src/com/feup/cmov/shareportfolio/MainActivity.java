package com.feup.cmov.shareportfolio;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import entities.SimpleQuoteDB;

public class MainActivity extends Activity implements OnClickListener{
	
	private Button mySharesButton, searchButton, shareEvolutionButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mySharesButton = (Button) findViewById(R.id.my_shares_button);
		searchButton = (Button) findViewById(R.id.search_button);
		shareEvolutionButton = (Button) findViewById(R.id.shares_evolution);
		
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
		if(v.getId() == shareEvolutionButton.getId()){
			Intent newIntent = new Intent(this.getApplicationContext(), SelectQuotesEvolution.class);
			startActivity(newIntent);
		}
		else if(v.getId()== mySharesButton.getId()){
			Intent newIntent = new Intent(this.getApplicationContext(), SimplePieChartActivity.class);
			Bundle bundle = new Bundle();
			ArrayList<SimpleQuoteDB> quotes = new ArrayList<SimpleQuoteDB>();
			//TODO: Load quotes from database
			//FOR NOW
			int[] array = {12, 8, 4, 15, 5};
			String[] ticks = {"GOOG", "IBM", "DELL", "AMZN", "AAPL"};
			double[] values = {12.0, 15.3, 7.5, 4.8, 17.3};
			for(int i = 0; i < array.length; i++){
				SimpleQuoteDB quote = new SimpleQuoteDB(ticks[i], array[i], values[i]);
				quotes.add(quote);
			}
			System.out.println(quotes);
			bundle.putSerializable("quotes", quotes);
			newIntent.putExtras(bundle);
			startActivity(newIntent);			
		}
		else if(v.getId() == searchButton.getId()){
			
		}
	}

}
