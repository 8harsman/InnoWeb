package de.basecamp.innoWeb.services;

/**
 * Service responsible for authenticating and logging
 * @author Niklas
 */
public interface SecurityService {

    /**
     * Returns <code>True</code> when User is authenticated, else returns <code>False</code>
     * @return boolean value for authentication status
     */
    boolean isAuthenticated();

    /**
     * Logs in a user
     * @param username Username of user
     * @param password encoded password of user
     */
    void autoLogin(String username, String password);
}