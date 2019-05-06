import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.Callable;


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
        String url = "http://localhost:8087/untitled/resources/infoenergo.getnewcommands_test"; // 404
        String url1 = "http://localhost:8087/commandserv/resources/infoenergo.getnewcommands"; // 200
        String url2 = "http://square.github.io/okhttp/";


        Request request1 = new Request.Builder().url(url).build();


        Observable<Response> resp2 = Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> e) throws Exception {

                Response resp3 = client.newCall(request1).execute();
                if (resp3.code() == 200) {
                    e.onNext(resp3);
                    e.onComplete();
                } else {
                    e.onError(new Exception("Code not 200"));
                }
            }
        });



        Observable<String> s_resp = resp2.map(response -> response.body().toString());

        Disposable d = s_resp.subscribeWith(new DisposableObserver<String>() {
            @Override
            public void onStart() {
                System.out.println("Started");
            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onComplete() {
                dispose();
            }

            @Override
            public void onError(Throwable t) {
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

}