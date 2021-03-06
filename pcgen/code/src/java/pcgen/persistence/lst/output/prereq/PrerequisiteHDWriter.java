/*
 * PrerequisiteHDWriter.java
 *
 * Copyright 2004 (C) Frugal <frugal@purplewombat.co.uk>
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
 * Last Editor: $Author: binkley $
 *
 * Last Edited: $Date: 2005/10/18 20:23:56 $
 *
 */
package pcgen.persistence.lst.output.prereq;

import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteOperator;
import pcgen.persistence.PersistenceLayerException;

import java.io.IOException;
import java.io.Writer;
import java.util.List;


public class PrerequisiteHDWriter extends AbstractPrerequisiteWriter implements PrerequisiteWriterInterface
{

	/* (non-Javadoc)
	 * @see pcgen.persistence.lst.output.prereq.PrerequisiteWriterInterface#kindHandled()
	 */
	public String kindHandled()
	{
		return "HD";
	}

	/* (non-Javadoc)
	 * @see pcgen.persistence.lst.output.prereq.PrerequisiteWriterInterface#operatorsHandled()
	 */
	public PrerequisiteOperator[] operatorsHandled()
	{
		return new PrerequisiteOperator[] {
				PrerequisiteOperator.GTEQ,
				PrerequisiteOperator.LT
		} ;
	}

	/* (non-Javadoc)
	 * @see pcgen.persistence.lst.output.prereq.PrerequisiteWriterInterface#write(java.io.Writer, pcgen.core.prereq.Prerequisite)
	 */
	public void write(Writer writer, Prerequisite prereq) throws PersistenceLayerException
	{
		checkValidOperator(prereq, operatorsHandled());

		// Case of PREHD:<min>-<max> is handled in PREMULT writer.
		// Only need to hand the PREHD:<min>+ version here

		try
		{
			if (prereq.getOperator().equals(PrerequisiteOperator.LT))
			{
				writer.write('!');
			}

			writer.write("PREHD:");
			writer.write(prereq.getOperand());
			writer.write('+');
		}
		catch (IOException e)
		{
			throw new PersistenceLayerException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see pcgen.persistence.lst.output.prereq.AbstractPrerequisiteWriter#specialCase(java.io.Writer writer, pcgen.core.prereq.Prerequisite prereq)
	 */
	boolean specialCase(Writer writer, Prerequisite prereq) throws IOException
	{
		//
		// If this is a PREMULT...
		//
		if (prereq.getKind() == null)
		{
			//
			// ...with exactly 2 entries...
			//
			List prereqList = prereq.getPrerequisites();
			if (prereqList.size() == 2)
			{
				//
				// ...both of which are PREHD. The first must specify >= and the second <=
				//
				final Prerequisite elementGTEQ =(Prerequisite) prereqList.get(0);
				final Prerequisite elementLTEQ =(Prerequisite) prereqList.get(1);
				if ("hd".equalsIgnoreCase(elementGTEQ.getKind()) && elementGTEQ.getOperator().equals(PrerequisiteOperator.GTEQ) &&
					"hd".equalsIgnoreCase(elementLTEQ.getKind()) && elementLTEQ.getOperator().equals(PrerequisiteOperator.LTEQ))
				{
					if (prereq.getOperator().equals(PrerequisiteOperator.LT))
					{
						writer.write('!');
					}
					writer.write("PREHD:");
					writer.write(elementGTEQ.getOperand());
					writer.write('-');
					writer.write(elementLTEQ.getOperand());
					return true;
				}
			}
		}
		return false;
	}

}
