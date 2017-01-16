package com.sabadell.bs_token.internal.data;

import com.sabadell.bs_token.internal.net.BsTokenApi;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import retrofit2.Response;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class BsTokenRepositoryTest {
  @Mock Persistence persistence;
  @Mock BsTokenApi api;
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  private BsTokenRepository repositoryUT;

  @Before public void before() {
    repositoryUT = new BsTokenRepository(persistence, api, BsTokenApi.URL_BASE_DEV);
  }

  @Test public void Verify_Clear_Cache_Tokenize_Credit_Card() {
    repositoryUT.clearCache()
        .test()
        .assertNoErrors()
        .assertValueCount(1)
        .assertComplete();

    verify(persistence, times(1)).clearCache();
  }

  @Test public void Verify_Balance_Of_Without_Valid_Address() {
    repositoryUT.balanceOf(null)
        .test()
        .assertNoValues()
        .assertErrorMessage(BsTokenRepository.ADDRESS_VALIDATION)
        .assertNotComplete();

    repositoryUT.balanceOf("")
        .test()
        .assertNoValues()
        .assertErrorMessage(BsTokenRepository.ADDRESS_VALIDATION)
        .assertNotComplete();
  }

  @Test public void Verify_Balance_Of_Success() {
    String address = "address";
    Balance balance = new Balance(1);

    when(api.balanceOf(address))
        .thenReturn(Observable.just(Response.success(balance)));

    repositoryUT.balanceOf(address)
        .test()
        .assertNoErrors()
        .assertValueCount(1)
        .assertValue(1)
        .assertComplete();
  }

  @Test public void Verify_Balance_Of_Failure() {
    String address = "address";
    String errorMessage = "Can't get balance";

    when(api.balanceOf(address)).thenReturn(this.<Balance>mockErrorResponse(errorMessage));

    repositoryUT.balanceOf(address)
        .test()
        .assertNoValues()
        .assertErrorMessage(errorMessage)
        .assertNotComplete();
  }

  @Test public void Verify_CashOut_Without_Valid_Amount() {
    String account = "account";
    String password = "password";
    String bankAccount = "bankAccount";

    repositoryUT.cashOut(0, account, password, bankAccount)
        .test()
        .assertNoValues()
        .assertErrorMessage(BsTokenRepository.AMOUNT_VALIDATION)
        .assertNotComplete();
  }

  @Test public void Verify_CashOut_Without_Valid_Address() {
    int amount = 1;
    String password = "password";
    String bankAccount = "bankAccount";

    repositoryUT.cashOut(amount, null, password, bankAccount)
        .test()
        .assertNoValues()
        .assertErrorMessage(BsTokenRepository.ADDRESS_VALIDATION)
        .assertNotComplete();

    repositoryUT.cashOut(amount, "", password, bankAccount)
        .test()
        .assertNoValues()
        .assertErrorMessage(BsTokenRepository.ADDRESS_VALIDATION)
        .assertNotComplete();
  }

  @Test public void Verify_CashOut_Without_Valid_Password() {
    int amount = 1;
    String account = "account";
    String bankAccount = "bankAccount";

    repositoryUT.cashOut(amount, account, null, bankAccount)
        .test()
        .assertNoValues()
        .assertErrorMessage(BsTokenRepository.PASSWORD_VALIDATION)
        .assertNotComplete();

    repositoryUT.cashOut(amount, account, "", bankAccount)
        .test()
        .assertNoValues()
        .assertErrorMessage(BsTokenRepository.PASSWORD_VALIDATION)
        .assertNotComplete();
  }

  @Test public void Verify_CashOut_Without_Valid_Bank_Account() {
    int amount = 1;
    String account = "account";
    String password = "password";

    repositoryUT.cashOut(amount, account, password, null)
        .test()
        .assertNoValues()
        .assertErrorMessage(BsTokenRepository.BANK_ACCOUNT_VALIDATION)
        .assertNotComplete();

    repositoryUT.cashOut(amount, account, password, "")
        .test()
        .assertNoValues()
        .assertErrorMessage(BsTokenRepository.BANK_ACCOUNT_VALIDATION)
        .assertNotComplete();
  }

  @Test public void Verify_CashOut_Success() {
    int amount = 1;
    String account = "account";
    String password = "password";
    String bankAccount = "bankAccount";
    String tx = "tx";

    when(api.cashOut(amount, account, password, bankAccount))
        .thenReturn(Observable.just(Response.success(new Tx("tx"))));

    repositoryUT.cashOut(amount, account, password, bankAccount)
        .test()
        .assertNoErrors()
        .assertValueCount(1)
        .assertValue(tx)
        .assertComplete();
  }

  @Test public void Verify_CashOut_Failure() {
    int amount = 1;
    String account = "account";
    String password = "password";
    String bankAccount = "bankAccount";
    String errorMessage = "Can't fulfill";

    when(api.cashOut(amount, account, password, bankAccount))
        .thenReturn(this.<Tx>mockErrorResponse(errorMessage));

    repositoryUT.cashOut(amount, account, password, bankAccount)
        .test()
        .assertNoValues()
        .assertErrorMessage(errorMessage)
        .assertNotComplete();
  }

  private <T> Observable<Response<T>> mockErrorResponse(String message) {
    Response<T> response = Response.error(404, ResponseBody.create(null, message));
    return Observable.just(response);
  }
}
