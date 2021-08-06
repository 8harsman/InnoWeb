package de.basecamp.innoWeb.controller;

import de.basecamp.innoWeb.materials.User;
import de.basecamp.innoWeb.services.SecurityService;
import de.basecamp.innoWeb.services.UserService;
import de.basecamp.innoWeb.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @GetMapping("/7f7c2667b88a979e8cb91234da70f6ecc51a54d9a9a0541f4a27d5cf83a3ee41")
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/7f7c2667b88a979e8cb91234da70f6ecc51a54d9a9a0541f4a27d5cf83a3ee41")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        if (error != null)
            model.addAttribute("error", "Benutzername oder Passwort sind ungültig.");

        if (logout != null)
            model.addAttribute("message", "Sie wurden erfolgreich abgemeldet.");

        return "login";
    }
}