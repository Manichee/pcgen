/*
 * 
 * Copyright (c) 2010 Tom Parker <thpr@users.sourceforge.net>
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
package actor.choose;

import java.net.URISyntaxException;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import pcgen.core.Ability;
import pcgen.core.AbilityCategory;
import pcgen.core.Globals;
import pcgen.core.SettingsHandler;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.context.LoadContext;
import plugin.lsttokens.choose.AbilityToken;

/**
 * The Class <code>AbilityTokenTest</code> verifies the AbilityToken
 * class is working correctly.
 *
 * <br/>
 * Last Editor: $Author:  $
 * Last Edited: $Date:  $
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision:  $
 */
public class AbilityTokenTest extends TestCase
{
	/** */
	private static final AbilityCategory CATEGORY = AbilityCategory.FEAT;
	private static final AbilityToken pca = new AbilityToken();
	private static final String ITEM_NAME = "ItemName";

	private LoadContext context;

	@Override
	@Before
	public void setUp() throws PersistenceLayerException, URISyntaxException
	{
		SettingsHandler.getGame().clearLoadContext();
		context = Globals.getContext();
		context.ref.importObject(CATEGORY);
	}

	private Ability getObject()
	{
		Ability obj = context.ref.constructCDOMObject(Ability.class, ITEM_NAME);
		//In case
		context.ref.registerAbbreviation(obj, ITEM_NAME);
		context.ref.reassociateCategory(CATEGORY, obj);
		return obj;
	}

	@Test
	public void testEncodeChoice()
	{
		assertEquals(getExpected(), pca.encodeChoice(getObject()));
	}

	protected String getExpected()
	{
		return ITEM_NAME;
	}

	@Test
	public void testDecodeChoice()
	{
		assertEquals(getObject(), pca.decodeChoice(getExpected(), CATEGORY));
	}

	@Test
	public void testLegacyDecodeChoice()
	{
		assertEquals(getObject(), pca.decodeChoice("CATEGORY=FEAT|" +ITEM_NAME, CATEGORY));
	}

}