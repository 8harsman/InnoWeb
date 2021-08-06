package de.basecamp.innoWeb.services;

import de.basecamp.innoWeb.dtos.InnovationDto;
import de.basecamp.innoWeb.materials.Innovation;
import de.basecamp.innoWeb.materials.Rating;
import de.basecamp.innoWeb.repositories.InnovationRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service used to manage Innovations (saving, deleting, creating, listing, etc.)
 * @author Keno, Henning, Jeremy, Anna
 */
@Service
@Transactional
public class InnovationService {

    @Autowired
    private InnovationRepositoryInterface repository;

    public InnovationService() {
    }


    public Innovation getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Innovation> listAll(String titel, String ersteller, String beschreibung, String bewertungenMin, String bewertungenMax, String datumMin, String datumMax, String potenzialMin, String potenzialMax) {

        Date datumMinZ;
        Date datumMaxZ;

        Calendar mycalendar1 = new GregorianCalendar(2021, 0, 1);
        Calendar mycalendar2 = new GregorianCalendar(2100, 0, 1);

        if (!datumMin.equals(""))
        {
            String[] values = datumMin.split("-");
            int jahr = Integer.parseInt(values[0]);
            int monat = Integer.parseInt(values[1])-1; //-1 da Calendar Monat Index bei 0 beginnt
            int tag = Integer.parseInt(values[2]);

            mycalendar1.set(jahr, monat, tag, 0,1);

        }

        datumMinZ = mycalendar1.getTime();

        if (!datumMax.equals(""))
        {
            String[] values = datumMax.split("-");
            int jahr = Integer.parseInt(values[0]);
            int monat = Integer.parseInt(values[1])-1; //-1 da Calendar Monat Index bei 0 beginnt
            int tag = Integer.parseInt(values[2]);

            mycalendar2.set(jahr, monat, tag, 23, 59);

        }

        datumMaxZ = mycalendar2.getTime();

        System.out.println("Suche nach " + titel + ersteller + beschreibung + bewertungenMin + bewertungenMax + datumMinZ + datumMaxZ + potenzialMin + potenzialMax);



        List<Innovation> list = repository.findByKeyword(titel, ersteller, beschreibung, datumMinZ, datumMaxZ);




        int bewertungenMinZ; //initialisierung mit 0 und 10 hier nicht möglich, lambda-Ausdruck unten final Variable benötigt
        int bewertungenMaxZ;

        //System.out.println("Min String " + bewertungenMin + "Max String" + bewertungenMax);

        if (bewertungenMin.equals(""))
        {
             bewertungenMinZ = 0;
        }

        else{
             bewertungenMinZ = Integer.parseInt(bewertungenMin);
        }

        if (bewertungenMax.equals(""))
        {
             bewertungenMaxZ = 10;
        }

        else{
             bewertungenMaxZ = Integer.parseInt(bewertungenMax);
        }

       // System.out.println("Min " + bewertungenMinZ + "Max " + bewertungenMaxZ);

        int potenzialMinZ; //initialisierung mit 0 und 10 hier nicht möglich, lambda-Ausdruck unten final Variable benötigt
        int potenzialMaxZ;

        if (potenzialMin.equals(""))
        {
            potenzialMinZ = 0;
        }

        else{
            potenzialMinZ = Integer.parseInt(potenzialMin);
        }

        if (potenzialMax.equals(""))
        {
            potenzialMaxZ = 1000;
        }

        else{
            potenzialMaxZ = Integer.parseInt(potenzialMax);
        }

        //System.out.println("Min Potenzial " + potenzialMinZ + "Max Potenzial " + potenzialMaxZ);


        //bevor wir nach Gesamtrating der Durchschnittsbewertung filtern können, müssen diese gesetzt werden
        for (Innovation innovation:  this.listAll() ) {
            innovation.setDurchschnittsBewertung();
            innovation.getDurchschnittsBewertung().setGesamtRating();
        }

        //Filter für AnzahlBewertungen notwendig, da per SQL schwierig, da nicht persistiert
        List<Innovation> list2 = list.stream()
                .filter(i -> i.getBewertungen().size() >= bewertungenMinZ && i.getBewertungen().size() <= bewertungenMaxZ && i.getDurchschnittsBewertung().getGesamtRating() >= potenzialMinZ && i.getDurchschnittsBewertung().getGesamtRating() <= potenzialMaxZ).collect(Collectors.toList());



        return list2;


    }

    public List<Innovation> listAll() {

        List<Innovation> result = new LinkedList<>();
        repository.findAll().forEach(result::add);
        return result;
    }

    /**
     * Lists all innovations with at least ratingCount number of ratings
     * @param ratingCount Number of ratings
     * @return A List containing all innovations with a specified number of ratings
     */
    public List<Innovation> listAllWithRatingCount(int ratingCount) {

        if (ratingCount < 0)
        {
            throw new IllegalArgumentException("Number of ratings cant be smaller than zero!");
        }
        if(ratingCount == 0)
        {
            return listAll();
        }
        else
        {
            List<Innovation> result = new LinkedList<>();
            result.addAll(repository.findAllWithRatingCount(ratingCount));
            return result;
        }
    }



    public void speicherBewertungFuerInnovation(Long id, Rating rating) {
        Innovation innovation = repository.findById(id).orElseThrow();
        innovation.addBewertung(rating);
        repository.save(innovation);
    }

    public void löscheBewertungFuerInnovation(Long id, Long idrating) {
        Innovation innovation = repository.findById(id).orElseThrow();
        innovation.deleteBewertung(innovation.getRatingFuerInnovation(idrating));
        repository.save(innovation);
    }

    public void löscheInnovation(Long id) {
        Innovation innovation = repository.findById(id).orElseThrow();
        repository.delete(innovation);
    }


    public Long saveInnovationAndGetId(Innovation idee) {
        return repository.save(idee).getId();
    }

    public void löscheAnhangFuerInnovation(Long id, Long idanhang) {
        Innovation innovation = repository.findById(id).orElseThrow();
        innovation.deleteAnhang(innovation.getAnhangFuerInnovation(idanhang));
        repository.save(innovation);

    }

    public boolean hatUserInnovationBereitsBewertet(String username, Innovation innovation) {

        List<Rating> bewertungen = innovation.getBewertungen();
        for (Rating bewertung: bewertungen) {
            if (bewertung.getErsteller().getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }


    public Rating getCurrentUsersRating(String username, Long innoId) {
        Innovation innovation = repository.findById(innoId).orElseThrow();
        return innovation.getBewertungen().stream()
                .filter(i -> i.getErsteller().getUsername().equals(username))
                .findFirst()
                .orElseThrow();
    }

    public void ersetzeBewertungVonNutzer(Long innoId, Rating bewertung, String username) {
        Innovation innovation = repository.findById(innoId).orElseThrow();
        Rating aktuelleNutzerBewertung = innovation.getBewertungen().stream()
                .filter(i -> i.getErsteller().getUsername().equals(username))
                .findFirst()
                .orElseThrow();
        List<Rating> bewertungen = innovation.getBewertungen();
        int indexVonNutzerBewertung = bewertungen.indexOf(aktuelleNutzerBewertung);
        bewertungen.remove(aktuelleNutzerBewertung);
        bewertungen.add(indexVonNutzerBewertung, bewertung);
        repository.save(innovation);
    }
}


