package rw.ac.auca.lostandfound.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rw.ac.auca.lostandfound.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}