package org.fooddelivery.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;

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
}