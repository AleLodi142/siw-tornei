package siw_tornei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import siw_tornei.model.Squadra;
import siw_tornei.service.SquadraService;

@Controller
public class SquadraController {

    private SquadraService squadraService;

    public SquadraController(SquadraService squadraService) {
        this.squadraService = squadraService;
    }

    @GetMapping("/squadre")
    public String getSquadre(Model model) {
        model.addAttribute("squadre", squadraService.findAll());
        return "squadra/list";
    }

    @GetMapping("/squadre/{id}")
    public String getSquadra(@PathVariable Long id, Model model) {
        model.addAttribute("squadra", squadraService.findById(id));
        return "squadra/show";
    }

    @GetMapping("/admin/squadra/form")
    public String formNewSquadra(Model model) {
        model.addAttribute("squadra", new Squadra());
        return "admin/squadra/form";
    }

    @PostMapping("/admin/squadra")
    public String saveSquadra(@ModelAttribute Squadra squadra) {
        squadraService.save(squadra);
        return "redirect:/squadre";
    }

    @GetMapping("/admin/squadra/{id}/edit")
    public String formEditSquadra(@PathVariable Long id, Model model) {
        model.addAttribute("squadra", squadraService.findById(id));
        return "admin/squadra/form";
    }

    @PostMapping("/admin/squadra/{id}/delete")
    public String deleteSquadra(@PathVariable Long id,Model model) {

        try {

            squadraService.deleteById(id);

            return "redirect:/squadre";

        } catch (IllegalStateException exception) {

            model.addAttribute("errore",exception.getMessage());
            model.addAttribute("squadre", squadraService.findAll());

            return "squadra/list";
        }
    }
}