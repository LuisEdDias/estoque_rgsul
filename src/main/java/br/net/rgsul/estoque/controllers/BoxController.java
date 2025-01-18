package br.net.rgsul.estoque.controllers;

import br.net.rgsul.estoque.dto.*;
import br.net.rgsul.estoque.entities.BoxFileDownload;
import br.net.rgsul.estoque.entities.BoxStatus;
import br.net.rgsul.estoque.entities.ItemStatus;
import br.net.rgsul.estoque.entities.Warehouse;
import br.net.rgsul.estoque.services.BoxService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@Controller
@RequestMapping("box")
public class BoxController {
    private final BoxService boxService;

    public BoxController(BoxService boxService) {
        this.boxService = boxService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("boxStatus", BoxStatus.values());
        model.addAttribute("warehouse", Warehouse.values());
        model.addAttribute("boxes", boxService.findAll());
        return "views/box/index";
    }

    @GetMapping("{id}")
    public String getById(@PathVariable Integer id, Model model){
        model.addAttribute("items", boxService.findAllItems(id));
        model.addAttribute("box", boxService.getBoxById(id));
        model.addAttribute("itemStatus", ItemStatus.values());
        model.addAttribute("boxStatus", BoxStatus.values());
        model.addAttribute("warehouse", Warehouse.values());
        return "views/box/box";
    }

    @GetMapping("{id}/export")
    public ResponseEntity<Resource> export(@PathVariable Integer id){
        boxService.exportBox(id);
        File file = BoxFileDownload.file;

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);
        String fileName = file.getName();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @PostMapping
    public String create(BoxDTO boxDTO){
        boxService.save(boxDTO);
        return "redirect:/box/" + boxDTO.id();
    }

    @PostMapping("{id}/check")
    public ResponseEntity<GetBoxCheckDTO> check(@PathVariable Integer id, @RequestBody BoxCheckDTO boxCheckDTO){
        return ResponseEntity.ok(boxService.boxCheck(id, boxCheckDTO));
    }

    @PutMapping("{id}")
    public ResponseEntity<GetBoxDTO> update(@PathVariable Integer id, @RequestBody BoxDTO boxDTO){
        return ResponseEntity.ok(boxService.update(id, boxDTO));
    }

    @PutMapping("{id}/move")
    public ResponseEntity<GetBoxDTO> move(@PathVariable Integer id, @RequestBody WarehouseDTO warehouseDTO){
        return ResponseEntity.ok(boxService.move(id, warehouseDTO));
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable Integer id){
        boxService.delete(id);
        return "redirect:/box";
    }
}
