package com.feup.cmov.shareportfolio;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import database.SimpleQuoteDB;
import database.StockDataSource;
import entities.Quote;

public class MainActivity extends Activity implements OnClickListener{
	
	private Button signInButton, signUpButton, shareEvolutionButton;
	private EditText usernameEditText, passwordEditText;
	private Intent newIntent;
	private Bundle bundle;
	private String username;
	private ArrayList<SimpleQuoteDB> quotes;
	private StockDataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		datasource = new StockDataSource(this);
	    datasource.open();
		
		//loading view objects
		signInButton = (Button) findViewById(R.id.sign_in);
		signUpButton = (Button) findViewById(R.id.sign_up);
		shareEvolutionButton = (Button) findViewById(R.id.shares_evolution);
		usernameEditText = (EditText) findViewById(R.id.username_txt);
		passwordEditText = (EditText) findViewById(R.id.passord_txt);
		
		newIntent = new Intent(this.getApplicationContext(), PieChartActivity.class);
		bundle = new Bundle();
		
		
		shareEvolutionButton.setOnClickListener(this);	
		signInButton.setOnClickListener(this);
		signUpButton.setOnClickListener(this);
		
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
			ArrayList<SimpleQuoteDB> myquotes;
			public LoadCurrentValues(ArrayList<SimpleQuoteDB> quotes){
				this.myquotes = quotes;
			}
			@Override
			public void run() {
				String[] companyNames = new String[myquotes.size()];
				for(int i = 0; i < myquotes.size(); i++){
					companyNames[i] = myquotes.get(i).getCompanyName();
				final ArrayList<Quote> quoteValues = RestApi.getCurrentValue(companyNames);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(!quoteValues.isEmpty()){
							Toast.makeText(getApplicationContext(), "Share current values loaded successfully", Toast.LENGTH_LONG).show();
							for(int i = 0; i < myquotes.size(); i++){
								SimpleQuoteDB sq = new SimpleQuoteDB(myquotes.get(i).getCompanyName(),myquotes.get(i).getShareNumber(), quoteValues.get(i).getCloseValue());
								datasource.updateShare(sq, username);
							}
							bundle.putSerializable("username", username);
							newIntent.putExtras(bundle);
							startActivity(newIntent);	
							
						}else{
							Toast.makeText(getApplicationContext(), "Unable to load current values/or no shares in the database", Toast.LENGTH_LONG).show();
							bundle.putSerializable("username", username);
							newIntent.putExtras(bundle);
							startActivity(newIntent);	
						}
					}
				});
			}}
		}
		if(v.getId() == shareEvolutionButton.getId()){
			Intent newIntent = new Intent(this.getApplicationContext(), SelectQuotesEvolution.class);
			bundle.putSerializable("username", username);
			newIntent.putExtras(bundle);
			startActivity(newIntent);
		}
		else if(v.getId()== signInButton.getId()){
			username = usernameEditText.getText().toString();
			String password = passwordEditText.getText().toString();
			if(datasource.validateLogin(username, password)){
				quotes = datasource.getAllShares(username);
				if(!quotes.isEmpty()) new Thread(new LoadCurrentValues(quotes)).start();
				else{
					bundle.putSerializable("username", username);
					newIntent.putExtras(bundle);
					startActivity(newIntent);					
				}
			}else
				Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
		}
		else if(v.getId() == signUpButton.getId()){
			username = usernameEditText.getText().toString();
			String password = passwordEditText.getText().toString();
			if(username != null && password != null) {
				datasource.addUser(username, password);
				Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_LONG).show();
			}
		}
	}

}
