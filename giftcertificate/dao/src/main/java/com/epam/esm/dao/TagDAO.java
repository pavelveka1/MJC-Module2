package com.epam.esm.dao;

import java.util.List;

import com.epam.esm.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface TagDAO.
 * Contains methods for work with Tag class
 */
@Repository
public interface TagDAO extends PagingAndSortingRepository<Tag, Long> {

    /**
     * Find all Tags
     *
     * @return list of Tags
     */
    Page<Tag> findAll(Pageable pageable);

    /**
     * Get list of GiftCertificate by tag id
     *
     * @param certificateId id of gift certificate
     * @return list of tag
     */
    @Query("select distinct t from Tag t inner join fetch t.giftCertificateList as certificates where " +
            "certificates.id = :certificateId")
    List<Tag> getTagsByGiftCertificateId(long certificateId);

    /**
     * Get Tag by name
     *
     * @param name name of tag
     * @return Tag
     */
    Tag getTagByName(String name);

    /**
     * Get widely used by user tag with max sun cost
     *
     * @param userId id of user
     * @return id of widely used by user tag
     */
    @Query(value = " select tag_id from (" +
            "   select tags.tag_id, tags.name,  count(*) as quantity, sum(cost) as sum_cost, users_id from orders" +
            "          join gift_certificates_has_orders on orders.id=gift_certificates_has_orders.orders_id" +
            "          join gift_certificates on gift_certificates.id=gift_certificates_has_orders.gift_certificates_id" +
            "          join gift_certificates_has_tags on gift_certificates.id=gift_certificates_has_tags.gift_certificates_id" +
            "          join tags on tags.tag_id=gift_certificates_has_tags.tags_id where users_id=:userId " +
            "group by tags.name order by quantity desc, sum_cost desc limit 1) as T",
            nativeQuery = true
    )
    Long getIdWidelyUsedByUserTagWithHighestCost(long userId);

}
