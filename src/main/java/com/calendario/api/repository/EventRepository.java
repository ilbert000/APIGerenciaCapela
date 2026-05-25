package com.calendario.api.repository;

import com.calendario.api.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Métodos customizados de query podem ser adicionados aqui se necessário
}