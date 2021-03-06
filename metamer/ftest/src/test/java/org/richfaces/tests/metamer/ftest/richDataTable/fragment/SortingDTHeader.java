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
package org.richfaces.tests.metamer.ftest.richDataTable.fragment;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SortingHeaderInterface;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class SortingDTHeader implements SortingHeaderInterface {

    @FindBy(css = "th:nth-of-type(1) a")
    private WebElement sexSortingAnchor;

    @FindBy(css = "th:nth-of-type(2) a")
    private WebElement nameSortingAnchor;

    @FindBy(css = "th:nth-of-type(3) a")
    private WebElement titleSortingAnchor;

    @FindBy(css = "th:nth-of-type(4) a")
    private WebElement numberOfKidsSortingAnchor;

    @FindByJQuery("[data-columnid$='columnName']")
    private WebElement nameSortingBuiltIn;

    @FindByJQuery("[data-columnid$='columnTitle']")
    private WebElement titleSortingBuiltIn;

    @FindByJQuery("[data-columnid$='columnNumberOfKids1']")
    private WebElement numberOfKidsSortingBuiltIn;

    @FindByJQuery("[data-columnid$='columnSex']")
    private WebElement sexSortingBuiltIn;

    @Override
    public void sortBySex(boolean isBuiltIn) {
        if (isBuiltIn) {
            sortByColumn(sexSortingBuiltIn);
        } else {
            sortByColumn(sexSortingAnchor);
        }
    }

    @Override
    public void sortByName(boolean isBuiltIn) {
        if (isBuiltIn) {
            sortByColumn(nameSortingBuiltIn);
        } else {
            sortByColumn(nameSortingAnchor);
        }
    }

    @Override
    public void sortByTitle(boolean isBuiltIn) {
        if (isBuiltIn) {
            sortByColumn(titleSortingBuiltIn);
        } else {
            sortByColumn(titleSortingAnchor);
        }
    }

    @Override
    public void sortByNumberOfKids(boolean isBuiltIn) {
        if (isBuiltIn) {
            sortByColumn(numberOfKidsSortingBuiltIn);
        } else {
            sortByColumn(numberOfKidsSortingAnchor);
        }
    }

    private void sortByColumn(WebElement sortingTrigger) {
        Graphene.guardAjax(sortingTrigger).click();
    }
}
