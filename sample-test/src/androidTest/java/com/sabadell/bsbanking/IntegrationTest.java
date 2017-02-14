package com.sabadell.bsbanking;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import io.victoralbertos.device_animation_test_rule.DeviceAnimationTestRule;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static com.sabadell.bsbanking.Raw.balanceAddress;
import static com.sabadell.bsbanking.Raw.cashInKo;
import static com.sabadell.bsbanking.Raw.cashInOk;
import static com.sabadell.bsbanking.Raw.cashOut;
import static com.sabadell.bsbanking.Raw.checkClearCache;
import static com.sabadell.bsbanking.Raw.checkPaymentTokenized;
import static com.sabadell.bsbanking.Raw.clearCache;
import static com.sabadell.bsbanking.Raw.paymentInvalidAddress;
import static com.sabadell.bsbanking.Raw.paymentInvalidAmount;
import static com.sabadell.bsbanking.Raw.tokenizeCreditCard;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class IntegrationTest {
  private final static String amount = "5032";

  @ClassRule static public DeviceAnimationTestRule
      deviceAnimationTestRule = new DeviceAnimationTestRule();

  @Rule public ActivityTestRule<BSBankingActivity> mActivityRule =
      new ActivityTestRule<>(BSBankingActivity.class);

  @Test public void _a00_Precondition_Cash_Out() {
    cashOut();
    clearCache();
  }

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
