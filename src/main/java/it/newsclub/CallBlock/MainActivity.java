package it.newsclub.CallBlock;

import it.newsclub.CallBlock.R;
import it.newsclub.CallBlock.Trackers;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Log;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.Logger.LogLevel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tjeannin.apprate.AppRate;

public class MainActivity extends Activity {
	
	
	
	private Handler mHandler = new Handler();
	private WebView webView;
	
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
    public static final String SENDER_ID = "269513474202";
    
    static final String TAG = "CallCenterBlocker";
    static final String STRING_LIKE = "Rate now CallCenterBlocker";
    static final String STRING_VOTA = "Rate!";
    static final String STRING_NOT_NOW = "Later";
    static final String STRING_EXIT ="Exit?";
    static final String STRING_SI = "Yes";
    static final String STRING_NO = "No";
    
    static final String STRING_UPDATE_ASK = "E' disponibile un'aggiornamento di " + TAG + ".";
    static final String STRING_UPDATE = "Aggiorna";
    static final String STRING_SHARE_BODY = "Ti consiglio questa app ";
    
    
    
    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    private static Context context;
    String regid;
    
    private Tracker t;
    
    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-69131948-3";
    public static int GENERAL_TRACKER = 0;
    
    private InterstitialAd interstitial;
    private AdView adView;
    
    private ValueCallback<Uri> mUploadMessage;  
    private final static int FILECHOOSER_RESULTCODE=1;
	public static final String MyPREFERENCES = "callblock";  
	static SharedPreferences sharedpreferences;
	
	SharedPreferences LocationPreference;
	

	
    private MyCallReceiver yourBR = null;
    
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1000;
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 5 * 60 * 1000; 
    protected LocationManager locationManager;
    
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainActivity.context = getApplicationContext();
		
		setup();
		
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setAutoCancel(true);
		
		registerReceiver(broadcastBufferReceiver, new IntentFilter(MyCallReceiver.BROADCAST_BUFFER_SEND_CODE));
		
		
	}
    	
	/*
	@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setup();
    }
    */
	
	// set up broadcast receiver
	private BroadcastReceiver broadcastBufferReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent bufferIntent) {
	    	
	    	showNotification();
	    	
	    	Bundle extras = bufferIntent.getExtras();
	    	if( extras != null ) {
	    		String message = extras.getString("num");
		    	//Toast.makeText(context, "CIAO " + message, Toast.LENGTH_LONG).show();

	    		webView.loadUrl("javascript:incomingAdd(\'" + message + "\');");
		    	//displayInterstitial();

	    		
		    	Intent intentone = new Intent(context.getApplicationContext(), MainActivity.class);
		        intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

		    	/*intentone.putExtra("num",sInCall);
		        */
		    	context.startActivity(intentone);
		        
		    		
	    	} 

	    	//Toast.makeText(context, "CIAO 1 ", Toast.LENGTH_LONG).show();

	    		
	    	
	    }
	};
	
    	
	@Override  
	 protected void onActivityResult(int requestCode, int resultCode,  
	                                    Intent intent) {  
	  if(requestCode==FILECHOOSER_RESULTCODE)  
	  {  
	   if (null == mUploadMessage) return;  
	            Uri result = intent == null || resultCode != RESULT_OK ? null  
	                    : intent.getData();  
	            mUploadMessage.onReceiveValue(result);  
	            mUploadMessage = null;  
	  }
	  }  
	
	
	
	
	
    @SuppressWarnings("deprecation")
	void setup() {
		setContentView(R.layout.main);
		
		/*
		AdRequest adRequest = new AdRequest.Builder().build();
		adView = (AdView)this.findViewById(R.id.adView);
		 */
		

    	sharedpreferences = context.getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
		preferenceSave();
        
		
		t = ((Trackers) getApplication()).getTracker(Trackers.TrackerName.GLOBAL_TRACKER);		
		GoogleAnalytics.getInstance(this).setLocalDispatchPeriod(15);
		
		
		
		
		/* prende l'id della registrazione */
		SharedPreferences.Editor editor = sharedpreferences.edit();  
        editor.putString("id", getRegistrationId(context));
        editor.commit();
        
        
		//mDisplay = (TextView) findViewById(R.id.textView1);
		
	           		
	    webView = (WebView) findViewById(R.id.webView1);
	    webView.getSettings().setJavaScriptEnabled(true);
	    webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
	    webView.setFocusable(true);
	    webView.setFocusableInTouchMode(true);
	    webView.getSettings().setUseWideViewPort(true);
	    
	    
	    
	    
	    webView.setWebChromeClient(new WebChromeClient() {
	    	@Override
	    	public void onProgressChanged(WebView view, int newProgress) {
	    		if( newProgress == 100 ) {
		            //hide loading image
		            findViewById(R.id.imageLoading1).setVisibility(View.GONE);
		            
		            
		            
		            //show webview
		            findViewById(R.id.webView1).setVisibility(View.VISIBLE);
		            //findViewById(R.id.adView).setVisibility(View.VISIBLE);
		        
		            /*
		            Bundle extras = getIntent().getExtras();
		    		if( extras != null ) {
		    			String message = extras.getString("num");
		    			Log.e("EXTRA " + message);
		    			Log.i(message);
		    			//Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		    			webView.loadUrl("javascript:incomingAdd(\'" + message + "\');");
		    			
		    		} 
		    		*/

		    		
	    		}
	        }
	    	
	    	
	    	
	    	
	           
	    	
	    	  //The undocumented magic method override  
	           //Eclipse will swear at you if you try to put @Override here  
	        // For Android 3.0+
	        public void openFileChooser(ValueCallback<Uri> uploadMsg) {  

	            mUploadMessage = uploadMsg;  
	            Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
	            i.addCategory(Intent.CATEGORY_OPENABLE);  
	            i.setType("image/*");  
	            MainActivity.this.startActivityForResult(Intent.createChooser(i,"File Chooser"), FILECHOOSER_RESULTCODE);  

	           }
	        
	        
	        

	        // For Android 3.0+
	           public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
	           mUploadMessage = uploadMsg;
	           Intent i = new Intent(Intent.ACTION_GET_CONTENT);
	           i.addCategory(Intent.CATEGORY_OPENABLE);
	           i.setType("*/*");
	           MainActivity.this.startActivityForResult(
	           Intent.createChooser(i, "File Browser"),
	           FILECHOOSER_RESULTCODE);
	           }

	        //For Android 4.1
	           public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
	               mUploadMessage = uploadMsg;  
	               Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
	               i.addCategory(Intent.CATEGORY_OPENABLE);  
	               i.setType("image/*");  
	               MainActivity.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), MainActivity.FILECHOOSER_RESULTCODE );

	           }
	    	
	           
	    	
	    });
	    
	    
	    	
	    
		webView.getSettings().setDomStorageEnabled(true);
		final MyJavaScriptInterface myJavaScriptInterface = new MyJavaScriptInterface(context);
		webView.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");

		webView.getSettings().setSavePassword(false);
		webView.clearFormData();
		
		
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setDatabaseEnabled(true);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
		   webView.getSettings().setDatabasePath("/data/data/" + webView.getContext().getPackageName() + "/databases/");
		}
		
		
		if (android.os.Build.VERSION.SDK_INT < 16) 
			 webView.setBackgroundColor(0x00000000);
		else
			 webView.setBackgroundColor(Color.argb(1, 0, 0, 0));
		
		webView.requestFocus();
		webView.loadUrl("file:///android_asset/www/index.html");
		
		
		
		
		
		if (checkPlayServices()) {
	        gcm = GoogleCloudMessaging.getInstance(this);
	        regid = getRegistrationId(context);
	
	        if (regid.isEmpty()) {
	            registerInBackground();
	        }
	    } else {
	        Log.i(TAG + " No valid Google Play Services APK found.");
	    }
		
		
		
		//adView.loadAd(adRequest);
		
		
    }

	@Override
    protected void onResume() {
        super.onResume();
        // Check device for Play Services APK.
        checkPlayServices();
        
        //registerReceiver(broadcastBufferReceiver, new IntentFilter(MyCallReceiver.BROADCAST_BUFFER_SEND_CODE));

        
        if (adView != null) {
            adView.resume();
        }
    }
	
	@Override
    protected void onPause() {
		
		super.onPause();
        if (adView != null) {
            adView.pause();
        }
        
		//this.unregisterReceiver(broadcastBufferReceiver);		

    }
	
	
	@Override
    public void onDestroy() {
		this.unregisterReceiver(broadcastBufferReceiver);		

        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
	
	
	
	private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG + " This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
	
	
	
	
	
	private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG + " Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
	
	public String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG + " Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG + " App version changed.");
            return "";
        }
        return registrationId;
    }
	
	
	private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    
                    Log.i("avvio...");
                    
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);
    }
	
	
	
	public void showNotification() {
	    PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
	    Resources r = getResources();
	    Notification notification = new NotificationCompat.Builder(this)
	            .setTicker("titolo")
	            .setSmallIcon(R.drawable.ic_launcher)
	            .setContentTitle("Call Center Block")
	            .setContentText("Call Center Block ha bloccato una chiamata ")
	            .setContentIntent(pi)
	            .setAutoCancel(true)
	            .build();

	    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	    notificationManager.notify(0, notification);
	}
	


	
	@Override
	  public void onStart() {
	    super.onStart();
	    GoogleAnalytics.getInstance(this).reportActivityStart(this);
	    
	    
	  }

	  @Override
	  public void onStop() {
		  super.onStop();
		    GoogleAnalytics.getInstance(this).reportActivityStop(this);
		  
		  AlertDialog.Builder builder = new AlertDialog.Builder(this)
			.setTitle(TAG)
			.setMessage(STRING_LIKE)
			.setPositiveButton(STRING_VOTA, null)
			.setNegativeButton(STRING_NO, null)
			.setNeutralButton(STRING_NOT_NOW, null);
			
			
			new AppRate(this)
				.setCustomDialog(builder)
				.setMinDaysUntilPrompt(5)
				.setMinLaunchesUntilPrompt(10)	    
			.init();
			
	  }
  
	  
	  
	  @Override
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
		  try {
			  if( webView != null ) { 
				  if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
					  webView.goBack();
					  return false;
				  }
				  
				  
				  
				  new AlertDialog.Builder(this)
		           
		            .setMessage(STRING_EXIT)
		            .setPositiveButton(STRING_SI, new DialogInterface.OnClickListener()
		            {
		                @Override
		                public void onClick(DialogInterface dialog, int which) {
		                    finish();    
		                }

		            })
		            .setNegativeButton(STRING_NO, null)
		            .show();  
			  } else {
				  Log.w("back 3...");
			  }
			  return super.onKeyDown(keyCode, event);
		  } catch (Exception e ){
			  Log.w("back 4...");
			  return false;
		  }
		  
    }	  

	  
	  
	  
	  
	  public void preferenceSave() {
		  
	  		Editor editor = sharedpreferences.edit();
	        editor.putString( "blacklist", "A,B,C" );
	        editor.commit(); 

	  	}
	  
	  
	  public static void preferenceSet(String key, String val) {
		  
	  		Editor editor = sharedpreferences.edit();
	        editor.putString( key, val );
	        editor.commit(); 

	  	}
	  
	  public  void callJavaScript(String methodName, Object...params){
	        StringBuilder stringBuilder = new StringBuilder();
	        stringBuilder.append("javascript:try{");
	        stringBuilder.append(methodName);
	        stringBuilder.append("(");
	        String separator = "";
	        for (Object param : params) {               
	            stringBuilder.append(separator);
	            separator = ",";
	            if(param instanceof String){
	                stringBuilder.append("'");
	            }
	                stringBuilder.append(param);
	            if(param instanceof String){
	                stringBuilder.append("'");
	            }

	        }
	        stringBuilder.append(")}catch(error){console.error(error.message);}");
	        final String call = stringBuilder.toString();
	        //Log.i(TAG, "callJavaScript: call="+call);


	        webView.loadUrl(call);
	    }

	  
	  
	  
	  /*
	  @Override
	  public boolean dispatchKeyEvent (KeyEvent event) {
	      if (event.getAction()==KeyEvent.ACTION_DOWN && event.getKeyCode()==KeyEvent.KEYCODE_BACK) {
	          //Toast.makeText(this, "Back button pressed", Toast.LENGTH_LONG).show();
	          if(webView.canGoBack() == true){
                  webView.goBack();
	          }
	          return true;
	      }
	      return false;
	  }
	  */
	  
	  
	  
	  final class MyJavaScriptInterface {
		  Context mContext;

		     MyJavaScriptInterface(Context c) {
		         mContext = c;
		     }
		     
		     @JavascriptInterface		    
		     public void showToast(String toast){
		         Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
		     }
		     	
		     
		     
		     
		     
		     
		     
		     
		     @JavascriptInterface
		     public void openAndroidDialog( String sTitle, String sText){
		      AlertDialog.Builder myDialog = new AlertDialog.Builder(MainActivity.this);
		      myDialog.setTitle(sTitle);
		      myDialog.setMessage(sText);
		      myDialog.setPositiveButton("OK", null);
		      myDialog.show();
		    
		    }
		     
		     
		     
		     
		     
		     
		     @JavascriptInterface
		     public void updateDialog(Integer nForce) {
		    	 new AlertDialog.Builder(MainActivity.this)
		 		.setTitle(TAG)
		 		.setMessage(STRING_UPDATE_ASK)
		 		.setPositiveButton(STRING_UPDATE, new DialogInterface.OnClickListener() {
		 			@Override
		            public void onClick(DialogInterface dialog, int which) {
		 				Intent intent = new Intent(Intent.ACTION_VIEW);
			    		intent.setData(Uri.parse("market://details?id=" + getApplicationContext().getPackageName() ));
			    		startActivity(intent);
			    		finish();
		            }
		        })
		 		.setNeutralButton(STRING_NOT_NOW, null)
		 		.show();
		     }
	     
		     
		     
		     
		     
		     @JavascriptInterface		    
		     public void goWeb(String s){
		         newWeb( s );
		     }

		  
		     @JavascriptInterface		    
		     public void smsSend(){
		    	 /*
		    	 Uri smsUri = Uri.parse("sms:");
		    	 Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
		    	 intent.putExtra("sms_body", "Da juventino... ti consiglio questa app http://market.android.com/details?id=it.newsclub.appJuve");
		    	 startActivity(intent);
		    	 */
		    	 
		    	 
		    	 Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
			     sharingIntent.setType("text/plain");
			     
			     String sText = STRING_SHARE_BODY + "\nhttp://market.android.com/details?id=" + getApplicationContext().getPackageName(); 
			     
			     sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
			     sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sText);
			     startActivity(Intent.createChooser(sharingIntent, "Share on:"));
		     }
		     
		     
		     
		     		     
		     @JavascriptInterface		    
		     public void shareContent( String sTitle, String sBody, String sUrl){
			     Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
			     sharingIntent.setType("text/plain");
			     
			     String sText = "\n" + sTitle + "\n" + sBody + "\n" + sUrl; 
			     
			     sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, sTitle);
			     sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sText);
			     startActivity(Intent.createChooser(sharingIntent, "Condividi su:"));
		     }
		     
		     
		     
		     
		     
		     @JavascriptInterface		    
		     public void marketGo(){
		    	 try { 
		    		  Intent intent = new Intent(Intent.ACTION_VIEW);
		    		  intent.setData(Uri.parse("market://details?id=it.newsclub.callcenterblock"));
		    		  startActivity(intent);
		    		} catch (Exception e) { //google play app is not installed
		    		  Intent intent = new Intent(Intent.ACTION_VIEW);
		    		  intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=it.newsclub.callcenterblock"));
		    		  startActivity(intent);
		    		}
		     }
		     
		     
		     @JavascriptInterface		    
		     public void quit(){
		         finish();
		     }		     

		     
		     
		     
		     @TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@JavascriptInterface
		     public void accSet(int n){
		    	 /*
		    	 runOnUiThread(new Runnable() {  
	                	@Override
	                	public void run() {
	   			    	 getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, 
	 			    	          WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);	       		    	 }	    	                    		
	                });
		    	 */

		    	 //		    	 setAcc(n);
		    	 
		    	 /*
		    	 if( n == 1 )
		    	 else {
	                runOnUiThread(new Runnable() {  
	                	@Override
	                	public void run() {
	       	    		    if (Build.VERSION.SDK_INT >= 11){
	       	    		        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	       	    		    }
	       		    	 }	    	                    		
	                });
		    	 }
		    	 */
	                
		     }
		     
		     
		     
		     @JavascriptInterface
		     public void interStart() {
		    	 runOnUiThread (new Thread(new Runnable() { 
		             public void run() {
				     	displayInterstitial();
		             }
		         }));
		     	
		     }
		     
		     
		     
		     @JavascriptInterface
		     public void pageShow() {
		    	 runOnUiThread (new Thread(new Runnable() { 
		             public void run() {
		            	 pageShowGo();
		             }
		         }));
		    	 
		     }
		     
		     
		     
		     
		     @JavascriptInterface
		     public int appVersionGet() {
		    	 return getAppVersion(context);
		     }
		     
		     
		     
		     
		     @JavascriptInterface		    
		     public String sharedGet( String sKey ) {
		    	 if( sharedpreferences.getString(sKey, null) == null )
		    		 return "";
		    	 else
		    		 return sharedpreferences.getString(sKey, "");
		     }	
		     
		     
		     
		     
		     
		     @JavascriptInterface		    
		     public void sharedPut( String sKey, String value ) {
		    	
		        SharedPreferences.Editor editor = sharedpreferences.edit();
		  
		        editor.putString(sKey, value);
		        editor.commit();
		     }
		     
		     @JavascriptInterface
		     public String regidGet() {
		    	 return regid;
		     }
		     
		     @JavascriptInterface
		     public String lastCallGet( int ncount ) {
		    	 return _lastCallGet( ncount );
		     }  
		     
		
		 }
	  
	  
	  
	  
	  	private void newWeb(String s) {	
		  Intent i = new Intent(MainActivity.this, BrowserActivity.class);
		  i.putExtra("url",s);
		  startActivity(i);
	  }
	  	
	  	
	  	
	  	
	  	
	  	private void pageShowGo() {
	  	}
	  	

	  	
	  	
	  	

		public SharedPreferences getGCMPreferences(Context context) {
		    
		    return getSharedPreferences(MainActivity.class.getSimpleName(),
		            Context.MODE_PRIVATE);
		}
		
		private SharedPreferences getGcmPreferences(Context context) {
	        // This sample app persists the registration ID in shared preferences, but
	        // how you store the regID in your app is up to you.
	        return getSharedPreferences(MainActivity.class.getSimpleName(),
	                Context.MODE_PRIVATE);
	    }
	    		
		
		private static int getAppVersion(Context context) {
		    try {
		        PackageInfo packageInfo = context.getPackageManager()
		                .getPackageInfo(context.getPackageName(), 0);
		        return packageInfo.versionCode;
		    } catch (NameNotFoundException e) {
		        // should never happen
		        throw new RuntimeException("Could not get package name: " + e);
		    }
		}
				
		
		
		private void sendRegistrationIdToBackend() {
		      // Your implementation here.

				String url = "http://www.callcenterblock.com/app/json.php";
				List<NameValuePair> params = new ArrayList<NameValuePair>();
	            params.add(new BasicNameValuePair("regid", regid));
	            params.add(new BasicNameValuePair("cmd", "install"));
	            params.add(new BasicNameValuePair("app", getApplicationContext().getPackageName()));

	            Context context = getApplicationContext(); // or activity.getApplicationContext()
	            PackageManager packageManager = context.getPackageManager();
	            String packageName = context.getPackageName();

	            String versionName = "not available"; // initialize String

	            
	            try {
	            	versionName = packageManager.getPackageInfo(packageName, 0).versionName;	                
	            } catch (PackageManager.NameNotFoundException e) {
	                e.printStackTrace();
	            }
	            
	            params.add(new BasicNameValuePair("version", versionName));
	            
	           DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpPost httpPost = new HttpPost(url);
	            try {
					httpPost.setEntity(new UrlEncodedFormEntity(params));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

	            try {
					HttpResponse httpResponse = httpClient.execute(httpPost);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}     
			
			
			
		    }		

	   
		public void displayInterstitial() {
			  
			 interstitial = new InterstitialAd(this);
		        interstitial.setAdUnitId("ca-app-pub-5993593385066306/6085041476");
		        interstitial.setAdListener(new AdListener() {
		        	
		        	
		            @Override
		            public void onAdLoaded() {
		                super.onAdLoaded();
		                interstitial.show();
		            }

		            @Override
		            public void onAdFailedToLoad(int errorCode) {
		                super.onAdFailedToLoad(errorCode);
		            }
		        });

		        
			    // Create ad request.
			    AdRequest adRequest2 = new AdRequest.Builder().build();
			    //.addTestDevice("4B15187138BAE8430F4E5E8F8562B21D").build();
			    

			    // Begin loading your interstitial.
			    interstitial.loadAd(adRequest2);
		        
		  }
	
		public static int dpToPx(int dp)
		{
		    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
		}

		public static int pxToDp(int px)
		{
		    return (int) (px / Resources.getSystem().getDisplayMetrics().density);
		}
		  
	  
		 private class MyLocationListener implements LocationListener {

		        public void onLocationChanged(Location location) {
		        	
		        	/*
		            String message = String.format(
		                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
		                    location.getLongitude(), location.getLatitude()
		                    
		            );
		            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
		        	*/
		        	
			        preferenceSet( "geo", String.format("%1$s|%2$s", location.getLongitude(), location.getLatitude() ) );

		            /*
		            Editor editor = sharedpreferences.edit();
			        editor.putString( "geo", String.format("%1|%2", location.getLongitude(), location.getLatitude() ) );
			        editor.commit(); 
*/
		            
		        }

		        public void onStatusChanged(String s, int i, Bundle b) {
		        	/*
		            Toast.makeText(MainActivity.this, "Provider status changed",
		                    Toast.LENGTH_LONG).show();
		        	*/
		        }

		        public void onProviderDisabled(String s) {
		        	/*
		            Toast.makeText(MainActivity.this,
		                    "Provider disabled by the user. GPS turned off",
		                    Toast.LENGTH_LONG).show();
		            */
		        }

		        public void onProviderEnabled(String s) {
		        	/*
		            Toast.makeText(MainActivity.this,
		                    "Provider enabled by the user. GPS turned on",
		                    Toast.LENGTH_LONG).show();
		        	*/
		        }

		    }
		 
		 
		 
		 public String _lastCallGet( int ncount ) {
		        
		        String[] projection = new String[]{CallLog.Calls.NUMBER,
	                    CallLog.Calls.TYPE,
	                    CallLog.Calls.DURATION,
	                    CallLog.Calls.DATE,
	                    CallLog.Calls.CACHED_NAME };
		        
		        
		        
		        Cursor cur = getContentResolver().query( CallLog.Calls.CONTENT_URI,projection, null,null, android.provider.CallLog.Calls.DATE + " DESC");

		        int number = cur.getColumnIndex( CallLog.Calls.NUMBER );
		        int duration = cur.getColumnIndex( CallLog.Calls.DURATION);
		        int type = cur.getColumnIndex(CallLog.Calls.TYPE);
		        int date = cur.getColumnIndex(CallLog.Calls.DATE);
		        int name = cur.getColumnIndex(CallLog.Calls.CACHED_NAME);
		        
		        
		        JSONArray jsAr = new JSONArray();
		        
		        cur.moveToFirst();
		        
		        
		        
		        for( int n=0; n<cur.getCount(); n++) {
		        	
		        	String phNumber = cur.getString( number );
		            String callDuration = cur.getString( duration );
		            int callType = cur.getInt(type);
		            String callDate = cur.getString(date);
		            String callName = cur.getString(name);
		            
	    			//Toast.makeText(context, "ok " + ncount, Toast.LENGTH_LONG).show();

		            //Log.i("nc: " + ncount);
		            //if( callType == CallLog.Calls.INCOMING_TYPE || callType == CallLog.Calls.MISSED_TYPE ) {
			        if( callType != CallLog.Calls.OUTGOING_TYPE ) {
		            	try {
		    		        JSONObject obj = new JSONObject();

		    		        
		            		obj.put("num", phNumber);
		            		obj.put("date", callDate);
		            		obj.put("type", callType);
		            		obj.put("name", callName);
		            		jsAr.put(obj);
			    			//Toast.makeText(context, obj.toString(), Toast.LENGTH_LONG).show();

		            	} catch (JSONException e) {
		            	    e.printStackTrace();
		            	}
			            	
		            
		            	
		            	if( --ncount == 0 )
		            		break;
		            }
		            
		            cur.moveToNext();
		            
		        }
		    cur.close();
		   
		    
		    return jsAr.toString();
		    
		    
		    

		}
		
}





