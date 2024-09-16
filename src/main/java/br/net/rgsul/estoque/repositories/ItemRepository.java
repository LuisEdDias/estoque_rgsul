package br.net.rgsul.estoque.repositories;

import br.net.rgsul.estoque.entities.Box;
import br.net.rgsul.estoque.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByBox(Box box);
    List<Item> findAllBySaved(Boolean saved);
}
