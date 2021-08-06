package de.basecamp.innoWeb.services;

import de.basecamp.innoWeb.materials.Dateianhang;
import de.basecamp.innoWeb.materials.Innovation;
import de.basecamp.innoWeb.materials.User;
import de.basecamp.innoWeb.repositories.UserRepositoryInterface;
import de.basecamp.innoWeb.repositories.DateianhangRepositoryInterface;
import de.basecamp.innoWeb.repositories.InnovationRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Stream;

@Service
@Transactional

/**
 * Represents a service, which provides the necessary methods to manage all dateianhang entities in the dateianhang-Repository.
 * @author Henning
 */
public class DateinanhangService {

    @Autowired
    private DateianhangRepositoryInterface dateianhangRepository;
    private UserRepositoryInterface userRepository;
    private InnovationRepositoryInterface innovationRepository;


    public Dateianhang store(MultipartFile file, Innovation innovation, User ersteller ) throws IOException {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Dateianhang dateianhang = new Dateianhang(fileName, ersteller, file.getContentType(), file.getBytes(), Date.from(Instant.now()));
        innovation.addAnhang(dateianhang);

        return dateianhangRepository.save(dateianhang);
    }

    public Dateianhang getFile(Long id) {
        return dateianhangRepository.findById(id).get();
    }

    public Stream<Dateianhang> getAllFiles() {
        return dateianhangRepository.findAll().stream();
    }
}
