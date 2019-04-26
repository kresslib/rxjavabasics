

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.file.FileSystem;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Helpers {

    private final static Vertx vertx = Vertx.vertx();

    public static FileSystem fs() {
        return vertx.fileSystem();
    }

    public static WebClient client() {
        return WebClient.create(vertx,
                new WebClientOptions().setDefaultPort(8080).setDefaultHost("localhost")
        );
    }

    public static WebClient client(int port, String host) {
        return WebClient.create(vertx,
                new WebClientOptions().setDefaultPort(port).setDefaultHost(host)
        );
    }

    public static Observable<String> villains_names() {
        return client()
                .get("/villains")
                .rxSend()
                .map(HttpResponse::bodyAsJsonObject)
                .map(json -> json.stream().map(Map.Entry::getValue).collect(Collectors.toList()))
                .flatMapObservable(Observable::fromIterable)
                .cast(String.class);
    }

    public static Observable<String> heroes_names() {
        return client()
                .get("/heroes")
                .rxSend()
                .map(HttpResponse::bodyAsJsonObject)
                .map(json -> json.stream().map(Map.Entry::getValue).collect(Collectors.toList()))
                .flatMapObservable(Observable::fromIterable)
                .cast(String.class);
    }

    public static Flowable<Character> heroes() {
        return fs().rxReadFile("src/main/resources/characters.json")
                .map(Buffer::toJsonArray)
                .flatMapPublisher(Flowable::fromIterable)
                .cast(JsonObject.class)
                .map(j -> j.mapTo(Character.class))
                .filter(s -> !s.isVillain());
    }

    public static Flowable<Character> villains() {
        return fs().rxReadFile("src/main/resources/characters.json")
                .map(Buffer::toJsonArray)
                .flatMapPublisher(Flowable::fromIterable)
                .cast(JsonObject.class)
                .map(j -> j.mapTo(Character.class))
                .filter(Character::isVillain);
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static final AtomicLong START_TIME = new AtomicLong();

    public static void log(String msg) {
        long now = System.currentTimeMillis();
        START_TIME.compareAndSet(0, now);
        long elapsed = now - START_TIME.get();
        String name = Thread.currentThread().getName();
        System.out.format("%2$-4s %1$-26s    %3$s\n", name, elapsed, msg);
    }

    private static final AtomicInteger threadCount = new AtomicInteger();
    public static final ThreadFactory threadFactory = r -> {
        Thread thread = new Thread(r);
        thread.setName("Scheduler-" + threadCount.getAndIncrement());
        return thread;
    };

    public static Observable<byte[]> getBytes(int port, String host, String URL) {
        byte[] b = null;
        return client(port, host)
                .get(URL).putHeader("imei", "123456")
                .rxSend()
                .map(HttpResponse::body)
                .map(body -> {
                    if (body.getBytes() == null) {
                        throw new RuntimeException("What a terrible failure!");
                    }
                    return body.getBytes();
                }).toObservable();

        //return body.getBytes();}).toObservable();
    }

    public static Observable<Integer> getBytes1(int port, String host, String URL) {
        return client(port, host)
                .get(URL).putHeader("emei", "12345")
                .rxSend()
                .map(HttpResponse::statusCode)
                .toObservable();
    }


}