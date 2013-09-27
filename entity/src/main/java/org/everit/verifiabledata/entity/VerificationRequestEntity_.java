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
