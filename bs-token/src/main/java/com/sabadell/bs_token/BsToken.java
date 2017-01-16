package com.sabadell.bs_token;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CheckResult;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.sabadell.bs_token.internal.data.BsTokenRepository;
import com.sabadell.bs_token.internal.data.Ignore;
import com.sabadell.bs_token.internal.data.Persistence;
import com.sabadell.bs_token.internal.net.BsTokenApi;
import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx_activity_result2.Result;

/**
 * Interface to operate with BSToken service.
 */
public interface BsToken {

  /**
   * Add cash to a given ethereum user account.
   *
   * Pay attention to the `targetUI()` method in the `Result` object emitted.
   *
   * This method returns a safety instance of the current `Activity`. Because the original one may
   * be recreated (due to configuration changes or some other system events) it would be unsafe
   * calling it.
   *
   * For more information, refer to: https://github.com/VictorAlbertos/RxActivityResult
   *
   * @param activity the activity from this method is called.
   * @param amount the amount of money of Euros in cents
   * @param address the address of the user
   * @return a safety instance of the current `Activity`wrapped in a result object, containing too
   * the result code of this operation. It can be Activity.RESULT_OK or Activity.RESULT_CANCELED
   */
  @CheckResult <T extends Activity> Observable<Result<T>> cashIn(final T activity,
      final int amount, final String address);

  /**
   * Call this method to locally cache a credit card token in order to make the future process of
   * paying more user friendly.
   *
   * @param activity the activity from this method is called.
   * @return a safety instance of the current `Activity`wrapped in a result object, containing too
   * the result code of this operation. It can be Activity.RESULT_OK or Activity.RESULT_CANCELED
   */
  @CheckResult <T extends Activity> Observable<Result<T>> tokenizeCreditCard(final T activity);

  /**
   * Get the balance of available tokens for a given account.
   *
   * @return the balance.
   */
  @CheckResult Observable<Integer> balanceOf(final String address);

  /**
   * Withdraw certain amount of tokens as money to a given bank account.
   *
   * @param amount the amount of money of Euros in cents
   * @param address the address of the user.
   * @param password the password associated with the user account.
   * @param bankAccount the bank account to withdraw money.
   * @return The tx of the Ethereum transaction.
   */
  @CheckResult Observable<String> cashOut(final int amount, final String address,
      final String password, final String bankAccount);

  /**
   * Calling this method clear the data from the cache regarding the tokenization credit card.
   *
   * @return Ignore is just a flag to avoid nullable types.
   */
  @CheckResult Observable<Ignore> clearCache();

  class Builder {
    private boolean dev;

    /**
     * Call this to let the api resolve against an instance of ethereumjs-testrpc.
     */
    public Builder development() {
      this.dev = true;
      return this;
    }

    public BsToken build(Context context) {
      String url = dev ? BsTokenApi.URL_BASE_DEV : BsTokenApi.URL_BASE;

      OkHttpClient okHttpClient = new okhttp3.OkHttpClient().newBuilder()
          .readTimeout(60, TimeUnit.SECONDS)
          .writeTimeout(60, TimeUnit.SECONDS)
          .connectTimeout(60, TimeUnit.SECONDS)
          .build();

      return new BsTokenRepository(new Persistence(context), new Retrofit.Builder()
          .baseUrl(url)
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .client(okHttpClient)
          .build().create(BsTokenApi.class), url);
    }
  }
}
