; Current Ver: $Revision: 1.28 $
; Last Editor: $Author: ericbeaudoin $
; Last Edited: $Date: 2006/02/26 04:39:14 $
;
;	This script is licensed under the following license



; Script Created with Venis IX 2.2.3	http://www.spaceblue.com/venis/info.php (recomended)
; 										NSIS 2.04				http://nsis.sourceforge.net



; Known issues
; ver fixed		Problem
; 1.5			PCGen install directory is not romoved under full uninstall
; 1.6			Need option to keep character and customsources directory
; 1.24			PCGen 5.9.3 Alpha
;			Reviewed and corrected the data sections
;			The command prompt boxes are now launched minimized from the shortcuts
;			There is no longer any GMGen notion in PCGen. I've renamed the section
;				Optionnal Plugins
; 1.25			Test to see if the commit notification works
; 1.26			Version for release 5.9.4 Alpha

; Outstanding Issues
;		Need file association for .pcg files
;		java -Dpcgen.inputfile=Ilse.pcg -Dpcgen.outputfile=Ilse.pdf -Dpcgen.templatefile=csheet_fantasy_std_blackandwhite.xslt -jar pcgen.jar



; Internal Version history
; 1.0 	First version uploaded to CVS
; 1.1 	Updated PDF Plug-in to only copy PDF output templates if PDF is selected
; 1.2 	Pointed the desktop shortcut to pcgen_high_mem.bat and added start /min to batches
; 1.3 	Changed path to force ${APPDIR} should solve problems with install directory (fail)
; 1.4 	Pointed shortcuts to icons in local, created internal version
; 1.5 	Changed location of uninstaller to fix full uninstall problem
; 1.6 	Added backup feature to save character, customsources, options if user wishes
; 1.7 	Changed to use of constant for simple version ie 5714 now ${SIMPVER}
; 1.8 	Added compilation instructions
; 1.9 	Added variables for OutDir and SrcDir {SpaceMonkey}
;     	Changed OutName to reflect the standard names for PCGen Windows installs
;     	Removed Section "Alderac Entertainment Group" from alpha
;     	Added missing steps in the instructions
;     	Corrected a typo with the pcgen-release-notes-${SIMPVER}
; 1.10	Version used for pcgen580rc1'
;		Alpha data sets are now separated
; 1.11	Updated registry information, so that their is a PCGen\PCGen ver listing
;		Removed Alpha data sources from main install, as per SpaceMonkey
; 1.12	Fixed the build instructions that I broke, testing with pcgen580rc2 to
;		ensure that these really work (glasswalkertheurge)
;		verified seperated alpha install
; 1.13  RC3 Release
;       Added Behemoth3 in d20ogl (SpaceMonkey)
; 1.16  RC4 Release (SpaceMonkey)
;       Added !insertmacro MUI_PAGE_DIRECTORY (I though I had already fixed that...)
; 1.17	Changed the OverVer tp "1.6", this will allow users to use Java 1.5 on their
;		machines, unfortunatly minor rev levels are not tracked, so if some future
;		1.5.x does not work, I will have to revisit this.  (GlassWalkerTheurge)



; Instructions for compilation
;  1.	Download latest version
;  2.	Extract version to ${SrcDir}\PCGen_${SIMPVER}b
;		[where ${SIMPVER} is the simple version number i.e. "5714".]
;		These directorys will now be refered to	as b and c.
;  a.	If you choose another path, you must change ${SrcDir} to the match
;  3.	create a directory called ${SrcDir}\PCGen_${SIMPVER}c
;  4.	create a directory called ${SrcDir}\Local and put all the icons and the splash screen in it
;  5.	copy the data directory from b to c
;  6.	erase everything in the b data directory except custom sources
;  7.	erase the custom sources directory in the data directory of c
;  8.	create a "plugin" directory in the c directory
;  9.	create the following directorys in the plugin directory on c;
;		gmgen, pdf, skin
; 10.	in the gmgen directory on c, create a directory called plugins
; 11.	in this directory, move everthing from the b plugin directory except
;		CharacterSheet.jar, the Export*.jar, GameMode*.jar and Lst*.jar
; 12.	in the pdf directory, create a lib and an outputsheets directories
; 13.	in the lib directory move the following file from the b lib directory;
;		fop.jar, fop.license.txt, jdom.jar, jdom.license.txt, xml-apis.jar,
;		xml-apis.readme.txt
; 14.	in the outputsheets directory, move all the \pdf\ folder from outputsheets in the b dir
; 15.	in the skin directory, create a lib directory
; 16.	from the b lib directory move everything except the following files;
;		jep.jar
; 17.	go to the b docs acknowledgments directory
; 18.	remove the html tags from the ogl license and the gnu license file
; 19.	save these files as one text file called PCGenLicense.txt
; 20.	the alpha source files should be placed in the data directory
; 21.	you can now build both the primary and the alpha source build.



; Begin Script ----------------------------------------------------------------------------
; Define constants
!define APPNAME "PCGen"
!define SIMPVER "595"
!define APPNAMEANDVERSION "PCGen 5.9.5"
!define APPDIR "PCGen${SIMPVER}"
!define TargetVer "1.4"
!define OverVer "1.6"
!define OutName "pcgen${SIMPVER}_win_install"
;!define OutDir "C:\Documents and Settings\Lisa\Desktop"
!define OutDir "D:\CVS\release"
;!define SrcDir "D:\@Download\PCGen"
!define SrcDir "D:\CVS\release\nsis_dir"

; Main Install settings
Name "${APPNAMEANDVERSION}"
InstallDir "$PROGRAMFILES\${APPNAME}"
InstallDirRegKey HKLM "Software\${APPNAME}\${APPDIR}" ""
OutFile "${OutDir}\${OutName}.exe"
SetCompressor lzma
CRCCheck on

; Install Type Settings
InstType "Full Install"
InstType "Average Install"
InstType "Average All SRD"
InstType "Min - SRD"
InstType "Min - SRD 3.5"
InstType "Min - MSRD"
; InstType "Alpha Data Sets"

;	Look and style
ShowInstDetails show
InstallColors FF8080 000030
XPStyle on
Icon "${SrcDir}\Local\PCGen2.ico"


; Modern interface settings
!include "MUI.nsh"

!define MUI_ABORTWARNING
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "${SrcDir}\PCGen_${SIMPVER}b\docs\acknowledgments\PCGenLicense.txt"
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

; Set languages (first is default language)
!insertmacro MUI_LANGUAGE "English"
!insertmacro MUI_RESERVEFILE_LANGDLL

; Set User Variables
Var "JREVer"
Var "JDKVer"

Function .onInit

	;Checks for versions of Java between TargetVer and OverVer
	; this can be changed once PCGen is stable on Java 1.5 and above

	ClearErrors
	ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
	StrCpy $JREVer $R0
	ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R0" "JavaHome"
	IfErrors 0 FoundVM

	ClearErrors
	ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
	StrCpy $JDKVer $R0
	ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$R0" "JavaHome"
	IfErrors 0 FoundVM

	ClearErrors
	ReadEnvStr $R0 "JAVA_HOME"
	IfErrors 0 FoundVM

	DetailPrint "Java not found"
	Sleep 800
	MessageBox MB_ICONEXCLAMATION|MB_YESNO \
		'Could not find a Java Runtime Environment installed on your computer. \
		$\nWithout it you cannot run "${APPNAME}". \
		$\n$\nWould you like to visit the Java website to download it?' \
		IDNO End
	ExecShell open "http://java.com/en/download/windows_automatic.jsp"
	Goto End

	FoundVM:
	DetailPrint "Java was found"
	DetailPrint $JREVer
	DetailPrint $JDKVer
	IntCmp $JREVer ${TargetVer} VMGood VMOld VMOver
	IntCmp $JDKVer ${TargetVer} VMGood VMOld VMOver
	Goto Error

	VMOver:
	IntCmp $JREVer ${OverVer} VMHigh VMGood VMHigh
	IntCmp $JDKVer ${OverVer} VMHigh VMGood VMHigh
	Goto Error

	VMHigh:
	DetailPrint "Java Version Bad"
	Sleep 800
	MessageBox MB_ICONEXCLAMATION|MB_YESNO \
		'Found Java Runtime Environment installed on your computer. \
		$\nVersion was not "${TargetVer}". \
		$\n$\nWould you like to visit the Java website to download newest?' \
		IDNO Error
	ExecShell open "http://java.com/en/download/windows_automatic.jsp"
	Goto Error

	VMGood:
	DetailPrint "Java Version Good"
	Goto End

	VMOld:
	DetailPrint "Java Version Bad"
	Sleep 800
	MessageBox MB_ICONEXCLAMATION|MB_YESNO \
		'Found Java Runtime Environment installed on your computer. \
		$\nVersion was not "${TargetVer}". \
		$\n$\nWould you like to visit the Java website to download newest?' \
		IDNO Error
	ExecShell open "http://java.com/en/download/windows_automatic.jsp"
	Goto Error

	Error:
	MessageBox MB_YESNO|MB_ICONQUESTION "PCGen will most likely not run, do you wish to install it anyway?" IDYES End
	Abort "Error during Java Detection"
	Goto End

	End:

 FunctionEnd

Section "PCGen" Section1

	SectionIn RO

	; Set Section properties
	SetOverwrite ifnewer

	; Set Section Files and Shortcuts
	SetOutPath "$INSTDIR\${APPDIR}\"
	File /r "${SrcDir}\PCGen_${SIMPVER}b\*.*"

SectionEnd

SubSection /e "Data" Section2

SubSection "d20OGL"

	Section "Alderac Entertainment Group"

	SectionIn 1 2

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\alderacentertainmentgroup\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\alderacentertainmentgroup\*.*"

	SectionEnd

	Section "Avalanch Press"

	SectionIn 1 2

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\avalanchepress\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\avalanchepress\*.*"

	SectionEnd

	Section "Bastion Press"

	SectionIn 1 2

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\bastionpress\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\bastionpress\*.*"

	SectionEnd

	Section "Battlefield Press"

	SectionIn 1 2

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\battlefieldpress\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\battlefieldpress\*.*"

	SectionEnd

	Section "Behemoth3"

	SectionIn 1 2

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\behemoth3\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\behemoth3\*.*"

	SectionEnd

	Section "Creative Mountain Games"

	SectionIn 1 2

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\creativemountaingames\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\creativemountaingames\*.*"

	SectionEnd

	Section "Fantasy Community Council"

	SectionIn 1 2

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\doghouserules\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\doghouserules\*.*"

	SectionEnd

	Section "Dog House Rules"

	SectionIn 1 2

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\fantasycommunitycouncil\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\fantasycommunitycouncil\*.*"

	SectionEnd

	Section "Fantasy Flight Games"

	SectionIn 1 2

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\fantasyflightgames\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\fantasyflightgames\*.*"

	SectionEnd

	Section "Green Ronin Publishing"

	SectionIn 1 2

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\greenronin\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\greenronin\*.*"

	SectionEnd

	Section "Lion's Den Press"

	SectionIn 1 2

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\lionsdenpress\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\lionsdenpress\*.*"

	SectionEnd

	Section "Malhavoc Press"

	SectionIn 1 2

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\malhavocpress\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\malhavocpress\*.*"

	SectionEnd

	Section "Mongoose"

	SectionIn 1 2

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\mongoosepublishing\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\mongoosepublishing\*.*"

	SectionEnd

	Section "MSRD"

	SectionIn 1 2 3 6

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\msrd\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\msrd\*.*"

	SectionEnd

;	Section "RPGObjects"
;
;	SectionIn 1 2
;
;	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\rpgobjects\"
;	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\rpgobjects\*.*"
;
;	SectionEnd

	Section "SRD"

	SectionIn 1 2 3 4

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\srd\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\srd\*.*"

	SectionEnd

	Section "SRD 3.5"

	SectionIn 1 2 3 5

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\srd35\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\srd35\*.*"

	SectionEnd

	Section "Sword and Sorcery Studios"

	SectionIn 1 2

	SetOutPath "$INSTDIR\${APPDIR}\data\d20ogl\swordandsorcerystudios\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\d20ogl\swordandsorcerystudios\*.*"

	SectionEnd

SubSectionEnd

SubSection "Permissioned"

	Section "Alderac Entertainment Group"

	SectionIn 1

	SetOutPath "$INSTDIR\${APPDIR}\data\permissioned\alderacentertainmentgroup\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\permissioned\alderacentgroup\*.*"

	SectionEnd

	Section "Atlas Games"

	SectionIn 1

	SetOutPath "$INSTDIR\${APPDIR}\data\permissioned\atlasgames\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\permissioned\atlasgames\*.*"

	SectionEnd

	Section "Auran d20"

	SectionIn 1

	SetOutPath "$INSTDIR\${APPDIR}\data\permissioned\aurand20\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\permissioned\aurand20\*.*"

	SectionEnd

	Section "Avalanche Press"

	SectionIn 1

	SetOutPath "$INSTDIR\${APPDIR}\data\permissioned\avalanchepress\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\permissioned\avalanchepress\*.*"

	SectionEnd

	Section "Green Ronin Publishing"

	SectionIn 1

	SetOutPath "$INSTDIR\${APPDIR}\data\permissioned\greenronin\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\permissioned\greenronin\*.*"

	SectionEnd

	Section "Malhavoc Press"

	SectionIn 1

	SetOutPath "$INSTDIR\${APPDIR}\data\permissioned\malhavocpress\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\permissioned\malhavocpress\*.*"

	SectionEnd

	Section "Mongoose"

	SectionIn 1

	SetOutPath "$INSTDIR\${APPDIR}\data\permissioned\mongoose\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\permissioned\mongoose\*.*"

	SectionEnd

	Section "RPG Objects"

	SectionIn 1

	SetOutPath "$INSTDIR\${APPDIR}\data\permissioned\rpgobjects\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\permissioned\rpgobjects\*.*"

	SectionEnd

SubSectionEnd

SubSection "Alpha" 

	Section "Avalanch Press"

	SectionIn 1 7

	SetOutPath "$INSTDIR\${APPDIR}\data\alpha\avalanchepress\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\alpha\avalanchepress\*.*"

	SectionEnd

	Section "Bastion Press"

	SectionIn 1 7

	SetOutPath "$INSTDIR\${APPDIR}\data\alpha\bastionpress\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\alpha\bastionpress\*.*"

	SectionEnd

	Section "Behemoth3"

	SectionIn 1 7

	SetOutPath "$INSTDIR\${APPDIR}\data\alpha\behemoth3\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\alpha\behemoth3\*.*"

	SectionEnd

	Section "Dog House Rules"

	SectionIn 1 7

	SetOutPath "$INSTDIR\${APPDIR}\data\alpha\doghouserules\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\alpha\doghouserules\*.*"

	SectionEnd

	Section "Fantasy Flight Games"

	SectionIn 1 7

	SetOutPath "$INSTDIR\${APPDIR}\data\alpha\fantasyflightgames\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\alpha\fantasyflightgames\*.*"

	SectionEnd

	Section "Green Ronin Publishing"

	SectionIn 1 7

	SetOutPath "$INSTDIR\${APPDIR}\data\alpha\greenronin\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\alpha\greenronin\*.*"

	SectionEnd

	Section "Malhavoc Press"

	SectionIn 1 7

	SetOutPath "$INSTDIR\${APPDIR}\data\alpha\malhavocpress\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\alpha\malhavocpress\*.*"

	SectionEnd

	Section "Mongoose"

	SectionIn 1 7

	SetOutPath "$INSTDIR\${APPDIR}\data\alpha\mongoose\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\alpha\mongoose\*.*"

	SectionEnd

	Section "Necromancer Games"

	SectionIn 1 7

	SetOutPath "$INSTDIR\${APPDIR}\data\alpha\necromancergames\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\alpha\necromancergames\*.*"

	SectionEnd

	Section "Panda Head"

	SectionIn 1 7

	SetOutPath "$INSTDIR\${APPDIR}\data\alpha\pandahead\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\alpha\pandahead\*.*"

	SectionEnd

	Section "Parent's Basement Games"

	SectionIn 1 7

	SetOutPath "$INSTDIR\${APPDIR}\data\alpha\parentsbasementgames\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\alpha\parentsbasementgames\*.*"

	SectionEnd

	Section "Pinnacle Entertainment"

	SectionIn 1 7

	SetOutPath "$INSTDIR\${APPDIR}\data\alpha\pinnacleentertainment\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\alpha\pinnacleentertainment\*.*"

	SectionEnd

	Section "RPG Objects"

	SectionIn 1 7

	SetOutPath "$INSTDIR\${APPDIR}\data\alpha\rpgobjects\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\alpha\rpgobjects\*.*"

	SectionEnd

	Section "Soverign Press"

	SectionIn 1 7

	SetOutPath "$INSTDIR\${APPDIR}\data\alpha\sovereignpress\"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\data\alpha\sovereignpress\*.*"

	SectionEnd

SubSectionEnd

SubSectionEnd

SubSection /e "PlugIns" Section3

	Section "Skins"

	SectionIn 1 2 3
	SetOutPath "$INSTDIR\${APPDIR}\lib"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\plugin\skin\lib\*.*"

	SectionEnd

	Section "PDF"

	SectionIn 1 2 3
	SetOutPath "$INSTDIR\${APPDIR}\lib"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\plugin\pdf\lib\*.*"
	SetOutPath "$INSTDIR\${APPDIR}\outputsheets"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\plugin\pdf\outputsheets\*.*"

	SectionEnd

	Section "Optionnal Plugins"

	SectionIn 1 2 3
	SetOutPath "$INSTDIR\${APPDIR}\plugins"
	File /r "${SrcDir}\PCGen_${SIMPVER}c\plugin\gmgen\plugins\*.*"

	SectionEnd

SubSectionEnd

Section "-Local" Section4

	; Set Section properties
	SetOverwrite ifnewer

	; Set Section Files and Shortcuts
	SetOutPath "$INSTDIR\${APPDIR}\Local\"
	File /r "${SrcDir}\Local\*.*"

	; Create Shortcuts
	SetOutPath "$INSTDIR\${APPDIR}\"
	CreateDirectory "$SMPROGRAMS\PCGen\${APPDIR}"
	CreateShortCut "$DESKTOP\${APPDIR}.lnk" "$INSTDIR\${APPDIR}\pcgen.bat" "" \
				"$INSTDIR\${APPDIR}\Local\PCGen2.ico" 0 SW_SHOWMINIMIZED
	CreateShortCut "$SMPROGRAMS\PCGEN\${APPDIR}\${APPDIR}-Low.lnk" "$INSTDIR\${APPDIR}\pcgen_low_mem.bat" "" \
				"$INSTDIR\${APPDIR}\Local\PCGen.ico" 0 SW_SHOWMINIMIZED
	CreateShortCut "$SMPROGRAMS\PCGEN\${APPDIR}\${APPDIR}.lnk" "$INSTDIR\${APPDIR}\pcgen.bat" "" \
				"$INSTDIR\${APPDIR}\Local\pcgen2.ico" 0 SW_SHOWMINIMIZED
	CreateShortCut "$SMPROGRAMS\PCGen\${APPDIR}\Release Notes.lnk" "$INSTDIR\${APPDIR}\pcgen-release-notes-${SIMPVER}.html" "" "$INSTDIR\${APPDIR}\Local\knight.ico"
	CreateShortCut "$SMPROGRAMS\PCGen\${APPDIR}\News.lnk" "http://pcgen.sourceforge.net/01_news.php" "" "$INSTDIR\${APPDIR}\Local\queen.ico"
	CreateShortCut "$SMPROGRAMS\PCGen\${APPDIR}\uninstall-${APPDIR}.lnk" "$INSTDIR\uninstall-${APPDIR}.exe"
	CreateShortCut "$SMPROGRAMS\PCGen\${APPDIR}\Manual.lnk" "$INSTDIR\${APPDIR}\docs\index.html" "" "$INSTDIR\${APPDIR}\Local\castle.ico"

SectionEnd

Section -FinishSection

	WriteRegStr HKLM "Software\${APPNAME}\${APPDIR}" "" "$INSTDIR\${APPDIR}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPDIR}" "DisplayName" "${APPDIR}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPDIR}" "UninstallString" "$INSTDIR\uninstall-${APPDIR}.exe"
	WriteUninstaller "$INSTDIR\uninstall-${APPDIR}.exe"

SectionEnd

; Modern install component descriptions
!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
	!insertmacro MUI_DESCRIPTION_TEXT ${Section1} "This is the PCGen Core"
	!insertmacro MUI_DESCRIPTION_TEXT ${Section2} "This section installs the data sets you need"
	!insertmacro MUI_DESCRIPTION_TEXT ${Section3} "This section installs the plug ins you may need"
	!insertmacro MUI_DESCRIPTION_TEXT ${Section4} "This is for icons and such"
!insertmacro MUI_FUNCTION_DESCRIPTION_END

Section Uninstall

	; Delete self
	Delete "$INSTDIR\uninstall-${APPDIR}.exe"

	; Remove from registry...
	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPDIR}"
	DeleteRegKey HKLM "Software\${APPNAME}\${APPDIR}"
	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPDIR}_alpha"

	; Delete Desktop Shortcut
	Delete "$DESKTOP\${APPDIR}.lnk"
	; Delete Shortcut Directory
	RMDir /r "$SMPROGRAMS\PCGen\${APPDIR}"

	MessageBox MB_YESNO|MB_ICONEXCLAMATION "Do you wish a full uninstall? $\nThis will remove all versions of pcgen from your computer." IDYES Full IDNO Partial

	Full:
	; Clean up PCGen program directory
	RMDir /r "$INSTDIR"
	Goto End

	Partial:
	MessageBox MB_YESNO "Do you wish to save, your characters, custom sources etc? $\nAnswering no will delete ${APPDIR}." IDYES Save IDNO NoSave

	Save:
	CreateDirectory "$INSTDIR\${APPDIR}_Save"
	CreateDirectory "$INSTDIR\${APPDIR}_Save\characters"
	CreateDirectory "$INSTDIR\${APPDIR}_Save\customsources"
	CreateDirectory "$INSTDIR\${APPDIR}_Save\options"
	CopyFiles /SILENT "$INSTDIR\${APPDIR}\characters\*.*" "$INSTDIR\${APPDIR}_Save\characters\"
	CopyFiles /SILENT "$INSTDIR\${APPDIR}\data\customsources\*.*" "$INSTDIR\${APPDIR}_Save\customsources\"
	CopyFiles /SILENT "$INSTDIR\${APPDIR}\*.ini" "$INSTDIR\${APPDIR}_Save\options\"
	MessageBox MB_ICONINFORMATION|MB_OK "A shortcut will be created on your desktop to the saved files."
	CreateShortCut "$DESKTOP\${APPDIR}_Save.lnk" "$INSTDIR\${APPDIR}_Save"

	NoSave:
	; Clean up PCGen program directory
	RMDir /r "$INSTDIR\${APPDIR}"

	End:

SectionEnd

; eof