package com.salescube.healthcare.demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.salescube.healthcare.demo.data.model.User;
import com.salescube.healthcare.demo.data.repo.UserRepo;
import com.salescube.healthcare.demo.sysctrl.ApiManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    private  static Retrofit retrofit = null;
    static String url = ApiManager.apiMainPath;

    public static  Retrofit getClient() {

            final User objUser = new UserRepo().getOwnerUser();

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request.Builder ongoing = chain.request().newBuilder();
                            ongoing.addHeader("USER_NAME", objUser.getUserName());
                            ongoing.addHeader("IMEI", objUser.getImeiNo());
                            return chain.proceed(ongoing.build());
                        }
                    }).build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            retrofit  = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        return retrofit;
    }
  public static Retrofit getRetrofitImage(Object data) {
      final User objUser = new UserRepo().getOwnerUser();
//        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            final String jsonStr = gson.toJson(data);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request.Builder ongoing = chain.request().newBuilder();
                            ongoing.addHeader("Data", jsonStr);
                            ongoing.addHeader("USER_NAME", objUser.getUserName());
                            ongoing.addHeader("IMEI", objUser.getImeiNo());
                            return chain.proceed(ongoing.build());
                        }
                    }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
//        }
        return retrofit;
    }

   /* public static Retrofit getAttendence() {
        if (retrofit == null) {

//            String url ="http://192.168.0.228:250/";
//            String url ="http://192.168.0.83:383/salescube-v2/api/";
            String url =ApiManager.apiMainPath;

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            retrofit  = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }*/
}
