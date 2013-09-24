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

public class VerifiableDataCreation {

    private long verifiableDataId;

    private String approveToken;

    private String rejectToken;

    public VerifiableDataCreation(final long verifiableDataId, final String approveToken, final String rejectToken) {
        super();
        this.verifiableDataId = verifiableDataId;
        this.approveToken = approveToken;
        this.rejectToken = rejectToken;
    }

    public String getApproveToken() {
        return approveToken;
    }

    public String getRejectToken() {
        return rejectToken;
    }

    public long getVerifiableDataId() {
        return verifiableDataId;
    }

    public void setApproveToken(final String approveToken) {
        this.approveToken = approveToken;
    }

    public void setRejectToken(final String rejectToken) {
        this.rejectToken = rejectToken;
    }

    public void setVerifiableDataId(final long verifiableDataId) {
        this.verifiableDataId = verifiableDataId;
    }
}
