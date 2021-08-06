package de.basecamp.innoWeb.controller;

import de.basecamp.innoWeb.dtos.InnovationDto;
import de.basecamp.innoWeb.materials.Ereignis;
import de.basecamp.innoWeb.materials.Innovation;
import de.basecamp.innoWeb.materials.Rating;
import de.basecamp.innoWeb.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Instant;
import java.util.Date;

@Controller
public class BewertungsController {

    InnovationService innovationService;
    UserService userService;
    EreignisService ereignisService;

    @Autowired
    public BewertungsController(InnovationService innovationService, UserService userService, EreignisService ereignisService) {
        this.innovationService = innovationService;
        this.userService = userService;
        this.ereignisService = ereignisService;
    }

    /**
     * Creates a new rating from the dto and saves the rating for the corresponding innovation
     * Also creates a new ereignis (event), which describes the new rating
     * @return Redirects to the details Mapping for the corresponding id
     * @param id id of the rated innovation
     * @param innovation dto of the rated innovation
     */
    @PostMapping("/postBewertung/{id}")
    public String postHome(@PathVariable Long id, InnovationDto innovation) {
        Rating bewertung = innovation.getBewertung();
        bewertung.setErstellungsdatum(Date.from(Instant.now()));
        bewertung.setErsteller(userService.getCurrentUser());
        innovationService.speicherBewertungFuerInnovation(id, bewertung);

        Innovation inno = Innovation.fromDto(innovation, userService.getCurrentUser());
        Ereignis ereignis = new Ereignis(userService.getCurrentUser(), inno, "BewertungNeu");
        ereignisService.saveEreignis(ereignis);

        return "redirect:/details/" + id;
    }

    /**
     * Gets the existing rating by the current user and the chosen innovation
     * @return the editRating.html
     * @param innoId Id of the innovation you want to change the rating for
     * @param model Holds the model attributes in the Spring Framework
     */
    @GetMapping("/editRating/{innoId}")
    String bewertungEditieren(@PathVariable Long innoId, Model model) {
        Innovation innovation = innovationService.getById(innoId);
        Rating currentUsersRating = innovationService.getCurrentUsersRating(userService.getCurrentUser().getUsername(), innoId);
        model.addAttribute("bewertung", currentUsersRating);
        model.addAttribute("in", InnovationDto.fromInnovation(innovation));
        model.addAttribute("id", innoId);
        return "editRating";
    }

    /**
     * Checks if the User has already rated the existing innovation or the number of ratings is already at max and returns the corresponding html
     * @return the details.html or ratingNachträglich.html
     * @param id Id of the innovation you want to rate
     * @param model Holds the model attributes in the Spring Framework
     */
    @GetMapping("/rating2/{id}")
    String nachträglichBewerten(@PathVariable Long id, Model model) {
        Innovation innovation = innovationService.getById(id);
        String aktuellerUser = userService.getCurrentUser().getUsername();
        if(innovationService.hatUserInnovationBereitsBewertet(aktuellerUser, innovation))
        {
            model.addAttribute("innovation", InnovationDto.fromInnovation(innovation));
            model.addAttribute("bereitsBewertet", innovationService.hatUserInnovationBereitsBewertet(aktuellerUser, innovation));
            model.addAttribute("maxBewertungenErreicht", innovation.maximaleAnzahlAnBewertungenErreicht());
            model.addAttribute("message", "Diese Innovation wurde bereits von dir bewertet");
            return "details";
        }
        if(innovation.maximaleAnzahlAnBewertungenErreicht())
        {
            model.addAttribute("innovation", InnovationDto.fromInnovation(innovation));
            model.addAttribute("bereitsBewertet", innovationService.hatUserInnovationBereitsBewertet(aktuellerUser, innovation));
            model.addAttribute("maxBewertungenErreicht", innovation.maximaleAnzahlAnBewertungenErreicht());
            model.addAttribute("message", "Maximale Anzahl an Bewertungen wurde bereits erreicht");
            return "details";
        }
        model.addAttribute("innovation", InnovationDto.fromInnovation(innovation));
        model.addAttribute("in", InnovationDto.fromInnovation(innovation));
        model.addAttribute("id", id);
        return "ratingNachträglich";
    }

    /**
     * Saves the edited rating for an innovation
     * @return Redirects to the details-mapping for the corresponding innovation
     * @param innoId Id of the innovation the user changed the rating for
     * @param bewertung Edited rating
     * @param model Holds the model attributes in the Spring Framework
     */
    @PostMapping("/postEditedRating/{innoId}")
    String postBewertungEditieren(@PathVariable Long innoId, Rating bewertung, Model model) {
        bewertung.setErstellungsdatum(Date.from(Instant.now()));
        bewertung.setErsteller(userService.getCurrentUser());
        innovationService.ersetzeBewertungVonNutzer(innoId, bewertung, userService.getCurrentUser().getUsername());
        return "redirect:/details/" + innoId;
    }

    /**
     * Deletes chosen rating of chosen innovation
     * @return multipleRatings.html
     * @param id id of the innovation of which the user wants to delete the rating
     * @param idrating id of the rating the user wants to delete
     * @param model Holds the model attributes in the Spring Framework
     */
    @GetMapping("/deleteRating/{id}/{idrating}")
    public String deleteRating(@PathVariable Long id, @PathVariable Long idrating, Model model) {
        innovationService.löscheBewertungFuerInnovation(id, idrating);

        Innovation innovation = innovationService.getById(id);
        model.addAttribute("innovation", InnovationDto.fromInnovation(innovation));
        return "multipleRatings";
    }

    /**
     * Checks if the User has already rated the newly created innovation or the number of ratings is already at max and returns the corresponding html
     * @return the details.html or rating.html
     * @param id Id of the innovation you want to rate
     * @param model Holds the model attributes in the Spring Framework
     */

    @GetMapping("/rating/{id}")
    String neuBewerten(@PathVariable Long id, Model model) {
        Innovation innovation = innovationService.getById(id);
        String aktuellerUser = userService.getCurrentUser().getUsername();
        if(innovationService.hatUserInnovationBereitsBewertet(aktuellerUser, innovation))
        {
            model.addAttribute("innovation", InnovationDto.fromInnovation(innovation));
            model.addAttribute("bereitsBewertet", innovationService.hatUserInnovationBereitsBewertet(aktuellerUser, innovation));
            model.addAttribute("maxBewertungenErreicht", innovation.maximaleAnzahlAnBewertungenErreicht());
            model.addAttribute("message", "Diese Innovation wurde bereits von dir bewertet");
            return "details";
        }
        if(innovation.maximaleAnzahlAnBewertungenErreicht())
        {
            model.addAttribute("innovation", InnovationDto.fromInnovation(innovation));
            model.addAttribute("bereitsBewertet", innovationService.hatUserInnovationBereitsBewertet(aktuellerUser, innovation));
            model.addAttribute("maxBewertungenErreicht", innovation.maximaleAnzahlAnBewertungenErreicht());
            model.addAttribute("message", "Maximale Anzahl an Bewertungen wurde bereits erreicht");
            return "details";
        }
        model.addAttribute("innovation", InnovationDto.fromInnovation(innovation));
        model.addAttribute("in", InnovationDto.fromInnovation(innovation));
        model.addAttribute("id", id);
        return "rating";
    }

    /**
     * Gets the innovation of which you want to see the ratings and adds it to the model
     * @return the multipleRatings.html
     * @param id Id of the innovation which ratings you want to see
     * @param model Holds the model attributes in the Spring Framework
     */
    @GetMapping("/multipleRatings/{id}")
    String bewertungenZeigen(@PathVariable Long id, Model model) {
        Innovation innovation = innovationService.getById(id);
        model.addAttribute("innovation", InnovationDto.fromInnovation(innovation));
        return "multipleRatings";
    }

}
