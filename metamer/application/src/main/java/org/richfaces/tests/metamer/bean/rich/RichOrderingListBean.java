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
package org.richfaces.tests.metamer.bean.rich;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.richfaces.component.UIOrderingList;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.model.Capital;
import org.richfaces.tests.metamer.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * Simple bean for rich:orderingList component example.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@ManagedBean(name = "richOrderingListBean")
@SessionScoped
public class RichOrderingListBean implements Serializable {

    private static final String CLASS_DASH = "class-";
    private static final String COLLECTION_TYPE = "collectionType";
    private static final String DEFAULT_COLLECTION = "LinkedList";
    private static final String INVALID_DASH = "invalid-";
    private static final String JAVA_UTIL_STRING = "java.util.";
    private static final Logger LOGGER = LoggerFactory.getLogger(RichOrderingListBean.class);
    private static final String STRING_DASH = "string-";
    private static final long serialVersionUID = 5868941019675985273L;

    private Attributes attributes;
    @ManagedProperty("#{model.capitals}")
    private List<Capital> capitals;
    @ManagedProperty("#{model.employees}")
    private List<Employee> employees;
    private List<Capital> emptyCapitals = Lists.newArrayList();
    private Collection<String> hiddenAttributes = new ArrayList<String>();
    private String validatorMessage;

    public static Object extractCollectionType(Attributes attributes) {
        String collectionString = attributes.get(COLLECTION_TYPE).getValue().toString();
        if (collectionString.startsWith(STRING_DASH)) {// starts with 'string-' >>> return String
            return collectionString.replace(STRING_DASH, JAVA_UTIL_STRING);
        } else if (collectionString.startsWith(CLASS_DASH)) {// starts with 'class-' >>> return Class
            try {
                return Class.forName(collectionString.replace(CLASS_DASH, JAVA_UTIL_STRING));
            } catch (ClassNotFoundException e) {
                LOGGER.error(e + "\n Setting collectionType back to " + DEFAULT_COLLECTION + '.');
                attributes.setAttribute(COLLECTION_TYPE, STRING_DASH + DEFAULT_COLLECTION);
                return extractCollectionType(attributes);
            }
        } else if (collectionString.startsWith(INVALID_DASH)) {// starts with 'invalid-' >>> return Class
            attributes.setAttribute(COLLECTION_TYPE, collectionString.replace("invalid", "class"));
            return extractCollectionType(attributes);
        } else {// starts with none of 'string-', 'class-', 'invalid-' >>> return String
            attributes.setAttribute(COLLECTION_TYPE, STRING_DASH + DEFAULT_COLLECTION);
            return extractCollectionType(attributes);
        }
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public List<Capital> getCapitals() {
        return capitals;
    }

    public Object getCollectionType() {
        return extractCollectionType(attributes);
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Capital> getEmptyCapitals() {
        return emptyCapitals;
    }

    public Collection<String> getHiddenAttributes() {
        return hiddenAttributes;
    }

    public String getValidatorMessage() {
        return validatorMessage;
    }

    @PostConstruct
    public void init() {
        LOGGER.debug("initializing bean " + getClass().getName());
        attributes = Attributes.getComponentAttributesFromFacesConfig(UIOrderingList.class, getClass());

        attributes.setAttribute("downText", "Down");
        attributes.setAttribute("downBottomText", "Bottom");
        attributes.setAttribute("listWidth", 300);
        attributes.setAttribute("listHeight", 500);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("upText", "Up");
        attributes.setAttribute("upTopText", "Top");
        attributes.setAttribute("required", false);
        attributes.setAttribute(COLLECTION_TYPE, STRING_DASH + DEFAULT_COLLECTION);
        attributes.setAttribute("valueChangeListener", "valueChangeListenerImproved");

        employees = employees.subList(0, 10);

        // TODO has to be tested in another way
        String[] attrsToHide = new String[] { "itemLabel", "itemValue", "value", "var",
            "converter", "converterMessage", "validator", "validatorMessage" };
        for (String attribute : attrsToHide) {
            hiddenAttributes.add(attribute);
            attributes.remove(attribute);
        }
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public void setCapitals(List<Capital> capitals) {
        this.capitals = capitals;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void setEmptyCapitals(List<Capital> emptyCapitals) {
        this.emptyCapitals = emptyCapitals;
    }

    public void setHiddenAttributes(Collection<String> hiddenAttributes) {
        this.hiddenAttributes = hiddenAttributes;
    }

    public void setValidatorMessage(String validatorMessage) {
        this.validatorMessage = validatorMessage;
    }

}
