package rw.ac.auca.lostandfound.controller;

import rw.ac.auca.lostandfound.model.ActivityLog;
import rw.ac.auca.lostandfound.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/logs")
public class ActivityLogController {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @GetMapping
    public List<ActivityLog> getAllLogs() {
        return activityLogRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(ActivityLog::getTimestamp).reversed())
                .collect(Collectors.toList());
    }
}