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
import org.everit.verifiabledata.api.dto.VerificationRequest;
import org.everit.verifiabledata.api.dto.VerificationResult;
import org.everit.verifiabledata.api.enums.VerificationLengthBase;

public class VerifyServiceImpl implements VerifyService {

    /**
     * EntityManager set by blueprint.
     */
    private EntityManager em;

    @Override
    public VerifiableDataCreation createVerifiableData(final Date tokenValidityEndDate, final long verificationLength,
            final VerificationLengthBase verificationLengthBase) {
        if ((tokenValidityEndDate == null) || (verificationLengthBase == null)) {
            throw new IllegalArgumentException("The one paramter is null. Cannot be null the paramters.");
        }

        return null;
    }

    @Override
    public VerificationRequest createVerificationRequest(final long verifiableDataId, final Date tokenValidityEndDate,
            final long verificationLength, final VerificationLengthBase verificationLengthBase) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Date getVerificationEndDate(final long verifiableDataId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean reduceVerificationEndDate(final long verifiableDataId, final Date verificationEndDate) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void revokeVerificationRequest(final long verificationRequestId) {
        // TODO Auto-generated method stub

    }

    public void setEm(final EntityManager em) {
        this.em = em;
    }

    @Override
    public VerificationResult verifyData(final String token) {
        // TODO Auto-generated method stub
        return null;
    }

}
