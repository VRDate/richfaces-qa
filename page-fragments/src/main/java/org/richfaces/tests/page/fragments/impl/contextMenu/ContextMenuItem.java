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
package org.richfaces.tests.page.fragments.impl.contextMenu;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ContextMenuItem {

    private final String itemText;

    public ContextMenuItem(String itemText) {
        if (itemText == null || itemText.length() == 0) {
            throw new IllegalArgumentException("Context menu item text can not be null or empty!");
        }
        this.itemText = itemText;
    }

    public String getText() {
        return itemText;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((itemText == null) ? 0 : itemText.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ContextMenuItem other = (ContextMenuItem) obj;
        if (itemText == null) {
            if (other.itemText != null) {
                return false;
            }
        } else if (!itemText.equals(other.itemText)) {
            return false;
        }
        return true;
    }

}
