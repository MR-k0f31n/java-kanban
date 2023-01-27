package ru.yandex.kanban.serverHTTP;

import ru.yandex.kanban.serverHTTP.implemented.KVServer;

import java.io.IOException;

public class ServerUtil {

    public static void main(String[] args) throws IOException {
        new KVServer().start();
    }
}
