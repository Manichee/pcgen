package pcgen.gui;

import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import pcgen.core.Constants;
import pcgen.core.Globals;

import java.io.*;

/**
 * A pcGenGUITestCase is an XMLTestCase.  It is an abstract 
 * class that the pcGenGUI<x> series of tests extend.
 * 
 * The basic idea is to run a test by getting PCGen to use the 
 * PCG file in testsuite/PCGfiles to generate a XML representation 
 * and compare the expected XML in the testsuite/csheets folder to 
 * the generated XML in the testsuite/output folder  
 */
public abstract class pcGenGUITestCase extends XMLTestCase
{
	/**
	 * Constructor
	 */
	public pcGenGUITestCase()
	{
		// Empty Constructor
	}

	/**
	 * Standard JUnit style constructor
	 * @param name
	 */
	public pcGenGUITestCase(String name) {
		super(name);
	}

	/**
	 * Set the JAXP factories to use the Xerces parser
	 * - declare to throw Exception as if this fails then 
	 * all the tests will fail, and JUnit copes with these 
	 * Exceptions for us.
	 */
	public void setUp() throws Exception {
		super.setUp();
		XMLUnit.setControlParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		
		/* 
		 * This next line is strictly not required - if no test parser is
		 * explicitly specified then the same factory class will be used for
		 * both test and control
		 */
		XMLUnit.setTestParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");

		XMLUnit.setSAXParserFactory("org.apache.xerces.jaxp.SAXParserFactoryImpl");
		XMLUnit.setTransformerFactory("org.apache.xalan.processor.TransformerFactoryImpl");
	}

	/**
	 * Run the test
	 * @param character The PC
	 * @param mode The game mode
	 * @throws Exception
	 */
	public void runTest(String character, String mode) throws Exception	{
		System.out.println("RUNTEST with the character: " + character + " and the game mode: " + mode);
		// Delete the old generated output for this test 
		new File("testsuite/output/" + character + ".xml").delete();
		// Set the pcc location to "data"
		String pccLoc = "data";
		try {
			// Read in options.ini and override the pcc location if it exists
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("options.ini"), "UTF-8"));
			while (br.ready()) {
				String line = br.readLine();
				if (line != null && line.startsWith("pcgen.options.pccFilesLocation="))	{
					pccLoc = line.substring(31);
					break;
				}
			}
			br.close();
		}
		catch (IOException e) {
			// Ignore, see method comment
		}
		// make a copy of the options.ini file
		new File("options.ini").renameTo(new File("options.ini.junit"));

		// The String holder for the XML of the expected result
		String expected;
		// The String holder for the XML of the actual result
		String actual;
		/* 
		 * Override the pcc location, game mode and several other properties in 
		 * the options.ini file
		 */
		try	{
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("options.ini"), "UTF-8"));
			bw.write("pcgen.options.game=" + mode + "\r\n");
			if (pccLoc != null)	{
				System.out.println("Using PCC Location of '" + pccLoc + "'.");
				bw.write("pcgen.options.pccFilesLocation=" + pccLoc + "\r\n");
			}
			bw.write("pcgen.files.pcgenCustomDir=testsuite\\\\customdata\r\n");
			bw.close();

			System.setProperty("pcgen.templatefile", "testsuite/base.xml");
			System.setProperty("pcgen.inputfile", "testsuite/PCGfiles/" + character	+ Constants.s_PCGEN_CHARACTER_EXTENSION);
			System.setProperty("pcgen.outputfile", "testsuite/output/" + character + ".xml");

			// Fire off PCGen, which will produce an XML file 
			pcGenGUI.main(Globals.EMPTY_STRING_ARRAY);

			// Read in the actual XML produced by PCGen
			actual = readFile(new File("testsuite/output/" + character + ".xml"));
			// Read in the expected XML
			expected = readFile(new File("testsuite/csheets/" + character + ".xml"));
		}
		finally	{
			// Make sure we don't delete the options.ini no matter what happens!
			new File("options.ini").delete();
			new File("options.ini.junit").renameTo(new File("options.ini"));
		}

		// Do the XML comparison
		assertXMLEqual(expected, actual);
	}

	/**
	 * Read the XML file and return it as a String
	 * @param outputFile
	 * @return String
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String readFile(File outputFile) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(outputFile), "UTF-8"));
		StringBuffer output = new StringBuffer();
		String line = br.readLine();
		while (line != null) {
			output.append(line).append("\n");
			line = br.readLine();
		}
		return output.toString();
	}

}