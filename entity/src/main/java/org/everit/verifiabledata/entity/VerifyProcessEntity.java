package org.everit.verifiabledata.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.everit.verifiabledata.api.TokenUsageResult;

@Entity
@Table(name = "VERIFY_PROCESS")
public class VerifyProcessEntity {

    @Id
    @GeneratedValue
    @Column(name = "VERIFICATION_PROCESS_ID")
    private long verificationProcessId;

    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationData;

    @Column(name = "TOKEN_BASE", length = 50)
    private String tokenBase;

    @Column(name = "TOKEN_VALIDITY_END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tokenValidityEndDate;

    @Column(name = "TOKEN_USAGE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tokenUsageDate;

    @Column(name = "TOKEN_USAGE_RESULT")
    private TokenUsageResult tokenUsageResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VERIFIABLE_DATA_ID", nullable = false, referencedColumnName = "VERIFIABLE_DATA_ID")
    private VerifyDataEntity verifyData;

    public VerifyProcessEntity() {
    }

    public VerifyProcessEntity(final long verificationProcessId, final Date creationData, final String tokenBase,
            final Date tokenValidityEndDate, final Date tokenUsageDate, final TokenUsageResult tokenUsageResult,
            final VerifyDataEntity verifyData) {
        super();
        this.verificationProcessId = verificationProcessId;
        this.creationData = creationData;
        this.tokenBase = tokenBase;
        this.tokenValidityEndDate = tokenValidityEndDate;
        this.tokenUsageDate = tokenUsageDate;
        this.tokenUsageResult = tokenUsageResult;
        this.verifyData = verifyData;
    }

    public Date getCreationData() {
        return creationData;
    }

    public String getTokenBase() {
        return tokenBase;
    }

    public Date getTokenUsageDate() {
        return tokenUsageDate;
    }

    public TokenUsageResult getTokenUsageResult() {
        return tokenUsageResult;
    }

    public Date getTokenValidityEndDate() {
        return tokenValidityEndDate;
    }

    public long getVerificationProcessId() {
        return verificationProcessId;
    }

    public VerifyDataEntity getVerifyData() {
        return verifyData;
    }

    public void setCreationData(final Date creationData) {
        this.creationData = creationData;
    }

    public void setTokenBase(final String tokenBase) {
        this.tokenBase = tokenBase;
    }

    public void setTokenUsageDate(final Date tokenUsageDate) {
        this.tokenUsageDate = tokenUsageDate;
    }

    public void setTokenUsageResult(final TokenUsageResult tokenUsageResult) {
        this.tokenUsageResult = tokenUsageResult;
    }

    public void setTokenValidityEndDate(final Date tokenValidityEndDate) {
        this.tokenValidityEndDate = tokenValidityEndDate;
    }

    public void setVerificationProcessId(final long verificationProcessId) {
        this.verificationProcessId = verificationProcessId;
    }

    public void setVerifyData(final VerifyDataEntity verifyData) {
        this.verifyData = verifyData;
    }

}
