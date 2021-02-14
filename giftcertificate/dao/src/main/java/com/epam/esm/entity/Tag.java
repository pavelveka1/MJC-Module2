package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tags")
@NamedQueries({
        @NamedQuery(name = "Tag.findById",
                query = "select distinct t from Tag t where t.id = :id"),
        @NamedQuery(name = "Tag.findByCertificateId",
                query = "select distinct t from Tag t " +
                        "inner join fetch t.giftCertificateList as certificates where certificates.id = :id"),
        @NamedQuery(name = "Tag.getTagByName",
                query = "select distinct t from Tag t  where name = :name")
})
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<GiftCertificate> giftCertificateList;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    public long getId() {
        return id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "gift_certificates_has_tags",
            joinColumns = @JoinColumn(name = "tags_id"),
            inverseJoinColumns = @JoinColumn(name = "gift_certificates_id"))
    public List<GiftCertificate> getGiftCertificateList() {
        return giftCertificateList;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '}';
    }
}
