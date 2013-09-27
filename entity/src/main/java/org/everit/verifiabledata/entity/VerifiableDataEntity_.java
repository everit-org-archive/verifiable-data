package org.everit.verifiabledata.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-09-27T08:04:34.522+0100")
@StaticMetamodel(VerifiableDataEntity.class)
public class VerifiableDataEntity_ {
	public static volatile SingularAttribute<VerifiableDataEntity, Long> verifiableDataId;
	public static volatile SingularAttribute<VerifiableDataEntity, Date> verifiedUntil;
	public static volatile ListAttribute<VerifiableDataEntity, VerificationRequestEntity> verificationRequestes;
}
