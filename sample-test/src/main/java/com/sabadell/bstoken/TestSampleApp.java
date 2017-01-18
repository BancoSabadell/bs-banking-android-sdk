package com.sabadell.bstoken;

import android.app.Application;
import android.os.AsyncTask;
import com.sabadell.bs_token.BsToken;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx_activity_result2.RxActivityResult;

public final class TestSampleApp extends Application {
  private BsToken bsTokens;

  @Override public void onCreate() {
    super.onCreate();
    RxActivityResult.register(this);
    bsTokens = new BsToken.Builder()
        .development()
        .build(this);
  }

  /**
   * Provide a single instance BsToken for the entire app lifecycle.
   */
  public BsToken bsToken() {
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
