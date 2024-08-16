package br.net.rgsul.estoque.controllers;

import br.net.rgsul.estoque.dto.GetItemDTO;
import br.net.rgsul.estoque.dto.ItemDTO;
import br.net.rgsul.estoque.dto.MovementsDTO;
import br.net.rgsul.estoque.dto.UpdateItemDTO;
import br.net.rgsul.estoque.services.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("item")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("{id}")
    public ResponseEntity<GetItemDTO> getItem(@PathVariable int id) {
        return ResponseEntity.ok(itemService.getItem(id));
    }

    @GetMapping
    public ResponseEntity<List<GetItemDTO>> getAll() {
        return ResponseEntity.ok(itemService.getAll());
    }

    @PostMapping
    public ResponseEntity<GetItemDTO> createItem(@RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(itemService.createItem(itemDTO));
    }

    @PutMapping("{id}")
    public ResponseEntity<GetItemDTO> updateItem(@PathVariable int id, @RequestBody UpdateItemDTO updateItemDTO) {
        return ResponseEntity.ok(itemService.updateItem(id, updateItemDTO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteItem(@PathVariable int id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok("Item deleted");
    }

    @GetMapping("movements/{id}")
    public ResponseEntity<MovementsDTO> getMovements(@PathVariable int id) {
        return ResponseEntity.ok(itemService.getMovements(id));
    }
}
