package br.net.rgsul.estoque.repositories;

import br.net.rgsul.estoque.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
