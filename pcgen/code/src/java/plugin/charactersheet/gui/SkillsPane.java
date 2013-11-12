/*
 * UnarmedPane.java
 *
 * Created on February 3, 2004, 3:23 PM
 */

package plugin.charactersheet.gui;

import gmgen.plugin.PlayerCharacterOutput;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.List;

import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.SkillFilter;
import pcgen.core.PlayerCharacter;
import pcgen.core.SettingsHandler;
import pcgen.core.Skill;
import pcgen.core.SkillUtilities;
import pcgen.core.analysis.QualifiedName;
import pcgen.core.analysis.SkillModifier;
import pcgen.core.analysis.SkillRankControl;
import pcgen.gui2.util.FontManipulation;
import pcgen.system.PCGenSettings;
import pcgen.util.enumeration.Visibility;
import plugin.charactersheet.CharacterSheetUtils;

/**
 * Confirmed no memory Leaks Dec 10, 2004
 * @author  ddjone3
 */
public class SkillsPane extends javax.swing.JPanel
{
	private PlayerCharacter pc;
	private List<Component> componentList = new ArrayList<Component>();

	private static final String BLANK = "";
	private static final String STAR = "*";
	private static final String PLUS = "+";
	private static final String EQUALS = "=";
	private static final String X = "X";
	private static final String SKILLS = "SKILLS";
	private static final String MAX_RANKS = "Max Ranks";
	private static final String N14_7 = "14/7";
	private static final String KEY = "Key";
	private static final String SKILL = "Skill";
	private static final String ABILITY = "Ability";
	private static final String MISC = "Misc";
	private static final String SKILL_NAME = "SKILL NAME";
	private static final String MOD = "Mod";
	private static final String RANKS = "Ranks";
	private static final String SKILL_TOKEN = "SKILL";
	private static final String ABMOD_TOKEN = "ABMOD";
	private static final String MISC_TOKEN = "MISC";

	/** Creates new form UnarmedPane */
	public SkillsPane()
	{
		initComponents();
		setLocalColor();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents()
	{//GEN-BEGIN:initComponents
		java.awt.GridBagConstraints gridBagConstraints;

		jPanel5 = new javax.swing.JPanel();
		jLabel2 = new javax.swing.JLabel();
		jPanel22 = new javax.swing.JPanel();
		jLabel14 = new javax.swing.JLabel();
		jPanel23 = new javax.swing.JPanel();
		jLabel15 = new javax.swing.JLabel();
		jPanel9 = new javax.swing.JPanel();
		jLabel6 = new javax.swing.JLabel();
		jPanel11 = new javax.swing.JPanel();
		jLabel4 = new javax.swing.JLabel();
		jPanel13 = new javax.swing.JPanel();
		jLabel7 = new javax.swing.JLabel();
		jPanel21 = new javax.swing.JPanel();
		jLabel13 = new javax.swing.JLabel();
		jPanel6 = new javax.swing.JPanel();
		jLabel5 = new javax.swing.JLabel();
		jPanel16 = new javax.swing.JPanel();
		jLabel16 = new javax.swing.JLabel();
		jPanel17 = new javax.swing.JPanel();
		jLabel9 = new javax.swing.JLabel();
		jPanel18 = new javax.swing.JPanel();
		jLabel10 = new javax.swing.JLabel();
		jPanel19 = new javax.swing.JPanel();
		jLabel11 = new javax.swing.JLabel();
		jPanel20 = new javax.swing.JPanel();
		jLabel12 = new javax.swing.JLabel();

		setLayout(new java.awt.GridBagLayout());

		setMaximumSize(new java.awt.Dimension(1000, 1000));
		jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		FontManipulation.xlarge(jLabel2);
		jLabel2.setText(SKILLS);
		jPanel5.add(jLabel2);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		add(jPanel5, gridBagConstraints);

		jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		jLabel14.setText(MAX_RANKS);
		jPanel22.add(jLabel14);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
		add(jPanel22, gridBagConstraints);

		jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		jLabel15.setText(N14_7);
		jPanel23.add(jLabel15);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 9;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
		add(jPanel23, gridBagConstraints);

		jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		jLabel6.setText(KEY);
		jPanel9.add(jLabel6);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(jPanel9, gridBagConstraints);

		jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		jLabel4.setText(SKILL);
		jPanel11.add(jLabel4);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		add(jPanel11, gridBagConstraints);

		jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		jLabel7.setText(ABILITY);
		jPanel13.add(jLabel7);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 1;
		add(jPanel13, gridBagConstraints);

		jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		jLabel13.setText(MISC);
		jPanel21.add(jLabel13);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 9;
		gridBagConstraints.gridy = 1;
		add(jPanel21, gridBagConstraints);

		jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		FontManipulation.large(jLabel5);
		jLabel5.setText(SKILL_NAME);
		jPanel6.add(jLabel5);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		add(jPanel6, gridBagConstraints);

		jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		jLabel16.setText(ABILITY);
		jPanel16.add(jLabel16);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(jPanel16, gridBagConstraints);

		jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		jLabel9.setText(MOD);
		jPanel17.add(jLabel9);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 2;
		add(jPanel17, gridBagConstraints);

		jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		jLabel10.setText(MOD);
		jPanel18.add(jLabel10);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 2;
		add(jPanel18, gridBagConstraints);

		jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		jLabel11.setText(RANKS);
		jPanel19.add(jLabel11);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 2;
		add(jPanel19, gridBagConstraints);

		jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		jLabel12.setText(MOD);
		jPanel20.add(jLabel12);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 9;
		gridBagConstraints.gridy = 2;
		add(jPanel20, gridBagConstraints);

	}//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel14;
	private javax.swing.JLabel jLabel15;
	private javax.swing.JLabel jLabel16;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel jPanel11;
	private javax.swing.JPanel jPanel13;
	private javax.swing.JPanel jPanel16;
	private javax.swing.JPanel jPanel17;
	private javax.swing.JPanel jPanel18;
	private javax.swing.JPanel jPanel19;
	private javax.swing.JPanel jPanel20;
	private javax.swing.JPanel jPanel21;
	private javax.swing.JPanel jPanel22;
	private javax.swing.JPanel jPanel23;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel9;

	// End of variables declaration//GEN-END:variables

	public void setColor()
	{
		setLocalColor();
		refresh();
	}

	public void setLocalColor()
	{
		setBackground(CharacterPanel.header);
		setBorder(new javax.swing.border.LineBorder(CharacterPanel.border, 2));
		jPanel5.setBackground(CharacterPanel.header);
		jPanel22.setBackground(CharacterPanel.header);
		jPanel23.setBackground(CharacterPanel.header);
		jPanel9.setBackground(CharacterPanel.header);
		jPanel11.setBackground(CharacterPanel.header);
		jPanel13.setBackground(CharacterPanel.header);
		jPanel21.setBackground(CharacterPanel.header);
		jPanel6.setBackground(CharacterPanel.header);
		jPanel16.setBackground(CharacterPanel.header);
		jPanel17.setBackground(CharacterPanel.header);
		jPanel18.setBackground(CharacterPanel.header);
		jPanel19.setBackground(CharacterPanel.header);
		jPanel20.setBackground(CharacterPanel.header);
	}

	public void setPc(PlayerCharacter pc)
	{
		this.pc = pc;
	}

	/**
	 * This method refreshes the Skills Pane
	 * 
	 * Karianna - Synchronize this method as suggested by James
	 * Bug 1945506 - SkillPanel throwing NPE
	 */
	public synchronized void refresh()
	{
		PlayerCharacterOutput pcOut = new PlayerCharacterOutput(pc);

		int totalLevels = pc.getDisplay().getTotalLevels();
		final String maxCrossClassRanks =
				SkillUtilities.maxCrossClassSkillForLevel(totalLevels,
					pc).toString();
		final String maxClassRanks =
				SkillUtilities.maxClassSkillForLevel(totalLevels, pc)
					.toString();
		jLabel15.setText(maxClassRanks + '/' + maxCrossClassRanks);

		// Use the output selection of skills
		SkillFilter filter = SkillFilter.getByValue(PCGenSettings.OPTIONS_CONTEXT.initInt(
				PCGenSettings.OPTION_SKILL_FILTER, SkillFilter.Usable.getValue()));
		
		if (filter == SkillFilter.SkillsTab)
		{
			filter = pc.getSkillFilter();
		}
		pc.populateSkills(filter);

		List<Skill> skillList =
				pc.getSkillListInOutputOrder(pc.getDisplay()
					.getPartialSkillList(Visibility.OUTPUT_ONLY));

		// Remove only the skill lines, not the header block. 
		for (int i = 0; i < componentList.size(); i++)
		{
			Component cmpt = componentList.get(i);
			// Karianna - Added null check for bug 1945506
			if (cmpt != null)
			{
				remove(cmpt);
			}
		}
		componentList.clear();

		boolean lightLine = true;
		int gridY = 3;
		for (int i = 0; i < skillList.size(); i++)
		{
			Skill skill = skillList.get(i);

			String check = BLANK;
			if (skill.getSafe(ObjectKey.USE_UNTRAINED))
			{
				check = STAR;
			}
			else if (skill.getSafe(ObjectKey.EXCLUSIVE))
			{
				check = X;
			}
			String name = QualifiedName.qualifiedName(pc, skill);
			String ability = skill.getKeyStatAbb();
			String total =
					Integer.toString(SkillRankControl.getTotalRank(pc, skill).intValue()
						+ SkillModifier.modifier(skill, pc).intValue());
			StringBuilder abSb = new StringBuilder();
			abSb.append(SKILL_TOKEN).append('.').append(i).append('.').append(
				ABMOD_TOKEN);
			String abilityMod = pcOut.getExportToken(abSb.toString());
			String ranks = SkillRankControl.getTotalRank(pc, skill).toString();
			StringBuilder miscSb = new StringBuilder();
			miscSb.append(SKILL_TOKEN).append('.').append(i).append('.')
				.append(MISC_TOKEN);
			String miscMod = pcOut.getExportToken(miscSb.toString());

			gridY =
					addLine(lightLine, gridY, check, CharacterSheetUtils
						.getTitle(name), CharacterSheetUtils.getSubTitle(name),
						ability, total, abilityMod, ranks, miscMod);

			if (lightLine)
			{
				lightLine = false;
			}
			else
			{
				lightLine = true;
			}
			gridY++;
		}

		// Reset the skills back to the display prefs.
		pc.populateSkills(pc.getSkillFilter());
		revalidate();
		repaint();
	}

	private int addLine(boolean lightLine, int gridY, String check,
		String name, String subName, String ability, String total,
		String abilityMod, String ranks, String miscMod)
	{
		java.awt.Color light;
		java.awt.Color dark;
		if (lightLine)
		{
			light = CharacterPanel.bodyLight;
			dark = CharacterPanel.bodyMedLight;
		}
		else
		{
			light = CharacterPanel.bodyMedLight;
			dark = CharacterPanel.bodyMedDark;
		}

		CharacterSheetUtils.addGbCell(this, check, 0, gridY, 1, 1,
			GridBagConstraints.BOTH, GridBagConstraints.CENTER,
			FlowLayout.CENTER, light, componentList);
		CharacterSheetUtils.addGbCell(this, name, 1, gridY, 1, 1,
			GridBagConstraints.BOTH, GridBagConstraints.WEST, FlowLayout.LEFT,
			light, componentList);
		CharacterSheetUtils.addGbCell(this, ability, 2, gridY, 1, 1,
			GridBagConstraints.BOTH, GridBagConstraints.EAST, FlowLayout.RIGHT,
			light, componentList);
		CharacterSheetUtils.addGbCell(this, total, 3, gridY, 1, 1,
			GridBagConstraints.BOTH, GridBagConstraints.CENTER,
			FlowLayout.CENTER, dark, componentList);
		CharacterSheetUtils.addGbCell(this, EQUALS, 4, gridY, 1, 1,
			GridBagConstraints.BOTH, GridBagConstraints.CENTER,
			FlowLayout.CENTER, light, componentList);
		CharacterSheetUtils.addGbCell(this, abilityMod, 5, gridY, 1, 1,
			GridBagConstraints.BOTH, GridBagConstraints.CENTER,
			FlowLayout.CENTER, light, componentList);
		CharacterSheetUtils.addGbCell(this, PLUS, 6, gridY, 1, 1,
			GridBagConstraints.BOTH, GridBagConstraints.CENTER,
			FlowLayout.CENTER, light, componentList);
		CharacterSheetUtils.addGbCell(this, ranks, 7, gridY, 1, 1,
			GridBagConstraints.BOTH, GridBagConstraints.CENTER,
			FlowLayout.CENTER, light, componentList);
		CharacterSheetUtils.addGbCell(this, PLUS, 8, gridY, 1, 1,
			GridBagConstraints.BOTH, GridBagConstraints.CENTER,
			FlowLayout.CENTER, light, componentList);
		CharacterSheetUtils.addGbCell(this, miscMod, 9, gridY, 1, 1,
			GridBagConstraints.BOTH, GridBagConstraints.CENTER,
			FlowLayout.CENTER, light, componentList);

		if (!subName.equals(BLANK))
		{
			gridY++;

			CharacterSheetUtils.addGbCell(this, BLANK, 0, gridY, 1, 1,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER,
				FlowLayout.CENTER, light, componentList);
			CharacterSheetUtils.addGbCell(this, subName, 1, gridY, 1, 2,
				GridBagConstraints.BOTH, GridBagConstraints.WEST,
				FlowLayout.LEFT, light, componentList);
			CharacterSheetUtils.addGbCell(this, BLANK, 3, gridY, 1, 1,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER,
				FlowLayout.CENTER, dark, componentList);
			CharacterSheetUtils.addGbCell(this, BLANK, 4, gridY, 1, 7,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER,
				FlowLayout.CENTER, light, componentList);
		}

		return gridY;
	}

	public void destruct()
	{
		//Put any code here that is needed to prevent memory leaks when this panel is destroyed
	}
}