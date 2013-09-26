package org.everit.verifiabledata.api.dto;

public class VerificationRequest {

    private String verifyTokenUUID;

    private String rejectTokenUUID;

    public VerificationRequest(final String verifyTokenUUID, final String rejectTokenUUID) {
        super();
        this.verifyTokenUUID = verifyTokenUUID;
        this.rejectTokenUUID = rejectTokenUUID;
    }

    public String getRejectTokenUUID() {
        return rejectTokenUUID;
    }

    public String getVerifyTokenUUID() {
        return verifyTokenUUID;
    }

    public void setRejectTokenUUID(final String rejectTokenUUID) {
        this.rejectTokenUUID = rejectTokenUUID;
    }

    public void setVerifyTokenUUID(final String verifyTokenUUID) {
        this.verifyTokenUUID = verifyTokenUUID;
    }

}
