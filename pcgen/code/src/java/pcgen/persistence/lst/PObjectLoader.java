/*
 * PObjectLoader.java
 * Copyright 2001 (C) Bryan McRoberts <merton_monk@yahoo.com>
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
 * Created on February 22, 2002, 10:29 PM
 *
 * Current Ver: $Revision: 1.119 $
 * Last Editor: $Author: soulcatcher $
 * Last Edited: $Date: 2006/02/14 21:00:18 $
 *
 */
package pcgen.persistence.lst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import pcgen.core.Campaign;
import pcgen.core.PObject;
import pcgen.core.utils.CoreUtility;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.lst.prereq.PreParserFactory;
import pcgen.persistence.lst.utils.FeatParser;
import pcgen.persistence.lst.utils.PObjectHelper;

/**
 * @author David Rice <david-pcgen@jcuz.com>
 * @version $Revision: 1.119 $
 */
public final class PObjectLoader {

	/**
	 * Creates a new instance of PObjectLoader Private since instances need never
	 * be created and API methods are public and static
	 */

	private static List featList = new ArrayList();

	private PObjectLoader() {
		// Empty Constructor
	}

	/**
	 * This method parses a Tag and its value from an LST formatted file (or other
	 * source, such as an LST editor) It applies the value of the tag to the
	 * provided PObject.
	 *
	 * @param obj
	 *          PObject which the tag will be applied to
	 * @param aTag
	 *          String tag and value to parse
	 * @return boolean true if the tag is parsed; else false.
	 * @throws PersistenceLayerException
	 */
	public static boolean parseTag(PObject obj, String aTag) throws PersistenceLayerException {
		return parseTagLevel(obj, aTag, -9);
	}

	/**
	 * This method parses a Tag and its value from an LST formatted file (or other
	 * source, such as an LST editor) It applies the value of the tag to the
	 * provided PObject If a level is given, the tag value is applied to the
	 * object at the specified level [as appropriate for the tag] A level of -9 or
	 * lower is treated as "at all levels."
	 *
	 * @param obj
	 *          PObject which the tag will be applied to
	 * @param aTag
	 *          String tag and value to parse
	 * @param anInt
	 *          int character level at which the tag becomes effective
	 * @return boolean true if the tag is parsed; else false.
	 * @throws PersistenceLayerException
	 */
	public static boolean parseTagLevel(PObject obj, String aTag, int anInt) throws PersistenceLayerException
	{
		if ((obj == null) || (aTag.length() < 1))
		{
			return false;
		}

		obj.setNewItem(false);

		aTag.charAt(0);

		boolean result = false;
		int colonIdx = aTag.indexOf(':');
		if (colonIdx < 0)
		{
			return false;
		}
		String key = aTag.substring(0, colonIdx);
		String value = aTag.substring(colonIdx + 1);
		Map tokenMap = TokenStore.inst().getTokenMap(GlobalLstToken.class);
		LstToken token = (LstToken) tokenMap.get(key);
		if (token != null)
		{
			LstUtils.deprecationCheck(token, obj, value);
			result = ((GlobalLstToken)token).parse(obj, value, anInt);
		}
		else
		{
			result = true;
			if (aTag.startsWith("CAMPAIGN:") && !(obj instanceof Campaign))
			{
				// blank intentionally
			}
			else if (aTag.startsWith("PRE") || aTag.startsWith("!PRE") || aTag.startsWith("RESTRICT:"))
			{
				if (aTag.toUpperCase().equals("PRE:.CLEAR"))
				{
					obj.clearPreReq();
				}
				else
				{
					aTag = CoreUtility.replaceAll(aTag, "<this>", obj.getName());
					try
					{
						PreParserFactory factory = PreParserFactory.getInstance();
						obj.addPreReq(factory.parse(aTag), anInt);
					}
					catch (PersistenceLayerException ple)
					{
						throw new PersistenceLayerException("Unable to parse a prerequisite: " + ple.getMessage());
					}
				}
			}
			else if (aTag.startsWith("SOURCE"))
			{
				obj.setSourceMap(parseSource(aTag));
			}
			else if (aTag.startsWith("VFEAT:"))
			{
					featList.add(new PObjectHelper(obj, aTag.substring(6), anInt));
			}
			else
			{
				result = false;
			}
		}

		return result;
	}

	public static void finishFeatProcessing()
	{
		for (Iterator i = featList.iterator(); i.hasNext();)
		{
			PObjectHelper p = (PObjectHelper) i.next();
			List vFeatList = FeatParser.parseVirtualFeatList(p.getTag());
			p.getObject().addVirtualFeats(vFeatList);
		}
	}

	/**
	 * This method parses a line in an LST file containing the source information
	 * into the map form used by a PObject.
	 *
	 * @param value String LST formatted source information line
	 * @return Map of source forms
	 */
	public static Map parseSource(String value)
	{
		Map sourceMap = new HashMap();
		StringTokenizer aTok = new StringTokenizer(value, "|");

		while (aTok.hasMoreTokens())
		{
			String arg = aTok.nextToken();
			String key = arg.substring(6, arg.indexOf(':'));
			String val = arg.substring(arg.indexOf(':') + 1);
			sourceMap.put(key, val);
		}

		return sourceMap;
	}
}