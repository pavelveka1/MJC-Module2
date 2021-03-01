package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Interface GiftCertificateDAO.
 * Contains methods for work with GiftCertificate class
 */
public interface GiftCertificateDAO extends PagingAndSortingRepository<GiftCertificate, Long>, GiftCertificateDAOCustom {

    GiftCertificate readByName(String certificateName);

    /**
     * Read only certificates which are not deleted
     *
     * @param certificateName Name of certificate
     * @return GiftCertificate by name
     */
    @Query("select gc from GiftCertificate as gc where gc.deleted=false and name=:certificateName")
    GiftCertificate readByNotDeletedName(String certificateName);

    @Query("select gc from GiftCertificate as gc where gc.id=:id and gc.deleted=false")
    GiftCertificate readById(Long id);

    /**
     * Delete Tag from DB by id
     *
     * @param certificateId - certificate with id will be deleted from DB
     */
    @Query("update GiftCertificate as gc set gc.deleted = :true where gc.id = :certificateId")
    void setDeletedFlag(Long certificateId);

}
