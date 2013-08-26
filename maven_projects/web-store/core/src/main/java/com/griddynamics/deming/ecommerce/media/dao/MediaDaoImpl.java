package com.griddynamics.deming.ecommerce.media.dao;

import org.broadleafcommerce.common.media.domain.Media;
import org.broadleafcommerce.common.media.domain.MediaImpl;
import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository("dmgMediaDao")
public class MediaDaoImpl implements MediaDao {

    @PersistenceContext(unitName="blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Nonnull
    @Override
    public Media create() {
        return (Media) entityConfiguration.createEntityInstance(Media.class.getName());
    }

    @Nonnull
    @Override
    public Media readMediaById(Long id) {
        return em.find(MediaImpl.class, id);
    }

    @Nonnull
    @Override
    public Media save(@Nonnull Media media) {
        return em.merge(media);
    }

    @Override
    public void delete(@Nonnull Media media) {
        if (!em.contains(media)) {
            media = readMediaById(media.getId());
        }
        em.remove(media);
    }
}
