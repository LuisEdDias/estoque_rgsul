package br.net.rgsul.estoque.repositories;

import br.net.rgsul.estoque.entities.Box;
import br.net.rgsul.estoque.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByBox(Box box);
    List<Item> findAllBySaved(Boolean saved);

    @Modifying
    @Query("update Item i set i.box = null, i.saved = false where i.box = :box")
    void updateItemsFromDeletedBox(@Param(value = "box") Box box);
}
