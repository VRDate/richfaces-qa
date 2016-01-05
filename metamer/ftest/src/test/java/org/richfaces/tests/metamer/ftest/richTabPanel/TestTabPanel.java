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
package org.richfaces.tests.metamer.ftest.richTabPanel;

import static org.richfaces.fragment.switchable.SwitchType.CLIENT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.switchable.SwitchType;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richTabPanel/simple.xhtml
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class TestTabPanel extends AbstractWebDriverTest {

    private final Attributes<TabPanelAttributes> tabPanelAttributes = getAttributes();

    @FindBy(xpath = "//input[contains(@id, 'switchButton1')]")
    private WebElement moveTo;

    @Page
    private TabPanelSimplePage page;

    @Override
    public String getComponentTestPagePath() {
        return "richTabPanel/simple.xhtml";
    }

    @Test
    @CoversAttributes("activeItem")
    @RegressionTest("https://issues.jboss.org/browse/RF-10351")
    public void testActiveItem() {
        // assert tab panel is visible
        assertVisible(page.getPanelTabAsWebElement(), "Panel Tab is not visible!");

        for (int i = 1; i < 3; i++) {
            tabPanelAttributes.set(TabPanelAttributes.activeItem, "tab" + i);
            WebElement active = page.getTabPanel().advanced().getActiveHeaderElement();
            assertEquals(active.getText(), "tab" + i + " header");
        }
    }

    @Test
    @CoversAttributes("cycledSwitching")
    public void testCycledSwitching() {
        String panelId = (String) executeJS("return testedComponentId");
        String result = "someString";
        result = (String) executeJS("return RichFaces.component('" + panelId + "').nextItem('tab5')");

        assertEquals(result, null);

        result = (String) executeJS("return RichFaces.component('" + panelId + "').prevItem('tab1')");
        assertEquals(result, null);

        // turn cycled switching on
        tabPanelAttributes.set(TabPanelAttributes.cycledSwitching, Boolean.TRUE);
        result = (String) executeJS("return RichFaces.component('" + panelId + "').nextItem('tab5')");
        assertEquals(result, "tab1");

        result = (String) executeJS("return RichFaces.component('" + panelId + "').prevItem('tab1')");
        assertEquals(result, "tab5");
    }

    @Test
    @CoversAttributes("dir")
    @Templates(value = "plain")
    public void testDir() {
        super.testDir(page.getPanelTabAsWebElement());
    }

    @Test
    @CoversAttributes("headerAlignment")
    @Templates(exclude = { "hDataTable", "richCollapsibleSubTable", "richDataGrid", "richDataTable" })
    public void testHeaderAlignment() {
        // assert initial setting, header should be on the left

        assertPresent(driver.findElement(By.xpath("//td[contains(@style, 'padding-right: 5px; width: 100%')]")),
            "Header is not on the left!");

        // move header to right and assert
        tabPanelAttributes.set(TabPanelAttributes.headerAlignment, "right");
        assertPresent(driver.findElement(By.xpath("//td[contains(@style, 'padding-left: 5px; width:100%')]")),
            "Header is not on the right!");

        // move the header back to left and assert
        tabPanelAttributes.set(TabPanelAttributes.headerAlignment, "left");
        assertPresent(driver.findElement(By.xpath("//td[contains(@style, 'padding-right: 5px; width: 100%')]")),
            "Header is not on the left!");
    }

    @Test
    @CoversAttributes("headerAlignment")
    @RegressionTest("https://issues.jboss.org/browse/RF-11550")
    @Templates(value = { "hDataTable", "richCollapsibleSubTable", "richDataGrid", "richDataTable" })
    public void testHeaderAlignmentIterationComponents() {
        testHeaderAlignment();
    }

    @Test
    @CoversAttributes("headerPosition")
    @Templates(value = "plain")
    public void testHeaderPosition() {
        // assert initial settings
        assertPresent(driver.findElement(By.xpath("//div[@class = 'rf-tab-hdr-tabline-vis rf-tab-hdr-tabline-top']")),
            "Header should be in the top!");

        // move to the bottom
        tabPanelAttributes.set(TabPanelAttributes.headerPosition, "bottom");
        assertPresent(driver.findElement(By.xpath("//div[@class = 'rf-tab-hdr-tabline-vis rf-tab-hdr-tabline-btm']")),
            "Header should be in the top!");

        // move back to top and assert
        tabPanelAttributes.set(TabPanelAttributes.headerPosition, "top");
        assertPresent(driver.findElement(By.xpath("//div[@class = 'rf-tab-hdr-tabline-vis rf-tab-hdr-tabline-top']")),
            "Header should be in the top!");
    }

    @Test
    @CoversAttributes("immediate")
    @RegressionTest("https://issues.jboss.org/browse/RF-10054")
    public void testImmediate() {
        tabPanelAttributes.set(TabPanelAttributes.immediate, Boolean.TRUE);
        page.getTabPanel().switchTo(2);
        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "item changed: tab1 -> tab3");
    }

    @Test
    public void testInit() {
        // assert panel tab visibility
        assertVisible(page.getPanelTabAsWebElement(), "Panel tab is not visible!");

        List<WebElement> allHeaders = page.getTabPanel().advanced().getAllVisibleHeadersElements();
        assertEquals(allHeaders.size(), 5);

        List<WebElement> allInactiveHeaders = page.getTabPanel().advanced().getAllInactiveHeadersElements();
        assertEquals(allInactiveHeaders.size(), 3);

        List<WebElement> allDisabledHeaders = page.getTabPanel().advanced().getAllDisabledHeadersElements();
        assertEquals(allDisabledHeaders.size(), 1);
        assertVisible(allDisabledHeaders.get(0), "Fourth disabled tab should be visible!");
    }

    @Test
    @CoversAttributes("itemChangeListener")
    @RegressionTest("https://issues.jboss.org/browse/RF-10523")
    public void testItemChangeListener() {
        page.getTabPanel().switchTo(2);
        page.assertListener(PhaseId.UPDATE_MODEL_VALUES, "item changed: tab1 -> tab3");
    }

    @Test
    @CoversAttributes({ "onbeforeitemchange", "onitemchange" })
    @RegressionTest("https://issues.jboss.org/browse/RF-10165")
    public void testItemchangeEvents() {
        attsSetter()
            .setAttribute(TabPanelAttributes.onbeforeitemchange).toValue("metamerEvents += \"beforeitemchange \"")
            .setAttribute(TabPanelAttributes.onitemchange).toValue("metamerEvents += \"itemchange \"")
            .asSingleAction().perform();
        executeJS("window.metamerEvents = \"\";");
        page.getTabPanel().switchTo(2);

        Object obj = executeJS("return window.metamerEvents");
        String[] events = ((String) obj).split(" ");

        assertEquals(events.length, 2, "Two events should be fired - beforeitemchange and itemchange.");
        assertEquals(events[0], "beforeitemchange", "Attribute onbeforeitemchange doesn't work");
        assertEquals(events[1], "itemchange", "Attribute onitemchange doesn't work");
    }

    @Test
    @CoversAttributes("lang")
    @Templates(value = "plain")
    public void testLang() {
        testLang(page.getPanelTabAsWebElement());
    }

    @Test
    @CoversAttributes("onbeforeitemchange")
    public void testOnbeforeitemchange() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.onbeforeitemchange, new Action() {
            @Override
            public void perform() {
                new Actions(driver).click(page.getTabPanel().advanced().getAllInactiveHeadersElements().get(2)).build().perform();
            }
        });
    }

    @Test
    @CoversAttributes("onclick")
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.onclick, new Action() {
            @Override
            public void perform() {
                new Actions(driver).click(page.getTabPanel().advanced().getAllInactiveHeadersElements().get(2)).build().perform();
            }
        });
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.ondblclick, new Action() {
            @Override
            public void perform() {
                new Actions(driver).doubleClick(page.getPanelTabAsWebElement()).build().perform();
            }
        });
    }

    @Test
    @CoversAttributes("onitemchange")
    public void testOnitemchange() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.onitemchange, new Action() {
            @Override
            public void perform() {
                new Actions(driver).click(page.getTabPanel().advanced().getAllInactiveHeadersElements().get(2)).build().perform();
            }
        });
    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.onmousedown, new Action() {
            @Override
            public void perform() {
                new Actions(driver).clickAndHold(page.getPanelTabAsWebElement()).release().build().perform();
            }
        });
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.onmousemove, new Action() {
            @Override
            public void perform() {
                new Actions(driver).moveToElement(page.getPanelTabAsWebElement()).build().perform();
            }
        });
    }

    @Test
    @CoversAttributes("onmouseout")
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.onmouseout, new Action() {
            @Override
            public void perform() {
                new Actions(driver).moveToElement(page.getPanelTabAsWebElement()).moveToElement(moveTo).build().perform();
            }
        });
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.onmouseover, new Action() {
            @Override
            public void perform() {
                new Actions(driver).moveToElement(page.getPanelTabAsWebElement()).build().perform();
            }
        });
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.onmouseup, new Action() {
            @Override
            public void perform() {
                new Actions(driver).moveToElement(moveTo).clickAndHold().moveToElement(page.getPanelTabAsWebElement())
                    .release().build().perform();
            }
        });
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        tabPanelAttributes.set(TabPanelAttributes.rendered, Boolean.FALSE);
        assertNotPresent(page.getPanelTabAsWebElement(), "TabPanel should not be rendered!");
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(page.getPanelTabAsWebElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(page.getPanelTabAsWebElement());
    }

    @Test
    @CoversAttributes("switchType")
    @Templates(value = "plain")
    public void testSwitchTypeAjax() {
        // since ajax is default choice, null type test will test this too
        tabPanelAttributes.set(TabPanelAttributes.switchType, "ajax");
        testSwitchTypeNull();
    }

    @Test
    @CoversAttributes("switchType")
    public void testSwitchTypeClient() {
        tabPanelAttributes.set(TabPanelAttributes.switchType, "client");
        page.getTabPanel().advanced().setSwitchType(CLIENT);

        for (int i = 2; i >= 0; i--) {
            page.getTabPanel().switchTo(i);
        }
    }

    @Test
    @CoversAttributes("switchType")
    public void testSwitchTypeNull() {
        for (int i = 2; i >= 0; i--) {
            page.getTabPanel().switchTo(i);
        }
    }

    @Test(groups = "smoke")
    @CoversAttributes("switchType")
    @RegressionTest("https://issues.jboss.org/browse/RF-10040")
    public void testSwitchTypeServer() {
        tabPanelAttributes.set(TabPanelAttributes.switchType, "server");
        page.getTabPanel().advanced().setSwitchType(SwitchType.SERVER);

        for (int i = 2; i >= 0; i--) {
            page.getTabPanel().switchTo(i);
        }
    }

    @Test
    @CoversAttributes("tabActiveHeaderClass")
    @IssueTracking("https://issues.jboss.org/browse/RF-9309")
    @Templates(value = "plain")
    public void testTabActiveHeaderClass() {
        String headerClass = "metamer-ftest-class";
        tabPanelAttributes.set(TabPanelAttributes.tabActiveHeaderClass, headerClass);

        WebElement activeElement = page.getTabPanel().advanced().getActiveHeaderElement();
        assertTrue(activeElement.getAttribute("class").contains(headerClass), "tabActiveHeaderClass does not work");

        for (WebElement elem : page.getTabPanel().advanced().getAllDisabledHeadersElements()) {
            assertFalse(elem.getAttribute("class").contains(headerClass), "tabActiveHeaderClass does not work");
        }

        for (WebElement elem : page.getTabPanel().advanced().getAllInactiveHeadersElements()) {
            assertFalse(elem.getAttribute("class").contains(headerClass), "tabActiveHeaderClass does not work");
        }
    }

    @Test
    @CoversAttributes("tabContentClass")
    @Templates(value = "plain")
    public void testTabContentClass() {
        String contentClass = "metamer-ftest-class";
        tabPanelAttributes.set(TabPanelAttributes.tabContentClass, contentClass);
        assertTrue(driver.findElement(By.xpath("//div[contains(text(), 'content of tab 1')] ")).getAttribute("class")
            .contains(contentClass));
    }

    @Test
    @CoversAttributes("tabDisabledHeaderClass")
    @IssueTracking("https://issues.jboss.org/browse/RF-9309")
    @Templates(value = "plain")
    public void testTabDisabledHeaderClass() {
        String headerClass = "metamer-ftest-class";
        tabPanelAttributes.set(TabPanelAttributes.tabDisabledHeaderClass, headerClass);

        WebElement activeElement = page.getTabPanel().advanced().getActiveHeaderElement();
        assertFalse(activeElement.getAttribute("class").contains(headerClass), "tabDisabledHeaderClass does not work");

        for (WebElement elem : page.getTabPanel().advanced().getAllDisabledHeadersElements()) {
            assertTrue(elem.getAttribute("class").contains(headerClass), "tabDisabledHeaderClass does not work");
        }

        for (WebElement elem : page.getTabPanel().advanced().getAllInactiveHeadersElements()) {
            assertFalse(elem.getAttribute("class").contains(headerClass), "tabDisabledHeaderClass does not work");
        }
    }

    @Test
    @CoversAttributes("tabHeaderClass")
    @IssueTracking({ "https://issues.jboss.org/browse/RF-9309", "https://issues.jboss.org/browse/RF-11549" })
    @Templates(value = "plain")
    public void testTabHeaderClass() {
        String testString = "metamer-ftest-class";
        tabPanelAttributes.set(TabPanelAttributes.tabHeaderClass, testString);

        WebElement activeElement = page.getTabPanel().advanced().getActiveHeaderElement();
        assertTrue(activeElement.getAttribute("class").contains(testString), "tabHeaderClass does not work");

        for (WebElement elem : page.getTabPanel().advanced().getAllDisabledHeadersElements()) {
            assertTrue(elem.getAttribute("class").contains(testString), "tabHeaderClass does not work");
        }

        for (WebElement elem : page.getTabPanel().advanced().getAllInactiveHeadersElements()) {
            assertTrue(elem.getAttribute("class").contains(testString), "tabHeaderClass does not work");
        }
    }

    @Test
    @CoversAttributes("tabInactiveHeaderClass")
    @IssueTracking("https://issues.jboss.org/browse/RF-9309")
    @Templates(value = "plain")
    public void testTabInactiveHeaderClass() {
        String testString = "metamer-ftest-class";
        tabPanelAttributes.set(TabPanelAttributes.tabInactiveHeaderClass, testString);

        WebElement activeElement = page.getTabPanel().advanced().getActiveHeaderElement();
        assertFalse(activeElement.getAttribute("class").contains(testString), "tabInactiveHeaderClass does not work");

        for (WebElement elem : page.getTabPanel().advanced().getAllDisabledHeadersElements()) {
            assertFalse(elem.getAttribute("class").contains(testString), "tabInactiveHeaderClass does not work");
        }

        for (WebElement elem : page.getTabPanel().advanced().getAllInactiveHeadersElements()) {
            assertTrue(elem.getAttribute("class").contains(testString), "tabInactiveHeaderClass does not work");
        }
    }

    @Test
    @CoversAttributes("title")
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(page.getPanelTabAsWebElement());
    }
}
