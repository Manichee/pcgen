/*
 * SpellKnown.java
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

import pcgen.core.bonus.util.SpellCastInfo;

/**
 * <code>SpellKnown</code>
 *
 * @author  Greg Bingleman <byngl@hotmail.com>
 */
final class SpellKnown extends BonusObj
{
	private static final String[] bonusHandled =
		{
			"SPELLKNOWN"
		};

	/**
	 * CLASS=<classname OR Any>;LEVEL=<level>
	 * TYPE=<type>;LEVEL=<level>
	 * @param token
	 * @return TRUE or FALSE
	 */
	boolean parseToken(final String token)
	{
		int idx = token.indexOf(";LEVEL=");

		if (idx < 0)
		{
			idx = token.indexOf(";LEVEL.");
		}

		if (idx < 0)
		{
			return false;
		}

		final String level = token.substring(idx + 7);

		addBonusInfo(new SpellCastInfo(token.substring(0, idx), level));

		return true;
	}

	String unparseToken(final Object obj)
	{
		final StringBuffer sb = new StringBuffer(30);
		final SpellCastInfo sci = (SpellCastInfo)obj;

		if (sci.getType() != null)
		{
			sb.append("TYPE.").append(((SpellCastInfo) obj).getType());
		}
		else if (sci.getPcClassName() != null)
		{
			sb.append("CLASS.").append(((SpellCastInfo) obj).getPcClassName());
		}

		sb.append(";LEVEL.").append(((SpellCastInfo) obj).getLevel());

		return sb.toString();
	}

	String[] getBonusesHandled()
	{
		return bonusHandled;
	}
}
