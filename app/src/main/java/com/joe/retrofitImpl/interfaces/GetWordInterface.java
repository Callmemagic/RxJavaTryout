package com.joe.retrofitImpl.interfaces;

import com.joe.retrofitImpl.bean.Translation;


import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * author: Joe Cheng
 */
public interface GetWordInterface {

    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20world")
    Observable<Translation> getCall();
}
