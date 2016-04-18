package it.newsclub.CallBlock;

/* http://techlovejump.in/2013/11/android-push-notification-using-google-cloud-messaging-gcm-php-google-play-service-library/
 * techlovejump.in
 * tutorial link
 * 
 *  */


import it.newsclub.CallBlock.R;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class GcmIntentService extends IntentService{
	Context context;
	public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static final String TAG = "CallCenterBlock";
    JSONObject json;
    

	public GcmIntentService() {
		super("GcmIntentService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Bundle extras = intent.getExtras();
		String msg = intent.getStringExtra("message");
		
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		
		 if (!extras.isEmpty()) {
			 
			 if (GoogleCloudMessaging.
	                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
	                sendNotification("Send error: " + extras.toString());
	            } else if (GoogleCloudMessaging.
	                    MESSAGE_TYPE_DELETED.equals(messageType)) {
	                sendNotification("Deleted messages on server: " +
	                        extras.toString());
	            // If it's a regular GCM message, do some work.
	            } else if (GoogleCloudMessaging.
	                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
	                // This loop represents the service doing some work.
	                for (int i=0; i<5; i++) {
	                    Log.i(TAG, "Working... " + (i+1)
	                            + "/5 @ " + SystemClock.elapsedRealtime());
	                    try {
	                        Thread.sleep(500);
	                    } catch (InterruptedException e) {
	                    }
	                }
	                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
	                // Post notification of received message.
	                //sendNotification("Received: " + extras.toString());
	                sendNotification(msg);
	                Log.i(TAG, "Received: " + extras.toString());
	            }
	        }
		 GcmBroadcastReceiver.completeWakefulIntent(intent);
	}
	
	private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        
        /*
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | 
        			    Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
			    notificationIntent, 0);
	*/
        
        
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        
        /*
        Intent myintent = new Intent(this, ReceiveActivity.class);
        myintent.putExtra("message", msg);
        contentIntent = PendingIntent.getActivity(this, 0, myintent, PendingIntent.FLAG_UPDATE_CURRENT);
      */
		
  
        String stext = "";
        String slink = "";
        try {
        	json = new JSONObject(msg);
        	stext = json.getString("text");
        	slink = json.getString("link");
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
       
		
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle(TAG)
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(stext))
        .setContentText(stext);

        
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
	
}