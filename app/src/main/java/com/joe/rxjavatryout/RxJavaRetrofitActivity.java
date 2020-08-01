package com.joe.rxjavatryout;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.joe.retrofitImpl.bean.Translation;
import com.joe.retrofitImpl.interfaces.GetWordInterface;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import static com.joe.Constants.base_URL;


/**
 * author: Joe Cheng
 */
public class RxJavaRetrofitActivity extends AppCompatActivity {
    public static final String TAG = RxJavaRetrofitActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_query_word);

        Observable.interval(2, 5, TimeUnit.SECONDS)
                //在Next()之前會執行doOnNext()，每次發送數字之前都會執行一次REQ
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "第" + aLong + "次" + "查詢");

                        //1. 產生retrofit物件
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(base_URL)
                                .addConverterFactory(GsonConverterFactory.create()) //GSON解析
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //支援RxJava
                                .build();

                        //2. 建立REQ接口
                        final GetWordInterface req = retrofit.create(GetWordInterface.class);

                        //3. 用Observable形式對http req進行封裝
                        Observable<Translation> observable = req.getCall(); //getCall() return一個Observable

                        //4. 切換執行緒發送HTTP REQ
                        observable.subscribeOn(Schedulers.io()) //切換到io thread進行http req
                                .observeOn(AndroidSchedulers.mainThread()) //切回到main thread處理
                                .subscribe(new Observer<Translation>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Translation result) {
                                        result.show();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d(TAG, "失敗：" + e.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });

                    }
                }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "對error事件處理" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "對complete事件處理");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
