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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.everit.verifiabledata.api.VerifyDataStatus;

@Entity
@Table(name = "VERIFY_DATA")
public class VerifyDataEntity {

    @Id
    @GeneratedValue
    @Column(name = "VERIFIABLE_DATA_ID")
    private long verifiableDataId;

    @Column(name = "STATUS")
    private VerifyDataStatus status;

    @Column(name = "STATUS_VALIDITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date statusValidityDate;

    @OneToMany(mappedBy = "verifyData", fetch = FetchType.LAZY, targetEntity = VerifyProcessEntity.class)
    private List<VerifyProcessEntity> verifyProcesses;

    public VerifyDataEntity() {
    }

    public VerifyDataEntity(final long verifiableDataId, final VerifyDataStatus status, final Date statusValidityDate) {
        super();
        this.verifiableDataId = verifiableDataId;
        this.status = status;
        this.statusValidityDate = statusValidityDate;
    }

    public VerifyDataStatus getStatus() {
        return status;
    }

    public Date getStatusValidityDate() {
        return statusValidityDate;
    }

    public long getVerifiableDataId() {
        return verifiableDataId;
    }

    public List<VerifyProcessEntity> getVerifyProcesses() {
        return verifyProcesses;
    }

    public void setStatus(final VerifyDataStatus status) {
        this.status = status;
    }

    public void setStatusValidityDate(final Date statusValidityDate) {
        this.statusValidityDate = statusValidityDate;
    }

    public void setVerifiableDataId(final long verifiableDataId) {
        this.verifiableDataId = verifiableDataId;
    }

    public void setVerifyProcesses(final List<VerifyProcessEntity> verifyProcesses) {
        this.verifyProcesses = verifyProcesses;
    }

}
