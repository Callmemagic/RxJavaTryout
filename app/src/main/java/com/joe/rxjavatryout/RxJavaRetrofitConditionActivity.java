package com.joe.rxjavatryout;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.joe.Constants;
import com.joe.retrofitImpl.bean.Translation;
import com.joe.retrofitImpl.interfaces.GetWordInterface;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * author: Joe Cheng
 * 可控制Retry機制的HTTP REQ/RESP
 */
public class RxJavaRetrofitConditionActivity extends AppCompatActivity {
    public static final String TAG = RxJavaRetrofitConditionActivity.class.getSimpleName();

    //發送REQ的次數
    private int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_query_word_condition);

        //1. 建立Retrofit物件
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        //2. 建立HTTP REQ接口
        GetWordInterface request = retrofit.create(GetWordInterface.class);

        //3.Observable 封裝HTTP REQ
        Observable<Translation> observable = request.getCall();

        //4.發送HTTP REQ ＆ 用repeatWhen()
        observable.repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {

                    @Override
                    public ObservableSource<?> apply(Object o) throws Exception {
                        //超過三次，結束
                        if(i > 3) {
                            //發到oneErorr事件
                            return Observable.error(new Throwable("訪問結束"));
                        }

                        //每兩秒問一次
                        return Observable.just(1).delay(2000, TimeUnit.MILLISECONDS);
                    }
                });
            }
        }).subscribeOn(Schedulers.io()) //切換到IO thread發http req
        .observeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Translation> (){

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Translation result) {
                        //接收RESP一次
                        result.show() ;
                        i++;
                    }

                    @Override
                    public void onError(Throwable e) {
                        //訪問結束
                        Log.d(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
