package org.everit.verifiabledata.core;

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

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.everit.token.api.TokenService;
import org.everit.token.api.dto.Token;
import org.everit.token.entity.TokenEntity;
import org.everit.verifiabledata.api.VerifyService;
import org.everit.verifiabledata.api.dto.VerifiableDataCreation;
import org.everit.verifiabledata.api.dto.VerificationRequest;
import org.everit.verifiabledata.api.dto.VerificationResult;
import org.everit.verifiabledata.api.enums.TokenUsageResult;
import org.everit.verifiabledata.api.enums.VerificationLengthBase;
import org.everit.verifiabledata.entity.VerifiableDataEntity;
import org.everit.verifiabledata.entity.VerificationRequestEntity;
import org.everit.verifiabledata.entity.VerificationRequestEntity_;

/**
 * Implementation of {@link VerifyService}.
 */
public class VerifyServiceImpl implements VerifyService {

    /**
     * EntityManager set by blueprint.
     */
    private EntityManager em;

    /**
     * The {@link TokenService} instance.
     */
    private TokenService tokenService;

    /**
     * The multiper in second to milliseconds.
     */
    private static final int MULTIPER = 1000;

    @Override
    public VerifiableDataCreation createVerifiableData(final Date tokenValidityEndDate, final long verificationLength,
            final VerificationLengthBase verificationLengthBase) {
        if ((tokenValidityEndDate == null) || (verificationLengthBase == null)) {
            throw new IllegalArgumentException("The one paramter is null. Cannot be null the paramters.");
        }
        if (verificationLength <= 0.0) {
            throw new IllegalArgumentException("Must be positive the verificationLength.");
        }

        String verifyTokenUUID = tokenService.createToken(tokenValidityEndDate);

        String rejectTokenUUID = tokenService.createToken(tokenValidityEndDate);

        Date validityEndDate = null;
        Date actualDate = new Date();
        if (verificationLengthBase.equals(VerificationLengthBase.REQUEST_CREATION)) {
            validityEndDate = new Date(actualDate.getTime() + (verificationLength * MULTIPER));
        } else {
            validityEndDate = tokenValidityEndDate;
        }

        VerifiableDataEntity verifiableDataEntity = new VerifiableDataEntity();
        verifiableDataEntity.setVerifiedUntil(validityEndDate);
        em.persist(verifiableDataEntity);

        VerificationRequestEntity verificationRequestEntity = new VerificationRequestEntity();
        verificationRequestEntity.setVerificationLength(verificationLength);
        verificationRequestEntity.setVerificationLengthBase(verificationLengthBase);
        verificationRequestEntity.setVerifiableData(verifiableDataEntity);
        verificationRequestEntity.setVerifyTokenUUID(em.getReference(TokenEntity.class, verifyTokenUUID));
        verificationRequestEntity.setRejectTokenUUID(em.getReference(TokenEntity.class, rejectTokenUUID));

        em.persist(verificationRequestEntity);
        em.flush();

        VerificationRequest verificationRequest = new VerificationRequest(
                verificationRequestEntity.getVerificationRequestId(), verifyTokenUUID, rejectTokenUUID);
        VerifiableDataCreation verifiableDataCreation = new VerifiableDataCreation(
                verifiableDataEntity.getVerifiableDataId(), verificationRequest);
        return verifiableDataCreation;
    }

    @Override
    public VerificationRequest createVerificationRequest(final long verifiableDataId, final Date tokenValidityEndDate,
            final long verificationLength, final VerificationLengthBase verificationLengthBase) {
        if ((tokenValidityEndDate == null) || (verificationLengthBase == null)) {
            throw new IllegalArgumentException("The one paramter is null. Cannot be null the paramters.");
        }

        if (verificationLength <= 0.0) {
            throw new IllegalArgumentException("Must be positive the verificationLength.");
        }

        VerificationRequest verificationRequest = null;
        VerifiableDataEntity verifiableDataEntity = findVerifiableDataEntityById(verifiableDataId);
        if (verifiableDataEntity != null) {
            em.lock(verifiableDataEntity, LockModeType.PESSIMISTIC_READ);
            em.lock(verifiableDataEntity, LockModeType.PESSIMISTIC_WRITE);

            List<VerificationRequestEntity> verificationReguestEntities =
                    findVerificationRequestEntitiesByVerifiableDataId(verifiableDataId);

            for (VerificationRequestEntity vre : verificationReguestEntities) {
                revokeVerificationRequest(vre.getVerificationRequestId());
                tokenService.revokeToken(vre.getVerifyTokenUUID().getTokenUuid());
                tokenService.revokeToken(vre.getRejectTokenUUID().getTokenUuid());
            }

            String verifyTokenUUID = tokenService.createToken(tokenValidityEndDate);
            String rejectTokenUUID = tokenService.createToken(tokenValidityEndDate);
            VerificationRequestEntity verificationRequestEntity = new VerificationRequestEntity();
            verificationRequestEntity.setVerificationLength(verificationLength);
            verificationRequestEntity.setVerificationLengthBase(verificationLengthBase);
            verificationRequestEntity.setVerifiableData(verifiableDataEntity);
            verificationRequestEntity.setVerifyTokenUUID(em.getReference(TokenEntity.class, verifyTokenUUID));
            verificationRequestEntity.setRejectTokenUUID(em.getReference(TokenEntity.class, rejectTokenUUID));
            em.persist(verificationRequestEntity);

            Date validityEndDate = null;
            Date actualDate = new Date();
            if (verificationLengthBase.equals(VerificationLengthBase.REQUEST_CREATION)) {
                validityEndDate = new Date(actualDate.getTime() + (verificationLength * MULTIPER));
            } else {
                validityEndDate = tokenValidityEndDate;
            }
            verifiableDataEntity.setVerifiedUntil(validityEndDate);
            em.merge(verifiableDataEntity);
            em.flush();
            verificationRequest = new VerificationRequest(verificationRequestEntity.getVerificationRequestId(),
                    verifyTokenUUID, rejectTokenUUID);

        } else {
            throw new IllegalArgumentException("The verifiable data is not exist.");
        }
        return verificationRequest;
    }

    /**
     * Determine the token usage result value.
     * 
     * @param token
     *            the {@link Token} object. Cannot be <code>null</code>.
     * @param verifyToken
     *            usign the verifyToken or not.
     * @return the correct {@link TokenUsageResult} value.
     */
    private TokenUsageResult determineTokenUsageResult(final Token token, final boolean verifyToken) {
        if ((token == null)) {
            throw new IllegalArgumentException("The one paramter is null. Cannot be null the paramters.");
        }
        TokenUsageResult result = null;
        Date actualDate = new Date();
        Date dateOfUse = token.getDateOfUse();
        Date expirationDate = token.getExpirationDate();
        Date revocationDate = token.getRevocationDate();
        if (actualDate.getTime() > expirationDate.getTime()) {
            result = TokenUsageResult.EXPIRED;
        } else if (dateOfUse != null) {
            if (verifyToken) {
                result = TokenUsageResult.VERIFIED;
            } else {
                result = TokenUsageResult.REJECTED;
            }
        } else {
            if (revocationDate != null) {
                result = TokenUsageResult.REJECTED;
            }
        }
        return result;
    }

    /**
     * Finds the verifiable data based on id.
     * 
     * @param id
     *            the id of the verifiable data.
     * @return the {@link VerifiableDataEntity} object if exist. Otherwise return <code>null</code>.
     */
    private VerifiableDataEntity findVerifiableDataEntityById(final long id) {
        return em.find(VerifiableDataEntity.class, id);
    }

    /**
     * Finds the verification request based on id.
     * 
     * @param id
     *            the id of the verification request.
     * @return the {@link VerificationRequestEntity} object if exist. Otherwise return <code>null</code>.
     */
    private VerificationRequestEntity findVerificationRequestEnityById(final long id) {
        return em.find(VerificationRequestEntity.class, id);
    }

    /**
     * Finds verifiable request entities based on verifiable data.
     * 
     * @param verifiableDataId
     *            the id of the verifiable data.
     * @return the verification request entities if no one return empty list.
     */
    private List<VerificationRequestEntity> findVerificationRequestEntitiesByVerifiableDataId(
            final long verifiableDataId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VerificationRequestEntity> criteriaQuery = cb.createQuery(VerificationRequestEntity.class);
        Root<VerificationRequestEntity> root = criteriaQuery
                .from(VerificationRequestEntity.class);
        Predicate predicate = cb.equal(root.get(VerificationRequestEntity_.verifiableData),
                em.getReference(VerifiableDataEntity.class, verifiableDataId));
        criteriaQuery.where(predicate);
        return em.createQuery(criteriaQuery).getResultList();
    }

    /**
     * Finds verifiable request entities based on reject token UUID.
     * 
     * @param tokenUuid
     *            the reject tokenUuid.
     * @return the verification request entities if no one return empty list.
     */
    private List<VerificationRequestEntity> findVerificationRequestEntityByRejectTokenUuid(final String tokenUuid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VerificationRequestEntity> criteriaQuery = cb.createQuery(VerificationRequestEntity.class);
        Root<VerificationRequestEntity> root = criteriaQuery
                .from(VerificationRequestEntity.class);
        Predicate predicate = cb.equal(root.get(VerificationRequestEntity_.rejectTokenUUID),
                em.getReference(TokenEntity.class, tokenUuid));
        criteriaQuery.where(predicate);
        return em.createQuery(criteriaQuery).getResultList();
    }

    /**
     * Finds verifiable request entities based on verify token UUID.
     * 
     * @param tokenUuid
     *            the verify tokenUuid.
     * @return the verification request entities if no one return empty list.
     */
    private List<VerificationRequestEntity> findVerificationRequestEntityByVerifyTokenUuid(final String tokenUuid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VerificationRequestEntity> criteriaQuery = cb.createQuery(VerificationRequestEntity.class);
        Root<VerificationRequestEntity> root = criteriaQuery
                .from(VerificationRequestEntity.class);
        Predicate predicate = cb.equal(root.get(VerificationRequestEntity_.verifyTokenUUID),
                em.getReference(TokenEntity.class, tokenUuid));
        criteriaQuery.where(predicate);
        return em.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Date getVerificationEndDate(final long verifiableDataId) {
        VerifiableDataEntity verifiableDataEntity = findVerifiableDataEntityById(verifiableDataId);
        Date result = null;
        if (verifiableDataEntity != null) {
            Date actualdate = new Date();
            if (actualdate.getTime() < verifiableDataEntity.getVerifiedUntil().getTime()) {
                result = verifiableDataEntity.getVerifiedUntil();
            }
        } else {
            throw new IllegalArgumentException("The verifiable data is not exist.");
        }
        return result;
    }

    @Override
    public void invalidateData(final long verifiableDataId) {
        List<VerificationRequestEntity> verificationRequests =
                findVerificationRequestEntitiesByVerifiableDataId(verifiableDataId);
        if (verificationRequests.isEmpty()) {
            throw new IllegalArgumentException("Not exist verifiable request in the verifiable data id.");
        } else {
            if (reduceVerificationEndDate(verifiableDataId, null)) {
                revokeVerificationRequest(verificationRequests.get(0).getVerificationRequestId());
            }
        }
    }

    @Override
    public boolean reduceVerificationEndDate(final long verifiableDataId, final Date verificationEndDate) {
        Date actualDate = new Date();
        Date usageVerificationEndDate = verificationEndDate;
        if ((verificationEndDate == null)) {
            usageVerificationEndDate = new Date();
        } else if (actualDate.getTime() > verificationEndDate.getTime()) {
            throw new IllegalArgumentException("The verification end date is smaller than actual date.");
        }

        boolean reduce = false;
        VerifiableDataEntity verifiableDataEntity = findVerifiableDataEntityById(verifiableDataId);
        if (verifiableDataEntity != null) {
            if (usageVerificationEndDate.getTime() < verifiableDataEntity.getVerifiedUntil().getTime()) {
                verifiableDataEntity.setVerifiedUntil(usageVerificationEndDate);
                em.merge(verifiableDataEntity);
                List<VerificationRequestEntity> verificationRequestEntities =
                        findVerificationRequestEntitiesByVerifiableDataId(verifiableDataId);
                for (VerificationRequestEntity vre : verificationRequestEntities) {
                    long verificationLength = (int) ((usageVerificationEndDate.getTime()
                            - tokenService.getToken(vre.getVerifyTokenUUID().getTokenUuid()).getCreationDate()
                            .getTime()) / MULTIPER);
                    vre.setVerificationLength(verificationLength);
                    em.merge(vre);
                }
                em.flush();

                reduce = true;
            }
        } else {
            throw new IllegalArgumentException("The verifiable data is not exist.");
        }
        return reduce;
    }

    /**
     * Revoke the verification request.
     * 
     * @param verificationRequestId
     *            the id of the verification request. Must be exist verifiable request.
     * 
     * @throws IllegalArgumentException
     *             if the parameter is <code>null</code> or the verifiable data is not exits or the verifiable request
     *             is not exist.
     */
    private void revokeVerificationRequest(final long verificationRequestId) {
        VerificationRequestEntity verificationRequestEntity = findVerificationRequestEnityById(verificationRequestId);
        if (verificationRequestEntity == null) {
            throw new IllegalArgumentException("The verifiable request is not exist.");
        }
        em.remove(verificationRequestEntity);
        em.flush();
    }

    public void setEm(final EntityManager em) {
        this.em = em;
    }

    public void setTokenService(final TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public VerificationResult verifyData(final String token) {
        if ((token == null)) {
            throw new IllegalArgumentException("The token paramter is null. Cannot be null the paramters.");
        }
        VerificationResult result = null;
        List<VerificationRequestEntity> verifyVerificationRequestEntities =
                findVerificationRequestEntityByVerifyTokenUuid(token);
        List<VerificationRequestEntity> rejectVerificationRequestEntities =
                findVerificationRequestEntityByRejectTokenUuid(token);
        if (verifyVerificationRequestEntities.size() == 1) {
            VerificationRequestEntity verificationRequestEntity = verifyVerificationRequestEntities.get(0);
            boolean verifyToken = tokenService.verifyToken(token);
            Token getToken = tokenService.getToken(token);
            if (verifyToken) {
                tokenService.revokeToken(verificationRequestEntity.getRejectTokenUUID().getTokenUuid());
                VerificationLengthBase verificationLengthBase = verificationRequestEntity.getVerificationLengthBase();
                VerifiableDataEntity verifiableData = verificationRequestEntity.getVerifiableData();
                if (verificationLengthBase.equals(VerificationLengthBase.VERIFICATION)) {
                    long validityEndDate = getToken.getDateOfUse().getTime()
                            + (verificationRequestEntity.getVerificationLength() * MULTIPER);
                    Date verifiedUntil = new Date(validityEndDate);
                    verifiableData.setVerifiedUntil(verifiedUntil);
                    em.merge(verifiableData);
                    em.flush();
                }
                // result = new VerificationResult(verifiableData.getVerifiableDataId(),
                // determineTokenUsageResult(getToken, true));
            }
            result = new VerificationResult(verificationRequestEntity.getVerifiableData().getVerifiableDataId(),
                    determineTokenUsageResult(getToken, true));
        } else if (rejectVerificationRequestEntities.size() == 1) {
            VerificationRequestEntity verificationRequestEntity = rejectVerificationRequestEntities.get(0);
            boolean verifyToken = tokenService.verifyToken(token);
            Token getToken = tokenService.getToken(token);
            if (verifyToken) {
                tokenService.revokeToken(verificationRequestEntity.getVerifyTokenUUID().getTokenUuid());
                reduceVerificationEndDate(verificationRequestEntity.getVerifiableData().getVerifiableDataId(), null);
            }
            result = new VerificationResult(verificationRequestEntity.getVerifiableData().getVerifiableDataId(),
                    determineTokenUsageResult(getToken, false));
        }
        return result;
    }
}
