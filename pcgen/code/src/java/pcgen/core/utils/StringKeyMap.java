/*
 * Copyright 2005 (C) Tom Parker <thpr@sourceforge.net>
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
 * Created on June 18, 2005.
 *
 * Current Ver: $Revision: 1.3 $
 * Last Editor: $Author: thpr $
 * Last Edited: $Date: 2005/08/26 01:18:02 $
 */
package pcgen.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tom Parker <thpr@sourceforge.net>
 * 
 * This encapsulates a Map in a typesafe way (prior to java 1.5 having the
 * ability to do that with typed collections)
 */
public class StringKeyMap
{

	private final Map map = new HashMap();

	public StringKeyMap()
	{
		// Do Nothing
	}

	public String getCharacteristic(StringKey key)
	{
		return (String) map.get(key);
	}

	public void setCharacteristic(StringKey key, String value)
	{
		map.put(key, value);
	}

	public boolean hasCharacteristic(StringKey key)
	{
		return map.containsKey(key);
	}

	public void addAllCharacteristics(StringKeyMap scs)
	{
		map.putAll(scs.map);
	}
	
	public String removeCharacteristic(StringKey key) 
	{
		return (String) map.remove(key);
	}
}
