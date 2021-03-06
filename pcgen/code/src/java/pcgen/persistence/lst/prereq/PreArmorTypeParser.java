/*
 * PreArmourTypeParser.java
 *
 * Copyright 2003 (C) Chris Ward <frugal@purplewombat.co.uk>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	   See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on 17-Dec-2003
 *
 * Current Ver: $Revision: 1.1 $
 * 
 * Last Editor: $Author: frugal $
 * 
 * Last Edited: $Date: 2004/01/09 21:31:38 $
 *
 */
package pcgen.persistence.lst.prereq;


/**
 * @author wardc
 *
 */
public class PreArmorTypeParser
	extends AbstractPrerequisiteListParser
	implements PrerequisiteParserInterface {



	/* (non-Javadoc)
	 * @see pcgen.persistence.lst.prereq.PrereqParserInterface#kindsHandled()
	 */
	public String[] kindsHandled() {
		return new String[]{"ARMORTYPE"};
	}
}
