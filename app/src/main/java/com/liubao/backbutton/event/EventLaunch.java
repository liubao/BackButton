package com.liubao.backbutton.event;

import com.liubao.backbutton.entity.EventEntity;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * * Created by liubao on 2019/1/13.
 */
interface EventLaunch {
    @GET("getevent.php")
    public Call<EventEntity> sendEvent(@Query(Querys.EVNET_ID) String id,
                                       @QueryMap() HashMap<String, String> params);
}
