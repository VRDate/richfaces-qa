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
package org.richfaces.tests.metamer.bean.rich;

import static org.richfaces.tests.metamer.bean.RichBean.logToPage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.richfaces.component.UIDataScroller;
import org.richfaces.event.DataScrollEvent;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:dataScroller.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22977 $
 */
@ManagedBean(name = "richDataScrollerBean")
@SessionScoped
public class RichDataScrollerBean implements Serializable {

    private static Logger logger;
    private static final long serialVersionUID = 122475400649809L;
    private Attributes attributes;
    private Map<String, String> facets;

    @ManagedProperty(value = "#{model.capitals.size()}")
    private int size; // used for customized page facet
    private boolean state = true;
    private Attributes tableAttributes;

    public Attributes getAttributes() {
        return attributes;
    }

    public Map<String, String> getFacets() {
        return facets;
    }

    /**
     * Used for customized page facet (customizedFacets.xhtml)
     *
     * @return List of items from which you can choose
     */
    public List<SelectItem> getPagesToScroll() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        double rows = Integer.parseInt(getTableAttributes().get("rows").getValue().toString());
        int page = Integer.parseInt(getAttributes().get("page").getValue().toString());
        for (int i = 1; i <= Math.ceil(size / rows); i++) {
            if (Math.abs(i - page) < 6) {
                SelectItem item = new SelectItem(i);
                list.add(item);
            }
        }
        return list;
    }

    public int getSize() {
        return size;
    }

    /**
     * Used for customized page facet (customizedFacets.xhtml)
     * @return max number in spinner which is number of pages in table
     */
    public int getSpinnerMaxNumber() {
        double rows = Integer.parseInt(getTableAttributes().get("rows").getValue().toString());
        return (int) Math.ceil(size / rows);
    }

    public Attributes getTableAttributes() {
        return tableAttributes;
    }

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIDataScroller.class, getClass());

        attributes.setAttribute("boundaryControls", "show");
        attributes.setAttribute("fastControls", "show");
        attributes.setAttribute("stepControls", "show");
        attributes.setAttribute("fastStep", 1);
        attributes.setAttribute("lastPageMode", "short");
        attributes.setAttribute("maxPages", 10);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("render", "richDataTable");
        attributes.setAttribute("page", 1);
        attributes.setAttribute("renderIfSinglePage", true);
        attributes.setAttribute("for", "richDataTable");

        tableAttributes = Attributes.getEmptyAttributes(getClass());
        tableAttributes.setAttribute("rows", 9);

        facets = new HashMap<String, String>();
        facets.put("first", "<<<");
        facets.put("fastRewind", "<<");
        facets.put("previous", "<");
        facets.put("next", ">");
        facets.put("fastForward", ">>");
        facets.put("last", ">>>");

        facets.put("first_disabled", "<<<d");
        facets.put("fastRewind_disabled", "<<d");
        facets.put("previous_disabled", "<d");
        facets.put("next_disabled", ">d");
        facets.put("fastForward_disabled", ">>d");
        facets.put("last_disabled", ">>>d");
    }

    /**
     * Getter for state.
     *
     * @return true if non-empty data model should be used in table
     */
    public boolean isState() {
        return state;
    }

    public void scrollListener(DataScrollEvent event) {
        logToPage("* scroll event: " + event.getOldScrolVal() + " -> " + event.getNewScrolVal());
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Setter for state.
     *
     * @param state true if non-empty data model should be used in table
     */
    public void setState(boolean state) {
        this.state = state;
    }

    public void setTableAttributes(Attributes tableAttributes) {
        this.tableAttributes = tableAttributes;
    }
}
