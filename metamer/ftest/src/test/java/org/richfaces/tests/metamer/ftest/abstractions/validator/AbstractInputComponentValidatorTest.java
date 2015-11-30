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
package org.richfaces.tests.metamer.ftest.abstractions.validator;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.tests.metamer.bean.abstractions.ValidatorBean;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.testng.Assert;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractInputComponentValidatorTest extends AbstractWebDriverTest {

    private static final String CUSTOM_MESSAGE = "Custom message!";
    private static final String VALIDATOR_MESSAGE_ATT = "validatorMessage";

    @FindBy(css = "[id$=message]")
    protected RichFacesMessage message;

    protected abstract void setCorrectValue();

    protected abstract void setIncorrectValue();

    @CoversAttributes(value = { "validator", "validatorMessage" })
    public void testValidator() {
        // set an incorrect value
        setIncorrectValue();
        // message will show up with default detail (in this case specified in validator)
        Assert.assertTrue(message.advanced().isVisible(), "Validator message should be visible");
        Assert.assertEquals(message.getDetail(), ValidatorBean.DEFAULT_VALIDATOR_ERROR_MSG);
        // set validatorMessage
        setAttribute(VALIDATOR_MESSAGE_ATT, CUSTOM_MESSAGE);
        // set an incorrect value
        setIncorrectValue();
        // message will show up with customized detail
        Assert.assertTrue(message.advanced().isVisible(), "Validator message should be visible");
        Assert.assertEquals(message.getDetail(), CUSTOM_MESSAGE);
        // set a correct value
        setCorrectValue();
        // no message should be visible
        Assert.assertFalse(message.advanced().isVisible(), "Validator message should not be visible");
    }

}