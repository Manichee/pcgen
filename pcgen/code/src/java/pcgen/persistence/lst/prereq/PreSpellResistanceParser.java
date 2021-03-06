/*
 * PreSpellResistanceParser.java
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.       See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on 18-Dec-2003
 *
 * Current Ver: $Revision: 1.5 $
 *
 * Last Editor: $Author: frugal $
 *
 * Last Edited: $Date: 2004/01/12 13:44:01 $
 *
 */
package pcgen.persistence.lst.prereq;

import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteException;
import pcgen.persistence.PersistenceLayerException;

/**
 * @author wardc
 *
 */
public class PreSpellResistanceParser extends AbstractPrerequisiteIntegerParser implements PrerequisiteParserInterface
{
	public String[] kindsHandled()
	{
		return new String[]{ "SR", "SREQ", "SRGT", "SRGTEQ", "SRLT", "SRLTEQ", "SRNEQ" };
	}

	public Prerequisite parse(String kind, String formula, boolean invertResult, boolean overrideQualify)
		throws PersistenceLayerException
	{
		Prerequisite prereq = super.parse(kind, formula, invertResult, overrideQualify);
		try
		{
			prereq.setKind("sr");
	
			// Get the comparator type SRGTEQ, SR, SRNEQ etc.
			String compType = kind.substring(2).toLowerCase();
	
			if (compType.length() == 0)
			{
				compType = "gteq";
			}
	
			prereq.setOperator(compType);
			if (invertResult) {
				prereq.setOperator( prereq.getOperator().invert());
			}
		}
		catch (PrerequisiteException pe)
		{
			throw new PersistenceLayerException("Unable to parse the prerequisite :'"+kind+":"+formula+"'. "+pe.getLocalizedMessage());
		}
		
		return prereq;
	}
}
