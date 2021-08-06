package de.basecamp.innoWeb.services;

import de.basecamp.innoWeb.materials.User;
import de.basecamp.innoWeb.repositories.RoleRepositoryInterface;
import de.basecamp.innoWeb.repositories.UserRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserService {
    @Autowired
    private UserRepositoryInterface userRepositoryInterface;
    @Autowired
    private RoleRepositoryInterface roleRepositoryInterface;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepositoryInterface.findAll()));
        userRepositoryInterface.save(user);
    }


    public User findByUsername(String username) {
        return userRepositoryInterface.findByUsername(username);
    }

    /**
     * @return the current user, using the web application
     */
    public User getCurrentUser() {

        System.out.println("aktueller User security  " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        System.out.println("username " + username);
        User user = userRepositoryInterface.findByUsername(username);
        return user;
    }
}