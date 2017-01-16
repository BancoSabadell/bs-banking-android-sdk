package com.sabadell.bstoken;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public final class BSTokenActivity extends AppCompatActivity {
  private TestSampleApp app;
  private TextView tv_output, et_address, et_password, et_amount, et_bank_account;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.bs_token);

    app = (TestSampleApp) getApplication();
    tv_output = (TextView) findViewById(R.id.tv_output);
    et_address = (TextView) findViewById(R.id.et_address);
    et_password = (TextView) findViewById(R.id.et_password);
    et_amount = (TextView) findViewById(R.id.et_amount);
    et_bank_account = (TextView) findViewById(R.id.et_bank_account);

    findViewById(R.id.bt_tokenize).setOnClickListener(view -> {
      app.bsTokens().tokenizeCreditCard(this).subscribe(result -> {
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

      app.bsTokens().cashIn(this, amount, address).subscribe(result -> {
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

      app.bsTokens().balanceOf(address)
          .subscribeOn(app.backgroundThread())
          .observeOn(app.mainThread())
          .subscribe(tx -> tv_output.setText(R.string.success_cash_out),
              error -> tv_output.setText(error.getMessage()));
    });

    findViewById(R.id.bt_cash_out).setOnClickListener(view -> {
      int amount = Integer.parseInt(et_amount.getText().toString());
      String address = et_address.getText().toString();
      String pass = et_password.getText().toString();
      String bankAccount = et_bank_account.getText().toString();

      app.bsTokens().cashOut(amount, address, pass, bankAccount)
          .subscribeOn(app.backgroundThread())
          .observeOn(app.mainThread())
          .subscribe(balance -> tv_output.setText(String.valueOf(balance)),
              error ->
                  Toast.makeText(this, error.getMessage(),
                      Toast.LENGTH_SHORT).show()
          );
    });
  }
}
