package ru.yandex.kanban.servers.implemented;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * Постман: https://www.getpostman.com/collections/a83b61d9e1c81c10575c
 */
public class KVServer {
    private static final int PORT = 8078;
    private final String apiToken;
    private final HttpServer server;
    private final Map<String, String> data = new HashMap<>();

    public KVServer() throws IOException {
        apiToken = generateApiToken();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);
        System.out.println("KVServer запущен на порту " + PORT);
    }

    private void load(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/load");
            if (!hasAuth(h)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if (h.getRequestMethod().equals("GET")) {
                String key = h.getRequestURI().getPath().substring("/load/".length());
                if (key.isEmpty()) {
                    System.out.println("Ключ для сохранения пустой. Ключ указывается в пути: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                } else {
                    if (!data.containsKey(key)) {
                        System.out.println("Такого ключа не существует");
                        h.sendResponseHeaders(404, 0);
                        throw new RuntimeException();
                    } else {
                        String obj = data.get(key);
                        System.out.println("Значение для ключа " + key + " успешно возвращено!");
                        h.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = h.getResponseBody()) {
                            outputStream.write(obj.getBytes(UTF_8));
                        }
                    }
                }
            } else {
                System.out.println("/load ждет GET-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void save(HttpExchange h) throws IOException {
        try {
            System.out.println("\nKVServer: начал обрботку события /load");
            if (!hasAuth(h)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_KEY со значением апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }

            switch (h.getRequestMethod()) {
                case "GET":
                    System.out.println("\nKVServer: начал обрботку метода /GET");
                    String key = h.getRequestURI().getPath().substring("/load/".length());
                    if (key.isEmpty()) {
                        System.out.println("Key пустой. key указывается в пути: /load/{key}");
                        h.sendResponseHeaders(400, 0);
                        return;
                    }
                    String value = data.get(key);
                    if (value != null) {
                        sendText(h, value);
                        System.out.println("Значение ключа " + key + " успешно отправлено!");
                    } else {
                        System.out.println("Значение ключа " + key + " несуществует!");
                        h.sendResponseHeaders(400, 0);
                    }
                    break;
                default:
                    System.out.println("/load ждёт GET-запрос, а получил: " + h.getRequestMethod());
                    h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void register(HttpExchange h) throws IOException {
        try {
            System.out.println("\nKVServer: начал обрботку события /register");
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, apiToken);
            } else {
                System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    public void start() {
        System.out.println("Запускаем KV сервер на порту " + PORT);
        System.out.println("---------------------------------");
        server.start();
    }

    public void stop() {
        System.out.println("Сервер остановлен");
        System.out.println("---------------------------------");
        server.stop(0);
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}