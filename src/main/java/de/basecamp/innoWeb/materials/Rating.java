package de.basecamp.innoWeb.materials;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.util.Precision;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * This class is representing the valuations of {@linkplain de.basecamp.innoWeb.materials.Innovation},
 * it contains elements necessary for the database, 8 unique valuation category's and one overall rating.
 * {@linkplain lombok.Lombok} is responsible to set all setter and getters including a Constructor.
 * @author Jeremy
 * @author Henning
 * @author Keno
 * @version 0.1
 */
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "Bewertung_1")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "Ersteller_ID", referencedColumnName = "User_ID")
    private User ersteller; //Ersteller dieser Bewertung

    @Temporal(TemporalType.TIMESTAMP)
    private Date erstellungsdatum;

    @Column(name = "Bewertung_1")
    private float kriterium1;

    @Column(name = "Bewertung_2")
    private float kriterium2;

    @Column(name = "Bewertung_3")
    private float kriterium3;

    @Column(name = "Bewertung_4")
    private float kriterium4;

    @Column(name = "Bewertung_5")
    private float kriterium5;

    @Column(name = "Bewertung_6")
    private float kriterium6;

    @Column(name = "Bewertung_7")
    private float kriterium7;

    @Column(name = "Bewertung_8")
    private float kriterium8;

    @Transient //wird nicht persistiert in der DB
    private float gesamtRating;

    @Transient
    private List<Float> ratings; // List of all rating category's

    /**
     * Sets the overall rating by adding all category's together
     */
    public void setGesamtRating() {
        this.gesamtRating = Precision.round((kriterium1 + kriterium2 + kriterium3 + kriterium4 + kriterium5
                + kriterium6 + kriterium7 + kriterium8), 2);

    }

    /**
     * Returns the whole creator name
     * {@return creator name}
     */
    public String getErstellerName() {
        String vorNach = "";

        // avoiding Nullpointer Exeption when accesing Durschnittsrating
        if (ersteller != null) {
            vorNach = ersteller.getVorname() + " " + ersteller.getNachname();
        }
        return vorNach;
    }

    /**
     * Creates a list of the valuation category's
     */
    public void createRatingList() {
        this.ratings = List.of(kriterium1, kriterium2,
                kriterium3, kriterium4, kriterium5, kriterium6, kriterium7, kriterium8);
    }
}
