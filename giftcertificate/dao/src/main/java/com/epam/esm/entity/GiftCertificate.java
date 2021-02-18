package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gift_certificates")
@EntityListeners(AuditListener.class)
@DynamicUpdate()
@NamedQueries({
        @NamedQuery(name = "GiftCertificate.findByName",
                query = "select distinct c from GiftCertificate c where c.name = :name")
})
public class GiftCertificate implements Serializable {

    private static final long SERIAL_VERSION_UID = 1L;

    private long id;

    private String name;

    private String description;

    private Integer price;

    private Integer duration;

    private LocalDateTime createDate;

    private LocalDateTime lastUpdateDate;

    private List<Order> orders;

    private List<Tag> tags;

    private boolean deleted;

    public GiftCertificate(long id, String name, String description, int price, int duration, LocalDateTime createDate,
                           LocalDateTime lastUpdateDate, boolean deleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.deleted = deleted;
    }

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

    @Column(name = "deleted")
    public boolean getDeleted() {
        return deleted;
    }

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "gift_certificates_has_orders",
            joinColumns = @JoinColumn(name = "gift_certificates_id"),
            inverseJoinColumns = @JoinColumn(name = "orders_id"))
    public List<Order> getOrders() {
        return orders;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GiftCertificate)) return false;
        GiftCertificate that = (GiftCertificate) o;
        return id == that.id &&
                deleted == that.deleted &&
                name.equals(that.name) &&
                description.equals(that.description) &&
                Objects.equals(price, that.price) &&
                Objects.equals(duration, that.duration) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(lastUpdateDate, that.lastUpdateDate) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, createDate, lastUpdateDate, tags);
    }
}
