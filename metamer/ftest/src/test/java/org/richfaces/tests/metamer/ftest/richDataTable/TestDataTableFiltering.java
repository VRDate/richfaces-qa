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
package org.richfaces.tests.metamer.ftest.richDataTable;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableFilteringTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.richColumn.ColumnAttributes;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.FilteringDT;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestDataTableFiltering extends DataTableFilteringTest {

    @FindByJQuery("table.rf-dt[id$=richDataTable]")
    private FilteringDT table;

    @Override
    protected FilteringDT getTable() {
        return table;
    }

    @Override
    public String getComponentTestPagePath() {
        return "richDataTable/filtering.xhtml";
    }

    @Test
    @CoversAttributes(value = { "filterExpression", "filterType", "filterValue" }, attributeEnumClass = ColumnAttributes.class)
    public void testFilterCombinations() {
        super.testFilterCombinations(false);
    }

    @Test
    public void testFilterName() {
        super.testFilterName(false);
    }

    @Test
    public void testFilterNumberOfKidsWithSpinner() {
        super.testFilterNumberOfKidsWithSpinner();
    }

    @Test
    public void testFilterSex() {
        super.testFilterSex();
    }

    @Test
    public void testFilterTitle() {
        super.testFilterTitle(false);
    }

    @Test
    public void testFullPageRefresh() {
        super.testFullPageRefresh(false);
    }

    @Test
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-9932 http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-790")
    public void testRerenderAll() {
        super.testRerenderAll(false);
    }

}
