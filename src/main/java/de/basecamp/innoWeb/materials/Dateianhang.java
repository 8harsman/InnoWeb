package de.basecamp.innoWeb.materials;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Blob;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "Dateianhang_1")

/**
 * Represents a file attachment which will be created, if users upload a file regarding a certain innovation.
 * A file attachment must have an id, a name, a creator (user), a type (file type), a creation date and the uploaded data.
 */

public class Dateianhang {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "Ersteller_ID", referencedColumnName = "User_ID")
    private User ersteller; //Ersteller dieser Bewertung

    @Column(name = "Datei_Name")
    private String name;

    @Column(name = "Typ")
    private String typ;

    @Column(name = "Datei", length = 16777215) //Länge für medium blob
    @Lob
    private byte[] datei;

    @Temporal(TemporalType.TIMESTAMP)
    private Date erstellungsdatum;



    /**
     * Constructor-method
     *
     * @param dateiname Name of uploaded file
     * @param ersteller User who uploaded the file
     * @param typ Type of the file
     * @param data The file data
     * @param erstellungsdatum Date of creation
     */
    public Dateianhang(String dateiname, User ersteller, String typ, byte[] data, Date erstellungsdatum) {
        this.name = dateiname;
        this.ersteller = ersteller;

        this.typ = typ;
        this.datei = data;
        this.erstellungsdatum = erstellungsdatum;

    }

    /**
     * returns creation date as a String
     */
    public String getErstellungsdatumString(){

    return this.erstellungsdatum.toString();
    }

    /**
     * returns the roundend file size as a String
     */
    public String getSize()
    {
        float sizeMB = (float) datei.length / 1000000;

        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);
        String sizeMBString = df.format(sizeMB);
        return sizeMBString;
    }





}
