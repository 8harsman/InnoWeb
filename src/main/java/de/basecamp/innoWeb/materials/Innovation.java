package de.basecamp.innoWeb.materials;

import de.basecamp.innoWeb.dtos.InnovationDto;
import org.apache.commons.math3.util.Precision;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * Represents an innovation which can be created in the web application. One innovation can be evaluated
 * 0-3 times. A single person is only allowed to evaluate once, however this evaluation can be changed.
 * Every innovation has an ID and has to have a title which is not blank, additionally an innovation
 * has an average evaluation, attachments(files), a creation date, a list of all its evaluations,
 * a creator, a description and a resubmission date.
 * When an innovation is created it can be viewed as an event {@linkplain de.basecamp.innoWeb.materials.Ereignis}
 * @author Keno, Jeremy, Henning
 */
@Entity
@Table(name = "Innovation_1")
public class Innovation {

    private static final int MAX_BEWERTUNGEN = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "Titel") //name NICHT case-sensitive
    private String titel;

    @Column(name = "Beschreibung")
    private String beschreibung;

    @ManyToOne
    @JoinColumn(name = "Ersteller_ID", referencedColumnName = "User_ID")
    private User ersteller; //Ersteller dieser Innovation

    @Temporal(TemporalType.TIMESTAMP)
    private Date erstellungsdatum;

    @Temporal(TemporalType.TIMESTAMP)
    private Date wiedervorlagedatum;

    @OneToMany(cascade = CascadeType.ALL) //wenn die Innovation gelöscht wird, werden auch alle zugehörigen Bewertungen gelöscht
    @JoinColumn(name="innovation_id")     //nämlich alle bewertungen bei denen die entsprechende innovation_id hinterlegt ist.
    List<Rating> bewertungen;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="innovation_id")
    List<Dateianhang> anhaenge;

    @OneToMany(cascade = CascadeType.ALL) //wenn die Innovation gelöscht wird, werden auch alle zugehörigen Bewertungen gelöscht
    @JoinColumn(name="innovation_id")     //nämlich alle bewertungen bei denen die entsprechende innovation_id hinterlegt ist.
    List<Ereignis> ereignisse;

    @Transient //wird nicht persistiert in der DB
    private Rating durchschnittsBewertung = new Rating();


    public Innovation() {
        this.bewertungen = new LinkedList<>();
        this.anhaenge = new LinkedList<>();
    }

    public void addBewertung(Rating bewertung) {
        bewertungen.add(bewertung);
    }

    public void addAnhang(Dateianhang anhang) {
        anhaenge.add(anhang);
    }

    public void deleteBewertung(Rating bewertung) {
        bewertungen.remove(bewertung);
    }

    public void deleteAnhang(Dateianhang anhang) {
        anhaenge.remove(anhang);
    }

    public Rating getRatingFuerInnovation(Long ratingId)
    {
        for (Rating rating : bewertungen) {
            if (rating.getId().equals(ratingId)) {
                return rating;
            }
        }
        return null;
    }

    public Dateianhang getAnhangFuerInnovation(Long anhangId)
    {
        for (Dateianhang anhang : anhaenge) {
            if (anhang.getId().equals(anhangId)) {
                return anhang;
            }
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public User getErsteller() {
        return ersteller;
    }

    public void setErsteller(User ersteller) {
        this.ersteller = ersteller;
    }

    public List<Rating> getBewertungen() {
        return bewertungen;
    }

    public void setBewertungen(List<Rating> bewertungen) {
        this.bewertungen = bewertungen;
    }

    public Date getErstellungsdatum() {
        return erstellungsdatum;
    }

    public void setErstellungsdatum(Date erstellungsdatum) {
        this.erstellungsdatum = erstellungsdatum;
    }

    public Date getWiedervorlagedatum() {
        return wiedervorlagedatum;
    }

    public void setWiedervorlagedatum(Date wiedervorlagedatum) {
        this.wiedervorlagedatum = wiedervorlagedatum;
    }

    public int countBewertungen(){
        return bewertungen.size();
    }

    public Rating getDurchschnittsBewertung() { return durchschnittsBewertung; }

    public List<Dateianhang> getAnhaenge() {
        return anhaenge;
    }

    public void setAnhaenge(List<Dateianhang> anhaenge) {
        this.anhaenge = anhaenge;
    }

    public void setDurchschnittsBewertung(){

        float kriterium1 = 0;
        float kriterium2 = 0;
        float kriterium3 = 0;
        float kriterium4 = 0;
        float kriterium5 = 0;
        float kriterium6 = 0;
        float kriterium7 = 0;
        float kriterium8 = 0;

        for (int i = 0; i < bewertungen.size(); i++) {

            kriterium1 = kriterium1 + bewertungen.get(i).getKriterium1();
            kriterium2 = kriterium2 + bewertungen.get(i).getKriterium2();
            kriterium3 = kriterium3 + bewertungen.get(i).getKriterium3();
            kriterium4 = kriterium4 + bewertungen.get(i).getKriterium4();
            kriterium5 = kriterium5 + bewertungen.get(i).getKriterium5();
            kriterium6 = kriterium6 + bewertungen.get(i).getKriterium6();
            kriterium7 = kriterium7 + bewertungen.get(i).getKriterium7();
            kriterium8 = kriterium8 + bewertungen.get(i).getKriterium8();
        }

       this.durchschnittsBewertung.setKriterium1(Precision.round((kriterium1 / bewertungen.size()),2));
        this.durchschnittsBewertung.setKriterium2(Precision.round((kriterium2 /  bewertungen.size()), 2));
        this.durchschnittsBewertung.setKriterium3(Precision.round((kriterium3 / bewertungen.size()), 2));
        this.durchschnittsBewertung.setKriterium4(Precision.round((kriterium4 / bewertungen.size()), 2));
        this.durchschnittsBewertung.setKriterium5(Precision.round((kriterium5 / bewertungen.size()), 2));
        this.durchschnittsBewertung.setKriterium6(Precision.round((kriterium6 / bewertungen.size()), 2));
        this.durchschnittsBewertung.setKriterium7(Precision.round((kriterium7 / bewertungen.size()), 2));
        this.durchschnittsBewertung.setKriterium8(Precision.round((kriterium8 / bewertungen.size()), 2));
    }

    public boolean maximaleAnzahlAnBewertungenErreicht(){
        return bewertungen.size() == MAX_BEWERTUNGEN;
    }

    public static Innovation fromDto(InnovationDto dto, User ersteller) {
        Innovation result = new Innovation();
        result.setId(dto.getId());
        result.setTitel(dto.getTitel());
        result.setErsteller(ersteller);
        if (dto.getBeschreibung() != null) result.setBeschreibung(dto.getBeschreibung());
        if (dto.getBewertung() != null) result.addBewertung(dto.getBewertung());
        if (dto.getAnhaenge() != null) result.addAnhang(dto.getAnhaenge().get(0));
        if (dto.getWiederholvorlageDatum() != null) result.setWiedervorlagedatum(dto.getWiederholvorlageDatum());

        return result;
    }
}
