package app;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Examples {
    public static void main(String[] args) {

        Observable.just("Resilient", "Elastic")
                .map(String::toLowerCase)
                .flatMap(word -> Observable.fromArray(word.split("")))
                .filter(letter -> !letter.equals("r") && !letter.equals("e"))
                .count()
                .subscribe(System.out::println);

        System.out.println();

        Observable.just(1, 20, 0, 3)
                .filter(x -> x != 0)
                .map(x -> 1 / x)
                .count()
                .subscribe(System.out::println, Throwable::printStackTrace);

        System.out.println();

        Flowable.range(1, 10)
                .flatMap(
                        v -> Flowable.just(v)
                                .subscribeOn(Schedulers.computation())
                                .map(w -> w * w)
                )
                .blockingSubscribe(System.out::println);

        System.out.println();

        Flowable.range(1, 10)
                .parallel()
                .runOn(Schedulers.computation())
                .map(v -> v * v)
                .sequential()
                .blockingSubscribe(System.out::println);

        System.out.println();

        Flowable.range(1, 5)
                .map(x -> Flowable.range(x, x))
                .flatMap(xs -> xs)
                .subscribe(System.out::println);

    }
}
