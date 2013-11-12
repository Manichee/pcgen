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
package plugin.lsttokens.kit.clazz;

import java.net.URISyntaxException;

import org.junit.Test;

import pcgen.cdom.enumeration.SubClassCategory;
import pcgen.core.PCClass;
import pcgen.core.SubClass;
import pcgen.core.kit.KitClass;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMSubLineLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractKitTokenTestCase;

public class SubClassTokenTest extends AbstractKitTokenTestCase<KitClass>
{

	static SubclassToken token = new SubclassToken();
	static CDOMSubLineLoader<KitClass> loader = new CDOMSubLineLoader<KitClass>(
			"SKILL", KitClass.class);

	@Override
	public void setUp() throws PersistenceLayerException, URISyntaxException
	{
		super.setUp();
		primaryContext.ref.constructCDOMObject(PCClass.class, "Wizard");
		secondaryContext.ref.constructCDOMObject(PCClass.class, "Wizard");
		primaryProf.setPcclass(primaryContext.ref.getCDOMReference(
				PCClass.class, "Wizard"));
		secondaryProf.setPcclass(secondaryContext.ref.getCDOMReference(
				PCClass.class, "Wizard"));
	}

	@Override
	public Class<KitClass> getCDOMClass()
	{
		return KitClass.class;
	}

	@Override
	public CDOMSubLineLoader<KitClass> getLoader()
	{
		return loader;
	}

	@Override
	public CDOMPrimaryToken<KitClass> getToken()
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
	public void testInvalidInputOnlyOne() throws PersistenceLayerException
	{
		SubClassCategory cat = SubClassCategory.getConstant("Wizard");
		SubClass sc = primaryContext.ref.constructCDOMObject(SubClass.class, "Fireball");
		primaryContext.ref.reassociateCategory(cat, sc);
		sc = secondaryContext.ref.constructCDOMObject(SubClass.class, "Fireball");
		secondaryContext.ref.reassociateCategory(cat, sc);
		sc = primaryContext.ref.constructCDOMObject(SubClass.class, "English");
		primaryContext.ref.reassociateCategory(cat, sc);
		sc = secondaryContext.ref.constructCDOMObject(SubClass.class, "English");
		secondaryContext.ref.reassociateCategory(cat, sc);
		assertTrue(parse("Fireball,English"));
		assertConstructionError();
	}

	@Test
	public void testRoundRobinSimple() throws PersistenceLayerException
	{
		SubClassCategory cat = SubClassCategory.getConstant("Wizard");
		SubClass sc = primaryContext.ref.constructCDOMObject(SubClass.class, "Fireball");
		primaryContext.ref.reassociateCategory(cat, sc);
		sc = secondaryContext.ref.constructCDOMObject(SubClass.class, "Fireball");
		secondaryContext.ref.reassociateCategory(cat, sc);
		runRoundRobin("Fireball");
	}
}