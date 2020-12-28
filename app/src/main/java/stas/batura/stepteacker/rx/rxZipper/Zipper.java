package stas.batura.stepteacker.rx.rxZipper;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class Zipper {

    public Zipper (Consumer<Container> cons) {
        try {
            consumer = cons;
            zipOperator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Consumer<Container> consumer ;

    Observable<Float> pressureObs = Observable.empty();
    Observable<Float> altitObs = Observable.empty();

    public void zipOperator() throws Exception {

        List<Integer> indexes = Arrays.asList(0, 1, 2, 3, 4);
        List<String> letters = Arrays.asList("a", "b", "c", "d", "e");

    }


    public void generatePress(Float press) {

        pressureObs = Observable.just(press);
    }

    public void generateAltit(Float alt) {
        altitObs = Observable.just(alt);
    }

    public void generObserv() {
        Observable.zip(pressureObs, altitObs, mergeEmittedItems())
                .subscribe(consumer);

        altitObs = Observable.empty();
        pressureObs = Observable.empty();
    }

    @NonNull
    private BiFunction<Float, Float, Container> mergeEmittedItems() {
        return new BiFunction<Float, Float, Container>() {
            @Override
            public Container apply(Float press, Float alt) throws Exception {
                return new Container(press, alt);
            }
        };
    }

//    @NonNull
//    private Consumer<String> printMergedItems() {
//        return new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                System.out.println(s);
//            }
//        };
//    }



}
