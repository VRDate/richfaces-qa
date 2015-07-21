/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richDataTable;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableFilteringTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.MultipleCoversAttributes;
import org.richfaces.tests.metamer.ftest.richColumn.ColumnAttributes;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.FilteringDT;
import org.testng.annotations.Test;

public class TestDataTableFilteringBuiltIn extends DataTableFilteringTest {

    @FindByJQuery("table.rf-dt[id$=richDataTable]")
    private FilteringDT table;

    @Override
    protected FilteringDT getTable() {
        return table;
    }

    @Override
    public String getComponentTestPagePath() {
        return "richDataTable/builtInFilteringAndSorting.xhtml";
    }

    @Test
    @MultipleCoversAttributes({
        @CoversAttributes("filterVar"),
        @CoversAttributes(value = { "filterExpression", "filterType", "filterValue" }, attributeEnumClass = ColumnAttributes.class)
    })
    public void testCombination() {
        super.testFilterCombinations(true);
    }

    @Test
    @CoversAttributes("filterVar")
    public void testFilterName() {
        super.testFilterName(true);
    }

    @Test
    @CoversAttributes("filterVar")
    public void testFilterTitle() {
        super.testFilterTitle(true);
    }

    @Test
    @CoversAttributes("filterVar")
    public void testNumberOfKids() {
        super.testFilterNumberOfKindsBuiltIn();
    }

}