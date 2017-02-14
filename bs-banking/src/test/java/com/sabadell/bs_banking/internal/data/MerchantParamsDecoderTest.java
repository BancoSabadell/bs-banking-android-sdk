package com.sabadell.bs_banking.internal.data;

import com.sabadell.bs_banking.BuildConfig;
import com.sabadell.bs_banking.internal.data.MerchantParamsDecoder.MerchantParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = Config.NONE, sdk = 23)
public final class MerchantParamsDecoderTest {
  private static final String merchantParamsUrl =
      "http://www.okokokokhjbjhvd.com/?Ds_SignatureVersion=HMAC_SHA256_V1&Ds_MerchantParameters=eyJEc19EYXRlIjoiMTUlMkYxMiUyRjIwMTYiLCJEc19Ib3VyIjoiMTElM0E1NCIsIkRzX1NlY3VyZVBheW1lbnQiOiIxIiwiRHNfQW1vdW50IjoiMCIsIkRzX0N1cnJlbmN5IjoiOTc4IiwiRHNfT3JkZXIiOiIxNDgxNzk5MjYyOTMiLCJEc19NZXJjaGFudENvZGUiOiIzMjc3OTI0ODciLCJEc19UZXJtaW5hbCI6IjAwMSIsIkRzX1Jlc3BvbnNlIjoiMDAwMCIsIkRzX1RyYW5zYWN0aW9uVHlwZSI6IjAiLCJEc19NZXJjaGFudERhdGEiOiIiLCJEc19BdXRob3Jpc2F0aW9uQ29kZSI6IjAwMDAwMCIsIkRzX0V4cGlyeURhdGUiOiIyMDEyIiwiRHNfTWVyY2hhbnRfSWRlbnRpZmllciI6ImNhZTFjMzAyYzZmZTMyZTcxMzQ0NTA2ODRhMGE4MWZjODIzOWRjNmIiLCJEc19Db25zdW1lckxhbmd1YWdlIjoiMSIsIkRzX0NhcmRfQ291bnRyeSI6IjcyNCJ9",
      invalidMerchantParamsUrl =
          "http://www.okokokokhjbjhvd.com/?Ds_SignatureVersion=HMAC_SHA256_V1&Ds_MerchantParameters=73733";

  @Test public void Verify_Decode_Success() {
    MerchantParamsDecoder merchantParamsDecoder = new MerchantParamsDecoder(merchantParamsUrl);
    MerchantParams merchantParams = merchantParamsDecoder.decode();
    assertThat(merchantParams.getDsExpiryDate(), is("2012"));
    assertThat(merchantParams.getDsMerchantIdentifier(),
        is("cae1c302c6fe32e7134450684a0a81fc8239dc6b"));
  }

  @Test(expected = RuntimeException.class)
  public void Verify_Decode_Failure() {
    MerchantParamsDecoder merchantParamsDecoder =
        new MerchantParamsDecoder(invalidMerchantParamsUrl);
    merchantParamsDecoder.decode();
  }
}
