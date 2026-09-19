package rw.ac.auca.lostandfound.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rw.ac.auca.lostandfound.model.Claim;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
}