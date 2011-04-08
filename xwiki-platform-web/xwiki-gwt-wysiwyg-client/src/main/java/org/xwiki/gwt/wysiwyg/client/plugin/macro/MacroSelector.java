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
package org.xwiki.gwt.wysiwyg.client.plugin.macro;

import java.util.ArrayList;
import java.util.List;

import org.xwiki.gwt.dom.client.Element;
import org.xwiki.gwt.dom.client.Range;
import org.xwiki.gwt.dom.client.Selection;
import org.xwiki.gwt.user.client.DeferredUpdater;
import org.xwiki.gwt.user.client.HandlerRegistrationCollection;
import org.xwiki.gwt.user.client.Updatable;
import org.xwiki.gwt.user.client.ui.rta.cmd.Command;
import org.xwiki.gwt.user.client.ui.rta.cmd.CommandListener;
import org.xwiki.gwt.user.client.ui.rta.cmd.CommandManager;

import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;

/**
 * Controls the currently selected macros.
 * 
 * @version $Id$
 */
public class MacroSelector implements Updatable, MouseDownHandler, KeyUpHandler, CommandListener
{
    /**
     * The command used to notify all the rich text area listeners when its content has been reset.
     */
    private static final Command RESET = new Command("reset");

    /**
     * The displayer used to select macros.
     */
    private final MacroDisplayer displayer;

    /**
     * Schedules updates and executes only the most recent one.
     */
    private final DeferredUpdater updater = new DeferredUpdater(this);

    /**
     * The list of currently selected macro containers.
     */
    private final List<Element> selectedContainers = new ArrayList<Element>();

    /**
     * The list of handler registrations that have to be removed when this object is destroyed.
     */
    private final HandlerRegistrationCollection registrations = new HandlerRegistrationCollection();

    /**
     * Creates a new macro selector.
     * 
     * @param displayer the displayer to be used for selecting the macros
     */
    public MacroSelector(MacroDisplayer displayer)
    {
        this.displayer = displayer;

        // Listen to events generated by the rich text area in order to keep track of the select macros.
        registrations.add(displayer.getTextArea().addMouseDownHandler(this));
        registrations.add(displayer.getTextArea().addKeyUpHandler(this));
        displayer.getTextArea().getCommandManager().addCommandListener(this);
    }

    /**
     * Destroys this selector.
     */
    public void destroy()
    {
        selectedContainers.clear();
        registrations.removeHandlers();
        displayer.getTextArea().getCommandManager().removeCommandListener(this);
    }

    /**
     * {@inheritDoc}
     * 
     * @see MouseDownHandler#onMouseDown(MouseDownEvent)
     */
    public void onMouseDown(MouseDownEvent event)
    {
        if (event.getSource() == displayer.getTextArea()) {
            // See if the target is a macro.
            Element target = (Element) event.getNativeEvent().getEventTarget().cast();
            if (displayer.isMacroContainer(target)) {
                // See if the macro is already selected.
                if (displayer.isSelected(target)) {
                    // If already selected then toggle the collapsed state.
                    displayer.setCollapsed(target, !displayer.isCollapsed(target));
                } else {
                    // Select the macro immediately.
                    update();
                }
            } else {
                // Otherwise just schedule an update for the list of selected macros.
                updater.deferUpdate();
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see KeyUpHandler#onKeyUp(KeyUpEvent)
     */
    public void onKeyUp(KeyUpEvent event)
    {
        if (event.getSource() == displayer.getTextArea()) {
            updater.deferUpdate();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see CommandListener#onBeforeCommand(CommandManager, Command, String)
     */
    public boolean onBeforeCommand(CommandManager sender, Command command, String param)
    {
        if (RESET.equals(command)) {
            // Clear the list of selected macro containers each time the content is reset to release the referenced DOM
            // nodes. Accessing these nodes after the rich text area has been reloaded can lead to "Access Denied"
            // JavaScript exceptions in IE.
            selectedContainers.clear();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see CommandListener#onCommand(CommandManager, Command, String)
     */
    public void onCommand(CommandManager sender, Command command, String param)
    {
        if (sender == displayer.getTextArea().getCommandManager()) {
            updater.deferUpdate();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see Updatable#update()
     */
    public void update()
    {
        // Clear previously selected macros.
        for (Element container : selectedContainers) {
            displayer.setSelected(container, false);
        }
        selectedContainers.clear();

        // Mark currently selected macros.
        Selection selection = displayer.getTextArea().getDocument().getSelection();
        for (int i = 0; i < selection.getRangeCount(); i++) {
            Range range = selection.getRangeAt(i);
            if (range.getStartContainer() == range.getEndContainer()
                && range.getStartContainer().getNodeType() == Node.ELEMENT_NODE
                && range.getEndOffset() - range.getStartOffset() == 1) {
                Node selectedNode = range.getStartContainer().getChildNodes().getItem(range.getStartOffset());
                if (displayer.isMacroContainer(selectedNode)) {
                    Element container = (Element) selectedNode;
                    selectedContainers.add(container);
                    displayer.setSelected(container, true);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see Updatable#canUpdate()
     */
    public boolean canUpdate()
    {
        return displayer.getTextArea().isAttached() && displayer.getTextArea().isEnabled();
    }

    /**
     * @return the number of macros currently selected
     */
    public int getMacroCount()
    {
        return selectedContainers.size();
    }

    /**
     * @param index the index of the selected macro to return
     * @return the selected macro at the specified index
     */
    public Element getMacro(int index)
    {
        return selectedContainers.get(index);
    }

    /**
     * @return the displayer used to select and detect macros
     */
    public MacroDisplayer getDisplayer()
    {
        return displayer;
    }
}
