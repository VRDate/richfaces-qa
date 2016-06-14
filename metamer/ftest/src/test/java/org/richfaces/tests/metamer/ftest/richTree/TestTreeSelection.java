/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.tests.metamer.ftest.richTree;

import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;

import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.Uses;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 */
@IssueTracking("https://issues.jboss.org/browse/RF-11766")
public class TestTreeSelection extends AbstractTreeSelectionTest {

    @Test(groups = "smoke")
    @Uses({
        @UseWithField(field = "selectionType", valuesFrom = FROM_FIELD, value = "selectionTypeAjax"),
        @UseWithField(field = "sample", valuesFrom = ValuesFrom.FROM_FIELD, value = "ALL_NODES")
    })
    @CoversAttributes({ "selection", "selectionType" })
    public void testSubNodesSelectionAjax() {
        testSubNodesSelectionWithEvents();
    }

    @Test
    @Uses({
        @UseWithField(field = "selectionType", valuesFrom = FROM_FIELD, value = "selectionTypeClient"),
        @UseWithField(field = "sample", valuesFrom = ValuesFrom.FROM_FIELD, value = "ALL_NODES")
    })
    @CoversAttributes("selectionType")
    public void testSubNodesSelectionClient() {
        testSubNodesSelectionWithEvents();
    }
}
