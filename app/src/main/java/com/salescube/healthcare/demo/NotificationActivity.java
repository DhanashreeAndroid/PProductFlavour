package com.salescube.healthcare.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.salescube.healthcare.demo.adapter.NotificationAdapter;
import com.salescube.healthcare.demo.data.model.NotificationModel;
import com.salescube.healthcare.demo.data.repo.NotificationRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private  NotificationRepo repo;
    private int maxid;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        setTitle("Notifications");

         repo = new NotificationRepo();

         maxid =  repo.getMaxId();
        recyclerView  =  findViewById(R.id.recyclerView);
     //   downloadNotification(maxid);
        display();


    }

 /*   private void downloadNotification() {





        try {
            Response<NotificationModel[]> response = call.execute();

            if (response.isSuccessful()) {
                NotificationModel[] clients = response.body();



            } else {

            }
        } catch (Exception e) {
            Log.d("Exception",""+e);

        }
    }*/

    private void downloadNotification(int maxid) {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<NotificationModel[]> call = apiService.getNotificationList(1,maxid);

        //   msgShort("Sending request dashboard " + token);


        call.enqueue(new Callback<NotificationModel[]>() {
            @Override
            public void onResponse(Call<NotificationModel[]> call, Response<NotificationModel[]> response) {
                if (response.isSuccessful()) {
                    NotificationModel[] view = response.body();

                    NotificationRepo repo = new NotificationRepo();
                    repo.insert(view);

                   // NotificationRepo.insert();
                }
            }

            @Override
            public void onFailure(Call<NotificationModel[]> call, Throwable t) {

            }
        });
    }

    private void display(){
        List<NotificationModel> list = repo.getNotificationList();

        NotificationAdapter maapter = new NotificationAdapter(NotificationActivity.this, list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(maapter);
    }
}
