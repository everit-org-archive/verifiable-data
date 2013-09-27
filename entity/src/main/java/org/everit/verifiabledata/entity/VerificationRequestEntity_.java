package org.everit.verifiabledata.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.everit.token.entity.TokenEntity;
import org.everit.verifiabledata.api.enums.VerificationLengthBase;

@Generated(value="Dali", date="2013-09-27T08:04:34.532+0100")
@StaticMetamodel(VerificationRequestEntity.class)
public class VerificationRequestEntity_ {
	public static volatile SingularAttribute<VerificationRequestEntity, Long> verificationRequestId;
	public static volatile SingularAttribute<VerificationRequestEntity, Long> verificationLength;
	public static volatile SingularAttribute<VerificationRequestEntity, TokenEntity> token;
	public static volatile SingularAttribute<VerificationRequestEntity, VerificationLengthBase> verificationLengthBase;
	public static volatile SingularAttribute<VerificationRequestEntity, VerifiableDataEntity> verifiableData;
}
