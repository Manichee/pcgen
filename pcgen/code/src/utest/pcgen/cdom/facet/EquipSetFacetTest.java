/*
 * Copyright (c) 2010 Tom Parker <thpr@users.sourceforge.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package pcgen.cdom.facet;

import pcgen.cdom.facet.base.AbstractListFacet;
import pcgen.cdom.testsupport.AbstractListFacetTest;
import pcgen.core.character.EquipSet;

public class EquipSetFacetTest extends AbstractListFacetTest<EquipSet>
{
	private EquipSetFacet facet = new EquipSetFacet();

	@Override
	protected AbstractListFacet<EquipSet> getFacet()
	{
		return facet;
	}

	private int n = 1;

	@Override
	protected EquipSet getObject()
	{
		return new EquipSet("0." + n++, "Start");
	}
}