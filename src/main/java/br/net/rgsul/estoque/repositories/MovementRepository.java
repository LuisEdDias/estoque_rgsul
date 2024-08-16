package br.net.rgsul.estoque.repositories;

import br.net.rgsul.estoque.entities.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    Optional<Movement> findByItemId(int itemId);
}
