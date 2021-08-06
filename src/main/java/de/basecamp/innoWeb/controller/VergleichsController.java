package de.basecamp.innoWeb.controller;

import de.basecamp.innoWeb.dtos.InnovationDto;
import de.basecamp.innoWeb.materials.Innovation;
import de.basecamp.innoWeb.services.InnovationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class VergleichsController {

    InnovationService innovationService;

    @Autowired
    public VergleichsController(InnovationService innovationService) {
        this.innovationService = innovationService;
    }

    /**
     * Gets a list with innovations which have at least 1 rating and without the chosen innovation and adds the list to the model
     * @return the vergleichen.html
     * @param id Id of the already chosen innovation
     * @param model Holds the model attributes in the Spring Framework
     */
    @GetMapping("/vergleichen/{id}")
    String vergleichen(@PathVariable Long id, Model model) {
        Innovation innovation = innovationService.getById(id);
        List<Innovation> innovationen = innovationService.listAllWithRatingCount(1);
        innovationen.remove(innovation);
        List<InnovationDto> dtos = innovationen.stream().map(InnovationDto::fromInnovation).collect(Collectors.toList());

        model.addAttribute("id",id);
        model.addAttribute("innovationen", dtos);
        model.addAttribute("innovation", InnovationDto.fromInnovation(innovation));
        return "vergleichen";
    }

    /**
     * Gets 2 innovations to compare by their id and adds them to the model
     * @return the vergleich.html
     * @param id Id of the first innovation
     * @param id1 Id of the second innovation
     */
    @GetMapping("/vergleich/{id}/{id1}")
    String vergleich(@PathVariable Long id, @PathVariable Long id1, Model model) {
        Innovation innovation = innovationService.getById(id);
        Innovation innovation1 = innovationService.getById(id1);

        model.addAttribute("innovation", InnovationDto.fromInnovation(innovation));
        model.addAttribute("innovation1", InnovationDto.fromInnovation(innovation1));
        return "vergleich";
    }
}
