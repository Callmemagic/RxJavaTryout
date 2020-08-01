package com.joe.rxjavatryout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    TextView tv1;
    Integer i = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = findViewById(R.id.tv1);
        tv1.setOnClickListener(this);

        //===================Observable 被觀察者======================
        //被觀察者-observable 負責拋出需求、待做的事情(emit)
        //create()創造事件序列
        //observable被訂閱時，事件會依序被觸發，觀察者也依序對事件做出回應
        //被觀察者調用觀察者的callback方法，這樣就是觀察者模式
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onComplete();
            }
        });

        //有別的更簡單的方法可以創造Observable
        //1.快速的
        //最多一次just 10個參數
        Observable observable1 = Observable.just("A", "B", "C");

        //2.先塞成陣列
        //可以塞10個以上的參數
        String[] words = {"A", "B", "C"};
        Observable observable2 = Observable.fromArray(words);

        //3.也可以塞成集合
        //可以塞10個以上的參數
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Observable observable3 = Observable.fromIterable(list);

        //4.延遲創建
        //觀察者Observer訂閱時，才動態創建被觀察者Obervable及發送事件
//        Observable observable4 = Observable.defer(new Callable<ObservableSource>() {
//            @Override
//            public ObservableSource call() throws Exception {
//                return Observable.just(i);
//            }
//        });
//
//        i = 15;
//
//        observable4.subscribe(new Observer<Integer>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                //訂閱的時候會取得最新的Observable的值，因此此時印出來的是15
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });

        //5.延遲指定時間後，發送一個數值0
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        //時間到了回一個0出去
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        //6.每隔一段時間就發送一次事件，次數為無限
        //從0開始每個一段時間一直遞增1
        //一開始是3秒延遲，後來每隔1秒產生一個數字
        Observable.interval(3, 1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        //7.每隔一段時間就發送一次事件，可限制次數
        //類似上面interval()，但可以限制次數，也可以定義起點
        //從3開始，第一次延遲2秒，後來每1秒發送一次，總共10次
        Observable.intervalRange(3, 10, 2, 1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        //8.連續發送事件序列，可選取範圍
        //類似於上面intervalRange()，但無延遲
        //從3開始，總共送10次
        Observable.range(3, 10)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        //===================Observable 被觀察者======================

        //=====================Observer 觀察者========================
        //1.Observer接口
        //要覆寫對應事件的方法，預設是先走onSubscibe()
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                //最先執行的
            }

            @Override
            public void onNext(Integer integer) {
                //當被觀察者發送onNext()事件，由這裡做callback
            }

            @Override
            public void onError(Throwable e) {
                //當被觀察者發送onError()事件，由這裡做callback
            }

            @Override
            public void onComplete() {
                //當被觀察者發送onComplete()事件，由這裡做callback
            }
        };

        //Subscriber接口
        //預設是先走onSubscibe()
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                //最先執行的
            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };
        //=====================Observer 觀察者========================

        //=====================subscribe 訂閱=========================
        observable.subscribe(observer);
        //Subscriber怎麼用呢？
        //=====================subscribe 訂閱=========================

        //整理～～
        //事件流的鏈式組合技
        //把上面的被觀察者、觀察者的code合併在一起就可以了
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            Disposable mDisposable;
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe連接");
                mDisposable = d;
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "執行onNext:" + value);

                if(value == 2)
                {
                    mDisposable.dispose();
                    Log.d(TAG, "斷開連結：" + mDisposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "執行onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "執行onComplete");
            }
        });

        //上面組合記得執行順序是：觀察者Observer的onSubscribe -> 被觀察者的subscribe -> 觀察者的onNext -> 觀察者的onComplete


    }

    @Override
    public void onClick(View v) {
//        startActivity(new Intent(this, RxJavaRetrofitActivity.class));
        startActivity(new Intent(this, RxJavaRetrofitConditionActivity.class));
    }
}
