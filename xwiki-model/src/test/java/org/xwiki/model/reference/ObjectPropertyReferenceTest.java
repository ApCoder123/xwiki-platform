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
package org.xwiki.model.reference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.xwiki.model.EntityType;

/**
 * Unit test for the Property reference ({@link ObjectPropertyReference}).
 * 
 * @version $Id$
 * @since 2.2M2
 */
public class ObjectPropertyReferenceTest
{
    /**
     * Test equivalence of constructors.
     */
    @Test
    public void testConstructors()
    {
        ObjectPropertyReference reference =
            new ObjectPropertyReference(new EntityReference("property", EntityType.OBJECT_PROPERTY,
                new EntityReference("Object", EntityType.OBJECT, new EntityReference("Page", EntityType.DOCUMENT,
                    new EntityReference("Space", EntityType.SPACE, new EntityReference("wiki", EntityType.WIKI))))));
        assertEquals(reference, new ObjectPropertyReference("wiki", "Space", "Page", "Object", "property"));
        assertEquals(reference, new ObjectPropertyReference("property", new ObjectReference("wiki", "Space", "Page",
            "Object")));
    }

    @Test
    public void testInvalidType()
    {
        try {
            ObjectPropertyReference reference =
                new ObjectPropertyReference(new EntityReference("space.page", EntityType.DOCUMENT));
            fail("Should have thrown exception");
        } catch (IllegalArgumentException expected) {
            assertEquals("Invalid type [DOCUMENT] for an object property reference", expected.getMessage());
        }
    }

    @Test
    public void testInvalidNullParent()
    {
        try {
            ObjectPropertyReference reference =
                new ObjectPropertyReference(new EntityReference("property", EntityType.OBJECT_PROPERTY, null));
            fail("Should have thrown exception");
        } catch (IllegalArgumentException expected) {
            assertEquals("Invalid parent reference [null] for an object property reference", expected.getMessage());
        }
    }

    /**
     * Tests that an object reference throws exception if it doesn't have a document as a parent.
     */
    @Test
    public void testInvalidParentType()
    {
        try {
            ObjectPropertyReference reference =
                new ObjectPropertyReference(new EntityReference("property", EntityType.OBJECT_PROPERTY,
                    new EntityReference("Space", EntityType.SPACE)));
            fail("Should have thrown exception");
        } catch (IllegalArgumentException expected) {
            assertEquals("Invalid parent reference [name = [Space], type = [SPACE], parent = [null]] for an object "
                + "property reference", expected.getMessage());
        }
    }
}
