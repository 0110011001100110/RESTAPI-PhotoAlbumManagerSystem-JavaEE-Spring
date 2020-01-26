package com.nilin.springboot.model;

//import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
//import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/*

If you reference another object using non-primary key column (username), make the referenced object serializable.

It's a known bug created in 2012 and still unsolved.

Make Shurl serialzable and it should work:

@Entity
public class Shurl implements Serializable {

    private static final long serialVersionUID = 1L;
}

 */

@Entity
@Table(name = "usersss" , schema = "mysql")
@DynamicUpdate
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Transient
    @Column
    private String passwordConfirm;

    @Column
    private String token;

    @Column
    private String fullname;

    @Temporal(TemporalType.DATE)
    @Column
    private Date dateOfBirth;

    @ManyToMany
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL , fetch = FetchType.LAZY) //mappedby shoud be same as User user in User class
    private Set<Album> albums = new HashSet<>();

    public Set<Album> getAlbums() { return albums; }
    public void setAlbums(Set<Album> albums) { this.albums = albums; }


    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Long getId() { return id;}
    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {this.token = token; }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }
    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}



