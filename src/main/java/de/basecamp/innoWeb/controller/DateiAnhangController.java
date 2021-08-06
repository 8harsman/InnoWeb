package de.basecamp.innoWeb.controller;

import de.basecamp.innoWeb.dtos.InnovationDto;
import de.basecamp.innoWeb.materials.Dateianhang;
import de.basecamp.innoWeb.materials.Innovation;
import de.basecamp.innoWeb.services.DateinanhangService;
import de.basecamp.innoWeb.services.EreignisService;
import de.basecamp.innoWeb.services.InnovationService;
import de.basecamp.innoWeb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DateiAnhangController {

    InnovationService innovationService;
    DateinanhangService dateinanhangService;
    UserService userService;

    @Autowired
    public DateiAnhangController(InnovationService innovationService, DateinanhangService dateinanhangService, UserService userService, EreignisService ereignisService) {
        this.innovationService = innovationService;
        this.dateinanhangService = dateinanhangService;
        this.userService = userService;
    }

    /**
     * Uploads a file and creates the corresponding dateinanhang entitz
     * @return Redirects to the details-mapping for the corresponding innovation
     * @param file file which the user wants to ulpoad
     * @param id Id of the innovation which the user wants to attach the file to
     * @param attributes Used to show messages
     */
    @PostMapping("/upload/{id}")
    public String uploadFile(@RequestParam("file") MultipartFile file, @PathVariable Long id, RedirectAttributes attributes)  {

        // check if file is empty
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/details/" + id;
        }

        String message = "";
        try {
            dateinanhangService.store(file, innovationService.getById(id), userService.getCurrentUser());

            message = "Datei erfolgreich hochgeladen: " + file.getOriginalFilename();
            attributes.addFlashAttribute("message", message);
            return "redirect:/details/" + id;

        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!" + e.toString();
            attributes.addFlashAttribute("message", message);
            return "redirect:/details/" + id;
        }
    }


    /**
     * Deletes the dateinanhang of an innovation
     * @return Redirects to the details-mapping for the corresponding innovation
     * @param idanhang id of the dateianhang which the user wants to delete
     * @param id Id of the innovation which the user wants delete the dateianhang from
     * @param model Holds the model attributes in the Spring Framework
     */
    @GetMapping("/deleteAnhang/{id}/{idanhang}")
    public String deleteAnhang(@PathVariable Long id, @PathVariable Long idanhang, Model model) {
        innovationService.löscheAnhangFuerInnovation(id, idanhang);

        Innovation innovation = innovationService.getById(id);
        model.addAttribute("innovation", InnovationDto.fromInnovation(innovation));
        return "redirect:/details/" + id;
    }

    /**
     * Downloads the dateinanhang of an innovation
     * @return
     * @param id Id of the dateianhang which the user wants to download
     */
    @GetMapping("/downloadAnhang/{id}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws Exception {

        try
        {
            Dateianhang dateianhang = dateinanhangService.getFile(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(dateianhang.getTyp()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dateianhang.getName() + "\"")
                    .body(new ByteArrayResource(dateianhang.getDatei()));
        }
        catch(Exception e)
        {
            throw new Exception("Error downloading file");
        }


    }
}
