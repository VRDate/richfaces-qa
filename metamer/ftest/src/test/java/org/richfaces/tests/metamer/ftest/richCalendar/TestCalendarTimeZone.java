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
package org.richfaces.tests.metamer.ftest.richCalendar;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.WebDriver;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCalendarTimeZone extends AbstractCalendarTest {

    private DateTimeFormatter dtf;

    @Override
    public String getComponentTestPagePath() {
        return "richCalendar/timeZone.xhtml";
    }

    @BeforeMethod
    public void initDateFormatter() {
        this.dtf = DateTimeFormat.forPattern(calendarAttributes.get(CalendarAttributes.datePattern));
    }

    private void setTimeZone(String tz, DateTime startTime) {
        executeJS("$('input[id$=timeZoneInput]').val('" + tz + "').change()");
        waitForCalendarInputChange(startTime);
    }

    @Test
    @CoversAttributes("timeZone")
    public void testTimeZone() {
        popupCalendar.openPopup().getFooterControls().todayDate();
        final DateTime dtStart = dtf.parseDateTime(popupCalendar.getInput().getStringValue());
        setTimeZone("GMT+01:00", dtStart);
        DateTime dtPlus1H = dtf.parseDateTime(popupCalendar.getInput().getStringValue());
        setTimeZone("GMT-02:00", dtStart);
        DateTime dtMinus2H = dtf.parseDateTime(popupCalendar.getInput().getStringValue());
        assertEquals(dtPlus1H.getHourOfDay(), dtStart.plusHours(1).getHourOfDay());
        assertEquals(dtMinus2H.getHourOfDay(), dtStart.minusHours(2).getHourOfDay());
    }

    private void waitForCalendarInputChange(final DateTime dtStart) {
        Graphene.waitModel()
            .withMessage("Calendar time has not changed after setting a different time zone.")
            .until(new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver input) {
                    return dtStart.getHourOfDay()
                    != dtf.parseDateTime(popupCalendar.getInput().getStringValue()).getHourOfDay();
                }
            });
    }
}
