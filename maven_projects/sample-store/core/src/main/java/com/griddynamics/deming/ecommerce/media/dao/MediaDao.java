package com.griddynamics.deming.ecommerce.media.dao;

import org.broadleafcommerce.common.media.domain.Media;

import javax.annotation.Nonnull;

public interface MediaDao {

    @Nonnull
    Media create();

    @Nonnull
    Media readMediaById(Long id);

    @Nonnull
    Media save(@Nonnull Media media);

    void delete(@Nonnull Media media);
}
