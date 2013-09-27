package org.everit.verifiabledata.api.dto;

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

/**
 * Information of the verifiable data.
 */
public class VerifiableDataCreation {

    /**
     * The id of the verifiable data.
     */
    private long verifiableDataId;

    /**
     * The verification request.
     */
    private VerificationRequest verificationRequest;

    /**
     * The simple constructor.
     * 
     * @param verifiableDataId
     *            the id of the verifiable data.
     * @param verificationRequest
     *            the verification request.
     */
    public VerifiableDataCreation(final long verifiableDataId, final VerificationRequest verificationRequest) {
        super();
        this.verifiableDataId = verifiableDataId;
        this.verificationRequest = verificationRequest;
    }

    public long getVerifiableDataId() {
        return verifiableDataId;
    }

    public VerificationRequest getVerificationRequest() {
        return verificationRequest;
    }

    public void setVerifiableDataId(final long verifiableDataId) {
        this.verifiableDataId = verifiableDataId;
    }

    public void setVerificationRequest(final VerificationRequest verificationRequest) {
        this.verificationRequest = verificationRequest;
    }

}
