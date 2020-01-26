package com.nilin.springboot.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "albums" )
public class Album implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

//    @Column
//    private String  userUsername; //userUsername: foreign key to User

    @Column
    private Long userId; //userId: foreign key to User

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL ,  fetch = FetchType.LAZY ) //targetEntity = Image.class
    private Set<Image> images = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId" , insertable=false, updatable=false)
    //@JoinColumn(name = "userUsername" , referencedColumnName="username", insertable=false, updatable=false) // //error: Repeated column in mapping for entity fixed by this
//improtant! joinColumn fk references to primary key in default. to reference to uniqe column, add referencedColumnName
// alter table albums add constraint FKdi64byktkuom2rwbh1l8b9wkn foreign key (userUsername) references usersss (username)
    private User user;

    public Album(String albumTitle, Long userId) {
        super();
        this.title = albumTitle;
        this.userId = userId;
    }

    public Album() {
    }

    /*
     * Getters and setters for model attributes
     */

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public Set<Image> getImages() { return images; }

    public void setImages(Set<Image> images) { this.images = images; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Album [id=" + id + ", title=" + title + ", userId=" + userId + "]";
    }

}

