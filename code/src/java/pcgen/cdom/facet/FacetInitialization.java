/*
 * Copyright (c) Thomas Parker, 2009.
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
package pcgen.cdom.facet;

import pcgen.core.PlayerCharacter.AddLanguageFacet;
import pcgen.core.PlayerCharacter.AutoLanguageFacet;
import pcgen.core.PlayerCharacter.FreeLanguageFacet;
import pcgen.core.PlayerCharacter.SkillLanguageFacet;
import pcgen.core.PlayerCharacter.StartingLanguageFacet;

public class FacetInitialization
{

	public static void initialize()
	{
		TemplateFacet templateFacet = FacetLibrary
				.getFacet(TemplateFacet.class);
		ConditionalTemplateFacet conditionalTemplateFacet = FacetLibrary
				.getFacet(ConditionalTemplateFacet.class);
		RaceFacet raceFacet = FacetLibrary.getFacet(RaceFacet.class);
		ClassFacet classFacet = FacetLibrary.getFacet(ClassFacet.class);
		ClassLevelFacet classLevelFacet = FacetLibrary
				.getFacet(ClassLevelFacet.class);
		CampaignFacet campaignFacet = FacetLibrary
				.getFacet(CampaignFacet.class);
		ExpandedCampaignFacet expandedCampaignFacet = FacetLibrary
				.getFacet(ExpandedCampaignFacet.class);
		EquipmentFacet equipmentFacet = FacetLibrary
				.getFacet(EquipmentFacet.class);
		EquippedEquipmentFacet equippedFacet = FacetLibrary
				.getFacet(EquippedEquipmentFacet.class);
		NaturalEquipmentFacet naturalEquipmentFacet = FacetLibrary
				.getFacet(NaturalEquipmentFacet.class);
		SourcedEquipmentFacet activeEquipmentFacet = FacetLibrary
				.getFacet(SourcedEquipmentFacet.class);
		ActiveEqModFacet activeEqModFacet = FacetLibrary
				.getFacet(ActiveEqModFacet.class);

		AlignmentFacet alignmentFacet = FacetLibrary
				.getFacet(AlignmentFacet.class);
		BioSetFacet bioSetFacet = FacetLibrary.getFacet(BioSetFacet.class);
		CheckFacet checkFacet = FacetLibrary.getFacet(CheckFacet.class);

		LanguageFacet languageFacet = FacetLibrary
				.getFacet(LanguageFacet.class);
		LanguageFacet freeLangFacet = FacetLibrary
				.getFacet(FreeLanguageFacet.class);
		LanguageFacet autoLangFacet = FacetLibrary
				.getFacet(AutoLanguageFacet.class);
		LanguageFacet addLangFacet = FacetLibrary
				.getFacet(AddLanguageFacet.class);
		LanguageFacet skillLangFacet = FacetLibrary
				.getFacet(SkillLanguageFacet.class);
		// LanguageFacet langAutoFacet =
		// FacetLibrary.getFacet(AutoLanguageFacet.class);
		LanguageFacet startingLangFacet = FacetLibrary
				.getFacet(StartingLanguageFacet.class);

		LevelFacet levelFacet = FacetLibrary.getFacet(LevelFacet.class);
		SizeFacet sizeFacet = FacetLibrary.getFacet(SizeFacet.class);
		BonusChangeFacet bonusChangeFacet = FacetLibrary
				.getFacet(BonusChangeFacet.class);
		DeityFacet deityFacet = FacetLibrary.getFacet(DeityFacet.class);
		DomainFacet domainFacet = FacetLibrary.getFacet(DomainFacet.class);
		CompanionModFacet companionModFacet = FacetLibrary
				.getFacet(CompanionModFacet.class);
		StatFacet statFacet = FacetLibrary.getFacet(StatFacet.class);
		SkillFacet skillFacet = FacetLibrary.getFacet(SkillFacet.class);
		ActiveAbilityFacet abFacet = FacetLibrary
				.getFacet(ActiveAbilityFacet.class);

		CDOMObjectConsolidationFacet cdomObjectFacet = FacetLibrary
				.getFacet(CDOMObjectConsolidationFacet.class);
		ObjectAdditionFacet additionFacet = FacetLibrary
				.getFacet(ObjectAdditionFacet.class);
		GrantedAbilityFacet grantedAbilityFacet = FacetLibrary
				.getFacet(GrantedAbilityFacet.class);

		autoLangFacet.addDataFacetChangeListener(languageFacet);
		freeLangFacet.addDataFacetChangeListener(languageFacet);
		addLangFacet.addDataFacetChangeListener(languageFacet);
		skillLangFacet.addDataFacetChangeListener(languageFacet);
		startingLangFacet.addDataFacetChangeListener(languageFacet);

		equipmentFacet.addDataFacetChangeListener(naturalEquipmentFacet);
		equippedFacet.addDataFacetChangeListener(activeEquipmentFacet);
		naturalEquipmentFacet.addDataFacetChangeListener(activeEquipmentFacet);
		activeEquipmentFacet.addDataFacetChangeListener(activeEqModFacet);

		campaignFacet.addDataFacetChangeListener(expandedCampaignFacet);

		classFacet.addLevelChangeListener(classLevelFacet);
		classFacet.addLevelChangeListener(levelFacet);
		levelFacet.addLevelChangeListener(conditionalTemplateFacet);

		raceFacet.addDataFacetChangeListener(sizeFacet);
		templateFacet.addDataFacetChangeListener(sizeFacet);
		conditionalTemplateFacet.addDataFacetChangeListener(sizeFacet);
		bonusChangeFacet.addBonusChangeListener(sizeFacet, "SIZEMOD", "NUMBER");

		expandedCampaignFacet.addDataFacetChangeListener(cdomObjectFacet);
		alignmentFacet.addDataFacetChangeListener(cdomObjectFacet);
		bioSetFacet.addDataFacetChangeListener(cdomObjectFacet);
		checkFacet.addDataFacetChangeListener(cdomObjectFacet);
		classFacet.addDataFacetChangeListener(cdomObjectFacet);
		companionModFacet.addDataFacetChangeListener(cdomObjectFacet);
		deityFacet.addDataFacetChangeListener(cdomObjectFacet);
		domainFacet.addDataFacetChangeListener(cdomObjectFacet);
		activeEquipmentFacet.addDataFacetChangeListener(cdomObjectFacet);
		activeEqModFacet.addDataFacetChangeListener(cdomObjectFacet);
		abFacet.addDataFacetChangeListener(cdomObjectFacet);
		raceFacet.addDataFacetChangeListener(cdomObjectFacet);
		sizeFacet.addDataFacetChangeListener(cdomObjectFacet);
		skillFacet.addDataFacetChangeListener(cdomObjectFacet);
		statFacet.addDataFacetChangeListener(cdomObjectFacet);
		templateFacet.addDataFacetChangeListener(cdomObjectFacet);
		// weaponProfList is still just a list of Strings
		// results.addAll(getWeaponProfList());
		classLevelFacet.addDataFacetChangeListener(cdomObjectFacet);
		conditionalTemplateFacet.addDataFacetChangeListener(cdomObjectFacet);
		grantedAbilityFacet.addDataFacetChangeListener(cdomObjectFacet);
		
		cdomObjectFacet.addDataFacetChangeListener(additionFacet);
	}
}
