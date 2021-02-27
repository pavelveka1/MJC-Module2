package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Class entity User
 *
 * @author Pavel Veka
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "User.findById",
                query = "select distinct u from User u where u.id = :id"),
        @NamedQuery(name = "User.findByName",
                query = "select distinct u from User u where u.username = :username")
}
)
public class User extends RepresentationModel<User> {

    private static final long SERIAL_VERSION_UID = 1L;

    private long id;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private List<Order> orders;

    private List<Role> roles;

    public User(long id, String username, String password, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    public long getId() {
        return id;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "users_has_roles",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"))
    public List<Role> getRoles() {
        return roles;
    }

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return id == user.id &&
                username.equals(user.username) &&
                password.equals(user.password) &&
                firstName.equals(user.firstName) &&
                lastName.equals(user.lastName) &&
                roles.equals(user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, username, password, firstName, lastName, roles);
    }
}
