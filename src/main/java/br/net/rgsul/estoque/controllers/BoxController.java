package br.net.rgsul.estoque.controllers;

import br.net.rgsul.estoque.dto.BoxCheckDTO;
import br.net.rgsul.estoque.dto.BoxDTO;
import br.net.rgsul.estoque.dto.GetBoxCheckDTO;
import br.net.rgsul.estoque.dto.GetBoxDTO;
import br.net.rgsul.estoque.entities.BoxStatus;
import br.net.rgsul.estoque.entities.ItemStatus;
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
        model.addAttribute("boxStatus", BoxStatus.values());
        model.addAttribute("boxes", boxService.findAll());
        return "views/box/index";
    }

    @GetMapping("{id}")
    public String getById(@PathVariable Integer id, Model model){
        model.addAttribute("items", boxService.findAllItems(id));
        model.addAttribute("box", boxService.getBoxById(id));
        model.addAttribute("itemStatus", ItemStatus.values());
        model.addAttribute("boxStatus", BoxStatus.values());
        return "views/box/box";
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

    @DeleteMapping("{id}")
    public String delete(@PathVariable Integer id){
        boxService.delete(id);
        return "redirect:/box";
    }
}
