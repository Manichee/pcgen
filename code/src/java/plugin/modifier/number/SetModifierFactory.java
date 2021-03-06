/*
 * Copyright 2014 (C) Tom Parker <thpr@users.sourceforge.net>
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package plugin.modifier.number;

import pcgen.base.calculation.CalculationModifier;
import pcgen.base.calculation.FormulaCalculation;
import pcgen.base.calculation.Modifier;
import pcgen.base.calculation.NEPCalculation;
import pcgen.base.formula.base.FormulaManager;
import pcgen.base.formula.base.LegalScope;
import pcgen.base.formula.inst.NEPFormula;
import pcgen.base.util.FormatManager;
import pcgen.cdom.base.FormulaFactory;
import pcgen.rules.persistence.token.AbstractSetModifierFactory;

/**
 * A SetModifierFactory is a ModifierFactory<Number> that returns a specific
 * value (independent of the input) when a Modifier produced by this
 * SetModifierFactory is processed.
 */
public class SetModifierFactory extends AbstractSetModifierFactory<Number>
{

	/**
	 * Identifies that the Modifier objects built by this SetModifierFactory act
	 * upon java.lang.Number objects.
	 * 
	 * @see pcgen.base.modifier.Modifier#getVariableFormat()
	 */
	@Override
	public Class<Number> getVariableFormat()
	{
		return Number.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Modifier<Number> getModifier(int userPriority, String instructions,
		FormulaManager formulaManager, LegalScope varScope,
		FormatManager<Number> formatManager)
	{
		try
		{
			return getFixedModifier(userPriority, formatManager, instructions);
		}
		catch (NumberFormatException e)
		{
			final NEPFormula<Number> f =
					FormulaFactory.getValidFormula(instructions,
						formulaManager, varScope, formatManager);
			NEPCalculation<Number> calc =
					new FormulaCalculation<Number>(f, this);
			return new CalculationModifier<Number>(calc, userPriority);
		}
	}

}
