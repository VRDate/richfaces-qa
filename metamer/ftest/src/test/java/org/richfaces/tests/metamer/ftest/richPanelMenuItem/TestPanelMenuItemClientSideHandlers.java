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
package org.richfaces.tests.metamer.ftest.richPanelMenuItem;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.component.Mode;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 */
@IssueTracking("https://issues.jboss.org/browse/RF-10486")
public class TestPanelMenuItemClientSideHandlers extends AbstractWebDriverTest {

    private final String[] ajaxEvents = new String[] { "onbeforeselect", "onbegin", "onbeforedomupdate", "onselect", "oncomplete" };
    private final String[] clientEvents = new String[] { "onbeforeselect", "onselect" };
    private final Attributes<PanelMenuItemAttributes> panelMenuItemAttributes = getAttributes();
    private final String[] serverEvents = new String[] { "onselect" };

    private String event;

    @Page
    private PanelMenuItemPage page;

    public PanelMenuItemPage getPage() {
        return page;
    }

    @Override
    public String getComponentTestPagePath() {
        return "richPanelMenuItem/simple.xhtml";
    }

    @Test
    @CoversAttributes({ "onbeforeselect", "onbegin", "onbeforedomupdate", "onselect", "oncomplete" })
    @UseWithField(field = "event", valuesFrom = FROM_FIELD, value = "ajaxEvents")
    public void testClientSideEvent() {
        panelMenuItemAttributes.set(PanelMenuItemAttributes.mode, Mode.ajax);
        testRequestEventsBefore(event);
        guardAjax(getPage().getItem()).select();
        testRequestEventsAfter(event);
    }

    @Test
    @CoversAttributes({ "onbeforeselect", "onbegin", "onbeforedomupdate", "onselect", "oncomplete" })
    @IssueTracking("https://issues.jboss.org/browse/RF-12549")
    public void testClientSideEventsOrderAjax() {
        panelMenuItemAttributes.set(PanelMenuItemAttributes.mode, Mode.ajax);
        testRequestEventsBefore(ajaxEvents);
        guardAjax(getPage().getItem()).select();
        testRequestEventsAfter(ajaxEvents);
    }

    @Test
    @CoversAttributes({ "onbeforeselect", "onselect" })
    public void testClientSideEventsOrderClient() {
        panelMenuItemAttributes.set(PanelMenuItemAttributes.mode, Mode.client);
        testRequestEventsBefore(clientEvents);
        getPage().getItem().select();
        testRequestEventsAfter(clientEvents);
    }

    @Test
    @Skip
    @CoversAttributes("onselect")
    @IssueTracking("https://issues.jboss.org/browse/RF-10844")
    public void testClientSideEventsOrderServer() {
        panelMenuItemAttributes.set(PanelMenuItemAttributes.mode, Mode.server);
        testRequestEventsBefore(serverEvents);
        guardHttp(getPage().getItem()).select();
        testRequestEventsAfter(serverEvents);
    }

}
