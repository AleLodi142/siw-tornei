package siw_tornei.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import siw_tornei.model.Giocatore;
import siw_tornei.service.GiocatoreService;
import siw_tornei.service.SquadraService;

@Controller
public class GiocatoreController {

    private GiocatoreService giocatoreService;
    private SquadraService squadraService;

    public GiocatoreController(
            GiocatoreService giocatoreService,
            SquadraService squadraService) {

        this.giocatoreService = giocatoreService;
        this.squadraService = squadraService;
    }

    @GetMapping("/giocatori")
    public String getGiocatori(
            @RequestParam(defaultValue = "") String testo,
            @RequestParam(defaultValue = "") String ruolo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        if (page < 0) {
            page = 0;
        }

        if (size < 1) {
            size = 5;
        }


        testo = testo.trim();
        ruolo = ruolo.trim();

        PageRequest pageRequest =
                PageRequest.of(page, size);

        Page<Giocatore> paginaGiocatori =
                giocatoreService.cercaConFiltri(
                        testo,
                        ruolo,
                        pageRequest
                );

        if (paginaGiocatori.getTotalPages() > 0
                && page >= paginaGiocatori.getTotalPages()) {

            int ultimaPagina =
                    paginaGiocatori.getTotalPages() - 1;

            return "redirect:/giocatori"
                    + "?testo=" + testo
                    + "&ruolo=" + ruolo
                    + "&page=" + ultimaPagina
                    + "&size=" + size;
        }

        model.addAttribute(
                "paginaGiocatori",
                paginaGiocatori
        );

        model.addAttribute(
                "testo",
                testo
        );

        model.addAttribute(
                "ruoloSelezionato",
                ruolo
        );

        model.addAttribute(
                "ruoli",
                giocatoreService.findRuoliDistinti()
        );

        return "giocatore/list";
    }

    @GetMapping("/giocatori/{id}")
    public String getGiocatore(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "giocatore",
                giocatoreService.findById(id)
        );

        return "giocatore/show";
    }

    @GetMapping("/admin/giocatore/form")
    public String formNewGiocatore(Model model) {

        model.addAttribute(
                "giocatore",
                new Giocatore()
        );

        model.addAttribute(
                "squadre",
                squadraService.findAll()
        );

        return "admin/giocatore/form";
    }

    @PostMapping("/admin/giocatore")
    public String saveGiocatore(
            @ModelAttribute Giocatore giocatore) {

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
    public String deleteGiocatore(
            @PathVariable Long id) {

        giocatoreService.deleteById(id);

        return "redirect:/giocatori";
    }
}