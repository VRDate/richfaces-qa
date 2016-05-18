/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010-2016, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.tests.metamer.ftest.richAccordionItem;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * Test rich:accordion keeping visual state (KVS) on page faces/components/richAccordion/simple.xhtml
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class TestAccordionItemKVS extends AbstractWebDriverTest {

    private final AccordionReloadTester accordionReloadTester = new AccordionReloadTester();

    @Override
    public String getComponentTestPagePath() {
        return "richAccordionItem/simple.xhtml";
    }

    @Test(groups = { "keepVisualStateTesting" })
    @RegressionTest(value = { "https://issues.jboss.org/browse/RF-12131", "https://issues.jboss.org/browse/RF-13727" })
    public void testRefreshFullPage() {
        accordionReloadTester.testFullPageRefresh();
    }

    @Test(groups = { "keepVisualStateTesting" })
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-12035")
    public void testRenderAll() {
        accordionReloadTester.testRerenderAll();
    }

    private class AccordionReloadTester extends ReloadTester<String> {

        /**
         * Used to create a variable JQuery expression based in accordion item ID number and find the element. The pattern
         * is:"div[id$='item" + headerItemNumber + ":header']"
         *
         * @param headerItemNumber number of item
         * @return WebElement with given number
         */
        private WebElement getHeaderElem(String headerItemNumber) {
            String expressionToUse = "div[id$='item" + headerItemNumber + ":header']";
            return driver.findElement(ByJQuery.selector(expressionToUse));
        }

        /**
         * Used to create a variable JQuery expression based in accordion item ID number and find the element. The pattern
         * is:"div[id$='item" + contentItemNumber + ":content']"
         *
         * @param contentItemNumber number of item
         * @return WebElement with given number
         */
        private WebElement getContentElem(String contentItemNumber) {
            String expressionToUse = "div[id$='item" + contentItemNumber + ":content']";
            return driver.findElement(ByJQuery.selector(expressionToUse));
        }

        @Override
        public void doRequest(String accordionIndex) {
            guardAjax(getHeaderElem(accordionIndex)).click();
        }

        @Override
        public void verifyResponse(String accordionIndex) {
            waitGui(driver).until().element(getContentElem(accordionIndex)).is().visible();
        }

        @Override
        public String[] getInputValues() {
            return new String[] { "1", "2", "3" };
        }
    }
}
