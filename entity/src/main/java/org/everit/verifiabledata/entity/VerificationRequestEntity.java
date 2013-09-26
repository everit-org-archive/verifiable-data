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

    public VerificationRequestEntity() {
    }

}
