package de.basecamp.innoWeb.dtos;

import de.basecamp.innoWeb.materials.Dateianhang;
import de.basecamp.innoWeb.materials.Innovation;
import de.basecamp.innoWeb.materials.Rating;
import de.basecamp.innoWeb.materials.User;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * DTO used to transfer Data of innovations within controllers and Thymeleaf
 * @author Jeremy, Keno, Henning
 */
public class InnovationDto {
    private Long id;
    @NotBlank(message = "Es muss mindestens ein Titel angegeben werden")
    private String titel;
    private String beschreibung;
    private Date erstellungsdatum;
    private Date wiederholvorlageDatum;
    private String ersteller;
    private Rating bewertung;
    private List<Rating> bewertungen;
    private int anzahlBewertungen;
    private Rating durchschnittsBewertung;
    private List<Dateianhang> anhaenge;

    public InnovationDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Date getErstellungsdatum() {
        return erstellungsdatum;
    }

    public void setErstellungsdatum(Date erstellungsdatum) {
        this.erstellungsdatum = erstellungsdatum;
    }

    public Date getWiederholvorlageDatum() {
        return wiederholvorlageDatum;
    }

    public void setWiederholvorlageDatum(Date wiederholvorlageDatum) {
        this.wiederholvorlageDatum = wiederholvorlageDatum;
    }

    public String getErsteller() {
        return ersteller;
    }

    public void setErsteller(String ersteller) {
        this.ersteller = ersteller;
    }

    public Rating getBewertung() {
        return bewertung;
    }

    public void setBewertung(Rating bewertung) {
        bewertung.setGesamtRating();
        bewertung.createRatingList();
        this.bewertung = bewertung;
    }

    public void setAnzahlBewertungen(int anzahl){
        this.anzahlBewertungen = anzahl;
    }

    public int getAnzahlBewertungen(){
        return this.anzahlBewertungen;
    }

    /**
     * Sets multiple ratings of different users. Maximum is 3.
     * @param ratings List of ratings belonging to one innovation
     */
    public void setBewertungen(List<Rating> ratings) {

        for (Rating var : ratings)
        {
            var.setGesamtRating(); //errechnet die Gesamtwerte für jede Bewertung zur Innovation
            var.createRatingList(); // erstellt Liste aller Kriterien zur Benutzung mit Thymeleaf
        }

        this.bewertungen = ratings;
    }


    public void setAnhaenge(List<Dateianhang> anhaenge) {
        this.anhaenge = anhaenge;
    }


    public List<Rating> getBewertungen() {

        return bewertungen;
    }

    public List<Dateianhang> getAnhaenge() {

        return anhaenge;
    }

    public Rating getDurchschnittsBewertung() {
        return durchschnittsBewertung;
    }

    public void setDurchschnittsBewertung(Rating durchschnittsBewertung) {
        this.durchschnittsBewertung = durchschnittsBewertung;
    }

    /**
     * Transforms a material to DTO, in this case an innovation
     * @param innovation basic innovation material
     * @return InnovationDto DTO used to transfer data for innovations
     */
    public static InnovationDto fromInnovation(Innovation innovation) {
        InnovationDto result = new InnovationDto();
        result.setId(innovation.getId());
        result.setTitel(innovation.getTitel());
        result.setErstellungsdatum(innovation.getErstellungsdatum());

        User ersteller = innovation.getErsteller();
        String erstellerString = ersteller.getVorname() + " " + ersteller.getNachname();
        result.setErsteller(erstellerString);

        if(!innovation.getBewertungen().isEmpty()) result.setBewertungen(innovation.getBewertungen());
        if(!innovation.getBewertungen().isEmpty()) result.setBewertung(innovation.getBewertungen().get(0));
        if(!innovation.getBewertungen().isEmpty()) innovation.setDurchschnittsBewertung();
        if(!innovation.getBewertungen().isEmpty()) innovation.getDurchschnittsBewertung().setGesamtRating();
        if(!innovation.getBewertungen().isEmpty()) innovation.getDurchschnittsBewertung().createRatingList();
        if(!innovation.getBewertungen().isEmpty()) result.setDurchschnittsBewertung(innovation.getDurchschnittsBewertung());
        if(innovation.getBeschreibung() != null) result.setBeschreibung(innovation.getBeschreibung());
        if(innovation.getWiedervorlagedatum() != null) result.setWiederholvorlageDatum(innovation.getWiedervorlagedatum());
        if(!innovation.getBewertungen().isEmpty()) result.setAnzahlBewertungen(innovation.countBewertungen());
        if(!innovation.getAnhaenge().isEmpty()) result.setAnhaenge(innovation.getAnhaenge());

        return result;
    }
}
