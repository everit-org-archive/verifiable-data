package org.everit.verifiabledata.api.dto;

import org.everit.verifiabledata.api.enums.TokenUsageResult;

public class VerificationResult {

    private Long verifiableDataId;

    private TokenUsageResult tokenUsageResult;

    public VerificationResult(final Long verifiableDataId, final TokenUsageResult tokenUsageResult) {
        super();
        this.verifiableDataId = verifiableDataId;
        this.tokenUsageResult = tokenUsageResult;
    }

    public TokenUsageResult getTokenUsageResult() {
        return tokenUsageResult;
    }

    public Long getVerifiableDataId() {
        return verifiableDataId;
    }

    public void setTokenUsageResult(final TokenUsageResult tokenUsageResult) {
        this.tokenUsageResult = tokenUsageResult;
    }

    public void setVerifiableDataId(final Long verifiableDataId) {
        this.verifiableDataId = verifiableDataId;
    }

}
