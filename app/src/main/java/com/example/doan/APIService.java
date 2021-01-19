package com.example.doan;

import com.example.doan.Notification.MyResponse;
import com.example.doan.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAWS_A2q4:APA91bEvwTsiHnCw_1YuGpkUIT2ZSMEbfohnWTNs-b_mKwRiWvnin7UFcp6JnuKdNd6qAJqFvQApeInp-xDJ9kbL3zXVBVaDEuF_ujLI7vUw21phRk4lI2pKkaZWXOQZikkv4B0Rf_e8"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
