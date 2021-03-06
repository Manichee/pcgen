/*
 * Weapon.java
 * Copyright 2002 (C) Greg Bingleman <byngl@hotmail.com>
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
 *
 * Created on December 13, 2002, 9:19 AM
 *
 * Current Ver: $Revision: 1.14 $
 * Last Editor: $Author: binkley $
 * Last Edited: $Date: 2005/11/13 20:01:22 $
 *
 */
package pcgen.core.bonus;


/**
 * <code>Weapon</code>
 *
 * @author  Greg Bingleman <byngl@hotmail.com>
 */
final class Weapon extends MultiTagBonusObj
{
	private static final String[] bonusHandled = new String[]
		{
			"WEAPON"
		};
	private static final String[] bonusTags = new String[]
		{
			"ATTACKS",
			"ATTACKSPROGRESS",
			"WEAPONBAB",
			"DAMAGE",
			"DAMAGESIZE",
			"TOHIT",
			"WIELDCATEGORY"
		};

	String[] getBonusesHandled()
	{
		return bonusHandled;
	}

	String getBonusTag(final int tagNumber)
	{
		return bonusTags[tagNumber];
	}

	int getBonusTagLength()
	{
		return bonusTags.length;
	}
}
