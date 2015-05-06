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
package org.richfaces.tests.metamer.ftest.abstractions.message;

import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.common.ClearType;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.richMessage.MessageAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.Assert;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractMessageComponentCommonTest extends AbstractWebDriverTest {

    private final Attributes<BasicAttributes> basicAttributes = getAttributes();
    private final Attributes<MessageAttributes> messageAttributes = getAttributes();

    public void checkDir() {
        testHTMLAttribute(getTestedElementRoot(), basicAttributes, BasicAttributes.dir, "null", getGenerateMessagesAction());
        testHTMLAttribute(getTestedElementRoot(), basicAttributes, BasicAttributes.dir, "ltr", getGenerateMessagesAction());
        testHTMLAttribute(getTestedElementRoot(), basicAttributes, BasicAttributes.dir, "rtl", getGenerateMessagesAction());
    }

    public void checkEscape() {
        messageAttributes.set(MessageAttributes.escape, Boolean.TRUE);
        String newSpanString = "<span id='newSpan'>newSpan</span>";
        getPage().getSimpleInput1().advanced().clear(ClearType.JS).sendKeys(newSpanString);
        submitWithHBtn();
        assertNotVisible(getPage().getNewSpan(), "new span should not be visible");
        messageAttributes.set(MessageAttributes.escape, Boolean.FALSE);
        getPage().getSimpleInput1().advanced().clear(ClearType.JS).sendKeys(newSpanString);
        submitWithHBtn();
        assertVisible(getPage().getNewSpan(), "new span should be visible");
    }

    public void checkLang() {
        String testedValue = "cz";
        // set lang to TESTVALUE
        basicAttributes.set(BasicAttributes.lang, testedValue);
        generateValidationMessagesWithWait();
        //get attribute lang of element
        Object allAtts = executeJS("var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) {"
            + " items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value; }; return items;",
            getTestedElementRoot().getTarget());
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) allAtts;
        String lang1 = getTestedElementRoot().getTarget().getAttribute("xml:lang");
        String lang2 = getTestedElementRoot().getTarget().getAttribute("lang");
        String lang3 = map.get("xml:lang");
        String attLang = (lang1 == null || lang1.isEmpty() ? (lang2 == null || lang2.isEmpty() ? lang3 : lang2) : lang1);

        Assert.assertEquals(attLang, testedValue, "Attribute xml:lang should be present.");
    }

    public void checkOnclick() {
        testFireEvent(Event.CLICK, getTestedElementRoot(), getGenerateMessagesAction());
    }

    public void checkOndblclick() {
        testFireEvent(Event.DBLCLICK, getTestedElementRoot(), getGenerateMessagesAction());
    }

    public void checkOnkeydown() {
        testFireEvent(Event.KEYDOWN, getTestedElementRoot(), getGenerateMessagesAction());
    }

    public void checkOnkeypress() {
        testFireEvent(Event.KEYPRESS, getTestedElementRoot(), getGenerateMessagesAction());
    }

    public void checkOnkeyup() {
        testFireEvent(Event.KEYUP, getTestedElementRoot(), getGenerateMessagesAction());
    }

    public void checkOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, getTestedElementRoot(), getGenerateMessagesAction());
    }

    public void checkOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, getTestedElementRoot(), getGenerateMessagesAction());
    }

    public void checkOnmouseout() {
        testFireEvent(Event.MOUSEOUT, getTestedElementRoot(), getGenerateMessagesAction());
    }

    public void checkOnmouseover() {
        testFireEvent(Event.MOUSEOVER, getTestedElementRoot(), getGenerateMessagesAction());
    }

    public void checkOnmouseup() {
        testFireEvent(Event.MOUSEUP, getTestedElementRoot(), getGenerateMessagesAction());
    }

    public void checkStyle() {
        final String value = "background-color: yellow;";
        testHTMLAttribute(getTestedElementRoot(), basicAttributes, BasicAttributes.style, value, getGenerateMessagesAction());
    }

    public void checkStyleClass() {
        final String styleClass = "metamer-ftest-class";
        testHTMLAttribute(getTestedElementRoot(), basicAttributes, BasicAttributes.styleClass, styleClass, getGenerateMessagesAction());
    }

    public void checkTitle() {
        testHTMLAttribute(getTestedElementRoot(), basicAttributes, BasicAttributes.title, "RF 5", getGenerateMessagesAction());
    }

    protected void generateValidationMessages() {
        executeJS("window.valuesSettingState=''");
        getPage().getWrongValuesButton().click();
        waitForValuesSetting();
    }

    protected void generateValidationMessagesWithWait() {
        generateValidationMessages();
        waitingForValidationMessagesToShow();
    }

    protected abstract MessageComponentCommonPage getPage();

    protected abstract FutureTarget<WebElement> getTestedElementRoot();

    protected abstract Action getGenerateMessagesAction();

    /**
     * Sets correct values by clicking button and wait for the client update.
     * !Does not do any request!.
     */
    protected void setCorrectValues() {
        getPage().getCorrectValuesButton().click();
        waitForValuesSetting();
    }

    protected void setCorrectValuesWithWaiting() {
        setCorrectValues();
        waitingForValidationMessagesToHide();
    }

    protected void submitWithA4jBtn() {
        MetamerPage.waitRequest(getPage().getA4jCommandButton(), WaitRequestType.XHR).click();
    }

    protected void submitWithHBtn() {
        MetamerPage.waitRequest(getPage().gethCommandButton(), WaitRequestType.HTTP).click();
    }

    protected void waitForValuesSetting() {
        String finishedString = "finished";
        String ret = expectedReturnJS("return window.valuesSettingState", finishedString);
        if (ret == null || !ret.equalsIgnoreCase(finishedString)) {
            throw new IllegalStateException("The setting of values with buttons was not acomplished.");
        }
    }

    /**
     * Implement at lowest level. This should wait for message(s) component to hide.
     * It should hide them (submit the form) if needed.
     */
    protected abstract void waitingForValidationMessagesToHide();

    /**
     * Implement at lowest level. This should wait for message(s) component to show.
     * It should generate them (submit the form) if needed.
     */
    protected abstract void waitingForValidationMessagesToShow();
}
