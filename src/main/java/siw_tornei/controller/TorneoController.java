package siw_tornei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import siw_tornei.model.Torneo;
import siw_tornei.service.ClassificaService;
import siw_tornei.service.PartitaService;
import siw_tornei.service.SquadraService;
import siw_tornei.service.TorneoService;

@Controller
public class TorneoController {

    private TorneoService torneoService;
    private PartitaService partitaService;
    private ClassificaService classificaService;
    private SquadraService squadraService;

    public TorneoController(
            TorneoService torneoService,
            PartitaService partitaService,
            ClassificaService classificaService,
            SquadraService squadraService) {

        this.torneoService = torneoService;
        this.partitaService = partitaService;
        this.classificaService = classificaService;
        this.squadraService = squadraService;
    }

    @GetMapping("/tornei")
    public String getTornei(Model model) {
        model.addAttribute("tornei", torneoService.findAll());
        return "torneo/list";
    }

    @GetMapping("/tornei/{id}")
public String getTorneo(
        @PathVariable Long id, Model model) {
        model.addAttribute("torneo", torneoService.findByIdWithSquadre(id));
        return "torneo/show";
    }

    @GetMapping("/tornei/{id}/calendario")
    public String getCalendario(@PathVariable Long id, Model model) {
        model.addAttribute("torneo", torneoService.findById(id));
        model.addAttribute(
                "partite",
                partitaService.findByTorneoIdOrderByDataOra(id)
        );

        return "torneo/calendario";
    }

    @GetMapping("/tornei/{id}/classifica")
    public String getClassifica(@PathVariable Long id, Model model) {
        model.addAttribute("torneo", torneoService.findById(id));
        model.addAttribute("classifica",classificaService.calcolaClassifica(id));

        return "torneo/classifica";
    }

    @GetMapping("/admin/torneo/form")
    public String formNewTorneo(Model model) {

        model.addAttribute("torneo", new Torneo());
        model.addAttribute("squadre", squadraService.findAll());

        return "admin/torneo/form";
    }

    @PostMapping("/admin/torneo")
    public String saveTorneo(
            @ModelAttribute("torneo") Torneo torneo) {

        torneoService.save(torneo);

        return "redirect:/tornei";
    }

    @GetMapping("/admin/torneo/{id}/edit")
    public String formEditTorneo(@PathVariable Long id, Model model) {

        model.addAttribute("torneo", torneoService.findById(id));
        model.addAttribute("squadre", squadraService.findAll());

        return "admin/torneo/form";
    }

    @PostMapping("/admin/torneo/{id}/delete")
    public String deleteTorneo(@PathVariable Long id, Model model) {

        try {

            torneoService.deleteById(id);

            return "redirect:/tornei";

        } catch (IllegalStateException exception) {

            model.addAttribute("errore", exception.getMessage());
            model.addAttribute("tornei", torneoService.findAll());

            return "torneo/list";
        }
    }
}