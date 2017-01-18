package com.sabadell.bs_token.internal.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * A simple caching layer to store the user data using shared preferences.
 */
public class Persistence {
  private final SharedPreferences preferences;
  private final String KEY_DS_EXPIRY_DATE = "key_ds_expiry_date";
  private final String KEY_DS_MERCHANT_IDENTIFIER = "key_ds_merchant_identifier";

  public Persistence(Context context) {
    this.preferences =
        context.getSharedPreferences(BsTokenRepository.class.getName(), Context.MODE_PRIVATE);
  }

  void saveTokenCreditCardData(String expiryDate, String merchantIdentifier) {
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString(KEY_DS_EXPIRY_DATE, expiryDate);
    editor.putString(KEY_DS_MERCHANT_IDENTIFIER, merchantIdentifier);
    editor.apply();
  }

  @Nullable String getExpiryDate() {
    return preferences.getString(KEY_DS_EXPIRY_DATE, null);
  }

  @Nullable String getMerchantIdentifier() {
    return preferences.getString(KEY_DS_MERCHANT_IDENTIFIER, null);
  }

  void clearCache() {
    SharedPreferences.Editor editor = preferences.edit();
    editor.remove(KEY_DS_EXPIRY_DATE);
    editor.remove(KEY_DS_MERCHANT_IDENTIFIER);
    editor.apply();
  }
}
