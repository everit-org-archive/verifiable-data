package org.everit.verifiabledata.api;

import java.util.Date;

import org.everit.verifiabledata.api.dto.VerifiableDataCreation;
import org.everit.verifiabledata.api.dto.VerificationRequest;
import org.everit.verifiabledata.api.dto.VerificationResult;
import org.everit.verifiabledata.api.enums.VerificationLengthBase;

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

public interface VerifyService {

    /**
     * Create a new verifiable data and request.
     * 
     * @param tokenValidityEndDate
     *            the expiration date of the token. Cannot be <code>null</code>.
     * @param verificationLength
     *            the verification length in seconds.
     * @param verificationLengthBase
     *            the {@link VerificationLengthBase} that is valid for the request. Cannot be <code>null</code>.
     * @return the {@link VerifiableDataCreation} object. If not created the verifiable data return <code>null</code>.
     * 
     * @throws IllegalArgumentException
     *             if one parameter is <code>null</code>.
     **/
    VerifiableDataCreation createVerifiableData(final Date tokenValidityEndDate, final long verificationLength,
            final VerificationLengthBase verificationLengthBase);

    /**
     * Create a new verification request in exist verifiable data. All existing verifiable that belongs to the
     * verifiable date request is withdraw.
     * 
     * @param verifiableDataId
     *            the id of the verifiable data. Must be exist verifiable data.
     * @param tokenValidityEndDate
     *            the expiration date of the token. Cannot be <code>null</code>.
     * @param verificationLength
     *            the verification length in seconds. Cannot be <code>null</code>.
     * @param verificationLengthBase
     *            the {@link VerificationLengthBase} that is valid for the request. Cannot be <code>null</code>.
     * @return the {@link VerificationRequest} object. If not creating the verifiable request return <code>null</code>.
     * 
     * @throws IllegalArgumentException
     *             if one parameter is <code>null</code> or the verifiable data is not exist.
     */
    VerificationRequest createVerificationRequest(final long verifiableDataId, final Date tokenValidityEndDate,
            final long verificationLength, final VerificationLengthBase verificationLengthBase);

    /**
     * Get verification end date of the verifiable data.
     * 
     * @param verifiableDataId
     *            the id of the verifiable data. Must be exist verifiable data.
     * @return the verification end data if bigger than actual date otherwise <code>null</code>.
     * 
     * @throws IllegalArgumentException
     *             if the not exist verifiable data or the verifiable data id is <code>null</code> or the verifiable
     *             data is not exist.
     */
    Date getVerificationEndDate(long verifiableDataId);

    /**
     * It cuts down on a verified end date to the given date.
     * 
     * @param verifiableDataId
     *            the id of the verifiable data. Must be valid id. Must be exist verifiable data.
     * @param verificationEndDate
     *            the new verification date. Cannot be <code>null</code>.
     * @return <code>true</code> if the cuts down successful ready. Return <code>false</code> if the specified date
     *         preceded in original date.
     * @throws IllegalArgumentException
     *             if the verificationEndDate before than actual date or the verificationEndDate is not reduce the
     *             verification end date or if one parameter is <code>null</code> or the verifiable data is not exits or
     *             the verifiable data is not exist.
     */
    boolean reduceVerificationEndDate(final long verifiableDataId, final Date verificationEndDate);

    /**
     * Revoke the verification request.
     * 
     * @param verificationRequestId
     *            the id of the verification request. Must be exist verifiable request.
     * 
     * @throws IllegalArgumentException
     *             if the parameter is <code>null</code> or the verifiable data is not exits or the verifiable request
     *             is not exist.
     */
    void revokeVerificationRequest(long verificationRequestId);

    /**
     * Verifying the data.
     * 
     * @param token
     *            the token. Cannot be <code>null</code>.
     * @return the {@link VerificationRequest} object. If the token invalid return <code>null</code>.
     * 
     * @throws IllegalArgumentException
     *             if parameter is <code>null</code>.
     */
    VerificationResult verifyData(final String token);

}
