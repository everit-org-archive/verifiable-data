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

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The entity of the verifiable data.
 */
@Entity
@Table(name = "VERIFY_VERIFIABLE_DATA")
public class VerifiableDataEntity {

    /**
     * The if of the verifiable data.
     */
    @Id
    @GeneratedValue
    @Column(name = "VERIFIABLE_DATA_ID")
    private long verifiableDataId;

    /**
     * The expiration date of the verifiable data. If <code>null</code> is data never verified.
     */
    @Column(name = "VERIFIED_UNTIL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date verifiedUntil;

    // /**
    // * The verification requests list.
    // */
    // @OneToMany(mappedBy = "verifiableData", fetch = FetchType.LAZY, targetEntity = VerificationRequestEntity.class)
    // private List<VerificationRequestEntity> verificationRequestes;

    /**
     * The default constructor.
     */
    public VerifiableDataEntity() {
    }

    /**
     * The simple constructor.
     * 
     * @param verifiableDataId
     *            the id of the verifiable data.
     * @param verifiedUntil
     *            the expiration date of the verifiable data.
     */
    public VerifiableDataEntity(final long verifiableDataId, final Date verifiedUntil) {
        super();
        this.verifiableDataId = verifiableDataId;
        this.verifiedUntil = verifiedUntil;
    }

    public long getVerifiableDataId() {
        return verifiableDataId;
    }

    // public List<VerificationRequestEntity> getVerificationRequestes() {
    // return verificationRequestes;
    // }

    public Date getVerifiedUntil() {
        return verifiedUntil;
    }

    public void setVerifiableDataId(final long verifiableDataId) {
        this.verifiableDataId = verifiableDataId;
    }

    // public void setVerificationRequestes(final List<VerificationRequestEntity> verificationRequestes) {
    // this.verificationRequestes = verificationRequestes;
    // }

    public void setVerifiedUntil(final Date verifiedUntil) {
        this.verifiedUntil = verifiedUntil;
    }

}
