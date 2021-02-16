package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Class entity Order
 *
 * @author Pavel Veka
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@Table(name = "orders")
@NamedQueries({
        @NamedQuery(name = "Order.findById",
                query = "select distinct o from Order o where o.id = :id")
})
public class Order implements Serializable {

    private static final long SERIAL_VERSION_UID = 1L;

    private long orders_id;

    private User user;

    private int cost;

    private String date;

    private List<GiftCertificate> certificates;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long getOrders_id() {
        return orders_id;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "users_id")
    public User getUser() {
        return user;
    }

    @Column(name = "cost")
    public int getCost() {
        return cost;
    }

    @Column(name = "buy_date")
    public String getDate() {
        return date;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "gift_certificates_has_orders",
            joinColumns = @JoinColumn(name = "orders_id"),
            inverseJoinColumns = @JoinColumn(name = "gift_certificates_id"))
    public List<GiftCertificate> getCertificates() {
        return certificates;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orders_id=" + orders_id +
                ", cost=" + cost +
                ", date='" + date + '\'' +
                ", certificates=" + certificates +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return orders_id == order.orders_id &&
                cost == order.cost &&
                user.equals(order.user) &&
                date.equals(order.date) &&
                certificates.equals(order.certificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orders_id, user, cost, date, certificates);
    }
}
