/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
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
package org.xwiki.gwt.wysiwyg.client.plugin.font;

import com.google.gwt.event.dom.client.HasChangeHandlers;

/**
 * Generic interface for picking a {@link String} value.
 * 
 * @version $Id$
 */
public interface Picker extends HasChangeHandlers
{
    /**
     * @return the selected value, or {@code null} is no value has been selected
     */
    String getSelectedValue();

    /**
     * Selects the given value.
     * 
     * @param value the value to be selected
     */
    void setSelectedValue(String value);
}
