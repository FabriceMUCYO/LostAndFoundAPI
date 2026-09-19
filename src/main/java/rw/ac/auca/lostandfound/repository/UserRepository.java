package rw.ac.auca.lostandfound.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rw.ac.auca.lostandfound.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}