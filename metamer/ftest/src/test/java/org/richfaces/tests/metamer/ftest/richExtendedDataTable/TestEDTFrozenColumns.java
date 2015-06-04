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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestEDTFrozenColumns extends AbstractWebDriverTest {

    private final Attributes<ExtendedDataTableAttributes> extendedDataTableAttributes = getAttributes();

    @Page
    private FrozenColumnsPage page;

    private final Integer[] ints = { 0, 2, 4 };
    private final Integer[] ints2 = { 1, 3 };

    private Integer numberOfColumns;
    private Point location;// for testScrollerForNotFrozenColumns

    @Override
    public String getComponentTestPagePath() {
        return "richExtendedDataTable/frozenColumns.xhtml";
    }

    @Test
    @CoversAttributes("frozenColumns")
    public void testInit() {
        Boolean present = new WebElementConditionFactory(page.getFrozenColumnsTdElement()).isPresent().apply(driver);
        assertFalse(present, "No frozen columns should be in page.");
    }

    /**
     * Tests if frozen columns html elements show when frozen columns are enabled(set to >=1).Checks if they are still
     * there after switching to another data page.
     */
    @Test
    @CoversAttributes("frozenColumns")
    @Templates(exclude = { "richExtendedDataTable" })
    @UseWithField(field = "numberOfColumns", valuesFrom = FROM_FIELD, value = "ints")
    public void testFrozenColumnsShow() {
        extendedDataTableAttributes.set(ExtendedDataTableAttributes.frozenColumns, numberOfColumns);
        // test
        _testFrozenColumnsShow();

        // change page
        page.getNextPageElement().click();
        Graphene.waitModel().until().element(page.getSecondPageSpanElement()).is().visible();
        // test
        _testFrozenColumnsShow();

        // change page
        page.getNextPageElement().click();
        Graphene.waitModel().until().element(page.getThirdPageSpanElement()).is().visible();
        // test
        _testFrozenColumnsShow();
    }

    @Test
    @CoversAttributes("frozenColumns")
    @UseWithField(field = "numberOfColumns", valuesFrom = FROM_FIELD, value = "ints")
    @Templates(value = "richExtendedDataTable")
    @RegressionTest("https://issues.jboss.org/browse/RF-13046")
    public void testFrozenColumnsShowInEDT() {
        testFrozenColumnsShow();
    }

    public void _testFrozenColumnsShow() {
        // wait for list of elements with frozen columns with expected size
        List<WebElement> frozenColumns = guardListSize(page.getFrozenColumnsElements(), numberOfColumns);
        // frozenColumns feature is implemented in such a way that thare are no frozen column when frozenColumns equals
        // number of columns
        int expectedNumberOfColumns = numberOfColumns == 4 ? 0 : numberOfColumns;
        assertEquals(frozenColumns.size(), expectedNumberOfColumns,
            "The number of frozen columns set is not equal to the number of frozen columns found");
    }

    /**
     * Tests if scroller for not frozen columns is moved to another position and if the default one is removed. Checks
     * if scroller is still there after switching to another data page.
     */
    @Test
    @CoversAttributes("frozenColumns")
    @UseWithField(field = "numberOfColumns", valuesFrom = FROM_FIELD, value = "ints2")
    @Templates(exclude = { "richExtendedDataTable" })
    // TODO https://issues.jboss.org/browse/RF-12236 , when numberOfColumns=4
    public void testScrollerForNotFrozenColumns() {
        // check if default scroller is present and get its location
        Graphene.waitModel().until("Default scroller is not in the page.").element(page.getDefaultScrollerElement()).is().present();
        location = page.getDefaultScrollerElement().getLocation();
        extendedDataTableAttributes.set(ExtendedDataTableAttributes.frozenColumns, numberOfColumns);
        // test
        _testScrollerForNotFrozenColumns();

        // change page
        page.getNextPageElement().click();
        Graphene.waitModel().until().element(page.getSecondPageSpanElement()).is().visible();
        // test
        _testScrollerForNotFrozenColumns();

        // change page
        page.getNextPageElement().click();
        Graphene.waitModel().until().element(page.getThirdPageSpanElement()).is().visible();
        // test
        _testScrollerForNotFrozenColumns();
    }

    /**
     * Tests if scroller for not frozen columns is moved to another position and if the default one is removed. Checks
     * if scroller is still there after switching to another data page.
     */
    @Test
    @CoversAttributes("frozenColumns")
    @UseWithField(field = "numberOfColumns", valuesFrom = FROM_FIELD, value = "ints2")
    @Templates(value = "richExtendedDataTable")
    @RegressionTest("https://issues.jboss.org/browse/RF-12278")
    // TODO https://issues.jboss.org/browse/RF-12236 , when numberOfColumns=4
    public void testScrollerForNotFrozenColumnsInRichExtendedDataTable() {
        testScrollerForNotFrozenColumns();
    }

    private void _testScrollerForNotFrozenColumns() {
        // check if there is default scroller
        Graphene.waitModel().until("Default scroller should not be in the page.").element(page.getDefaultScrollerElement()).is()
            .not().present();
        // check if there is scroller for not frozen columns
        Graphene.waitModel().until("Scroller for the not frozen columns should be in the page.")
            .element(page.getMovedScrollerElement()).is().present();
        // check if the location of scroller moved
        assertNotEquals(location, page.getMovedScrollerElement().getLocation(), "The position of scroller has not been changed.");
    }

    public static class FrozenColumnsPage extends MetamerPage {

        @FindBy(xpath = "//div[contains(@id,'richEDT')]//td[@class='rf-edt-ftr-fzn']//div//table//tr//td")
        private List<WebElement> frozenColumnsElements;
        @FindBy(xpath = "//div[contains(@id,'richEDT')]//td[@class='rf-edt-ftr-fzn']")
        private WebElement frozenColumnsTdElement;
        @FindBy(xpath = "//div[contains(@id,'richEDT')]//div[@class='rf-edt-ftr']//table//tbody//tr//td[@colspan=1][1]//div[@class='rf-edt-scrl']")
        private WebElement defaultScrollerElement;
        @FindBy(xpath = "//div[contains(@id,'richEDT')]//div[@class='rf-edt-ftr']//table//tbody//tr//td[@colspan=1][2]//div[@class='rf-edt-scrl']")
        private WebElement movedScrollerElement;
        @FindBy(css = "div[id$=richEDT] a.rf-ds-btn-next")
        private WebElement nextPageElement;
        @FindBy(xpath = "//div[contains(@id,'richEDT')]//span[contains(@id, 'scroller2_ds_2')][contains(text(), '2')]")
        private WebElement secondPageSpanElement;
        @FindBy(xpath = "//div[contains(@id,'richEDT')]//span[contains(@id, 'scroller2_ds_3')][contains(text(), '3')]")
        private WebElement thirdPageSpanElement;

        /**
         * @return the defaultScrollerElement
         */
        public WebElement getDefaultScrollerElement() {
            return defaultScrollerElement;
        }

        /**
         * @return the frozenColumnsElements
         */
        public List<WebElement> getFrozenColumnsElements() {
            return frozenColumnsElements;
        }

        /**
         * @return the frozenColumnsTdElement
         */
        public WebElement getFrozenColumnsTdElement() {
            return frozenColumnsTdElement;
        }

        /**
         * @return the movedScrollerElement
         */
        public WebElement getMovedScrollerElement() {
            return movedScrollerElement;
        }

        /**
         * @return the nextPageElement
         */
        public WebElement getNextPageElement() {
            return nextPageElement;
        }

        /**
         * @return the secondPageSpanElement
         */
        public WebElement getSecondPageSpanElement() {
            return secondPageSpanElement;
        }

        /**
         * @return the thirdPageSpanElement
         */
        public WebElement getThirdPageSpanElement() {
            return thirdPageSpanElement;
        }
    }
}
