/*
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
 */
package org.richfaces.tests.metamer.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.richfaces.model.CalendarDataModel;
import org.richfaces.model.CalendarDataModelItem;
import org.richfaces.tests.metamer.bean.rich.RichCalendarBean;

/**
 * Managed bean with calendar's model.
 *
 * @author Ilya Shaikovsky, Exadel
 * @version $Revision: 21077 $
 */
@ManagedBean
@ApplicationScoped
public class CalendarModel implements CalendarDataModel {

    public static final String WEEKEND_DAY_CLASS = "yellowDay";
    public static final String BUSY_DAY_CLASS = "aquaDay";

    private boolean checkBusyDay(Calendar calendar) {
        return (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY);
    }

    private boolean checkWeekend(Calendar calendar) {
        return (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY);
    }

    @Override
    public CalendarDataModelItem[] getData(Date[] dateArray) {
        CalendarDataModelItem[] modelItems = new CalendarModelItem[dateArray.length];
        Calendar current = GregorianCalendar.getInstance(RichCalendarBean.TIME_ZONE);
        Calendar today = GregorianCalendar.getInstance(RichCalendarBean.TIME_ZONE);
        today.setTime(new Date());
        CalendarModelItem modelItem;
        for (int i = 0; i < dateArray.length; i++) {
            current.setTime(dateArray[i]);
            modelItem = new CalendarModelItem();
            if (checkBusyDay(current)) {
                modelItem.setEnabled(false);
                modelItem.setStyleClass(BUSY_DAY_CLASS);
            } else if (checkWeekend(current)) {
                modelItem.setEnabled(false);
                modelItem.setStyleClass(WEEKEND_DAY_CLASS);
            } else {
                modelItem.setEnabled(true);
                modelItem.setStyleClass("");
            }
            modelItems[i] = modelItem;
        }
        return modelItems;
    }

    @Override
    public Object getToolTip(Date date) {
        return null;
    }
}
