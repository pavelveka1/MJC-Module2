package com.epam.esm.entity;

        import javax.persistence.*;

        import org.apache.log4j.Logger;

public class AuditListener {

    private static final Logger logger = Logger.getLogger(AuditListener.class);
    private static final String PRE_INSERT="Pre insert: ";
    private static final String POST_INSERT="Post insert: ";
    private static final String PRE_UPDATE="Pre update: ";
    private static final String POST_UPDATE="Post update: ";
    private static final String PRE_REMOVE="Pre remove: ";
    private static final String POST_REMOVE="Post remove: ";

    @PrePersist
    public void onPrePersist(Object object) {
        logger.info(PRE_INSERT+object);
    }

    @PostPersist
    public void onPostPersist(Object object) {
        logger.info(POST_INSERT+ object);
    }

    @PreUpdate
    public void onPreUpdate(Object object) {
        logger.info(PRE_UPDATE+object);

    }

    @PostUpdate
    public void onPostUpdate(Object object) {
        logger.info(POST_UPDATE+object);

    }

    @PreRemove
    public void onPreRemove(Object object) {
        logger.info(PRE_REMOVE+object);

    }

    @PostRemove
    public void onPostRemove(Object object) {
        logger.info(POST_REMOVE+object);

    }
}


