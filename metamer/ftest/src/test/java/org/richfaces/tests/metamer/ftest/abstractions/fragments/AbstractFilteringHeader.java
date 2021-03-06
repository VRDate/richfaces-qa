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
package org.richfaces.tests.metamer.ftest.abstractions.fragments;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.ClearType;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.model.Employee;

import com.google.common.base.Strings;

public abstract class AbstractFilteringHeader implements FilteringHeaderInterface {

    @FindBy(css = "[id$='columnHeaderSexInput']")
    private SelectComponent sexSelect;// the Selenium Select component does not work within xvfb in here

    @ArquillianResource
    private WebDriver driver;

    private void fillInAndBlur(TextInputComponentImpl input, String text) {
        if (Strings.isNullOrEmpty(text)) {
            input.advanced().clear(ClearType.WD);
        } else {
            input.clear().sendKeys(text);
        }
        makeBlur();
    }

    @Override
    public void filterName(String name, boolean isBuiltIn) {
        if (isBuiltIn) {
            filterNameBuiltIn(name);
        } else {
            filterName(name);
        }
    }

    @Override
    public void filterName(String name) {
        fillInAndBlur(getNameInput(), name);
    }

    @Override
    public void filterNameBuiltIn(String name) {
        fillInAndBlur(getNameBuiltInInput(), name);
    }

    @Override
    public void filterNumberOfKidsBuiltIn(int numberOfKids) {
        fillInAndBlur(getNumberOfKidsInput(), String.valueOf(numberOfKids));
    }

    @Override
    public void filterNumberOfKidsWithSpinner(int numberOfKids) {
        getNumberOfKidsSpinner().setValue(numberOfKids);
        makeBlur();
    }

    @Override
    public void filterSex(Employee.Sex sex) {
        String option = sex == null ? "ALL" : sex.toString();
        guardAjax(getSexSelect()).selectByValue(option);
    }

    @Override
    public void filterTitle(String title, boolean isBuiltIn) {
        if (isBuiltIn) {
            filterTitleBuiltIn(title);
        } else {
            filterTitle(title);
        }
    }

    @Override
    public void filterTitle(String title) {
        fillInAndBlur(getTitleInput(), title);
    }

    @Override
    public void filterTitleBuiltIn(String title) {
        fillInAndBlur(getTitleBuildInInput(), title);
    }

    @Override
    public SelectComponent getSexSelect() {
        return sexSelect;
    }

    public void makeBlur() {
        try {
            Graphene.guardAjax(driver.findElement(By.id("blurButtonFooter"))).click();
        } catch (Exception ignored) {
            // if no ajax then it has been already done
        }
    }

    public static class SelectComponent {

        @Root
        private WebElement rootElement;

        public void selectByValue(String text) {
            Utils.jQ("val('" + text + "').change();", rootElement);
        }
    }
}
