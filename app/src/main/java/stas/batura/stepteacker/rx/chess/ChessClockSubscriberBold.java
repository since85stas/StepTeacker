package stas.batura.stepteacker.rx.chess;

import android.util.Log;

import rx.Subscriber;

public class ChessClockSubscriberBold extends Subscriber<Long> {

    public ChessStateChageListner mListner;

//    long[] timeIntervalsSub;

    long interval;

    ChessClockRx observable;

    long saved;

    public static final String TAG = ChessClockSubscriberBold.class.getName();


    public ChessClockSubscriberBold(ChessStateChageListner listner, long interval, ChessClockRx observable) {
        super();
        mListner = listner;
        this.interval = interval;
        this.observable = observable;
        Log.d(TAG, "onstart: ");
    }

    @Override
    public void onCompleted() {
        stopTimer();
        Log.d(TAG, "onCompleted: ");
    }

    @Override
    public void onError(Throwable e) {
        stopTimer();
        Log.d(TAG, "onError: ");
    }

    private void stopTimer() {
        mListner.timeFinish();
//        observable.recreate();
        unsubscribe();
        observable = null;
        mListner = null;
    }

    @Override
    public void onNext(Long aLong) {
        mListner.timeChange(aLong);
//        if ( aLong >= count * interval ) {
////            int interval = count ;
////            mListner.nextInterval( interval %2 == 1  );
//            count++;
//
//        }
    }
}
