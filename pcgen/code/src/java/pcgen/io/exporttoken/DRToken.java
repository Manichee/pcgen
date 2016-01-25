/*
 * DRToken.java
 * Copyright 2003 (C) Devon Jones <soulcatcher@evilsoft.org>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.     See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on December 15, 2003, 12:21 PM
 *
 * Current Ver: $Revision: 1.8 $
 * Last Editor: $Author: karianna $
 * Last Edited: $Date: 2005/11/16 16:03:20 $
 *
 */
package pcgen.io.exporttoken;

import pcgen.core.PlayerCharacter;
import pcgen.io.ExportHandler;

/**
 * Deals with DR token
 */
public class DRToken extends Token
{
	/** Token Name */
	public static final String TOKENNAME = "DR";

	/**
	 * @see pcgen.io.exporttoken.Token#getTokenName()
	 */
	public String getTokenName()
	{
		return TOKENNAME;
	}

	/**
	 * @see pcgen.io.exporttoken.Token#getToken(java.lang.String, pcgen.core.PlayerCharacter, pcgen.io.ExportHandler)
	 */
	public String getToken(String tokenSource, PlayerCharacter pc, ExportHandler eh)
	{
		return getDRToken(pc);
	}

	/**
	 * Get the DR Token
	 * @param pc
	 * @return DR Token
	 */
	public static String getDRToken(PlayerCharacter pc)
	{
		return pc.calcDR();
	}
}