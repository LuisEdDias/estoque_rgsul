package br.net.rgsul.estoque.controllers;

import br.net.rgsul.estoque.dto.GetItemDTO;
import br.net.rgsul.estoque.dto.ItemDTO;
import br.net.rgsul.estoque.dto.MovementDTO;
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
        model.addAttribute("itemStatus", ItemStatus.values());
        return "views/item/index";
    }

    @PostMapping
    public ResponseEntity<GetItemDTO> createItem(@RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(itemService.createItem(itemDTO));
    }

    @PutMapping("{id}")
    public ResponseEntity<GetItemDTO> updateItem(@PathVariable int id, @RequestBody UpdateItemDTO updateItemDTO) {
        return ResponseEntity.ok(itemService.updateItem(id, updateItemDTO));
    }

    @PutMapping("{id}/mov")
    public ResponseEntity<GetItemDTO> movItem(@PathVariable int id, @RequestBody MovementDTO movementDTO) {
        return ResponseEntity.ok(itemService.movItem(id, movementDTO));
    }
}
