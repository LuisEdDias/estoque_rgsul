package br.net.rgsul.estoque.controllers;

import br.net.rgsul.estoque.dto.BoxDTO;
import br.net.rgsul.estoque.dto.GetBoxDTO;
import br.net.rgsul.estoque.services.BoxService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("box")
public class BoxController {
    private final BoxService boxService;

    public BoxController(BoxService boxService) {
        this.boxService = boxService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("boxes", boxService.findAll());
        return "views/box/index";
    }

    @GetMapping("{id}")
    public String getById(@PathVariable Integer id, Model model){
        model.addAttribute("box", boxService.findAllItems(id));
        return "views/box/box";
    }

    @GetMapping("new-box")
    public String newBox(Model model){
        return "views/box/new-box";
    }

    @PostMapping
    public String create(BoxDTO boxDTO){
        boxService.save(boxDTO);
        return "redirect:/box";
    }

    @PutMapping("{id}")
    public ResponseEntity<GetBoxDTO> update(@PathVariable Integer id, @RequestBody BoxDTO boxDTO){
        return ResponseEntity.ok(boxService.update(id, boxDTO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id){
        boxService.delete(id);
        return ResponseEntity.ok("Caixa " + id + " com sucesso!");
    }
}
