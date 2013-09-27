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



public class VerifyTestImpl implements VerifyTest {

    // private VerifyService verifyService;

    // public void setVerifyService(final VerifyService verifyService) {
    // this.verifyService = verifyService;
    // }

    @Override
    public void testCreate() {
        // VerifiableDataCreation createVerifiableData = verifyService.createVerifiableData(new Date());
        // System.out.println("Create id: " + createVerifiableData.getVerifiableDataId());
    }

    @Override
    public void testSelect() {
        // // VerifiableDataCreation createVerifiableData = verifyService.createVerifiableData(new Date());
        // // String write = "Create id: " + createVerifiableData.getVerifiableDataId();
        // VerifiableDataCreation select = verifyService.select(1);
        // String write = "\t select id: " + select.getVerifiableDataId();
        // System.out.println(write);
    }
}
