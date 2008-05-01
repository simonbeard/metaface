package metaface.server;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;
import java.io.*;
import metaface.mpeg4.*;
import metaface.dsp.*;
import metaface.nlp.*;

/**
 *  This class is used to parse a Virtual Human Markup Language (VHML)document.
 *  <p>
 *
 *  Follow this link to retrieve the Document Type Definition 
 *  <A HREF="../../resources/vhml.dtd">vhml.dtd</A> Follow this link to retrieve an
 *  example XML file <A HREF="../../resources/vhml_example.xml">
 *  vhml_example.xml</A>
 *
 *@author     Simon Beard
 *@version    1.0
 *@see        VHMLParser
 */
class VHMLReader extends DefaultHandler {
	/**
	 *  Index of VHML element
	 */
	private final static int AFRAID = 0;
	/**
	 *  Index of VHML element
	 */
	private final static int ANGRY = 1;
	/**
	 *  Index of VHML element
	 */
	private final static int CONFUSED = 2;
	/**
	 *  Index of VHML element
	 */
	private final static int DAZED = 3;
	/**
	 *  Index of VHML element
	 */
	private final static int DISGUSTED = 4;
	/**
	 *  Index of VHML element
	 */
	private final static int HAPPY = 5;
	/**
	 *  Index of VHML element
	 */
	private final static int NEUTRAL = 6;
	/**
	 *  Index of VHML element
	 */
	private final static int SURPRISED = 7;
	/**
	 *  Index of VHML element
	 */
	private final static int SAD = 8;
	/**
	 *  Index of VHML element
	 */
	private final static int DEFAULT = 9;
	/**
	 *  Index of VHML element
	 */
	private final static int AGREE = 0;
	/**
	 *  Index of VHML element
	 */
	private final static int DISAGREE = 1;
	/**
	 *  Index of VHML element
	 */
	private final static int CONCENTRATE = 2;
	/**
	 *  Index of VHML element
	 */
	private final static int EMPHASIS = 3;
	/**
	 *  Index of VHML element
	 */
	private final static int SIGH = 4;
	/**
	 *  Index of VHML element
	 */
	private final static int SMILE = 5;
	/**
	 *  Index of VHML element
	 */
	private final static int SHRUG = 6;
	/**
	 *  Index of VHML element
	 */
	private final static int EMPHASIZESYLLABLE = 0;
	/**
	 *  Index of VHML element
	 */
	private final static int EMPHASISESYLLABLE = 1;
	/**
	 *  Index of VHML element
	 */
	private final static int PHONEME = 2;
	/**
	 *  Index of VHML element
	 */
	private final static int PROSODY = 3;
	/**
	 *  Index of VHML element
	 */
	private final static int SAYAS = 4;
	/**
	 *  Index of VHML element
	 */
	private final static int VOICE = 5;
	/**
	 *  Index of VHML element
	 */
	private final static int EMPH = 0;
	/**
	 *  Index of VHML element
	 */
	private final static int PRON = 1;
	/**
	 *  Index of VHML element
	 */
	private final static int PITCH = 2;
	/**
	 *  Index of VHML element
	 */
	private final static int RANGE = 3;
	/**
	 *  Index of VHML element
	 */
	private final static int RATE = 4;
	/**
	 *  Index of VHML element
	 */
	private final static int PROS = 5;
	/**
	 *  Index of VHML element
	 */
	private final static int LOOKLEFT = 0;
	/**
	 *  Index of VHML element
	 */
	private final static int LOOKRIGHT = 1;
	/**
	 *  Index of VHML element
	 */
	private final static int LOOKUP = 2;
	/**
	 *  Index of VHML element
	 */
	private final static int LOOKDOWN = 3;
	/**
	 *  Index of VHML element
	 */
	private final static int EYESLEFT = 4;
	/**
	 *  Index of VHML element
	 */
	private final static int EYESRIGHT = 5;
	/**
	 *  Index of VHML element
	 */
	private final static int EYESUP = 6;
	/**
	 *  Index of VHML element
	 */
	private final static int EYESDOWN = 7;
	/**
	 *  Index of VHML element
	 */
	private final static int HEADLEFT = 8;
	/**
	 *  Index of VHML element
	 */
	private final static int HEADRIGHT = 9;
	/**
	 *  Index of VHML element
	 */
	private final static int HEADUP = 10;
	/**
	 *  Index of VHML element
	 */
	private final static int HEADDOWN = 11;
	/**
	 *  Index of VHML element
	 */
	private final static int HEADROLLLEFT = 12;
	/**
	 *  Index of VHML element
	 */
	private final static int HEADROLLRIGHT = 13;
	/**
	 *  Index of VHML element
	 */
	private final static int EYEBROWUP = 14;
	/**
	 *  Index of VHML element
	 */
	private final static int EYEBROWDOWN = 15;
	/**
	 *  Index of VHML element
	 */
	private final static int EYEBLINK = 16;
	/**
	 *  Index of VHML element
	 */
	private final static int WINK = 17;
	/**
	 *  Index of VHML element
	 */
	private final static int JAWOPEN = 18;
	/**
	 *  Index of VHML element
	 */
	private final static int JAWCLOSE = 19;
	/**
	 *  The phonemes from the VHML document
	 */
	private String allphonemes;
	/**
	 *  The MPEG-4 FAPs from the VHML document
	 */
	private Vector allfaps;
	/**
	 *  A list of FAPs that overrun the paragraph boundary
	 */
	private Vector excessfaps;
	/**
	 *  Current paragraph FAPs
	 */
	private Vector faps;
	/**
	 *  Current paragraph phonemes
	 */
	private Vector phonemes;
	/**
	 *  The speech text from the VHML document
	 */
	private String speechtext;
	/**
	 *  The URLs and targets from the VHML document
	 */
	private Hashtable browserupdates;
	/**
	 *  A list of VHML expression elements
	 */
	private Vector faceexpressionlist;
	/**
	 *  The current expression element
	 */
	private VHMLFAElement faceexpression;
	/**
	 *  A list of expression element names
	 */
	private String[] expressiontags;
	/**
	 *  A list mapping expressions to SML element names
	 */
	private String[] speechexpressionmap;
	/**
	 *  A list of speech elements
	 */
	private Vector speechlist;
	/**
	 *  A list of speech element names
	 */
	private String[] speechtags;
	/**
	 *  A list of SML element names
	 */
	private String[] smltags;
	/**
	 *  A list of VHML gesture elements
	 */
	private Vector gesturelist;
	/**
	 *  A list of gesture element names
	 */
	private String[] gesturetags;
	/**
	 *  A list of facial animation elements
	 */
	private Vector facialanimationlist;
	/**
	 *  A list of VHML facial animation names
	 */
	private String[] facialanimationtags;
	/**
	 *  Characters between tags
	 */
	private String tempchar = "";
	/**
	 *  The current web browser targer
	 */
	private String target = "";
	/**
	 *  Plain text from the VHML docuemnt
	 */
	private String plaintext = "";
	/**
	 *  The previous error when calculating FAPs
	 */
	private float preverror;
	/**
	 *  The current paragraph visemes
	 */
	private Vector visemes;
	/**
	 *  The current paragraph faps
	 */
	private Vector fapvec;
	/**
	 *  The frame rate
	 */
	private int fps;
	/**
	 *  The SML output file writer
	 */
	private FileWriter fw;
	/**
	 *  The DSP
	 */
	private DigitalSignalProcessor dsp;
	/**
	 *  The NLP
	 */
	private NaturalLanguageProcessor nlp;
	/**
	 *  Whether to render blinking in FAPs
	 */
	private boolean blinking = true;
	/**
	 *  The VHML interpreation semantics
	 */
	private VHMLSemantics semantics;
	/**
	 *  The current paragraph pause length
	 */
	private int natpause;
	/**
	 *  The current break element pause length
	 */
	private int breakpause;
	/**
	 *  The SML file header
	 */
	private static String VOICE_HEADER = "<?xml version=\"1.0\"?>\n<!DOCTYPE sml SYSTEM \"./sml-v01.dtd\">\n<sml>\n<p>\n";
	/**
	 *  The SML file footer
	 */
	private static String VOICE_FOOTER = "</p>\n</sml>";


	/**
	 *  Constructor for the VHMLReader object
	 *
	 *@param  framespersec              Description of the Parameter
	 *@param  digitalsignalprocessor    Description of the Parameter
	 *@param  naturallanguageprocessor  Description of the Parameter
	 *@param  smldtdpath                Description of the Parameter
	 */
	public VHMLReader(int framespersec, DigitalSignalProcessor digitalsignalprocessor, NaturalLanguageProcessor naturallanguageprocessor, String smldtdpath) {
		super();

		if (smldtdpath != null) {
			if (smldtdpath.length() > 0) {
				VOICE_HEADER = "<?xml version=\"1.0\"?>\n<!DOCTYPE sml SYSTEM \"" + smldtdpath + "/sml-v01.dtd\">\n<sml>\n<p>\n";
			}
		}
		phonemes = new Vector();
		faps = new Vector();
		browserupdates = new Hashtable();
		speechtext = "";

		dsp = digitalsignalprocessor;
		nlp = naturallanguageprocessor;
		fps = framespersec;

		allphonemes = new String("");
		allfaps = new Vector();
		excessfaps = new Vector();

		faceexpressionlist = new Vector();
		expressiontags = new String[10];
		expressiontags[0] = new String("afraid");
		expressiontags[1] = new String("angry");
		expressiontags[2] = new String("confused");
		expressiontags[3] = new String("dazed");
		expressiontags[4] = new String("disgusted");
		expressiontags[5] = new String("happy");
		expressiontags[6] = new String("neutral");
		expressiontags[7] = new String("surprised");
		expressiontags[8] = new String("sad");
		expressiontags[9] = new String("default-emotion");

		speechexpressionmap = new String[10];
		speechexpressionmap[0] = new String("sad");
		speechexpressionmap[1] = new String("angry");
		speechexpressionmap[2] = new String("neutral");
		speechexpressionmap[3] = new String("sad");
		speechexpressionmap[4] = new String("angry");
		speechexpressionmap[5] = new String("happy");
		speechexpressionmap[6] = new String("neutral");
		speechexpressionmap[7] = new String("happy");
		speechexpressionmap[8] = new String("sad");
		speechexpressionmap[9] = new String("happy");

		gesturelist = new Vector();

		gesturetags = new String[7];
		gesturetags[0] = new String("agree");
		gesturetags[1] = new String("disagree");
		gesturetags[2] = new String("concentrate");
		gesturetags[3] = new String("emphasis");
		gesturetags[4] = new String("sigh");
		gesturetags[5] = new String("smile");
		gesturetags[6] = new String("shrug");

		facialanimationlist = new Vector();

		facialanimationtags = new String[20];
		facialanimationtags[0] = new String("look-left");
		facialanimationtags[1] = new String("look-right");
		facialanimationtags[2] = new String("look-up");
		facialanimationtags[3] = new String("look-down");
		facialanimationtags[4] = new String("eyes-left");
		facialanimationtags[5] = new String("eyes-right");
		facialanimationtags[6] = new String("eyes-up");
		facialanimationtags[7] = new String("eyes-down");
		facialanimationtags[8] = new String("head-left");
		facialanimationtags[9] = new String("head-right");
		facialanimationtags[10] = new String("head-up");
		facialanimationtags[11] = new String("head-down");
		facialanimationtags[12] = new String("head-roll-left");
		facialanimationtags[13] = new String("head-roll-right");
		facialanimationtags[14] = new String("eyebrow-up");
		facialanimationtags[15] = new String("eyebrow-down");
		facialanimationtags[16] = new String("eye-blink");
		facialanimationtags[17] = new String("wink");
		facialanimationtags[18] = new String("jaw-open");
		facialanimationtags[19] = new String("jaw-close");

		speechlist = new Vector();

		speechtags = new String[6];
		speechtags[0] = new String("emphasize-syllable");
		speechtags[1] = new String("emphasise-syllable");
		speechtags[2] = new String("phoneme");
		speechtags[3] = new String("prosody");
		speechtags[4] = new String("say-as");
		speechtags[5] = new String("voice");

		smltags = new String[5];
		smltags[0] = new String("emph");
		smltags[1] = new String("pron");
		smltags[2] = new String("pitch");
		smltags[3] = new String("range");
		smltags[4] = new String("rate");

		natpause = 0;
		breakpause = 0;
		preverror = 0f;

		semantics = new VHMLSemantics(framespersec);
	}


	/**
	 *  Allows access to the MPEG-4 FAPs retrieved
	 *
	 *@return    A list of FAPData MPEG-4 FAP frames
	 */
	public Vector getFrames() {
		return (faps);
	}


	/**
	 *  Allows access to the phonemes retrieved
	 *
	 *@return    A list of phonemes
	 */
	public Vector getPhonemes() {
		return (phonemes);
	}


	/**
	 *  Allows access to the plain text retrieved
	 *
	 *@return    A String of characters
	 */
	public String getText() {
		return (speechtext);
	}


	/**
	 *  Allows access to the URLs and targets retrieved
	 *
	 *@return    A table of URLs referenced by their targets as keys
	 */
	public Hashtable getBrowserUpdates() {
		return (browserupdates);
	}


	/**
	 *  The parser calls this once at the beginning of a document
	 *
	 *@exception  SAXException  XML parsing Exception
	 */
	public void startDocument() throws SAXException {
		semantics.reset();
		faceexpressionlist = new Vector();
		natpause = 0;
		breakpause = 0;
		gesturelist = new Vector();
		facialanimationlist = new Vector();
		speechlist = new Vector();
		excessfaps = new Vector();
		plaintext = new String("");
	}


	/**
	 *  The parser calls this once after parsing a document
	 *
	 *@exception  SAXException  XML parsing Exception
	 */
	public void endDocument() throws SAXException {
	}


	/**
	 *  The parser calls this when encountering character data
	 *
	 *@param  ch                the characers encountered between tags
	 *@param  start             the starting point in the character array
	 *@param  length            the length of character data occupying the array
	 *@exception  SAXException  XML parsing Exception
	 */
	public void characters(char[] ch, int start, int length)
			 throws SAXException {
		int startfap;
		int i;
		int j;
		String temp;
		char[] test1;
		boolean test2;
		boolean test3;
		boolean sent = false;
		float temperr;
		int numframes;
		int leftover;
		VHMLSpeechElement se;
		int duration;
		String pi;
		Vector fapvec;
		String filename;
		String text;

		//Conduct test on character data
		test1 = new String(ch, start, length).toCharArray();
		test2 = false;
		test3 = false;
		for (j = 0; j < length; j++) {
			if ((test1[j] != ' ') && (test1[j] != '\n') && (test1[j] != '\t') && (test1[j] != '\0')) {
				test2 = true;
			}
			if ((((int) test1[j] >= (int) 'a') && ((int) test1[j] <= (int) 'z')) || (((int) test1[j] >= (int) 'A') && ((int) test1[j] <= (int) 'Z')) || (((int) test1[j] >= (int) '0') && ((int) test1[j] <= (int) '9'))) {
				test3 = true;
				j = length;
			}
		}

		//If whitesapce and no characters -- append data to buffer
		if ((test2) && (!test3)) {
			plaintext = plaintext.concat(new String(ch, start, length));
		}

		//If whitespace and character data -- synthesise audio
		if ((test2) && (test3)) {
			plaintext = plaintext.concat(new String(ch, start, length));

			//Construct SML file for SPEMO
			text = new String(VOICE_HEADER);
			text = text.concat("<");
			text = text.concat(speechexpressionmap[speechExpression(speechlist)]);
			text = text.concat(">");

			//Write SML tags according to VHML elements encountered
			for (i = 0; i < speechlist.size(); i++) {
				se = (VHMLSpeechElement) speechlist.elementAt(i);
				if ((!se.getIsExpression()) && (se.getDuration() != 0)) {
					text = text.concat("<" + smltags[se.getId()]);
					for (j = 0; j < se.getNumElements(); j++) {
						text = text.concat(" " + se.getAttribute(j) + "=\"" + se.getValue(j) + "\"");
					}
					text = text.concat(">");
				}
			}
			text = text.concat(new String(ch, start, length));
			for (i = speechlist.size() - 1; i >= 0; i--) {
				se = (VHMLSpeechElement) speechlist.elementAt(i);
				if ((!se.getIsExpression()) && (se.getDuration() != 0)) {
					text = text.concat("</" + smltags[se.getId()] + ">");
				}
			}

			//Write footer infor to SML file
			text = text.concat("</");
			text = text.concat(speechexpressionmap[speechExpression(speechlist)]);
			text = text.concat(">\n");
			text = text.concat(VOICE_FOOTER);

			//Output to temporary file
			filename = "";
			try {
				filename = File.createTempFile("MFFW_sml", ".xml").toString();
				fw = new FileWriter(filename);
				fw.write(text);
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			//Get phoneme information from NLP
			pi = nlp.getNextPhonemes(filename);
			while (pi != null) {

				//Skip NLP pauses
				pi = pi.substring(pi.indexOf("\n") + 1);
				pi = pi.substring(pi.indexOf("\n") + 1);

				//Add any pauses or breaks from VHMl document
				if (natpause > 0) {
					temp = new String(dsp.silencePhonemeString(natpause) + "\n");
					if (breakpause > 0) {
						temp = temp.concat(dsp.silencePhonemeString(breakpause) + "\n");
						breakpause = 0;
					}
					temp = temp.concat(pi);
					pi = temp;
					natpause = 0;
				} else if (breakpause > 0) {
					temp = new String(dsp.silencePhonemeString(breakpause) + "\n");
					temp = temp.concat(pi);
					pi = temp;
					breakpause = 0;
				}

				//Get visemes
				visemes = dsp.phonemesToVisemes(pi);

				//Calculate frames
				numframes = (int) (((float) FAPData.totalDuration(visemes) / 1000f) / (float) fps);

				//Take speech elements from list
				for (i = 0; i < speechlist.size(); i++) {
					se = (VHMLSpeechElement) speechlist.elementAt(i);
					speechlist.remove(i);
					duration = se.getDuration();
					if (duration > 0) {
						duration = duration - numframes;
						if (duration < 0) {
							duration = 0;
						}
						se.setDuration(duration);
					}
					speechlist.insertElementAt(se, i);
				}

				//Get FAPs
				fapvec = FAPData.visemesToFAPData(visemes, preverror, fps);

				//Calculate error with FAPs
				temperr = preverror;
				preverror = (float) fapvec.size();
				preverror = (float) preverror / (float) fps;
				preverror = ((float) FAPData.totalDuration(visemes) / 1000f) - preverror;
				preverror = preverror + temperr;

				// Update global faps and phonemes
				for (i = 0; i < fapvec.size(); i++) {
					allfaps.add(fapvec.elementAt(i));
				}
				allphonemes = allphonemes.concat(pi);

				//Get next phoneme information
				pi = nlp.getNextPhonemes(filename);
			}

			//Remove file
			new File(filename).delete();

		} else if ((natpause > 0) || (breakpause > 0)) {

			//If no character data -- but there are breaks or pauses then get phonemes
			pi = "";
			if (natpause > 0) {
				temp = new String(dsp.silencePhonemeString(natpause) + "\n");
				if (breakpause > 0) {
					temp = temp.concat(dsp.silencePhonemeString(breakpause) + "\n");
					breakpause = 0;
				}
				pi = temp;
				natpause = 0;
			} else if (breakpause > 0) {
				temp = new String(dsp.silencePhonemeString(breakpause) + "\n");
				pi = temp;
				breakpause = 0;
			}

			//Get visemes
			visemes = dsp.phonemesToVisemes(pi);

			//Calculate number of frames
			numframes = (int) (((float) FAPData.totalDuration(visemes) / 1000f) / (float) fps);

			//Take speech elements from list
			for (i = 0; i < speechlist.size(); i++) {
				se = (VHMLSpeechElement) speechlist.elementAt(i);
				speechlist.remove(i);
				duration = se.getDuration();
				if (duration > 0) {
					duration = duration - numframes;
					if (duration < 0) {
						duration = 0;
					}
					se.setDuration(duration);
				}
				if (duration != 0) {
					speechlist.insertElementAt(se, i);
				}
			}

			//Get FAPs
			fapvec = FAPData.visemesToFAPData(visemes, preverror, fps);

			//Calculate error with FAPs
			temperr = preverror;
			preverror = (float) fapvec.size();
			preverror = (float) preverror / (float) fps;
			preverror = ((float) FAPData.totalDuration(visemes) / 1000f) - preverror;
			preverror = preverror + temperr;

			// Update global faps and phonemes
			for (i = 0; i < fapvec.size(); i++) {
				allfaps.add(fapvec.elementAt(i));
			}
			allphonemes = allphonemes.concat(pi);
		}
	}


	/**
	 *  The parser calls this for each element in a document
	 *
	 *@param  namespaceURI      used to resolve the namespace of the element
	 *@param  localName         the local name of the element
	 *@param  rawName           the resolved name of the element
	 *@param  atts              the attributes of the element
	 *@exception  SAXException  XML parsing Exception
	 */
	public void startElement(String namespaceURI, String localName,
			String rawName, Attributes atts)
			 throws SAXException {
		String key = rawName;
		String temp;
		String temp2;
		int tempnum;
		int wait;
		int duration;
		int intensity;
		int toframe;
		int fromframe;
		int repeat;
		float temperr;
		VHMLSpeechElement se;
		int pitch;
		int range;
		int rate;
		int volume;
		String val;
		String which;
		VHMLFAElement gesture;
		VHMLFAElement facialanimation;
		String src;
		int chars;
		int i;

		//Work out which element has been encountered
		if (key.compareTo("vhml") == 0) {

			//Add to pause in audio and FAPs
			natpause = natpause + 450;
		} else if ((key.compareTo("paragraph") == 0) || (key.compareTo("p") == 0)) {

			//Reset
			allphonemes = new String("");
			allfaps = new Vector();
			natpause = natpause + 100;
			target = atts.getValue("target");
			blinking = true;
		} else if (key.compareTo("embed") == 0) {

			//Get embedded HTML
			src = atts.getValue("src");
			if (target != null) {
				browserupdates.put(target, src);
			}
		} else if (key.compareTo("say-as") == 0) {

			//Update speech element list
			temp = atts.getValue("sub");
			if (temp != null) {
				se = new VHMLSpeechElement(PRON, -1, false, 1);
				se.setAttribute(0, "sub");
				se.setValue(0, temp);
				speechlist.add(se);
			} else {
				speechlist.add(new VHMLSpeechElement(PRON, 0, false, 0));
			}
		} else if ((key.compareTo("emphasise-syllable") == 0) || (key.compareTo("emphasize-syllable") == 0)) {

			//Update speech element list
			temp = atts.getValue("duration");
			duration = (int) getFrames(temp);
			temp = atts.getValue("level");
			if (temp == null) {
				temp = new String("moderate");
			}
			if (!(temp.compareTo("none") == 0) && !(temp.compareTo("reduced") == 0)) {
				temp2 = atts.getValue("target");
				if (temp2 == null) {
					se = new VHMLSpeechElement(EMPH, duration, false, 1);
				} else {
					se = new VHMLSpeechElement(EMPH, duration, false, 2);
					se.setAttribute(1, "target");
					se.setValue(1, temp2);
				}
				se.setAttribute(0, "level");
				se.setValue(0, temp);
				speechlist.add(se);
			} else {
				speechlist.add(new VHMLSpeechElement(EMPH, 0, false, 0));
			}
		} else if ((key.compareTo("afraid") == 0) || (key.compareTo("angry") == 0) || (key.compareTo("confused") == 0)
				 || (key.compareTo("dazed") == 0) || (key.compareTo("disgusted") == 0) || (key.compareTo("happy") == 0)
				 || (key.compareTo("neutral") == 0) || (key.compareTo("surprised") == 0) || (key.compareTo("sad") == 0)
				 || (key.compareTo("default") == 0)) {
			tempnum = 10;

			//Work out which expression
			for (i = 0; i < 10; i++) {
				if (key.compareTo(expressiontags[i]) == 0) {
					tempnum = i;
					i = 10;
				}
			}
			if (tempnum < 10) {

				//Get attributes
				temp = atts.getValue("wait");
				wait = (int) getFrames(temp);
				if (wait < 0) {
					wait = 0;
				}
				temp = atts.getValue("duration");
				duration = (int) getFrames(temp);
				temp = atts.getValue("intensity");
				if (temp == null) {
					intensity = 32;
				} else if (temp.compareTo("high") == 0) {
					intensity = 63;
				} else if (temp.compareTo("medium") == 0) {
					intensity = 32;
				} else if (temp.compareTo("low") == 0) {
					intensity = 10;
				} else {
					try {
						intensity = Integer.parseInt(temp);
					} catch (Exception e) {
						intensity = -1;
					}
					if (intensity == -1) {
						intensity = 32;
					} else {
						intensity = (int) ((float) intensity * 0.63f);
					}
				}
				if (duration >= 0) {
					toframe = wait + duration + (allfaps.size()) - 1;
				} else {
					toframe = (allfaps.size()) + wait - 1;
				}

				//Add to expression list
				faceexpression = new VHMLFAElement(tempnum, wait, duration, intensity, allfaps.size() - 1, toframe);
				faceexpressionlist.add(faceexpression);

				//Update speech element list
				speechlist = removeExpressions(speechlist);
				speechlist.add(new VHMLSpeechElement(tempnum, duration, true, 0));

				//Add silence for wait
				if (wait > 0) {
					allphonemes = allphonemes.concat("_ " + (int) (((float) wait / (float) fps) * 1000f) + "\n");
					visemes = dsp.phonemesToVisemes("_ " + (int) (((float) wait / (float) fps) * 1000f) + "\n");
					fapvec = FAPData.visemesToFAPData(visemes, preverror, fps);
					temperr = preverror;
					preverror = (float) fapvec.size();
					preverror = (float) preverror / (float) fps;
					preverror = ((float) FAPData.totalDuration(visemes) / 1000f) - preverror;
					preverror = preverror + temperr;

					// Update global faps and phonemes
					for (i = 0; i < fapvec.size(); i++) {
						allfaps.add(fapvec.elementAt(i));
					}
				}
			}
		} else if (key.compareTo("break") == 0) {
			temp = atts.getValue("size");

			//Add silence for break
			if (temp != null) {
				if (temp.compareTo("none") == 0) {
					breakpause = 0;
				} else if (temp.compareTo("small") == 0) {
					breakpause = 100;
				} else if (temp.compareTo("medium") == 0) {
					breakpause = 500;
				} else if (temp.compareTo("large") == 0) {
					breakpause = 1000;
				}
			} else {
				temp = atts.getValue("time");
				if (temp != null) {
					breakpause = (int) getFrames(temp);
				} else {
					breakpause = 500;
				}
			}
		} else if (key.compareTo("prosody") == 0) {
			se = new VHMLSpeechElement(PROS, 0, false, 0);
			speechlist.add(se);

			//Update speech element list
			temp = atts.getValue("duration");
			duration = (int) getFrames(temp);
			temp = atts.getValue("pitch");
			if ((temp != null) && (temp.compareTo("default") != 0)) {
				if (temp.compareTo("high") == 0) {
					val = new String("+99%");
				} else if (temp.compareTo("medium") == 0) {
					val = new String("+0%");
				} else if (temp.compareTo("low") == 0) {
					val = new String("-99%");
				} else {
					val = new String(temp);
				}
				se = new VHMLSpeechElement(PITCH, duration, false, 1);
				se.setAttribute(0, "middle");
				se.setValue(0, val);
				speechlist.add(se);
			}
			temp = atts.getValue("range");
			if ((temp != null) && (temp.compareTo("default") != 0)) {
				if (temp.compareTo("high") == 0) {
					val = new String("+99%");
				} else if (temp.compareTo("medium") == 0) {
					val = new String("+0%");
				} else if (temp.compareTo("low") == 0) {
					val = new String("-99%");
				} else {
					val = new String(temp);
				}
				se = new VHMLSpeechElement(PITCH, duration, false, 1);
				se.setAttribute(0, "range");
				se.setValue(0, val);
				speechlist.add(se);
			}
			temp = atts.getValue("rate");
			if ((temp != null) && (temp.compareTo("default") != 0)) {
				if (temp.compareTo("fast") == 0) {
					val = new String("+40%");
				} else if (temp.compareTo("medium") == 0) {
					val = new String("+0%");
				} else if (temp.compareTo("slow") == 0) {
					val = new String("-40%");
				} else {
					val = new String(temp);
				}
				se = new VHMLSpeechElement(RATE, duration, false, 1);
				se.setAttribute(0, "speed");
				se.setValue(0, val);
				speechlist.add(se);
			}
			se = new VHMLSpeechElement(PROS, 0, false, 0);
			speechlist.add(se);
		} else if ((key.compareTo("agree") == 0) || (key.compareTo("disagree") == 0) || (key.compareTo("concentrate") == 0) ||
				(key.compareTo("emphasis") == 0) || (key.compareTo("sigh") == 0) || (key.compareTo("smile") == 0) ||
				(key.compareTo("shrug") == 0)) {
			tempnum = 7;

			//Work out which facial animation element
			for (i = 0; i < 7; i++) {
				if (key.compareTo(gesturetags[i]) == 0) {
					tempnum = i;
					i = 10;
				}
			}
			if (tempnum < 7) {

				//Get attributes
				temp = atts.getValue("wait");
				wait = (int) getFrames(temp);
				if (wait < 0) {
					wait = 0;
				}
				temp = atts.getValue("duration");
				duration = (int) getFrames(temp);
				temp = atts.getValue("intensity");
				if (temp == null) {
					intensity = 50;
				} else if (temp.compareTo("high") == 0) {
					intensity = 100;
				} else if (temp.compareTo("medium") == 0) {
					intensity = 50;
				} else if (temp.compareTo("low") == 0) {
					intensity = 10;
				} else {
					try {
						intensity = Integer.parseInt(temp);
					} catch (Exception e) {
						intensity = 50;
					}
				}
				if (duration >= 0) {
					toframe = wait + duration + (allfaps.size()) - 1;
				} else {
					toframe = (allfaps.size()) + wait - 1;
				}
				temp = atts.getValue("repeat");
				if (temp == null) {
					repeat = 1;
				} else {
					try {
						repeat = Integer.parseInt(temp);
					} catch (Exception e) {
						repeat = 1;
					}
				}

				//Update gesture list
				gesture = new VHMLFAElement(tempnum, wait, duration, intensity, allfaps.size() - 1, toframe, repeat);
				gesturelist.add(gesture);

				//Add silence for wait
				if (wait > 0) {
					allphonemes = allphonemes.concat("_ " + (int) (((float) wait / (float) fps) * 1000f) + "\n");
					visemes = dsp.phonemesToVisemes("_ " + (int) (((float) wait / (float) fps) * 1000f) + "\n");
					fapvec = FAPData.visemesToFAPData(visemes, preverror, fps);
					temperr = preverror;
					preverror = (float) fapvec.size();
					preverror = (float) preverror / (float) fps;
					preverror = ((float) FAPData.totalDuration(visemes) / 1000f) - preverror;
					preverror = preverror + temperr;

					// Update global faps and phonemes
					for (i = 0; i < fapvec.size(); i++) {
						allfaps.add(fapvec.elementAt(i));
					}
				}

				if (key.compareTo("emphasis") == 0) {
					temp = atts.getValue("level");
					if (temp == null) {
						temp = new String("moderate");
					}

					//Update speech element list
					if (!(temp.compareTo("none") == 0) && !(temp.compareTo("reduced") == 0)) {
						se = new VHMLSpeechElement(EMPH, duration, false, 1);
						se.setAttribute(0, "level");
						se.setValue(0, temp);
						speechlist.add(se);
					} else {
						speechlist.add(new VHMLSpeechElement(EMPH, 0, false, 0));
					}
				}
			}
		} else if ((key.compareTo("look-left") == 0) || (key.compareTo("look-right") == 0) || (key.compareTo("look-up") == 0) ||
				(key.compareTo("look-down") == 0) || (key.compareTo("eyes-left") == 0) || (key.compareTo("eyes-right") == 0) ||
				(key.compareTo("eyes-up") == 0) || (key.compareTo("eyes-down") == 0) || (key.compareTo("head-left") == 0) ||
				(key.compareTo("head-right") == 0) || (key.compareTo("head-up") == 0) || (key.compareTo("head-down") == 0) ||
				(key.compareTo("head-roll-left") == 0) || (key.compareTo("head-roll-right") == 0) || (key.compareTo("eyebrow-up") == 0) ||
				(key.compareTo("eyebrow-down") == 0) || (key.compareTo("eye-blink") == 0) || (key.compareTo("wink") == 0) ||
				(key.compareTo("jaw-open") == 0) || (key.compareTo("jaw-close") == 0)) {
			if ((key.compareTo("eye-blink") == 0) || (key.compareTo("wink") == 0)) {
				blinking = false;
			}
			tempnum = 20;

			//Work out which element
			for (i = 0; i < 20; i++) {
				if (key.compareTo(facialanimationtags[i]) == 0) {
					tempnum = i;
					i = 20;
				}
			}
			if (tempnum < 20) {

				//Get attributes
				temp = atts.getValue("wait");
				wait = (int) getFrames(temp);
				if (wait < 0) {
					wait = 0;
				}
				temp = atts.getValue("duration");
				duration = (int) getFrames(temp);
				temp = atts.getValue("intensity");
				if (temp == null) {
					intensity = 50;
				} else if (temp.compareTo("high") == 0) {
					intensity = 100;
				} else if (temp.compareTo("medium") == 0) {
					intensity = 50;
				} else if (temp.compareTo("low") == 0) {
					intensity = 10;
				} else {
					try {
						intensity = Integer.parseInt(temp);
					} catch (Exception e) {
						intensity = 50;
					}
				}
				if (duration >= 0) {
					toframe = wait + duration + (allfaps.size()) - 1;
				} else {
					toframe = (allfaps.size()) + wait - 1;
				}
				temp = atts.getValue("repeat");
				if (temp == null) {
					repeat = 1;
				} else {
					try {
						repeat = Integer.parseInt(temp);
					} catch (Exception e) {
						repeat = 1;
					}
				}
				temp = atts.getValue("which");
				which = temp;

				//Update facial animation list
				facialanimation = new VHMLFAElement(tempnum, wait, duration, intensity, allfaps.size() - 1, toframe, repeat, which);
				facialanimationlist.add(facialanimation);

				//Add silence for wait
				if (wait > 0) {
					allphonemes = allphonemes.concat("_ " + (int) (((float) wait / (float) fps) * 1000f) + "\n");
					visemes = dsp.phonemesToVisemes("_ " + (int) (((float) wait / (float) fps) * 1000f) + "\n");
					fapvec = FAPData.visemesToFAPData(visemes, preverror, fps);
					temperr = preverror;
					preverror = (float) fapvec.size();
					preverror = (float) preverror / (float) fps;
					preverror = ((float) FAPData.totalDuration(visemes) / 1000f) - preverror;
					preverror = preverror + temperr;

					// Update global faps and phonemes
					for (i = 0; i < fapvec.size(); i++) {
						allfaps.add(fapvec.elementAt(i));
					}
				}
			}
		}
	}


	/**
	 *  Applies the expression list to the current paragraph FAPs
	 *
	 *@param  faps  The current paragraph MPEG-4 FAPs
	 *@return       The modified FAPs
	 */
	private Vector applyFacialExpressions(Vector faps) {
		int fromframe;
		int toframe;
		int id;
		int intensity;
		int i;

		for (i = 0; i < faceexpressionlist.size(); i++) {
			id = ((VHMLFAElement) faceexpressionlist.elementAt(i)).getId();
			fromframe = ((VHMLFAElement) faceexpressionlist.elementAt(i)).getFromFrame();
			toframe = ((VHMLFAElement) faceexpressionlist.elementAt(i)).getToFrame();
			if (toframe >= faps.size() - 3) {
				toframe = faps.size() - 1;
			}
			intensity = ((VHMLFAElement) faceexpressionlist.elementAt(i)).getIntensity();
			if ((toframe > fromframe) && (intensity > 0)) {
				faps = semantics.applyFacialExpression(faps, fromframe, toframe, id, intensity);
			}
		}

		return (faps);
	}


	/**
	 *  Overlays the left over FAPs from the previous paragraph with the FAPs from
	 *  this paragraph
	 *
	 *@param  faps     The current paragraph MPEG-4 FAPs
	 *@param  overlay  The FAPs left over from the previous paragraph
	 *@return          The modified FAPs
	 */
	private Vector overlayFAPExcess(Vector faps, Vector overlay) {
		int i;
		FAPData tempfap;
		FAPData tempfap2;
		int viseme1;
		int viseme2;
		int blend;

		for (i = 0; i < overlay.size(); i++) {
			if (i >= faps.size()) {
				i = overlay.size();
			} else {
				tempfap = (FAPData) faps.elementAt(i);
				tempfap2 = (FAPData) overlay.elementAt(i);
				overlay.remove(i);
				viseme1 = tempfap.getViseme1();
				viseme2 = tempfap.getViseme2();
				blend = tempfap.getVisemeBlend();
				tempfap2.setVisemeVariables(1);
				tempfap2.setVisemeValues(viseme1, viseme2, blend);
				overlay.insertElementAt(tempfap2, i);
			}
		}
		for (i = overlay.size(); i < faps.size(); i++) {
			tempfap = (FAPData) faps.elementAt(i);
			overlay.addElement(tempfap);
		}
		return (overlay);
	}


	/**
	 *  Gets the excess MPEG-4 FAPs from the FAPs of teh current paragraph
	 *
	 *@param  faps  The current paragraph MPEG-4 FAPs
	 *@param  from  The index at which the paragraph frames should stop
	 *@return       The excess FAPs
	 */
	private Vector getFAPExcess(Vector faps, int from) {
		Vector temp;
		int i;

		temp = new Vector();
		for (i = from; i < faps.size(); i++) {
			temp.addElement(faps.elementAt(i));
		}
		return temp;
	}


	/**
	 *  Applies the facial gestures list to the current paragraph FAPs
	 *
	 *@param  faps  The current paragraph MPEG-4 FAPs
	 *@return       The modified FAPs
	 */
	private Vector applyFacialGestures(Vector faps) {
		int fromframe;
		int toframe;
		int id;
		int intensity;
		int i;
		int repeat;
		int fr2;
		int to2;
		int int2;
		int j;

		for (i = 0; i < gesturelist.size(); i++) {
			id = ((VHMLFAElement) gesturelist.elementAt(i)).getId();
			fromframe = ((VHMLFAElement) gesturelist.elementAt(i)).getFromFrame();
			toframe = ((VHMLFAElement) gesturelist.elementAt(i)).getToFrame();
			if (toframe >= faps.size() - 3) {
				toframe = faps.size() - 1;
			}
			intensity = ((VHMLFAElement) gesturelist.elementAt(i)).getIntensity();
			repeat = ((VHMLFAElement) gesturelist.elementAt(i)).getRepeat();
			if ((toframe > fromframe) && (intensity > 0)) {
				faps = semantics.applyFacialGesture(faps, fromframe, toframe, id, intensity, repeat);
			}
		}

		return (faps);
	}


	/**
	 *  Applies the blinking list to the current paragraph FAPs
	 *
	 *@param  faps  The current paragraph MPEG-4 FAPs
	 *@return       The modified FAPs
	 */
	private Vector applyBlinking(Vector faps) {
		int fromframe;
		int toframe;
		toframe = faps.size() - 1;
		fromframe = 0;
		if (toframe > fromframe) {
			faps = semantics.applyBlinking(faps, fromframe, toframe);
		}
		return (faps);
	}


	/**
	 *  Applies the facial animation list to the current paragraph FAPs
	 *
	 *@param  faps  The current paragraph MPEG-4 FAPs
	 *@return       The modified FAPs
	 */
	private Vector applyFacialAnimation(Vector faps) {
		int fromframe;
		int toframe;
		int id;
		int intensity;
		int i;
		int repeat;
		String which;

		for (i = 0; i < facialanimationlist.size(); i++) {
			id = ((VHMLFAElement) facialanimationlist.elementAt(i)).getId();
			fromframe = ((VHMLFAElement) facialanimationlist.elementAt(i)).getFromFrame();
			toframe = ((VHMLFAElement) facialanimationlist.elementAt(i)).getToFrame();
			if (toframe >= faps.size() - 3) {
				toframe = faps.size() - 1;
			}
			intensity = ((VHMLFAElement) facialanimationlist.elementAt(i)).getIntensity();
			repeat = ((VHMLFAElement) facialanimationlist.elementAt(i)).getRepeat();
			which = ((VHMLFAElement) facialanimationlist.elementAt(i)).getWhich();
			if ((toframe > fromframe) && (intensity > 0)) {
				faps = semantics.applyFacialAnimation(faps, fromframe, toframe, id, intensity, repeat, which);
			}
		}

		return (faps);
	}


	/**
	 *  Get the next speech expression element to render
	 *
	 *@param  sl  A list of the speech elements
	 *@return     The VHML - SML mapping index for rendering
	 */
	private int speechExpression(Vector sl) {
		int i;
		VHMLSpeechElement se;

		for (i = sl.size() - 1; i >= 0; i--) {
			se = (VHMLSpeechElement) sl.elementAt(i);
			if ((se.getIsExpression()) && (se.getDuration() != 0)) {
				return (se.getId());
			}
		}
		return (DEFAULT);
	}


	/**
	 *  Remove speech expression elemnts from the current paragraph list
	 *
	 *@param  sl  A list of speech elements
	 *@return     The modified speech elements list
	 */
	private Vector removeExpressions(Vector sl) {
		int i;
		VHMLSpeechElement se;

		for (i = sl.size() - 1; i >= 0; i--) {
			se = (VHMLSpeechElement) sl.elementAt(i);
			if (se.getIsExpression()) {
				sl.remove(i);
			}
		}
		return (sl);
	}


	/**
	 *  Gets the number of frames corresponding to a VHML document specified time
	 *
	 *@param  time  The VHML time string
	 *@return       The number of corresponding frames
	 */
	private float getFrames(String time) {
		int i;
		int convert;
		int convert2;
		int units;
		float temp;

		//Interpret Xms, Xs, etc. according to framerate
		units = 1;
		if (time != null) {
			convert = time.indexOf('m');
			if (convert < 0) {
				convert = time.indexOf('s');
				if (convert > 0) {
					units = 0;
				} else {
					convert = time.length();
				}
			}

			if (convert > 0) {
				time = time.substring(0, convert);
			} else {
				return (-1);
			}

			temp = Float.parseFloat(time);
			if (units == 1) {
				temp = (temp / 1000f) * (float) fps;
			} else {
				temp = temp * (float) fps;
			}

			return (temp);
		} else {
			return (-1);
		}
	}


	/**
	 *  The parser calls this when encountering a closing tag.
	 *
	 *@param  namespaceURI      used to resolve the namespace of the element
	 *@param  localName         the local name of the element
	 *@param  rawName           the resolved name of the element
	 *@exception  SAXException  Description of the Exception
	 */
	public void endElement(String namespaceURI, String localName,
			String rawName)
			 throws SAXException {
		String key = rawName;
		int tempnum;
		int i;
		int j;
		String temp;
		VHMLFAElement gesture;
		VHMLFAElement facialanimation;
		VHMLSpeechElement se;
		int size;

		//Work out which element has been encountered
		if (key.compareTo("vhml") == 0) {
		} else if (key.compareTo("person") == 0) {
		} else if ((key.compareTo("paragraph") == 0) || (key.compareTo("p") == 0)) {
			String line;

			size = allfaps.size();

			//Initialise MPEG-4 FAPs
			allfaps = semantics.initFAPs(allfaps);

			//Overlay previous paragraph's excess
			allfaps = overlayFAPExcess(allfaps, excessfaps);

			//Overlay expression, gesture, and facial animation
			allfaps = applyFacialExpressions(allfaps);
			allfaps = applyFacialGestures(allfaps);
			allfaps = applyFacialAnimation(allfaps);

			//Overlay blinking
			if (blinking) {
				allfaps = applyBlinking(allfaps);
			}

			//Get excess FAPs from paragraph
			excessfaps = getFAPExcess(allfaps, size);

			//Reset element lists
			gesturelist = new Vector();
			facialanimationlist = new Vector();
			speechlist = new Vector();
			faceexpressionlist = new Vector();

			//Store all FAPs
			for (i = 0; i < size; i++) {
				faps.add(allfaps.elementAt(i));
			}

			//Store phonemes individually in a list
			allphonemes = allphonemes.trim();
			i = allphonemes.indexOf('\n');
			j = 0;
			while (i > 0) {
				line = allphonemes.substring(j, i);
				phonemes.add(line);
				i++;
				j = i;
				i = allphonemes.indexOf(j, '\n');
			}
			line = allphonemes.substring(j, allphonemes.length());
			phonemes.add(line);
			speechtext = new String(plaintext);
			target = null;
		} else if (key.compareTo("say-as") == 0) {

			//Remove from list or update duration
			for (i = speechlist.size() - 1; i >= 0; i--) {
				se = (VHMLSpeechElement) speechlist.elementAt(i);
				if (se.getId() == PRON) {
					if (!se.getIsClosedTag()) {
						speechlist.remove(i);
						se.setClosedTag(true);
						if (se.getDuration() < 0) {
							se.setDuration(0);
						}
						speechlist.insertElementAt(se, i);
						i = -1;
					}
				}
			}
		} else if ((key.compareTo("emphasise-syllable") == 0) || (key.compareTo("emphasize-syllable") == 0)) {

			//Remove from list or update duration
			for (i = speechlist.size() - 1; i >= 0; i--) {
				se = (VHMLSpeechElement) speechlist.elementAt(i);
				if (se.getId() == EMPH) {
					if (!se.getIsClosedTag()) {
						speechlist.remove(i);
						se.setClosedTag(true);
						if (se.getDuration() < 0) {
							se.setDuration(0);
						}
						speechlist.insertElementAt(se, i);
						i = -1;
					}
				}
			}
		} else if (key.compareTo("mark") == 0) {
		} else if (key.compareTo("embed") == 0) {
		} else if (key.compareTo("prosody") == 0) {

			//Remove from list or update duration
			for (i = speechlist.size() - 1; i >= 0; i--) {
				se = (VHMLSpeechElement) speechlist.elementAt(i);
				if (se.getId() == PROS) {
					speechlist.remove(i);
					se.setClosedTag(true);
					speechlist.insertElementAt(se, i);
					for (j = i - 1; j >= 0; j--) {
						se = (VHMLSpeechElement) speechlist.elementAt(j);
						speechlist.remove(j);
						se.setClosedTag(true);
						if (se.getDuration() < 0) {
							se.setDuration(0);
						}
						speechlist.insertElementAt(se, j);
						if (se.getId() == PROS) {
							j = -1;
						}
					}
					i = -1;
				}
			}
		} else if ((key.compareTo("afraid") == 0) || (key.compareTo("angry") == 0) || (key.compareTo("confused") == 0)
				 || (key.compareTo("dazed") == 0) || (key.compareTo("disgusted") == 0) || (key.compareTo("happy") == 0)
				 || (key.compareTo("neutral") == 0) || (key.compareTo("surprised") == 0) || (key.compareTo("sad") == 0)
				 || (key.compareTo("default") == 0)) {

			//Remove from list or update duration
			for (i = speechlist.size() - 1; i >= 0; i--) {
				se = (VHMLSpeechElement) speechlist.elementAt(i);
				if (expressiontags[se.getId()].compareTo(key) == 0) {
					if (!se.getIsClosedTag()) {
						speechlist.remove(i);
						se.setClosedTag(true);
						if (se.getDuration() < 0) {
							se.setDuration(0);
						}
						speechlist.insertElementAt(se, i);
						i = -1;
					}
				}
			}

			//Determine which expression
			tempnum = 10;
			for (i = 0; i < 10; i++) {
				if (key.compareTo(expressiontags[i]) == 0) {
					tempnum = i;
					i = 10;
				}
			}

			//Remove from list or update duration
			if (tempnum < 10) {
				for (i = faceexpressionlist.size() - 1; i >= 0; i--) {
					faceexpression = (VHMLFAElement) faceexpressionlist.elementAt(i);
					if ((tempnum == faceexpression.getId()) && (!faceexpression.getIsClosedTag())) {
						faceexpressionlist.removeElementAt(i);
						faceexpression.setClosedTag(true);
						if (!faceexpression.isDurationSet()) {
							faceexpression.setToFrame(allfaps.size());
						}
						faceexpressionlist.insertElementAt(faceexpression, i);
						i = -1;
					}
				}
			}
		} else if ((key.compareTo("look-left") == 0) || (key.compareTo("look-right") == 0) || (key.compareTo("look-up") == 0) ||
				(key.compareTo("look-down") == 0) || (key.compareTo("eyes-left") == 0) || (key.compareTo("eyes-right") == 0) ||
				(key.compareTo("eyes-up") == 0) || (key.compareTo("eyes-down") == 0) || (key.compareTo("head-left") == 0) ||
				(key.compareTo("head-right") == 0) || (key.compareTo("head-up") == 0) || (key.compareTo("head-down") == 0) ||
				(key.compareTo("head-roll-left") == 0) || (key.compareTo("head-roll-right") == 0) || (key.compareTo("eyebrow-up") == 0) ||
				(key.compareTo("eyebrow-down") == 0) || (key.compareTo("eye-blink") == 0) || (key.compareTo("wink") == 0) ||
				(key.compareTo("jaw-open") == 0) || (key.compareTo("jaw-close") == 0)) {
			tempnum = 20;

			//Determine facial animation element
			for (i = 0; i < 20; i++) {
				if (key.compareTo(facialanimationtags[i]) == 0) {
					tempnum = i;
					i = 20;
				}
			}
			if (tempnum < 20) {

				//Remove from list or update duration
				for (i = facialanimationlist.size() - 1; i >= 0; i--) {
					facialanimation = (VHMLFAElement) facialanimationlist.elementAt(i);
					if ((tempnum == facialanimation.getId()) && (!facialanimation.getIsClosedTag())) {
						facialanimationlist.removeElementAt(i);
						facialanimation.setClosedTag(true);
						if (!facialanimation.isDurationSet()) {
							facialanimation.setToFrame(allfaps.size());
						}
						facialanimationlist.insertElementAt(facialanimation, i);
						i = -1;
					}
				}
			}
		} else if ((key.compareTo("agree") == 0) || (key.compareTo("disagree") == 0) || (key.compareTo("concentrate") == 0) ||
				(key.compareTo("emphasis") == 0) || (key.compareTo("sigh") == 0) || (key.compareTo("smile") == 0) ||
				(key.compareTo("shrug") == 0)) {
			tempnum = 7;

			//Determine gesture
			for (i = 0; i < 7; i++) {
				if (key.compareTo(gesturetags[i]) == 0) {
					tempnum = i;
					i = 10;
				}
			}
			if (tempnum < 7) {

				//Remove from list or update duration
				for (i = gesturelist.size() - 1; i >= 0; i--) {
					gesture = (VHMLFAElement) gesturelist.elementAt(i);
					if ((tempnum == gesture.getId()) && (!gesture.getIsClosedTag())) {
						gesturelist.removeElementAt(i);
						gesture.setClosedTag(true);
						if (!gesture.isDurationSet()) {
							gesture.setToFrame(allfaps.size());
						}
						gesturelist.insertElementAt(gesture, i);
						i = -1;
					}
				}

				//Remove from list or update duration
				if (key.compareTo("emphasis") == 0) {
					for (i = speechlist.size() - 1; i >= 0; i--) {
						se = (VHMLSpeechElement) speechlist.elementAt(i);
						if (se.getId() == EMPH) {
							if (!se.getIsClosedTag()) {
								speechlist.remove(i);
								se.setClosedTag(true);
								if (se.getDuration() < 0) {
									se.setDuration(0);
								}
								speechlist.insertElementAt(se, i);
								i = -1;
							}
						}
					}
				}
			}
		}
	}
}

