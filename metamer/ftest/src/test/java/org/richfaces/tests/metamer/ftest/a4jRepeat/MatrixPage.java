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
package org.richfaces.tests.metamer.ftest.a4jRepeat;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Vector;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 5.0.0.Alpha1
 */
public class MatrixPage extends MetamerPage {

    public static final int ROWS_COUNT = 4;
    public static final int COLUMNS_COUNT = 4;
    public static final By BY_CELL = By.cssSelector("div.cell");
    public static final By BY_INPUT = By.cssSelector("input[type=text]");
    public static final By BY_INCREMENTLINK = By.cssSelector("a[id$=increaseLink]");
    public static final By BY_DECREMENTLINK = By.cssSelector("a[id$=decreaseLink]");
    public static final By BY_CLEARLINK = By.cssSelector("a[id$=clearLink]");

    @FindBy(css = "div[id$=matrixInput] > table > tbody > tr")
    private List<WebElement> inputRowsElements;
    @FindBy(css = "div[id$=matrixOutput] > table > tbody > tr")
    private List<WebElement> outputRowsElements;

    private Vector<Vector<Integer>> matrix;

    /**
     * @return the inputRowsElements
     */
    public List<WebElement> getInputRowsElements() {
        return inputRowsElements;
    }

    /**
     * @return the matrix
     */
    public Vector<Vector<Integer>> getMatrix() {
        return matrix;
    }

    /**
     * @return the outputRowsElements
     */
    public List<WebElement> getOutputRowsElements() {
        return outputRowsElements;
    }

    public void incrementValue(int row, int column) {
        Graphene.guardAjax(getInputRowsElements().get(row).findElements(BY_CELL).get(column).findElement(BY_INCREMENTLINK)).click();

        Integer oldValue = getMatrix().get(row).get(column);
        getMatrix().get(row).set(column, oldValue + 1);
    }

    public void decrementValue(int row, int column) {
        Graphene.guardAjax(getInputRowsElements().get(row).findElements(BY_CELL).get(column).findElement(BY_DECREMENTLINK)).click();

        Integer oldValue = getMatrix().get(row).get(column);
        getMatrix().get(row).set(column, oldValue - 1);
    }

    public void changeValue(int row, int column, int newValue) {
        WebElement inputElement = getInputRowsElements().get(row).findElements(BY_CELL).get(column).findElement(BY_INPUT);
        inputElement.clear();
        Graphene.guardAjax(inputElement).sendKeys(Integer.toString(newValue), Keys.RETURN);

        getMatrix().get(row).set(column, newValue);
    }

    public void clearValue(int row, int column) {
        Graphene.guardAjax(getInputRowsElements().get(row).findElements(BY_CELL).get(column).findElement(BY_CLEARLINK)).click();

        getMatrix().get(row).set(column, 0);
    }

    public Integer obtainOutputValue(int row, int column) {
        return Integer.valueOf(getOutputRowsElements().get(row).findElements(BY_CELL).get(column).getText());
    }

    public Integer obtainInputValue(int row, int column) {
        return Integer.valueOf(getInputRowsElements().get(row).findElements(BY_CELL).get(column).findElement(BY_INPUT)
            .getAttribute("value"));
    }

    public void checkMatrix() {
        for (int row = 0; row < ROWS_COUNT; row++) {
            for (int column = 0; column < COLUMNS_COUNT; column++) {
                Integer expectedValue = getMatrix().get(row).get(column);
                assertEquals(obtainInputValue(row, column), expectedValue,
                    String.format("The input value on coordinates row: %s, column: %s does not match: ", row, column));
                assertEquals(obtainOutputValue(row, column), expectedValue,
                    String.format("The output value on coordinates row: %s, column: %s does not match: ", row, column));
            }
        }
    }

    public void initializeMatrix() {
        matrix = new Vector<Vector<Integer>>(COLUMNS_COUNT);
        for (int x = 0; x < COLUMNS_COUNT; x++) {
            getMatrix().add(x, new Vector<Integer>(ROWS_COUNT));
            for (int y = 0; y < ROWS_COUNT; y++) {
                getMatrix().get(x).add(y, 0);
            }
        }
    }
}
