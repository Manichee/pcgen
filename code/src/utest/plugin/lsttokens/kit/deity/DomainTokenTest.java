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
package plugin.lsttokens.kit.deity;

import org.junit.Test;

import pcgen.core.Domain;
import pcgen.core.kit.KitDeity;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMSubLineLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractKitTokenTestCase;

public class DomainTokenTest extends AbstractKitTokenTestCase<KitDeity>
{

	static DomainToken token = new DomainToken();
	static CDOMSubLineLoader<KitDeity> loader = new CDOMSubLineLoader<KitDeity>(
			"SKILL", KitDeity.class);

	@Override
	public Class<KitDeity> getCDOMClass()
	{
		return KitDeity.class;
	}

	@Override
	public CDOMSubLineLoader<KitDeity> getLoader()
	{
		return loader;
	}

	@Override
	public CDOMPrimaryToken<KitDeity> getToken()
	{
		return token;
	}

	@Test
	public void testInvalidInputEmptyCount() throws PersistenceLayerException
	{
		assertTrue(parse("Fireball"));
		assertConstructionError();
	}

	@Test
	public void testRoundRobinSimple() throws PersistenceLayerException
	{
		primaryContext.ref.constructCDOMObject(Domain.class, "Fireball");
		secondaryContext.ref.constructCDOMObject(Domain.class, "Fireball");
		runRoundRobin("Fireball");
	}

	@Test
	public void testRoundRobinTwo() throws PersistenceLayerException
	{
		primaryContext.ref.constructCDOMObject(Domain.class, "Fireball");
		secondaryContext.ref.constructCDOMObject(Domain.class, "Fireball");
		primaryContext.ref.constructCDOMObject(Domain.class, "English");
		secondaryContext.ref.constructCDOMObject(Domain.class, "English");
		runRoundRobin("Fireball" + getJoinCharacter() + "English");
	}

	@Test
	public void testInvalidListEnd() throws PersistenceLayerException
	{
		assertFalse(parse("TestWP1" + getJoinCharacter()));
	}

	private char getJoinCharacter()
	{
		return '|';
	}

	@Test
	public void testInvalidListStart() throws PersistenceLayerException
	{
		assertFalse(parse(getJoinCharacter() + "TestWP1"));
	}

	@Test
	public void testInvalidListDoubleJoin() throws PersistenceLayerException
	{
		assertFalse(parse("TestWP2" + getJoinCharacter() + getJoinCharacter()
				+ "TestWP1"));
	}

}