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
package org.xwiki.gwt.dom.client.internal;

import org.xwiki.gwt.dom.client.Document;
import org.xwiki.gwt.dom.client.Selection;
import org.xwiki.gwt.dom.client.SelectionManager;
import org.xwiki.gwt.dom.client.internal.mozilla.NativeSelection;

/**
 * The default {@link SelectionManager} implementation. Retrieves the selection object using Mozilla's API.
 * 
 * @version $Id$
 */
public class DefaultSelectionManager implements SelectionManager
{
    /**
     * {@inheritDoc}
     * 
     * @see SelectionManager#getSelection(Document)
     */
    public Selection getSelection(Document document)
    {
        return new DefaultSelection(NativeSelection.getInstance(document));
    }
}
