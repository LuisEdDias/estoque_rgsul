package br.net.rgsul.estoque.controllers;

import br.net.rgsul.estoque.dto.*;
import br.net.rgsul.estoque.entities.ItemStatus;
import br.net.rgsul.estoque.entities.Warehouse;
import br.net.rgsul.estoque.services.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        model.addAttribute("itemStatus", ItemStatus.values());
        model.addAttribute("warehouse", Warehouse.values());
        return "views/item/item";
    }

    @GetMapping("/estoque/{warehouse}")
    public String index(Model model, @PathVariable String warehouse) {
        Warehouse wh = Warehouse.valueOf(warehouse.toUpperCase());
        model.addAttribute("currentWarehouse", wh);
        model.addAttribute("items", itemService.getAllByWarehouse(wh));
        model.addAttribute("itemStatus", ItemStatus.values());
        model.addAttribute("warehouse", Warehouse.values());
        return "views/item/index";
    }

    @PostMapping
    public ResponseEntity<GetItemDTO> createItem(@RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(itemService.createItem(itemDTO));
    }

    @PostMapping("/file")
    public ResponseEntity<List<ItemDTO>> uploadFile(@ModelAttribute ItemsFileDTO itemsFileDTO) {
        return ResponseEntity.ok(itemService.createItemsByFile(itemsFileDTO));
    }

    @PutMapping("{id}")
    public ResponseEntity<GetItemDTO> updateItem(@PathVariable int id, @RequestBody UpdateItemDTO updateItemDTO) {
        return ResponseEntity.ok(itemService.updateItem(id, updateItemDTO));
    }

    @PutMapping("{id}/move")
    public ResponseEntity<GetItemDTO> movItem(@PathVariable int id, @RequestBody MovementDTO movementDTO) {
        return ResponseEntity.ok(itemService.movItem(id, movementDTO));
    }

    @PutMapping("move-all")
    public ResponseEntity<List<GetItemDTO>> movItems(@RequestBody MoveAllDTO moveAllDTO) {
        return ResponseEntity.ok(itemService.movItems(moveAllDTO));
    }

    @DeleteMapping("{id}")
    public String deleteItem(@PathVariable int id) {
        itemService.deleteItem(id);
        return "views/index";
    }
}
