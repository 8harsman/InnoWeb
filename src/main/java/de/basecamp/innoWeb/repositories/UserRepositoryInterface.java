package de.basecamp.innoWeb.repositories;

import de.basecamp.innoWeb.materials.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryInterface extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
