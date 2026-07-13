package siw_tornei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import siw_tornei.model.Partita;
import siw_tornei.service.ArbitroService;
import siw_tornei.service.PartitaService;
import siw_tornei.service.SquadraService;
import siw_tornei.service.TorneoService;

@Controller
public class PartitaController {

    private PartitaService partitaService;
    private TorneoService torneoService;
    private SquadraService squadraService;
    private ArbitroService arbitroService;

    public PartitaController(
            PartitaService partitaService,
            TorneoService torneoService,
            SquadraService squadraService,
            ArbitroService arbitroService) {

        this.partitaService = partitaService;
        this.torneoService = torneoService;
        this.squadraService = squadraService;
        this.arbitroService = arbitroService;
    }

    /* Pagine pubbliche */

    @GetMapping("/partite")
    public String getPartite(Model model) {

        model.addAttribute(
                "partite",
                partitaService.findAll()
        );

        return "partita/list";
    }

    @GetMapping("/partite/{id}")
    public String getPartita(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "partita",
                partitaService.findById(id)
        );

        return "partita/show";
    }

    /* Form per nuova partita */

    @GetMapping("/admin/partita/form")
    public String formNewPartita(Model model) {

        model.addAttribute(
                "partita",
                new Partita()
        );

        caricaDatiForm(model);

        return "admin/partita/form";
    }

    /* Salvataggio o modifica */

    @PostMapping("/admin/partita")
    public String savePartita(
            @ModelAttribute("partita") Partita partita,
            Model model) {

        try {

            partitaService.save(partita);

            return "redirect:/partite";

        } catch (IllegalArgumentException exception) {

            model.addAttribute(
                    "errore",
                    exception.getMessage()
            );

            caricaDatiForm(model);

            return "admin/partita/form";
        }
    }

    /* Form modifica partita */

    @GetMapping("/admin/partita/{id}/edit")
    public String formEditPartita(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "partita",
                partitaService.findById(id)
        );

        caricaDatiForm(model);

        return "admin/partita/form";
    }

    /* Eliminazione */

    @PostMapping("/admin/partita/{id}/delete")
    public String deletePartita(@PathVariable Long id, Model model) {

        try {

            partitaService.deleteById(id);

            return "redirect:/partite";

        } catch (IllegalStateException exception) {

            model.addAttribute("errore", exception.getMessage());
            model.addAttribute("partite", partitaService.findAll());

            return "partita/list";
        }
    }

    /*
     * Metodo privato usato per non ripetere ogni volta
     * gli stessi addAttribute.
     */

    private void caricaDatiForm(Model model) {

        model.addAttribute(
                "tornei",
                torneoService.findAll()
        );

        model.addAttribute(
                "squadre",
                squadraService.findAll()
        );

        model.addAttribute(
                "arbitri",
                arbitroService.findAll()
        );
    }
}