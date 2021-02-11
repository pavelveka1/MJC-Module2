package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Class entity Order
 *
 * @author Pavel Veka
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
@NamedQueries({
        @NamedQuery(name = "Order.findById",
                query = "select distinct o from Order o where o.id = :id")
})
public class Order implements Serializable {

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
}
