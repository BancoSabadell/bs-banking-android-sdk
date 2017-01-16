package com.sabadell.bs_token.internal.presentation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.sabadell.bs_token.R;

public final class RedsysTokenizeCreditCardActivity extends Activity {
  public static final String URL_BASE_KEY = "url_base_key", MERCHANT_PARAMS_URL_KEY =
      "merchant_params_url_key";
  private static final String URL_OK = "http://www.ok.com",
      URL_KO = "http://www.ko.com";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.connection_activity);

    String urlBase = fieldValidation(URL_BASE_KEY);
    initWebView(urlBase);
  }

  private String fieldValidation(String field) {
    String fieldValue = getIntent().getStringExtra(field);

    if (fieldValue == null || fieldValue.isEmpty()) {
      throw new IllegalArgumentException(
          "A value for " + field + " is mandatory. Submit a value using "
              + field
              + " key with your intent bundle");
    }

    return fieldValue;
  }

  @SuppressLint("SetJavaScriptEnabled")
  private void initWebView(String urlBase) {
    final WebView webView = (WebView) findViewById(R.id.webview);
    webView.getSettings().setJavaScriptEnabled(true);

    webView.setWebViewClient(new WebViewClient() {
      @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(URL_OK)) {
          finishOk(url);
          return true;
        }

        if (url.startsWith(URL_KO)) {
          finishKo();
          return true;
        }

        return super.shouldOverrideUrlLoading(view, url);
      }

      @Override public void onPageFinished(WebView view, String url) {
      }
    });

    String url = urlBase + "tokenizeCreditCard";
    webView.loadUrl(url);
  }

  private void finishKo() {
    setResult(Activity.RESULT_CANCELED, new Intent());
    finish();
  }

  private void finishOk(String urlMerchantParams) {
    Intent intent = new Intent();
    intent.putExtra(MERCHANT_PARAMS_URL_KEY, urlMerchantParams);
    setResult(Activity.RESULT_OK, intent);
    finish();
  }
}
