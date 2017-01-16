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
  String URL_BASE = "http://ec2-35-166-206-15.us-west-2.compute.amazonaws.com:8080/api/v1/";
  String URL_BASE_DEV = "http://ec2-35-166-206-15.us-west-2.compute.amazonaws.com:8080/test/api/v1/";

  @GET("balance/{account}") Observable<Response<Balance>> balanceOf(
      @Path("account") String account);

  @FormUrlEncoded
  @POST("cashOut") Observable<Response<Tx>> cashOut(
      @Field("amount") int amount,
      @Field("account") String account,
      @Field("password") String password,
      @Field("bank_account") String bankAccount);
}
