package ru.yandex.kanban.servers;

import ru.yandex.kanban.servers.implemented.KVServer;

import java.io.IOException;

public class ServerUtil {

    public static void main(String[] args) throws IOException {
        new KVServer().start();
    }
}
