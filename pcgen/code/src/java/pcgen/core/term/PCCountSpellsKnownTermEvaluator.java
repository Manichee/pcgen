/**
 * pcgen.core.term.PCCountSpellsKnownTermEvaluator.java
 * Copyright (c) 2008 Andrew Wilson <nuance@users.sourceforge.net>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created 07-Aug-2008 22:41:16
 *
 * Current Ver: $Revision:$
 * Last Editor: $Author:$
 * Last Edited: $Date:$
 *
 */

package pcgen.core.term;

import pcgen.cdom.enumeration.AssociationListKey;
import pcgen.core.PlayerCharacter;
import pcgen.core.SettingsHandler;
import pcgen.core.PObject;
import pcgen.core.Globals;

public class PCCountSpellsKnownTermEvaluator
		extends BasePCTermEvaluator implements TermEvaluator
{
	private final int[] nums;

	public PCCountSpellsKnownTermEvaluator(
			String originalText, int[] nums)
	{
		this.originalText = originalText;
		this.nums         = nums;
	}

	public Float resolve(PlayerCharacter pc)
	{
		Float count = 0f;

		if (SettingsHandler.getPrintSpellsWithPC())
		{
			if (nums[0] == -1)
			{
				for (PObject pcClass : pc.getClassList())
				{
					count += pc.getAssocCount(pcClass, AssociationListKey.CHARACTER_SPELLS);
				}
			}
			else
			{
				final PObject pObj = pc.getSpellClassAtIndex(nums[0]);

				if (pObj != null)
				{
					count = (float) pc.getCharacterSpells(
									pObj,
									null, Globals.getDefaultSpellBook(), nums[1]).size();
				}
			}
		}

		return count;
	}

	public boolean isSourceDependant()
	{
		return false;
	}

	public boolean isStatic()
	{
		return false;
	}
}
