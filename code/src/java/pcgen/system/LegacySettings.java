/*
 * LegacySettings.java
 * Copyright 2010(C) James Dempsey <jdempsey@users.sourceforge.net>
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
 * Created on 19/11/2010, 20:19:00
 *
 * $Id: LegacySettings.java 13875 2010-11-19 22:05:26Z jdempsey $
 */
package pcgen.system;

import pcgen.core.SettingsHandler;

/**
 * The Class <code>LegacySettings</code> stores the settings managed by the 
 * original SettingsHandler class. It is expected that most settings will be 
 * migrated away to other PropertyContexts as part of the CDOM UI project. 
 *
 * <br/>
 * Last Editor: $Author: jdempsey $
 * Last Edited: $Date: 2010-11-19 14:05:26 -0800 (Fri, 19 Nov 2010) $
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision: 13875 $
 */
public class LegacySettings extends PropertyContext
{
	/** Our singleton instance */
	private static final LegacySettings instance = new LegacySettings();


	/**
	 * Create a new LegacySettings instance. Private to avoid multiples.
	 */
	private LegacySettings()
	{
		super("legacy.ini", null, SettingsHandler.getOptions());
	}

	/**
	 * @return The singleton LegacySettings instance.
	 */
	public static LegacySettings getInstance()
	{
		return instance;
	}

	/* (non-Javadoc)
	 * @see pcgen.system.PropertyContext#afterPropertiesLoaded()
	 */
	@Override
	protected void afterPropertiesLoaded()
	{
		SettingsHandler.getOptionsFromProperties(null);

		super.afterPropertiesLoaded();
	}

	/* (non-Javadoc)
	 * @see pcgen.system.PropertyContext#beforePropertiesSaved()
	 */
	@Override
	protected void beforePropertiesSaved()
	{
		SettingsHandler.setOptionsProperties(null);
		super.beforePropertiesSaved();
	}

}
