package com.feup.cmov.shareportfolio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import entities.Quote;

public class RestApi {
	private static String urlQuoteVariation = "http://ichart.finance.yahoo.com/table.txt?";
	private static String urlQuoteCurrentValue = "http://finance.yahoo.com/d/quotes?f=sl1d1t1v&s=";

	@SuppressLint("SimpleDateFormat")
	public static void main(String args[]){
		String[] array = {};
		System.out.println(getCurrentValue(array));
		
		
	}
	/**
	 * Function that returns the response form server
	 * 
	 * @param command
	 * @return
	 */
	public static String getResponse(String link) {
		HttpURLConnection con = null;
		String payload = "Error";
		try {
			URL url = new URL(link);
			System.out.println("" + url);
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(10000);
			// con.setConnectTimeout(15000);
			// con.setRequestMethod("GET");
			con.setDoInput(true);
			try {
				con.connect();
			} catch (IOException e) {
				System.err.println("Failed to connect to server");
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "UTF-8"));
			payload = "";
			String aux;
			while ((aux = reader.readLine()) != null) {
				payload += aux + "\n";
			}
			payload = payload.substring(0, payload.length()-1);
			reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				con.disconnect();
		}
		return payload;
	}
	/**
	 * Function that constructs the url for the http request that provides the share evolution for a given period
	 * @param string
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static String constructURLVariation(String tickName, Date prevDate, Date postDate, char periodicity){
		String url = urlQuoteVariation;
		/*
		 a – initial month (0 denotes January, and so on) 
		 b – initial day 
		 c – initial year 
		 d – final month 
		 e – final day 
		 f – final year 
		 g – periodicity (d - daily; w - weekly; m - monthly)
		 */
		url += "a=" + prevDate.getMonth() + "&";
		url += "b=" + prevDate.getDate() + "&";
		url += "c=" + (prevDate.getYear() + 1900) + "&";
		url += "d=" + postDate.getMonth() + "&";
		url += "e=" + (postDate.getDate() - 1) + "&";
		url += "f=" + (postDate.getYear() + 1900) + "&";
		url += "g=" + periodicity + "&s=" + tickName;
		return url;
	}
	/**
	 * Function that retrieves an ArrayList of Quotes with their evolution in a given period
	 * @param tickName Company Tick name
	 * @param prevDate period first date
	 * @param postDate period final date
	 * @param periodicity (Daily, Weekly, Monthly)
	 * @return
	 */
	public static ArrayList<Quote> getQuotesHistory(String tickName, Date prevDate, Date postDate, char periodicity){
		String string = getResponse(constructURLVariation(tickName, prevDate, postDate, periodicity));
		ArrayList<Quote> array = new ArrayList<Quote>();
		Quote quote;
		String[] arrayString = string.split("\n");
		for(int i = 1; i < arrayString.length; i++){
			String[] quoteString = arrayString[i].split(",");
			String date = quoteString[0]; 
			double open = Double.parseDouble(quoteString[1]);
			double High = Double.parseDouble(quoteString[2]);
			double Low = Double.parseDouble(quoteString[3]);
			double Close = Double.parseDouble(quoteString[4]);
			long Volume = Long.parseLong(quoteString[5]);
			double AdjClose = Double.parseDouble(quoteString[6]);
			quote = new Quote(date, open, High, Low, Close, Volume, AdjClose);
			array.add(quote);
		}
		return array;
	}
	/**
	 * 
	 * @param tickNames
	 * @return
	 */
	public static ArrayList<Quote> getCurrentValue(String[] tickNames){
		String url = urlQuoteCurrentValue;
		for(int i = 0; i < tickNames.length; i++){
			url += tickNames[i] + ",";
		}
		url = url.substring(0, url.length()-1);
		
		String response = getResponse(url);
		ArrayList<Quote> array = new ArrayList<Quote>();
		if(tickNames.length == 0) return array;
		String[] arrayString = response.split("\n");
		for(int i = 0; i < arrayString.length; i++){
			String[] quoteString = arrayString[i].split(",");
			Quote quote = new Quote();
			quote.setCompanyName(quoteString[0]);
			quote.setCloseValue(Double.parseDouble(quoteString[1]));
			quote.setDate(quoteString[2]);
			quote.setTime(quoteString[3]);
			try{
				quote.setVolume(Long.parseLong(quoteString[4]));
			}catch(NumberFormatException e){
				
			}
			array.add(quote);
		}
		return array;
	}
}

