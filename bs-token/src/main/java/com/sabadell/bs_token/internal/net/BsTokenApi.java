package com.sabadell.bs_token.internal.net;

import com.sabadell.bs_token.internal.data.Balance;
import com.sabadell.bs_token.internal.data.Tx;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BsTokenApi {
  String URL_BASE = "http://localhost:8080/bs_token/api/v1/";
  String URL_BASE_DEV = "http://localhost:8081/bs_token/test/api/v1/";

  @GET("balance/{account}") Observable<Response<Balance>> balanceOf(
      @Path("account") String account);

  @FormUrlEncoded
  @POST("cashOut") Observable<Response<Tx>> cashOut(
      @Field("amount") int amount,
      @Field("account") String account,
      @Field("password") String password,
      @Field("bank_account") String bankAccount);
}
