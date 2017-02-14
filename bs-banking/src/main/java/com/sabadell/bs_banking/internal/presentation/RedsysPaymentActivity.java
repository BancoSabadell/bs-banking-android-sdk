package com.sabadell.bs_banking.internal.presentation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.sabadell.bs_banking.R;

public final class RedsysPaymentActivity extends Activity {
  public static final String URL_BASE_KEY = "url_base_key", AMOUNT_KEY = "amount",
      ADDRESS_KEY = "address",
      CREDIT_CARD_TOKEN_KEY = "credit_card_token_key";

  private static final String URL_OK = "http://www.ok.com",
      URL_KO = "http://www.ko.com";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.connection_activity);

    String urlBase = fieldValidation(URL_BASE_KEY);
    String amount = fieldValidation(AMOUNT_KEY);
    String address = fieldValidation(ADDRESS_KEY);
    String creditCardToken = getIntent().getStringExtra((CREDIT_CARD_TOKEN_KEY));

    initWebView(urlBase, amount, address, creditCardToken);
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
  private void initWebView(String urlBase, String amount, String address,
      String creditCardToken) {
    final WebView webView = (WebView) findViewById(R.id.webview);
    webView.getSettings().setJavaScriptEnabled(true);

    webView.setWebViewClient(new WebViewClient() {
      @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(URL_OK)) {
          finishOk();
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

    String url = urlBase + "cashIn?amount="
        + amount
        + "&account="
        + address;

    if (creditCardToken != null) {
      url += "&creditCardToken=" + creditCardToken;
    }

    webView.loadUrl(url);
  }

  private void finishKo() {
    setResult(Activity.RESULT_CANCELED, new Intent());
    finish();
  }

  private void finishOk() {
    setResult(Activity.RESULT_OK, new Intent());
    finish();
  }
}
