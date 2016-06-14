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
package org.richfaces.tests.metamer.ftest.richSelect;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.ScrollingType;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.select.RichFacesSelect;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richSelect/simple.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestSelect extends AbstractWebDriverTest {

    private static final String TESTSIZE = "300px";

    private final Attributes<SelectAttributes> selectAttributes = getAttributes();

    @FindBy(css = "div[id$=selectItem10]")
    private WebElement item10;
    @FindBy(css = "div.rf-sel-lst-scrl")
    private WebElement listElement;
    @FindBy(css = "div.rf-sel-lst-cord")
    private WebElement listRoot;
    @FindBy(css = "span[id$=output]")
    private WebElement output;
    @FindBy(css = "div[id$=select]")
    private RichFacesSelect select;

    private final Action selectHawaiiGuardedAction = new Action() {
        @Override
        public void perform() {
            Graphene.guardAjax(select.openSelect()).select(10);
        }
    };
    private final Action selectHawaiiWithKeyboardGuardedAction = new Action() {
        @Override
        public void perform() {
            select.advanced().setScrollingType(ScrollingType.BY_KEYS);
            Graphene.guardAjax(select.openSelect()).select(10);
        }
    };

    @BeforeMethod
    public void closeSelectPopupIfVisible() {
        if (select.advanced().isPopupPresent()) {
            // focus somewhere else
            blur(WaitRequestType.NONE);
            // wait for popup to close
            select.advanced().waitUntilSuggestionsAreNotVisible().perform();
        }
    }

    @Override
    public String getComponentTestPagePath() {
        return "richSelect/simple.xhtml";
    }

    @Test(groups = "smoke")
    @CoversAttributes("clientFilterFunction")
    public void testClientFilterFunction() {
        selectAttributes.set(SelectAttributes.clientFilterFunction, "filterValuesByLength");
        select.type("4");// get all states with 4 letters
        List<WebElement> suggestions = select.advanced().getSuggestionsElements();
        assertEquals(suggestions.size(), 3);
        assertEquals(suggestions.get(0).getText(), "Iowa");
        assertEquals(suggestions.get(1).getText(), "Ohio");
        assertEquals(suggestions.get(2).getText(), "Utah");

        select.type("5");// get all states with 5 letters
        suggestions = select.advanced().getSuggestionsElements();
        assertEquals(suggestions.size(), 3);
        assertEquals(suggestions.get(0).getText(), "Idaho");
        assertEquals(suggestions.get(1).getText(), "Maine");
        assertEquals(suggestions.get(2).getText(), "Texas");
    }

    @Test
    @CoversAttributes("defaultLabel")
    @Templates("plain")
    public void testDefaultLabel() {
        selectAttributes.set(SelectAttributes.defaultLabel, "new label");
        assertEquals(select.advanced().getInput().getStringValue(), "new label", "Default label should change");

        selectAttributes.set(SelectAttributes.defaultLabel, "");
        assertPresent(select.advanced().getInput().advanced().getInputElement(), "Input should be present on the page.");
        select.advanced().waitUntilSuggestionsAreNotVisible().perform();
        assertEquals(select.advanced().getInput().getStringValue(), "", "Default label should change");
    }

    @Test
    @CoversAttributes("disabled")
    public void testDisabled() {
        selectAttributes.set(SelectAttributes.disabled, Boolean.TRUE);
        assertPresent(select.advanced().getInput().advanced().getInputElement(), "Input should be present on the page.");
        select.advanced().waitUntilSuggestionsAreNotVisible().perform();
        try {
            select.openSelect();
        } catch (TimeoutException ex) {
            return;
        }
        Assert.fail("Select should be disabled.");
    }

    @Test(groups = "smoke")
    @CoversAttributes("enableManualInput")
    @IssueTracking(value = { "https://issues.jboss.org/browse/RF-9663", "https://issues.jboss.org/browse/RF-9855" })
    public void testEnableManualInput() {
        selectAttributes.set(SelectAttributes.enableManualInput, Boolean.FALSE);
        String readonly = select.advanced().getInput().advanced().getInputElement().getAttribute("readonly");
        assertTrue("readonly".equals(readonly) || "true".equals(readonly), "Input should be read-only");

        select.advanced().setOpenByInputClick(true);
        select.openSelect();
        select.advanced().waitUntilSuggestionsAreVisible().perform();
        selectHawaiiGuardedAction.perform();
        assertTrue(item10.getAttribute("class").contains("rf-sel-sel"));
        assertEquals(output.getText(), "Hawaii");
    }

    @Test
    @CoversAttributes("enableManualInput")
    @Templates(value = "plain")
    public void testEnableManualInputListContent() {
        selectAttributes.set(SelectAttributes.enableManualInput, Boolean.TRUE);
        //These list will be test add more records, if you wanted tests another selection
        List<SelectSettings> valuesToSelect = new ArrayList<TestSelect.SelectSettings>();
        valuesToSelect.add(new SelectSettings(3, 3, 4, "a", "Arkansas"));
        valuesToSelect.add(new SelectSettings(1, 5, 3, "c", "Colorado"));
        valuesToSelect.add(new SelectSettings(0, 9, 1, "g", "Georgia"));
        Graphene.guardAjax(select.openSelect());
        //count number of row which are suggested in the beginning
        int numberOfSuggestionInTheBegining = select.advanced().getSuggestionsElements().size();
        for (SelectSettings value : valuesToSelect) {
            //Filter list
            select.type(value.getFirstChar());
            //Count number of suggestion after first char
            List<WebElement> suggestions = select.advanced().getSuggestionsElements();
            assertEquals(suggestions.size(), value.numberOfSuggestionAfterFirstChar, "Count of filtered options ('" + value.getFirstChar() + "')");
            //Choose value
            Graphene.guardAjax(select.openSelect()).select(value.getPossitionInFilteredList());
            assertEquals(output.getText(), value.getName());
            Graphene.guardAjax(select.openSelect());
            //Count number of suggested element after select
            int numberOfSuggestionAfterSelect = select.advanced().getSuggestionsElements().size();
            //it should match with number of elements before suggestion
            assertEquals(numberOfSuggestionAfterSelect, numberOfSuggestionInTheBegining);
            //Verify if selected elements in properly tagged by css class
            assertTrue(suggestions.get(value.getPosstionInFullList()).getAttribute("class").contains("rf-sel-sel"),
                "The " + value.getPosstionInFullList() + " item should contain class for selected");
        }
    }

    @Test(groups = "smoke")
    @IssueTracking("https://issues.jboss.org/browse/RF-11320")
    public void testFiltering() {
        select.type("a");
        List<WebElement> suggestions = select.advanced().getSuggestionsElements();
        assertEquals(suggestions.size(), 4, "Count of filtered options ('a')");
        String[] selectOptions = { "Alabama", "Alaska", "Arizona", "Arkansas" };
        for (int i = 0; i < selectOptions.length; i++) {
            assertEquals(suggestions.get(i).getText(), selectOptions[i]);
        }
        Graphene.guardAjax(select.openSelect()).select(3);
        assertEquals(output.getText(), "Arkansas");
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Arkansas");
    }

    @Test
    @CoversAttributes("immediate")
    public void testImmediate() {
        selectAttributes.set(SelectAttributes.immediate, Boolean.TRUE);
        selectHawaiiGuardedAction.perform();
        getMetamerPage().assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: -> Hawaii");
    }

    @Test
    @Templates(value = "plain")
    public void testInit() {
        assertEquals(select.advanced().getInput().getStringValue(), "Click here to edit", "Default label");
        assertPresent(select.advanced().getInput().advanced().getInputElement(), "Input should be present on the page.");
        assertPresent(select.advanced().getShowButtonElement(), "Show button should be present on the page.");
        select.advanced().waitUntilSuggestionsAreNotVisible().perform();
    }

    @Test
    @CoversAttributes("itemClass")
    @Templates(value = "plain")
    public void testItemClass() {
        final String value = "metamer-ftest-class";
        selectAttributes.set(SelectAttributes.itemClass, value);
        select.openSelect();
        List<WebElement> suggestions = select.advanced().getSuggestionsElements();
        assertEquals(suggestions.size(), 50);
        for (WebElement webElement : suggestions) {
            assertTrue(webElement.getAttribute("class").contains(value));
        }
    }

    @Test
    @CoversAttributes("listClass")
    @IssueTracking("https://issues.jboss.org/browse/RF-9735")
    @Templates(value = "plain")
    public void testListClass() {
        testStyleClass(listRoot, BasicAttributes.listClass);
    }

    @Test
    @CoversAttributes("listHeight")
    @IssueTracking("https://issues.jboss.org/browse/RF-9737")
    @Templates(value = "plain")
    public void testListHeight() {
        selectAttributes.set(SelectAttributes.listHeight, TESTSIZE);
        select.openSelect();
        assertEquals(listElement.getCssValue("height"), TESTSIZE, "Height of list did not change");

        selectAttributes.set(SelectAttributes.listHeight, "");
        select.openSelect();
        assertEquals(listElement.getCssValue("height"), "100px", "Height of list did not change");
    }

    @Test
    @CoversAttributes("listWidth")
    @IssueTracking("https://issues.jboss.org/browse/RF-9737")
    @Templates(value = "plain")
    public void testListWidth() {
        int tolerance = 20;
        int testedValue = 300;
        int defaultValue = 200;

        selectAttributes.set(SelectAttributes.listWidth, TESTSIZE);
        select.openSelect();
        assertEquals(Integer.valueOf(listElement.getCssValue("width").replace("px", "")), testedValue, tolerance);

        selectAttributes.set(SelectAttributes.listWidth, "");
        select.openSelect();
        assertEquals(Integer.valueOf(listElement.getCssValue("width").replace("px", "")), defaultValue, tolerance);
    }

    @Test
    @CoversAttributes("maxListHeight")
    @Templates(value = "plain")
    public void testMaxListHeight() {
        selectAttributes.set(SelectAttributes.maxListHeight, TESTSIZE);
        select.openSelect();
        assertEquals(listElement.getCssValue("max-height"), TESTSIZE, "Height of list did not change");

        selectAttributes.set(SelectAttributes.maxListHeight, "");
        select.openSelect();
        assertEquals(listElement.getCssValue("max-height"), "100px", "Height of list did not change");
    }

    @Test
    @CoversAttributes("minListHeight")
    @Templates(value = "plain")
    public void testMinListHeight() {
        selectAttributes.set(SelectAttributes.minListHeight, TESTSIZE);
        select.openSelect();
        assertEquals(listElement.getCssValue("min-height"), TESTSIZE, "Height of list did not change");

        selectAttributes.set(SelectAttributes.minListHeight, "");
        select.openSelect();
        assertEquals(listElement.getCssValue("min-height"), "20px", "Height of list did not change");
    }

    @Test
    @CoversAttributes("onblur")
    public void testOnblur() {
        testFireEvent("blur", new Action() {
            @Override
            public void perform() {
                Graphene.guardAjax(select.openSelect()).select(0);
            }
        });
    }

    @Test
    @CoversAttributes("onchange")
    public void testOnchange() {
        testFireEvent("change", selectHawaiiGuardedAction);
    }

    @Test
    @CoversAttributes("onclick")
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(Event.CLICK, select.advanced().getInput().advanced().getInputElement());
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, select.advanced().getInput().advanced().getInputElement());
    }

    @Test
    @CoversAttributes("onfocus")
    @Templates(value = "plain")
    public void testOnfocus() {
        testFireEvent(selectAttributes, SelectAttributes.onfocus,
            new Actions(driver).click(select.advanced().getInput().advanced().getInputElement()).build());
    }

    @Test
    @CoversAttributes("onkeydown")
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, select.advanced().getInput().advanced().getInputElement());
    }

    @Test
    @CoversAttributes("onkeypress")
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, select.advanced().getInput().advanced().getInputElement());
    }

    @Test
    @CoversAttributes("onkeyup")
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, select.advanced().getInput().advanced().getInputElement());
    }

    @Test
    @CoversAttributes("onlistclick")
    @Templates(value = "plain")
    public void testOnlistclick() {
        testFireEvent("listclick", selectHawaiiGuardedAction);
    }

    @Test
    @CoversAttributes("onlistdblclick")
    @Templates(value = "plain")
    public void testOnlistdblclick() {
        testFireEvent("listdblclick", new Action() {
            @Override
            public void perform() {
                select.openSelect();
                Utils.triggerJQ(executor, "dblclick", listElement);
            }
        });
    }

    @Test
    @CoversAttributes("onlisthide")
    public void testOnlisthide() {
        testFireEvent("listhide", new Action() {
            @Override
            public void perform() {
                select.openSelect();
                getMetamerPage().getRequestTimeElement().click();
                select.advanced().waitUntilSuggestionsAreNotVisible().perform();
            }
        });
    }

    @Test
    @CoversAttributes("onlistkeydown")
    @Templates(value = "plain")
    public void testOnlistkeydown() {
        testFireEvent("listkeydown", new Action() {
            @Override
            public void perform() {
                select.openSelect();
                Utils.triggerJQ(executor, "keydown", listElement);
            }
        });
    }

    @Test
    @CoversAttributes("onlistkeypress")
    @Templates(value = "plain")
    public void testOnlistkeypress() {
        testFireEvent("listkeypress", new Action() {
            @Override
            public void perform() {
                select.openSelect();
                Utils.triggerJQ(executor, "keypress", listElement);
            }
        });
    }

    @Test
    @CoversAttributes("onlistkeyup")
    @Templates(value = "plain")
    public void testOnlistkeyup() {
        testFireEvent("listkeyup", new Action() {
            @Override
            public void perform() {
                select.openSelect();
                Utils.triggerJQ(executor, "keyup", listElement);
            }
        });
    }

    @Test
    @CoversAttributes("onlistmousedown")
    @Templates(value = "plain")
    public void testOnlistmousedown() {
        testFireEvent("listmousedown", new Action() {
            @Override
            public void perform() {
                Utils.triggerJQ(executor, "mousedown", listElement);
            }
        });
    }

    @Test
    @CoversAttributes("onlistmousemove")
    @Templates(value = "plain")
    public void testOnlistmousemove() {
        testFireEvent("listmousemove", new Action() {
            @Override
            public void perform() {
                Utils.triggerJQ(executor, "mousemove", listElement);
            }
        });
    }

    @Test
    @CoversAttributes("onlistmouseout")
    @Templates(value = "plain")
    public void testOnlistmouseout() {
        testFireEvent("listmouseout", new Action() {
            @Override
            public void perform() {
                Utils.triggerJQ(executor, "mouseout", listElement);
            }
        });
    }

    @Test
    @CoversAttributes("onlistmouseover")
    @Templates(value = "plain")
    public void testOnlistmouseover() {
        testFireEvent("listmouseover", new Action() {
            @Override
            public void perform() {
                select.openSelect();
                new Actions(driver).moveToElement(listElement).moveToElement(getMetamerPage().getRequestTimeElement()).perform();
            }
        });
    }

    @Test
    @CoversAttributes("onlistmouseup")
    @Templates(value = "plain")
    public void testOnlistmouseup() {
        testFireEvent("listmouseup", selectHawaiiGuardedAction);
    }

    @Test
    @CoversAttributes("onlistshow")
    public void testOnlistshow() {
        testFireEvent("listshow", new Action() {
            @Override
            public void perform() {
                select.openSelect();
            }
        });
    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, select.advanced().getInput().advanced().getInputElement());
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, select.advanced().getInput().advanced().getInputElement());
    }

    @Test
    @CoversAttributes("onmouseout")
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, select.advanced().getInput().advanced().getInputElement());
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, select.advanced().getInput().advanced().getInputElement());
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, select.advanced().getInput().advanced().getInputElement());
    }

    @Test
    @CoversAttributes("onselectitem")
    public void testOnselectitem() {
        testFireEvent("selectitem", selectHawaiiGuardedAction);
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        selectAttributes.set(SelectAttributes.rendered, Boolean.FALSE);
        assertNotPresent(select.advanced().getRootElement(), "Component should not be rendered when rendered=false.");
    }

    @Test
    @CoversAttributes("selectFirst")
    @IssueTracking("https://issues.jboss.org/browse/RF-11320")
    public void testSelectFirst() {
        selectAttributes.set(SelectAttributes.selectFirst, Boolean.TRUE);

        select.type("a");
        List<WebElement> suggestions = select.advanced().getSuggestionsElements();
        assertEquals(suggestions.size(), 4, "Count of filtered options ('a')");
        String[] selectOptions = { "Alabama", "Alaska", "Arizona", "Arkansas" };
        for (int i = 0; i < selectOptions.length; i++) {
            assertEquals(suggestions.get(i).getText(), selectOptions[i]);
        }
        assertTrue(suggestions.get(0).getAttribute("class").contains("rf-sel-sel"),
            "First item should contain class for selected item.");
        new Actions(driver).sendKeys(Keys.RETURN).perform();

        String previousTime = getMetamerPage().getRequestTimeElement().getText();
        Utils.triggerJQ(executor, "blur", select.advanced().getInput().advanced().getInputElement());
        Graphene.waitModel().until().element(getMetamerPage().getRequestTimeElement()).text().not().equalTo(previousTime);
        assertEquals(output.getText(), "Alabama", "Output should be Alabama");
    }

    @Test
    @CoversAttributes("selectItemClass")
    @Templates(value = "plain")
    public void testSelectItemClass() {
        selectAttributes.set(SelectAttributes.selectItemClass, "metamer-ftest-class");

        Graphene.guardAjax(select.openSelect()).select(0);
        select.openSelect();
        List<WebElement> suggestions = select.advanced().getSuggestionsElements();
        // to assert selectItemClass we need to mouse over the element
        new Actions(driver).moveToElement(suggestions.get(0)).build().perform();
        assertTrue(suggestions.get(0).getAttribute("class").contains("metamer-ftest-class"),
            "Selected item should contain set class");
        for (int i = 1; i < suggestions.size(); i++) {
            assertFalse(suggestions.get(i).getAttribute("class").contains("metamer-ftest-class"),
                "Not selected item should not contain set class");
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-14259")
    public void testSelectTypeDelete_errorStyleClassWillBeRemoved() {
        selectAttributes.set(SelectAttributes.enableManualInput, Boolean.TRUE);
        WebElement spanElement = select.advanced().getRootElement().findElement(By.tagName("span"));
        // the input has not the error styleClass
        assertFalse(spanElement.getAttribute("class").contains("rf-sel-fld-err"));

        // open select and select the first option
        Graphene.guardAjax(select.openSelect()).select(0);
        assertEquals(output.getText(), "Alabama", "Output should be Alabama");

        // type 'a'
        select.advanced().getInput().sendKeys("a");
        // the input has the error styleClass
        assertTrue(spanElement.getAttribute("class").contains("rf-sel-fld-err"));
        // no suggestions will be available
        assertEquals(select.advanced().getSuggestionsElements().size(), 0);

        // remove the last character
        select.advanced().getInput().sendKeys(Keys.BACK_SPACE);
        // the input has not the error styleClass
        assertFalse(spanElement.getAttribute("class").contains("rf-sel-fld-err"));
        // all suggestions will be available
        assertEquals(select.advanced().getSuggestionsElements().size(), 50);

        // select the second suggestion
        Graphene.guardAjax(select.openSelect()).select(1);
        assertEquals(output.getText(), "Alaska", "Output should be Alaska");
        // the input has not the error styleClass
        assertFalse(spanElement.getAttribute("class").contains("rf-sel-fld-err"));
    }

    @Test
    public void testSelectWithKeyboard() {
        selectHawaiiWithKeyboardGuardedAction.perform();
        assertTrue(item10.getAttribute("class").contains("rf-sel-sel"),
            "Selected item should contain class for selected option.");
        assertEquals(output.getText(), "Hawaii");
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Hawaii");
    }

    @Test
    public void testSelectWithMouse() {
        Graphene.guardAjax(select.openSelect()).select(10);
        assertTrue(item10.getAttribute("class").contains("rf-sel-sel"),
            "Selected item should contain class for selected option.");
        assertEquals(output.getText(), "Hawaii");
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Hawaii");
    }

    @Test
    @CoversAttributes("showButton")
    public void testShowButton() {
        selectAttributes.set(SelectAttributes.showButton, Boolean.FALSE);
        assertNotVisible(select.advanced().getShowButtonElement(), "Show button should not be visible.");

        selectAttributes.set(SelectAttributes.showButton, Boolean.TRUE);
        assertVisible(select.advanced().getShowButtonElement(), "Show button should be visible.");
    }

    @Test
    @CoversAttributes("showButton")
    public void testShowButtonClick() {
        selectAttributes.set(SelectAttributes.showButton, Boolean.TRUE);
        assertVisible(select.advanced().getShowButtonElement(), "Show button should be visible.");
        select.advanced().setOpenByInputClick(false);
        select.openSelect();
        List<WebElement> suggestions = select.advanced().getSuggestionsElements();
        assertEquals(suggestions.size(), 50, "There should be 50 options.");

        String[] selectOptions = { "Alabama", "Hawaii", "Massachusetts", "New Mexico", "South Dakota" };
        for (int i = 0; i < suggestions.size(); i += 10) {
            assertEquals(suggestions.get(i).getText(), selectOptions[i / 10], "Select option nr. " + i);
        }
        selectHawaiiGuardedAction.perform();
        assertEquals(output.getText(), "Hawaii");
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(select.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(select.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("style")
    @IssueTracking("https://issues.jboss.org/browse/RF-10782")
    @Templates(value = "plain")
    public void testStyleWidth() {
        setAttribute("style", "width: 700px");
        assertEquals(select.advanced().getRootElement().getCssValue("width"), "700px");
    }

    @Test
    @CoversAttributes("tabindex")
    @Templates(value = "plain")
    public void testTabindex() {
        testHTMLAttribute(select.advanced().getInput().advanced().getInputElement(), selectAttributes, SelectAttributes.tabindex, "100");
    }

    @Test
    @CoversAttributes("title")
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(select.advanced().getInput().advanced().getInputElement());
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-14254")
    public void testTypingUnknownWillNotShowSuggestions() {
        selectAttributes.set(SelectAttributes.enableManualInput, Boolean.TRUE);
        WebElement spanElement = select.advanced().getRootElement().findElement(By.tagName("span"));
        // the input has not error styleClass
        assertFalse(spanElement.getAttribute("class").contains("rf-sel-fld-err"));

        // type 'q' (unknown)
        select.type("q");
        // no suggestions will be available
        assertEquals(select.advanced().getSuggestionsElements().size(), 0);
        spanElement = select.advanced().getRootElement().findElement(By.tagName("span"));
        // the input has error styleClass
        assertTrue(spanElement.getAttribute("class").contains("rf-sel-fld-err"));

        // delete
        select.advanced().getInput().sendKeys(Keys.BACK_SPACE);
        // all suggestions will be available
        assertEquals(select.advanced().getSuggestionsElements().size(), 50);
        spanElement = select.advanced().getRootElement().findElement(By.tagName("span"));
        // the input has not error styleClass
        assertFalse(spanElement.getAttribute("class").contains("rf-sel-fld-err"));

        // type 'q' (unknown)
        select.type("q");
        // no suggestions will be available
        assertEquals(select.advanced().getSuggestionsElements().size(), 0);
        spanElement = select.advanced().getRootElement().findElement(By.tagName("span"));
        // the input has error styleClass
        assertTrue(spanElement.getAttribute("class").contains("rf-sel-fld-err"));

        // type 'a'
        select.type("a");
        // 4 suggestions will be available
        assertEquals(select.advanced().getSuggestionsElements().size(), 4);
        spanElement = select.advanced().getRootElement().findElement(By.tagName("span"));
        // the input has not error styleClass
        assertFalse(spanElement.getAttribute("class").contains("rf-sel-fld-err"));

        // type 'aq' (unknown)
        select.type("aq");
        // no suggestions will be available
        assertEquals(select.advanced().getSuggestionsElements().size(), 0);
        spanElement = select.advanced().getRootElement().findElement(By.tagName("span"));
        // the input has not error styleClass
        assertTrue(spanElement.getAttribute("class").contains("rf-sel-fld-err"));
    }

    @Test
    @CoversAttributes("value")
    @Templates("plain")
    public void testValue() {
        selectAttributes.set(SelectAttributes.value, "North Carolina");
        assertEquals(select.advanced().getInput().getStringValue(), "North Carolina", "Input should contain selected value.");
    }

    @Test
    @CoversAttributes("valueChangeListener")
    public void testValueChangeListener() {
        selectHawaiiGuardedAction.perform();
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Hawaii");
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
    }

    class SelectSettings {

        private final int possitionInFilteredList;
        private final int posstionInFullList;
        private int numberOfSuggestionAfterFirstChar;
        private final String firstChar;
        private final String name;

        public SelectSettings(int possitionInFilteredList, int posstionInFullList, int numberOfSuggestionAfterFirstChar,
            String firstChar, String name) {
            this.possitionInFilteredList = possitionInFilteredList;
            this.posstionInFullList = posstionInFullList;
            this.numberOfSuggestionAfterFirstChar = numberOfSuggestionAfterFirstChar;
            this.firstChar = firstChar;
            this.name = name;
        }

        public int getPossitionInFilteredList() {
            return possitionInFilteredList;
        }

        public int getPosstionInFullList() {
            return posstionInFullList;
        }

        public int getNumberOfSuggestionAfterFirstChar() {
            return numberOfSuggestionAfterFirstChar;
        }

        public String getFirstChar() {
            return firstChar;
        }

        public String getName() {
            return name;
        }
    }
}
