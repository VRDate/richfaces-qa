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

import org.jboss.arquillian.graphene.Graphene;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/a4jAjax/hSelectBooleanCheckbox.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 */
@Templates("plain")
public class TestHSelectBooleanCheckbox extends AbstractAjaxTest {

    private final Attributes<AjaxAttributes> ajaxAttributes = getAttributes();

    @Override
    public String getComponentTestPagePath() {
        return "a4jAjax/hSelectBooleanCheckbox.xhtml";
    }

    @Override
    public String getDefaultOutput() {
        return "false";
    }

    @Override
    public String getExpectedOutput() {
        return "true";
    }

    @Override
    public void performAction() {
        Graphene.guardAjax(page.getSelectBooleanCheckboxElement()).click();
    }

    @Override
    public void performAction(String input) {
        Graphene.guardAjax(page.getSelectBooleanCheckboxElement()).click();
    }

    @Override
    public void performSingleAjaxRequestAction() {
        performAction();
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
    public void testDisabled() {
        ajaxAttributes.set(AjaxAttributes.disabled, true);
        Graphene.guardNoRequest(page.getSelectBooleanCheckboxElement()).click();
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
    public void testImmediate() {
        super.testImmediate();
    }

    @Test
    public void testImmediateBypassUpdates() {
        super.testImmediateBypassUpdates();
    }

    @Test
    public void testLimitRender() {
        super.testLimitRender("true");
    }

    @Test
    @UseWithField(field = "listener", valuesFrom = ValuesFrom.FROM_ENUM, value = "")
    public void testListener() {
        super.testListener();
    }

    @Test
    public void testOnerror() {
        super.testOnerror();
    }

    @Test
    public void testRender() {
        super.testRender();
    }

    @Test
    public void testSimpleClick() {
        super.testClick();
    }

    @Test
    public void testStatus() {
        super.testStatus();
    }
}
