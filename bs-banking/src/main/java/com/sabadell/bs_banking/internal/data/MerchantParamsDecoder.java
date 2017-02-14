package com.sabadell.bs_banking.internal.data;

import android.net.Uri;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.UnsupportedEncodingException;

/**
 * Decode Redsys merchant params
 */
public final class MerchantParamsDecoder {
  private final String merchantParamsUrl;
  private final Gson gson;

  public MerchantParamsDecoder(String merchantParamsUrl) {
    this.merchantParamsUrl = merchantParamsUrl;
    this.gson = new Gson();
  }

  public MerchantParams decode() {
    Uri uri = Uri.parse(merchantParamsUrl);
    String base64 = uri.getQueryParameter("Ds_MerchantParameters");

    byte[] data = Base64.decode(base64, Base64.DEFAULT);
    try {
      String json = new String(data, "UTF-8");
      return gson.fromJson(json, MerchantParams.class);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public final static class MerchantParams {
    @SerializedName("Ds_ExpiryDate")
    private final String dsExpiryDate;

    @SerializedName("Ds_Merchant_Identifier")
    private final String dsMerchantIdentifier;

    public MerchantParams(String dsExpiryDate, String dsMerchantIdentifier) {
      this.dsExpiryDate = dsExpiryDate;
      this.dsMerchantIdentifier = dsMerchantIdentifier;
    }

    public String getDsExpiryDate() {
      return dsExpiryDate;
    }

    public String getDsMerchantIdentifier() {
      return dsMerchantIdentifier;
    }
  }
}
