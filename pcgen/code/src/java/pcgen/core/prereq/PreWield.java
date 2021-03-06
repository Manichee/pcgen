/*
 * PreWield.java
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
 * Created on November 28, 2003
 *
 * Current Ver: $Revision: 1.10 $
 * Last Editor: $Author: nuance $
 * Last Edited: $Date: 2005/09/22 03:12:17 $
 *
 */
package pcgen.core.prereq;

import pcgen.core.Equipment;
import pcgen.core.PlayerCharacter;

/**
 * @author jayme cox <jaymecox@users.sourceforge.net>
 *
 */
public class PreWield
	extends AbstractPrerequisiteTest
	implements PrerequisiteTest {

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.PlayerCharacter)
	 */
	public int passes(final Prerequisite prereq, final Equipment equipment, PlayerCharacter aPC) {
		int runningTotal = 0;
		if (equipment.getWield().equalsIgnoreCase(prereq.getKey()))
		{
			runningTotal++;
		}

		return countedTotal( prereq, prereq.getOperator().compare(runningTotal, 1));
	}

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#kindsHandled()
	 */
	public String kindHandled() {
		return "WIELD"; //$NON-NLS-1$
	}
}

