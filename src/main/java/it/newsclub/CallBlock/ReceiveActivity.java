package it.newsclub.CallBlock;
/* http://techlovejump.in/2013/11/android-push-notification-using-google-cloud-messaging-gcm-php-google-play-service-library/
 * techlovejump.in
 * tutorial link
 * 
 *  */


import it.newsclub.CallBlock.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiveActivity extends Activity {
	
	  TextView name;
	    TextView deal;
	    TextView valid;
	    TextView address;
	    JSONObject json;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		/*
		Intent notificationIntent = getIntent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
			    notificationIntent, 0);
		*/
		
		Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | 
        			    Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
			    notificationIntent, 0);
		

		
		
		Intent intent = getIntent();
		String message = intent.getExtras().getString("message");
		String stext = "";
		String slink = "";
		try {
			json = new JSONObject(message);
			stext = json.getString("text");
			slink = json.getString("link");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		Toast.makeText(this, stext, Toast.LENGTH_LONG).show();
		
		
	
		
		if( slink != "" ) {		
			Toast.makeText(this, slink, Toast.LENGTH_LONG).show();
			Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(slink));
			intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  		  	startActivity(intent2);
		}
	
		
		setContentView(R.layout.main);
		//Intent intent = getIntent();
		
		/*
		setContentView(R.layout.activity_receive);
		Intent intent = getIntent();
		
		name = (TextView) findViewById(R.id.name);
		deal = (TextView) findViewById(R.id.deal);
		valid = (TextView) findViewById(R.id.valid);
		address = (TextView)findViewById(R.id.address);
		String message = intent.getExtras().getString("message");
		try {
			json = new JSONObject(message);
			String stime = json.getString("name");
			name.setText(stime);
			
			String slecturename = json.getString("deal");
			deal.setText(slecturename);
			
			String sroom = json.getString("valid");
			valid.setText(sroom);
			
			String sfaculty = json.getString("address");
			address.setText(sfaculty);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.receive, menu);
		return true;
	}

}
