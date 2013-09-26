package org.everit.verifiabledata.core;

/*
 * Copyright (c) 2011, Everit Kft.
 *
 * All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

import java.util.Date;

import javax.persistence.EntityManager;

import org.everit.verifiabledata.api.VerifyService;
import org.everit.verifiabledata.api.dto.VerifiableDataCreation;
import org.everit.verifiabledata.api.enums.VerifyDataStatus;
import org.everit.verifiabledata.entity.VerifyDataEntity;
import org.everit.verifiabledata.entity.VerifyProcessEntity;

public class VerifyServiceImpl implements VerifyService {

    /**
     * EntityManager set by blueprint.
     */
    private EntityManager em;

    @Override
    public VerifiableDataCreation createVerifiableData(final Date tokenValidityEndDate) {
        VerifyDataEntity verifyDataEntity = new VerifyDataEntity();
        verifyDataEntity.setStatusValidityDate(tokenValidityEndDate);
        verifyDataEntity.setStatus(VerifyDataStatus.WAITING_VERIFICATION);
        em.persist(verifyDataEntity);
        em.flush();
        verifyDataEntity.getVerifiableDataId();
        VerifyProcessEntity verifyProcessEntity = new VerifyProcessEntity();
        verifyProcessEntity.setTokenValidityEndDate(tokenValidityEndDate);
        verifyProcessEntity.setVerifyData(verifyDataEntity);
        em.persist(verifyProcessEntity);
        em.flush();
        VerifiableDataCreation verifiableDataCreation = new VerifiableDataCreation(
                verifyDataEntity.getVerifiableDataId(), "", "");
        return verifiableDataCreation;
    }

    @Override
    public VerifiableDataCreation select(final long id) {
        System.out.println("--------------------");
        System.out.println("id: " + id);
        VerifyDataEntity find = em.find(VerifyDataEntity.class, id);
        System.out.println("--------------------");
        System.out.println("status: " + find.getStatus());
        VerifiableDataCreation verifiableDataCreation = new VerifiableDataCreation(
                find.getVerifiableDataId(), "", "");
        return verifiableDataCreation;
    }

    public void setEm(final EntityManager em) {
        this.em = em;
    }

}
