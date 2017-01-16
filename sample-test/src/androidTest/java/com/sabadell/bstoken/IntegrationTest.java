package com.sabadell.bstoken;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import io.victoralbertos.device_animation_test_rule.DeviceAnimationTestRule;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static com.sabadell.bstoken.Raw.balanceAddress;
import static com.sabadell.bstoken.Raw.cashInKo;
import static com.sabadell.bstoken.Raw.cashInOk;
import static com.sabadell.bstoken.Raw.cashOut;
import static com.sabadell.bstoken.Raw.checkClearCache;
import static com.sabadell.bstoken.Raw.checkPaymentTokenized;
import static com.sabadell.bstoken.Raw.clearCache;
import static com.sabadell.bstoken.Raw.paymentInvalidAddress;
import static com.sabadell.bstoken.Raw.paymentInvalidAmount;
import static com.sabadell.bstoken.Raw.tokenizeCreditCard;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class IntegrationTest {
  private final static String amount = "5032";

  @ClassRule static public DeviceAnimationTestRule
      deviceAnimationTestRule = new DeviceAnimationTestRule();

  @Rule public ActivityTestRule<BSTokenActivity> mActivityRule =
      new ActivityTestRule<>(BSTokenActivity.class);

  @Test public void _a01_Verify_Cash_In_Invalid_Amount() {
    paymentInvalidAmount();
  }

  @Test public void _a02_Verify_Cash_In_Invalid_Address() {
    paymentInvalidAddress(amount);
  }

  @Test public void _a03_Verify_Cash_In_Ok() {
    cashInOk(amount);
  }

  @Test public void _a04_Verify_Cash_In_Ko() {
    cashInKo(amount);
  }

  @Test public void _a05_Verify_Balance_Address() {
    balanceAddress(amount);
  }

  @Test public void _a07_Verify_Tokenize_Credit_Card() {
    tokenizeCreditCard();
  }

  @Test public void _a08_Verify_Use_Tokenize_Credit_Card() {
    checkPaymentTokenized();
  }

  @Test public void _a09_Verify_Clear_Cache() {
    clearCache();
    checkClearCache();
  }

  @Test public void _b01_Verify_CashOut() {
    cashOut(amount);
  }

  @Test public void _b02_Verify_Balance_Address() {
    balanceAddress("0");
  }
}
