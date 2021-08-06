package de.basecamp.innoWeb.repositories;

import de.basecamp.innoWeb.materials.Dateianhang;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface class extends the JpaRepository interface, which provides the methods to work with our dateianhang entities in the database.
 */

public interface DateianhangRepositoryInterface extends JpaRepository<Dateianhang, Long> {



}
