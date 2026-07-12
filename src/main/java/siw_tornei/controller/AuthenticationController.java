package siw_tornei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import siw_tornei.model.Utente;
import siw_tornei.service.UtenteService;

@Controller
public class AuthenticationController {

    private UtenteService utenteService;

    public AuthenticationController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "authentication/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("utente", new Utente());
        return "authentication/register";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("utente") Utente utente,
            Model model) {

        if (utenteService.existsByUsername(utente.getUsername())) {
            model.addAttribute(
                    "usernameError",
                    "Username già utilizzato"
            );

            return "authentication/register";
        }

        utenteService.registraUtente(utente);

        return "authentication/registrationSuccessful";
    }

    @GetMapping("/success")
    public String loginSuccess() {
        return "redirect:/";
    }
}