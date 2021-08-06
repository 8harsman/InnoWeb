package de.basecamp.innoWeb.materials;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;


@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "Ereignis")

/**
 * Represents an event which will be automatically created, if users change data in the web application.
 * An event must have a id, an user-id, an innovation-id and an "art", which describes the kind of the event. It also can relate to a rating or a "dateianhang" (file attachement).
 * An Event is created if a new innovation or rating is created. Extensions are conceivable.
 * @author Henning
 */

public class Ereignis {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "User_ID", referencedColumnName = "User_ID")
    private User ersteller;

    @ManyToOne
    @JoinColumn(name = "Innovation_ID", referencedColumnName = "id")
    private Innovation innovation;

    @ManyToOne
    @JoinColumn(name = "Rating_ID", referencedColumnName = "id")
    private Rating rating;

    @ManyToOne
    @JoinColumn(name = "Anhang_ID", referencedColumnName = "id")
    private Dateianhang dateianhang;

    @Temporal(TemporalType.TIMESTAMP)
    private Date datum;

    @Column(name = "Art")
    private String art;

    public String getErstellungsdatumString(){

        return this.datum.toString();
    }



    /**
     *  Returns a text, which describes the event
     */

    public String getEreignistext(){

        if (this.art.equals("InnovationNeu"))
        {

            return (this.datum + ": " + this.ersteller.getName() + " hat die Innovation '" + this.innovation.getTitel() + "' angelegt");
        }

        if (this.art.equals("BewertungNeu") )
        {

            return (this.datum + ": " + this.ersteller.getName() + " hat die Innovation '" + this.innovation.getTitel() + "' bewertet");
        }

        //placeholder
        if (this.art.equals("AnlageLöschen"))
        {

            return "";
        }

        //placeholder
        if (this.art.equals("BewertungLöschen"))
        {

            return "";
        }

        //placeholder
        if (this.art.equals("AnhangNeu"))
        {

            return "";
        }



        return "fail";
    }

    /**
     * Constructor-method
     *
     * @param ersteller User who is responsible for the event
     * @param innovation Corresponding innovation regarding the event
     * @param art Describes the kind of the event
     */
    public Ereignis(User ersteller, Innovation innovation, String art) {

        this.ersteller = ersteller;
        this.innovation = innovation;
        this.art = art;
        this.datum = Date.from(Instant.now());

    }


}
