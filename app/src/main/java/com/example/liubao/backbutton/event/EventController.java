package com.example.liubao.backbutton.event;


import com.example.liubao.backbutton.API;
import com.example.liubao.backbutton.MLod;
import com.example.liubao.backbutton.common.LoginCommon;
import com.example.liubao.backbutton.entity.EventEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * * Created by liubao on 2019/1/13.
 */
public class EventController {

    public static void send(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.EVENT_UTL).
                        addConverterFactory(GsonConverterFactory.create()).build();
        EventLaunch eventLaunch = retrofit.create(EventLaunch.class);
        Call<EventEntity> call = eventLaunch.sendEvent(id, LoginCommon.getBaseParams());
        call.enqueue(new Callback<EventEntity>() {
            @Override
            public void onResponse(Call<EventEntity> call, Response<EventEntity> response) {
                EventEntity eventEntity = response.body();
                if (eventEntity == null) {
                    return;
                }
                MLod.d(eventEntity.info);
            }

            @Override
            public void onFailure(Call<EventEntity> call, Throwable t) {

            }
        });
    }


}
