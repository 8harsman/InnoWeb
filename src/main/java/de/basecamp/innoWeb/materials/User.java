package de.basecamp.innoWeb.materials;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "User_1")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "User_ID")
    private Long id;

    @Column(name = "Vorname")
    private String vorname;

    @Column(name = "Nachname")
    private String nachname;

    @Column(name = "Email")
    private String username;

    @Column(name = "Password")
    private String password;

    @Transient
    private String passwordConfirm;

    @ManyToMany
    private Set<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() { return nachname; }

    public void setNachname(String nachname) { this.nachname = nachname; }

    public String getName(){return  vorname + " " + nachname;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }

    public String getPasswordConfirm() { return passwordConfirm; }

    public void setPasswordConfirm(String passwordConfirm) { this.passwordConfirm = passwordConfirm; }

    public Set<Role> getRoles() { return roles; }

    public void setRoles(Set<Role> roles) { this.roles = roles; }

}
