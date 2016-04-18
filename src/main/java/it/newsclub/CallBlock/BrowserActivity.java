package it.newsclub.CallBlock;



import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;



public class BrowserActivity extends Activity {

	private WebView webView;
	private Button closeButton; 
	private String action;
	private ProgressBar progressBar;
	
	
	private AdView adViewBrowser;
    private AdRequest adRequestBrowser;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);
		
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		

        
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView) findViewById(R.id.webView2);
        progressBar.setVisibility(View.VISIBLE);
        
        AdView adViewBrowser = (AdView)this.findViewById(R.id.adViewBrowser);
		adRequestBrowser = new AdRequest.Builder().build();
		adViewBrowser.loadAd(adRequestBrowser);

		        
        
        webView.setWebChromeClient(new WebChromeClient() {
	    	@Override
	    	public void onProgressChanged(WebView view, int newProgress) {
	    		
	    		/*
	    		Toast.makeText(getApplicationContext(), Integer.toString(newProgress),
	    				   Toast.LENGTH_LONG).show();
	    		*/
	    		if( newProgress == 100 )
	    			progressBar.setVisibility(View.GONE);
	    		
	        }
	    });
        
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });        
        
        if (Build.VERSION.SDK_INT < 8) {
            webView.getSettings().setPluginState(PluginState.ON);
        } else {
            webView.getSettings().setPluginState(PluginState.ON);
        }
        
        
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        //webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);        
                
        //Bundle extras = getIntent().getExtras(); 
        //String name = extras.getString("url");
        action = getIntent().getStringExtra("url");

/*        if( action != null ) {
		    if (Build.VERSION.SDK_INT >= 11)
		        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	    }
*/
        
        webView.loadUrl(action);   
        
        
        //Log.v(action);
        
		closeButton = (Button)findViewById(R.id.button1);
        closeButton.setOnClickListener( new OnClickListener() {
        	@Override
            public void onClick(View v) {
        		//Log.v("chiudi");
        		webView.loadUrl("about:blank");
        		finish();
            }
        });
        
        webView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus())
                        {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });
        
        
	
	}
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browser, menu);
		return true;
	}
	
		

}
