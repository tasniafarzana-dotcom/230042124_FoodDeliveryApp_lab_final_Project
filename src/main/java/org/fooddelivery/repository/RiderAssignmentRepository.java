package org.fooddelivery.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.fooddelivery.model.RiderAssignment;
import org.fooddelivery.util.SerializationUtils;

public class RiderAssignmentRepository implements IRiderAssignmentRepository {
    
    private static final String DATA_DIR = "data/assignments";
    private final Map<String, RiderAssignment> cache = new HashMap<>();

    public RiderAssignmentRepository() {
        ensureDataDir();
        loadFromFile();
    }

    @Override
    public void save(RiderAssignment assignment) {
        cache.put(assignment.getId(), assignment);
        saveToFile();
    }

    @Override
    public void delete(String id) {
        cache.remove(id);
        saveToFile();
    }

    @Override
    public Optional<RiderAssignment> findById(String id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public List<RiderAssignment> findByRiderIdAndStatus(String riderId, String status) {
        List<RiderAssignment> result = new ArrayList<>();
        for (RiderAssignment assignment : cache.values()) {
            if (assignment.getRiderId().equals(riderId) && 
                assignment.getStatus().equals(status)) {
                result.add(assignment);
            }
        }
        return result;
    }

    @Override
    public List<RiderAssignment> findByOrderId(String orderId) {
        List<RiderAssignment> result = new ArrayList<>();
        for (RiderAssignment assignment : cache.values()) {
            if (assignment.getOrderId().equals(orderId)) {
                result.add(assignment);
            }
        }
        return result;
    }

    private void ensureDataDir() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
        }
    }

    private void saveToFile() {
        try {
            String json = SerializationUtils.toJson(new ArrayList<>(cache.values()));
            Files.write(Paths.get(DATA_DIR, "assignments.json"), json.getBytes());
        } catch (IOException e) {
        }
    }

    private void loadFromFile() {
        try {
            Path file = Paths.get(DATA_DIR, "assignments.json");
            if (Files.exists(file)) {
                String json = new String(Files.readAllBytes(file));
                List<RiderAssignment> assignments = SerializationUtils.fromJsonList(json, RiderAssignment.class);
                for (RiderAssignment assignment : assignments) {
                    cache.put(assignment.getId(), assignment);
                }
            }
        } catch (IOException e) {
        }
    }
}