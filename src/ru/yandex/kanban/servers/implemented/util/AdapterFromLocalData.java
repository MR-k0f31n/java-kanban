package ru.yandex.kanban.servers.implemented.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdapterFromLocalData extends TypeAdapter<LocalDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime == null) {
            jsonWriter.value(String.valueOf(localDateTime));
            return;
        }
        jsonWriter.value(localDateTime.format(formatter));
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        final String text = jsonReader.nextString();
        if (text.equals("null")) {
            return null;
        }
        return LocalDateTime.parse(text, formatter);
    }
}
