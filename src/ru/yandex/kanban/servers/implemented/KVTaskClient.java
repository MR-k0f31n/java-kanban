package ru.yandex.kanban.servers.implemented;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String url;
    private String apiToken;
    private HttpClient httpClient;

    public KVTaskClient(String url) throws IOException, InterruptedException {
        this.url = url;
        httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(this.url + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString()
        );
        apiToken = response.body();
    }

    public void put(String key, String json) {
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, handler);
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка." +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key) {
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка." +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
            return null;
        }
    }
}
