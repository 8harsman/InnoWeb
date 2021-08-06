package de.basecamp.innoWeb.repositories;

import de.basecamp.innoWeb.materials.Ereignis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface class extends the JpaRepository interface, which provides the methods to work with our ereignis entities in the database.
 */

@Repository
public interface EreignisRepositoryInterface extends CrudRepository<Ereignis, Long> {
    //Custom-Methoden können hier ergänzt werden, CRUD-Methoden vorhanden
}
