package siw_tornei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import siw_tornei.model.Arbitro;
import siw_tornei.service.ArbitroService;

@Controller
public class ArbitroController {

    private final ArbitroService arbitroService;

    public ArbitroController(ArbitroService arbitroService) {
        this.arbitroService = arbitroService;
    }

    @GetMapping("/arbitri")
    public String getArbitri(Model model) {
        model.addAttribute("arbitri", arbitroService.findAll());
        return "arbitro/list";
    }

    @GetMapping("/arbitri/{id}")
    public String getArbitro(@PathVariable Long id, Model model) {
        model.addAttribute("arbitro", arbitroService.findById(id));
        return "arbitro/show";
    }

    @GetMapping("/admin/arbitro/form")
    public String formNewArbitro(Model model) {
        model.addAttribute("arbitro", new Arbitro());
        return "admin/arbitro/form";
    }

   @PostMapping("/admin/arbitro")
    public String saveArbitro(
            @ModelAttribute("arbitro") Arbitro arbitro,
            Model model) {

        try {
            arbitroService.save(arbitro);
            return "redirect:/arbitri";

        } catch (IllegalArgumentException exception) {
            model.addAttribute("errore", exception.getMessage());
            return "admin/arbitro/form";
        }
    }

    @GetMapping("/admin/arbitro/{id}/edit")
    public String formEditArbitro(@PathVariable Long id, Model model) {
        model.addAttribute("arbitro", arbitroService.findById(id));
        return "admin/arbitro/form";
    }

    @PostMapping("/admin/arbitro/{id}/delete")
    public String deleteArbitro(@PathVariable Long id, Model model) {

        try {

            arbitroService.deleteById(id);

            return "redirect:/arbitri";

        } catch (IllegalStateException exception) {

            model.addAttribute("errore", exception.getMessage());
            model.addAttribute("arbitri", arbitroService.findAll());

            return "arbitro/list";
        }
    }
}