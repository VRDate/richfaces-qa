/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.showcase.progressBar;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestClientProgressBar extends AbstractProgressBarTest {

    private static final int MAX_DEVIATION = runInPortalEnv ? 5000 : 2500;
    private static final int GRAPHICAL_DEVIATION = 5;
    private static final String STYLE_NAME = "style";
    private static final String START_WIDTH = "width: 0%;";
    private static final String END_WIDTH = "width: 100%;";

    @FindByJQuery("input[type='button']:eq(0)")
    private WebElement startButton;

    @FindBy(css = "div[class='rf-pb-prgs']")
    private WebElement progressBar;

    @Test
    public void testClientProgressBarIsRisingGraphically() {
        startButton.click();
        String width = START_WIDTH;
        while (!width.equals(END_WIDTH)) {
            long currentTimeBeforeChange = System.currentTimeMillis();
            waitGui(webDriver).withTimeout(20, TimeUnit.SECONDS)
                    .until()
                    .element(progressBar)
                    .attribute(STYLE_NAME)
                    .not()
                    .equalTo(width);
            width = progressBar.getAttribute(STYLE_NAME);

            long currentTimeAfterChange = System.currentTimeMillis();
            long duration = currentTimeAfterChange - currentTimeBeforeChange;
            assertTrue(duration < MAX_DEVIATION, "The graphical rising of progress bar should not take more than " + MAX_DEVIATION + ", and "
                    + "was " + duration);
            getTheNumberFromValueAndSaveToList(width.split(" ")[1]);
        }
        checkTheDeviationInList(GRAPHICAL_DEVIATION);
    }
}
