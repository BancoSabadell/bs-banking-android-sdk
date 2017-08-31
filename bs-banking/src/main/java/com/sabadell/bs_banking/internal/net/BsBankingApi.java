package com.sabadell.bs_banking.internal.net;

import com.sabadell.bs_banking.internal.data.Tx;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BsBankingApi {
  String URL_BASE = "http://innochain.innocells.io:8082/bs_banking/api/v1/";
  String URL_BASE_DEV = "http://admin-hackathon.westeurope.cloudapp.azure.com:8081/bs_banking/api/v1/";

  @FormUrlEncoded
  @POST("cashOut") Observable<Response<Tx>> cashOut(
      @Field("amount") int amount,
      @Field("account") String account,
      @Field("password") String password,
      @Field("bank_account") String bankAccount);
}
