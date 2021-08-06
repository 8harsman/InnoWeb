package de.basecamp.innoWeb.repositories;

import de.basecamp.innoWeb.materials.Innovation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

/**
 * This interface class extends the JpaRepository interface, which provides the methods to work with our innovation entities in the database.
 */


@Repository
public interface InnovationRepositoryInterface extends CrudRepository<Innovation, Long> {

   //defines a query which can be used for filtering in the backend, which is NOT used at the moment
   @Query("select u from Innovation u where u.titel like %:titel% and (u.ersteller.vorname like %:ersteller% or u.ersteller.nachname like %:ersteller%) and u.beschreibung like %:beschreibung% and erstellungsdatum >= :datumMin and erstellungsdatum <= :datumMax ")
    List<Innovation> findByKeyword(String titel, String ersteller, String beschreibung, Date datumMin, Date datumMax);                                                                //and (select count(r.innovation_id) from Rating r where r.innovation_id = u.id) = 0

   //defines a query which can be used for sorting in the backend, which is NOT used at the moment
   @Query("SELECT r FROM Innovation r ORDER BY erstellungsdatum DESC")
   List<Innovation> findAll();

   //defines a query which can be used for filtering in the backend, which is NOT used at the moment
   @Query("SELECT r FROM Innovation r where size(r.bewertungen) >= :rC ")
   List<Innovation> findAllWithRatingCount(@Param("rC") int ratingCount);

}
