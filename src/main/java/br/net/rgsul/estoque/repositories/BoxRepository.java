package br.net.rgsul.estoque.repositories;

import br.net.rgsul.estoque.entities.Box;
import br.net.rgsul.estoque.entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoxRepository extends JpaRepository<Box, Integer> {
    List<Box> findAllByWarehouse(Warehouse warehouse);
}
