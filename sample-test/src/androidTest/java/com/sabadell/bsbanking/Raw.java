package com.sabadell.bsbanking;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.web.webdriver.DriverAtoms;
import android.support.test.espresso.web.webdriver.Locator;
import android.view.View;
import android.widget.TextView;
import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.webClick;

final class Raw {
  private static final String PASSWORD = "dummy_password",
      ADDRESS = "0x25e940685e0999d4aa7bd629d739c6a04e625761",
      BANK_ACCOUNT = "91210004184502000513";

  static void paymentInvalidAmount() {
    onView(withId(R.id.et_amount)).perform(replaceText("0"), closeSoftKeyboard());
    onView(withId(R.id.et_address)).perform(replaceText(ADDRESS), closeSoftKeyboard());
    onView(withId(R.id.bt_cash_in)).perform(scrollTo(), click());
    onView(withId(R.id.tv_output)).check(matches(withText("Invalid amount")));
  }

  static void paymentInvalidAddress(String amount) {
    onView(withId(R.id.et_amount)).perform(replaceText(amount), closeSoftKeyboard());
    onView(withId(R.id.bt_cash_in)).perform(scrollTo(), click());
    onView(withId(R.id.tv_output)).check(matches(withText("Invalid address")));
  }

  static void cashInOk(String amount) {
    onView(withId(R.id.et_address)).perform(replaceText(ADDRESS), closeSoftKeyboard());
    onView(withId(R.id.et_amount)).perform(replaceText(amount), closeSoftKeyboard());
    onView(withId(R.id.bt_cash_in)).perform(scrollTo(), click());

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
    onView(withId(R.id.tv_output)).check(matches(withText(R.string.success_cash_in)));
    waitTime();
  }

  static void cashInKo(String amount) {
    onView(withId(R.id.et_address)).perform(replaceText(ADDRESS), closeSoftKeyboard());
    onView(withId(R.id.et_amount)).perform(replaceText(amount), closeSoftKeyboard());
    onView(withId(R.id.bt_cash_in)).perform(scrollTo(), click());

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
    onView(withId(R.id.bt_balance_of)).perform(scrollTo(), click());
    onView(withId(R.id.tv_output)).check(matches(withText(expectedAmount)));
  }

  static void cashOut() {
    onView(withId(R.id.et_address)).perform(replaceText(ADDRESS), closeSoftKeyboard());
    onView(withId(R.id.bt_balance_of)).perform(scrollTo(), click());

    String amount = getText(withId(R.id.tv_output));
    if (amount.equals("0")) return;

    onView(withId(R.id.et_amount)).perform(replaceText(amount), closeSoftKeyboard());
    onView(withId(R.id.et_address)).perform(replaceText(ADDRESS), closeSoftKeyboard());
    onView(withId(R.id.et_password)).perform(replaceText(PASSWORD), closeSoftKeyboard());
    onView(withId(R.id.et_bank_account)).perform(replaceText(BANK_ACCOUNT), closeSoftKeyboard());
    onView(withId(R.id.bt_cash_out)).perform(scrollTo(), click());
    onView(withId(R.id.tv_output)).check(matches(withText(R.string.success_cash_out)));
    balanceAddress("0");
  }

  static void cashOut(String amount) {
    onView(withId(R.id.et_amount)).perform(replaceText(amount), closeSoftKeyboard());
    onView(withId(R.id.et_address)).perform(replaceText(ADDRESS), closeSoftKeyboard());
    onView(withId(R.id.et_password)).perform(replaceText(PASSWORD), closeSoftKeyboard());
    onView(withId(R.id.et_bank_account)).perform(replaceText(BANK_ACCOUNT), closeSoftKeyboard());
    onView(withId(R.id.bt_cash_out)).perform(scrollTo(), click());
    onView(withId(R.id.tv_output)).check(matches(withText(R.string.success_cash_out)));
  }

  static void tokenizeCreditCard() {
    onView(withId(R.id.bt_tokenize)).perform(scrollTo(), click());

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
    onView(withId(R.id.bt_cash_in)).perform(scrollTo(), click());

    onWebView()
        .withElement(findElement(Locator.XPATH, "//input[@alt='Continuar']"));
  }

  static void clearCache() {
    onView(withId(R.id.bt_clear_cache)).perform(scrollTo(), click());
  }

  static void checkClearCache() {
    onView(withId(R.id.et_address)).perform(replaceText(ADDRESS), closeSoftKeyboard());
    onView(withId(R.id.et_amount)).perform(replaceText("123"), closeSoftKeyboard());
    onView(withId(R.id.bt_cash_in)).perform(scrollTo(), click());

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

  private static String getText(final Matcher<View> matcher) {
    final String[] stringHolder = {null};
    onView(matcher).perform(new ViewAction() {
      @Override
      public Matcher<View> getConstraints() {
        return isAssignableFrom(TextView.class);
      }

      @Override
      public String getDescription() {
        return "getting text from a TextView";
      }

      @Override
      public void perform(UiController uiController, View view) {
        TextView tv = (TextView) view;
        stringHolder[0] = tv.getText().toString();
      }
    });
    return stringHolder[0];
  }
}
