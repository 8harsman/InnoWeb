package de.basecamp.innoWeb.services;

import de.basecamp.innoWeb.materials.Ereignis;
import de.basecamp.innoWeb.repositories.EreignisRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional

/**
 * Represents a service, which provides the necessary methods to manage all ereignis entities in the Ereignis-Repository.
 * @author Henning
 */
public class EreignisService {

    @Autowired
    private EreignisRepositoryInterface repository;

    public EreignisService() {
    }


    public Ereignis getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Ereignis> getAllEreignisse() {
        List<Ereignis> result = new LinkedList<>();
        repository.findAll().forEach(result::add);
        return result;
    }



    public void löscheEreignis(Long id) {
        Ereignis ereignis = repository.findById(id).orElseThrow();
        repository.delete(ereignis);
    }


    public void saveEreignis(Ereignis ereignis) {
        repository.save(ereignis);
    }
}
