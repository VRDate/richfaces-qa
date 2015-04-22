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
package org.richfaces.tests.metamer.bean.issues;

import java.io.Serializable;
import java.text.MessageFormat;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "rf13928")
@SessionScoped
public class RF13928 implements Serializable {

    public static final String DATA_WAITING = "waiting for data";
    public static final String SUBSCRIPTION_WAITING = "waiting for subscription";
    private static final long serialVersionUID = 1L;

    private String data;
    private boolean enabled;
    private String subscription;
    private int subscriptions = 0;
    private int updates = 0;

    public String getData() {
        return data;
    }

    public String getSubscription() {
        return subscription;
    }

    @PostConstruct
    public void init() {
        data = DATA_WAITING;
        subscription = SUBSCRIPTION_WAITING;
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public void subscribe() {
        subscription = MessageFormat.format("subscribed #{0} times", ++subscriptions);
    }

    public void update() {
        data = MessageFormat.format("received data #{0}", ++updates);
    }
}
