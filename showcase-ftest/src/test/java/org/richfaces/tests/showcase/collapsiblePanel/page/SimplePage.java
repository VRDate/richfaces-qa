/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.collapsiblePanel.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;

/**
 *
 * @author pmensik
 */
public class SimplePage {

    @FindByJQuery("div[class*='rf-cp-hdr']:eq(0)")
    public WebElement firstPanel;

    @FindByJQuery("div[class*='rf-cp-hdr']:eq(1)")
    public WebElement secondPanel;

    @FindByJQuery("div[class='rf-cp-b']:eq(0)")
    public WebElement firstPanelContent;

    @FindByJQuery("div[class='rf-cp-b']:eq(1)")
    public WebElement secondPanelContent;
}
