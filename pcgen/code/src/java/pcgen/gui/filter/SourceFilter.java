/*
 * SourceFilter.java
 * Copyright 2002 (C) Thomas Behr <ravenlock@gmx.de>
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
 * Created on February 9, 2002, 2:30 PM
 */
package pcgen.gui.filter;

import pcgen.core.Constants;
import pcgen.core.PObject;
import pcgen.core.PlayerCharacter;
import pcgen.core.SourceUtilities;
import pcgen.util.PropertyFactory;

import java.util.StringTokenizer;

/**
 * <code>SourceFilter</code>
 *
 * @author Thomas Behr
 * @version $Revision: 1.31 $
 */
final class SourceFilter extends AbstractPObjectFilter
{
	public static final int HIGH = 0;
	public static final int LOW = 1;
	private String source;
	private int detailLevel;

	SourceFilter(String src, int argDetailLevel)
	{
		super();
		this.detailLevel = argDetailLevel;
		this.source = (this.detailLevel == LOW) ? normalizeSource(src) : src;

		int cInt = source.indexOf(":");
		int pInt = source.indexOf("|");

		if (source.startsWith("SOURCE") && (cInt > -1) && (pInt > cInt))
		{
			source = source.substring(cInt + 1, pInt);
		}

		setCategory(PropertyFactory.getString("in_sourceLabel"));
		setName(source);
		setDescription(PropertyFactory.getString("in_filterAccObj") + " " + getName() + ".");
	}

	public boolean accept(PlayerCharacter aPC, PObject pObject)
	{
		if (pObject == null)
		{
			return false;
		}

		if (detailLevel == LOW)
		{
			return normalizeSource(SourceUtilities.returnSourceInForm(pObject, Constants.SOURCELONG, true)).equals(source);
		}

		return SourceUtilities.returnSourceInForm(pObject, Constants.SOURCELONG, true).equals(source);
	}

	private static String normalizeSource(String s)
	{
		String work = s;

		if (work.indexOf(",") > -1)
		{
			work = new StringTokenizer(s, ",").nextToken();
		}

		return work.trim();
	}
}
