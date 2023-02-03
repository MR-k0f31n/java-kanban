package ru.yandex.kanban.servers.implemented;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final String apiToken;
    private final HttpClient httpClient;

    public KVTaskClient() throws IOException, InterruptedException {
        httpClient = HttpClient.newHttpClient();
        apiToken = reg();
        System.out.println("Присвоент токен: " + apiToken);
    }

    public String reg() throws IOException, InterruptedException {
        System.out.println("\nKVTaskClient: Начало обработки события /register клиента.");
        URI register = URI.create("http://localhost:8078" + "/register");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(register)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            System.out.println("\nKVTaskClient: Событие /register прошло успешно");
        }

        return response.body();
    }

    public void put(String key, String json) {
        System.out.println("\nKVTaskClient: Начало обработки события /save клиента.");
        URI put = URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + apiToken);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(put)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .build();

        try {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            System.out.println("\nKVTaskClient: Код состояния PUT метода: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка." +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
            System.out.println("\n" + e.getMessage());
        }
    }

    public String load(String key) {
        System.out.println("\nKVTaskClient: Начало обработки события /load клиента.");
        URI load = URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + apiToken);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(load)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .build();

        try {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            System.out.println("\nKVTaskClient: Код состояния Load метода: " + response.statusCode());
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка." +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
            System.out.println("\n" + e.getMessage());
            return e.getMessage();
        }
    }
}
