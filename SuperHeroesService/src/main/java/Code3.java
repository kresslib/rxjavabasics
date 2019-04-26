import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.client.HttpResponse;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class Code3 {

    public static void main(String[] args) {

        Path path = Paths.get("C:/Temp/TempFile.db");
        String URL = "/commandserv/resources/infoenergo.getnewcommands";

        Helpers.getBytes1(8087, "localhost", URL).subscribe((code) -> {
            if (code == 200) {
                Helpers.getBytes(8087, "localhost", URL).subscribe((bytes) -> {
                    if (bytes != null) {
                        System.out.println("Bytes : " + bytes.length);
                        Files.write(path, bytes, StandardOpenOption.CREATE);
                    } else {
                        System.out.println("Пустой массив");
                    }
                });
            } else {
                System.out.println("Code : " + code);
            }
        });


        // получить статус-код
//        Helpers.getBytes1(8087, "localhost", URL).subscribe((code) -> {
//            System.out.println("Code : " + code);
//        });

    }

}
