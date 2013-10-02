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

import org.everit.token.api.exception.NoSuchTokenException;
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
     * The time multiplier.
     */
    private static final int TIME_MULTIPLIER = 4;

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

    private List<VerifiableDataCreation> createVerifiableDataCreationsExpired() {
        List<VerifiableDataCreation> verifiableDataCreationsExpired = new ArrayList<VerifiableDataCreation>();
        // Create expired verifiable datas.
        for (int i = 0; i < CONSTANT; i++) {
            Date actualDate = new Date();
            long time = actualDate.getTime() + (TIME_MULTIPLIER * CONSTANT);
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
            long time = actualDate.getTime() + (TIME_MULTIPLIER * CONSTANT);
            Date tokenValidityEndDate = new Date(time);
            long verificationLength = CONSTANT / CONSTANT;
            VerificationRequest createVerificationRequest = verifyService.createVerificationRequest(
                    vdc.getVerifiableDataId(),
                    tokenValidityEndDate,
                    verificationLength,
                    getRandomVerificationLengthBase());
            Assert.assertNotNull(createVerificationRequest);
            Assert.assertTrue(createVerificationRequest.getVerificationRequestId() > 0L);
            VerifiableDataCreation verifiableDataCreation = new VerifiableDataCreation(vdc.getVerifiableDataId(),
                    createVerificationRequest);
            verifiableDataCreationsExpired.add(verifiableDataCreation);
        }
        return verifiableDataCreationsExpired;
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
    public void testComplex() {
        List<VerifiableDataCreation> createVerifiableDataCreationsExpired = createVerifiableDataCreationsExpired();
        List<VerifiableDataCreation> createVerifiableDataCreation = createVerifiableDataCreation();
        createVerifiableDataCreation.addAll(createVerifiableDataCreation());
        createVerifiableDataCreation.addAll(createVerifiableDataCreation());
        createVerifiableDataCreation.addAll(createVerifiableDataCreation());
        createVerifiableDataCreation.addAll(createVerifiableDataCreation());

        for (VerifiableDataCreation vdc : createVerifiableDataCreation) {
            Date verificationEndDate = verifyService.getVerificationEndDate(vdc.getVerifiableDataId());
            Assert.assertNull(verificationEndDate);

            Date actualData = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(actualData);
            c.add(Calendar.DATE, 1);
            Date endDate = c.getTime();
            boolean reduceVerificationEndDate = verifyService.reduceVerificationEndDate(vdc.getVerifiableDataId(),
                    endDate);
            Assert.assertFalse(reduceVerificationEndDate);

            verificationEndDate = verifyService.getVerificationEndDate(vdc.getVerifiableDataId());
            Assert.assertNull(verificationEndDate);

            VerificationResult verifyData = verifyService.verifyData(vdc.getVerificationRequest().getVerifyTokenUUID());
            Assert.assertNotNull(verifyData);
            Assert.assertEquals(TokenUsageResult.VERIFIED, verifyData.getTokenUsageResult());

            verificationEndDate = verifyService.getVerificationEndDate(vdc.getVerifiableDataId());
            Assert.assertNotNull(verificationEndDate);

            reduceVerificationEndDate = verifyService.reduceVerificationEndDate(vdc.getVerifiableDataId(),
                    endDate);
            Assert.assertTrue(reduceVerificationEndDate);

            verificationEndDate = verifyService.getVerificationEndDate(vdc.getVerifiableDataId());
            Assert.assertNotNull(verificationEndDate);

            reduceVerificationEndDate = verifyService.reduceVerificationEndDate(vdc.getVerifiableDataId(),
                    endDate);
            Assert.assertFalse(reduceVerificationEndDate);

            verificationEndDate = verifyService.getVerificationEndDate(vdc.getVerifiableDataId());
            Assert.assertNotNull(verificationEndDate);
        }

        for (VerifiableDataCreation vdc : createVerifiableDataCreationsExpired) {
            Date verificationEndDate = verifyService.getVerificationEndDate(vdc.getVerifiableDataId());
            Assert.assertNull(verificationEndDate);

            Date actualData = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(actualData);
            c.add(Calendar.DATE, 1);
            Date endDate = c.getTime();
            boolean reduceVerificationEndDate = verifyService.reduceVerificationEndDate(vdc.getVerifiableDataId(),
                    endDate);
            Assert.assertFalse(reduceVerificationEndDate);

            VerificationResult verifyData = verifyService.verifyData(vdc.getVerificationRequest().getVerifyTokenUUID());
            Assert.assertNotNull(verifyData);
            Assert.assertEquals(TokenUsageResult.EXPIRED, verifyData.getTokenUsageResult());
            Assert.assertEquals(new Long(vdc.getVerifiableDataId()), verifyData.getVerifiableDataId());

            verificationEndDate = verifyService.getVerificationEndDate(vdc.getVerifiableDataId());
            Assert.assertNull(verificationEndDate);

            reduceVerificationEndDate = verifyService.reduceVerificationEndDate(vdc.getVerifiableDataId(),
                    endDate);
            Assert.assertFalse(reduceVerificationEndDate);
        }
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
        List<VerifiableDataCreation> verifiableDataCreationsExpired = createVerifiableDataCreationsExpired();
        // Creating not expired verifiable datas.
        List<VerifiableDataCreation> verifiableDataCreations = createVerifiableDataCreation();
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());

        for (VerifiableDataCreation vdc : verifiableDataCreations) {
            // Testing the getVerification end date in the not expired verifiable data.
            Date verificationEndDate = verifyService.getVerificationEndDate(vdc.getVerifiableDataId());
            // Date actualDate = new Date();
            Assert.assertNull(verificationEndDate);
            // Assert.assertNotNull(verificationEndDate);
            // Assert.assertTrue(actualDate.getTime() < verificationEndDate.getTime());

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
    public void testInvalidateData() {
        // Creating verifiable datas and new verification requests.
        List<VerifiableDataCreation> verifiableDataCreationsExpired = createVerifiableDataCreationsExpired();

        List<VerifiableDataCreation> createVerifiableDataCreation = createVerifiableDataCreation();
        createVerifiableDataCreation.addAll(createVerifiableDataCreation());
        createVerifiableDataCreation.addAll(createVerifiableDataCreation());
        createVerifiableDataCreation.addAll(createVerifiableDataCreation());
        createVerifiableDataCreation.addAll(createVerifiableDataCreation());

        for (VerifiableDataCreation vdc : createVerifiableDataCreation) {
            verifyService.invalidateData(vdc.getVerifiableDataId());

            try {
                verifyService.invalidateData(vdc.getVerifiableDataId());
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }
        }

        for (VerifiableDataCreation vdc : verifiableDataCreationsExpired) {
            verifyService.invalidateData(vdc.getVerifiableDataId());
        }

    }

    @Override
    public void testReduceVerificationEndDate() {
        List<VerifiableDataCreation> verifiableDataCreationsExpired = createVerifiableDataCreationsExpired();

        // Create verifable datas.
        List<VerifiableDataCreation> verifiableDataCreations = createVerifiableDataCreation();
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
            Assert.assertFalse(reduceVerificationEndDate);
            // Assert.assertTrue(reduceVerificationEndDate);

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
    public void testRejectedRequest() {
        List<VerifiableDataCreation> verifiableDataCreationsExpired = createVerifiableDataCreationsExpired();

        List<VerifiableDataCreation> createVerifiableDataCreation = createVerifiableDataCreation();
        createVerifiableDataCreation.addAll(createVerifiableDataCreation());
        createVerifiableDataCreation.addAll(createVerifiableDataCreation());
        createVerifiableDataCreation.addAll(createVerifiableDataCreation());
        createVerifiableDataCreation.addAll(createVerifiableDataCreation());
        createVerifiableDataCreation.addAll(createVerifiableDataCreation());

        for (VerifiableDataCreation vdc : createVerifiableDataCreation) {
            VerificationResult verifyData = verifyService.verifyData(vdc.getVerificationRequest().getRejectTokenUUID());
            Assert.assertNotNull(verifyData);
            Assert.assertEquals(TokenUsageResult.REJECTED, verifyData.getTokenUsageResult());

            verifyData = verifyService.verifyData(createVerifiableDataCreation.get(0)
                    .getVerificationRequest().getVerifyTokenUUID());
            Assert.assertNotNull(verifyData);
            Assert.assertEquals(TokenUsageResult.REJECTED, verifyData.getTokenUsageResult());

            verifyData = verifyService.verifyData(createVerifiableDataCreation.get(0)
                    .getVerificationRequest().getRejectTokenUUID());
            Assert.assertNotNull(verifyData);
            Assert.assertEquals(TokenUsageResult.REJECTED, verifyData.getTokenUsageResult());
        }

        for (VerifiableDataCreation vdc : verifiableDataCreationsExpired) {
            // Testing verifyData in the expired verification data.
            VerificationResult verifyData = verifyService.verifyData(vdc.getVerificationRequest()
                    .getVerifyTokenUUID());
            Assert.assertNotNull(verifyData);
            Assert.assertEquals(TokenUsageResult.EXPIRED, verifyData.getTokenUsageResult());

            verifyData = verifyService.verifyData(vdc.getVerificationRequest()
                    .getVerifyTokenUUID());
            Assert.assertNotNull(verifyData);
            Assert.assertEquals(TokenUsageResult.EXPIRED, verifyData.getTokenUsageResult());
        }

    }

    @Override
    public void testVerifyData() {
        // Creating verifiable datas and new verification requests.
        List<VerifiableDataCreation> verifiableDataCreationsExpired = createVerifiableDataCreationsExpired();

        List<VerifiableDataCreation> verifiableDataCreations = createVerifiableDataCreation();
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());
        verifiableDataCreations.addAll(createVerifiableDataCreation());

        for (VerifiableDataCreation vdc : verifiableDataCreations) {
            // Testing the verifyData method in the not expired verifiable data.

            VerificationResult verifyData = verifyService.verifyData(vdc.getVerificationRequest()
                    .getVerifyTokenUUID());
            Assert.assertNotNull(verifyData);
            Assert.assertEquals(TokenUsageResult.VERIFIED, verifyData.getTokenUsageResult());

            verifyData = verifyService.verifyData(vdc.getVerificationRequest()
                    .getVerifyTokenUUID());
            Assert.assertNotNull(verifyData);
            Assert.assertEquals(TokenUsageResult.VERIFIED, verifyData.getTokenUsageResult());

            verifyData = verifyService.verifyData(vdc.getVerificationRequest()
                    .getRejectTokenUUID());
            Assert.assertNotNull(verifyData);
            Assert.assertEquals(TokenUsageResult.REJECTED, verifyData.getTokenUsageResult());

            // Testing null token.
            try {
                verifyService.verifyData(null);
            } catch (IllegalArgumentException e) {
                Assert.assertNotNull(e);
            }

            // Testing invalid token.
            String testWrongToken = "test";
            try {
                verifyService.verifyData(testWrongToken);
            } catch (NoSuchTokenException e) {
                Assert.assertNotNull(e);
            }
        }

        for (VerifiableDataCreation vdc : verifiableDataCreationsExpired) {
            // Testing verifyData in the expired verification data.
            VerificationResult verifyData = verifyService.verifyData(vdc.getVerificationRequest()
                    .getVerifyTokenUUID());
            Assert.assertNotNull(verifyData);
            Assert.assertEquals(TokenUsageResult.EXPIRED, verifyData.getTokenUsageResult());
        }
    }
}
