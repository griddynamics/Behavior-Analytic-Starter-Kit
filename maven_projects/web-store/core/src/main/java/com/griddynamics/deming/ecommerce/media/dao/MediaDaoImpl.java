/*
* Copyright 2013 Grid Dynamics, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

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
