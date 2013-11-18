package com.example.androidplotexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import entities.Quote;

public class RestApi {
	//private static String urlTest = "http://ichart.finance.yahoo.com/table.txt?a=9&b=5&c=2013&d=9&e=19&f=2013&g=d&s=GOOG";
	private static String urlQuoteVariation = "http://ichart.finance.yahoo.com/table.txt?";
	//private static String urlTest = "http://finance.yahoo.com/d/quotes?f=sl1d1t1v&s=DELL";
	//"http://finance.yahoo.com/d/quotes?f=sl1d1t1v&s=DELL";
	//"http://ichart.finance.yahoo.com/table.txt?a=9&b=5&c=2013&d=9&e=19&f=2013&g=d&s=DELL"

	@SuppressLint("SimpleDateFormat")
	public static void main(String args[]){
		//Date date1 = null, date2 = null;
		try {
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			Date date1 = df.parse("2013/10/16");
			Date date2 = new Date();
			System.out.println(date1);
			System.out.println(date2);
			System.out.println(getQuotesHistory("GOOG", date1, date2, 'w'));

		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
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
	 * 
	 * @param string
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static String constructURL(String tickName, Date prevDate, Date postDate, char periodicity){
		String url = urlQuoteVariation;
		/*
		 a � initial month (0 denotes January, and so on) 
		 b � initial day 
		 c � initial year 
		 d � final month 
		 e � final day 
		 f � final year 
		 g � periodicity (d - daily; w - weekly; m - monthly)
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
	public static ArrayList<Quote> getQuotesHistory(String tickName, Date prevDate, Date postDate, char periodicity){
		String string = getResponse(constructURL(tickName, prevDate, postDate, periodicity));
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
}
