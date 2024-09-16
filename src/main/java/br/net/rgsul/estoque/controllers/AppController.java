package br.net.rgsul.estoque.controllers;

import br.net.rgsul.estoque.entities.BoxStatus;
import br.net.rgsul.estoque.services.BoxService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class AppController {
    @GetMapping
    public String index(Model model) {
        model.addAttribute("boxStatus", BoxStatus.values());
        return "views/index";
    }
}
