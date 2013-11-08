/*
 * Copyright (c) 2007 Tom Parker <thpr@users.sourceforge.net>
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */
package plugin.lsttokens.skill;

import org.junit.Test;

import pcgen.cdom.enumeration.ObjectKey;
import pcgen.core.Skill;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractYesNoTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class UseuntrainedTokenTest extends AbstractYesNoTokenTestCase<Skill>
{

	static UseuntrainedToken token = new UseuntrainedToken();
	static CDOMTokenLoader<Skill> loader = new CDOMTokenLoader<Skill>(
			Skill.class);

	@Override
	public Class<Skill> getCDOMClass()
	{
		return Skill.class;
	}

	@Override
	public CDOMLoader<Skill> getLoader()
	{
		return loader;
	}

	@Override
	public CDOMPrimaryToken<Skill> getToken()
	{
		return token;
	}

	@Override
	public ObjectKey<Boolean> getObjectKey()
	{
		return ObjectKey.USE_UNTRAINED;
	}

	@Test
	public void dummyTest()
	{
		// Just to get Eclipse to recognize this as a JUnit 4.0 Test Case
	}

}