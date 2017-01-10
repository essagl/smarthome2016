package org.openhapi.smarthome2016.server.core;

import javax.persistence.*;
import java.security.Principal;

/**
 * Created by ulrich on 07.01.17.
 */
@Entity
@Table(name = "users")
@NamedQueries(
        {
                @NamedQuery(
                        name = "org.openhapi.smarthome2016.server.core.User.findAll",
                        query = "SELECT u FROM User u"
                ),

                @NamedQuery(
                        name = "org.openhapi.smarthome2016.server.core.User.findByName",
                        query = "SELECT u FROM User u where name = :name"
                ),
                @NamedQuery(
                        name = "org.openhapi.smarthome2016.server.core.User.findByNameAndPassword",
                        query = "SELECT u FROM User u where name = :name and password = :password"
                )
        }
)
public class User implements Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private  String name;

    @Column(name = "password")
    private  String password;


    @Column(name = "roles")
    private String roles;


    public User() {
    }

    public User(String name) {
        this.name = name;
        this.roles = null;
        this.password = null;
    }

    public User(String name, String password) {
        this.name = name;
        this.roles = null;
        this.password = password;
    }

    public User(String name, String password, String roles) {
        this.name = name;
        this.roles = roles;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }



    public long getId() {
        return id;
    }

    public String getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!getName().equals(user.getName())) return false;
        return getPassword() != null ? getPassword().equals(user.getPassword()) : user.getPassword() == null;
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }
}


