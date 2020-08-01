package com.joe.retrofitImpl.bean;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

/**
 * author: Joe Cheng
 */
public class Translation {

    @SerializedName("status")
    private int status;

    @SerializedName("content")
    private content content;


    private static class content {
        @SerializedName("from")
        private String from;
        @SerializedName("to")
        private String to;
        @SerializedName("vendor")
        private String vendor;
        @SerializedName("out")
        private String out;
        @SerializedName("err_no")
        private int errNo;
    }

    //定义 输出返回数据 的方法
    public void show() {
        Log.d("RxJava", content.out );
    }

}
