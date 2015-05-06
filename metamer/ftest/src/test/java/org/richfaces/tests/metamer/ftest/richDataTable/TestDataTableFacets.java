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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableFacetsTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.SimpleDT;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestDataTableFacets extends DataTableFacetsTest {

    @FindBy(css = "table.rf-dt[id$=richDataTable]")
    private SimpleDT table;

    @FindBy(css = "table.rf-dt[id$=richDataTable]")
    private WebElement tableRoot;

    private final Attributes<DataTableAttributes> attributes = getAttributes();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataTable/facets.xhtml");
    }

    @Override
    protected SimpleDT getTable() {
        return table;
    }

    @Test
    public void testCapitalFooterFacet() {
        super.testCapitalFooterFacet();
    }

    @Test
    public void testCapitalHeaderFacet() {
        super.testCapitalHeaderFacet();
    }

    @Test
    @CoversAttributes("captionClass")
    @Templates("plain")
    public void testCaptionClass() {
        testStyleClass(tableRoot.findElement(By.tagName("caption")), BasicAttributes.captionClass);
    }

    @Test
    @CoversAttributes("footerClass")
    @Templates("plain")
    public void testFooterClass() {
        testStyleClass(table.getFooter().getTableFooterElement(), BasicAttributes.footerClass);

    }

    @Test
    @CoversAttributes("headerClass")
    @Templates("plain")
    public void testHeaderClass() {
        testStyleClass(table.getHeader().getTableHeaderElement(), BasicAttributes.headerClass);
    }

    @Test
    public void testHeaderFacet() {
        super.testHeaderFacet();
    }

    @Test
    public void testNoDataFacet() {
        super.testNoDataFacet();
    }

    @Test
    public void testStateFooterFacet() {
        super.testStateFooterFacet();
    }

    @Test
    public void testStateHeaderFacet() {
        super.testStateHeaderFacet();
    }
}
