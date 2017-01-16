package com.sabadell.bs_token.internal.data;

import android.app.Activity;
import android.content.Intent;
import com.sabadell.bs_token.BsToken;
import com.sabadell.bs_token.internal.net.BsTokenApi;
import com.sabadell.bs_token.internal.net.NetworkResponse;
import com.sabadell.bs_token.internal.presentation.RedsysPaymentActivity;
import com.sabadell.bs_token.internal.presentation.RedsysTokenizeCreditCardActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import rx_activity_result2.Result;
import rx_activity_result2.RxActivityResult;

public final class BsTokenRepository implements BsToken {
  private final BsTokenApi api;
  private final NetworkResponse networkResponse;
  private final Persistence persistence;
  private final String urlBase;
  static final String PASSWORD_VALIDATION = "Invalid password",
      AMOUNT_VALIDATION = "Invalid amount",
      ADDRESS_VALIDATION = "Invalid account",
      BANK_ACCOUNT_VALIDATION = "Invalid bank account";

  public BsTokenRepository(Persistence persistence, BsTokenApi api, String urlBase) {
    this.persistence = persistence;
    this.api = api;
    this.urlBase = urlBase;
    this.networkResponse = new NetworkResponse();
  }

  @Override
  public <T extends Activity> Observable<Result<T>> cashIn(T activity, int amount,
      String address) {
    if (amount == 0) return Observable.error(new RuntimeException(AMOUNT_VALIDATION));

    if (address == null || address.isEmpty()) {
      return Observable.error(new RuntimeException(ADDRESS_VALIDATION));
    }

    Intent intent = new Intent(activity, RedsysPaymentActivity.class);

    intent.putExtra(RedsysPaymentActivity.URL_BASE_KEY, urlBase);
    intent.putExtra(RedsysPaymentActivity.AMOUNT_KEY, String.valueOf(amount));
    intent.putExtra(RedsysPaymentActivity.ADDRESS_KEY, address);
    intent.putExtra(RedsysPaymentActivity.CREDIT_CARD_TOKEN_KEY,
        persistence.getMerchantIdentifier());

    return RxActivityResult.on(activity).startIntent(intent);
  }

  @Override public <T extends Activity> Observable<Result<T>> tokenizeCreditCard(T activity) {
    Intent intent = new Intent(activity, RedsysTokenizeCreditCardActivity.class);
    intent.putExtra(RedsysTokenizeCreditCardActivity.URL_BASE_KEY, urlBase);

    return RxActivityResult.on(activity).startIntent(intent)
        .doOnNext(new Consumer<Result<T>>() {
          @Override public void accept(Result<T> result) throws Exception {
            String url = result.data()
                .getStringExtra(RedsysTokenizeCreditCardActivity.MERCHANT_PARAMS_URL_KEY);

            MerchantParamsDecoder merchantParamsDecoder = new MerchantParamsDecoder(url);
            MerchantParamsDecoder.MerchantParams merchantParams = merchantParamsDecoder.decode();

            persistence.saveTokenCreditCardData(merchantParams.getDsExpiryDate(),
                merchantParams.getDsMerchantIdentifier());
          }
        });
  }

  @Override public Observable<Integer> balanceOf(String address) {
    if (address == null || address.isEmpty()) {
      return Observable.error(new RuntimeException(ADDRESS_VALIDATION));
    }
    return api.balanceOf(address)
        .compose(networkResponse.<Balance>process())
        .map(new Function<Balance, Integer>() {
          @Override public Integer apply(Balance balance) throws Exception {
            return balance.getAmount();
          }
        });
  }

  @Override public Observable<String> cashOut(int amount, String address,
      String password, String bankAccount) {
    if (amount == 0) return Observable.error(new RuntimeException(AMOUNT_VALIDATION));

    if (address == null || address.isEmpty()) {
      return Observable.error(new RuntimeException(ADDRESS_VALIDATION));
    }

    if (password == null || password.isEmpty()) {
      return Observable.error(new RuntimeException(PASSWORD_VALIDATION));
    }

    if (bankAccount == null || bankAccount.isEmpty()) {
      return Observable.error(new RuntimeException(BANK_ACCOUNT_VALIDATION));
    }

    return api.cashOut(amount, address, password, bankAccount)
        .compose(networkResponse.<Tx>process())
        .map(new Function<Tx, String>() {
          @Override public String apply(Tx tx) throws Exception {
            return tx.getValue();
          }
        });
  }

  @Override public Observable<Ignore> clearCache() {
    return Observable.create(new ObservableOnSubscribe<Ignore>() {
      @Override public void subscribe(ObservableEmitter<Ignore> emitter) throws Exception {
        persistence.clearCache();
        emitter.onNext(Ignore.Instance);
        emitter.onComplete();
      }
    });
  }
}
