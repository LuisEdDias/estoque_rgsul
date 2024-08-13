package br.net.rgsul.estoque.repositories;

import br.net.rgsul.estoque.entities.Box;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoxRepository extends JpaRepository<Box, Integer> {
}
