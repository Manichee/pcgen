/*
 * PreClassLevelMaxParser.java
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
 * Current Ver: $Revision: 1.16 $
 *
 * Last Editor: $Author: byngl $
 *
 * Last Edited: $Date: 2005/11/09 13:48:47 $
 *
 */
package pcgen.persistence.lst.prereq;

import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteOperator;
import pcgen.persistence.PersistenceLayerException;

import java.util.Iterator;


/**
 * @author wardc
 *
 */
public class PreClassLevelMaxParser extends AbstractPrerequisiteListParser implements PrerequisiteParserInterface
{
	public String[] kindsHandled()
	{
		return new String[]{ "CLASSLEVELMAX" };
	}


	/* (non-Javadoc)
	 * @see pcgen.persistence.lst.prereq.PrerequisiteParserInterface#parse(java.lang.String, java.lang.String, boolean)
	 */
	public Prerequisite parse(String kind, String formula, boolean invertResult, boolean overrideQualify) throws PersistenceLayerException
	{
		Prerequisite prereq = super.parse(kind, formula, invertResult, overrideQualify);

		// ClassLevelMax is basically the inverse of class
		//
		// PRECLASSLEVELMAX:Barbarian=1					-> !PRECLASS:Barbarian=2
		// !PRECLASSLEVELMAX:Barbarian=1				-> PRECLASS:Barbarian=2
		// PRECLASSLEVELMAX:Fighter=1,SPELLCASTER=2		-> PREMULT:2,[!PRECLASS:Fighter=2],[!PRECLASS:SPELLCASTER=3]
		// PRECLASSLEVELMAX:Fighter,SPELLCASTER=2		-> PREMULT:2,[!PRECLASS:Fighter=3],[!PRECLASS:SPELLCASTER=3]
		//

		changeFromLevelMax(prereq);
		if (invertResult)
		{
			prereq.setOperator(prereq.getOperator().invert());
		}

		return prereq;
	}

	//
	// Change all occurances of PRECLASSLEVELMAX to PRECLASS
	//
	private void changeFromLevelMax(Prerequisite p)
	{
		for (Iterator iter = p.getPrerequisites().iterator(); iter.hasNext(); )
		{
			changeFromLevelMax((Prerequisite) iter.next());
		}

		if (p.getKind() == null)	// PREMULT
		{
			// Nothing to do. This is a PREMULT and we don't care about them.
		}
		else if ("classlevelmax".equals(p.getKind()))
		{
			p.setKind("class");
			//
			// PRECLASS is GTEQ, so need to change operator to LT and then add 1 to the operand
			// which results in the of negation of the PRECLASS
			// 'class_levels >= 4'   --> 'class_levels < 5'
			//


			//
			// If the entry after the '=' is non-numeric AbstractPrerequisiteListParser#parse will
			// have placed the entire entry into the key. We need to separate the key from the
			// operand.
			//
			String oper = p.getKey();
			int idxEquals = oper.indexOf('=');
			if (idxEquals >= 0)
			{
				p.setKey(oper.substring(0, idxEquals));
				oper = oper.substring(idxEquals + 1);
			}
			else
			{
				oper = p.getOperand();
			}
			
			try
			{
				oper = Integer.toString(Integer.parseInt(oper) + 1);
			}
			catch (NumberFormatException nfe)
			{
				oper = "(" + oper + ")+1";
			}
			p.setOperand(oper);
			p.setOperator(PrerequisiteOperator.LT);
		}
	}

}
