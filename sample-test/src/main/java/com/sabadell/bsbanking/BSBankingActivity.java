package com.sabadell.bsbanking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public final class BSBankingActivity extends AppCompatActivity {
  private ApiEscrow apiEscrow;
  private TestSampleApp app;
  private TextView tv_output, et_address, et_password, et_amount, et_bank_account;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.bs_token);

    apiEscrow = new Retrofit.Builder()
        .baseUrl(ApiEscrow.URL_BASE)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(ApiEscrow.class);
    app = (TestSampleApp) getApplication();
    tv_output = (TextView) findViewById(R.id.tv_output);
    et_address = (TextView) findViewById(R.id.et_address);
    et_password = (TextView) findViewById(R.id.et_password);
    et_amount = (TextView) findViewById(R.id.et_amount);
    et_bank_account = (TextView) findViewById(R.id.et_bank_account);

    findViewById(R.id.bt_tokenize).setOnClickListener(view -> {
      app.bsToken().tokenizeCreditCard(this).subscribe(result -> {
        int resultCode = result.resultCode();

        if (resultCode == RESULT_OK) {
          tv_output.setText(R.string.success_tokenize);
        } else {
          tv_output.setText(R.string.failure);
        }
      }, error -> tv_output.setText(error.getMessage()));
    });

    findViewById(R.id.bt_cash_in).setOnClickListener(view -> {
      int amount = Integer.parseInt(et_amount.getText().toString());
      String address = et_address.getText().toString();

      app.bsToken().cashIn(this, amount, address).subscribe(result -> {
        int resultCode = result.resultCode();

        if (resultCode == RESULT_OK) {
          tv_output.setText(R.string.success_cash_in);
        } else {
          tv_output.setText(R.string.failure);
        }
      }, error -> tv_output.setText(error.getMessage()));
    });

    findViewById(R.id.bt_balance_of).setOnClickListener(view -> {
      String address = et_address.getText().toString();

      apiEscrow.balanceOf(address)
          .subscribeOn(app.backgroundThread())
          .observeOn(app.mainThread())
          .subscribe(balance -> tv_output.setText(String.valueOf(balance.body().amount)),
              error -> tv_output.setText(error.getMessage()));
    });

    findViewById(R.id.bt_cash_out).setOnClickListener(view -> {
      int amount = Integer.parseInt(et_amount.getText().toString());
      String address = et_address.getText().toString();
      String pass = et_password.getText().toString();
      String bankAccount = et_bank_account.getText().toString();

      app.bsToken().cashOut(amount, address, pass, bankAccount)
          .subscribeOn(app.backgroundThread())
          .observeOn(app.mainThread())
          .subscribe(tx -> tv_output.setText(R.string.success_cash_out),
              error -> tv_output.setText(error.getMessage()));
    });

    findViewById(R.id.bt_clear_cache).setOnClickListener(view -> {
      app.bsToken().clearCache()
          .subscribe(tx -> tv_output.setText(R.string.success_clear_cache),
              error -> tv_output.setText(error.getMessage()));
    });
  }

  interface ApiEscrow {
    String URL_BASE = "http://localhost:8081/escrow/test/api/v1/";

    @GET("balance/{account}") Observable<Response<Balance>> balanceOf(
        @Path("account") String account);

    final class Balance {
      private final int amount;

      public Balance(int amount) {
        this.amount = amount;
      }
    }
  }
}
