package br.net.rgsul.estoque.controllers;

import br.net.rgsul.estoque.dto.BoxDTO;
import br.net.rgsul.estoque.services.BoxService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("box")
public class BoxController {
    private final BoxService boxService;

    public BoxController(BoxService boxService) {
        this.boxService = boxService;
    }

    @GetMapping
    public ResponseEntity<List<BoxDTO>> getAll(){
        return ResponseEntity.ok(boxService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<BoxDTO> getById(@PathVariable Integer id){
        return ResponseEntity.ok(boxService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BoxDTO> create(@RequestBody BoxDTO boxDTO){
        return ResponseEntity.ok(boxService.save(boxDTO));
    }

    @PutMapping("{id}")
    public ResponseEntity<BoxDTO> update(@PathVariable Integer id, @RequestBody BoxDTO boxDTO){
        return ResponseEntity.ok(boxService.update(id, boxDTO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id){
        boxService.delete(id);
        return ResponseEntity.ok("Caixa " + id + " com sucesso!");
    }
}
