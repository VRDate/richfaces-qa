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
package org.richfaces.tests.metamer.ftest.a4jAjax;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/a4jAjax/hCommandButton.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 */
@RegressionTest("https://issues.jboss.org/browse/RF-10482")
public class TestHCommandButton extends AbstractAjaxTest {

    private final Attributes<AjaxAttributes> ajaxAttributes = getAttributes();

    @Override
    public String getDefaultOutput() {
        return "";
    }

    @Override
    public String getExpectedOutput() {
        return "RichFaces 4";
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jAjax/hCommandButton.xhtml");
    }

    @Override
    public void performAction() {
        performAction("RichFaces 4");
    }

    public void performAction(String input) {
        page.getInputElement().clear();
        page.getInputElement().sendKeys(input);
        Graphene.guardAjax(page.getButtonElement()).click();
    }

    @Test
    public void testBypassUpdates() {
        super.testBypassUpdates();
    }

    @Test
    public void testData() {
        super.testData();
    }

    @Test
    @CoversAttributes("disabled")
    public void testDisabled() {
        ajaxAttributes.set(AjaxAttributes.disabled, true);

        page.getInputElement().sendKeys("RichFaces 4");
        Graphene.guardHttp(page.getButtonElement()).click();

        assertOutput1Changed();
        assertOutput2Changed();
    }

    @Test
    public void testEvents() {
        super.testEvents();
    }

    @Test
    public void testExecute() {
        super.testExecute();
    }

    @Test
    public void testFullPageRefresh() {
        super.testFullPageRefresh();
    }

    @Test
    public void testImmediate() {
        super.testImmediate();
    }

    @Test
    public void testImmediateBypassUpdates() {
        super.testImmediateBypassUpdates();
    }

    @Test
    public void testLimitRender() {
        super.testLimitRender("RichFaces 4");
    }

    @Test
    @UseWithField(field = "listener", valuesFrom = ValuesFrom.FROM_ENUM, value = "")
    public void testListener() {
        testListener(getActionMapForListeners());
    }

    @Test
    public void testRender() {
        super.testRender();
    }

    @Test
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-11341")
    public void testRerenderAll() {
        super.testRerenderAll();
    }

    @Test
    public void testSimpleClick() {
        page.getInputElement().sendKeys("RichFaces 4");
        MetamerPage.waitRequest(page.getButtonElement(), WaitRequestType.XHR).click();

        assertOutput1Changed();
        assertOutput2Changed();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9665")
    public void testSimpleClickUnicode() {
        page.getInputElement().sendKeys("ľščťžýáíéúôň фывацукйешгщь");
        MetamerPage.waitRequest(page.getButtonElement(), WaitRequestType.XHR).click();

        assertEquals(page.getOutput1Element().getText(), "ľščťžýáíéúôň фывацукйешгщь", "Output2 should change");
        assertEquals(page.getOutput2Element().getText(), "ľščťžýáíéúôň фывацукйешгщь", "Output2 should change");
    }

    @Test
    public void testStatus() {
        super.testStatus();
    }
}
