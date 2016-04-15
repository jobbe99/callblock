package it.newsclub.CallBlock;


import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.android.internal.telephony.ITelephony;
import com.google.analytics.tracking.android.Log;
import com.google.android.gms.location.LocationClient;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.widget.Toast;
 

public class MyCallReceiver extends BroadcastReceiver {
	
	public static final String BROADCAST_BUFFER_SEND_CODE = "it.newsclub.SEND_CODE";
	
    @Override
    public void onReceive(Context context, Intent intent) {
 
    	
    	Boolean bEndCall = false;
    	
    	SharedPreferences sharedpreferences;
    	sharedpreferences = context.getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
   	  
    	String incomingNumber = "";
    	
    	// allo squillo del telefono...
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
        	
        	// recupera l'elenco dei numeri da bloccare
            String sBlock = sharedpreferences.getString("blacklist", "NO");
            
            TelephonyManager telephony = (TelephonyManager) 
            		context.getSystemService(Context.TELEPHONY_SERVICE);    
            try {
				  
            	Class c = Class.forName(telephony.getClass().getName());   
            	Method m = c.getDeclaredMethod("getITelephony");
			  
            	m.setAccessible(true);
            	ITelephony telephonyService = (ITelephony) m.invoke(telephony);
			
            	Bundle b = intent.getExtras();
            	incomingNumber = b.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            	
            	// blocca la chiamata e fa ripartire l'app
				if( sBlock.indexOf(incomingNumber) > 0 ) {
			    	telephonyService.endCall();
			    	
			    	Toast.makeText(context, "Blocked by Callcenter Block!", Toast.LENGTH_SHORT).show();
			    	bEndCall = true;
			    }
				
            }catch (Exception e){  e.printStackTrace(); 
				Log.e( "Errore");
            }

            
            // se non ha interrotto la chiamata esce
            if( !bEndCall )
            	return;
            
            // aggiunge l'ultimo numero alle chiamate
            long time= System.currentTimeMillis();
            
            /*
            String sGeo = sharedpreferences.getString("geo", "1|1");
            String incomingName = "";
            
            String mPhoneNumber = telephony.getLine1Number();  
        	String mOperatorName = telephony.getSimOperator();
        	String mCountry = telephony.getNetworkCountryIso();

        	 WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
 	        //String ip = BigInteger.valueOf(wm.getDhcpInfo().netmask).toString();

 	        byte[] ipAddress = BigInteger.valueOf(wm.getDhcpInfo().ipAddress).toByteArray();
 	        InetAddress myaddr;
 	        String hostaddr = "";
 	        
 	        try {
 				myaddr = InetAddress.getByAddress(ipAddress);
 		        hostaddr = myaddr.getHostAddress(); // numeric representation (such as "127.0.0.1")

 			} catch (UnknownHostException e1) {
 				// TODO Auto-generated catch block
 				e1.printStackTrace();
 			}
 	        
 	        
 	       
 	        String sLocale = context.getResources().getConfiguration().locale.getISO3Country();
        	
        	*/
            
            /*
            String sInCall = incomingNumber + "|" + incomingName + "|" + time + 
	        		"|" + sGeo + "|" + mPhoneNumber + "|" + 
	        		hostaddr + "|" + mOperatorName + "|" + mCountry + "|" + sLocale;
            */
            
        	

            String id = sharedpreferences.getString("id", "NO");
            
            String sInCall = incomingNumber + "|" + "00" + "|" + time + 
	        		"|" + "0|0" + "|" + "00" + "|" + 
	        		"00" + "|" + "00" + "|" + "00" + "|" + "00" + "|" + id;

      	
	        // rilancia l'app
            
            
            
	        Intent bufferIntentSendCode = new Intent(BROADCAST_BUFFER_SEND_CODE);
	    	bufferIntentSendCode.putExtra("num", sInCall);
	    	context.sendBroadcast(bufferIntentSendCode);
	    	
            	      
        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_IDLE)
                || intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                        TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            // This code will execute when the call is disconnected
            //Toast.makeText(context, "Detected call hangup event", Toast.LENGTH_LONG).show();
                 
        }
    }
}
    

