package siw_tornei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import siw_tornei.model.Utente;
import siw_tornei.service.UtenteService;

@Controller
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/utenti")
    public String getUtenti(Model model) {
        model.addAttribute("utenti", utenteService.findAll());
        return "utenti";
    }

    @GetMapping("/utenti/{id}")
    public String getUtente(@PathVariable Long id, Model model) {
        model.addAttribute("utente", utenteService.findById(id));
        return "utente";
    }

    @GetMapping("/admin/utente/form")
    public String formNewUtente(Model model) {
        model.addAttribute("utente", new Utente());
        return "admin/formUtente";
    }

    @GetMapping("/admin/utente/{id}/edit")
    public String formEditUtente(@PathVariable Long id, Model model) {
        model.addAttribute("utente", utenteService.findById(id));
        return "admin/formUtente";
    }
}