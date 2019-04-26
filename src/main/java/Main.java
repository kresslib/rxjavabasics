import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import org.reactivestreams.Subscriber;
import rxjavaclasses.HotStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static rxjavaclasses.HotStream.nap;

public class Main {

    public static void main(String[] args) {
//        Observable<String> observable = Observable.fromArray(new String[]{"one", "two", "three"});
//        Observer<String> observer1 = new Observer<String>() {
//            public void onSubscribe(Disposable disposable) {
//
//            }
//
//            public void onNext(String s) {
//                System.out.println("onNext: " + s);
//            }
//
//            public void onError(Throwable e) {
//                System.out.println("onError: " + e);
//            }
//
//            public void onComplete() {
//                System.out.println("onCompleted");
//            }
//
//        };
//
//        observable.subscribe(observer1);

        List<String> SUPER_HEROES = Arrays.asList(
                "Superman",
                "Batman",
                "Aquaman",
                "Asterix",
                "Captain America"
        );
        System.out.println("subscribe");
        Observable<String> stream = Observable.fromIterable(SUPER_HEROES);
        stream.subscribe(
                name -> System.out.println(name)
        );
        System.out.println("map & filter");
        Observable.fromIterable(SUPER_HEROES).map(n -> n.toUpperCase()).filter(name -> name.startsWith("A")).subscribe(name -> System.out.println(name));
        System.out.println("Observable");
        Observable.fromIterable(SUPER_HEROES).doOnNext(s -> System.out.println("Next >> " + s)).doOnComplete(() -> System.out.println("Completion")).subscribe();
        System.out.println("Exception!!!");
        Observable.fromIterable(SUPER_HEROES)
                .map(name -> {
                    if (name.endsWith("x")) {
                        throw new RuntimeException("What a terrible failure!");
                    }
                    return name.toUpperCase();
                }).doOnNext(s -> System.out.println(">> " + s))
                .doOnComplete(() -> System.out.println("Completion... not called"))
                .doOnError(err -> System.out.println("Oh no! " + err.getMessage()))
                .subscribe();
        System.out.println("Observer");
        Observable.just("Black Canary", "Catwoman", "Elektra")
                .subscribe(
                        name -> System.out.println(">> " + name),
                        Throwable::printStackTrace,
                        () -> System.out.println("Completion")
                );
        System.out.println("stream");
        stream = Observable.create(subscriber -> {
            // Emit items
            subscriber.onNext("Black Canary");
            subscriber.onNext("Catwoman");
            subscriber.onError(new Exception("What a terrible failure"));
            subscriber.onNext("Elektra");
            // Notify the completion
            subscriber.onComplete();
        });
        stream.subscribe(
                i -> System.out.println("Received: " + i),
                err -> System.out.println("BOOM2"),
                () -> System.out.println("Completion")
        );
        stream.subscribe(
                i -> System.out.println("Received: " + i),
                err -> System.out.println("BOOM"),
                () -> System.out.println("Completion")
        );
//        System.out.println("endless stream");
//        stream = Observable.create(subscriber -> {
//            boolean done = false;
//            Scanner scan = new Scanner(System.in);
//            while(! done) {
//                String input = scan.next();
//                if (input.contains("done")) {
//                    done = true;
//                    subscriber.onComplete();
//                } else if (input.contains("error")) {
//                    subscriber.onError(new Exception(input));
//                } else {
//                    subscriber.onNext(input);
//                }
//            }
//        });
//
//        stream
//                .subscribe(
//                        i -> System.out.println("Received: " + i),
//                        err -> System.out.println("BOOM"),
//                        () -> System.out.println("Completion")
//                );
        System.out.println("Cold streams");
        stream = Observable.just("Black Canary", "Catwoman", "Elektra");

        stream
                .subscribe(
                        i -> System.out.println("[A] Received: " + i),
                        err -> System.out.println("[A] BOOM"),
                        () -> System.out.println("[A] Completion")
                );

        stream
                .subscribe(
                        i -> System.out.println("[B] Received: " + i),
                        err -> System.out.println("[B] BOOM"),
                        () -> System.out.println("[B] Completion")
                );
        System.out.println("Hot streams");
        System.out.println("Stopping emissions");
        Observable<Integer> stream_int = HotStream.create();

        Disposable s1 = stream_int
                .subscribe(
                        i -> System.out.println("[A] Received: " + i),
                        err -> System.out.println("[A] BOOM"),
                        () -> System.out.println("[A] Completion")
                );

        nap();

        Disposable s2 = stream_int
                .subscribe(
                        i -> System.out.println("[B] Received: " + i),
                        err -> System.out.println("[B] BOOM"),
                        () -> System.out.println("[B] Completion")
                );
        nap(5);
        s1.dispose();

        nap(3);

        // Cancel the subscription for B
        s2.dispose();
        nap(3);
        s1 = stream_int
                .subscribe(
                        i -> System.out.println("[A] Received: " + i),
                        err -> System.out.println("[A] BOOM"),
                        () -> System.out.println("[A] Completion")
                );
        nap(3);
        s1.dispose();
        System.out.println("Single - поток, излучающий 1 элемент");

//        Disposable d = Single.just("Superman")
//                .doOnSuccess(s -> System.out.println("Hello " + s))
//                .subscribe();
//        d.dispose();
        Single<String> singleString = Single.just("Superman").doOnSuccess(s -> System.out.println("Hello " + s));
        Disposable d = singleString.subscribe();
        d.dispose();
        singleString.subscribe();
        System.out.println("Single - поток, излучающий 1 элемент, ERROR");
        singleString = Single.just("Superman").map(n -> {
            throw new RuntimeException("What a terrible failure!");
        });
        singleString.subscribe((name, err) -> {
                    if (err == null) {
                        System.out.println("Hello " + name);
                    } else {
                        //err.printStackTrace();
                        System.out.println("ERROR : " + name);
                    }
                }
        );

    }
}
