package siw_tornei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import siw_tornei.model.Giocatore;
import siw_tornei.service.GiocatoreService;
import siw_tornei.service.SquadraService;

@Controller
public class GiocatoreController {

    private GiocatoreService giocatoreService;
    private SquadraService squadraService;

    public GiocatoreController(GiocatoreService giocatoreService, SquadraService squadraService) {
        this.giocatoreService = giocatoreService;
        this. squadraService = squadraService;
    }

    @GetMapping("/giocatori")
    public String getGiocatori(Model model) {
        model.addAttribute("giocatori", giocatoreService.findAll());
        return "giocatore/list";
    }

    @GetMapping("/giocatori/{id}")
    public String getGiocatore(@PathVariable Long id, Model model) {
        model.addAttribute("giocatore", giocatoreService.findById(id));
        return "giocatore/show";
    }

   @GetMapping("/admin/giocatore/form")
public String formNewGiocatore(Model model) {

    model.addAttribute("giocatore", new Giocatore());
    model.addAttribute("squadre", squadraService.findAll());

    return "admin/giocatore/form";
}

    @PostMapping("/admin/giocatore")
    public String saveGiocatore(@ModelAttribute Giocatore giocatore) {
        giocatoreService.save(giocatore);
        return "redirect:/giocatori";
    }

   @GetMapping("/admin/giocatore/{id}/edit")
    public String formEditGiocatore(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "giocatore",
                giocatoreService.findById(id)
        );

        model.addAttribute(
                "squadre",
                squadraService.findAll()
        );

        return "admin/giocatore/form";
    }

    @PostMapping("/admin/giocatore/{id}/delete")
    public String deleteGiocatore(@PathVariable Long id) {
        giocatoreService.deleteById(id);
        return "redirect:/giocatori";
    }
}