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
package org.richfaces.tests.metamer.ftest.richOrderingList;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Utils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Selenium tests for page faces/components/richOrderingList/jsApi.xhtml.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestOrderingListJSApi extends AbstractOrderingListTest {

    @ArquillianResource
    private Keyboard keyboard;

    @FindBy(id = "focus")
    private WebElement focus;
    @FindBy(css = "[id$=getList]")
    private WebElement getListLength;
    @FindBy(css = "[id$=up]")
    private WebElement upButton;
    @FindBy(css = "[id$=down]")
    private WebElement downButton;
    @FindBy(css = "[id$=upTop]")
    private WebElement upTopButton;
    @FindBy(css = "[id$=downBottom]")
    private WebElement downBottomButton;
    @FindBy(css = "input[id$=value]")
    private WebElement value;

    private Integer getActualSelectedItemIndex() {
        return Utils.getIndexOfElement(orderingList.advanced().getSelectedItemsElements().get(0));
    }

    @Override
    public String getComponentTestPagePath() {
        return "richOrderingList/jsApi.xhtml";
    }

    private String getValueFromOutputJSField() {
        return value.getAttribute("value");
    }

    @Test
    public void testFocus() {
        assertEquals(orderingList.advanced().getSelectedItemsElements().size(), 0);

        focus.click();

        keyboard.sendKeys(Keys.ARROW_DOWN);
        final List<WebElement> selectedItemsElements = orderingList.advanced().getSelectedItemsElements();
        assertEquals(selectedItemsElements.size(), 1);
        assertEquals(selectedItemsElements.get(0).getText(), "Montgomery");
    }

    @Test
    public void testInit() {
        assertVisible(orderingList.advanced().getRootElement(), "The ordering list should be visible.");
        assertButtonDisabled(orderingList.advanced().getBottomButtonElement(), "The button [last] should be disabled.");
        assertButtonDisabled(orderingList.advanced().getDownButtonElement(), "The button [down] should be disabled.");
        assertButtonDisabled(orderingList.advanced().getTopButtonElement(), "The button [first] should be disabled.");
        assertButtonDisabled(orderingList.advanced().getUpButtonElement(), "The button [up] should be disabled.");
    }

    @Test
    public void testListLength() {
        getListLength.click();
        Assert.assertEquals(Integer.valueOf(getValueFromOutputJSField()), Integer.valueOf(orderingList.advanced().getList().size()));
    }

    @Test
    public void testMoving() {
        orderingList.advanced().select(0);
        checkButtonsStateTop();

        downButton.click();
        checkButtonsStateMiddle();
        Assert.assertEquals(getActualSelectedItemIndex(), Integer.valueOf(1));

        downButton.click();
        checkButtonsStateMiddle();
        Assert.assertEquals(getActualSelectedItemIndex(), Integer.valueOf(2));

        upButton.click();
        checkButtonsStateMiddle();
        Assert.assertEquals(getActualSelectedItemIndex(), Integer.valueOf(1));

        upButton.click();
        checkButtonsStateTop();
        Assert.assertEquals(getActualSelectedItemIndex(), Integer.valueOf(0));

        downBottomButton.click();
        checkButtonsStateBottom();
        Assert.assertEquals(getActualSelectedItemIndex(), Integer.valueOf(orderingList.advanced().getList().size() - 1));

        upTopButton.click();
        checkButtonsStateTop();
        Assert.assertEquals(getActualSelectedItemIndex(), Integer.valueOf(0));
    }
}
