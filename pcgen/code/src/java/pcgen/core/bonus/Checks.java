/*
 * Checks.java
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
 * Current Ver: $Revision: 1.16 $
 * Last Editor: $Author: binkley $
 * Last Edited: $Date: 2005/11/13 20:01:22 $
 *
 */
package pcgen.core.bonus;

import pcgen.core.PObject;
import pcgen.core.SettingsHandler;

import java.util.Iterator;

/**
 * <code>Checks</code>
 *
 * @author  Greg Bingleman <byngl@hotmail.com>
 */
final class Checks extends BonusObj
{
	private static final String[] bonusHandled =
		{
			"CHECKS"
		};

	boolean parseToken(final String argToken)
	{
		boolean isBase = false;
		final String token;

		if (argToken.startsWith("BASE."))
		{
			token = argToken.substring(5);
			isBase = true;
		}
		else
		{
			token = argToken;
		}

		PObject aCheck = SettingsHandler.getGame().getCheckNamed(token);

		if (aCheck != null)
		{
			addBonusInfo(new CheckInfo(aCheck, isBase));

			return true;
		}
		else if ("ALL".equals(token))
		{
			// Special case of:  BONUS:CHECKS|ALL|x
			for (Iterator ac = SettingsHandler.getGame().getUnmodifiableCheckList().iterator(); ac.hasNext();)
			{
				aCheck = (PObject) ac.next();
				addBonusInfo(new CheckInfo(aCheck, isBase));
			}

			return true;
		}

		return false;
	}

	String unparseToken(final Object obj)
	{
		String token = "";

		if (((CheckInfo) obj).isBase)
		{
			token = "BASE.";
		}

		return token + ((CheckInfo) obj).pobj.getName();
	}

	private static class CheckInfo
	{
		final PObject pobj;
		final boolean isBase;

		CheckInfo(final PObject argPobj, final boolean argIsBase)
		{
			pobj = argPobj;
			isBase = argIsBase;
		}
	}

	String[] getBonusesHandled()
	{
		return bonusHandled;
	}
}
