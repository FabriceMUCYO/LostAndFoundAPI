package rw.ac.auca.lostandfound.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import rw.ac.auca.lostandfound.model.ActivityLog;

public interface ActivityLogRepository extends MongoRepository<ActivityLog, String> {
}