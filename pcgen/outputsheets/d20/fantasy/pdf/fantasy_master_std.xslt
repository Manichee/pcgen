<?xml version="1.0" encoding="UTF-8"?>
<!-- $Header: /cvsroot/pcgendocs/pcgendocs/outputsheets/d20/fantasy/pdf/fantasy_master_std.xslt,v 1.20 2006/02/24 23:39:48 frank_kliewe Exp $ -->
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fo="http://www.w3.org/1999/XSL/Format" 
	xmlns:xalan="http://xml.apache.org/xalan" 
	xmlns:Psionics="my:Psionics" 
	xmlns:myAttribs="my:Attribs" 
	exclude-result-prefixes="myAttribs Psionics">
	
	<xsl:import href="fantasy_common.xsl"/>
	<xsl:import href="leadership.xsl"/>

	<xsl:output indent="yes"/>
	
	<xsl:variable name="vAttribs_tree">
		<myAttribs:myAttribs>
			<xsl:copy-of select="$vAttribs/*"/>
			<xsl:copy-of select="document('leadership.xsl')/*/myAttribs:*/*"/>
		</myAttribs:myAttribs>
	</xsl:variable>
	<xsl:variable name="vAttribs_all" select="xalan:nodeset($vAttribs_tree)"/>
	
		
	<!-- Include all of the output attributes -->
	<!-- vAttribs will be set up in the stylesheet that calls this one -->
	<xsl:template name="attrib">
		<xsl:param name="attribute"/>
		<xsl:copy-of select="$vAttribs_all/*/*[name() = $attribute]/@*"/>
		<xsl:for-each select="$vAttribs_all/*/*[name() = $attribute]/subattrib/@*">
			<xsl:variable name="bar" select="name()"/>
			<xsl:call-template name="attrib">
				<xsl:with-param name="attribute" select="$bar"/>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="page.layouts">
			<!--	PAGE DEFINITIONS	-->
			<fo:layout-master-set>
				<fo:simple-page-master master-name="Portrait 2 Column" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="15mm" margin-left="6mm" margin-right="6mm">
					<fo:region-body region-name="body" column-count="2" column-gap="2mm" margin-bottom="7mm"/>
					<fo:region-after region-name="footer" extent="0.25in"/>
				</fo:simple-page-master>
				<fo:simple-page-master master-name="Portrait" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="15mm" margin-left="6mm" margin-right="6mm">
					<fo:region-body region-name="body" margin-bottom="7mm"/>
					<fo:region-after region-name="footer" extent="0.25in"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
	</xsl:template>

	<!--
====================================
====================================
	TEMPLATE - PAGE FOOTER
====================================
====================================-->
	<xsl:template name="page.footer">
		<fo:static-content flow-name="footer" font-family="sans-serif">
			<xsl:call-template name="page.footer.content"/>
		</fo:static-content>
	</xsl:template>

	<!--
====================================
====================================
	TEMPLATE - PAGE FOOTER
====================================
====================================-->
	<xsl:template name="page.footer.content">
		<fo:table table-layout="fixed">
			<fo:table-column column-width="1.875in"/>
			<fo:table-column column-width="3.75in"/>
			<fo:table-column column-width="1.875in"/>
			<fo:table-body>
				<fo:table-row keep-with-next="always" keep-together="always">
					<fo:table-cell text-align="start" border-top-color="black" border-top-style="solid" border-top-width="0.1pt" background-color="transparent" padding-top="2pt">
						<fo:block font-size="5pt">
							<xsl:value-of select="export/date"/>
							<xsl:text> </xsl:text>
							<xsl:value-of select="export/time"/>
						</fo:block>
						<fo:block font-size="5pt" font-weight="bold">Created using PCGen <xsl:value-of select="export/version"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="center" wrap-option="no-wrap" border-top-color="black" border-top-style="solid" border-top-width="0.1pt" background-color="transparent" padding-top="2pt">
						<fo:block text-align="center" font-size="5pt">PCGen Character Template by Frugal, based on work by ROG, Arcady, Barak, Dimrill &amp; Dekker.</fo:block>
						<fo:block text-align="center" font-size="5pt">For suggestions please post to pcgen@yahoogroups.com with "OS Suggestion" in the subject line.</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="end" border-top-color="black" border-top-style="solid" border-top-width="0.1pt" background-color="transparent" padding-top="2pt">
						<fo:block font-size="7pt">Page <fo:page-number/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<!--
		Start the character
		-->
	<xsl:template match="character">
		<!-- calculate the number of weapons and skills on the first page -->
		<xsl:variable name="first_page_weapon_count">
			<xsl:call-template name="view.weapon.num"/>
		</xsl:variable>
		<xsl:variable name="first_page_skills_count">
			<xsl:call-template name="view.skills.num"/>
		</xsl:variable>
		<xsl:message>Number of weapons on first page = <xsl:value-of select="$first_page_weapon_count"/></xsl:message>
		<xsl:message>Number of skills on first page = <xsl:value-of select="$first_page_skills_count"/></xsl:message>
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<xsl:call-template name="page.layouts"/>
			<!--
				Start the first page
				-->
			<fo:page-sequence>
				<xsl:attribute name="master-reference">Portrait</xsl:attribute>
				<xsl:call-template name="page.footer"/>
				<!--	CHARACTER BODY STARTS HERE !!!	-->
				<fo:flow flow-name="body"  font-size="8pt">
					<!--	CHARACTER HEADER	-->
					<fo:block span="all" space-after.optimum="3pt">
						<xsl:apply-templates select="basics"/>
					</fo:block>
					<fo:block span="all">
						<fo:table table-layout="fixed">
							<fo:table-column column-width="55mm"/>
							<fo:table-column column-width="49mm"/>
							<fo:table-column column-width="86mm"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell number-rows-spanned="2" border-width="1pt" border-color="red">
										<xsl:apply-templates select="abilities"/>
									</fo:table-cell>
									<fo:table-cell number-columns-spanned="2" border-width="1pt" border-color="red">
										<xsl:apply-templates select="." mode="hp_table"/>
										<xsl:apply-templates select="armor_class"/>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell>
										<xsl:apply-templates select="initiative"/>
										<xsl:apply-templates select="basics/bab" mode="bab"/>
									</fo:table-cell>
									<fo:table-cell number-rows-spanned="2">
										<xsl:apply-templates select="skills">
											<xsl:with-param name="first_skill" select="0"/>
											<xsl:with-param name="last_skill" select="$first_page_skills_count"/>
											<xsl:with-param name="column_width" select="'narrow'"/>
										</xsl:apply-templates>
										<xsl:apply-templates select="class_features/bardic_music"/>
										<xsl:apply-templates select="class_features/turning"/>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell number-columns-spanned="2">
										<xsl:apply-templates select="saving_throws"/>
										<xsl:apply-templates select="attack" mode="ranged_melee"/>
										<xsl:apply-templates select="weapons/unarmed"/>
										<xsl:apply-templates select="weapons">
											<xsl:with-param name="first_weapon" select="1"/>
											<xsl:with-param name="last_weapon" select="$first_page_weapon_count"/>
											<xsl:with-param name="column_width" select="'wide'"/>
										</xsl:apply-templates>
										<xsl:apply-templates select="protection"/>
										<xsl:apply-templates select="class_features/rage"/>
										<xsl:apply-templates select="class_features/wildshape"/>
										<xsl:apply-templates select="class_features/stunning_fist"/>
										<xsl:apply-templates select="class_features/wholeness_of_body"/>
										<xsl:apply-templates select="class_features/layonhands"/>
										<xsl:apply-templates select="class_features/psionics"/>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
			<!--
				Start the Second page
				-->
			<fo:page-sequence>
				<xsl:attribute name="master-reference">Portrait 2 Column</xsl:attribute>
				<xsl:call-template name="page.footer"/>
				<fo:flow flow-name="body"  font-size="8pt">
					<fo:block>
						<xsl:apply-templates select="weapons">
							<xsl:with-param name="first_weapon" select="$first_page_weapon_count+1"/>
							<xsl:with-param name="last_weapon" select="9999"/>
							<xsl:with-param name="column_width" select="'narrow'"/>
						</xsl:apply-templates>
						<xsl:apply-templates select="skills">
							<xsl:with-param name="first_skill" select="$first_page_skills_count+1"/>
							<xsl:with-param name="last_skill" select="9999"/>
							<xsl:with-param name="column_width" select="'wide'"/>
						</xsl:apply-templates>
						<xsl:apply-templates select="equipment" />
						<xsl:apply-templates select="weight_allowance"/>
						<xsl:call-template name="money"/>
						<xsl:apply-templates select="misc/magics"/>
						<xsl:apply-templates select="special_abilities"/>
						<xsl:apply-templates select="leadership"/>
						<xsl:apply-templates select="feats"/>
						<xsl:apply-templates select="domains"/>
						<xsl:apply-templates select="weapon_proficiencies"/>
						<xsl:apply-templates select="languages"/>
						<xsl:apply-templates select="templates"/>
						<xsl:apply-templates select="prohibited_schools"/>
						<xsl:apply-templates select="companions"/>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
			<xsl:apply-templates select="spells"/>
			<xsl:apply-templates select="basics" mode="bio"/>
			<xsl:apply-templates select="basics/notes" mode="bio"/>
		</fo:root>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - CHARACTER HEADER
====================================
====================================-->
	<xsl:template match="basics">
		<!-- Character Header -->
		<fo:table table-layout="fixed" width="190mm">
			<xsl:choose>
				<xsl:when test="string-length(portrait) &gt; 0">
					<fo:table-column column-width="22mm"/>
					<fo:table-column column-width="2mm"/>
					<fo:table-column column-width="22mm"/>
					<fo:table-column column-width="2mm"/>
					<fo:table-column column-width="22mm"/>
					<fo:table-column column-width="2mm"/>
					<fo:table-column column-width="22mm"/>
					<fo:table-column column-width="2mm"/>
					<fo:table-column column-width="22mm"/>
					<fo:table-column column-width="2mm"/>
					<fo:table-column column-width="22mm"/>
					<fo:table-column column-width="2mm"/>
					<fo:table-column column-width="22mm"/>
					<fo:table-column column-width="2mm"/>
					<fo:table-column column-width="22mm"/>
				</xsl:when>
				<xsl:otherwise>
					<fo:table-column column-width="27mm"/>
					<!-- Class -->
					<fo:table-column column-width="2mm"/>
					<!--  -->
					<fo:table-column column-width="25mm"/>
					<!-- Experience -->
					<fo:table-column column-width="2mm"/>
					<!-- -->
					<fo:table-column column-width="25mm"/>
					<!-- Race -->
					<fo:table-column column-width="2mm"/>
					<!-- -->
					<fo:table-column column-width="25mm"/>
					<!-- Size -->
					<fo:table-column column-width="2mm"/>
					<!-- -->
					<fo:table-column column-width="25mm"/>
					<!-- Height -->
					<fo:table-column column-width="2mm"/>
					<!-- -->
					<fo:table-column column-width="24mm"/>
					<!-- Weight -->
					<fo:table-column column-width="2mm"/>
					<!-- -->
					<fo:table-column column-width="27mm"/>
					<!-- Vision -->
				</xsl:otherwise>
			</xsl:choose>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell number-columns-spanned="3">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="10pt" font-weight="bold">
							<xsl:value-of select="name"/>
							<xsl:if test="string-length(followerof) &gt; 0">	- <xsl:value-of select="followerof"/>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell number-columns-spanned="3" font-weight="bold">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="10pt">
							<xsl:value-of select="playername"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell padding-top="2.5pt" number-columns-spanned="3">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">
							<xsl:value-of select="deity/name"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell padding-top="2.5pt">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">
							<xsl:value-of select="alignment/long"/>
						</fo:block>
					</fo:table-cell>
					<xsl:if test="string-length(portrait) &gt; 0">
						<fo:table-cell/>
						<fo:table-cell number-rows-spanned="6">
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'picture'"/>
							</xsl:call-template>
							<fo:block>
								<xsl:variable name="portrait_file" select="portrait"/>
								<fo:external-graphic src="file:{$portrait_file}" width="22mm"/>
							</fo:block>
						</fo:table-cell>
					</xsl:if>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell number-columns-spanned="3">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">NAME</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell number-columns-spanned="3">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">PLAYERNAME</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell number-columns-spanned="3">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">DEITY</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">ALIGNMENT</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="8pt" padding-top="3pt">
							<xsl:value-of select="classes/shortform"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="8pt" padding-top="3pt">
							<xsl:value-of select="experience/current"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="8pt" padding-top="3pt">
							<xsl:value-of select="race"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="8pt" padding-top="3pt">
							<xsl:value-of select="size/long"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="8pt" padding-top="3pt"><xsl:value-of select="height/total"/></fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="8pt" padding-top="3pt">
							<xsl:value-of select="weight/weight_unit"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="8pt" padding-top="3pt">
							<xsl:value-of select="vision/all"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">CLASS</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">EXPERIENCE</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">RACE</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">SIZE</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">HEIGHT</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">WEIGHT</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">VISION</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="8pt" padding-top="3pt">
							<xsl:value-of select="classes/levels_total"/>
							<xsl:if test="classes/levels_total != classes/levels_ecl">/<xsl:value-of select="classes/levels_ecl"/>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="8pt" padding-top="3pt">
							<xsl:value-of select="experience/next_level"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="8pt" padding-top="3pt">
							<xsl:value-of select="age"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="8pt" padding-top="3pt">
							<xsl:value-of select="gender/long"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="8pt" padding-top="3pt">
							<xsl:value-of select="eyes/color"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="8pt" padding-top="3pt">
							<xsl:value-of select="hair/color"/>, <xsl:value-of select="hair/length"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<fo:block font-size="8pt" padding-top="3pt">
							<xsl:value-of select="poolpoints/cost"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">
							<xsl:text>Character Level</xsl:text>
							<xsl:if test="classes/levels_total != classes/levels_ecl">
								<xsl:text>ECL / </xsl:text>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">NEXT LEVEL</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">AGE</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">GENDER</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">EYES</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">HAIR</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" padding-top="1pt">POINTS</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - CLASS FEATURES

  Returns the size in MM the class
  features take up on the LHS of the
  first page
====================================
====================================-->
	<xsl:template name="features.left">
		<xsl:param name="features"/>
		<xsl:param name="RunningTotal" select="0"/>
		<xsl:choose>
			<xsl:when test="not($features)">
				<!--  No more Items so return Running Total -->
				<xsl:copy-of select="$RunningTotal"/>
			</xsl:when>
			<xsl:otherwise>
				<!--  Call template for remaining Items -->
				<xsl:variable name="ClassLength">
					<xsl:choose>
						<xsl:when test="name($features[1]) = 'rage'">25</xsl:when>
						<xsl:when test="name($features[1]) = 'wildshape'">25</xsl:when>
						<xsl:when test="name($features[1]) = 'stunning_fist'">14</xsl:when>
						<xsl:when test="name($features[1]) = 'wholeness_of_body'">14</xsl:when>
						<xsl:when test="name($features[1]) = 'psionics'">56</xsl:when>
						<xsl:when test="name($features[1]) = 'layonhands'">14</xsl:when>
						<xsl:otherwise>0</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:call-template name="features.left">
					<xsl:with-param name="features" select="$features[position() &gt; 1]"/>
					<xsl:with-param name="RunningTotal" select="$ClassLength + $RunningTotal"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - CLASS FEATURES

  Returns the size in MM the class
  features take up on the RHS of the
  first page
====================================
====================================-->
	<xsl:template name="features.right">
		<xsl:param name="features"/>
		<xsl:param name="RunningTotal" select="0"/>
		<xsl:choose>
			<xsl:when test="not($features)">
				<!--  No more Items so return Running Total -->
				<xsl:copy-of select="$RunningTotal"/>
			</xsl:when>
			<xsl:otherwise>
				<!--  Call template for remaining Items -->
				<xsl:variable name="ClassLength">
					<xsl:choose>
						<xsl:when test="name($features[1]) = 'bardic_music'">17</xsl:when>
						<xsl:when test="name($features[1]) = 'turning'">43</xsl:when>
						<xsl:otherwise>0</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:call-template name="features.right">
					<xsl:with-param name="features" select="$features[position() &gt; 1]"/>
					<xsl:with-param name="RunningTotal" select="$ClassLength + $RunningTotal"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - VIEW WEAPON NUMBER

	Returns the number of weapons that can
	be shown on the front page
====================================
====================================-->
	<xsl:template name="view.weapon.num">
		<xsl:variable name="featureheight">
			<xsl:call-template name="features.left">
				<xsl:with-param name="features" select="/character/class_features/*"/>
			</xsl:call-template>
		</xsl:variable>
		<!-- 143 is the number of mm available to weapons and features
         28mm is the size of a single large ranged weapon block
         20mm is the size of a single large weapon block
         24mm is the size of a single simple weapon block
		-->
		<!-- This should be made more complicated so that it determines the 
		     size of each weapon block in turn so that a correct cumulative
         height can be determined -->
		<!--
		This does not seem to work very well.

		<xsl:value-of select="floor( (140-$featureheight) div 28) "/>

		For now, just make it 3 weapons max.
		-->
		<xsl:value-of select="3"/>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - VIEW SKILLS NUMBER

	Returns the number of skills that can
	be shown on the front page
====================================
====================================-->
	<xsl:template name="view.skills.num">
		<xsl:variable name="featureheight">
			<xsl:call-template name="features.right">
				<xsl:with-param name="features" select="/character/class_features/*"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:value-of select="floor( (205-$featureheight) div 3.6) - 2"/>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - ABILITY BLOCK
====================================
====================================-->
	<xsl:template match="abilities">
		<!-- BEGIN Ability Block -->
		<fo:table table-layout="fixed">
			<fo:table-column column-width="9mm"/>
			<!-- 52mm total -->
			<fo:table-column column-width="1mm"/>
			<fo:table-column column-width="6mm"/>
			<fo:table-column column-width="1mm"/>
			<fo:table-column column-width="6mm"/>
			<fo:table-column column-width="1.5mm"/>
			<fo:table-column column-width="6mm"/>
			<fo:table-column column-width="1mm"/>
			<fo:table-column column-width="6mm"/>
			<fo:table-column column-width="1.5mm"/>
			<fo:table-column column-width="6mm"/>
			<fo:table-column column-width="1mm"/>
			<fo:table-column column-width="6mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">ABILITY NAME</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">BASE SCORE</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">BASE MOD</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">ABILITY SCORE</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">ABILITY MOD</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">TEMP SCORE</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">TEMP MOD</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:for-each select="ability">
					<fo:table-row>
						<fo:table-cell>
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'stat.title'"/>
							</xsl:call-template>
							<fo:block line-height="10pt" font-weight="bold" font-size="10pt" space-before="1pt">
								<xsl:value-of select="name/short"/>
							</fo:block>
							<fo:block line-height="4pt" font-size="4pt">
								<xsl:value-of select="name/long"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell/>
						<fo:table-cell>
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'stat.base.score'"/>
							</xsl:call-template>
							<fo:block space-before.optimum="2pt" font-size="10pt">
								<xsl:value-of select="base"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell/>
						<fo:table-cell>
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'stat.base.modifier'"/>
							</xsl:call-template>
							<fo:block space-before.optimum="2pt" font-size="10pt">
								<xsl:value-of select="basemod"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell/>
						<fo:table-cell>
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'stat.score'"/>
							</xsl:call-template>
							<fo:block space-before.optimum="2pt" font-size="10pt">
								<xsl:value-of select="no_temp_score"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell/>
						<fo:table-cell>
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'stat.modifier'"/>
							</xsl:call-template>
							<fo:block space-before.optimum="2pt" font-size="10pt">
								<xsl:value-of select="no_temp_modifier"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell/>
						<fo:table-cell height="4pt">
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'stat.temp.score'"/>
							</xsl:call-template>
							<fo:block space-before.optimum="2pt" font-size="10pt">
								<xsl:value-of select="score"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell/>
						<fo:table-cell height="4pt">
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'stat.temp.modifier'"/>
							</xsl:call-template>
							<fo:block space-before.optimum="2pt" font-size="10pt">
								<xsl:value-of select="modifier"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row height="2pt"/>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
		<!-- END Ability Block -->
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - HP 
====================================
====================================-->
	<xsl:template match="character" mode="hp_table">
		<fo:table table-layout="fixed">
			<xsl:choose>
				<xsl:when test="hit_points/usealternatedamage = 0">
					<fo:table-column column-width="12mm"/>
					<!-- TITLE -->
					<fo:table-column column-width="2mm"/>
					<!-- space -->
					<fo:table-column column-width="8mm"/>
					<!-- TOTAL HP -->
					<fo:table-column column-width="2mm"/>
					<!-- space -->
					<fo:table-column column-width="30.5mm"/>
					<!-- WOUNDS -->
					<fo:table-column column-width="2mm"/>
					<!-- space  -->
					<fo:table-column column-width="28.5mm"/>
					<!-- SUBDUAL -->
					<fo:table-column column-width="2mm"/>
					<!-- space -->
					<fo:table-column column-width="18mm"/>
					<!-- DR -->
					<fo:table-column column-width="3mm"/>
					<!-- space -->
					<fo:table-column column-width="27mm"/>
					<!-- SPEED -->
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell/>
							<fo:table-cell/>
							<fo:table-cell>
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell/>
							<fo:table-cell display-align="after">
								<fo:block text-align="center" font-size="4pt">WOUNDS/CURRENT HP</fo:block>
							</fo:table-cell>
							<fo:table-cell/>
							<fo:table-cell display-align="after">
								<fo:block text-align="center" font-size="4pt">SUBDUAL DAMAGE</fo:block>
							</fo:table-cell>
							<fo:table-cell/>
							<fo:table-cell display-align="after">
								<fo:block text-align="center" font-size="4pt">DAMAGE REDUCTION</fo:block>
							</fo:table-cell>
							<fo:table-cell/>
							<fo:table-cell display-align="after">
								<fo:block text-align="center" font-size="6pt">SPEED</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell>
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'hp.title'"/>
								</xsl:call-template>
								<fo:block line-height="10pt" font-weight="bold" font-size="10pt" space-before="1pt">HP</fo:block>
								<fo:block line-height="4pt" font-size="4pt">hit points</fo:block>
							</fo:table-cell>
							<fo:table-cell/>
							<fo:table-cell>
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'hp.total'"/>
								</xsl:call-template>
								<fo:block space-before.optimum="2pt" font-size="10pt">
									<xsl:value-of select="hit_points/points"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell/>
							<fo:table-cell display-align="center">
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'hp.current'"/>
								</xsl:call-template>
								<fo:block font-size="10pt"/>
							</fo:table-cell>
							<fo:table-cell/>
							<fo:table-cell display-align="center">
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'hp.subdual'"/>
								</xsl:call-template>
								<fo:block font-size="10pt"/>
							</fo:table-cell>
							<fo:table-cell/>
							<fo:table-cell display-align="center">
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'damage.reduction'"/>
								</xsl:call-template>
								<fo:block font-size="8pt">
									<xsl:value-of select="hit_points/damage_reduction"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell/>
							<fo:table-cell display-align="center">
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'speed'"/>
								</xsl:call-template>
								<fo:block font-size="8pt">
									<xsl:value-of select="basics/move/all"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</xsl:when>
				<xsl:otherwise>
					<fo:table-column column-width="12mm"/><!-- TITLE Vitality -->
					<fo:table-column column-width="2mm"/><!-- space -->
					<fo:table-column column-width="8mm"/><!-- TOTAL Vitality -->
					<fo:table-column column-width="2mm"/><!-- space -->
					<fo:table-column column-width="18mm"/><!-- WOUNDS -->
					<fo:table-column column-width="2mm"/><!-- space  -->
					<fo:table-column column-width="18mm"/><!-- SUBDUAL -->
					<fo:table-column column-width="2mm"/><!-- space -->
					<fo:table-column column-width="12mm"/><!-- TITLE Wound points-->
					<fo:table-column column-width="2mm"/><!-- space -->
					<fo:table-column column-width="8mm"/><!-- TOTAL Wound points -->
					<fo:table-column column-width="2mm"/><!-- space -->
					<fo:table-column column-width="18mm"/><!-- DR -->
					<fo:table-column column-width="2mm"/><!-- space -->
					<fo:table-column column-width="27mm"/><!-- SPEED -->
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell/><!-- TITLE Vitality -->
							<fo:table-cell/><!-- space -->
							<fo:table-cell>	<!-- TOTAL Vitality -->
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell/><!-- space -->
							<fo:table-cell display-align="after">
								<fo:block text-align="center" font-size="4pt">WOUNDS/CURRENT HP</fo:block>
							</fo:table-cell>
							<fo:table-cell/><!-- space -->
							<fo:table-cell display-align="after">
								<fo:block text-align="center" font-size="4pt">SUBDUAL DAMAGE</fo:block>
							</fo:table-cell>
							<fo:table-cell/><!-- space -->
							<fo:table-cell/><!-- TITLE Wound points -->
							<fo:table-cell/><!-- space -->
							<fo:table-cell>	<!-- TOTAL Wound points -->
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell/><!-- space -->
							<fo:table-cell display-align="after">
								<fo:block text-align="center" font-size="4pt">DAMAGE REDUCTION</fo:block>
							</fo:table-cell>
							<fo:table-cell/><!-- space -->
							<fo:table-cell display-align="after">
								<fo:block text-align="center" font-size="6pt">SPEED</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell>
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'hp.title'"/>
								</xsl:call-template>
								<fo:block line-height="10pt" font-weight="bold" font-size="10pt" space-before="1pt">VP</fo:block>
								<fo:block line-height="4pt" font-size="4pt">Vitality</fo:block>
							</fo:table-cell>
							<fo:table-cell/><!-- space -->
							<fo:table-cell>
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'hp.total'"/>
								</xsl:call-template>
								<fo:block space-before.optimum="2pt" font-size="10pt">
									<xsl:value-of select="hit_points/points"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell/><!-- space -->
							<fo:table-cell display-align="center">
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'hp.current'"/>
								</xsl:call-template>
								<fo:block font-size="10pt"/>
							</fo:table-cell>
							<fo:table-cell/><!-- space -->
							<fo:table-cell display-align="center">
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'hp.subdual'"/>
								</xsl:call-template>
								<fo:block font-size="10pt"/>
							</fo:table-cell>
							<fo:table-cell/><!-- space -->
							<fo:table-cell>
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'hp.title'"/>
								</xsl:call-template>
								<fo:block line-height="10pt" font-weight="bold" font-size="10pt" space-before="1pt">WP</fo:block>
								<fo:block line-height="4pt" font-size="4pt">Wound Points</fo:block>
							</fo:table-cell>
							<fo:table-cell/><!-- space -->
							<fo:table-cell>
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'hp.total'"/>
								</xsl:call-template>
								<fo:block space-before.optimum="2pt" font-size="10pt">
									<xsl:value-of select="hit_points/alternate"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell/><!-- space -->
							<fo:table-cell display-align="center">
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'damage.reduction'"/>
								</xsl:call-template>
								<fo:block font-size="8pt">
									<xsl:value-of select="hit_points/damage_reduction"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell/><!-- space -->
							<fo:table-cell display-align="center">
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'speed'"/>
								</xsl:call-template>
								<fo:block font-size="8pt">
									<xsl:value-of select="basics/move/all"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</xsl:otherwise>
			</xsl:choose>
		</fo:table>
		<!-- END HP Table -->
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - AC TABLE
====================================
====================================-->
	<xsl:template match="armor_class">
		<fo:table table-layout="fixed" space-before="2pt">
			<fo:table-column column-width="12mm"/>			<!-- TITLE -->
			<fo:table-column column-width="2mm"/>			<!-- space -->
			<fo:table-column column-width="8mm"/>			<!-- TOTAL AC -->
			<fo:table-column column-width="2mm"/>			<!-- : -->
			<fo:table-column column-width="8mm"/>			<!-- FLAT -->
			<fo:table-column column-width="2mm"/>			<!-- : -->
			<fo:table-column column-width="8mm"/>			<!-- TOUCH -->
			<fo:table-column column-width="2mm"/>			<!-- = -->
			<fo:table-column column-width="7mm"/>			<!-- BASE -->
			<fo:table-column column-width="2mm"/>			<!-- + -->
			<fo:table-column column-width="6mm"/>			<!-- armour -->
			<fo:table-column column-width="2mm"/>			<!-- + -->
			<fo:table-column column-width="6mm"/>			<!-- armour -->
			<fo:table-column column-width="2mm"/>			<!-- + -->
			<fo:table-column column-width="6mm"/>			<!-- stat -->
			<fo:table-column column-width="2mm"/>			<!-- + -->
			<fo:table-column column-width="5mm"/>			<!--  size -->
			<fo:table-column column-width="2mm"/>			<!-- + -->
			<fo:table-column column-width="5mm"/>			<!-- natural armour-->
			<fo:table-column column-width="2mm"/>			<!-- + -->
			<fo:table-column column-width="5mm"/>			<!-- misc   -->
			<fo:table-column column-width="4mm"/>			<!-- space -->
			<fo:table-column column-width="7mm"/>			<!-- miss chance -->
			<fo:table-column column-width="3mm"/>			<!-- space -->
			<fo:table-column column-width="7mm"/>			<!-- arcane spell failure -->
			<fo:table-column column-width="2mm"/>			<!-- space -->
			<fo:table-column column-width="7mm"/>			<!-- armour check-->
			<fo:table-column column-width="2mm"/>			<!-- space -->
			<fo:table-column column-width="7mm"/>			<!-- SR -->
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'ac.title'"/>
						</xsl:call-template>
						<fo:block line-height="10pt" font-weight="bold" font-size="10pt" space-before="1pt">AC</fo:block>
						<fo:block line-height="4pt" font-size="4pt">armor class</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell display-align="center">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'ac.total'"/>
						</xsl:call-template>
						<fo:block font-size="10pt">
							<xsl:value-of select="total"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<fo:block text-align="center" font-size="6pt">:</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'ac.flatfooted'"/>
						</xsl:call-template>
						<fo:block font-size="10pt">
							<xsl:value-of select="flat"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<fo:block text-align="center" font-size="6pt">:</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'ac.touch'"/>
						</xsl:call-template>
						<fo:block font-size="10pt">
							<xsl:value-of select="touch"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<fo:block text-align="center" font-size="6pt">=</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'ac'"/>
						</xsl:call-template>
						<fo:block font-size="10pt">
							<xsl:value-of select="base"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<fo:block text-align="center" font-size="6pt">+</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'ac'"/>
						</xsl:call-template>
						<fo:block font-size="10pt">
							<xsl:value-of select="armor_bonus"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<fo:block text-align="center" font-size="6pt">+</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'ac'"/>
						</xsl:call-template>
						<fo:block font-size="10pt">
							<xsl:value-of select="shield_bonus"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<fo:block text-align="center" font-size="6pt">+</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'ac'"/>
						</xsl:call-template>
						<fo:block font-size="10pt">
							<xsl:value-of select="stat_mod"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<fo:block text-align="center" font-size="6pt">+</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'ac'"/>
						</xsl:call-template>
						<fo:block font-size="10pt">
							<xsl:value-of select="size_mod"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<fo:block text-align="center" font-size="6pt">+</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'ac'"/>
						</xsl:call-template>
						<fo:block font-size="10pt">
							<xsl:value-of select="natural"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<fo:block text-align="center" font-size="6pt">+</fo:block>
					</fo:table-cell>
					<fo:table-cell display-align="center">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'ac'"/>
						</xsl:call-template>
						<fo:block text-align="center" font-size="10pt">
							<xsl:value-of select="misc + class_bonus + dodge_bonus"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell display-align="center">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'miss_chance'"/>
						</xsl:call-template>
						<fo:block font-size="10pt">
							<!-- Miss chance -->
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell display-align="center">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'spell_failure'"/>
						</xsl:call-template>
						<fo:block font-size="10pt">
							<xsl:value-of select="spell_failure"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell display-align="center">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'ac_check'"/>
						</xsl:call-template>
						<fo:block font-size="10pt">
							<xsl:value-of select="check_penalty"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell display-align="center">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'spell_resistance'"/>
						</xsl:call-template>
						<fo:block font-size="10pt">
							<xsl:value-of select="spell_resistance"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row height="0.5pt"/>
				<fo:table-row>
					<fo:table-cell/>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="6pt">TOTAL</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="6pt">FLAT</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="6pt">TOUCH</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">BASE</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">ARMOR BONUS</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">SHIELD BONUS</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">STAT</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">SIZE</fo:block>
					</fo:table-cell>
					<fo:table-cell number-columns-spanned="3">
						<fo:block text-align="center" font-size="4pt">NATURAL</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">MISC</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">MISS CHANCE</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">ARCANE SPELL FAILURE</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">ARMOR CHECK PENALTY</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">SPELL RESIST</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<!-- END AC Table -->
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - Initiative TABLE
====================================
====================================-->
	<xsl:template match="initiative">
		<!-- BEGIN ini-base table -->
		<fo:table table-layout="fixed">
			<!-- 47mm -->
			<fo:table-column column-width="20mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="7mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="7mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="7mm"/>
			<fo:table-body>
				<fo:table-row height="2pt"/>
				<fo:table-row>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'initiative.title'"/>
						</xsl:call-template>
						<fo:block line-height="10pt" font-weight="bold" font-size="10pt" space-before="1pt">INITIATIVE</fo:block>
						<fo:block line-height="4pt" font-size="4pt">modifier</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'initiative.total'"/>
						</xsl:call-template>
						<fo:block space-before.optimum="2pt" font-size="10pt">
							<xsl:value-of select="total"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block text-align="center" space-before.optimum="5pt" font-size="6pt">=</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'initiative.general'"/>
						</xsl:call-template>
						<fo:block space-before.optimum="2pt" font-size="10pt">
							<xsl:value-of select="dex_mod"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block text-align="center" space-before.optimum="5pt" font-size="6pt">+</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'initiative.general'"/>
						</xsl:call-template>
						<fo:block space-before.optimum="2pt" font-size="10pt">
							<xsl:value-of select="misc_mod"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row height="0.5pt"/>
				<fo:table-row>
					<fo:table-cell/>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" space-before.optimum="1pt" font-size="6pt">TOTAL</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">DEX MODIFIER</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">MISC MODIFIER</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<!-- END ini-base table -->
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - Base Attack TABLE
====================================
====================================-->
	<xsl:template match="bab" mode="bab">
		<!-- BEGIN ini-base table -->
		<fo:table table-layout="fixed">
			<!-- 47mm -->
			<fo:table-column column-width="20mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="25mm"/>
			<fo:table-body>
				<fo:table-row height="2pt"/>
				<fo:table-row>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bab.title'"/>
						</xsl:call-template>
						<fo:block line-height="10pt" font-weight="bold" font-size="7.5pt">BASE ATTACK</fo:block>
						<fo:block line-height="4pt" font-size="4pt">bonus</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bab.total'"/>
						</xsl:call-template>
						<fo:block space-before.optimum="2pt" font-size="10pt">
							<xsl:call-template name="process.attack.string">
								<xsl:with-param name="bab" select="."/>
							</xsl:call-template>
							<!--xsl:value-of select="../../attack/melee/base_attack_bonus"/-->
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<!-- END ini-base table -->
	</xsl:template>

	<xsl:template name="skills.empty">
		<xsl:param name="pos"/>

		<xsl:variable name="shade">
			<xsl:choose>
				<xsl:when test="$pos mod 2 = 0">darkline</xsl:when>
				<xsl:otherwise>lightline</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<fo:table-row height="9pt">
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat('skills.', $shade)"/></xsl:call-template>
			<fo:table-cell/>
			<fo:table-cell/>
			<fo:table-cell number-columns-spanned="2"/>
			<fo:table-cell/>
			<fo:table-cell number-columns-spanned="2"/>
			<fo:table-cell>
				<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat('skills.', $shade, '.total')"/></xsl:call-template>
			</fo:table-cell>
			<fo:table-cell number-columns-spanned="2">
				<fo:block text-align="center" space-before.optimum="3pt" line-height="6pt" font-size="6pt">=</fo:block>
			</fo:table-cell>
			<fo:table-cell/>
			<fo:table-cell number-columns-spanned="2">
				<fo:block text-align="center" space-before.optimum="3pt" line-height="6pt" font-size="6pt">+</fo:block>
			</fo:table-cell>
			<fo:table-cell/>
			<fo:table-cell number-columns-spanned="2">
				<fo:block text-align="center" space-before.optimum="3pt" line-height="6pt" font-size="6pt">+</fo:block>
			</fo:table-cell>
			<fo:table-cell/>
		</fo:table-row>
	</xsl:template>

	<!--
====================================
====================================
	TEMPLATE - SKILLS TABLE
====================================
====================================-->
	<xsl:template match="skills">
		<xsl:param name="first_skill" select="0"/>
		<xsl:param name="last_skill" select="0"/>
		<xsl:param name="column_width" select="'wide'"/>
		<!-- begin skills table -->
		<xsl:if test="count(skill) &gt;= $first_skill">
			<xsl:variable name="columns">
				<xsl:choose>
					<xsl:when test="$column_width='wide'">
						<fo:table-column column-width="4mm"/>
						<fo:table-column column-width="38mm"/>
						<fo:table-column column-width="3mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="8mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="8mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="8mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="8mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="8mm"/>
					</xsl:when>
					<xsl:otherwise>
						<fo:table-column column-width="4mm"/>
						<fo:table-column column-width="38mm"/>
						<fo:table-column column-width="2mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="7mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="7mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="6mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="6mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="1mm"/>
						<fo:table-column column-width="7mm"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>

			<fo:table table-layout="fixed" border-collapse="collapse" padding="0.5pt">
				<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'skills.border'"/></xsl:call-template>
				<xsl:copy-of select="$columns"/>
				<fo:table-body>
					<fo:table-row height="2pt"/>
					<fo:table-row>
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'skills.header'"/></xsl:call-template>
						<fo:table-cell />
						<fo:table-cell number-columns-spanned="6">
							<fo:block text-align="end" line-height="10pt" font-weight="bold" font-size="10pt">SKILLS</fo:block>
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="8">
							<fo:block text-align="end" space-before.optimum="4pt" line-height="4pt" font-size="4pt">
								MAX RANKS
							</fo:block>
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="2">
							<fo:table table-layout="fixed" space-before.optimum="0.2mm">
								<fo:table-column column-width="7.625mm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell>
											<fo:block space-before.optimum="1pt" line-height="8pt" font-size="6pt">
												<xsl:value-of select="max_class_skill_level"/>/<xsl:value-of select="max_cross_class_skill_level"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'skills.header'"/></xsl:call-template>
						<fo:table-cell />
						<fo:table-cell number-columns-spanned="2">
							<fo:block font-weight="bold" font-size="8pt">
								SKILL NAME
							</fo:block>
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="3">
							<fo:block font-size="4pt">
								KEY ABILITY
							</fo:block>
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="3">
							<fo:block text-align="center" font-size="4pt">
								SKILL MODIFIER
							</fo:block>
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="3">
							<fo:block text-align="center" font-size="4pt">
								ABILITY MODIFIER
							</fo:block>
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="3">
							<fo:block text-align="center" space-before.optimum="5pt" font-size="4pt">
								RANKS
							</fo:block>
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="2">
							<fo:block text-align="center" font-size="4pt">
								MISC MODIFIER
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			
			
			
			<fo:table table-layout="fixed" border-collapse="collapse" padding="0.5pt">
				<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'skills.border'"/></xsl:call-template>
				<xsl:copy-of select="$columns"/>
				<fo:table-body>
					<xsl:for-each select="skill">
						<xsl:if test="position() &gt;= $first_skill and position() &lt;= $last_skill">
							<xsl:variable name="shade">
								<xsl:choose>
									<xsl:when test="position() mod 2 = 0">darkline</xsl:when>
									<xsl:otherwise>lightline</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<fo:table-row>
								<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat('skills.', $shade)"/></xsl:call-template>
								<fo:table-cell>
									<fo:block font-size="6pt" font-family="ZapfDingbats">
										<xsl:if test="translate( substring(untrained,1,1), 'Y', 'y')='y'">
											&#x2713;
										</xsl:if>
										<xsl:if test="translate( substring(exclusive,1,1), 'Y', 'y')='y'">
											&#x2717;
										</xsl:if>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block space-before.optimum="1pt" font-size="8pt">
										<xsl:value-of select="name"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell number-columns-spanned="2"/>
								<fo:table-cell>
									<fo:block space-before.optimum="1pt" font-size="8pt">
										<xsl:value-of select="ability"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell number-columns-spanned="2"/>
								<fo:table-cell>
									<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat('skills.', $shade, '.total')"/></xsl:call-template>
									<fo:block text-align="center" space-before.optimum="1pt" font-size="8pt">
										<xsl:value-of select="skill_mod"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell number-columns-spanned="2">
									<fo:block text-align="center" space-before.optimum="3pt" line-height="6pt" font-size="6pt">=</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="center" space-before.optimum="1pt" font-size="8pt">
										<xsl:value-of select="ability_mod"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell number-columns-spanned="2">
									<fo:block text-align="center" space-before.optimum="3pt" line-height="6pt" font-size="6pt">+</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="center" space-before.optimum="1pt" font-size="8pt">
										<xsl:if test="ranks>0">
											<xsl:value-of select="ranks"/>
										</xsl:if>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell number-columns-spanned="2">
									<fo:block text-align="center" space-before.optimum="3pt" line-height="6pt" font-size="6pt">+</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="center" space-before.optimum="1pt" font-size="8pt">
										<xsl:if test="misc_mod!=0">
											<xsl:value-of select="misc_mod"/>
										</xsl:if>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:if>
					</xsl:for-each>
					<xsl:call-template name="skills.empty"><xsl:with-param name="pos" select="count(skill)+1"/></xsl:call-template>
					<xsl:call-template name="skills.empty"><xsl:with-param name="pos" select="count(skill)+2"/></xsl:call-template>
					<fo:table-row>
						<fo:table-cell number-columns-spanned="17" padding-top="1pt">
							<fo:block text-align="center" font-size="6pt">
								&#x2713;: can be used untrained.
								&#x2717;: exclusive skills
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</xsl:if>
		<!-- END Skills table -->
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - SAVES TABLE
====================================
====================================-->
	<xsl:template match="saving_throws">
		<!-- BEGIN Saves table -->
		<fo:table table-layout="fixed" space-before="2mm">
			<fo:table-column column-width="79mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="21mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<xsl:apply-templates select="." mode="saves"/>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell padding-start="1pt">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'border'"/>
						</xsl:call-template>
						<fo:block font-size="4pt">conditional modifiers</fo:block>
						<fo:block font-size="4pt">
							<xsl:value-of select="conditional_modifiers"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<!-- END Saves table -->
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - SAVES TABLE
====================================
====================================-->
	<xsl:template match="saving_throws" mode="saves">
		<!-- BEGIN Saves table -->
		<fo:table table-layout="fixed">
			<fo:table-column column-width="25mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="6mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="6mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="6mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="6mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="5mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="5mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="5mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block text-align="center" space-before.optimum="1pt" font-size="6pt">SAVING THROWS</fo:block>
					</fo:table-cell>
					<fo:table-cell number-columns-spanned="3">
						<fo:block text-align="center" space-before.optimum="1pt" font-size="6pt">TOTAL</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">BASE SAVE</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">ABILITY</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">MAGIC</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">MISC</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">EPIC</fo:block>
					</fo:table-cell>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block text-align="center" font-size="4pt">TEMP</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:for-each select="saving_throw">
					<fo:table-row space-before="2pt">
						<fo:table-cell>
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'saves.title'"/>
							</xsl:call-template>
							<fo:block line-height="10pt" font-weight="bold" font-size="10pt" space-before="1pt">
								<xsl:value-of select="translate(name/long, 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
							</fo:block>
							<fo:block line-height="4pt" font-size="4pt">(<xsl:value-of select="ability"/>)</fo:block>
						</fo:table-cell>
						<fo:table-cell/>
						<fo:table-cell>
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'saves.total'"/>
							</xsl:call-template>
							<fo:block space-before.optimum="2pt" font-size="10pt">
								<xsl:value-of select="total"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block text-align="center" space-before.optimum="5pt" font-size="6pt">=</fo:block>
						</fo:table-cell>
						<xsl:call-template name="saves.entry"><xsl:with-param name="value" select="base"/></xsl:call-template>
						<xsl:call-template name="saves.entry"><xsl:with-param name="value" select="abil_mod"/></xsl:call-template>
						<xsl:call-template name="saves.entry"><xsl:with-param name="value" select="magic_mod"/></xsl:call-template>
						<xsl:call-template name="saves.entry"><xsl:with-param name="value" select="misc_mod"/></xsl:call-template>
						<xsl:call-template name="saves.entry"><xsl:with-param name="value" select="epic_mod"/></xsl:call-template>
						<fo:table-cell>
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'border.temp'"/>
							</xsl:call-template>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<xsl:template name="saves.entry">
		<xsl:param name="value"/>		
		<fo:table-cell>
			<xsl:call-template name="attrib">
				<xsl:with-param name="attribute" select="'saves'"/>
			</xsl:call-template>
			<fo:block space-before.optimum="2pt" font-size="10pt">
				<xsl:value-of select="$value"/>
			</fo:block>
		</fo:table-cell>
		<fo:table-cell>
			<fo:block text-align="center" space-before.optimum="5pt" font-size="6pt">+</fo:block>
		</fo:table-cell>
	</xsl:template>

	<!--
====================================
====================================
	TEMPLATE - ATTACK TABLE
====================================
====================================-->
	<xsl:template match="attack" mode="ranged_melee">
		<!-- BEGIN Attack table -->
		<fo:table table-layout="fixed" space-before="2mm">
			<fo:table-column column-width="17mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="22mm"/>
			<fo:table-column column-width="3mm"/>
			<fo:table-column column-width="21mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="6mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="6mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="6mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="5mm"/>
			<fo:table-column column-width="2mm"/>
			<fo:table-column column-width="5mm"/>
			<fo:table-body>
				<xsl:call-template name="to_hit.header" />
				<xsl:apply-templates select="melee" mode="to_hit">
					<xsl:with-param name="title" select="'MELEE'"/>
				</xsl:apply-templates>
				<fo:table-row height="2.5pt"/>
				<xsl:apply-templates select="ranged" mode="to_hit">
					<xsl:with-param name="title" select="'RANGED'"/>
				</xsl:apply-templates>
				<fo:table-row height="2.5pt"/>
				<xsl:apply-templates select="grapple" mode="to_hit">
					<xsl:with-param name="title" select="'GRAPPLE'"/>
				</xsl:apply-templates>
			</fo:table-body>
		</fo:table>
		<!-- END Attack table -->
	</xsl:template>
	<xsl:template name="to_hit.header">
		<xsl:param name="dalign" select="'after'"/>
		<fo:table-row>
			<fo:table-cell/>
			<xsl:call-template name="attack.header.entry"><xsl:with-param name="title" select="'TOTAL'"/><xsl:with-param name="font.size" select="'6pt'"/></xsl:call-template>
			<xsl:call-template name="attack.header.entry"><xsl:with-param name="title" select="'BASE ATTACK BONUS'"/></xsl:call-template>
			<xsl:call-template name="attack.header.entry"><xsl:with-param name="title" select="'STAT'"/></xsl:call-template>
			<xsl:call-template name="attack.header.entry"><xsl:with-param name="title" select="'SIZE'"/></xsl:call-template>
			<xsl:call-template name="attack.header.entry"><xsl:with-param name="title" select="'MISC'"/></xsl:call-template>
			<xsl:call-template name="attack.header.entry"><xsl:with-param name="title" select="'EPIC'"/></xsl:call-template>
			<xsl:call-template name="attack.header.entry"><xsl:with-param name="title" select="'TEMP'"/></xsl:call-template>
		</fo:table-row>
	</xsl:template>
	<xsl:template name="attack.header.entry">
		<xsl:param name="title"/>
		<xsl:param name="font.size" select="'4pt'"/>
		<fo:table-cell/>
		<fo:table-cell display-align="after">
			<fo:block text-align="center" font-size="6pt">
				<xsl:attribute name="font-size"><xsl:value-of select="$font.size"/></xsl:attribute>
				<xsl:value-of select="$title"/>
			</fo:block>
		</fo:table-cell>		
	</xsl:template>
	
	<xsl:template match="melee|ranged|grapple" mode="to_hit">
		<xsl:param name="title"/>
		<fo:table-row>
			<fo:table-cell>
				<xsl:call-template name="attrib">
					<xsl:with-param name="attribute" select="'tohit.title'"/>
				</xsl:call-template>
				<fo:block space-before.optimum="0.5pt" line-height="10pt" font-weight="bold" font-size="10pt" space-before="1pt">
					<xsl:value-of select="$title"/>
				</fo:block>
				<fo:block line-height="4pt" font-size="4pt">attack bonus</fo:block>
			</fo:table-cell>
			<fo:table-cell/>
			<xsl:call-template name="attack.entry"><xsl:with-param name="value" select="total"/><xsl:with-param name="separator" select="'='"/></xsl:call-template>
			<xsl:call-template name="attack.entry"><xsl:with-param name="value" select="base_attack_bonus"/></xsl:call-template>
			<xsl:call-template name="attack.entry"><xsl:with-param name="value" select="stat_mod"/></xsl:call-template>
			<xsl:call-template name="attack.entry"><xsl:with-param name="value" select="size_mod"/></xsl:call-template>
			<xsl:call-template name="attack.entry"><xsl:with-param name="value" select="misc_mod"/></xsl:call-template>
			<xsl:call-template name="attack.entry"><xsl:with-param name="value" select="epic_mod"/></xsl:call-template>
			<fo:table-cell>
				<xsl:call-template name="attrib">
					<xsl:with-param name="attribute" select="'border.temp'"/>
				</xsl:call-template>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
	
	<xsl:template name="attack.entry">
		<xsl:param name="value" />
		<xsl:param name="separator" select="'+'"/>
		<fo:table-cell>
			<xsl:call-template name="attrib">
				<xsl:with-param name="attribute" select="'tohit'"/>
			</xsl:call-template>
			<fo:block space-before.optimum="3pt" font-size="8pt">
				<xsl:value-of select="$value"/>
			</fo:block>
		</fo:table-cell>
		<fo:table-cell border-bottom="0pt" border-top="0pt">
			<xsl:call-template name="attrib">
				<xsl:with-param name="attribute" select="'tohit'"/>
			</xsl:call-template>
			<fo:block space-before.optimum="5pt" font-size="6pt">
				<xsl:value-of select="$separator"/>
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	
	
	<!--
====================================
====================================
	TEMPLATE - Unarmed ATTACK TABLE
====================================
====================================-->
	<xsl:template match="weapons/unarmed">
		<!-- START Unarmed Attack Table -->
		<fo:table table-layout="fixed" space-before="2mm">
			<fo:table-column column-width="35mm"/>
			<fo:table-column column-width="35mm"/>
			<fo:table-column column-width="16mm"/>
			<fo:table-column column-width="16mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell number-rows-spanned="2">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.title'"/>
						</xsl:call-template>
						<fo:block font-weight="bold" font-size="10pt">UNARMED</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt">TOTAL ATTACK BONUS</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt">DAMAGE</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt">CRITICAL</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.hilight'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">
							<xsl:value-of select="total"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.hilight'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">
							<xsl:value-of select="damage"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.hilight'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">
							<xsl:value-of select="critical"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<!-- STOP Unarmed Attack Table -->
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - First 3 weapons
====================================
====================================-->
	<xsl:template match="weapons">
		<xsl:param name="first_weapon" select="0"/>
		<xsl:param name="last_weapon" select="0"/>
		<xsl:param name="column_width" select="'wide'"/>
		<xsl:for-each select="weapon">
			<xsl:if test="(position() &gt;= $first_weapon) and (position() &lt;= $last_weapon)">
				<xsl:apply-templates select="common">
					<xsl:with-param name="column_width" select="$column_width"/>
				</xsl:apply-templates>
				<xsl:apply-templates select="melee">
					<xsl:with-param name="column_width" select="$column_width"/>
				</xsl:apply-templates>
				<xsl:apply-templates select="ranges">
					<xsl:with-param name="column_width" select="$column_width"/>
				</xsl:apply-templates>
				<xsl:apply-templates select="simple">
					<xsl:with-param name="column_width" select="$column_width"/>
				</xsl:apply-templates>
				<xsl:apply-templates select="common" mode="special_properties">
					<xsl:with-param name="column_width" select="$column_width"/>
				</xsl:apply-templates>
			</xsl:if>
		</xsl:for-each>
		<xsl:if test="position() &gt;= $first_weapon">
			<fo:block font-size="5pt" space-before="1pt" color="black">
				<fo:inline font-weight="bold">*</fo:inline>: weapon is equipped
			</fo:block>
			<fo:block font-size="5pt" space-before="1pt" color="black">
				<fo:inline font-weight="bold">1H-P</fo:inline>: One handed, in primary hand.
				<fo:inline font-weight="bold">1H-O</fo:inline>: One handed, in off hand.
				<fo:inline font-weight="bold">2H</fo:inline>: Two handed.
				<fo:inline font-weight="bold">2W-P-(OH)</fo:inline>: 2 weapons, primary hand (off hand weapon is heavy).
				<fo:inline font-weight="bold">2W-P-(OL)</fo:inline>: 2 weapons, primary hand (off hand weapon is light).
				<fo:inline font-weight="bold">2W-OH</fo:inline>: 2 weapons, off hand.
			</fo:block>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - Weapons - common
====================================
====================================-->
	<xsl:template match="common">
		<xsl:param name="column_width" select="'wide'"/>
		<fo:table table-layout="fixed" space-before="2mm" keep-with-next="always" keep-together="always">
			<xsl:choose>
				<xsl:when test="$column_width='wide'">
					<fo:table-column column-width="50mm"/>
					<fo:table-column column-width="21mm"/>
					<fo:table-column column-width="9mm"/>
					<fo:table-column column-width="9mm"/>
					<fo:table-column column-width="13mm"/>
				</xsl:when>
				<xsl:otherwise>
					<fo:table-column column-width="47mm"/>
					<fo:table-column column-width="19mm"/>
					<fo:table-column column-width="8mm"/>
					<fo:table-column column-width="8mm"/>
					<fo:table-column column-width="12mm"/>
				</xsl:otherwise>
			</xsl:choose>
			<fo:table-body>
				<fo:table-row keep-with-next.within-column="always">
					<!-- Name row (including Hand, Type, Size and Crit -->
					<fo:table-cell number-rows-spanned="2">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.title'"/>
						</xsl:call-template>
						<fo:block font-weight="bold" font-size="10pt">
							<xsl:variable name="name" select="substring-before(name/short,'(')"/>
							<xsl:variable name="description" select="substring-after(name/short,'(')"/>
							<xsl:value-of select="$name"/>
							<xsl:if test="string-length($name) = 0">
								<xsl:value-of select="name/short"/>
							</xsl:if>
							<xsl:if test="string-length($description) &gt; 0">
								<fo:inline font-size="6pt">
									<xsl:text>(</xsl:text>
									<xsl:value-of select="$description"/>
								</fo:inline>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt">CURRENT HAND</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt">TYPE</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt">SIZE</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt">CRITICAL</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row keep-with-next.within-column="always">
					<!-- Hand, Type, Size and Crit -->
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon'"/>
						</xsl:call-template>
						<fo:block font-size="7pt">
							<xsl:value-of select="hand"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon'"/>
						</xsl:call-template>
						<fo:block font-size="7pt">
							<xsl:value-of select="type"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon'"/>
						</xsl:call-template>
						<fo:block font-size="7pt">
							<xsl:value-of select="size"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.hilight'"/>
						</xsl:call-template>
						<fo:block font-size="7pt">
							<xsl:value-of select="critical/range"/>
							<xsl:text>/x</xsl:text>
							<xsl:value-of select="critical/multiplier"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - weapons - special properties
====================================
====================================-->
	<xsl:template match="common" mode="special_properties">
		<xsl:param name="column_width" select="'wide'"/>
		<fo:table table-layout="fixed" keep-with-next="always" keep-together="always">
			<xsl:choose>
				<xsl:when test="$column_width='wide'">
					<!-- 102mm -->
					<fo:table-column column-width="21mm"/>
					<fo:table-column column-width="81mm"/>
				</xsl:when>
				<xsl:otherwise>
					<!-- 94mm -->
					<fo:table-column column-width="21mm"/>
					<fo:table-column column-width="73mm"/>
				</xsl:otherwise>
			</xsl:choose>
			<fo:table-body>
				<fo:table-row keep-with-next.within-column="always">
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" font-weight="bold">Special Properties</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon'"/>
						</xsl:call-template>
						<fo:block font-size="6pt" space-before="1pt">
							<xsl:value-of select="special_properties"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - weapons - simple
====================================
====================================-->
	<xsl:template match="simple">
		<xsl:param name="column_width" select="'wide'"/>
		<xsl:call-template name="simple_weapon">
			<xsl:with-param name="to_hit" select="to_hit"/>
			<xsl:with-param name="damage" select="damage"/>
			<xsl:with-param name="column_width" select="$column_width"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="simple_weapon">
		<xsl:param name="to_hit" select="''"/>
		<xsl:param name="damage" select="''"/>
		<xsl:param name="column_width" select="'wide'"/>
		<fo:table table-layout="fixed" keep-with-next="always" keep-together="always">
			<xsl:choose>
				<xsl:when test="$column_width='wide'">
					<!-- 102mm -->
					<fo:table-column column-width="51mm"/>
					<fo:table-column column-width="51mm"/>
				</xsl:when>
				<xsl:otherwise>
					<!-- 94mm -->
					<fo:table-column column-width="47mm"/>
					<fo:table-column column-width="47mm"/>
				</xsl:otherwise>
			</xsl:choose>
			<fo:table-body>
				<fo:table-row keep-with-next.within-column="always">
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt">TOTAL ATTACK BONUS</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.title'"/>
						</xsl:call-template>
						<fo:block font-size="6pt">DAMAGE</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row keep-with-next.within-column="always">
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.hilight'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">
							<xsl:value-of select="$to_hit"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weapon.hilight'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">
							<xsl:value-of select="$damage"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<!--
====================================
====================================
	TEMPLATE - weapons - melee - single tohit/damage
====================================
====================================-->
	<xsl:template name="weapon.complex.tohit">
		<xsl:param name="title"/>
		<xsl:param name="tohit"/>
		<xsl:param name="damage"/>
		
		<fo:table-cell>
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weapon.title'"/></xsl:call-template>
			<fo:block font-size="5pt" font-weight="bold" space-before="1pt">
				<xsl:value-of select="$title"/>
			</fo:block>
		</fo:table-cell>
		<fo:table-cell>
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weapon.hilight'"/></xsl:call-template>
			<fo:block font-size="7pt" space-before="1pt">
				<xsl:value-of select="$tohit"/>
			</fo:block>
		</fo:table-cell>
		<fo:table-cell>
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weapon.hilight'"/></xsl:call-template>
			<fo:block font-size="7pt" space-before="1pt">
				<xsl:value-of select="$damage"/>
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - weapons - melee
====================================
====================================-->
	<xsl:template match="melee">
		<xsl:param name="column_width" select="'wide'"/>
		<fo:table table-layout="fixed" keep-with-next="always" keep-together="always">
			<xsl:choose>
				<xsl:when test="$column_width='wide'">
					<fo:table-column column-width="8mm"/>
					<fo:table-column column-width="29mm"/>
					<fo:table-column column-width="13mm"/>
					<fo:table-column column-width="11mm"/>
					<fo:table-column column-width="28mm"/>
					<fo:table-column column-width="13mm"/>
				</xsl:when>
				<xsl:otherwise>
					<fo:table-column column-width="8mm"/>
					<fo:table-column column-width="26mm"/>
					<fo:table-column column-width="12mm"/>
					<fo:table-column column-width="11mm"/>
					<fo:table-column column-width="25mm"/>
					<fo:table-column column-width="12mm"/>
				</xsl:otherwise>
			</xsl:choose>
			<fo:table-body>
				<fo:table-row keep-with-next.within-column="always">
					<!-- To hit and Damage titles -->
					<fo:table-cell>
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weapon.title'"/></xsl:call-template>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weapon.title'"/></xsl:call-template>
						<fo:block font-size="6pt" font-weight="bold" space-before="1pt">To Hit</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weapon.title'"/></xsl:call-template>
						<fo:block font-size="6pt" font-weight="bold" space-before="1pt">Dam</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weapon.title'"/></xsl:call-template>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weapon.title'"/></xsl:call-template>
						<fo:block font-size="6pt" font-weight="bold" space-before="1pt">To Hit</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weapon.title'"/></xsl:call-template>
						<fo:block font-size="6pt" font-weight="bold" space-before="1pt">Dam</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row keep-with-next.within-column="always">
					<!-- 1HP, 2WP-OH -->
					<xsl:call-template name="weapon.complex.tohit">
						<xsl:with-param name="title" select="'1H-P'"/>
						<xsl:with-param name="tohit" select="w1_h1_p/to_hit"/>
						<xsl:with-param name="damage" select="w1_h1_p/damage" /> 
					</xsl:call-template>
					<xsl:call-template name="weapon.complex.tohit">
						<xsl:with-param name="title" select="'2W-P-(OH)'"/>
						<xsl:with-param name="tohit" select="w2_p_oh/to_hit"/>
						<xsl:with-param name="damage" select="w2_p_oh/damage" /> 
					</xsl:call-template>
				</fo:table-row>
				<fo:table-row keep-with-next.within-column="always">
					<!-- 1HO, 2WPOL -->
					<xsl:call-template name="weapon.complex.tohit">
						<xsl:with-param name="title" select="'1H-O'"/>
						<xsl:with-param name="tohit" select="w1_h1_o/to_hit"/>
						<xsl:with-param name="damage" select="w1_h1_o/damage" /> 
					</xsl:call-template>
					<xsl:call-template name="weapon.complex.tohit">
						<xsl:with-param name="title" select="'2W-P-(OL)'"/>
						<xsl:with-param name="tohit" select="w2_p_ol/to_hit"/>
						<xsl:with-param name="damage" select="w2_p_ol/damage" /> 
					</xsl:call-template>
				</fo:table-row>
				<fo:table-row keep-with-next.within-column="always">
					<!-- 2H, OH -->
					<xsl:call-template name="weapon.complex.tohit">
						<xsl:with-param name="title" select="'2H'"/>
						<xsl:with-param name="tohit" select="w1_h2/to_hit"/>
						<xsl:with-param name="damage" select="w1_h2/damage" /> 
					</xsl:call-template>
					<xsl:call-template name="weapon.complex.tohit">
						<xsl:with-param name="title" select="'2W-OH'"/>
						<xsl:with-param name="tohit" select="w2_o/to_hit"/>
						<xsl:with-param name="damage" select="w2_o/damage" /> 
					</xsl:call-template>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - weapons ranged
====================================
====================================-->
	<xsl:template match="ranges">
		<xsl:param name="column_width" select="'wide'"/>
		<fo:table table-layout="fixed" keep-with-next="always" keep-together="always">
			<xsl:choose>
				<xsl:when test="$column_width='wide'">
					<fo:table-column column-width="7mm"/>
					<fo:table-column column-width="19mm"/>
					<fo:table-column column-width="19mm"/>
					<fo:table-column column-width="19mm"/>
					<fo:table-column column-width="19mm"/>
					<fo:table-column column-width="19mm"/>
				</xsl:when>
				<xsl:otherwise>
					<fo:table-column column-width="7mm"/>
					<fo:table-column column-width="18mm"/>
					<fo:table-column column-width="18mm"/>
					<fo:table-column column-width="17mm"/>
					<fo:table-column column-width="17mm"/>
					<fo:table-column column-width="17mm"/>
				</xsl:otherwise>
			</xsl:choose>
			<fo:table-body>
				<xsl:if test="./ammunition">
					<fo:table-row keep-with-next.within-column="always">
						<fo:table-cell number-columns-spanned="6">
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'weapon.title'"/>
							</xsl:call-template>
							<fo:block font-size="5pt" font-weight="bold">Ammunition: <xsl:value-of select="ammunition/name"/>
								<xsl:if test="string(./ammunition/special_properties) != ''">
									(<xsl:value-of select="./ammunition/special_properties"/>)
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<fo:table-row keep-with-next.within-column="always">
					<!-- Distances -->
					<fo:table-cell>
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weapon.title'"/></xsl:call-template>
					</fo:table-cell>
					<xsl:for-each select="range">
						<fo:table-cell>
							<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weapon.title'"/></xsl:call-template>
							<fo:block font-size="5pt" font-weight="bold">
								<xsl:value-of select="distance"/>
							</fo:block>
						</fo:table-cell>
					</xsl:for-each>
				</fo:table-row>
				<fo:table-row keep-with-next.within-column="always">
					<!-- Range To-Hits -->
					<fo:table-cell>
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weapon.title'"/></xsl:call-template>
						<fo:block font-size="6pt" font-weight="bold">To Hit</fo:block>
					</fo:table-cell>
					<xsl:for-each select="range">
						<fo:table-cell>
							<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weapon.hilight'"/></xsl:call-template>
							<fo:block font-size="7pt" space-before="1pt">
								<xsl:value-of select="to_hit"/>
							</fo:block>
						</fo:table-cell>
					</xsl:for-each>
				</fo:table-row>
				<fo:table-row keep-with-next.within-column="always">
					<!-- Damages -->
					<fo:table-cell>
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weapon.title'"/></xsl:call-template>
						<fo:block font-size="6pt" font-weight="bold">Dam</fo:block>
					</fo:table-cell>
					<xsl:for-each select="range">
						<fo:table-cell>
							<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weapon.hilight'"/></xsl:call-template>
							<fo:block font-size="7pt" space-before="1pt">
								<xsl:value-of select="damage"/>
							</fo:block>
						</fo:table-cell>
					</xsl:for-each>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - Protection
====================================
====================================-->
	<xsl:template match="protection">
		<!-- BEGIN Armor table -->
		<fo:table table-layout="fixed" space-before="2mm">
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'protection.border'"/></xsl:call-template>
			<fo:table-column column-width="55mm"/>
			<fo:table-column column-width="12mm"/>
			<fo:table-column column-width="6mm"/>
			<fo:table-column column-width="8mm"/>
			<fo:table-column column-width="6mm"/>
			<fo:table-column column-width="15mm"/>
			<fo:table-header>
				<fo:table-row>
					<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'protection.title'"/></xsl:call-template>
					<fo:table-cell padding-top="1pt">
						<fo:block font-size="7pt">
							ARMOR
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding-top="3pt">
						<fo:block font-size="4pt">
							TYPE
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding-top="3pt">
						<fo:block font-size="4pt">
							AC
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding-top="3pt">
						<fo:block font-size="4pt">
							MAXDEX
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding-top="3pt">
						<fo:block font-size="4pt">
							CHECK
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding-top="3pt">
						<fo:block font-size="4pt">
							SPELL FAILURE
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<xsl:for-each select="armor|shield|item">
					<xsl:variable name="shade">
						<xsl:choose>
							<xsl:when test="position() mod 2 = 0">darkline</xsl:when>
							<xsl:otherwise>lightline</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					
					<fo:table-row>
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat('protection.', $shade)"/></xsl:call-template>
						<fo:table-cell>
							<fo:block font-size="8pt">
								<xsl:value-of select="name"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="center">
							<fo:block font-size="8pt">
								<xsl:value-of select="type"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="center">
							<fo:block font-size="8pt">
								<xsl:value-of select="totalac"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="center">
							<fo:block font-size="8pt">
								<xsl:value-of select="maxdex"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="center">
							<fo:block font-size="8pt">
								<xsl:value-of select="accheck"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="center">
							<fo:block font-size="8pt">
								<xsl:value-of select="spellfail"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat('protection.', $shade)"/></xsl:call-template>
						<fo:table-cell number-columns-spanned="6" text-align="center">
							<fo:block font-size="6pt">
								<xsl:value-of select="special_properties"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - CLASS FEATURE PER DAY
====================================
====================================-->
	<xsl:template name="class.feature.perday">
		<xsl:param name="attribute"/>
		<xsl:param name="name" />
		<xsl:param name="uses" />
		<xsl:param name="uses.title" select="'Uses per day'" />
		<xsl:param name="description.title" select="''"/>
		<xsl:param name="description" />
		<xsl:param name="width" select="'wide'" />
		
		<fo:table table-layout="fixed" space-before="2mm" keep-together="always" border-collapse="collapse">
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat($attribute, '.border')"/></xsl:call-template>
			<fo:table-column column-width="18mm"/>
			<xsl:choose>
				<xsl:when test="$width='narrow'"><fo:table-column column-width="68mm"/></xsl:when>
				<xsl:otherwise><fo:table-column column-width="84mm"/></xsl:otherwise>
			</xsl:choose>
			<fo:table-body>
				<fo:table-row keep-with-next.within-column="always">
					<fo:table-cell padding-top="1pt" number-columns-spanned="2">
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat($attribute, '.title')"/></xsl:call-template>
						<fo:block font-size="10pt" font-weight="bold">
							<xsl:value-of select="$name"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row keep-with-next.within-column="always">
					<fo:table-cell padding-top="1pt" text-align="end">
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat($attribute, '.title')"/></xsl:call-template>
						<fo:block font-size="8pt"><xsl:value-of select="$uses.title"/></fo:block>
					</fo:table-cell>
					<fo:table-cell padding-top="1pt" padding-left="3pt">
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="$attribute"/></xsl:call-template>
						<fo:block font-size="9pt" font-family="ZapfDingbats">
							<xsl:call-template name="for.loop">
								<xsl:with-param name="count">
									<xsl:call-template name="stripLeadingPlus">
										<xsl:with-param name="string" select="$uses"/>
									</xsl:call-template>
								</xsl:with-param>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:if test="$description != '' ">
					<fo:table-row keep-with-next.within-column="always">
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat($attribute, '.border')"/></xsl:call-template>
						<xsl:choose>
							<xsl:when test="$description.title != '' ">
								<fo:table-cell padding-top="1pt" text-align="end">
									<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="$attribute"/></xsl:call-template>
									<fo:block font-size="6pt"><xsl:value-of select="$description.title"/></fo:block>
								</fo:table-cell>
								<fo:table-cell padding-top="1pt" padding-left="3pt">
									<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="$attribute"/></xsl:call-template>
									<fo:block font-size="6pt"><xsl:value-of select="$description"/></fo:block>
								</fo:table-cell>
							</xsl:when>
							<xsl:otherwise>
								<fo:table-cell padding="3pt" number-columns-spanned="2">
									<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="$attribute"/></xsl:call-template>
									<fo:block font-size="5pt">
										<xsl:value-of select="$description"/>
									</fo:block>
								</fo:table-cell>
							</xsl:otherwise>
						</xsl:choose>
					</fo:table-row>
				</xsl:if>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!--
====================================
====================================
	TEMPLATE - RAGE
====================================
====================================-->
	<xsl:template match="rage">
		<xsl:call-template name="class.feature.perday">
			<xsl:with-param name="attribute" select="'rage'"/>
			<xsl:with-param name="name" select="'BARBARIAN RAGE'"/>
			<xsl:with-param name="uses" select="uses_per_day"/>
			<xsl:with-param name="description" select="description"/>
		</xsl:call-template>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - WILDSHAPE
====================================
====================================-->
	<xsl:template match="wildshape">
		<xsl:call-template name="class.feature.perday">
			<xsl:with-param name="attribute" select="'wildshape'"/>
			<xsl:with-param name="name" select="'DRUID WILDSHAPE'"/>
			<xsl:with-param name="uses" select="uses_per_day"/>
			<xsl:with-param name="description" select="concat('Duration = ',duration,' Hours')"/>
		</xsl:call-template>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - PERFORM
====================================
====================================-->
	<xsl:template match="bardic_music">
		<xsl:call-template name="class.feature.perday">
			<xsl:with-param name="attribute" select="'bard'"/>
			<xsl:with-param name="name" select="'BARDIC MUSIC'"/>
			<xsl:with-param name="uses" select="uses_per_day"/>
<!--			<xsl:with-param name="description.title" select="effects"/> -->
			<xsl:with-param name="description" select="text"/>
			<xsl:with-param name="width" select="'narrow'"/>
		</xsl:call-template>
	</xsl:template>

	<!--
====================================
====================================
	TEMPLATE - TURNING
====================================
====================================-->
	<xsl:template name="turning.hitdice">
		<xsl:param name="die"/>
		<xsl:param name="number"/>
	
		<xsl:variable name="shade">
			<xsl:choose>
				<xsl:when test="$number mod 2 = 0">darkline</xsl:when>
				<xsl:otherwise>lightline</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<fo:table-row>
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat('turning.', $shade)"/></xsl:call-template>
			<fo:table-cell>
				<fo:block font-size="7pt"><xsl:value-of select="$die"/></fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block font-size="7pt"><xsl:value-of select="$number"/></fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - TURNING
====================================
====================================-->
	<xsl:template name="turning.info">
		<xsl:param name="title"/>
		<xsl:param name="info"/>
	
		<fo:table-row>
			<fo:table-cell padding-top="1pt" text-align="end">
				<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'turning.title'"/></xsl:call-template>
				<fo:block font-size="8pt">
					<xsl:value-of select="$title"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'turning'"/></xsl:call-template>
				<fo:block font-size="8pt">
					<xsl:value-of select="$info"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - TURNING
====================================
====================================-->
	<xsl:template match="turning">
		<!-- BEGIN Turning Table -->
		<fo:table table-layout="fixed" space-before="2mm" keep-together="always"  border-collapse="collapse" padding="0.5pt">
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'turning'"/></xsl:call-template>
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'turning.border'"/></xsl:call-template>
			<fo:table-column column-width="52mm"/>
			<fo:table-column column-width="34mm"/>
			<fo:table-body>
				<fo:table-row keep-with-next.within-column="always">
					<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'turning.title'"/></xsl:call-template>
					<fo:table-cell padding-top="1pt" number-columns-spanned="2">
						<fo:block font-size="10pt" font-weight="bold">
							<xsl:value-of select="concat(@type, ' ', @kind)"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'turning.title'"/></xsl:call-template>
					<fo:table-cell>
						<fo:table table-layout="fixed">
							<fo:table-column column-width="26mm"/>
							<fo:table-column column-width="26mm"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'turning.title'"/></xsl:call-template>
										<fo:block font-size="7pt">TURNING CHECK</fo:block>
										<fo:block font-size="7pt">RESULT</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'turning.title'"/></xsl:call-template>
										<fo:block font-size="7pt"><xsl:value-of select="@kind"/> AFFECTED</fo:block>
										<fo:block font-size="6pt">(MAXIMUM HIT DICE)</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:table-cell>
					<fo:table-cell>
						<fo:table table-layout="fixed" border-collapse="collapse" padding="0.5pt">
							<fo:table-column column-width="17mm"/>
							<fo:table-column column-width="17mm"/>
							<fo:table-body>
								<xsl:call-template name="turning.info">
									<xsl:with-param name="title" select="'Turning Check'"/>
									<xsl:with-param name="info" select="turn_check" />
								</xsl:call-template>
							</fo:table-body>
						</fo:table>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row keep-with-next.within-column="always">
					<fo:table-cell>
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'turning.title'"/></xsl:call-template>
						<fo:table table-layout="fixed"  border-collapse="collapse" padding="0.5pt">
							<fo:table-column column-width="26mm"/>
							<fo:table-column column-width="26mm"/>
							<fo:table-body>
								<fo:table-row height="1pt"/>
								<xsl:call-template name="turning.hitdice">
									<xsl:with-param name="die" select="'Up to 0'"/>
									<xsl:with-param name="number" select="number(level)-4" />
								</xsl:call-template>
								<xsl:call-template name="turning.hitdice">
									<xsl:with-param name="die" select="'1 - 3'"/>
									<xsl:with-param name="number" select="number(level)-3" />
								</xsl:call-template>
								<xsl:call-template name="turning.hitdice">
									<xsl:with-param name="die" select="'4 - 6'"/>
									<xsl:with-param name="number" select="number(level)-2" />
								</xsl:call-template>
								<xsl:call-template name="turning.hitdice">
									<xsl:with-param name="die" select="'7 - 9'"/>
									<xsl:with-param name="number" select="number(level)-1" />
								</xsl:call-template>
								<xsl:call-template name="turning.hitdice">
									<xsl:with-param name="die" select="'10 - 12'"/>
									<xsl:with-param name="number" select="level" />
								</xsl:call-template>
								<xsl:call-template name="turning.hitdice">
									<xsl:with-param name="die" select="'13 - 15'"/>
									<xsl:with-param name="number" select="number(level)+1" />
								</xsl:call-template>
								<xsl:call-template name="turning.hitdice">
									<xsl:with-param name="die" select="'16 - 18'"/>
									<xsl:with-param name="number" select="number(level)+2" />
								</xsl:call-template>
								<xsl:call-template name="turning.hitdice">
									<xsl:with-param name="die" select="'19 - 21'"/>
									<xsl:with-param name="number" select="number(level)+3" />
								</xsl:call-template>
								<xsl:call-template name="turning.hitdice">
									<xsl:with-param name="die" select="'22+'"/>
									<xsl:with-param name="number" select="number(level)+4" />
								</xsl:call-template>
							</fo:table-body>
						</fo:table>
					</fo:table-cell>
					<fo:table-cell>
						<fo:table table-layout="fixed" border-collapse="collapse" padding="0.5pt">
							<fo:table-column column-width="17mm"/>
							<fo:table-column column-width="17mm"/>
							<fo:table-body>
								<xsl:call-template name="turning.info">
									<xsl:with-param name="title" select="'Turn level'"/>
									<xsl:with-param name="info" select="level" />
								</xsl:call-template>
								<xsl:call-template name="turning.info">
									<xsl:with-param name="title" select="'Turn damage'"/>
									<xsl:with-param name="info" select="damage" />
								</xsl:call-template>
								<fo:table-row>
									<fo:table-cell number-columns-spanned="2" padding-top="1pt" text-align="end">
										<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'turning.title'"/></xsl:call-template>
										<fo:block font-size="8pt" padding-top="2pt">
											<xsl:value-of select="notes"/>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell number-columns-spanned="2">
						<fo:table border-collapse="collapse" padding="0.5pt" table-layout="fixed">
							<fo:table-column column-width="22mm"/>
							<fo:table-column column-width="64mm"/>
							<fo:table-body>
								<xsl:call-template name="turns.per.day">
									<xsl:with-param name="title" select="concat(@type, '/DAY')"/>
									<xsl:with-param name="value" select="uses_per_day"/>
								</xsl:call-template>
							</fo:table-body>
						</fo:table>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<!-- END Turning Table -->
	</xsl:template>
	

	
	<xsl:template name="turns.per.day">
		<xsl:param name="title" />
		<xsl:param name="value"/>
		<fo:table-row>
			<fo:table-cell  padding-top="2pt" padding-right="2pt">
				<fo:block text-align="end" display-align="center" font-size="9pt"><xsl:value-of select="$title"/></fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="2pt" padding-bottom="2pt" padding-left="2pt">
				<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'turning'"/></xsl:call-template>
				<fo:block text-align="start" font-size="10pt" font-family="ZapfDingbats">
					<xsl:call-template name="for.loop">
						<xsl:with-param name="count" select="$value"/>
					</xsl:call-template>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - Stunning Fist
====================================
====================================-->
	<xsl:template match="stunning_fist">
		<xsl:call-template name="class.feature.perday">
			<xsl:with-param name="attribute" select="'stunningfist'"/>
			<xsl:with-param name="name" select="'STUNNING FIST'"/>
			<xsl:with-param name="uses" select="uses_per_day"/>
		</xsl:call-template>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - WHOLENESS OF BODY
====================================
====================================-->
	<xsl:template match="wholeness_of_body">
		<xsl:call-template name="class.feature.perday">
			<xsl:with-param name="attribute" select="'wholeness'"/>
			<xsl:with-param name="name" select="'WHOLENESS OF BODY'"/>
			<xsl:with-param name="uses" select="hp_per_day"/>
			<xsl:with-param name="uses.title" select="'HP per day'"/>
		</xsl:call-template>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - LAY ON HANDS
====================================
====================================-->
	<xsl:template match="layonhands">
		<xsl:call-template name="class.feature.perday">
			<xsl:with-param name="attribute" select="'bard'"/>
			<xsl:with-param name="name" select="'LAY ON HANDS'"/>
			<xsl:with-param name="uses" select="hp_per_day"/>
			<xsl:with-param name="uses.title" select="'HP per day'"/>
		</xsl:call-template>
	</xsl:template>

	<!--
====================================
====================================
	TEMPLATE - PSIONICS ATTACK / DEFENCE TABLE
====================================
====================================-->
	<Psionics:attacks>
		<attack name="ego.whip" title="EGO WHIP" damage="1d4 DEX" pp="3"/>
		<attack name="id.insinuation" title="ID INSINUATION" damage="1d2 STR" pp="3"/>
		<attack name="mind.blast" title="MIND BLAST" damage="1d4 CHA" pp="9"/>
		<attack name="mind.thrust" title="MIND THRUST" damage="1d2 INT" pp="1"/>
		<attack name="psychic.crush" title="PSYCHIC CRUSH" damage="2d4 WIS" pp="5"/>
		<defences>
			<defence name="Empty Mind" pp="1" mentalhardness="">
				<attack name="ego.whip" value="+1" />
				<attack name="id.insinuation" value="-2" />
				<attack name="mind.blast" value="+3" />
				<attack name="mind.thrust" value="-3" />
				<attack name="psychic.crush" value="-5" />
			</defence>
			<defence name="Intellect Fortress" pp="5" mentalhardness="3">
				<attack name="ego.whip" value="-2" />
				<attack name="id.insinuation" value="+1" />
				<attack name="mind.blast" value="+0" />
				<attack name="mind.thrust" value="+6" />
				<attack name="psychic.crush" value="+4" />
			</defence>
			<defence name="Mental Barrier" pp="3" mentalhardness="2">
				<attack name="ego.whip" value="-1" />
				<attack name="id.insinuation" value="+4" />
				<attack name="mind.blast" value="-3" />
				<attack name="mind.thrust" value="+1" />
				<attack name="psychic.crush" value="+3" />
			</defence>
			<defence name="Thought Shield" pp="1" mentalhardness="1">
				<attack name="ego.whip" value="-4" />
				<attack name="id.insinuation" value="-1" />
				<attack name="mind.blast" value="-2" />
				<attack name="mind.thrust" value="+4" />
				<attack name="psychic.crush" value="+2" />
			</defence>
			<defence name="Tower of Iron Will" pp="5" mentalhardness="2">
				<attack name="ego.whip" value="+3" />
				<attack name="id.insinuation" value="+0" />
				<attack name="mind.blast" value="-1" />
				<attack name="mind.thrust" value="+5" />
				<attack name="psychic.crush" value="-3" />
			</defence>
			<defence name="Nonpsionic Buffer" pp="" mentalhardness="">
				<attack name="ego.whip" value="-8" />
				<attack name="id.insinuation" value="-9" />
				<attack name="mind.blast" value="+4" />
				<attack name="mind.thrust" value="-8" />
				<attack name="psychic.crush" value="-8" />
			</defence>
			<defence name="Flat-footed or out of Power Points" pp="" mentalhardness="">
				<attack name="ego.whip" value="+8" />
				<attack name="id.insinuation" value="+7" />
				<attack name="mind.blast" value="+8" />
				<attack name="mind.thrust" value="+8" />
				<attack name="psychic.crush" value="+8" />
			</defence>
		</defences>
	</Psionics:attacks>

	<!--
====================================
====================================
	TEMPLATE - PSIONICS
====================================
====================================-->
	<xsl:template name="psionic.entry">
		<xsl:param name="title"/>
		<xsl:param name="title.cols" select="1"/>
		<xsl:param name="value"/>
		<xsl:param name="value.cols" select="1"/>

		<fo:table-cell padding-top="1pt" text-align="end">
			<xsl:attribute name="number-columns-spanned"><xsl:value-of select="$title.cols"/></xsl:attribute>
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'psionics.title'"/></xsl:call-template>
			<fo:block font-size="8pt"><xsl:value-of select="$title"/></fo:block>
		</fo:table-cell>
		<fo:table-cell padding-top="1pt" padding-left="3pt">
			<xsl:attribute name="number-columns-spanned"><xsl:value-of select="$value.cols"/></xsl:attribute>
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'psionics'"/></xsl:call-template>
			<fo:block font-size="8pt"><xsl:value-of select="$value"/></fo:block>
		</fo:table-cell>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - PSIONICS
====================================
====================================-->
	<xsl:template match="psionics">
		<!-- BEGIN psionicsTable -->
		<fo:table table-layout="fixed" space-before="2mm" padding="0.5pt">
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'psionics.border'"/></xsl:call-template>
			<fo:table-column column-width="17mm"/>
			<fo:table-column column-width="17mm"/>
			<fo:table-column column-width="17mm"/>
			<fo:table-column column-width="17mm"/>
			<fo:table-column column-width="17mm"/>
			<fo:table-column column-width="17mm"/>
			<fo:table-body>
				<fo:table-row keep-with-next.within-column="always">
					<fo:table-cell padding-top="1pt" number-columns-spanned="6">
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'psionics.title'"/></xsl:call-template>
						<fo:block font-size="10pt" font-weight="bold">Psionics</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row keep-with-next.within-column="always">
					<xsl:call-template name="psionic.entry">
						<xsl:with-param name="title" select="'Base PP'"/>
						<xsl:with-param name="value" select="base_pp"/>
					</xsl:call-template>
					<xsl:call-template name="psionic.entry">
						<xsl:with-param name="title" select="'Bonus  PP'"/>
						<xsl:with-param name="value" select="bonus_pp"/>
					</xsl:call-template>
					<xsl:call-template name="psionic.entry">
						<xsl:with-param name="title" select="'Total PP'"/>
						<xsl:with-param name="value" select="total_pp"/>
					</xsl:call-template>
				</fo:table-row>
				<fo:table-row keep-with-next.within-column="always">
					<xsl:call-template name="psionic.entry">
						<xsl:with-param name="title" select="'Current PP'"/>
						<xsl:with-param name="value" select="''"/>
						<xsl:with-param name="value.cols" select="5"/>
					</xsl:call-template>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<xsl:if test = "type = '3.0'">
		<!-- Attack / Defence table -->
		<fo:table table-layout="fixed" padding="0.5pt">
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'psionics.border'"/></xsl:call-template>
			<fo:table-column column-width="34mm"/>
			<fo:table-column column-width="8mm"/>
			<fo:table-column column-width="12mm"/>
			<fo:table-column column-width="12mm"/>
			<fo:table-column column-width="12mm"/>
			<fo:table-column column-width="12mm"/>
			<fo:table-column column-width="12mm"/>
			<fo:table-body>
				<fo:table-row keep-with-next.within-column="always">
					<fo:table-cell padding-top="1pt">
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'psionics.title'"/></xsl:call-template>
						<fo:block font-size="5pt"/>
					</fo:table-cell>
					<fo:table-cell padding-top="1pt">
						<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'psionics.title'"/></xsl:call-template>
						<fo:block font-size="5pt">Mental Hardness</fo:block>
					</fo:table-cell>
					<xsl:variable name="attacks" select="document('')/*/Psionics:attacks/attack"/>
					<xsl:for-each select="$attacks">
						<fo:table-cell padding-top="1pt">
							<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'psionics.title'"/></xsl:call-template>
							<fo:block font-size="5pt"><xsl:value-of select="@title"/></fo:block>
							<fo:block font-size="4pt">(<xsl:value-of select="@damage"/>) <xsl:value-of select="@pp"/>pp</fo:block>
						</fo:table-cell>
					</xsl:for-each>
				</fo:table-row>

				<xsl:variable name="defences" select="document('')/*/Psionics:attacks/defences/defence"/>
				<xsl:for-each select="$defences">
					<fo:table-row keep-with-previous.within-column="always">
						<fo:table-cell padding-top="1pt" padding-left="3pt">
							<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'psionics.title'"/></xsl:call-template>
							<fo:block font-size="7pt"><xsl:value-of select="@name"/>
								<xsl:if test="@pp != ''"> (<xsl:value-of select="@pp"/>pp)</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-top="1pt">
							<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'psionics'"/></xsl:call-template>
							<fo:block font-size="7pt"><xsl:value-of select="@mentalhardness"/></fo:block>
						</fo:table-cell>
						<xsl:for-each select="attack">
							<fo:table-cell padding-top="1pt">
								<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'psionics'"/></xsl:call-template>
								<fo:block font-size="7pt"><xsl:value-of select="@value"/></fo:block>
							</fo:table-cell>
						</xsl:for-each>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
		<!-- END psionicsTable -->
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - DOMAINS
====================================
====================================-->
	<xsl:template match="domains">
		<!-- BEGIN Domains Table -->
		<xsl:call-template name="stripped.list">
			<xsl:with-param name="attribute" select="'domains'" />
			<xsl:with-param name="title" select="'DOMAINS'" />
			<xsl:with-param name="list" select="domain"/>
			<xsl:with-param name="name.tag" select="'name'"/>
			<xsl:with-param name="desc.tag" select="'power'"/>
		</xsl:call-template>
		<!-- END Domains Table -->
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - WEAPON PROFICIENCIES
====================================
====================================-->
	<xsl:template match="weapon_proficiencies">
		<!-- BEGIN weapon_proficiencies Table -->
		<xsl:call-template name="list">
			<xsl:with-param name="attribute" select="'proficiencies'"/>
			<xsl:with-param name="title" select="'PROFICIENCIES'"/>
			<xsl:with-param name="value" select="." />
		</xsl:call-template>
		<!-- END weapon_proficiencies Table -->
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - LANGUAGES
====================================
====================================-->
	<xsl:template match="languages">
		<!-- BEGIN Languages Table -->
		<xsl:call-template name="list">
			<xsl:with-param name="attribute" select="'languages'"/>
			<xsl:with-param name="title" select="'LANGUAGES'"/>
			<xsl:with-param name="value" select="." />
		</xsl:call-template>
		<!-- END Languages Table -->
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - TEMPLATES
====================================
====================================-->
	<xsl:template match="templates">
		<!-- BEGIN Templates Table -->
		<xsl:call-template name="stripped.list">
			<xsl:with-param name="attribute" select="'templates'" />
			<xsl:with-param name="title" select="'TEMPLATES'" />
			<xsl:with-param name="list" select="template"/>
			<xsl:with-param name="name.tag" select="'name'"/>
		</xsl:call-template>
		<!-- END Templates Table -->
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - PROHIBITED
====================================
====================================-->
	<xsl:template match="prohibited_schools">
		<xsl:if test=". != ''">
			<xsl:call-template name="list">
				<xsl:with-param name="attribute" select="'prohibited'"/>
				<xsl:with-param name="title" select="'PROHIBITED'"/>
				<xsl:with-param name="value" select="." />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - COMPANIONS
====================================
====================================-->
	<xsl:template match="companions">
		<!-- BEGIN Companions Table -->
		<xsl:apply-templates select="familiar"/>
		<xsl:apply-templates select="mount"/>
		<xsl:apply-templates select="companion"/>
		<xsl:call-template name="followers.list"/>
		<!-- END Companions Table -->
	</xsl:template>
	<xsl:template match="familiar">
		<!-- BEGIN Familiar Table -->
		<xsl:call-template name="show_companion">
			<xsl:with-param name="followerType" select="'Familiar'"/>
		</xsl:call-template>
		<!-- END Familiar Table -->
	</xsl:template>
	<xsl:template match="mount">
		<!-- BEGIN Familiar Table -->
		<xsl:call-template name="show_companion">
			<xsl:with-param name="followerType" select="'Special Mount'"/>
		</xsl:call-template>
		<!-- END Familiar Table -->
	</xsl:template>
	<xsl:template match="companion">
		<!-- BEGIN Familiar Table -->
		<xsl:call-template name="show_companion">
			<xsl:with-param name="followerType" select="'Animal Companion'"/>
		</xsl:call-template>
		<!-- END Familiar Table -->
	</xsl:template>
	<xsl:template name="followers.list">
		<xsl:if test="count(follower) &gt; 0">
			<fo:table table-layout="fixed" space-after.optimum="2mm">
				<fo:table-column column-width="94mm"/>
				<fo:table-body>
					<fo:table-row keep-with-next.within-column="always">
						<fo:table-cell>
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'companions.title'"/>
							</xsl:call-template>
							<fo:block font-size="10pt" font-weight="bold">Followers: </fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row keep-with-next.within-column="always">
						<fo:table-cell>
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'companions'"/>
							</xsl:call-template>
							<xsl:for-each select="follower">
								<fo:block font-size="8pt">
									<xsl:value-of select="name"/>
								</fo:block>
							</xsl:for-each>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</xsl:if>
	</xsl:template>
	<xsl:template name="show_companion">
		<xsl:param name="followerType" select="Follower"/>
		<fo:table table-layout="fixed" space-before.optimum="2mm">
			<fo:table-column column-width="25mm"/>
			<fo:table-column column-width="15mm"/>
			<fo:table-column column-width="13mm"/>
			<fo:table-column column-width="14mm"/>
			<fo:table-column column-width="13mm"/>
			<fo:table-column column-width="14mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell number-columns-spanned="6">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'companions.title'"/>
						</xsl:call-template>
						<fo:block font-size="10pt" font-weight="bold">
							<xsl:value-of select="$followerType"/>: <xsl:value-of select="name"/> (<xsl:value-of select="race"/>)</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row keep-with-next.within-column="always">
					<fo:table-cell text-align="end">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'companions.title'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">HP:</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'companions'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">
							<xsl:value-of select="hp"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="end">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'companions.title'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">AC:</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'companions'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">
							<xsl:value-of select="ac"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="end">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'companions.title'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">INIT:</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'companions'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">
							<xsl:value-of select="initiative_mod"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row keep-with-next.within-column="always">
					<fo:table-cell text-align="end">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'companions.title'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">FORT:</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'companions'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">
							<xsl:value-of select="fortitude"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="end">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'companions.title'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">REF:</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'companions'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">
							<xsl:value-of select="reflex"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="end">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'companions.title'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">WILL:</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'companions'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">
							<xsl:value-of select="willpower"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:for-each select="attacks/attack">
					<xsl:if test="string-length(common/name/long) &gt; 0">
						<fo:table-row keep-with-next.within-column="always">
							<fo:table-cell text-align="end">
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'companions.title'"/>
								</xsl:call-template>
								<fo:block font-size="8pt">
									<xsl:variable name="name" select="substring-before(common/name/long,'(')"/>
									<xsl:variable name="description" select="substring-after(common/name/long,'(')"/>
									<xsl:value-of select="$name"/>
									<xsl:if test="string-length($name) = 0">
										<xsl:value-of select="common/name/long"/>
									</xsl:if>
									<xsl:if test="string-length($description) &gt; 0">
										<fo:inline font-size="5pt">
											<xsl:text>(</xsl:text>
											<xsl:value-of select="$description"/>
										</fo:inline>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'companions'"/>
								</xsl:call-template>
								<fo:block font-size="8pt">
									<xsl:value-of select="simple/to_hit"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell text-align="end">
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'companions.title'"/>
								</xsl:call-template>
								<fo:block font-size="8pt">DAM:</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'companions'"/>
								</xsl:call-template>
								<fo:block font-size="8pt">
									<xsl:value-of select="simple/damage"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell text-align="end">
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'companions.title'"/>
								</xsl:call-template>
								<fo:block font-size="8pt">CRIT:</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<xsl:call-template name="attrib">
									<xsl:with-param name="attribute" select="'companions'"/>
								</xsl:call-template>
								<fo:block font-size="8pt">
									<xsl:value-of select="common/critical/range"/>/x<xsl:value-of select="common/critical/multiplier"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:if>
				</xsl:for-each>
				<fo:table-row keep-with-next.within-column="always">
					<fo:table-cell text-align="end">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'companions.title'"/>
						</xsl:call-template>
						<fo:block font-size="8pt">Special:</fo:block>
					</fo:table-cell>
					<fo:table-cell number-columns-spanned="5">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'companions'"/>
						</xsl:call-template>
						<fo:block font-size="7pt">
							<xsl:value-of select="special_properties"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - Equipment
====================================
====================================-->
	<xsl:template match="equipment">
		<fo:block>
			<fo:table table-layout="fixed" space-before.optimum="2mm">
				<xsl:call-template name="attrib">
					<xsl:with-param name="attribute" select="'equipment.border'"/>
				</xsl:call-template>
				<fo:table-column column-width="51mm"/>
				<fo:table-column column-width="19mm"/>
				<fo:table-column column-width="6mm"/>
				<fo:table-column column-width="8mm"/>
				<fo:table-column column-width="10mm"/>
				<fo:table-header>
					<fo:table-row>
						<fo:table-cell padding-top="1pt" number-columns-spanned="5">
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'equipment.title'"/>
							</xsl:call-template>
							<fo:block font-size="9pt">EQUIPMENT</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'equipment.title'"/>
						</xsl:call-template>
						<fo:table-cell padding-top="1pt">
							<fo:block font-size="7pt">ITEM</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-top="1pt">
							<fo:block font-size="7pt">LOCATION</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-top="1pt">
							<fo:block font-size="7pt">QTY</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-top="1pt">
							<fo:block font-size="7pt">WT</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-top="1pt">
							<fo:block font-size="7pt">COST</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-footer>
					<fo:table-row>
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'equipment.title'"/>
						</xsl:call-template>
						<fo:table-cell padding-top="1pt" number-columns-spanned="3">
							<fo:block font-size="7pt">TOTAL WEIGHT CARRIED/VALUE</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-top="1pt">
							<fo:block font-size="7pt">
								<xsl:value-of select="total/weight"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-top="1pt">
							<fo:block font-size="7pt">
								<xsl:variable name="TotalValue">
									<xsl:call-template name="Total">
										<xsl:with-param name="Items" select="item[contains(type, 'COIN')=false and contains(type, 'GEM')=false]"/>
										<xsl:with-param name="RunningTotal" select="0"/>
									</xsl:call-template>
								</xsl:variable>
								<xsl:value-of select="format-number($TotalValue, '####0.0#')"/> gp
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-footer>
				<fo:table-body>
					<xsl:for-each select="item[contains(type, 'COIN')=false and contains(type, 'GEM')=false]">
						<xsl:variable name="shade">
							<xsl:choose>
								<xsl:when test="position() mod 2 = 0 ">darkline</xsl:when>
								<xsl:otherwise>lightline</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
					
						<fo:table-row>
							<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat('equipment.', $shade)"/></xsl:call-template>
							<fo:table-cell>
								<fo:block space-before.optimum="1pt" font-size="8pt">
									<xsl:if test="contains(type, 'MAGIC') or contains(type, 'PSIONIC')">
										<xsl:attribute name="font-weight">bold</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="name"/>
								</fo:block>
								<fo:block space-before.optimum="1pt" font-size="5pt">
									<xsl:value-of select="contents"/>
								</fo:block>
								<fo:block space-before.optimum="1pt" font-size="5pt">
									<xsl:value-of select="special_properties"/>
								</fo:block>
								<fo:block space-before.optimum="1pt" font-size="5pt">
									<xsl:value-of select="note"/>
								</fo:block>
								<!-- Display the number of charges left if any -->
								<xsl:if test="charges &gt; 0">
									<fo:block font-size="7pt" font-family="ZapfDingbats">
										<xsl:call-template name="for.loop">
											<xsl:with-param name="count" select="charges"/>
										</xsl:call-template>
									</fo:block>
								</xsl:if>
								<!-- Display the ammunition as a series of checkboxes -->
								<xsl:if test="contains(type, 'POTION') or contains(type, 'AMMUNITION')">
									<fo:block font-size="7pt" font-family="ZapfDingbats">
										<xsl:call-template name="for.loop">
											<xsl:with-param name="count" select="quantity"/>
										</xsl:call-template>
									</fo:block>
								</xsl:if>
							</fo:table-cell>
							<fo:table-cell text-align="center">
								<fo:block space-before.optimum="1pt" font-size="7pt">
									<xsl:value-of select="location"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block text-align="center" space-before.optimum="1pt" font-size="7pt">
									<xsl:value-of select="quantity"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block text-align="center" space-before.optimum="1pt" font-size="7pt">
									<xsl:value-of select="format-number(weight, '####0.0#')"/>
									<xsl:if test="quantity &gt; 1">
										(<xsl:value-of select="format-number(weight * quantity, '####0.0#')"/>)
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block text-align="center" space-before.optimum="1pt" font-size="7pt">
									<xsl:value-of select="format-number(cost, '####0.0#')"/>
									<xsl:if test="quantity &gt; 1">
										(<xsl:value-of select="format-number(cost * quantity, '####0.0#')"/>)
									</xsl:if>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
		</fo:block>
		<!-- END Equipment table -->
	</xsl:template>
	
	<!--
====================================
====================================
	TEMPLATE - WEIGHT ALLOWANCE SINGLE ENTRY
====================================
====================================-->
	<xsl:template name="weight.entry">
		<xsl:param name="title"/>
		<xsl:param name="value"/>
			
		<fo:table-cell padding-top="1pt" padding-right="1mm">
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weight.darkline'"/></xsl:call-template>
			<fo:block font-size="7pt" text-align="end"><xsl:value-of select="$title"/></fo:block>
		</fo:table-cell>
		<fo:table-cell padding-top="1pt" padding-left="1mm">
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weight.lightline'"/></xsl:call-template>
			<fo:block font-size="7pt"><xsl:value-of select="$value"/></fo:block>
		</fo:table-cell>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - WEIGHT ALLOWANCE
====================================
====================================-->
	<xsl:template match="weight_allowance">
		<!-- BEGIN Weight table -->
		<fo:table table-layout="fixed" space-before.optimum="2mm" padding="0.5pt">
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="'weight.border'"/></xsl:call-template>
			<fo:table-column column-width="20mm"/>
			<fo:table-column column-width="11mm"/>
			<fo:table-column column-width="20mm"/>
			<fo:table-column column-width="11mm"/>
			<fo:table-column column-width="20mm"/>
			<fo:table-column column-width="12mm"/>
			<fo:table-body>
				<fo:table-row keep-with-next.within-column="always">
					<fo:table-cell padding-top="1pt" number-columns-spanned="6">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'weight.title'"/>
						</xsl:call-template>
						<fo:block font-size="9pt">WEIGHT ALLOWANCE</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row keep-with-next.within-column="always">
					<xsl:call-template name="weight.entry">
						<xsl:with-param name="title" select="'Light'"/>
						<xsl:with-param name="value" select="light"/>
					</xsl:call-template>
					<xsl:call-template name="weight.entry">
						<xsl:with-param name="title" select="'Medium'"/>
						<xsl:with-param name="value" select="medium"/>
					</xsl:call-template>
					<xsl:call-template name="weight.entry">
						<xsl:with-param name="title" select="'Heavy'"/>
						<xsl:with-param name="value" select="heavy"/>
					</xsl:call-template>
				</fo:table-row>
				<fo:table-row keep-with-next.within-column="always">
					<xsl:call-template name="weight.entry">
						<xsl:with-param name="title" select="'Lift over head'"/>
						<xsl:with-param name="value" select="lift_over_head"/>
					</xsl:call-template>
					<xsl:call-template name="weight.entry">
						<xsl:with-param name="title" select="'Lift off ground'"/>
						<xsl:with-param name="value" select="lift_off_ground"/>
					</xsl:call-template>
					<xsl:call-template name="weight.entry">
						<xsl:with-param name="title" select="'Push / Drag'"/>
						<xsl:with-param name="value" select="push_drag"/>
					</xsl:call-template>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - Money
====================================
====================================-->
	<xsl:template name="money">
		<xsl:if test="count (misc/funds/fund|equipment/item[contains(type, 'COIN') or contains(type, 'GEM')]) &gt; 0">
			<fo:table table-layout="fixed" space-before.optimum="2mm">
				<xsl:call-template name="attrib">
					<xsl:with-param name="attribute" select="'money.border'"/>
				</xsl:call-template>
				<fo:table-column column-width="94mm"/>
				<fo:table-header>
					<fo:table-row keep-with-next.within-column="always">
						<fo:table-cell padding-top="1pt">
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'money.title'"/>
							</xsl:call-template>
							<fo:block font-size="9pt">MONEY</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-footer>
					<fo:table-row>
						<fo:table-cell>
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'money.title'"/>
							</xsl:call-template>
							<fo:block font-size="7pt" text-align="end">
								<xsl:variable name="TotalValue">
									<xsl:call-template name="Total">
										<xsl:with-param name="Items" select="equipment/item[contains(type, 'COIN') or contains(type, 'GEM')]"/>
										<xsl:with-param name="RunningTotal" select="0"/>
									</xsl:call-template>
								</xsl:variable>
								Total   = <xsl:value-of select="format-number($TotalValue, '####0.0#')"/> gp
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-footer>
				<fo:table-body>
					<!-- dump coins -->
					<xsl:for-each select="equipment/item[contains(type, 'COIN')]">
						<xsl:sort order="descending" select="cost" data-type="number"/>
						<xsl:variable name="shade">
							<xsl:choose>
								<xsl:when test="position() mod 2 = 0">darkline</xsl:when>
								<xsl:otherwise>lightline</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<fo:table-row keep-with-next.within-column="always">
							<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat('money.', $shade)"/></xsl:call-template>
							<fo:table-cell>
								<fo:block>
									<xsl:value-of select="name"/>: <xsl:value-of select="quantity"/>
									 <fo:inline font-size="6pt">[<xsl:value-of select="location"/>]</fo:inline>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
					<xsl:variable name="coin_count" select="count( equipment/item[contains(type, 'COIN')] )"/>
					
					<!-- dump gems -->
					<xsl:for-each select="equipment/item[contains(type, 'GEM')]">
						<xsl:sort order="descending" select="cost" data-type="number"/>
						<xsl:variable name="shade">
							<xsl:choose>
								<xsl:when test="($coin_count + position()) mod 2 = 0">darkline</xsl:when>
								<xsl:otherwise>lightline</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<fo:table-row keep-with-next.within-column="always">
							<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat('money.', $shade)"/></xsl:call-template>
							<fo:table-cell>
								<fo:block>
									<xsl:value-of select="quantity"/> x <xsl:value-of select="name"/> (<xsl:value-of select="cost"/>)
									 <fo:inline font-size="6pt">[<xsl:value-of select="location"/>]</fo:inline>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
					<xsl:variable name="gem_count" select="count( equipment/item[contains(type, 'GEM')] )"/>

					<!-- misc funds -->
					<xsl:for-each select="misc/funds/fund">
						<xsl:variable name="shade">
							<xsl:choose>
								<xsl:when test="($coin_count + $gem_count + position()) mod 2 = 0 ">darkline</xsl:when>
								<xsl:otherwise>lightline</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<fo:table-row keep-with-next.within-column="always">
							<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat('money.', $shade)"/></xsl:call-template>
							<fo:table-cell>
								<fo:block font-size="7pt">
									<xsl:value-of select="."/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - Misc Magic
====================================
====================================-->
	<xsl:template match="magics">
		<xsl:if test="count(magic) &gt; 0">
			<fo:table table-layout="fixed" space-before.optimum="2mm">
				<xsl:call-template name="attrib">
					<xsl:with-param name="attribute" select="'magic.border'"/>
				</xsl:call-template>
				<fo:table-column column-width="94mm"/>
				<fo:table-header>
					<fo:table-row keep-with-next.within-column="always">
						<fo:table-cell padding-top="1pt">
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'magic.title'"/>
							</xsl:call-template>
							<fo:block font-size="9pt">MAGIC</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<xsl:for-each select="magic">
						<fo:table-row keep-with-next.within-column="always">
							<fo:table-cell>
								<fo:block font-size="7pt">
									<xsl:value-of select="."/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - Special Abilities
====================================
====================================-->
	<xsl:template match="special_abilities">
		<xsl:if test="count(ability) &gt; 0">
			<xsl:call-template name="stripped.list">
				<xsl:with-param name="attribute" select="'special_abilities'" />
				<xsl:with-param name="title" select="'SPECIAL ABILITIES'" />
				<xsl:with-param name="list" select="ability" />
				<xsl:with-param name="name.tag" select="'name'"/>
				<xsl:with-param name="desc.tag" select="''"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - FEATS
====================================
====================================-->
	<xsl:template match="feats">
		<xsl:if test="count(feat[hidden != 'T' and name != '']) &gt; 0">
			<xsl:call-template name="stripped.list">
				<xsl:with-param name="attribute" select="'feats'" />
				<xsl:with-param name="title" select="'FEATS'" />
				<xsl:with-param name="list" select="feat[hidden != 'T' and name != '']"/>
				<xsl:with-param name="name.tag" select="'name'"/>
				<xsl:with-param name="desc.tag" select="'description'"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - SPELLS
====================================
====================================-->
	<xsl:template match="spells">
		<!-- BEGIN Spells Pages -->
		<xsl:if test="count(.//spell) &gt; 0">
			<fo:page-sequence>
				<xsl:attribute name="master-reference">Portrait</xsl:attribute>
				<xsl:call-template name="page.footer"/>
				<fo:flow flow-name="body"  font-size="8pt">
					<xsl:apply-templates select="spells_innate/racial_innate"/>
					<xsl:apply-templates select="spells_innate/class_innate"/>
					<xsl:apply-templates select="known_spells"/>
					<xsl:apply-templates select="memorized_spells"/>
				</fo:flow>
			</fo:page-sequence>
		</xsl:if>
		<!-- END Spells Pages -->
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - Racial Innate
====================================
====================================-->
	<xsl:template match="racial_innate">
		<xsl:if test="count(.//spell) &gt; 0">
			<fo:block>
				<fo:table table-layout="fixed">
					<xsl:call-template name="spells.known.header.row">
						<xsl:with-param name="columnOne" select="''"/>
						<xsl:with-param name="title" select="'Innate Racial Spells'"/>
					</xsl:call-template>
					<fo:table-body>
						<xsl:apply-templates select="spell" mode="details">
							<xsl:with-param name="columnOne" select="'Times'"/>
						</xsl:apply-templates>
					</fo:table-body>
				</fo:table>
			</fo:block>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - INNATE CLASS SPELLS
====================================
====================================-->
	<xsl:template match="class_innate">
		<xsl:if test="count(.//spell) &gt; 0">
			<xsl:for-each select="spellbook">
				<fo:table table-layout="fixed" space-before="5mm">
					<xsl:call-template name="spells.known.header.row">
						<xsl:with-param name="columnOne" select="''"/>
						<xsl:with-param name="title" select="concat(@name, ' Innate Spells')"/>
					</xsl:call-template>
					<fo:table-body>
						<xsl:apply-templates select="spell" mode="details">
							<xsl:with-param name="columnOne" select="'Times'"/>
						</xsl:apply-templates>
					</fo:table-body>
				</fo:table>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - KNOWN SPELLS
====================================
====================================-->
	<xsl:template match="known_spells">
		<xsl:if test="count(.//spell) &gt; 0">
			<xsl:apply-templates select="class" mode="spells.known"/>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - KNOWN SPELLS - SINGLE CLASS
====================================
====================================-->
	<xsl:template match="class" mode="spells.known">
		<xsl:if test="count(.//spell) &gt; 0">
			<fo:block break-before="page"/>
			<fo:table table-layout="fixed">
				<xsl:variable name="titletext">
					<xsl:choose>
						<xsl:when test="@spellcastertype = 'Psionic'">
							<xsl:value-of select="concat(@spelllistclass, ' Powers')"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="concat(@spelllistclass, ' Spells')"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="columnOne">
					<xsl:choose>
						<xsl:when test="@spellcastertype = 'Psionic'">PowerPoints</xsl:when>
						<xsl:otherwise>Boxes</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="columnOneTitle">
					<xsl:choose>
						<xsl:when test="@spellcastertype = 'Psionic'">Power Points</xsl:when>
						<xsl:otherwise/>
					</xsl:choose>
				</xsl:variable>
				<xsl:call-template name="spells.known.header.row">
					<xsl:with-param name="columnOne" select="$columnOneTitle"/>
					<xsl:with-param name="title" select="$titletext"/>
					<xsl:with-param name="details" select="'false'"/>
				</xsl:call-template>
				<fo:table-body>
					<fo:table-row height="2mm"/>
					<fo:table-row>
						<fo:table-cell number-columns-spanned="100">
							<xsl:apply-templates select="." mode="spell.level.table"/>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row height="2mm"/>
					<xsl:apply-templates select="level" mode="known.spells">
						<xsl:with-param name="columnOne" select="$columnOne"/>
						<xsl:with-param name="columnOneTitle" select="$columnOneTitle"/>
					</xsl:apply-templates>
				</fo:table-body>
			</fo:table>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - CLASS (SPELL.LEVEL.TABLE)
====================================
====================================-->
	<xsl:template match="class" mode="spell.level.table">
		<fo:table table-layout="fixed" border-collapse="collapse">
			<fo:table-column column-width="proportional-column-width(2)"/>
			<fo:table-column column-width="proportional-column-width(2)"/>
			<xsl:for-each select="level">
				<fo:table-column column-width="proportional-column-width(1)"/>
			</xsl:for-each>
			<fo:table-column column-width="proportional-column-width(2)"/>
			<fo:table-body>
				<xsl:apply-templates select="." mode="spell.level.count"/>
				<xsl:apply-templates select="." mode="spell.level.known"/>
				<xsl:apply-templates select="." mode="spell.level.cast"/>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - CLASS (SPELL.LEVEL.COUNT)
====================================
====================================-->
	<xsl:template match="class" mode="spell.level.count">
		<fo:table-row keep-with-next.within-column="always">
			<fo:table-cell/>
			<fo:table-cell>
				<xsl:call-template name="attrib">
					<xsl:with-param name="attribute" select="'spelllist.known.header'"/>
				</xsl:call-template>
				<fo:block font-size="6pt" font-weight="bold" space-start="2pt" space-before="2pt" space-after="1pt">LEVEL</fo:block>
			</fo:table-cell>
			<xsl:for-each select="level">
				<fo:table-cell>
					<xsl:call-template name="attrib">
						<xsl:with-param name="attribute" select="'spelllist.known.header.centre'"/>
					</xsl:call-template>
					<fo:block space-before="2pt" space-after="1pt" font-size="6pt">
						<xsl:value-of select="@number"/>
					</fo:block>
				</fo:table-cell>
			</xsl:for-each>
			<fo:table-cell/>
		</fo:table-row>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - CLASS (SPELL.LEVEL.KNOWN)
====================================
====================================-->
	<xsl:template match="class" mode="spell.level.known">
		<fo:table-row keep-with-next.within-column="always">
			<fo:table-cell/>
			<fo:table-cell>
				<xsl:call-template name="attrib">
					<xsl:with-param name="attribute" select="'spelllist.known.header'"/>
				</xsl:call-template>
				<fo:block font-size="6pt" font-weight="bold" space-start="2pt" space-before="2pt" space-after="1pt">KNOWN</fo:block>
			</fo:table-cell>
			<xsl:for-each select="level">
				<fo:table-cell>
					<xsl:call-template name="attrib">
						<xsl:with-param name="attribute" select="'spelllist.known.known'"/>
					</xsl:call-template>
					<fo:block font-size="6pt" space-before="2pt" space-after="1pt">
						<xsl:value-of select="@known"/>
					</fo:block>
				</fo:table-cell>
			</xsl:for-each>
			<fo:table-cell/>
		</fo:table-row>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - CLASS (SPELL.LEVEL.CAST)
====================================
====================================-->
	<xsl:template match="class" mode="spell.level.cast">
		<fo:table-row padding-bottom="2mm">
			<fo:table-cell/>
			<fo:table-cell>
				<xsl:call-template name="attrib">
					<xsl:with-param name="attribute" select="'spelllist.known.header'"/>
				</xsl:call-template>
				<fo:block font-size="6pt" font-weight="bold" space-start="2pt" space-before="2pt" space-after="1pt">PER DAY</fo:block>
			</fo:table-cell>
			<xsl:for-each select="level">
				<fo:table-cell>
					<xsl:call-template name="attrib">
						<xsl:with-param name="attribute" select="'spelllist.known.perday'"/>
					</xsl:call-template>
					<fo:block font-size="6pt" space-before="2pt" space-after="1pt">
						<xsl:value-of select="@cast"/>
					</fo:block>
				</fo:table-cell>
			</xsl:for-each>
			<fo:table-cell/>
		</fo:table-row>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - KNOWN SPELL LEVEL
====================================
====================================-->
	<xsl:template match="level" mode="known.spells">
		<xsl:param name="columnOne" select="'Boxes'"/>
		<xsl:param name="columnOneTitle" select="''"/>
		<xsl:if test="count(.//spell) &gt; 0">
			<fo:table-row keep-with-next.within-column="always">
				<fo:table-cell number-columns-spanned="11" padding-top="1pt">
					<xsl:call-template name="attrib">
						<xsl:with-param name="attribute" select="'spelllist.header'"/>
					</xsl:call-template>
					<fo:block font-size="12pt">
						LEVEL <xsl:value-of select="@number"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<xsl:call-template name="spells.header.column.titles">
				<xsl:with-param name="columnOne" select="$columnOneTitle"/>
			</xsl:call-template>
			<xsl:apply-templates select="spell" mode="details">
				<xsl:with-param name="columnOne" select="$columnOne"/>
			</xsl:apply-templates>
			<fo:table-row height="1mm"/>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - KNOWN SPELL HEADER ROW
====================================
====================================-->
	<xsl:template name="spells.known.header.row">
		<xsl:param name="title" select="''"/>
		<xsl:param name="columnOne" select="''"/>
		<xsl:param name="details" select="'true'"/>
		<fo:table-column column-width="11mm"/>
		<fo:table-column column-width="37mm"/>
		<!-- name -->
		<fo:table-column column-width="6mm"/>
		<!-- dc -->
		<fo:table-column column-width="18mm"/>
		<!-- saving throw -->
		<fo:table-column column-width="8mm"/>
		<!-- time -->
		<fo:table-column column-width="32mm"/>
		<!-- duration -->
		<fo:table-column column-width="16mm"/>
		<!-- range -->
		<fo:table-column column-width="9mm"/>
		<!-- comp -->
		<fo:table-column column-width="18mm"/>
		<!-- SR -->
		<fo:table-column column-width="15mm"/>
		<!-- school -->
		<fo:table-column column-width="20mm"/>
		<!-- source -->
		<fo:table-header>
			<fo:table-row>
				<fo:table-cell number-columns-spanned="11" padding-top="1pt">
					<xsl:call-template name="attrib">
						<xsl:with-param name="attribute" select="'spelllist.header'"/>
					</xsl:call-template>
					<fo:block font-size="12pt">
						<xsl:value-of select="$title"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<xsl:if test="$details = 'true'">
				<xsl:call-template name="spells.header.column.titles">
					<xsl:with-param name="columnOne" select="$columnOne"/>
				</xsl:call-template>
			</xsl:if>
		</fo:table-header>
		<fo:table-footer>
			<fo:table-row>
				<fo:table-cell number-columns-spanned="11" padding-top="1pt">
					<xsl:call-template name="attrib">
						<xsl:with-param name="attribute" select="'spelllist.footer'"/>
					</xsl:call-template>
					<fo:block font-size="5pt">* =Domain/Speciality Spell
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-footer>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - KNOWN SPELL HEADER COLUMN TITLES
====================================
====================================-->
	<xsl:template name="spells.header.column.titles">
		<xsl:param name="columnOne" select="''"/>
		<fo:table-row keep-with-next.within-column="always">
			<xsl:call-template name="attrib">
				<xsl:with-param name="attribute" select="'spelllist.levelheader'"/>
			</xsl:call-template>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt" font-weight="bold">
					<xsl:value-of select="$columnOne"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt" font-weight="bold">Name</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt" font-weight="bold">DC</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt" font-weight="bold">Saving Throw</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt" font-weight="bold">Time</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt" font-weight="bold">Duration</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt" font-weight="bold">Range</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt" font-weight="bold">Comp.</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt" font-weight="bold">Spell Resistance</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt" font-weight="bold">School</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt" font-weight="bold">Source</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - SPELL DETAILS
====================================
====================================-->
	<xsl:template match="spell" mode="details">
		<xsl:param name="columnOne" select="'Times'"/>
		<xsl:variable name="shade">
			<xsl:choose>
				<xsl:when test="position() mod 2 = 0">darkline</xsl:when>
				<xsl:otherwise>lightline</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<fo:table-row keep-with-next.within-column="always">
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat('spelllist.', $shade)"/></xsl:call-template>
			<xsl:choose>
				<xsl:when test="$columnOne = 'Times'">
					<xsl:choose>
						<xsl:when test="times_memorized &gt;= 0">
							<fo:table-cell padding-top="0pt">
								<fo:block text-align="start" font-size="8pt" font-family="ZapfDingbats">
									<xsl:call-template name="for.loop">
										<xsl:with-param name="count" select="times_memorized"/>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
						</xsl:when>
						<xsl:otherwise>
							<fo:table-cell padding-top="1pt" text-align="start">
								<fo:block text-align="start" font-size="7pt">At Will</fo:block>
							</fo:table-cell>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="$columnOne = 'Boxes'">
					<fo:table-cell padding-top="0pt">
						<fo:block text-align="start" font-size="8pt" font-family="ZapfDingbats">
							<xsl:call-template name="for.loop">
								<xsl:with-param name="count" select="5"/>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
				</xsl:when>
				<xsl:when test="$columnOne = 'PowerPoints'">
					<fo:table-cell padding-top="0pt">
						<fo:block text-align="start" font-size="8pt">
							<xsl:variable name="ppcount" select="((../@number)*2)-1"/>
							<xsl:choose>
								<xsl:when test="number($ppcount) &gt; 0">
									<xsl:value-of select="$ppcount"/>
								</xsl:when>
								<xsl:otherwise>0/1</xsl:otherwise>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
				</xsl:when>
			</xsl:choose>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="7pt">
					<xsl:value-of select="bonusspell"/>
					<xsl:value-of select="name"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt">
					<xsl:value-of select="dc"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt">
					<xsl:value-of select="saveinfo"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt">
					<xsl:value-of select="castingtime"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt">
					<xsl:value-of select="duration"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt">
					<xsl:value-of select="range"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt">
					<xsl:value-of select="components"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt">
					<xsl:value-of select="spell_resistance"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt">
					<xsl:value-of select="school/fullschool"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt">
				<fo:block text-align="start" font-size="5pt">
					<xsl:value-of select="source/sourceshort"/>
					<xsl:text>: </xsl:text>
					<xsl:value-of select="source/sourcepage"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
		<fo:table-row>
			<xsl:call-template name="attrib"><xsl:with-param name="attribute" select="concat('spelllist.', $shade)"/></xsl:call-template>
			<fo:table-cell padding-top="1pt"/>
			<fo:table-cell padding-top="1pt" number-columns-spanned="5">
				<fo:block text-align="start" font-size="5pt">
					<fo:inline font-style="italic">Effect: </fo:inline>
					<xsl:value-of select="effect"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-top="1pt" number-columns-spanned="5">
				<fo:block text-align="start" font-size="5pt">
					<fo:inline font-style="italic">Target: </fo:inline>
					<xsl:value-of select="target"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - MEMORIZED SPELLS
====================================
====================================-->
	<xsl:template match="memorized_spells">
		<xsl:if test="count(.//spell) &gt; 0">
			<fo:block break-before="page">
				<xsl:apply-templates mode="spells.memorized"/>
			</fo:block>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - SPELLS MEMORIZED HEADER
====================================
====================================-->
	<xsl:template name="spells.memorized.header">
		<xsl:param name="title" select="'Unknown'"/>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="190mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell padding-top="1pt">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'spells.memorized.header'"/>
						</xsl:call-template>
						<fo:block font-size="12pt">
							<xsl:value-of select="$title"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - RACIAL_INNATE_MEMORIZED (SPELLS.MEMORIZED)
====================================
====================================-->
	<xsl:template match="racial_innate_memorized" mode="spells.memorized">
		<xsl:if test="count(.//spell) &gt; 0">
			<xsl:call-template name="spells.memorized.header">
				<xsl:with-param name="title" select="'Innate'"/>
			</xsl:call-template>
			<fo:table table-layout="fixed" space-after="5mm">
				<fo:table-column column-width="8mm"/>
				<fo:table-column column-width="30mm"/>
				<fo:table-body>
					<xsl:apply-templates mode="spells.memorized"/>
				</fo:table-body>
			</fo:table>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - CLASS_INNATE_MEMORIZED (SPELLS.MEMORIZED)
====================================
====================================-->
	<xsl:template match="class_innate_memorized" mode="spells.memorized">
		<xsl:if test="count(.//spell) &gt; 0">
			<xsl:apply-templates mode="spells.memorized.innate"/>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - SPELLBOOK (SPELLS.MEMORIZED.INNATE)
====================================
====================================-->
	<xsl:template match="spellbook" mode="spells.memorized.innate">
		<xsl:if test="count(.//spell) &gt; 0">
			<xsl:call-template name="spells.memorized.header">
				<xsl:with-param name="title" select="concat(@name, ' Innate Spells')"/>
			</xsl:call-template>
			<fo:table table-layout="fixed">
				<fo:table-column column-width="8mm"/>
				<fo:table-column column-width="30mm"/>
				<fo:table-body>
					<xsl:apply-templates mode="spells.memorized"/>
				</fo:table-body>
			</fo:table>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - SPELLBOOK (SPELLS.MEMORIZED)
====================================
====================================-->
	<xsl:template match="spellbook" mode="spells.memorized">
		<xsl:if test="count(.//spell) &gt; 0">
			<fo:table table-layout="fixed" space-before="4mm">
				<fo:table-column column-width="38mm"/>
				<fo:table-column column-width="38mm"/>
				<fo:table-column column-width="38mm"/>
				<fo:table-column column-width="38mm"/>
				<fo:table-column column-width="38mm"/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell padding-top="1pt" number-columns-spanned="5">
							<xsl:call-template name="attrib">
								<xsl:with-param name="attribute" select="'spells.memorized.header'"/>
							</xsl:call-template>
							<fo:block font-size="10pt">
								Spellbook: <xsl:value-of select="@name"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<xsl:apply-templates mode="spells.memorized"/>
				</fo:table-body>
			</fo:table>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - CLASS (SPELLS.MEMORIZED)
====================================
====================================-->
	<xsl:template match="class" mode="spells.memorized">
		<xsl:if test="count(.//spell) &gt; 0">
			<fo:table-row>
				<fo:table-cell padding-top="1pt" number-columns-spanned="5">
					<xsl:call-template name="attrib">
						<xsl:with-param name="attribute" select="'spells.memorized.header'"/>
					</xsl:call-template>
					<fo:block font-size="8pt">
						<xsl:value-of select="@spelllistclass"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row>
				<xsl:apply-templates select="level[@number &lt; 5]" mode="spells.memorized"/>
			</fo:table-row>
			<fo:table-row>
				<xsl:apply-templates select="level[@number &gt;= 5]" mode="spells.memorized"/>
			</fo:table-row>
		</xsl:if>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - LEVEL (SPELLS.MEMORIZED)
====================================
====================================-->
	<xsl:template match="level" mode="spells.memorized">
		<fo:table-cell padding-top="1pt">
			<fo:block font-size="5pt">
				<xsl:if test="count(.//spell) &gt; 0">
					<fo:table table-layout="fixed">
						<fo:table-column column-width="8mm"/>
						<fo:table-column column-width="30mm"/>
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell padding-top="1pt" number-columns-spanned="2">
									<xsl:call-template name="attrib">
										<xsl:with-param name="attribute" select="'spells.memorized.level'"/>
									</xsl:call-template>
									<fo:block font-size="7pt">
										Level <xsl:value-of select="@number"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<xsl:apply-templates mode="spells.memorized"/>
						</fo:table-body>
					</fo:table>
				</xsl:if>
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - SPELLS (SPELLS.MEMORIZED)
====================================
====================================-->
	<xsl:template match="spell" mode="spells.memorized">
		<fo:table-row>
			<xsl:choose>
				<xsl:when test="times_memorized &gt;= 0">
					<fo:table-cell padding-top="0pt" text-align="end">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'spells.memorized'"/>
						</xsl:call-template>
						<fo:block font-size="7pt" font-family="ZapfDingbats">
							<xsl:call-template name="for.loop">
								<xsl:with-param name="count" select="times_memorized"/>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
				</xsl:when>
				<xsl:otherwise>
					<fo:table-cell padding-top="1pt">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'spells.memorized'"/>
						</xsl:call-template>
						<fo:block font-size="6pt">At Will</fo:block>
					</fo:table-cell>
				</xsl:otherwise>
			</xsl:choose>
			<fo:table-cell>
				<fo:block font-size="7pt">
					<xsl:call-template name="attrib">
						<xsl:with-param name="attribute" select="'spells.memorized'"/>
					</xsl:call-template>
					<xsl:value-of select="bonusspell"/>
					<xsl:value-of select="name"/> (DC:<xsl:value-of select="dc"/>)
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>

	
	<!--
====================================
====================================
	TEMPLATE - BIO
====================================
====================================-->
	<xsl:template name="bio.entry">
		<xsl:param name="title"/>
		<xsl:param name="value"/>
		<fo:table-row>
			<fo:table-cell padding-top="1pt" height="9pt">
				<xsl:call-template name="attrib">
					<xsl:with-param name="attribute" select="'bio'"/>
				</xsl:call-template>
				<fo:block font-size="9pt">
					<xsl:value-of select="$value"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
		<fo:table-row>
			<fo:table-cell padding-top="0.5pt">
				<xsl:call-template name="attrib">
					<xsl:with-param name="attribute" select="'bio.title'"/>
				</xsl:call-template>
				<fo:block font-size="6pt"><xsl:value-of select="$title"/></fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - BIO
====================================
====================================-->
	<xsl:template match="basics" mode="bio">
		<!-- BEGIN BIO Pages -->
		<xsl:if test="string-length(translate(normalize-space(concat(description,bio)), ' ', '')) &gt; 0">
			<fo:page-sequence>
				<xsl:attribute name="master-reference">Portrait</xsl:attribute>
				<xsl:call-template name="page.footer"/>
				<fo:flow flow-name="body" font-size="8pt">
					<fo:block font-size="14pt" break-before="page" span="all">
						<xsl:call-template name="attrib">
							<xsl:with-param name="attribute" select="'bio'"/>
						</xsl:call-template>
						<xsl:value-of select="name"/>
						<xsl:if test="string-length(followerof) &gt; 0">- <xsl:value-of select="followerof"/>
						</xsl:if>
					</fo:block>
					<fo:block>
						<fo:table table-layout="fixed">
							<fo:table-column column-width="94mm"/>
							<xsl:if test="string-length(portrait) &gt; 0">
								<fo:table-column column-width="2mm"/>
								<fo:table-column column-width="94mm"/>
							</xsl:if>
							<fo:table-body>
								<fo:table-row>
									<xsl:if test="string-length(portrait) &gt; 0">
										<fo:table-cell display-align="center" number-rows-spanned="36">
											<xsl:call-template name="attrib">
												<xsl:with-param name="attribute" select="'picture'"/>
											</xsl:call-template>
											<fo:block start-indent="1mm" height="100mm">
												<xsl:variable name="portrait_file" select="portrait"/>
												<fo:external-graphic src="file:{$portrait_file}" width="92mm" scaling="uniform"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell number-rows-spanned="36"/>
									</xsl:if>
									<fo:table-cell>
										<xsl:call-template name="attrib">
											<xsl:with-param name="attribute" select="'bio'"/>
										</xsl:call-template>
										<fo:block font-size="9pt">
											<xsl:value-of select="race"/>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell padding-top="1pt">
										<xsl:call-template name="attrib">
											<xsl:with-param name="attribute" select="'bio.title'"/>
										</xsl:call-template>
										<fo:block font-size="6pt">RACE</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'AGE'"/>
									<xsl:with-param name="value" select="age"/>
								</xsl:call-template>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'GENDER'"/>
									<xsl:with-param name="value" select="gender/long"/>
								</xsl:call-template>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'VISION'"/>
									<xsl:with-param name="value" select="vision/all"/>
								</xsl:call-template>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'ALIGNMENT'"/>
									<xsl:with-param name="value" select="alignment/long"/>
								</xsl:call-template>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'DOMINANT HAND'"/>
									<xsl:with-param name="value" select="handed"/>
								</xsl:call-template>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'HEIGHT'"/>
									<xsl:with-param name="value" select="height/total"/>
								</xsl:call-template>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'WEIGHT'"/>
									<xsl:with-param name="value" select="weight/weight_unit"/>
								</xsl:call-template>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'EYE COLOUR'"/>
									<xsl:with-param name="value" select="eyes/color"/>
								</xsl:call-template>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'SKIN COLOUR'"/>
									<xsl:with-param name="value" select="skin/color"/>
								</xsl:call-template>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'HAIR'"/>
									<xsl:with-param name="value" select="concat(hair/color, ', ', hair/length)"/>
								</xsl:call-template>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'PHOBIAS'"/>
									<xsl:with-param name="value" select="phobias"/>
								</xsl:call-template>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'PERSONALITY TRAITS'"/>
									<xsl:with-param name="value">
										<xsl:for-each select="personality/trait">
											<xsl:if test="position() &gt; 1">, </xsl:if>
											<xsl:value-of select="."/>
										</xsl:for-each>
									</xsl:with-param>
								</xsl:call-template>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'INTERESTS'"/>
									<xsl:with-param name="value" select="interests"/>
								</xsl:call-template>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'SPOKEN STYLE'"/>
									<xsl:with-param name="value" select="concat(speechtendency, ', ', catchphrase)"/>
								</xsl:call-template>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'RESIDENCE'"/>
									<xsl:with-param name="value" select="residence"/>
								</xsl:call-template>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'LOCATION'"/>
									<xsl:with-param name="value" select="location"/>
								</xsl:call-template>
								<xsl:call-template name="bio.entry">
									<xsl:with-param name="title" select="'REGION'"/>
									<xsl:with-param name="value" select="region"/>
								</xsl:call-template>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block font-size="14pt" font-weight="bold" space-before="5mm" span="all">
						Description:
					</fo:block>
					<xsl:for-each select="description/para">
						<fo:block font-size="9pt" text-indent="5mm" span="all">
							<xsl:value-of select="."/>
						</fo:block>
					</xsl:for-each>
					<fo:block font-size="14pt" font-weight="bold" span="all">
						Biography:
					</fo:block>
					<xsl:for-each select="bio/para">
						<fo:block font-size="9pt" text-indent="5mm" span="all">
							<xsl:value-of select="."/>
						</fo:block>
					</xsl:for-each>
				</fo:flow>
			</fo:page-sequence>
		</xsl:if>
		<!-- END BIO Pages -->
	</xsl:template>
	<!--
====================================
====================================
	TEMPLATE - CHARACTER NOTES
====================================
====================================-->
	<xsl:template match="notes" mode="bio">
		<!-- BEGIN CHARACTER NOTES Pages -->
		<xsl:if test="count(.//note) &gt; 0">
			<fo:page-sequence master-reference="Portrait 2 Column">
				<xsl:call-template name="page.footer"/>
				<fo:flow flow-name="body" font-size="8pt">
					<fo:block font-size="14pt" font-weight="bold" space-after.optimum="2mm" break-before="page" span="all">
						Notes:
					</fo:block>
					<xsl:for-each select="note">
						<fo:block font-size="12pt" space-after.optimum="2mm" space-before.optimum="5mm">
							<xsl:value-of select="name"/>:
						</fo:block>
						<xsl:for-each select="value/para">
							<fo:block font-size="9pt" text-indent="5mm">
								<xsl:value-of select="."/>
							</fo:block>
						</xsl:for-each>
					</xsl:for-each>
				</fo:flow>
			</fo:page-sequence>
		</xsl:if>
		<!-- END CHARACTER NOTES Pages -->
	</xsl:template>
</xsl:stylesheet>
