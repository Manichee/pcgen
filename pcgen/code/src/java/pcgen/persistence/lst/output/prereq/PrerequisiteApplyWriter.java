/*
 * PrerequisiteApplyWriter.java
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
 * Current Ver: $Revision: 1.4 $
 *
 * Last Editor: $Author: byngl $
 *
 * Last Edited: $Date: 2005/11/06 18:01:14 $
 *
 */
package pcgen.persistence.lst.output.prereq;

import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteOperator;
import pcgen.persistence.PersistenceLayerException;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

public class PrerequisiteApplyWriter extends AbstractPrerequisiteWriter implements PrerequisiteWriterInterface
{

	/* (non-Javadoc)
	 * @see pcgen.persistence.lst.output.prereq.PrerequisiteWriterInterface#kindHandled()
	 */
	public String kindHandled()
	{
		return "apply";
	}

	/* (non-Javadoc)
	 * @see pcgen.persistence.lst.output.prereq.PrerequisiteWriterInterface#operatorsHandled()
	 */
	public PrerequisiteOperator[] operatorsHandled()
	{
		return new PrerequisiteOperator[] {
				PrerequisiteOperator.EQ,
				PrerequisiteOperator.NEQ
		};
	}

	/* (non-Javadoc)
	 * @see pcgen.persistence.lst.output.prereq.PrerequisiteWriterInterface#write(java.io.Writer, pcgen.core.prereq.Prerequisite)
	 */
	public void write(Writer writer, Prerequisite prereq) throws PersistenceLayerException
	{
		List subreqs = prereq.getPrerequisites();

		try
		{
			checkValidOperator(prereq, operatorsHandled());
			if (prereq.getOperator().equals( PrerequisiteOperator.NEQ ))
			{
				writer.write('!');
			}
			writer.write("PREAPPLY:");

			if ( Integer.parseInt(prereq.getOperand()) > 1 )
			{
				// must be a "A and b" operation
				int i=0;
				for (Iterator iter = subreqs.iterator(); iter.hasNext(); i++)
				{
					Prerequisite subreq = (Prerequisite) iter.next();
					if (i > 0)
					{
						writer.write(',');
					}
					writer.write(subreq.getOperand());
				}
			}
			else
			{
				for (Iterator iter = subreqs.iterator(); iter.hasNext();)
				{
					Prerequisite subreq = (Prerequisite) iter.next();
					if (subreq.getKind()==null)
					{
						// must be an "A or B" operation
						List subsubreqs = subreq.getPrerequisites();
						int i = 0;
						for (Iterator iterator = subsubreqs.iterator(); iterator.hasNext(); i++)
						{
							Prerequisite subsubreq = (Prerequisite) iterator.next();
							if (i > 0)
							{
								writer.write(';');
							}
							writer.write(subsubreq.getOperand());
						}
					}
					else
					{
						writer.write(subreq.getOperand());
					}
				}
			}
		}
		catch (NumberFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			throw new PersistenceLayerException(e.getMessage());
		}
	}

}
