/*
 * Feat.java
 * Copyright 2004 (C) Devon Jones <soulcatcher@eviloft.org>
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
 * Current Ver: $Revision: 1.6 $
 * Last Editor: $Author: binkley $
 * Last Edited: $Date: 2005/11/13 20:01:22 $
 *
 */
package pcgen.core.bonus;


/**
 * <code>Feat</code>
 *
 * @author  Devon Jones <soulcatcher@eviloft.org>
 * @author  Greg Bingleman <byngl@hotmail.com>
 */
final class Feat extends BonusObj
{
	private static final String[] bonusHandled =
		{
			"FEAT"
		};

	boolean parseToken(final String token)
	{
		if ("POOL".equals(token))
		{
			addBonusInfo(token);
			return true;
		}

		return false;
	}

	String unparseToken(final Object obj)
	{
		return (String) obj;
	}

	String[] getBonusesHandled()
	{
		return bonusHandled;
	}
}
