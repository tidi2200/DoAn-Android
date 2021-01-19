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
                    "Authorization:key=AAAAWS_A2q4:APA91bFuWUsgnqJllOj5tUTN_zHTUbxAULFA4WS2f3VA0MDiurEPTDhqBjSlO6XwCd0ogWKtSNpYCODTGakmIETmGHOAgWwBOZm0ht98cJrP1jFz7rd1KhYoassq9MGJtsgctdcxKD_B"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
