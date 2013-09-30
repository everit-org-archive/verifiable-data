package org.everit.verifiabledata.itests.core;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.everit.verifiabledata.api.VerifyService;
import org.everit.verifiabledata.api.dto.VerifiableDataCreation;
import org.everit.verifiabledata.api.dto.VerificationRequest;
import org.everit.verifiabledata.api.dto.VerificationResult;
import org.everit.verifiabledata.api.enums.TokenUsageResult;
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

/**
 * Implementation of {@link VerifyTest}.
 */
public class VerifyTestImpl implements VerifyTest {

    /**
     * The {@link VerifyService} instance.
     */
    private VerifyService verifyService;

    /**
     * 10 hour in seconds.
     */
    private static final int TEN_HOUR_IN_SEC = 36000;

    /**
     * The default constant size.
     */
    private static final int CONSTANT = 200;

    /**
     * Creating verifiable datas.
     * 
     * @return the {@link VerifiableDataCreation} list.
     */
    private List<VerifiableDataCreation> createVerifiableDataCreation() {
        List<VerifiableDataCreation> verifiableDataCreations = new ArrayList<VerifiableDataCreation>();
        for (int i = 0; i < CONSTANT; i++) {
            Date actualDate = new Date();
            Date tokenValidityEndDate = getValidTokenValidityEndDate();
            long verificationLength = tokenValidityEndDate.getTime() - actualDate.getTime() - (2 * TEN_HOUR_IN_SEC);
            VerifiableDataCreation createVerifiableData = verifyService.createVerifiableData(tokenValidityEndDate,
                    verificationLength,
                    getRandomVerificationLengthBase());
            Assert.assertNotNull(createVerifiableData);
            verifiableDataCreations.add(createVerifiableData);
        }
        return verifiableDataCreations;
    }

    /**
     * Select random {@link VerificationLengthBase}.
     * 
     * @return the random {@link VerificationLengthBase}.
     */
    private VerificationLengthBase getRandomVerificationLengthBase() {
        VerificationLengthBase result = null;
        Random random = new Random();
        int number = random.nextInt(2);
        if (number == 0) {
            result = VerificationLengthBase.REQUEST_CREATION;
        } else {
            result = VerificationLengthBase.VERIFICATION;
        }
        return result;
    }

    /**
     * Determine the valid token validity end date.
     * 
     * @return the validity end date.
     */
    private Date getValidTokenValidityEndDate() {
        Date actualDate = new Date();
        Calendar tokenValidityEndDate = Calendar.getInstance();
        tokenValidityEndDate.setTime(actualDate);
        Random random = new Random();
        int days = random.nextInt(CONSTANT) + 1;
        tokenValidityEndDate.add(Calendar.DATE, days);
        return tokenValidityEndDate.getTime();
    }

    public void setVerifyService(final VerifyService verifyService) {
        this.verifyService = verifyService;
    }

    @Override
    public void testCreates() {
        List<VerifiableDataCreation> verifiableDataCreations = createVerifiableDataCreation();
        for (int i = 0; i < CONSTANT; i++) {
            Date actualDate = new Date();
            Date tokenValidityEndDate = getValidTokenValidityEndDate();
            long verificationLength = tokenValidityEndDate.getTime() - actualDate.getTime() - (2 * TEN_HOUR_IN_SEC);

            // Testing the null validity end date.
            try {
                verifyService.createVerifiableData(null,
                        verificationLength,
                        getRandomVerificationLengthBase());
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing the null verification length base.
            try {
                verifyService.createVerifiableData(tokenValidityEndDate,
                        verificationLength,
                        null);
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing the negative verification length.
            try {
                verifyService.createVerifiableData(tokenValidityEndDate,
                        -1L,
                        getRandomVerificationLengthBase());
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing the zero verification length.
            try {
                verifyService.createVerifiableData(tokenValidityEndDate,
                        0L,
                        getRandomVerificationLengthBase());
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing null verification length base and negative verification length.
            try {
                verifyService.createVerifiableData(tokenValidityEndDate,
                        -1L,
                        null);
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }
        }

        for (VerifiableDataCreation vdc : verifiableDataCreations) {
            // Testing the createVerificationRequest.
            Date actualDate = new Date();
            Date tokenValidityEndDate = getValidTokenValidityEndDate();
            long verificationLength = tokenValidityEndDate.getTime() - actualDate.getTime() - (2 * TEN_HOUR_IN_SEC);
            VerificationRequest createVerificationRequest = verifyService.createVerificationRequest(
                    vdc.getVerifiableDataId(),
                    tokenValidityEndDate,
                    verificationLength,
                    getRandomVerificationLengthBase());
            Assert.assertNotNull(createVerificationRequest);

            // Testing invalid verification data id.
            try {
                verifyService.createVerificationRequest(
                        0L,
                        tokenValidityEndDate,
                        verificationLength,
                        getRandomVerificationLengthBase());
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing the negative invalid verification data id.
            try {
                verifyService.createVerificationRequest(
                        -1L,
                        tokenValidityEndDate,
                        verificationLength,
                        getRandomVerificationLengthBase());
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing the null validity end date.
            try {
                verifyService.createVerificationRequest(
                        vdc.getVerifiableDataId(),
                        null,
                        verificationLength,
                        getRandomVerificationLengthBase());
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing the negative verification length.
            try {
                verifyService.createVerificationRequest(
                        vdc.getVerifiableDataId(),
                        tokenValidityEndDate,
                        -1L,
                        getRandomVerificationLengthBase());
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing the zero verification length.
            try {
                verifyService.createVerificationRequest(
                        vdc.getVerifiableDataId(),
                        tokenValidityEndDate,
                        0L,
                        getRandomVerificationLengthBase());
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing null verification length base.
            try {
                verifyService.createVerificationRequest(
                        vdc.getVerifiableDataId(),
                        tokenValidityEndDate,
                        verificationLength,
                        null);
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing negative invalid verification data id and negative verification length.
            try {
                verifyService.createVerificationRequest(
                        -1L,
                        tokenValidityEndDate,
                        -1L,
                        getRandomVerificationLengthBase());
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }
        }

    }

    @Override
    public void testGetVerificationEndDate() {
        List<VerifiableDataCreation> verifiableDataCreationsExpired = new ArrayList<VerifiableDataCreation>();
        // Create expired verifiable datas.
        for (int i = 0; i < CONSTANT; i++) {
            Date actualDate = new Date();
            long time = actualDate.getTime() + (4 * CONSTANT);
            Date tokenValidityEndDate = new Date(time);
            long verificationLength = CONSTANT / CONSTANT;
            VerifiableDataCreation createVerifiableData = verifyService.createVerifiableData(tokenValidityEndDate,
                    verificationLength,
                    getRandomVerificationLengthBase());
            Assert.assertNotNull(createVerifiableData);
            verifiableDataCreationsExpired.add(createVerifiableData);
        }

        List<VerifiableDataCreation> verifiableDataCreations = createVerifiableDataCreation();
        // Create expired verifiable datas with new verification requests.
        for (VerifiableDataCreation vdc : verifiableDataCreations) {
            Date actualDate = new Date();
            long time = actualDate.getTime() + (4 * CONSTANT);
            Date tokenValidityEndDate = new Date(time);
            long verificationLength = CONSTANT / CONSTANT;
            VerificationRequest createVerificationRequest = verifyService.createVerificationRequest(
                    vdc.getVerifiableDataId(),
                    tokenValidityEndDate,
                    verificationLength,
                    getRandomVerificationLengthBase());
            Assert.assertNotNull(createVerificationRequest);
            VerifiableDataCreation verifiableDataCreation = new VerifiableDataCreation(vdc.getVerifiableDataId(),
                    createVerificationRequest);
            verifiableDataCreationsExpired.add(verifiableDataCreation);
        }

        // Creating not expired verifiable datas.
        verifiableDataCreations = createVerifiableDataCreation();
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());

        for (VerifiableDataCreation vdc : verifiableDataCreations) {
            // Testing the getVerification end date in the not expired verifiable data.
            Date verificationEndDate = verifyService.getVerificationEndDate(vdc.getVerifiableDataId());
            Date actualDate = new Date();
            Assert.assertNotNull(verificationEndDate);
            Assert.assertTrue(actualDate.getTime() < verificationEndDate.getTime());

            // Testing the invalid verifiable data id.
            try {
                verifyService.getVerificationEndDate(0L);
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing the negative invalid verifiable data id.
            try {
                verifyService.getVerificationEndDate(-1L);
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }
        }

        for (VerifiableDataCreation vdc : verifiableDataCreationsExpired) {
            // Testing the getVerificationEndDate in the expired verifiable data.
            Date verificationEndDate = verifyService.getVerificationEndDate(vdc.getVerifiableDataId());
            Assert.assertTrue(verificationEndDate == null);
        }
    }

    @Override
    public void testReduceVerificationEndDate() {
        List<VerifiableDataCreation> verifiableDataCreationsExpired = new ArrayList<VerifiableDataCreation>();
        // Create expired verifiable datas.
        for (int i = 0; i < CONSTANT; i++) {
            Date actualDate = new Date();
            long time = actualDate.getTime() + (4 * CONSTANT);
            Date tokenValidityEndDate = new Date(time);
            long verificationLength = CONSTANT / CONSTANT;
            VerifiableDataCreation createVerifiableData = verifyService.createVerifiableData(tokenValidityEndDate,
                    verificationLength,
                    getRandomVerificationLengthBase());
            Assert.assertNotNull(createVerifiableData);
            verifiableDataCreationsExpired.add(createVerifiableData);
        }

        List<VerifiableDataCreation> verifiableDataCreations = createVerifiableDataCreation();
        // Create new verification request.
        for (VerifiableDataCreation vdc : verifiableDataCreations) {
            Date actualDate = new Date();
            long time = actualDate.getTime() + (4 * CONSTANT);
            Date tokenValidityEndDate = new Date(time);
            long verificationLength = CONSTANT / CONSTANT;
            VerificationRequest createVerificationRequest = verifyService.createVerificationRequest(
                    vdc.getVerifiableDataId(),
                    tokenValidityEndDate,
                    verificationLength,
                    getRandomVerificationLengthBase());
            Assert.assertNotNull(createVerificationRequest);
            VerifiableDataCreation verifiableDataCreation = new VerifiableDataCreation(vdc.getVerifiableDataId(),
                    createVerificationRequest);
            verifiableDataCreationsExpired.add(verifiableDataCreation);
        }

        // Create verifable datas.
        verifiableDataCreations = createVerifiableDataCreation();
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());

        for (VerifiableDataCreation vdc : verifiableDataCreations) {
            // Testing reduceVericationEndDate method in the not expired verifiable datas.
            Date actualDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(actualDate);
            c.add(Calendar.SECOND, CONSTANT);
            boolean reduceVerificationEndDate = verifyService.reduceVerificationEndDate(vdc.getVerifiableDataId(),
                    c.getTime());
            Assert.assertTrue(reduceVerificationEndDate);

            // Testing invalid verifiable data id.
            try {
                verifyService.reduceVerificationEndDate(0L, c.getTime());
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing the negative invalid verifiable data id.
            try {
                verifyService.reduceVerificationEndDate(-1L, c.getTime());
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing the null verification end date.
            reduceVerificationEndDate = verifyService.reduceVerificationEndDate(vdc.getVerifiableDataId(), null);
            Assert.assertTrue(reduceVerificationEndDate);
        }

        for (VerifiableDataCreation vdc : verifiableDataCreationsExpired) {
            // Testing the reduceVerifactionEndDate in expired verifiable data.
            try {
                verifyService.reduceVerificationEndDate(vdc.getVerifiableDataId(),
                        new Date());
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing null verification end date in expired verifiable data.
            try {
                verifyService.reduceVerificationEndDate(vdc.getVerifiableDataId(),
                        null);
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

        }
    }

    @Override
    public void testRevokeVerificationRequest() {
        // Creating verifiable datas and new verification requests.
        List<VerifiableDataCreation> verifiableDataCreationsExpired = new ArrayList<VerifiableDataCreation>();
        for (int i = 0; i < CONSTANT; i++) {
            Date actualDate = new Date();
            long time = actualDate.getTime() + (4 * CONSTANT);
            Date tokenValidityEndDate = new Date(time);
            long verificationLength = CONSTANT / CONSTANT;
            VerifiableDataCreation createVerifiableData = verifyService.createVerifiableData(tokenValidityEndDate,
                    verificationLength,
                    getRandomVerificationLengthBase());
            Assert.assertNotNull(createVerifiableData);
            verifiableDataCreationsExpired.add(createVerifiableData);
        }

        List<VerifiableDataCreation> createVerifiableDataCreation = createVerifiableDataCreation();
        for (VerifiableDataCreation vdc : createVerifiableDataCreation) {
            Date actualDate = new Date();
            long time = actualDate.getTime() + (4 * CONSTANT);
            Date tokenValidityEndDate = new Date(time);
            long verificationLength = CONSTANT / CONSTANT;
            VerificationRequest createVerificationRequest = verifyService.createVerificationRequest(
                    vdc.getVerifiableDataId(),
                    tokenValidityEndDate,
                    verificationLength,
                    getRandomVerificationLengthBase());
            Assert.assertNotNull(createVerificationRequest);
            VerifiableDataCreation verifiableDataCreation = new VerifiableDataCreation(vdc.getVerifiableDataId(),
                    createVerificationRequest);
            verifiableDataCreationsExpired.add(verifiableDataCreation);
        }

        List<VerifiableDataCreation> verifiableDataCreations = createVerifiableDataCreation();
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());

        for (VerifiableDataCreation vdc : verifiableDataCreations) {
            // Testing the revokeVerificationRequest method in the noz expired verification data.
            verifyService.revokeVerificationRequest(vdc.getVerificationRequest().getVerificationRequestId());

            // Testing the again revoke verification request in the previous verifiable request id.
            try {
                verifyService.revokeVerificationRequest(vdc.getVerificationRequest().getVerificationRequestId());
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing the invalid verifcation request id.
            try {
                verifyService.revokeVerificationRequest(0L);
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing the negative invalid verification request id.
            try {
                verifyService.revokeVerificationRequest(-1L);
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }
        }

        for (VerifiableDataCreation vdc : verifiableDataCreationsExpired) {
            // Testing revokeVericationRequest method in the expired verifiable data.
            verifyService.revokeVerificationRequest(vdc.getVerificationRequest().getVerificationRequestId());

            // Testing the again revoke verification request in the previous verifiable request id.
            try {
                verifyService.revokeVerificationRequest(vdc.getVerificationRequest().getVerificationRequestId());
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing the invalid verifcation request id.
            try {
                verifyService.revokeVerificationRequest(0L);
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing the negative invalid verification request id.
            try {
                verifyService.revokeVerificationRequest(-1L);
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }
        }

    }

    @Override
    public void testVerifyData() {
        // Creating verifiable datas and new verification requests.
        List<VerifiableDataCreation> verifiableDataCreationsExpired = new ArrayList<VerifiableDataCreation>();
        for (int i = 0; i < CONSTANT; i++) {
            Date actualDate = new Date();
            long time = actualDate.getTime() + (4 * CONSTANT);
            Date tokenValidityEndDate = new Date(time);
            long verificationLength = CONSTANT / CONSTANT;
            VerifiableDataCreation createVerifiableData = verifyService.createVerifiableData(tokenValidityEndDate,
                    verificationLength,
                    getRandomVerificationLengthBase());
            Assert.assertNotNull(createVerifiableData);
            verifiableDataCreationsExpired.add(createVerifiableData);
        }

        List<VerifiableDataCreation> createVerifiableDataCreation = createVerifiableDataCreation();
        for (VerifiableDataCreation vdc : createVerifiableDataCreation) {
            Date actualDate = new Date();
            long time = actualDate.getTime() + (4 * CONSTANT);
            Date tokenValidityEndDate = new Date(time);
            long verificationLength = CONSTANT / CONSTANT;
            VerificationRequest createVerificationRequest = verifyService.createVerificationRequest(
                    vdc.getVerifiableDataId(),
                    tokenValidityEndDate,
                    verificationLength,
                    getRandomVerificationLengthBase());
            Assert.assertNotNull(createVerificationRequest);
            VerifiableDataCreation verifiableDataCreation = new VerifiableDataCreation(vdc.getVerifiableDataId(),
                    createVerificationRequest);
            verifiableDataCreationsExpired.add(verifiableDataCreation);
        }

        List<VerifiableDataCreation> verifiableDataCreations = createVerifiableDataCreation();
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());

        for (VerifiableDataCreation vdc : verifiableDataCreations) {
            // Testing the verifyData method in the not expired verifiable data.
            VerificationResult verifyData = verifyService.verifyData(vdc.getVerificationRequest().getVerifyTokenUUID());
            Assert.assertNotNull(verifyData);
            Assert.assertEquals(TokenUsageResult.VERIFIED, verifyData.getTokenUsageResult());

            // Testing null token.
            try {
                verifyService.verifyData(null);
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing invalid token.
            String testWrongToken = "test";
            VerificationResult result = verifyService.verifyData(testWrongToken);
            Assert.assertTrue(result == null);
        }

        for (VerifiableDataCreation vdc : verifiableDataCreationsExpired) {
            // Testing verifyData in the expired verification data.
            VerificationResult verifyData = verifyService.verifyData(vdc.getVerificationRequest().getVerifyTokenUUID());
            Assert.assertNotNull(verifyData);
            Assert.assertEquals(TokenUsageResult.EXPIRED, verifyData.getTokenUsageResult());
        }
    }
}
