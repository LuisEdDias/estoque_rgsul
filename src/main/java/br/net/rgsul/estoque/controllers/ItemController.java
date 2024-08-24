package br.net.rgsul.estoque.controllers;

import br.net.rgsul.estoque.dto.GetItemDTO;
import br.net.rgsul.estoque.dto.ItemDTO;
import br.net.rgsul.estoque.dto.UpdateItemDTO;
import br.net.rgsul.estoque.entities.ItemStatus;
import br.net.rgsul.estoque.services.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("item")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("{id}")
    public String getItem(@PathVariable int id, Model model) {
        model.addAttribute("item", itemService.getItem(id));
        model.addAttribute("movements", itemService.getItemMovements(id));
        return "views/item/item";
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("items", itemService.getAll());
        model.addAttribute("status", ItemStatus.values());
        return "views/item/list";
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
}
