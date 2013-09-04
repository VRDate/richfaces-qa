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
package org.richfaces.tests.showcase.dataTable.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class DataTableEditPage {

    @FindByJQuery("input[value=Delete]:visible")
    public WebElement deleteButtonInpopup;
    @FindByJQuery("input[value=Cancel]:visible")
    public WebElement cancelButtonInpopup;
    @FindByJQuery("input[value=Store]:visible")
    public WebElement storeButtonInpopup;

    @FindByJQuery("table[id$=editGrid] tbody tr:eq(0) td:eq(1)")
    public WebElement vendorpopup;
    @FindByJQuery("table[id$=editGrid] tbody tr:eq(1) td:eq(1)")
    public WebElement modelpopup;
    @FindByJQuery("input[id$=price]")
    public WebElement priceInputpopup;
    @FindByJQuery("input[id$=mage]")
    public WebElement mileageInputpopup;
    @FindByJQuery("input[id$=vin]")
    public WebElement vinInputpopup;

    @FindByJQuery("span[id$=price] span")
    public WebElement errorMsgPrice;
    @FindByJQuery("span[id$=mage] span")
    public WebElement errorMsgMileage;
    @FindByJQuery("span[id$=vin] span")
    public WebElement errorMsgVin;

    @FindByJQuery("tbody[class=rf-dt-b]")
    public WebElement table;

}
