/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.push;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestPushTopicsContext extends AbstractWebDriverTest {

    @FindBy(css = "div[id$='uuid']")
    private WebElement uuidElement;

    /* *****************************************************************************
     * Tests ********************************************************************* ********
     */
    @Test
    public void testUuidIsChangingInSomeIntervals() {
        String uuid = uuidElement.getText();
        checkTheUuid(uuid);
        List<Long> deviations = new ArrayList<Long>();
        for (int i = 0; i < 20; i++) {
            Long deviation = checkDeviation();
            deviations.add(deviation);
        }
        Collections.sort(deviations);
        long median = deviations.get(9);
        assertTrue(((median > 4800) && (median < 5200)),
            "The median of five measurements should be in range of (4800ms, 5200ms)");
    }

    /* *******************************************************************************
     * Help methods ************************************************************** *****************
     */

    /**
     * Checks the deviation between two pushes, also checking that the uuid is changing
     *
     * @param uuidRetriever
     *            retriever which points to the uuid text
     * @return the deviation between two pushes
     */
    private Long checkDeviation() {
        Long beforePush = System.currentTimeMillis();
        String uuidBefore = uuidElement.getText();
        Graphene.waitAjax(webDriver).withTimeout(30, TimeUnit.SECONDS).until()
                .element(uuidElement)
                .text()
                .not()
                .equalTo(uuidBefore);
        Long afterPush = System.currentTimeMillis();
        checkTheUuid(uuidElement.getText());
        return new Long(afterPush - beforePush);
    }

    /**
     * Check very simply whether uuid is correct, it means whether it has 36 characters and that contains 4 hyphens
     */
    private void checkTheUuid(String uuid) {
        assertEquals(uuid.length(), 36, "The length of uuid is wrong!");
        assertEquals(uuid.matches("-{4}"), "Wrong uuid, there should be 4 hyphens");
    }
}