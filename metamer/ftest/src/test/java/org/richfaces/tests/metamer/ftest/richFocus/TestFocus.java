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
package org.richfaces.tests.metamer.ftest.richFocus;

import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.test.selenium.support.ui.ElementIsFocused;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestFocus extends AbstractWebDriverTest {

    private final Attributes<FocusAttributes> focusAttributes = getAttributes();

    @Page
    private FocusSimplePage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richFocus/simple.xhtml");
    }

    @Test
    @CoversAttributes("ajaxRendered")
    public void testAjaxRenderedFalse() {
        focusAttributes.set(FocusAttributes.ajaxRendered, false);

        page.ajaxValidateInputs();

        page.typeStringAndDoNotCareAboutFocus();

        String actual = page.getNameInput().getStringValue();
        assertEquals(actual.trim(), "", "The input should be empty! Because no inputs should have focus!");
    }

    @Test
    public void testFocusOnFirstInputAfterLoad() {
        page.typeStringAndDoNotCareAboutFocus();

        String actual = page.getNameInput().getStringValue();
        assertEquals(actual, AbstractFocusPage.EXPECTED_STRING,
            "The first input (with label name) was not focused after page load!");
    }

    @Test
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richExtendedDataTable", "uiRepeat" })
    @CoversAttributes("preserve")
    public void testPreserveIgnoresValidationAware() {
        focusAttributes.set(FocusAttributes.validationAware, false);
        testPreserveTrue();
        focusAttributes.set(FocusAttributes.validationAware, true);
        testPreserveTrue();
    }

    @Test
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-14046")
    @Templates({ "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richExtendedDataTable", "uiRepeat" })
    @CoversAttributes("preserve")
    public void testPreserveIgnoresValidationAwareInIterationComponents() {
        testPreserveIgnoresValidationAware();
    }

    private void testPreserveTrue() {
        focusAttributes.set(FocusAttributes.preserve, true);
        // focus the age input
        page.getAgeInput().advanced().getInputElement().click();
        // validate empty fields
        page.ajaxValidateInputs();
        // wait for the age input is focused again
        waitModel().until(new ElementIsFocused(page.getAgeInput().advanced().getInputElement()));
        // type a string
        page.typeStringAndDoNotCareAboutFocus();
        // the age input text is changed to the typed text
        String actual = page.getAgeInput().getStringValue();
        assertTrue(actual.contains(AbstractFocusPage.EXPECTED_STRING),
            "The age input should be focused, since the preserve is true and before form submission that input was focused!");
    }

    @Test
    @CoversAttributes("validationAware")
    @Templates(exclude = { "richAccordion", "richCollapsiblePanel", "richTabPanel" })
    public void testValidationAwareFalse() {
        // richPopupPanel is disabled because in place where following attribute is to be set the popup
        // window appears, therefore making it unclickable
        focusAttributes.set(FocusAttributes.validationAware, false);

        page.getNameInput().sendKeys("Robert");
        page.getAgeInput().sendKeys("38");

        page.ajaxValidateInputs();
        waitModel().until(new ElementIsFocused(page.getNameInput().advanced().getInputElement()));

        page.typeStringAndDoNotCareAboutFocus();

        String actual = page.getNameInput().getStringValue();
        assertTrue(actual.contains(AbstractFocusPage.EXPECTED_STRING), "The name input should contain string "
            + AbstractFocusPage.EXPECTED_STRING + ", because validationAware is false!");
    }

    @Test
    @CoversAttributes("validationAware")
    @Templates(value = { "richAccordion", "richCollapsiblePanel", "richTabPanel" })
    @RegressionTest("https://issues.jboss.org/browse/RF-13263")
    public void testValidationAwareFalseInSwitchablePanels() {
        testValidationAwareFalse();
    }

    @Test
    @CoversAttributes("validationAware")
    public void testValidationAwareTrue() {
        page.getNameInput().sendKeys("Robert");
        page.getAgeInput().sendKeys("38");

        page.ajaxValidateInputs();
        waitModel().until(new ElementIsFocused(page.getAddressInput().advanced().getInputElement()));

        page.typeStringAndDoNotCareAboutFocus();

        String actual = page.getAddressInput().getStringValue();
        assertEquals(actual, AbstractFocusPage.EXPECTED_STRING,
            "The address input should be focused! Since validationAware is true and that input is incorrect!");
    }
}
