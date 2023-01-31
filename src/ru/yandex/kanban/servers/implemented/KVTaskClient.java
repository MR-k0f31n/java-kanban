package ru.yandex.kanban.servers.implemented;

import com.google.gson.JsonArray;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private String apiToken;
    private final HttpClient httpClient;

    public KVTaskClient(String url) throws IOException, InterruptedException {
        this.url = url;
        httpClient = HttpClient.newHttpClient();
        register();
    }

    public void register()  throws IOException, InterruptedException {
        System.out.println("Началась обработка события /register клиента.");
        URI uri = URI.create(this.url + "/register");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        apiToken = response.body();
    }

    public void put(String key, String json) {
        System.out.println("Началась обработка события /save клиента.");
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            System.out.println("Код состояния: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка." +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key) {
        System.out.println("Началась обработка события /load клиента.");
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            System.out.println("Код состояния: " + response.statusCode());
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка." +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
            return "Во время выполнения запроса возникла ошибка.";
        }
    }
}
