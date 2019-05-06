import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;


public class Code3 {

    //    public static void main(String[] args) {
//
//        Path path = Paths.get("C:/Temp/TempFile.db");
//        String URL = "/commandserv/resources/infoenergo.getnewcommands";
//
//        Helpers.getBytes1(8087, "localhost", URL).subscribe((code) -> {
//            System.out.println(code);
//            if (code == 200) {
//                Helpers.getBytes(8087, "localhost", URL).subscribe((bytes) -> {
//                    if (bytes != null) {
//                        System.out.println("Bytes : " + bytes.length);
//                        Files.write(path, bytes, StandardOpenOption.CREATE);
//                    } else {
//                        System.out.println("Пустой массив");
//                    }
//                });
//            } else {
//                System.out.println("Code : " + code);
//            }
//        });

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = client.newBuilder();
        builder.connectTimeout(500, TimeUnit.MILLISECONDS)
                .readTimeout(2000, TimeUnit.MILLISECONDS);
        OkHttpClient client2 = builder.build();

        String url404 = "http://localhost:8087/untitled/resources/infoenergo.getnewcommands_test"; // 404
        String url200 = "http://localhost:8087/commandserv/resources/infoenergo.getnewcommands"; // 200
        String url2 = "http://square.github.io/okhttpjkhjhkjhkj/";


        Request request1 = new Request.Builder().url(url200).addHeader("imei", "12345").build();


        Observable<String> resp = Observable.create(e -> {
            long millisStart = System.currentTimeMillis();

            try (Response resp3 = client2.newCall(request1).execute()) {
                if (resp3.code() == 200) {
                    e.onNext("Соединение установлено (код 200)");
                    // проверяем, есть ли новая база данных
                    if (checkDatabaseExist(resp3)) {
                        e.onNext("Обнаружена база данных");
                        e.onComplete();
                    } else {
                        e.onError(new Exception("Новых команд не обнаружено"));
                    }
                } else {
                    e.onError(new Exception("Code not 200"));
                }
            } catch (SocketTimeoutException ex) {
                long millisFinish = System.currentTimeMillis();
                long timeCodeExecute = (millisFinish - millisStart) / 1000;
                e.onError(new Exception("Вышел тайм-аут соединения спустя " + timeCodeExecute + " сек."));
            }
        });


        //Observable<String> s_resp = resp.map(response -> response.body().toString());

        Disposable d = resp.subscribeWith(new DisposableObserver<String>() {
            @Override
            public void onStart() {
                System.out.println("Started");
            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onComplete() {
                System.out.println("---  Выполнение завершено успешно  ---");
                dispose();
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("---  Выполнение завершено успешно с ошибкой ---");
                System.out.println(t);
                dispose();
            }

        });


//        Path path = Paths.get("C:/Temp/TempFile.db");
//        String URL = "/commandserv/resources/infoenergo.getnewcommands";
//
//        Helpers.getBytes1(8087, "localhost", URL).subscribe((code) -> {
//            System.out.println(code);
//            if (code == 200) {
//                Helpers.getBytes(8087, "localhost", URL).subscribe((bytes) -> {
//                    if (bytes != null) {
//                        System.out.println("Bytes : " + bytes.length);
//                        Files.write(path, bytes, StandardOpenOption.CREATE);
//                    } else {
//                        System.out.println("Пустой массив");
//                    }
//                });
//            } else {
//                System.out.println("Code : " + code);
//            }
//        });

        // получить статус-код
//        Helpers.getBytes1(8087, "localhost", URL).subscribe((code) -> {
//            System.out.println("Code : " + code);
//        });

    }

    private static boolean checkDatabaseExist(Response response) {
        return Integer.parseInt(response.header("exist")) > 0;
    }


//    public static void main2(String[] args) {
//        OkHttpClient client = new OkHttpClient();
//        OkHttpClient.Builder builder = client.newBuilder();
//        builder.connectTimeout(500, TimeUnit.MILLISECONDS)
//                .readTimeout(2000, TimeUnit.MILLISECONDS);
//        OkHttpClient client2 = builder.build();
//
//        String url404 = "http://localhost:8087/untitled/resources/infoenergo.getnewcommands_test"; // 404
//        String url200 = "http://localhost:8087/commandserv/resources/infoenergo.getnewcommands"; // 200
//        String url2 = "http://square.github.io/okhttpjkhjhkjhkj/";
//
//
//        Request request1 = new Request.Builder().url(url200).build();
//
//
//        Observable<Response> resp = Observable.create(e -> {
//            long millisStart = System.currentTimeMillis();
//
//            try (Response resp3 = client2.newCall(request1).execute()) {
//                if (resp3.code() == 200) {
//                    if (resp3.body() != null) {
//                        e.onNext(resp3);
//
//
//                        e.onComplete();
//                    } else {
//                        e.onError(new Exception("Пустой массив байтов"));
//                    }
//                } else {
//                    e.onError(new Exception("Code not 200"));
//                }
//            } catch (SocketTimeoutException ex) {
//                long millisFinish = System.currentTimeMillis();
//                long timeCodeExecute = (millisFinish - millisStart) / 1000;
//                e.onError(new Exception("Вышел тайм-аут соединения спустя " + timeCodeExecute + " сек."));
//            }
//        });
//
//
//        //Observable<String> s_resp = resp.map(response -> response.body().toString());
//
//        Disposable d = resp.subscribeWith(new DisposableObserver<Response>() {
//            @Override
//            public void onStart() {
//                System.out.println("Started");
//            }
//
//            @Override
//            public void onNext(Response s) {
//
//            }
//
//            @Override
//            public void onComplete() {
//                dispose();
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                System.out.println(t);
//                dispose();
//            }
//
//        });
//
//
////        Path path = Paths.get("C:/Temp/TempFile.db");
////        String URL = "/commandserv/resources/infoenergo.getnewcommands";
////
////        Helpers.getBytes1(8087, "localhost", URL).subscribe((code) -> {
////            System.out.println(code);
////            if (code == 200) {
////                Helpers.getBytes(8087, "localhost", URL).subscribe((bytes) -> {
////                    if (bytes != null) {
////                        System.out.println("Bytes : " + bytes.length);
////                        Files.write(path, bytes, StandardOpenOption.CREATE);
////                    } else {
////                        System.out.println("Пустой массив");
////                    }
////                });
////            } else {
////                System.out.println("Code : " + code);
////            }
////        });
//
//        // получить статус-код
////        Helpers.getBytes1(8087, "localhost", URL).subscribe((code) -> {
////            System.out.println("Code : " + code);
////        });
//
//    }

}