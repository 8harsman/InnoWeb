package de.basecamp.innoWeb.controller;
import de.basecamp.innoWeb.dtos.InnovationDto;
import de.basecamp.innoWeb.materials.*;
import de.basecamp.innoWeb.services.EreignisService;
import de.basecamp.innoWeb.services.InnovationService;
import de.basecamp.innoWeb.services.DateinanhangService;
import de.basecamp.innoWeb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Sets the controller mapping (page-navigation) for the Spring framework and controls the data between the html, the entities (materials), the services and the repositories.
 */

@Controller
public class InnovationController {

    InnovationService innovationService;
    DateinanhangService dateinanhangService;
    UserService userService;
    EreignisService ereignisService;


    @Autowired
    public InnovationController(InnovationService innovationService, UserService userService, DateinanhangService anhangservice, EreignisService ereignisService) {

        this.innovationService = innovationService;
        this.userService = userService;
        this.dateinanhangService = anhangservice;
        this.ereignisService = ereignisService;
    }

    /**
     * Gets all innovations, converts them to dtos and adds them to the model
     * Gets all ereignisse and adds them to the model
     * @return the "Home"-page (index.html)
     * @param model Holds the model attributes in the Spring Framework
     */
    @GetMapping("/")
    String home(Model model) {
        List<Innovation> innovationen = innovationService.listAll();
        List<InnovationDto> dtos = innovationen.stream().map(InnovationDto::fromInnovation).collect(Collectors.toList());
        List<Ereignis> ereignisse = ereignisService.getAllEreignisse();
        model.addAttribute("innovationen", dtos);
        model.addAttribute("ereignisse", ereignisse);
        System.out.println(ereignisse);
        return "index";
    }

    /**
     * Adds a new innovation-dto to the model
     * @return the hinzufuegen.html
     * @param model Holds the model attributes in the Spring Framework
     */
    @GetMapping("/hinzufuegen")
    String hinzufügen(Model model) {
        model.addAttribute("innovation",new InnovationDto());
        return "hinzufuegen";
    }

    /**
     * Adds a innovation-dto for the corresponding id to the model
     * Adds a boolean if the user has already rated the innovation to the model
     * Adds a boolean value if the max number of ratings is reached to the model
     * @return the details.html
     * @param model Holds the model attributes in the Spring Framework
     */
    @GetMapping("/details/{id}")
    String details(@PathVariable Long id, Model model) {
        Innovation innovation = innovationService.getById(id);

        User user = userService.getCurrentUser();
        model.addAttribute("innovation", InnovationDto.fromInnovation(innovation));
        model.addAttribute("bereitsBewertet", innovationService.hatUserInnovationBereitsBewertet(user.getUsername(), innovation));
        model.addAttribute("maxBewertungenErreicht", innovation.maximaleAnzahlAnBewertungenErreicht());
        return "details";
    }



    /**
     * Saves a newly created innovation
     * @return the hinzufuegen.html or rating.html or redirects to the hinzufuegen-mapping or redirects to the details mapping
     * @param innovationDto innovation dto to create the a new innovation
     * @param bindingResult
     * @param action User Input, which describes if the new innovation should be rated or not
     * @param file optional file attachment
     * @param attributes Used to show messages
     * @param model Holds the model attributes in the Spring Framework
     */
    @PostMapping("/postInnovation")
    public String postInnovation(@Valid @ModelAttribute("innovation") InnovationDto innovationDto, BindingResult bindingResult, @RequestParam("action") String action, @RequestParam("file") MultipartFile file, RedirectAttributes attributes, Model model) {
        if(bindingResult.hasErrors()) {
            return "hinzufuegen";
        }
        Innovation innovation = Innovation.fromDto(innovationDto, userService.getCurrentUser());
        innovation.setErstellungsdatum(Date.from(Instant.now()));
        Long id = innovationService.saveInnovationAndGetId(innovation);
        model.addAttribute("in", innovationDto);
        model.addAttribute("id", id);

        // check if file is empty
        if (file.isEmpty()) {

            Ereignis ereignis = new Ereignis(userService.getCurrentUser(), innovation, "InnovationNeu");
            ereignisService.saveEreignis(ereignis);

            if(action.equals("bewerten"))
            {
                String message = "Innovation erfolgreich angelegt!";
                attributes.addFlashAttribute("message", message);

                return "rating";
            }
            else if(action.equals("nicht bewerten"))
            {
                String message = "Innovation erfolgreich angelegt!";
                attributes.addFlashAttribute("message", message);
                return "redirect:/details/" + id;
            }
            else
            {
                return "hinzufuegen";
            }

        }

        String message;
        try {
            dateinanhangService.store(file, innovationService.getById(id), userService.getCurrentUser());


            Ereignis ereignis = new Ereignis(userService.getCurrentUser(), innovation, "InnovationNeu");
            ereignisService.saveEreignis(ereignis);

            message = "Datei erfolgreich hochgeladen: " + file.getOriginalFilename();
            attributes.addFlashAttribute("message2", message);

            message = "Innovation erfolgreich angelegt!";
            attributes.addFlashAttribute("message", message);


            if(action.equals("bewerten"))
            {
                return "rating";
            }
            else if(action.equals("nicht bewerten"))
            {

                return "redirect:/details/" + id;
            }
            else
            {
                return "hinzufuegen";
            }

        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "! " + e.toString();
            attributes.addFlashAttribute("message", message);
            System.out.println(e.toString());

            return "redirect:/hinzufuegen";
        }

    }


    /**
     * Deletes chosen innovation by id
     * @return Redirects to the /-mapping
     * @param id id of the innovation to delete
     * @param model Holds the model attributes in the Spring Framework
     */
    @GetMapping("/deleteInnovation/{id}")
    public String deleteInnovation(@PathVariable Long id, Model model) {
        innovationService.löscheInnovation(id);

        List<Innovation> innovationen = innovationService.listAll();
        List<InnovationDto> dtos = innovationen.stream().map(InnovationDto::fromInnovation).collect(Collectors.toList());
        model.addAttribute("innovationen", dtos);
        return "redirect:/";
    }



    /**
     * Creates a dto from a existing innovation and adds it to the model
     * @return the editInnovation.html
     * @param id Id of the innovation which the user wants to edit
     * @param model Holds the model attributes in the Spring Framework
     */
    @GetMapping("/editInnovation/{id}")
    public String editInnovation(@PathVariable Long id, Model model) {
        InnovationDto innovation = InnovationDto.fromInnovation(innovationService.getById(id));
        model.addAttribute("innovation", innovation);
        model.addAttribute("id", id);
        return "editInnovation";
    }


    /**
     * Saves an edited innovation with edited attributes from the dto
     * @return the details.html or the hinzufuegen.html
     * @param id Id of the innovation which the user wants to edit
     * @param innovationDto dto which keeps the edited data
     * @param bindingResult
     * @param model Holds the model attributes in the Spring Framework
     */
    @PostMapping("/postEditedInnovation/{id}")
    public String postInnovation(@PathVariable Long id, @Valid @ModelAttribute("innovation") InnovationDto innovationDto, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "hinzufuegen";
        }

        Innovation innovation = innovationService.getById(id);
        innovation.setTitel(innovationDto.getTitel());
        innovation.setBeschreibung(innovationDto.getBeschreibung());
        innovationService.saveInnovationAndGetId(innovation);

        User user = userService.getCurrentUser();
        model.addAttribute("innovation", InnovationDto.fromInnovation(innovation));
        model.addAttribute("bereitsBewertet", innovationService.hatUserInnovationBereitsBewertet(user.getUsername(), innovation));
        model.addAttribute("maxBewertungenErreicht", innovation.maximaleAnzahlAnBewertungenErreicht());
        model.addAttribute("message", "Erfolgreich gespeichert");

        return "details";
    }
}
