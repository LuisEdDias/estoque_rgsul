package br.net.rgsul.estoque.repositories;

import br.net.rgsul.estoque.entities.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findAllByItemId(int id);
}
