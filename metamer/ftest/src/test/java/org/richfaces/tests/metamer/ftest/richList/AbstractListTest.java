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
package org.richfaces.tests.metamer.ftest.richList;

import static java.lang.Math.max;
import static java.lang.Math.min;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.list.RichFacesList;
import org.richfaces.fragment.list.RichFacesListItem;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.model.Employee;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractListTest extends AbstractWebDriverTest {

    static final List<Employee> employees = Model.unmarshallEmployees();
    static final int ELEMENTS_TOTAL = employees.size();
    static final Integer[] INTS = { -1, 0, 1, ELEMENTS_TOTAL / 2, ELEMENTS_TOTAL - 1, ELEMENTS_TOTAL, ELEMENTS_TOTAL + 1 };
    private static final String START_ATTRIBUTE = "start";

    @FindBy(css = "[id$=richList]")
    protected RichFacesList list;

    protected Integer first;
    protected Integer rows;
    protected int expectedBegin;
    protected int displayedRows;
    protected int expectedEnd;
    List<Employee> expectedEmployees;

    private void countExpectedValues() {
        initFirst();
        initRows();
        // expected begin
        if (first < 0) {
            expectedBegin = 0;
        } else {
            expectedBegin = first;
        }
        expectedBegin = minMax(0, expectedBegin, ELEMENTS_TOTAL);
        // expected displayed rows
        if (rows < 1 || rows > ELEMENTS_TOTAL) {
            displayedRows = ELEMENTS_TOTAL;
        } else {
            displayedRows = rows;
        }
        if (first < 0) {
            displayedRows = 0;
        }
        displayedRows = min(displayedRows, ELEMENTS_TOTAL - expectedBegin);
        // expected end
        if (rows < 1) {
            expectedEnd = ELEMENTS_TOTAL - 1;
        } else {
            expectedEnd = rows - 1;
        }
        expectedEmployees = employees.subList(expectedBegin, expectedBegin + displayedRows);
    }

    private void initFirst() throws NumberFormatException {
        String firstAtt = getAttributes().get(ListAttributes.first);
        if (firstAtt != null && !firstAtt.isEmpty()) {
            first = Integer.valueOf(firstAtt);
        } else {
            first = 0;
        }
    }

    private void initRows() throws NumberFormatException {
        String rowsAtt = getAttributes().get(ListAttributes.rows);
        if (rowsAtt != null && !rowsAtt.isEmpty()) {
            rows = Integer.valueOf(rowsAtt);
        } else {
            rows = Integer.MAX_VALUE;
        }
    }

    private void countExpectedValues(int activePageNumber) {
        initRows();
        initFirst();
        expectedBegin = rows * (activePageNumber - 1) + (first >= 0 ? first : 0);
        displayedRows = rows;
        expectedEmployees = employees.subList(expectedBegin, expectedBegin + rows);
    }

    private int minMax(int min, int value, int max) {
        return max(min, min(max, value));
    }

    private void verifyCounts(boolean usingDataScroller) {
        assertEquals(list.getItems().size(), displayedRows);
        // RF-11781:
        assertEquals(list.getRootElement().getAttribute(START_ATTRIBUTE), String.valueOf(usingDataScroller ? expectedBegin + 1 : first + 1));
    }

    protected void verifyList() {
        countExpectedValues();
        verifyCounts(false);
        verifyRows();
    }

    protected void verifyList(int activePageNumber) {
        countExpectedValues(activePageNumber);
        verifyCounts(true);
        verifyRows();
    }

    private void verifyRows() {
        List<RichFacesListItem> items = list.getItems();
        int rowCount = items.size();
        if (rowCount > 0) {
            List<Integer> rowsToTest = getListWithTestPages(rowCount);
            for (Integer position : rowsToTest) {
                Employee employee = expectedEmployees.get(position);
                assertEquals(items.get(position).getText(), employee.getName());
            }
        }
    }

    /**
     * Returns a list of integers which stand for number of rows which we are going to test.
     *
     * @param visiblePageRows number of visible rows on the current page
     * @return List of integers representing a set of rows to test
     */
    private List<Integer> getListWithTestPages(int visiblePageRows) {
        List<Integer> rowsToTest = new ArrayList<Integer>();
        rowsToTest.add(0); // first item
        rowsToTest.add((int) Math.round((visiblePageRows - 1) / 2 - 0.5 * (visiblePageRows - 1) / 2)); // item in first quarter
        rowsToTest.add((int) Math.round((visiblePageRows - 1) / 2)); // item in half
        rowsToTest.add((int) Math.round((visiblePageRows - 1) / 2 + 0.5 * (visiblePageRows - 1) / 2)); // item in third quarter
        rowsToTest.add(visiblePageRows - 1); // last item
        return Collections.unmodifiableList(rowsToTest);
    }
}
