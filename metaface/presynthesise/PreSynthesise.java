package metaface.presynthesise;
import java.util.*;
import java.io.*;
import metaface.dsp.*;
import metaface.nlp.*;
import metaface.server.*;
import metaface.mpeg4.*;

/**
 *  This class allows pre-rendering of VHML files into VHMLOutput files.
 *  VHMLOutput files are in the form of: <BR/>
 *  <code>
 * &lt;eca_synthesis&gt;<br/>
 *  &nbsp;&nbsp;&lt;text&gt;the text spoken by the ECA&lt;/text&gt;<br/>
 *  &nbsp;&nbsp;&lt;url target="name.of.browser.frame"&gt;http://some.url.here&lt;/url&gt;
 *  <br/>
 *  &nbsp;&nbsp;&lt;url target="another.browser.frame"&gt;http://another.url.here&lt;/url&gt;
 *  <br/>
 *  &nbsp;&nbsp;....<br/>
 *  &nbsp;&nbsp;&lt;phonemes&gt;<br/>
 *  &nbsp;&nbsp;&nbsp;&nbsp;A list of the ECA phonemes for speech<br/>
 *  &nbsp;&nbsp;&lt;phonemes&gt;<br/>
 *  &nbsp;&nbsp;&lt;faps&gt;<br/>
 *  &nbsp;&nbsp;&nbsp;&nbsp;A list of the MPEG-4 FAPs for facial animation<br/>
 *  &nbsp;&nbsp;&lt;faps&gt;<br/>
 *  &lt;/eca_synthesis&gt;<br/>
 *  </code><p>
 *
 *  Follow this link to retrieve the Document Type Definition 
 *  <A HREF="../../resources/vhml_output.dtd">vhml_output.dtd</A> Follow this link
 *  to retrieve an example XML file <A HREF="../../resources/vhml_example.xml">
 *  vhml_example.xml</A>
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        metaface.server.VHMLParser
 *@see        metaface.client.VHMLOutputReader
 */
public class PreSynthesise {
	/**
	 *  The input path of VHML files
	 */
	private String input;
	/**
	 *  The output path of VHMLOutput files
	 */
	private String output;
	/**
	 *  The number of files to render
	 */
	private int numFiles;
	/**
	 *  The VHML parser
	 */
	private VHMLParser parser;
	/**
	 *  The path to the TTS library files
	 */
	private String ttspath;
	/**
	 *  The DSP
	 */
	private DigitalSignalProcessor dsp;
	/**
	 *  The NLP
	 */
	private NaturalLanguageProcessor nlp;


	/**
	 *  The mainline of the class
	 *
	 *@param  argv  Command line arguments
	 */
	public static void main(String argv[]) {
		if (argv.length != 2) {
			System.out.println("USAGE:   java PreSynthesise <input directory> <output directory>");
			System.out.println("EXAMPLE: c:\\j2sdk1.4.1_02\\bin\\java PreSynthesise c:\\vhml c:\\output");
		} else {
			new PreSynthesise(argv[0], argv[1]);
		}
	}


	/**
	 *  Constructor for the PreSynthesise object
	 *
	 *@param  input_path   The directory of the VHML files to render
	 *@param  output_path  The directory of where to place VHMLOutput files
	 */
	public PreSynthesise(String input_path, String output_path) {
		input = input_path;
		output = output_path;
		ttspath = "../Libraries";
		synthesise();
	}


	/**
	 *  Constructor for the PreSynthesise object
	 *
	 *@param  input_path   The directory of the VHML files to render
	 *@param  output_path  The directory of where to place VHMLOutput files
	 *@param  path         The path to the TTS library files
	 */
	public PreSynthesise(String input_path, String output_path, String path) {
		input = input_path;
		output = output_path;
		ttspath = path;
		synthesise();
	}


	/**
	 *  Synthesise VHMLOutput files from VHML files
	 */
	private void synthesise() {
		File dir;
		int i;
		int j;
		File[] files;
		FileWriter fw;
		String speechtext = "";
		Vector faps;
		Vector phonemes;
		Hashtable browserupdates;
		String responsetext;
		char[] buffer;
		String target;
		String url;
		Enumeration keys;

		System.out.println("PreSynthesising Files");

		//Initialise
		nlp = new MetaFaceFestival(ttspath);
		dsp = new MetaFaceMbrola();
		dir = new File(output);
		if (!dir.exists()) {
			dir.mkdir();
		}

		//Get list of VHML files
		files = new File(input).listFiles();

		//For each file
		for (i = 0; i < files.length; i++) {
			System.out.println("file " + (i + 1) + " of " + files.length + " " + files[i].getName());

			//Process if an XML file
			if ((files[i].getName().indexOf(".xml") > 0) || (files[i].getName().indexOf(".XML") > 0)) {
				System.out.println("Creating VHML Output: " + output + "\\" + files[i].getName());

				//Parse file
				parser = new VHMLParser(files[i].getAbsolutePath(), 25, dsp, nlp, input);

				//Get faps, phonemes, text, and urls
				faps = parser.getFrames();
				phonemes = parser.getPhonemes();
				speechtext = parser.getText();
				browserupdates = parser.getBrowserUpdates();

				//Write to a VHMLOutput file
				try {
					fw = new FileWriter(output + "\\" + files[i].getName(), false);
					fw.write("<?xml version=\"1.0\"?>\n");
					fw.write("<!DOCTYPE eca_synthesis SYSTEM \"./vhml_output.dtd\">\n");
					fw.write("<eca_synthesis>\n");
					fw.write("<text>\n");
					fw.write(speechtext);
					fw.write("</text>\n");
					keys = browserupdates.keys();
					while (keys.hasMoreElements()) {
						target = (String) (keys.nextElement());
						url = (String) (browserupdates.get(target));
						fw.write("<url target=\"" + target + "\">" + url + "</url>\n");
					}
					fw.write("<phonemes>\n");
					for (j = 0; j < phonemes.size(); j++) {
						fw.write(((String) phonemes.elementAt(j)) + "\n");
					}
					fw.write("</phonemes>\n");
					fw.write("<faps>\n");
					for (j = 0; j < faps.size(); j++) {
						fw.write(((FAPData) faps.elementAt(j)).toString(j));
					}
					fw.write("</faps>\n");
					fw.write("</eca_synthesis>\n");
					fw.flush();
					fw.close();
				} catch (IOException e) {
					System.out.println("new fap file can't be written");
					e.printStackTrace();
				}
			}
		}
	}
}

