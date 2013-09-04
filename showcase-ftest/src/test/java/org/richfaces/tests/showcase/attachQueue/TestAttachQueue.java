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
package org.richfaces.tests.showcase.attachQueue;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.attachQueue.page.AttachQueuePage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestAttachQueue extends AbstractWebDriverTest {

    private static final int DELAY_IN_MILISECONDS = 2000;
    private static final int NO_DELAY = 0;

    @Page
    private AttachQueuePage page;

    @Test
    public void testInput() {
        for (int i = 0; i < 5; i++) {
            typeToTheInputAndCheckTheDelay();
        }
    }

    @Test
    public void testButton() {
        for (int i = 0; i < 5; i++) {
            clickOnTheButtonAndCheckTheDelay();
        }
    }

    /*
     * types a character to the input and check whether delay after which the ajax processing is visible is between
     * DELAY_IN_MILISECONDS and DELAY_IN_MILISECONDS + 1000
     */
    private void typeToTheInputAndCheckTheDelay() {
        long timeBeforePressingKey = System.currentTimeMillis();
        page.input.sendKeys("a");
        waitGui(webDriver).withTimeout(3, TimeUnit.SECONDS)
                .until()
                .element(page.ajaxRequestProcessing)
                .is()
                .visible();
        long timeAfterAjaxRequestIsPresent = System.currentTimeMillis();
        page.submit.click();
        waitGui(webDriver).until()
                .element(page.ajaxRequestProcessing)
                .is()
                .visible();
        long actualDelay = timeAfterAjaxRequestIsPresent - timeBeforePressingKey;
        assertTrue((actualDelay >= DELAY_IN_MILISECONDS) && (actualDelay <= DELAY_IN_MILISECONDS + 1000),
            "The delay should be between " + DELAY_IN_MILISECONDS + "ms and " + (DELAY_IN_MILISECONDS + 1000)
                + "ms but was:" + actualDelay);
    }

    /*
     * clicks on the button and check whether delay after which the ajax processing is visible is NO_DELAY
     * */
    private void clickOnTheButtonAndCheckTheDelay() {
        long timeBeforePressingKey = System.currentTimeMillis();
        page.submit.click();
        waitGui(webDriver).until()
                .element(page.ajaxRequestProcessing)
                .is()
                .visible();
        long actualDelay = System.currentTimeMillis() - timeBeforePressingKey;
        assertTrue((actualDelay >= NO_DELAY) && (actualDelay <= NO_DELAY + 500), "The delay should be between "
            + NO_DELAY + "ms and " + (NO_DELAY + 500) + "ms but was:!" + actualDelay);
    }
}
