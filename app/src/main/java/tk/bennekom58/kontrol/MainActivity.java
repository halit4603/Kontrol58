package tk.bennekom58.kontrol;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private Boolean noError = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Switch from splash screen to app
        setTheme(R.style.AppTheme);

        setContentView(R.layout.activity_main);

        // Connect to the SwipeRefreshLayout
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Connect the WebView element
        mWebView = (WebView) findViewById(R.id.activity_main_webview);

        // Enable Javascript in WebView
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        // Create onRefreshListener for pulldown gesture
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mWebView.reload();
                    }
                }
        );

        // Create handler for non-blocking delayed action
        final Handler handler=new Handler();
        final Runnable r=new Runnable() {
            public void run() {
                System.exit(0);
            }
        };

        // Stop refresh and splash screen if page finished loading
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView web, String url) {
                // Stop
                if (mySwipeRefreshLayout.isRefreshing()) {
                    mySwipeRefreshLayout.setRefreshing(false);
                }

                // Only show webview when there is no pageload error(s)
                if (noError) {
                    mWebView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                noError = false;
                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_LONG).show();
                handler.postDelayed(r, 3000);
            }
        });

        mWebView.loadUrl("https://kontrol.bennekom58.tk/");
    }
}
