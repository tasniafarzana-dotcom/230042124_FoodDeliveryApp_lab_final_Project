package org.fooddelivery.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class SerializationUtils {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static <T> void saveToFile(T data, String filePath) {
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
            try (Writer writer = new FileWriter(filePath)) {
                gson.toJson(data, writer);
            }
        } catch (IOException e) {
            System.out.println("Save error: " + e.getMessage());
        }
    }

    public static <T> T loadFromFile(String filePath, Type type) {
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            System.out.println("Load error: " + e.getMessage());
            return null;
        }
    }

    public static String toJson(Object data) {
        return gson.toJson(data);
    }

    
    public static <T> List<T> fromJsonList(String json, Class<T> clazz) {
        Type type = TypeToken.getParameterized(List.class, clazz).getType();
        return gson.fromJson(json, type);
    }
}