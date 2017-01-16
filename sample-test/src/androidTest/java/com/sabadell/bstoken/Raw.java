package com.sabadell.bstoken;

import android.support.test.espresso.web.webdriver.DriverAtoms;
import android.support.test.espresso.web.webdriver.Locator;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.webClick;

final class Raw {
  private static final String PASSWORD = "dummy_password",
      ADDRESS = "0xe26124c8a5c4c747a794eb6abbc9aeb7a5ef2fd7",
      BANK_ACCOUNT = "91210004184502000513";

  static void paymentInvalidAmount() {
    onView(withId(R.id.et_address)).perform(replaceText(ADDRESS), closeSoftKeyboard());
    onView(withId(R.id.tv_output)).check(matches(withText("Invalid amount")));
  }

  static void paymentInvalidAddress(String amount) {
    onView(withId(R.id.et_amount)).perform(replaceText(amount), closeSoftKeyboard());
    onView(withId(R.id.tv_output)).check(matches(withText("Invalid address")));
  }

  static void cashInOk(String amount) {
    onView(withId(R.id.et_address)).perform(replaceText(ADDRESS), closeSoftKeyboard());
    onView(withId(R.id.et_amount)).perform(replaceText(amount), closeSoftKeyboard());
    onView(withId(R.id.bt_cash_in)).perform(click());

    onWebView()
        .withElement(findElement(Locator.ID, "inputCard"))
        .perform(DriverAtoms.webKeys("4548812049400004"))
        .withElement(findElement(Locator.ID, "cad1"))
        .perform(DriverAtoms.webKeys("12"))
        .withElement(findElement(Locator.ID, "cad2"))
        .perform(DriverAtoms.webKeys("20"))
        .withElement(findElement(Locator.ID, "codseg"))
        .perform(DriverAtoms.webKeys("123"))
        .withElement(findElement(Locator.ID, "divImgAceptar"))
        .perform(webClick())
        .withElement(findElement(Locator.NAME, "pin"))
        .perform(DriverAtoms.webKeys("123456"))
        .withElement(findElement(Locator.XPATH, "//img[@alt='Aceptar']"))
        .perform(webClick())
        .withElement(findElement(Locator.XPATH, "//input[@alt='Continuar']"))
        .perform(webClick());

    waitTime();
    onView(withId(R.id.tv_output)).check(matches(withText(R.string.success_cash_out)));
    waitTime();
  }

  static void cashInKo(String amount) {
    onView(withId(R.id.et_address)).perform(replaceText(ADDRESS), closeSoftKeyboard());
    onView(withId(R.id.et_amount)).perform(replaceText(amount), closeSoftKeyboard());
    onView(withId(R.id.bt_cash_in)).perform(click());

    onWebView()
        .withElement(findElement(Locator.ID, "inputCard"))
        .perform(DriverAtoms.webKeys("1111111111111117"))
        .withElement(findElement(Locator.ID, "cad1"))
        .perform(DriverAtoms.webKeys("12"))
        .withElement(findElement(Locator.ID, "cad2"))
        .perform(DriverAtoms.webKeys("20"))
        .withElement(findElement(Locator.ID, "divImgAceptar"))
        .perform(webClick())
        .withElement(findElement(Locator.XPATH, "//input[@alt='Cancelar']"))
        .perform(webClick());

    waitTime();
    onView(withId(R.id.tv_output)).check(matches(withText(R.string.failure)));
  }

  static void balanceAddress(String expectedAmount) {
    onView(withId(R.id.et_address)).perform(replaceText(ADDRESS), closeSoftKeyboard());
    onView(withId(R.id.bt_balance_of)).perform(click());
    onView(withId(R.id.tv_output)).check(matches(withText(expectedAmount)));
  }

  static void cashOut(String amount) {
    onView(withId(R.id.et_amount)).perform(replaceText(amount), closeSoftKeyboard());
    onView(withId(R.id.et_address)).perform(replaceText(ADDRESS), closeSoftKeyboard());
    onView(withId(R.id.et_password)).perform(replaceText(PASSWORD), closeSoftKeyboard());
    onView(withId(R.id.et_bank_account)).perform(replaceText(BANK_ACCOUNT), closeSoftKeyboard());
    onView(withId(R.id.bt_cash_out)).perform(click());
    onView(withId(R.id.tv_output)).check(matches(withText(R.string.success_cash_out)));
  }

  static void tokenizeCreditCard() {
    onView(withId(R.id.bt_tokenize)).perform(click());

    onWebView()
        .withElement(findElement(Locator.ID, "inputCard"))
        .perform(DriverAtoms.webKeys("4548812049400004"))
        .withElement(findElement(Locator.ID, "cad1"))
        .perform(DriverAtoms.webKeys("12"))
        .withElement(findElement(Locator.ID, "cad2"))
        .perform(DriverAtoms.webKeys("20"))
        .withElement(findElement(Locator.ID, "codseg"))
        .perform(DriverAtoms.webKeys("123"))
        .withElement(findElement(Locator.ID, "divImgAceptar"))
        .perform(webClick())
        .withElement(findElement(Locator.NAME, "pin"))
        .perform(DriverAtoms.webKeys("123456"))
        .withElement(findElement(Locator.XPATH, "//img[@alt='Aceptar']"))
        .perform(webClick())
        .withElement(findElement(Locator.XPATH, "//input[@alt='Continuar']"))
        .perform(webClick());

    waitTime();
    onView(withId(R.id.tv_output)).check(matches(withText(R.string.success_tokenize)));
  }

  static void checkPaymentTokenized() {
    onView(withId(R.id.et_address)).perform(replaceText(ADDRESS), closeSoftKeyboard());
    onView(withId(R.id.et_amount)).perform(replaceText("123"), closeSoftKeyboard());
    onView(withId(R.id.bt_cash_in)).perform(click());

    onWebView()
        .withElement(findElement(Locator.XPATH, "//input[@alt='Continuar']"));
  }

  static void clearCache() {
    onView(withId(R.id.bt_clear_cache)).perform(click());
  }

  static void checkClearCache() {
    onView(withId(R.id.et_address)).perform(replaceText(ADDRESS), closeSoftKeyboard());
    onView(withId(R.id.et_amount)).perform(replaceText(""), closeSoftKeyboard());
    onView(withId(R.id.bt_cash_in)).perform(click());

    onWebView()
        .withElement(findElement(Locator.ID, "inputCard"))
        .perform(DriverAtoms.webKeys("1111111111111117"));
  }

  private static void waitTime() {
    waitN(2500);
  }

  static void waitN(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
