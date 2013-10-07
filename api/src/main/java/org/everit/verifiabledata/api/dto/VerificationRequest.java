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
 * Information of the verification request.
 */
public class VerificationRequest {

    /**
     * The id of the verification request.
     */
    private long verificationRequestId;
    /**
     * The token UUID to the verify.
     */
    private String verifyTokenUUID;

    /**
     * The token UUID to reject.
     */
    private String rejectTokenUUID;

    /**
     * The simple constructor.
     * 
     * @param verificationRequestId
     *            the id of the verification request.
     * @param verifyTokenUUID
     *            the token UUID to verify.
     * @param rejectTokenUUID
     *            the token UUID to reject.
     */
    public VerificationRequest(final long verificationRequestId, final String verifyTokenUUID,
            final String rejectTokenUUID) {
        super();
        this.verificationRequestId = verificationRequestId;
        this.verifyTokenUUID = verifyTokenUUID;
        this.rejectTokenUUID = rejectTokenUUID;
    }

    public String getRejectTokenUUID() {
        return rejectTokenUUID;
    }

    public long getVerificationRequestId() {
        return verificationRequestId;
    }

    public String getVerifyTokenUUID() {
        return verifyTokenUUID;
    }

    public void setRejectTokenUUID(final String rejectTokenUUID) {
        this.rejectTokenUUID = rejectTokenUUID;
    }

    public void setVerificationRequestId(final long verificationRequestId) {
        this.verificationRequestId = verificationRequestId;
    }

    public void setVerifyTokenUUID(final String verifyTokenUUID) {
        this.verifyTokenUUID = verifyTokenUUID;
    }

}
