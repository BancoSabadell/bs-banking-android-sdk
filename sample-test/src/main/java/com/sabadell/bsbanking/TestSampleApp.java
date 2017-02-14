package com.sabadell.bsbanking;

import android.app.Application;
import android.os.AsyncTask;
import com.sabadell.bs_banking.BsBanking;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx_activity_result2.RxActivityResult;

public final class TestSampleApp extends Application {
  private BsBanking bsTokens;

  @Override public void onCreate() {
    super.onCreate();
    RxActivityResult.register(this);
    bsTokens = new BsBanking.Builder()
        .development()
        .build(this);
  }

  /**
   * Provide a single instance BsToken for the entire app lifecycle.
   */
  public BsBanking bsToken() {
    return bsTokens;
  }

  /**
   * Sync with main thread after subscribing to observables emitting from data layer.
   */
  public Scheduler mainThread() {
    return AndroidSchedulers.mainThread();
  }

  /**
   * Using this executor as the scheduler for all async operations allow us to tell espresso when
   * the app is in an idle state in order to wait for the response.
   */
  public Scheduler backgroundThread() {
    return Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR);
  }
}
