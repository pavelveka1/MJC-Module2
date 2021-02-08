package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gift_certificates")
@DynamicUpdate(true)
@NamedQueries({
        @NamedQuery(name = "GiftCertificate.findById",
                query = "select distinct c from GiftCertificate c where c.id = :id"),
        @NamedQuery(name = "GiftCertificate.findAll",
                query = "select distinct c from GiftCertificate c"),
        @NamedQuery(name = "GiftCertificate.findByName",
                query = "select distinct c from GiftCertificate c where c.name = :name"),
        @NamedQuery(name = "GiftCertificate.findByTagName",
                query = "select distinct c from GiftCertificate c " +
                        "inner join fetch c.tags as t where t.name = :name"),
        @NamedQuery(name = "GiftCertificate.findByNameOrDescription",
                query = "select distinct c from GiftCertificate c where c.name like :nameOrDescription")
})
public class GiftCertificate implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private String name;

    private String description;

    private Integer price;

    private Integer duration;

    private LocalDateTime createDate;

    private LocalDateTime lastUpdateDate;

    private List<Order> orders;

    private List<Tag> tags;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long getId() {
        return id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    @Column(name = "price")
    public Integer getPrice() {
        return price;
    }

    @Column(name = "duration")
    public Integer getDuration() {
        return duration;
    }

    @Column(name = "create_date")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @Column(name = "last_update_date")
    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "gift_certificates_has_orders",
            joinColumns = @JoinColumn(name = "gift_certificates_id"),
            inverseJoinColumns = @JoinColumn(name = "orders_id"))
    public List<Order> getOrders() {
        return orders;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "gift_certificates_has_tags",
            joinColumns = @JoinColumn(name = "gift_certificates_id"),
            inverseJoinColumns = @JoinColumn(name = "tags_id"))
    public List<Tag> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "GiftCertificate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", tags=" + tags +
                '}';
    }
}
