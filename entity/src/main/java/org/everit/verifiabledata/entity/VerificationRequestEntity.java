package org.everit.verifiabledata.entity;

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.everit.token.entity.TokenEntity;
import org.everit.verifiabledata.api.enums.VerificationLengthBase;

/**
 * The entity of the verification request.
 */
@Entity
@Table(name = "VERIFY_VERIFICATION_REQUEST")
public class VerificationRequestEntity {

    /**
     * The id of the verification request.
     */
    @Id
    @GeneratedValue
    @Column(name = "VERIFICATION_REQUEST_ID")
    private long verificationRequestId;

    /**
     * The length of the verification data. The length in seconds.
     */
    @Column(name = "VERIFICATION_LENGTH")
    private long verificationLength;

    /**
     * The token UUID of the verification request.
     */
    @ManyToOne
    @JoinColumn(name = "TOKEN_UUID")
    private TokenEntity token;

    /**
     * The type of {@link VerificationLengthBase}.
     */
    @Column(name = "VERIFICATION_LENGTH_BASE")
    @Enumerated(EnumType.STRING)
    private VerificationLengthBase verificationLengthBase;

    /**
     * The verifiable data.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VERIFIABLE_DATA_ID", nullable = false, referencedColumnName = "VERIFIABLE_DATA_ID")
    private VerifiableDataEntity verifiableData;

    /**
     * The default constructor.
     */
    public VerificationRequestEntity() {
    }

    /**
     * The simple constructor.
     * 
     * @param verificationRequestId
     *            the id of the verification request.
     * @param verificationLength
     *            the length of the verification request.
     * @param tokenUuid
     *            the token UUID of the verification request.
     * @param verificationLengthBase
     *            the verification length base of the verification request.
     * @param verifiableData
     *            the verifiable data object of the verification request.
     */
    public VerificationRequestEntity(final long verificationRequestId, final long verificationLength,
            final TokenEntity tokenUuid,
            final VerificationLengthBase verificationLengthBase, final VerifiableDataEntity verifiableData) {
        super();
        this.verificationRequestId = verificationRequestId;
        this.verificationLength = verificationLength;
        token = tokenUuid;
        this.verificationLengthBase = verificationLengthBase;
        this.verifiableData = verifiableData;
    }

    public TokenEntity getToken() {
        return token;
    }

    public VerifiableDataEntity getVerifiableData() {
        return verifiableData;
    }

    public long getVerificationLength() {
        return verificationLength;
    }

    public VerificationLengthBase getVerificationLengthBase() {
        return verificationLengthBase;
    }

    public long getVerificationRequestId() {
        return verificationRequestId;
    }

    public void setToken(final TokenEntity token) {
        this.token = token;
    }

    public void setVerifiableData(final VerifiableDataEntity verifiableData) {
        this.verifiableData = verifiableData;
    }

    public void setVerificationLength(final long verificationLength) {
        this.verificationLength = verificationLength;
    }

    public void setVerificationLengthBase(final VerificationLengthBase verificationLengthBase) {
        this.verificationLengthBase = verificationLengthBase;
    }

    public void setVerificationRequestId(final long verificationRequestId) {
        this.verificationRequestId = verificationRequestId;
    }

}
