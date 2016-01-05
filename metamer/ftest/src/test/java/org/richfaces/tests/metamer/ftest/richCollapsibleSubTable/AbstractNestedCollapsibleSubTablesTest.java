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
package org.richfaces.tests.metamer.ftest.richCollapsibleSubTable;

import static org.testng.Assert.assertEquals;

import java.text.MessageFormat;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.collapsibleSubTableToggler.RichFacesCollapsibleSubTableToggler;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;

import com.google.common.base.Predicate;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractNestedCollapsibleSubTablesTest extends AbstractWebDriverTest {

    protected static final boolean COLLAPSES = true;
    protected static final boolean EXPANDS = false;

    @FindByJQuery(value = ".data-1:visible")
    private List<WebElement> firstTableDatas;
    @FindBy(xpath = "//span[contains(@id,'level1Toggler')]//ancestor::td[1]")
    private List<RichFacesCollapsibleSubTableToggler> level1togglers;
    @FindBy(xpath = "//span[contains(@id,'level2Toggler')]//ancestor::td[1]")
    private List<RichFacesCollapsibleSubTableToggler> level2togglers;
    @FindBy(xpath = "//span[contains(@id,'level3Toggler')]//ancestor::td[1]")
    private List<RichFacesCollapsibleSubTableToggler> level3togglers;
    @FindByJQuery(value = ".data-2:visible")
    private List<WebElement> secondTableDatas;
    @FindByJQuery(value = ".data-3:visible")
    private List<WebElement> thirdTableDatas;

    public void assertFirstTableDataNotVisible() {
        assertTableData(firstTableDatas, 0);
    }

    public void assertFirstTableDataVisible() {
        assertTableData(firstTableDatas, 4);
    }

    public void assertSecondTableDataNotVisible() {
        assertTableData(secondTableDatas, 0);
    }

    public void assertSecondTableDataVisible() {
        assertTableData(secondTableDatas, 2);
    }

    public void assertTableData(List<WebElement> datas, int expectedSize) {
        assertEquals(datas.size(), expectedSize);
        int i = 0;
        for (WebElement data : datas) {
            assertEquals(data.getText(), MessageFormat.format("item {0}", ++i));
        }
    }

    public void assertThirdTableDataNotVisible() {
        assertTableData(thirdTableDatas, 0);
    }

    public void assertThirdTableDataVisible() {
        assertTableData(thirdTableDatas, 3);
    }

    public RichFacesCollapsibleSubTableToggler getToggler1a() {
        return level1togglers.get(0);
    }

    public RichFacesCollapsibleSubTableToggler getToggler1b() {
        return level1togglers.get(1);
    }

    public RichFacesCollapsibleSubTableToggler getToggler2a() {
        return level2togglers.get(0);
    }

    public RichFacesCollapsibleSubTableToggler getToggler2b1() {
        return level2togglers.get(1);
    }

    public RichFacesCollapsibleSubTableToggler getToggler2b2() {
        return level2togglers.get(2);
    }

    public RichFacesCollapsibleSubTableToggler getToggler3a() {
        return level3togglers.get(0);
    }

    public RichFacesCollapsibleSubTableToggler getToggler3b1() {
        return level3togglers.get(1);
    }

    public RichFacesCollapsibleSubTableToggler getToggler3b2() {
        return level3togglers.get(2);
    }

    public void toggle(RichFacesCollapsibleSubTableToggler toggler) {
        boolean wasExpanded = toggler.isExpanded();
        toggler.toggle();
        waitUntilTable(wasExpanded ? COLLAPSES : EXPANDS, toggler);
    }

    public void waitUntilTable(final boolean collapse, final RichFacesCollapsibleSubTableToggler toggler) {
        Graphene.waitModel().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver t) {
                return collapse ^ toggler.isExpanded();
            }
        });
    }

}
