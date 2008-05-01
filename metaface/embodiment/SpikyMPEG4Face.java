package metaface.embodiment;
import java.awt.geom.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.awt.font.*;
import metaface.mpeg4.*;

/**
 *  This class constructs a male MPEG-4 two-dimensional face for a MetaFace
 *  application.<p>
 *
 *  <img src="../../resources/male.jpg"/>
 *
 *@author     Simon Beard 
 *@version    1.0
 */
public class SpikyMPEG4Face extends MPEG4Face {

	private Rectangle2D.Float black_mouth;
	private Rectangle2D.Float gums;
	private Rectangle2D.Float teeth;
	private Line2D.Float[] teeth_lines;
	private float[] tounge_poke;
	private QuadCurve2D.Float tounge_semi;
	private Ellipse2D.Float head;
	private GeneralPath brow;
	private GeneralPath left_brow;
	private GeneralPath right_brow;
	private GeneralPath[] hair;
	private GeneralPath[] hair_left;
	private GeneralPath[] hair_right;
	private GeneralPath[] hair_back;
	private GeneralPath left_eyelid;
	private GeneralPath left_bottom_eyelid;
	private GeneralPath right_eyelid;
	private GeneralPath right_bottom_eyelid;
	private GeneralPath face_left_eyelid;
	private GeneralPath face_left_bottom_eyelid;
	private GeneralPath face_right_eyelid;
	private GeneralPath face_right_bottom_eyelid;
	private GeneralPath left_eye;
	private GeneralPath right_eye;
	private GeneralPath face_left_eye;
	private GeneralPath face_right_eye;
	private Ellipse2D.Float left_pupil;
	private Ellipse2D.Float right_pupil;
	private Ellipse2D.Float face_left_pupil;
	private Ellipse2D.Float face_right_pupil;
	private QuadCurve2D.Float left_eyebrow;
	private QuadCurve2D.Float right_eyebrow;
	private QuadCurve2D.Float face_left_eyebrow;
	private QuadCurve2D.Float face_right_eyebrow;
	private GeneralPath mouth;
	private GeneralPath left_mouth;
	private GeneralPath right_mouth;
	private float mouth_width;
	private float scale_mouth_x;
	private float scale_mouth_y;
	private float eyebrow_width;
	private double theta;
	private double theta2;
	private float pupil_dilation[];
	private float eye_yaw[];
	private float eye_pitch[];
	private Float PI;
	private float left_pupil_radius;
	private float left_pupil_center[];
	private float right_pupil_radius;
	private float right_pupil_center[];
	private boolean visemeDraw;
	private int viseme1;
	private int viseme2;
	private int visemeBlend;
	private MPEG4ExpressionCoordinates expressionCoords;
	private int[] headColour;
	private float head_bob[];
	private GeneralPath neck;

	private float eyelid[];
	private float eyebrow_raise[];
	private float eyebrow_squeeze[];
	private float mouth_vertical[];
	private float mouth_horizontal[];
	private float mouth2_vertical[];
	private float mouth2_horizontal[];
	private float corner_mouth_vertical[];
	private float eyebrowLeftExpression[];
	private float eyebrowRightExpression[];
	private float mouthExpression[];
	private float eyelidTopLeftExpression[];
	private float eyelidTopRightExpression[];
	private float eyelidBottomLeftExpression[];
	private float eyelidBottomRightExpression[];
	private float pupilLeftExpression[];
	private float pupilRightExpression[];
	private float browExpression[];
	private float visemeDispl[];

	private Color neck_colour = new Color(221, 195, 153);
	private Color hair_colour1 = new Color(153, 51, 0);
	private Color hair_colour2 = new Color(220, 135, 58);
	private Color hair_colour3 = new Color(237, 180, 105);
	private Color white = new Color(255, 255, 255);
	private Color black = new Color(0, 0, 0);
	private Color tounge_colour = new Color(164, 14, 18);
	private Color gum_colour = new Color(255, 100, 100);
	private Color mouth_colour = new Color(125, 9, 42);
	private BasicStroke eyebrow_stroke;
	private BasicStroke mouth_stroke;
	private BasicStroke lip_stroke;

	private FAPUData FAPU;


	/**
	 *  Creates new SpikyMPEG4Face
	 */
	public SpikyMPEG4Face() {
		super();

		//Set Facial Animation Parameter Units (FAPU) for the MPEG-4 Face
		FAPU = new FAPUData(2.5f, 1.0f, 2.0f, 2.4f, 2.8f, 0.00001f);

		int i;

		//For each MPEG-4 Expression: define the feature point translations from the neutral face
		float neutralEyebrowLeft[] = {1.8f, 4.0f, 2.7f, 3.0f, 3.5f, 3.6f};
		float neutralEyebrowRight[] = {4.3f, 3.6f, 5.1f, 3.0f, 6.0f, 4.0f};
		float neutralMouth[] = {2.8f, 7.4f, 3.45f, 7.4f, 3.9f, 7.4f, 4.35f, 7.4f, 5.0f, 7.4f, 4.35f, 7.4f, 3.9f, 7.4f, 3.45f, 7.4f};
		float neutralEyelidTopLeft[] = {1.7f, 4.1f, 1.7f, 3.6f, 3.9f, 3.6f, 3.9f, 4.1f, 2.7f, 3.4f, 1.7f, 4.1f};
		float neutralEyelidTopRight[] = {3.9f, 4.1f, 3.9f, 3.6f, 6.1f, 3.6f, 6.1f, 4.1f, 5.1f, 3.4f, 3.9f, 4.1f};
		float neutralEyelidBottomLeft[] = {1.7f, 6.8f, 1.7f, 6.9f, 3.9f, 6.9f, 3.9f, 6.8f, 2.7f, 6.2f, 1.7f, 6.8f};
		float neutralEyelidBottomRight[] = {3.9f, 6.8f, 3.9f, 6.9f, 6.1f, 6.9f, 6.1f, 6.8f, 5.1f, 6.2f, 3.9f, 6.8f};
		float neutralPupilLeft[] = {2.7f, 5.2f, 0.3f, 2.7f, 5.2f, 0.15f};
		float neutralPupilRight[] = {4.8f, 5.2f, 0.3f, 4.8f, 5.2f, 0.15f};
		float neutralBrow[] = {2.4f, 3.5f, 5.4f, 3.5f, 3.9f, 3.5f, 3.9f, 3.5f, 3.9f, 3.5f};

		float angerEyebrowLeft[] = {1.8f, 3.8f, 3.1f, 3.0f, 3.5f, 3.9f};
		float angerEyebrowRight[] = {4.3f, 3.9f, 4.7f, 3.0f, 6.0f, 3.8f};
		float angerMouth[] = {2.8f, 7.7f, 3.45f, 7.4f, 3.9f, 7.4f, 4.35f, 7.4f, 5.0f, 7.7f, 4.35f, 7.4f, 3.9f, 7.4f, 3.45f, 7.4f};
		float angerEyelidTopLeft[] = {1.7f, 4.1f, 1.7f, 3.6f, 3.9f, 3.6f, 3.9f, 4.1f, 2.7f, 3.4f, 1.7f, 4.1f};
		float angerEyelidTopRight[] = {3.9f, 4.1f, 3.9f, 3.6f, 6.1f, 3.6f, 6.1f, 4.1f, 5.1f, 3.4f, 3.9f, 4.1f};
		float angerEyelidBottomLeft[] = {1.7f, 6.8f, 1.7f, 6.9f, 3.9f, 6.9f, 3.9f, 6.8f, 2.7f, 6.2f, 1.7f, 6.8f};
		float angerEyelidBottomRight[] = {3.9f, 6.8f, 3.9f, 6.9f, 6.1f, 6.9f, 6.1f, 6.8f, 5.1f, 6.2f, 3.9f, 6.8f};
		float angerPupilLeft[] = {2.7f, 5.2f, 0.25f, 2.7f, 5.2f, 0.125f};
		float angerPupilRight[] = {4.8f, 5.2f, 0.25f, 4.8f, 5.2f, 0.125f};
		float angerBrow[] = {2.4f, 3.5f, 5.4f, 3.5f, 3.9f, 5.0f, 3.9f, 5.0f, 3.9f, 5.0f};

		float sadEyebrowLeft[] = {1.8f, 4.0f, 2.7f, 3.9f, 3.1f, 3.4f};
		float sadEyebrowRight[] = {4.6f, 3.4f, 5.1f, 3.9f, 6.0f, 4.0f};
		float sadMouth[] = {2.8f, 7.6f, 3.45f, 7.4f, 3.9f, 7.5f, 4.35f, 7.4f, 5.0f, 7.6f, 4.35f, 7.4f, 3.9f, 7.5f, 3.45f, 7.4f};
		float sadEyelidTopLeft[] = {1.7f, 4.1f, 1.7f, 3.6f, 3.9f, 3.6f, 3.9f, 4.1f, 2.7f, 3.4f, 1.7f, 4.1f};
		float sadEyelidTopRight[] = {3.9f, 4.1f, 3.9f, 3.6f, 6.1f, 3.6f, 6.1f, 4.1f, 5.1f, 3.4f, 3.9f, 4.1f};
		float sadEyelidBottomLeft[] = {1.7f, 6.8f, 1.7f, 6.9f, 3.9f, 6.9f, 3.9f, 6.8f, 2.7f, 6.2f, 1.7f, 6.8f};
		float sadEyelidBottomRight[] = {3.9f, 6.8f, 3.9f, 6.9f, 6.1f, 6.9f, 6.1f, 6.8f, 5.1f, 6.2f, 3.9f, 6.8f};
		float sadPupilLeft[] = {2.7f, 5.2f, 0.2f, 2.7f, 5.2f, 0.1f};
		float sadPupilRight[] = {4.8f, 5.2f, 0.2f, 4.8f, 5.2f, 0.1f};
		float sadBrow[] = {1.4f, 3.5f, 6.4f, 3.5f, 6.4f, 5.0f, 3.9f, 3.5f, 1.4f, 5.0f};

		float surpriseEyebrowLeft[] = {1.8f, 3.7f, 2.7f, 2.5f, 3.5f, 3.3f};
		float surpriseEyebrowRight[] = {4.3f, 3.3f, 5.1f, 2.5f, 6.0f, 3.7f};
		float surpriseMouth[] = {3.4f, 7.5f, 3.65f, 7.4f, 3.9f, 7.4f, 4.15f, 7.4f, 4.4f, 7.5f, 4.15f, 7.4f, 3.9f, 7.4f, 3.65f, 7.4f};
		float surpriseEyelidTopLeft[] = {1.7f, 4.1f, 1.7f, 3.6f, 3.9f, 3.6f, 3.9f, 4.1f, 2.7f, 3.4f, 1.7f, 4.1f};
		float surpriseEyelidTopRight[] = {3.9f, 4.1f, 3.9f, 3.6f, 6.1f, 3.6f, 6.1f, 4.1f, 5.1f, 3.4f, 3.9f, 4.1f};
		float surpriseEyelidBottomLeft[] = {1.7f, 6.8f, 1.7f, 6.9f, 3.9f, 6.9f, 3.9f, 6.8f, 2.7f, 6.2f, 1.7f, 6.8f};
		float surpriseEyelidBottomRight[] = {3.9f, 6.8f, 3.9f, 6.9f, 6.1f, 6.9f, 6.1f, 6.8f, 5.1f, 6.2f, 3.9f, 6.8f};
		float surprisePupilLeft[] = {2.7f, 5.2f, 0.3f, 2.7f, 5.2f, 0.15f};
		float surprisePupilRight[] = {4.8f, 5.2f, 0.3f, 4.8f, 5.2f, 0.15f};
		float surpriseBrow[] = {2.4f, 3.5f, 5.4f, 3.5f, 3.9f, 3.5f, 3.9f, 3.5f, 3.9f, 3.5f};

		float joyEyebrowLeft[] = {1.8f, 4.0f, 2.7f, 3.0f, 3.5f, 3.6f};
		float joyEyebrowRight[] = {4.3f, 3.6f, 5.1f, 3.0f, 6.0f, 4.0f};
		float joyMouth[] = {2.8f, 7.2f, 3.45f, 7.4f, 3.9f, 7.4f, 4.35f, 7.4f, 5.0f, 7.2f, 4.35f, 7.4f, 3.9f, 7.4f, 3.45f, 7.4f};
		float joyEyelidTopLeft[] = {1.7f, 4.1f, 1.7f, 3.6f, 3.9f, 3.6f, 3.9f, 4.1f, 2.7f, 3.4f, 1.7f, 4.1f};
		float joyEyelidTopRight[] = {3.9f, 4.1f, 3.9f, 3.6f, 6.1f, 3.6f, 6.1f, 4.1f, 5.1f, 3.4f, 3.9f, 4.1f};
		float joyEyelidBottomLeft[] = {1.7f, 6.8f, 1.7f, 6.9f, 3.9f, 6.9f, 3.9f, 6.8f, 2.7f, 6.2f, 1.7f, 6.8f};
		float joyEyelidBottomRight[] = {3.9f, 6.8f, 3.9f, 6.9f, 6.1f, 6.9f, 6.1f, 6.8f, 5.1f, 6.2f, 3.9f, 6.8f};
		float joyPupilLeft[] = {2.7f, 5.2f, 0.3f, 2.7f, 5.2f, 0.15f};
		float joyPupilRight[] = {4.8f, 5.2f, 0.3f, 4.8f, 5.2f, 0.15f};
		float joyBrow[] = {2.4f, 3.5f, 5.4f, 3.5f, 3.9f, 3.5f, 3.9f, 3.5f, 3.9f, 3.5f};

		float disgustEyebrowLeft[] = {1.8f, 4.2f, 2.5f, 2.8f, 3.5f, 3.6f};
		float disgustEyebrowRight[] = {4.3f, 4.0f, 5.1f, 4.5f, 6.0f, 4.0f};
		float disgustMouth[] = {2.8f, 7.4f, 3.45f, 7.2f, 3.9f, 7.4f, 4.35f, 7.4f, 5.0f, 7.35f, 4.35f, 7.4f, 3.9f, 7.4f, 3.45f, 7.2f};
		float disgustEyelidTopLeft[] = {1.7f, 4.1f, 1.7f, 3.6f, 3.9f, 3.6f, 3.9f, 5.6f, 2.7f, 4.9f, 1.7f, 5.6f};
		float disgustEyelidTopRight[] = {3.9f, 4.1f, 3.9f, 3.6f, 6.1f, 3.6f, 6.1f, 5.6f, 5.1f, 4.9f, 3.9f, 5.6f};
		float disgustEyelidBottomLeft[] = {1.7f, 6.8f, 1.7f, 6.9f, 3.9f, 6.9f, 3.9f, 6.8f, 2.7f, 6.2f, 1.7f, 6.8f};
		float disgustEyelidBottomRight[] = {3.9f, 6.8f, 3.9f, 6.9f, 6.1f, 6.9f, 6.1f, 6.8f, 5.1f, 6.2f, 3.9f, 6.8f};
		float disgustPupilLeft[] = {2.7f, 5.2f, 0.3f, 2.7f, 5.2f, 0.15f};
		float disgustPupilRight[] = {4.8f, 5.2f, 0.3f, 4.8f, 5.2f, 0.15f};
		float disgustBrow[] = {2.4f, 3.5f, 5.4f, 3.5f, 3.9f, 3.5f, 3.9f, 3.5f, 3.9f, 3.5f};

		float fearEyebrowLeft[] = {2.0f, 3.4f, 3.7f, 3.8f, 3.7f, 3.2f};
		float fearEyebrowRight[] = {4.1f, 3.2f, 4.1f, 3.8f, 5.8f, 3.4f};
		float fearMouth[] = {2.8f, 7.5f, 3.45f, 7.4f, 3.9f, 7.4f, 4.35f, 7.4f, 5.0f, 7.5f, 4.35f, 7.4f, 3.9f, 7.4f, 3.45f, 7.4f};
		float fearEyelidTopLeft[] = {1.7f, 4.1f, 1.7f, 3.6f, 3.9f, 3.6f, 3.9f, 4.1f, 2.7f, 3.4f, 1.7f, 4.1f};
		float fearEyelidTopRight[] = {3.9f, 4.1f, 3.9f, 3.6f, 6.1f, 3.6f, 6.1f, 4.1f, 5.1f, 3.4f, 3.9f, 4.1f};
		float fearEyelidBottomLeft[] = {1.7f, 6.8f, 1.7f, 6.9f, 3.9f, 6.9f, 3.9f, 6.8f, 2.7f, 6.2f, 1.7f, 6.8f};
		float fearEyelidBottomRight[] = {3.9f, 6.8f, 3.9f, 6.9f, 6.1f, 6.9f, 6.1f, 6.8f, 5.1f, 6.2f, 3.9f, 6.8f};
		float fearPupilLeft[] = {2.7f, 5.2f, 0.4f, 2.7f, 5.2f, 0.2f};
		float fearPupilRight[] = {4.8f, 5.2f, 0.4f, 4.8f, 5.2f, 0.2f};
		float fearBrow[] = {2.4f, 3.5f, 5.4f, 3.5f, 3.9f, 3.5f, 3.9f, 3.5f, 3.9f, 3.5f};

		//Put all of these translations into a look up table for quick access when rendering
		expressionCoords = new MPEG4ExpressionCoordinates(16);

		expressionCoords.setCoords(MPEG4ExpressionCoordinates.NEUTRAL, MPEG4ExpressionCoordinates.EYEBROW_LEFT, neutralEyebrowLeft, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.NEUTRAL, MPEG4ExpressionCoordinates.EYEBROW_RIGHT, neutralEyebrowRight, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.NEUTRAL, MPEG4ExpressionCoordinates.MOUTH, neutralMouth, 16);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.NEUTRAL, MPEG4ExpressionCoordinates.PUPIL_LEFT, neutralPupilLeft, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.NEUTRAL, MPEG4ExpressionCoordinates.PUPIL_RIGHT, neutralPupilRight, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.NEUTRAL, MPEG4ExpressionCoordinates.EYELID_TOP_LEFT, neutralEyelidTopLeft, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.NEUTRAL, MPEG4ExpressionCoordinates.EYELID_TOP_RIGHT, neutralEyelidTopRight, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.NEUTRAL, MPEG4ExpressionCoordinates.EYELID_BOTTOM_LEFT, neutralEyelidBottomLeft, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.NEUTRAL, MPEG4ExpressionCoordinates.EYELID_BOTTOM_RIGHT, neutralEyelidBottomRight, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.NEUTRAL, MPEG4ExpressionCoordinates.BROW, neutralBrow, 10);

		expressionCoords.setCoords(MPEG4ExpressionCoordinates.ANGER, MPEG4ExpressionCoordinates.EYEBROW_LEFT, angerEyebrowLeft, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.ANGER, MPEG4ExpressionCoordinates.EYEBROW_RIGHT, angerEyebrowRight, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.ANGER, MPEG4ExpressionCoordinates.MOUTH, angerMouth, 16);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.ANGER, MPEG4ExpressionCoordinates.PUPIL_LEFT, angerPupilLeft, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.ANGER, MPEG4ExpressionCoordinates.PUPIL_RIGHT, angerPupilRight, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.ANGER, MPEG4ExpressionCoordinates.EYELID_TOP_LEFT, angerEyelidTopLeft, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.ANGER, MPEG4ExpressionCoordinates.EYELID_TOP_RIGHT, angerEyelidTopRight, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.ANGER, MPEG4ExpressionCoordinates.EYELID_BOTTOM_LEFT, angerEyelidBottomLeft, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.ANGER, MPEG4ExpressionCoordinates.EYELID_BOTTOM_RIGHT, angerEyelidBottomRight, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.ANGER, MPEG4ExpressionCoordinates.BROW, angerBrow, 10);

		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SADNESS, MPEG4ExpressionCoordinates.EYEBROW_LEFT, sadEyebrowLeft, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SADNESS, MPEG4ExpressionCoordinates.EYEBROW_RIGHT, sadEyebrowRight, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SADNESS, MPEG4ExpressionCoordinates.MOUTH, sadMouth, 16);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SADNESS, MPEG4ExpressionCoordinates.PUPIL_LEFT, sadPupilLeft, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SADNESS, MPEG4ExpressionCoordinates.PUPIL_RIGHT, sadPupilRight, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SADNESS, MPEG4ExpressionCoordinates.EYELID_TOP_LEFT, sadEyelidTopLeft, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SADNESS, MPEG4ExpressionCoordinates.EYELID_TOP_RIGHT, sadEyelidTopRight, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SADNESS, MPEG4ExpressionCoordinates.EYELID_BOTTOM_LEFT, sadEyelidBottomLeft, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SADNESS, MPEG4ExpressionCoordinates.EYELID_BOTTOM_RIGHT, sadEyelidBottomRight, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SADNESS, MPEG4ExpressionCoordinates.BROW, sadBrow, 10);

		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SURPRISE, MPEG4ExpressionCoordinates.EYEBROW_LEFT, surpriseEyebrowLeft, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SURPRISE, MPEG4ExpressionCoordinates.EYEBROW_RIGHT, surpriseEyebrowRight, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SURPRISE, MPEG4ExpressionCoordinates.MOUTH, surpriseMouth, 16);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SURPRISE, MPEG4ExpressionCoordinates.PUPIL_LEFT, surprisePupilLeft, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SURPRISE, MPEG4ExpressionCoordinates.PUPIL_RIGHT, surprisePupilRight, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SURPRISE, MPEG4ExpressionCoordinates.EYELID_TOP_LEFT, surpriseEyelidTopLeft, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SURPRISE, MPEG4ExpressionCoordinates.EYELID_TOP_RIGHT, surpriseEyelidTopRight, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SURPRISE, MPEG4ExpressionCoordinates.EYELID_BOTTOM_LEFT, surpriseEyelidBottomLeft, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SURPRISE, MPEG4ExpressionCoordinates.EYELID_BOTTOM_RIGHT, surpriseEyelidBottomRight, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.SURPRISE, MPEG4ExpressionCoordinates.BROW, surpriseBrow, 10);

		expressionCoords.setCoords(MPEG4ExpressionCoordinates.JOY, MPEG4ExpressionCoordinates.EYEBROW_LEFT, joyEyebrowLeft, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.JOY, MPEG4ExpressionCoordinates.EYEBROW_RIGHT, joyEyebrowRight, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.JOY, MPEG4ExpressionCoordinates.MOUTH, joyMouth, 16);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.JOY, MPEG4ExpressionCoordinates.PUPIL_LEFT, joyPupilLeft, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.JOY, MPEG4ExpressionCoordinates.PUPIL_RIGHT, joyPupilRight, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.JOY, MPEG4ExpressionCoordinates.EYELID_TOP_LEFT, joyEyelidTopLeft, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.JOY, MPEG4ExpressionCoordinates.EYELID_TOP_RIGHT, joyEyelidTopRight, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.JOY, MPEG4ExpressionCoordinates.EYELID_BOTTOM_LEFT, joyEyelidBottomLeft, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.JOY, MPEG4ExpressionCoordinates.EYELID_BOTTOM_RIGHT, joyEyelidBottomRight, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.JOY, MPEG4ExpressionCoordinates.BROW, joyBrow, 10);

		expressionCoords.setCoords(MPEG4ExpressionCoordinates.DISGUST, MPEG4ExpressionCoordinates.EYEBROW_LEFT, disgustEyebrowLeft, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.DISGUST, MPEG4ExpressionCoordinates.EYEBROW_RIGHT, disgustEyebrowRight, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.DISGUST, MPEG4ExpressionCoordinates.MOUTH, disgustMouth, 16);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.DISGUST, MPEG4ExpressionCoordinates.PUPIL_LEFT, disgustPupilLeft, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.DISGUST, MPEG4ExpressionCoordinates.PUPIL_RIGHT, disgustPupilRight, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.DISGUST, MPEG4ExpressionCoordinates.EYELID_TOP_LEFT, disgustEyelidTopLeft, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.DISGUST, MPEG4ExpressionCoordinates.EYELID_TOP_RIGHT, disgustEyelidTopRight, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.DISGUST, MPEG4ExpressionCoordinates.EYELID_BOTTOM_LEFT, disgustEyelidBottomLeft, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.DISGUST, MPEG4ExpressionCoordinates.EYELID_BOTTOM_RIGHT, disgustEyelidBottomRight, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.DISGUST, MPEG4ExpressionCoordinates.BROW, disgustBrow, 10);

		expressionCoords.setCoords(MPEG4ExpressionCoordinates.FEAR, MPEG4ExpressionCoordinates.EYEBROW_LEFT, fearEyebrowLeft, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.FEAR, MPEG4ExpressionCoordinates.EYEBROW_RIGHT, fearEyebrowRight, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.FEAR, MPEG4ExpressionCoordinates.MOUTH, fearMouth, 16);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.FEAR, MPEG4ExpressionCoordinates.PUPIL_LEFT, fearPupilLeft, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.FEAR, MPEG4ExpressionCoordinates.PUPIL_RIGHT, fearPupilRight, 6);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.FEAR, MPEG4ExpressionCoordinates.EYELID_TOP_LEFT, fearEyelidTopLeft, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.FEAR, MPEG4ExpressionCoordinates.EYELID_TOP_RIGHT, fearEyelidTopRight, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.FEAR, MPEG4ExpressionCoordinates.EYELID_BOTTOM_LEFT, fearEyelidBottomLeft, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.FEAR, MPEG4ExpressionCoordinates.EYELID_BOTTOM_RIGHT, fearEyelidBottomRight, 12);
		expressionCoords.setCoords(MPEG4ExpressionCoordinates.FEAR, MPEG4ExpressionCoordinates.BROW, fearBrow, 10);

		PI = new Float(Math.PI);
		theta = 0.0;
		theta2 = 0.0;

		//Create geomtric coordinates for face
		black_mouth = new Rectangle2D.Float(0.84f, 5.7f, 6f, 4f);
		gums = new Rectangle2D.Float(0.84f, 5.7f, 6f, 1.4f);
		teeth = new Rectangle2D.Float(0.84f, 7.03f, 6f, 0.3f);
		teeth_lines = new Line2D.Float[15];
		teeth_lines[0] = new Line2D.Float(2.84f, 7.03f, 2.84f, 7.33f);
		teeth_lines[8] = new Line2D.Float(4.14f, 7.03f, 4.14f, 7.33f);
		teeth_lines[1] = new Line2D.Float(2.94f, 7.03f, 2.94f, 7.33f);
		teeth_lines[9] = new Line2D.Float(4.34f, 7.03f, 4.34f, 7.33f);
		teeth_lines[2] = new Line2D.Float(3.04f, 7.03f, 3.04f, 7.33f);
		teeth_lines[10] = new Line2D.Float(4.44f, 7.03f, 4.44f, 7.33f);
		teeth_lines[3] = new Line2D.Float(3.14f, 7.03f, 3.14f, 7.33f);
		teeth_lines[11] = new Line2D.Float(4.54f, 7.03f, 4.54f, 7.33f);
		teeth_lines[4] = new Line2D.Float(3.24f, 7.03f, 3.24f, 7.33f);
		teeth_lines[12] = new Line2D.Float(4.64f, 7.03f, 4.64f, 7.33f);
		teeth_lines[5] = new Line2D.Float(3.34f, 7.03f, 3.34f, 7.33f);
		teeth_lines[13] = new Line2D.Float(4.74f, 7.03f, 4.74f, 7.33f);
		teeth_lines[6] = new Line2D.Float(3.54f, 7.03f, 3.54f, 7.33f);
		teeth_lines[14] = new Line2D.Float(4.84f, 7.03f, 4.84f, 7.33f);
		teeth_lines[7] = new Line2D.Float(3.84f, 7.03f, 3.84f, 7.33f);
		tounge_semi = new QuadCurve2D.Float(3.34f, 8.73f, 3.84f, 8.73f, 4.34f, 8.73f);
		tounge_poke = new float[15];
		tounge_poke[0] = 0.0f;
		tounge_poke[4] = 1f;
		tounge_poke[8] = 1f;
		tounge_poke[12] = 0.0f;
		tounge_poke[1] = 0.0f;
		tounge_poke[5] = 0.0f;
		tounge_poke[9] = 0.0f;
		tounge_poke[13] = 0.0f;
		tounge_poke[2] = 0.0f;
		tounge_poke[6] = 0.0f;
		tounge_poke[10] = 0.0f;
		tounge_poke[14] = 0.0f;
		tounge_poke[3] = 0.8f;
		tounge_poke[7] = 0.0f;
		tounge_poke[11] = 0.0f;

		head = new Ellipse2D.Float(0.5f, 1.7f, 6.8f, 7.0f);

		neck = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		neck.moveTo(2.6f, 17.0f);
		neck.lineTo(3.3f, 7.0f);
		neck.lineTo(5.3f, 7.0f);
		neck.lineTo(5.9f, 17.0f);
		neck.closePath();

		hair = new GeneralPath[3];
		hair[0] = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		hair[0].moveTo(0.8f, 3.7f);
		hair[0].lineTo(0.3f, 3.1f);
		hair[0].lineTo(0.9f, 3.0f);
		hair[0].lineTo(0.6f, 2.1f);
		hair[0].lineTo(1.0f, 2.5f);
		hair[0].lineTo(0.9f, 1.3f);
		hair[0].lineTo(1.65f, 2.0f);
		hair[0].lineTo(2.0f, 0.6f);
		hair[0].lineTo(2.7f, 1.5f);
		hair[0].lineTo(2.6f, 0.4f);
		hair[0].lineTo(3.1f, 1.3f);
		hair[0].lineTo(3.6f, 0.2f);
		hair[0].lineTo(4.0f, 1.5f);
		hair[0].lineTo(4.3f, 0.2f);
		hair[0].lineTo(4.5f, 1.0f);
		hair[0].lineTo(4.7f, 0.0f);
		hair[0].lineTo(5.2f, 1.1f);
		hair[0].lineTo(6.0f, 0.5f);
		hair[0].lineTo(6.0f, 1.8f);
		hair[0].lineTo(6.5f, 0.9f);
		hair[0].lineTo(6.6f, 2.8f);
		hair[0].lineTo(7.0f, 2.0f);
		hair[0].lineTo(7.2f, 3.1f);
		hair[0].lineTo(7.6f, 3.2f);
		hair[0].lineTo(7.0f, 3.7f);
		hair[0].quadTo(3.9f, 0.0f, 0.8f, 3.7f);
		hair[0].closePath();

		hair[1] = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		hair[1].moveTo(1.0f, 3.8f);
		hair[1].lineTo(0.4f, 2.8f);
		hair[1].lineTo(1.0f, 2.8f);
		hair[1].lineTo(0.7f, 1.7f);
		hair[1].lineTo(1.4f, 2.5f);
		hair[1].lineTo(1.2f, 1.3f);
		hair[1].lineTo(2.0f, 2.2f);
		hair[1].lineTo(2.0f, 1.1f);
		hair[1].lineTo(2.7f, 1.9f);
		hair[1].lineTo(2.8f, 0.9f);
		hair[1].lineTo(3.1f, 1.5f);
		hair[1].lineTo(3.3f, 0.7f);
		hair[1].lineTo(3.7f, 1.6f);
		hair[1].lineTo(4.0f, 0.7f);
		hair[1].lineTo(4.5f, 2.1f);
		hair[1].lineTo(4.9f, 0.5f);
		hair[1].lineTo(5.2f, 1.8f);
		hair[1].lineTo(5.8f, 1.0f);
		hair[1].lineTo(6.0f, 2.1f);
		hair[1].lineTo(6.4f, 1.7f);
		hair[1].lineTo(6.4f, 2.5f);
		hair[1].lineTo(7.0f, 1.7f);
		hair[1].lineTo(6.8f, 3.1f);
		hair[1].lineTo(7.3f, 3.2f);
		hair[1].lineTo(6.8f, 3.8f);
		hair[1].quadTo(3.9f, 1.0f, 1.0f, 3.8f);
		hair[1].closePath();

		hair[2] = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		hair[2].moveTo(1.2f, 3.9f);
		hair[2].lineTo(1.0f, 2.5f);
		hair[2].lineTo(1.6f, 3.0f);
		hair[2].lineTo(1.5f, 2.0f);
		hair[2].lineTo(2.0f, 2.7f);
		hair[2].lineTo(2.4f, 1.5f);
		hair[2].lineTo(2.8f, 2.1f);
		hair[2].lineTo(3.0f, 1.4f);
		hair[2].lineTo(3.3f, 2.1f);
		hair[2].lineTo(3.6f, 1.2f);
		hair[2].lineTo(4.2f, 2.1f);
		hair[2].lineTo(4.5f, 1.1f);
		hair[2].lineTo(4.9f, 2.3f);
		hair[2].lineTo(5.2f, 1.2f);
		hair[2].lineTo(5.5f, 2.5f);
		hair[2].lineTo(6.1f, 1.7f);
		hair[2].lineTo(6.3f, 2.9f);
		hair[2].lineTo(6.8f, 2.3f);
		hair[2].lineTo(6.6f, 3.9f);
		hair[2].lineTo(5.4f, 2.8f);
		hair[2].lineTo(3.9f, 2.5f);
		hair[2].lineTo(2.4f, 2.8f);
		hair[2].lineTo(1.2f, 3.9f);
		hair[2].closePath();

		hair_back = new GeneralPath[3];
		hair_back[2] = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		hair_back[2].moveTo(1.2f, 3.9f);
		hair_back[2].lineTo(1.0f, 2.5f);
		hair_back[2].lineTo(1.6f, 3.0f);
		hair_back[2].lineTo(1.5f, 2.0f);
		hair_back[2].lineTo(2.0f, 2.7f);
		hair_back[2].lineTo(2.4f, 1.5f);
		hair_back[2].lineTo(2.8f, 2.1f);
		hair_back[2].lineTo(3.0f, 1.4f);
		hair_back[2].lineTo(3.3f, 2.1f);
		hair_back[2].lineTo(3.6f, 1.2f);
		hair_back[2].lineTo(4.2f, 2.1f);
		hair_back[2].lineTo(4.5f, 1.1f);
		hair_back[2].lineTo(4.9f, 2.3f);
		hair_back[2].lineTo(5.2f, 1.2f);
		hair_back[2].lineTo(5.5f, 2.5f);
		hair_back[2].lineTo(6.1f, 1.7f);
		hair_back[2].lineTo(6.3f, 2.9f);
		hair_back[2].lineTo(6.8f, 2.3f);
		hair_back[2].lineTo(6.6f, 3.9f);
		hair_back[2].lineTo(1.2f, 3.9f);
		hair_back[2].closePath();

		hair_left = new GeneralPath[3];
		hair_left[0] = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		hair_left[0].moveTo(2.35f, 3.0f);
		hair_left[0].lineTo(3.35f, 0.6f);
		hair_left[0].lineTo(3.9f, 1.3f);
		hair_left[0].lineTo(4.6f, 0.1f);
		hair_left[0].lineTo(4.6f, 1.5f);
		hair_left[0].lineTo(6.2f, 0.4f);
		hair_left[0].lineTo(5.6f, 1.8f);
		hair_left[0].lineTo(6.7f, 1.5f);
		hair_left[0].lineTo(6.5f, 2.0f);
		hair_left[0].lineTo(7.5f, 1.85f);
		hair_left[0].lineTo(6.5f, 2.8f);
		hair_left[0].lineTo(5.9f, 2.85f);
		hair_left[0].lineTo(5.3f, 2.8f);
		hair_left[0].lineTo(4.5f, 3.05f);
		hair_left[0].closePath();

		hair_left[1] = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		hair_left[1].moveTo(1.5f, 3.85f);
		hair_left[1].lineTo(1.2f, 1.8f);
		hair_left[1].lineTo(1.45f, 1.9f);
		hair_left[1].lineTo(1.75f, 1.1f);
		hair_left[1].lineTo(1.9f, 2.2f);
		hair_left[1].lineTo(2.4f, 0.7f);
		hair_left[1].lineTo(2.4f, 1.6f);
		hair_left[1].lineTo(2.75f, 0.7f);
		hair_left[1].lineTo(3.3f, 1.6f);
		hair_left[1].lineTo(3.85f, 0.6f);
		hair_left[1].lineTo(3.6f, 2.2f);
		hair_left[1].lineTo(4.05f, 1.9f);
		hair_left[1].lineTo(3.9f, 2.9f);
		hair_left[1].lineTo(4.3f, 2.5f);
		hair_left[1].lineTo(4.5f, 3.4f);
		hair_left[1].lineTo(3.7f, 3.4f);
		hair_left[1].lineTo(2.8f, 3.55f);
		hair_left[1].lineTo(2.3f, 3.85f);
		hair_left[1].closePath();

		hair_left[2] = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		hair_left[2].moveTo(0.6f, 4.65f);
		hair_left[2].lineTo(0.0f, 3.6f);
		hair_left[2].lineTo(0.6f, 3.65f);
		hair_left[2].lineTo(0.5f, 2.85f);
		hair_left[2].lineTo(1.0f, 3.15f);
		hair_left[2].lineTo(1.05f, 2.1f);
		hair_left[2].lineTo(1.65f, 3.2f);
		hair_left[2].lineTo(1.9f, 2.7f);
		hair_left[2].lineTo(2.0f, 3.6f);
		hair_left[2].lineTo(2.35f, 3.3f);
		hair_left[2].lineTo(2.2f, 4.1f);
		hair_left[2].lineTo(2.0f, 4.2f);
		hair_left[2].lineTo(1.2f, 4.4f);
		hair_left[2].closePath();

		hair_right = new GeneralPath[3];
		hair_right[0] = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		hair_right[0].moveTo((3.9f - 2.35f) + 3.9f, 3.0f);
		hair_right[0].lineTo((3.9f - 3.35f) + 3.9f, 0.6f);
		hair_right[0].lineTo((3.9f - 3.9f) + 3.9f, 1.3f);
		hair_right[0].lineTo((3.9f - 4.6f) + 3.9f, 0.1f);
		hair_right[0].lineTo((3.9f - 4.6f) + 3.9f, 1.5f);
		hair_right[0].lineTo((3.9f - 6.2f) + 3.9f, 0.4f);
		hair_right[0].lineTo((3.9f - 5.6f) + 3.9f, 1.8f);
		hair_right[0].lineTo((3.9f - 6.7f) + 3.9f, 1.5f);
		hair_right[0].lineTo((3.9f - 6.5f) + 3.9f, 2.0f);
		hair_right[0].lineTo((3.9f - 7.5f) + 3.9f, 1.85f);
		hair_right[0].lineTo((3.9f - 6.5f) + 3.9f, 2.8f);
		hair_right[0].lineTo((3.9f - 5.9f) + 3.9f, 2.85f);
		hair_right[0].lineTo((3.9f - 5.3f) + 3.9f, 2.8f);
		hair_right[0].lineTo((3.9f - 4.5f) + 3.9f, 3.05f);
		hair_right[0].closePath();

		hair_right[1] = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		hair_right[1].moveTo((3.9f - 1.5f) + 3.9f, 3.85f);
		hair_right[1].lineTo((3.9f - 1.2f) + 3.9f, 1.8f);
		hair_right[1].lineTo((3.9f - 1.45f) + 3.9f, 1.9f);
		hair_right[1].lineTo((3.9f - 1.75f) + 3.9f, 1.1f);
		hair_right[1].lineTo((3.9f - 1.9f) + 3.9f, 2.2f);
		hair_right[1].lineTo((3.9f - 2.4f) + 3.9f, 0.7f);
		hair_right[1].lineTo((3.9f - 2.4f) + 3.9f, 1.6f);
		hair_right[1].lineTo((3.9f - 2.75f) + 3.9f, 0.7f);
		hair_right[1].lineTo((3.9f - 3.3f) + 3.9f, 1.6f);
		hair_right[1].lineTo((3.9f - 3.85f) + 3.9f, 0.6f);
		hair_right[1].lineTo((3.9f - 3.6f) + 3.9f, 2.2f);
		hair_right[1].lineTo((3.9f - 4.05f) + 3.9f, 1.9f);
		hair_right[1].lineTo((3.9f - 3.9f) + 3.9f, 2.9f);
		hair_right[1].lineTo((3.9f - 4.3f) + 3.9f, 2.5f);
		hair_right[1].lineTo((3.9f - 4.5f) + 3.9f, 3.4f);
		hair_right[1].lineTo((3.9f - 3.7f) + 3.9f, 3.4f);
		hair_right[1].lineTo((3.9f - 2.8f) + 3.9f, 3.55f);
		hair_right[1].lineTo((3.9f - 2.3f) + 3.9f, 3.85f);
		hair_right[1].closePath();

		hair_right[2] = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		hair_right[2].moveTo((3.9f - 0.6f) + 3.9f, 4.65f);
		hair_right[2].lineTo((3.9f - 0.0f) + 3.9f, 3.6f);
		hair_right[2].lineTo((3.9f - 0.6f) + 3.9f, 3.65f);
		hair_right[2].lineTo((3.9f - 0.5f) + 3.9f, 2.85f);
		hair_right[2].lineTo((3.9f - 1.0f) + 3.9f, 3.15f);
		hair_right[2].lineTo((3.9f - 1.05f) + 3.9f, 2.1f);
		hair_right[2].lineTo((3.9f - 1.65f) + 3.9f, 3.2f);
		hair_right[2].lineTo((3.9f - 1.9f) + 3.9f, 2.7f);
		hair_right[2].lineTo((3.9f - 2.0f) + 3.9f, 3.6f);
		hair_right[2].lineTo((3.9f - 2.35f) + 3.9f, 3.3f);
		hair_right[2].lineTo((3.9f - 2.2f) + 3.9f, 4.1f);
		hair_right[2].lineTo((3.9f - 2.0f) + 3.9f, 4.2f);
		hair_right[2].lineTo((3.9f - 1.2f) + 3.9f, 4.4f);
		hair_right[2].closePath();

		left_eyelid = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		left_eyelid.moveTo(1.7f, 4.1f);
		left_eyelid.lineTo(1.7f, 3.6f);
		left_eyelid.lineTo(3.9f, 3.6f);
		left_eyelid.lineTo(3.9f, 4.1f);
		left_eyelid.quadTo(2.7f, 3.4f, 1.7f, 4.1f);
		left_eyelid.closePath();

		right_eyelid = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		right_eyelid.moveTo(3.9f, 4.1f);
		right_eyelid.lineTo(3.9f, 3.6f);
		right_eyelid.lineTo(6.1f, 3.6f);
		right_eyelid.lineTo(6.1f, 4.1f);
		right_eyelid.quadTo(5.1f, 3.4f, 3.9f, 4.1f);
		right_eyelid.closePath();

		left_bottom_eyelid = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		left_bottom_eyelid.moveTo(1.7f, 6.8f);
		left_bottom_eyelid.lineTo(1.7f, 7.6f);
		left_bottom_eyelid.lineTo(3.9f, 7.6f);
		left_bottom_eyelid.lineTo(3.9f, 6.8f);
		left_bottom_eyelid.quadTo(2.7f, 6.2f, 1.7f, 6.8f);
		left_bottom_eyelid.closePath();

		right_bottom_eyelid = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		right_bottom_eyelid.moveTo(3.9f, 6.8f);
		right_bottom_eyelid.lineTo(3.9f, 7.6f);
		right_bottom_eyelid.lineTo(6.1f, 7.6f);
		right_bottom_eyelid.lineTo(6.1f, 6.8f);
		right_bottom_eyelid.quadTo(5.1f, 6.2f, 3.9f, 6.8f);
		right_bottom_eyelid.closePath();

		face_left_eyelid = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		face_left_eyelid.moveTo(1.7f + 3f, 4.1f);
		face_left_eyelid.lineTo(1.7f + 3f, 3.6f);
		face_left_eyelid.lineTo(3.9f + 3f, 3.6f);
		face_left_eyelid.lineTo(3.9f + 3f, 4.1f);
		face_left_eyelid.quadTo(2.7f + 3f, 3.4f, 1.7f + 3f, 4.1f);
		face_left_eyelid.closePath();

		face_right_eyelid = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		face_right_eyelid.moveTo(3.9f - 3f, 4.1f);
		face_right_eyelid.lineTo(3.9f - 3f, 3.6f);
		face_right_eyelid.lineTo(6.1f - 3f, 3.6f);
		face_right_eyelid.lineTo(6.1f - 3f, 4.1f);
		face_right_eyelid.quadTo(5.1f - 3f, 3.4f, 3.9f - 3f, 4.1f);
		face_right_eyelid.closePath();

		face_left_bottom_eyelid = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		face_left_bottom_eyelid.moveTo(1.7f + 3f, 6.8f);
		face_left_bottom_eyelid.lineTo(1.7f + 3f, 7.6f);
		face_left_bottom_eyelid.lineTo(3.9f + 3f, 7.6f);
		face_left_bottom_eyelid.lineTo(3.9f + 3f, 6.8f);
		face_left_bottom_eyelid.quadTo(2.7f + 3f, 6.2f, 1.7f + 3f, 6.8f);
		face_left_bottom_eyelid.closePath();

		face_right_bottom_eyelid = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		face_right_bottom_eyelid.moveTo(3.9f - 3f, 6.8f);
		face_right_bottom_eyelid.lineTo(3.9f - 3f, 7.6f);
		face_right_bottom_eyelid.lineTo(6.1f - 3f, 7.6f);
		face_right_bottom_eyelid.lineTo(6.1f - 3f, 6.8f);
		face_right_bottom_eyelid.quadTo(5.1f - 3f, 6.2f, 3.9f - 3f, 6.8f);
		face_right_bottom_eyelid.closePath();

		brow = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		brow.moveTo(2.4f, 3.5f);
		brow.lineTo(5.4f, 3.5f);
		brow.lineTo(3.9f, 3.5f);
		brow.lineTo(3.9f, 3.5f);
		brow.lineTo(3.9f, 3.5f);
		brow.lineTo(3.9f, 3.5f);
		brow.closePath();

		left_brow = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		left_brow.moveTo(2.4f + 3, 3.5f);
		left_brow.lineTo(3.9f + 3, 3.5f);
		left_brow.closePath();

		right_brow = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		right_brow.moveTo(5.4f - 3, 3.5f);
		right_brow.lineTo(3.9f - 3, 3.5f);
		right_brow.closePath();

		left_eye = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		left_eye.moveTo(2.4f, 4.1f);
		left_eye.quadTo(3.6f, 3.4f, 3.8f, 4.9f);
		left_eye.quadTo(3.9f, 5.7f, 3.2f, 6.4f);
		left_eye.quadTo(2.6f, 6.7f, 2.0f, 6.2f);
		left_eye.quadTo(1.5f, 5.0f, 2.4f, 4.1f);
		left_eye.closePath();
		right_eye = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		right_eye.moveTo(5.4f, 4.1f);
		right_eye.quadTo(4.2f, 3.4f, 4.0f, 4.9f);
		right_eye.quadTo(3.9f, 5.7f, 4.6f, 6.4f);
		right_eye.quadTo(5.2f, 6.7f, 5.8f, 6.2f);
		right_eye.quadTo(6.3f, 5.0f, 5.4f, 4.1f);
		right_eye.closePath();
		face_left_eye = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		face_left_eye.moveTo(2.4f + 3f, 4.1f);
		face_left_eye.quadTo(3.6f + 3f, 3.4f, 3.8f + 3f, 4.9f);
		face_left_eye.quadTo(3.9f + 3f, 5.7f, 3.2f + 3f, 6.4f);
		face_left_eye.quadTo(2.6f + 3f, 6.7f, 2.0f + 3f, 6.2f);
		face_left_eye.quadTo(1.5f + 3f, 5.0f, 2.4f + 3f, 4.1f);
		face_left_eye.closePath();
		face_right_eye = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		face_right_eye.moveTo(5.4f - 3f, 4.1f);
		face_right_eye.quadTo(4.2f - 3f, 3.4f, 4.0f - 3f, 4.9f);
		face_right_eye.quadTo(3.9f - 3f, 5.7f, 4.6f - 3f, 6.4f);
		face_right_eye.quadTo(5.2f - 3f, 6.7f, 5.8f - 3f, 6.2f);
		face_right_eye.quadTo(6.3f - 3f, 5.0f, 5.4f - 3f, 4.1f);
		face_right_eye.closePath();
		left_pupil = new Ellipse2D.Float(2.7f, 5.2f, 0.3f, 0.3f);
		right_pupil = new Ellipse2D.Float(4.8f, 5.2f, 0.3f, 0.3f);
		face_left_pupil = new Ellipse2D.Float(2.7f + 3, 5.2f, 0.3f, 0.3f);
		face_right_pupil = new Ellipse2D.Float(4.8f - 3f, 5.2f, 0.3f, 0.3f);
		left_eyebrow = new QuadCurve2D.Float(1.8f, 4.0f, 2.7f, 3.0f, 3.5f, 3.6f);
		right_eyebrow = new QuadCurve2D.Float(4.3f, 3.6f, 5.1f, 3.0f, 6.0f, 4.0f);
		face_left_eyebrow = new QuadCurve2D.Float(1.8f + 3f, 4.0f, 2.7f + 3f, 3.0f, 3.5f + 3f, 3.6f);
		face_right_eyebrow = new QuadCurve2D.Float(4.3f - 3f, 3.6f, 5.1f - 3f, 3.0f, 6.0f - 3f, 4.0f);
		eyebrow_width = 0.2f;

		mouth = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		mouth.moveTo(2.8f, 7.4f);
		mouth.lineTo(3.45f, 7.4f);
		mouth.lineTo(3.9f, 7.4f);
		mouth.lineTo(4.35f, 7.4f);
		mouth.lineTo(5.0f, 7.4f);
		mouth.lineTo(4.35f, 7.5f);
		mouth.lineTo(3.9f, 7.5f);
		mouth.lineTo(3.45f, 7.5f);
		mouth.lineTo(2.8f, 7.4f);
		mouth_width = 0.07f;
		mouth.closePath();

		left_mouth = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		left_mouth.moveTo(2.8f + 2, 7.4f);
		left_mouth.lineTo(3.9f + 2, 7.4f);
		left_mouth.closePath();

		right_mouth = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		right_mouth.moveTo(2.8f, 7.4f);
		right_mouth.lineTo(3.45f, 7.4f);
		right_mouth.closePath();

		scale_mouth_x = 1.0f;
		scale_mouth_y = 1.0f;

		// For each viseme: set displacements from neutral mouth position
		float[] val;
		val = new float[16];
		val[0] = 0f;
		val[1] = 0f;
		val[2] = 0f;
		val[3] = 0f;
		val[4] = 0f;
		val[5] = 0f;
		val[6] = 0f;
		val[7] = 0f;
		val[8] = 0f;
		val[9] = 0f;
		val[10] = 0f;
		val[11] = 0f;
		val[12] = 0f;
		val[13] = 0f;
		val[14] = 0f;
		val[15] = 0f;
		for (i = 0; i < 16; i++) {
			val[i] = val[i] / 2f;
		}
		expressionCoords.setVisemeDisplacement(0, val, 16);
		val[0] = 0.05f;
		val[1] = 0.1f;
		val[2] = 0f;
		val[3] = 0f;
		val[4] = 0f;
		val[5] = 0.05f;
		val[6] = 0.25f;
		val[7] = 0f;
		val[8] = 0.35f;
		val[9] = 0.1f;
		val[10] = 0.25f;
		val[11] = 0f;
		val[12] = 0f;
		val[13] = 0.05f;
		val[14] = 0f;
		val[15] = 0f;
		for (i = 0; i < 16; i++) {
			val[i] = val[i] / 2f;
		}
		expressionCoords.setVisemeDisplacement(1, val, 16);
		val[0] = 0.05f;
		val[1] = 0.1f - 0.5f;
		val[2] = 0.03f;
		val[3] = 0f - 0.5f;
		val[4] = 0.02f;
		val[5] = -0.1f - 0.5f;
		val[6] = 0.01f;
		val[7] = -0.11f - 0.5f;
		val[8] = 0.22f;
		val[9] = -0.1f - 0.5f;
		val[10] = 0.03f;
		val[11] = 0f - 0.5f;
		val[12] = 0.02f;
		val[13] = -0.1f;
		val[14] = 0.01f;
		val[15] = -0.11f - 0.5f;
		for (i = 0; i < 16; i++) {
			val[i] = val[i] / 2f;
		}
		expressionCoords.setVisemeDisplacement(2, val, 16);
		val[0] = 0.45f;
		val[1] = 0.15f;
		val[2] = 0.2f;
		val[3] = -0.25f;
		val[4] = -0.03f;
		val[5] = -0.6f;
		val[6] = -0.2f;
		val[7] = -0.4f;
		val[8] = -0.4f;
		val[9] = 0f;
		val[10] = -0.3f;
		val[11] = 0.3f;
		val[12] = -0.04f;
		val[13] = 0.3f;
		val[14] = 0.15f;
		val[15] = 0.21f;
		for (i = 0; i < 16; i++) {
			val[i] = val[i] / 2f;
		}
		expressionCoords.setVisemeDisplacement(3, val, 16);
		val[0] = 0.45f;
		val[1] = 0.1f;
		val[2] = 0.1f;
		val[3] = -0.3f;
		val[4] = 0f;
		val[5] = -0.6f;
		val[6] = -0.1f;
		val[7] = -0.3f;
		val[8] = -0.35f;
		val[9] = 0f;
		val[10] = -0.1f;
		val[11] = 0.25f;
		val[12] = 0f;
		val[13] = 0.22f;
		val[14] = 0.27f;
		val[15] = 0.24f;
		for (i = 0; i < 16; i++) {
			val[i] = val[i] / 2f;
		}
		expressionCoords.setVisemeDisplacement(4, val, 16);
		val[0] = 0.55f;
		val[1] = 0.1f;
		val[2] = 0.2f;
		val[3] = -0.27f;
		val[4] = 0.1f;
		val[5] = -0.5f;
		val[6] = 0f;
		val[7] = -0.32f;
		val[8] = -0.3f;
		val[9] = 0f;
		val[10] = -0.05f;
		val[11] = 0.2f;
		val[12] = 0.1f;
		val[13] = 0.2f;
		val[14] = 0.35f;
		val[15] = 0.3f;
		for (i = 0; i < 16; i++) {
			val[i] = val[i] / 2f;
		}
		expressionCoords.setVisemeDisplacement(5, val, 16);
		val[0] = 0.9f;
		val[1] = 0.15f;
		val[2] = 0.45f;
		val[3] = -0.35f;
		val[4] = 0.12f;
		val[5] = -0.45f;
		val[6] = -0.1f;
		val[7] = -0.35f;
		val[8] = -0.4f;
		val[9] = 0.2f;
		val[10] = -0.35f;
		val[11] = 0.15f;
		val[12] = 0.1f;
		val[13] = 0.1f;
		val[14] = 0.57f;
		val[15] = 0.15f;
		for (i = 0; i < 16; i++) {
			val[i] = val[i] / 2f;
		}
		expressionCoords.setVisemeDisplacement(6, val, 16);
		val[0] = 0.55f;
		val[1] = 0.13f;
		val[2] = 0.45f;
		val[3] = -0.25f;
		val[4] = 0.2f;
		val[5] = -0.4f;
		val[6] = 0.2f;
		val[7] = -0.2f;
		val[8] = -0.1f;
		val[9] = 0.3f;
		val[10] = 0f;
		val[11] = 0.2f;
		val[12] = 0.2f;
		val[13] = 0.2f;
		val[14] = 0.23f;
		val[15] = 0.2f;
		for (i = 0; i < 16; i++) {
			val[i] = val[i] / 2f;
		}
		expressionCoords.setVisemeDisplacement(7, val, 16);
		val[0] = 0.45f;
		val[1] = 0.12f;
		val[2] = 0.17f;
		val[3] = -0.2f;
		val[4] = 0.02f;
		val[5] = -0.5f;
		val[6] = -0.2f;
		val[7] = -0.3f;
		val[8] = -0.34f;
		val[9] = 0.1f;
		val[10] = -0.1f;
		val[11] = 0.45f;
		val[12] = 0.07f;
		val[13] = 0.4f;
		val[14] = 0.25f;
		val[15] = 0.42f;
		for (i = 0; i < 16; i++) {
			val[i] = val[i] / 2f;
		}
		expressionCoords.setVisemeDisplacement(8, val, 16);
		val[0] = 0.77f;
		val[1] = 0.05f;
		val[2] = 0.25f;
		val[3] = -0.6f;
		val[4] = 0.08f;
		val[5] = -0.85f;
		val[6] = 0.08f;
		val[7] = -0.5f;
		val[8] = -0.45f;
		val[9] = 0.1f;
		val[10] = -0.2f;
		val[11] = 0.4f;
		val[12] = 0.05f;
		val[13] = 0.3f;
		val[14] = 0.35f;
		val[15] = 0.3f;
		for (i = 0; i < 16; i++) {
			val[i] = val[i] / 2f;
		}
		expressionCoords.setVisemeDisplacement(9, val, 16);
		val[0] = 0.7f;
		val[1] = 0f;
		val[2] = 0.25f;
		val[3] = -0.65f;
		val[4] = 0.1f;
		val[5] = -0.9f;
		val[6] = 0.1f;
		val[7] = -0.5f;
		val[8] = -0.45f;
		val[9] = -0.05f;
		val[10] = 0f;
		val[11] = 0.65f;
		val[12] = 0.2f;
		val[13] = 0.55f;
		val[14] = 0.35f;
		val[15] = 0.6f;
		for (i = 0; i < 16; i++) {
			val[i] = val[i] / 2f;
		}
		expressionCoords.setVisemeDisplacement(10, val, 16);
		val[0] = 0.9f;
		val[1] = 0.15f;
		val[2] = 0.45f;
		val[3] = -0.35f;
		val[4] = 0.12f;
		val[5] = -0.45f;
		val[6] = -0.1f;
		val[7] = -0.35f;
		val[8] = -0.4f;
		val[9] = 0.2f;
		val[10] = -0.35f;
		val[11] = 0.15f;
		val[12] = 0.1f;
		val[13] = 0.1f;
		val[14] = 0.57f;
		val[15] = 0.15f;
		for (i = 0; i < 16; i++) {
			val[i] = val[i] / 2f;
		}
		expressionCoords.setVisemeDisplacement(11, val, 16);
		val[0] = 0.75f;
		val[1] = 0f;
		val[2] = 0.4f;
		val[3] = -0.45f;
		val[4] = 0.1f;
		val[5] = -0.63f;
		val[6] = -0.02f;
		val[7] = -0.44f;
		val[8] = -0.35f;
		val[9] = 0f;
		val[10] = -0.15f;
		val[11] = 0.3f;
		val[12] = 0.1f;
		val[13] = 0.4f;
		val[14] = 0.45f;
		val[15] = 0.3f;
		for (i = 0; i < 16; i++) {
			val[i] = val[i] / 2f;
		}
		expressionCoords.setVisemeDisplacement(12, val, 16);
		val[0] = 0.75f;
		val[1] = 0.1f;
		val[2] = 0.2f;
		val[3] = -0.57f;
		val[4] = 0.07f;
		val[5] = -0.85f;
		val[6] = 0.04f;
		val[7] = -0.5f;
		val[8] = -0.5f;
		val[9] = 0.02f;
		val[10] = -0.2f;
		val[11] = 0.34f;
		val[12] = 0.1f;
		val[13] = 0.6f;
		val[14] = 0.4f;
		val[15] = 0.35f;
		for (i = 0; i < 16; i++) {
			val[i] = val[i] / 2f;
		}
		expressionCoords.setVisemeDisplacement(13, val, 16);
		val[0] = 0.75f;
		val[1] = 0.15f;
		val[2] = 0.3f;
		val[3] = -0.3f;
		val[4] = 0.04f;
		val[5] = -0.5f;
		val[6] = -0.3f;
		val[7] = -0.3f;
		val[8] = -0.8f;
		val[9] = 0.1f;
		val[10] = -0.37f;
		val[11] = 0.3f;
		val[12] = 0f;
		val[13] = 0.45f;
		val[14] = 0.45f;
		val[15] = 0.3f;
		for (i = 0; i < 16; i++) {
			val[i] = val[i] / 2f;
		}
		expressionCoords.setVisemeDisplacement(14, val, 16);

		pupil_dilation = new float[2];
		eye_yaw = new float[2];
		eye_pitch = new float[2];
		left_pupil_center = new float[2];
		right_pupil_center = new float[2];

		left_pupil_radius = pupil_dilation[0] / 20;
		left_pupil_center[0] = (2.7f + eye_yaw[0]) + (pupil_dilation[0] / 20);
		left_pupil_center[1] = (5.2f + eye_pitch[0]) + (pupil_dilation[0] / 20);
		right_pupil_radius = pupil_dilation[1] / 20;
		right_pupil_center[0] = (2.7f + eye_yaw[1]) + (pupil_dilation[1] / 20);
		right_pupil_center[1] = (5.2f + eye_pitch[1]) + (pupil_dilation[1] / 20);

		headColour = new int[3];
		headColour[0] = 231;
		headColour[1] = 213;
		headColour[2] = 183;

		head_bob = new float[2];
		head_bob[0] = 0;
		head_bob[1] = 0;

		eyelid = new float[4];
		eyebrow_raise = new float[6];
		eyebrow_squeeze = new float[2];
		mouth_vertical = new float[8];
		mouth_horizontal = new float[2];
		corner_mouth_vertical = new float[2];

		eyebrowLeftExpression = new float[6];
		eyebrowRightExpression = new float[6];
		mouthExpression = new float[16];
		eyelidTopLeftExpression = new float[12];
		eyelidTopRightExpression = new float[12];
		eyelidBottomLeftExpression = new float[12];
		eyelidBottomRightExpression = new float[12];
		pupilLeftExpression = new float[6];
		pupilRightExpression = new float[6];
		browExpression = new float[10];
		visemeDispl = new float[16];

		eyebrow_stroke = new BasicStroke(eyebrow_width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		mouth_stroke = new BasicStroke(mouth_width / 2f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
		lip_stroke = new BasicStroke(mouth_width, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
	}

	/**
	 *  Animate the MPEG-4 face according to an MPEG-4 FAP
	 *
	 *@param  frame  The FAP to animate the face with
	 */
	public final void animateFace(FAPData frame) {
		int i;
		int expression1;
		int expression2;
		int intensity1;
		int intensity2;
		float tounge;

		headColour[0] = 231;
		headColour[1] = 213;
		headColour[2] = 183;

		float irisD;
		irisD = FAPU.IrisD();

		//If there is an expression to render
		if (frame.expressionMaskSet()) {

			//Get expression values
			expression1 = frame.getExpression1();
			expression2 = frame.getExpression2();
			intensity1 = frame.getExpressionIntensity1();
			intensity2 = frame.getExpressionIntensity2();

			//Set eye closure for disgust
			if (expression1 == MPEG4ExpressionCoordinates.DISGUST) {
				irisD = ((irisD * 1024f) - (1.8f * ((float) intensity1 / 63f))) / 1024f;
			}
			if (expression2 == MPEG4ExpressionCoordinates.DISGUST) {
				irisD = ((irisD * 1024f) - (1.8f * ((float) intensity1 / 63f))) / 1024f;
			}

			//Set colour change for anger and fear
			if (expression1 == MPEG4ExpressionCoordinates.ANGER) {
				headColour[0] = (int) (((float) headColour[0]) - ((((float) intensity1) / 63f) * 18f));
				headColour[1] = (int) (((float) headColour[1]) - ((((float) intensity1) / 63f) * 140f));
				headColour[2] = (int) (((float) headColour[2]) - ((((float) intensity1) / 63f) * 145f));
			}
			if (expression2 == MPEG4ExpressionCoordinates.ANGER) {
				headColour[0] = (int) (((float) headColour[0]) - ((((float) intensity2) / 63f) * 18f));
				headColour[1] = (int) (((float) headColour[1]) - ((((float) intensity2) / 63f) * 140f));
				headColour[2] = (int) (((float) headColour[2]) - ((((float) intensity2) / 63f) * 145f));
			}
			if (expression1 == MPEG4ExpressionCoordinates.FEAR) {
				headColour[0] = (int) (((float) headColour[0]) + ((((float) intensity1) / 63f) * 6f));
				headColour[1] = (int) (((float) headColour[1]) + ((((float) intensity1) / 63f) * 11f));
				headColour[2] = (int) (((float) headColour[2]) + ((((float) intensity1) / 63f) * 20f));
			}
			if (expression2 == MPEG4ExpressionCoordinates.FEAR) {
				headColour[0] = (int) (((float) headColour[0]) + ((((float) intensity2) / 63f) * 6f));
				headColour[1] = (int) (((float) headColour[1]) + ((((float) intensity2) / 63f) * 11f));
				headColour[2] = (int) (((float) headColour[2]) + ((((float) intensity2) / 63f) * 20f));
			}

			//If init face set
			if (frame.getExpressionInitFace() == 1) {
				expressionCoords.setInitFace(expression1, expression2, intensity1, intensity2);
			}
		} else {
			expression1 = expressionCoords.getInitFace();
			expression2 = 0;
			intensity1 = 63;
			intensity2 = 0;
		}

		//Look up table for feature point displacements from neutral position
		eyebrowLeftExpression = expressionCoords.getCoords(expression1, expression2, intensity1, intensity2, MPEG4ExpressionCoordinates.EYEBROW_LEFT, 6);
		eyebrowRightExpression = expressionCoords.getCoords(expression1, expression2, intensity1, intensity2, MPEG4ExpressionCoordinates.EYEBROW_RIGHT, 6);
		mouthExpression = expressionCoords.getCoords(expression1, expression2, intensity1, intensity2, MPEG4ExpressionCoordinates.MOUTH, 16);
		pupilLeftExpression = expressionCoords.getCoords(expression1, expression2, intensity1, intensity2, MPEG4ExpressionCoordinates.PUPIL_LEFT, 6);
		pupilRightExpression = expressionCoords.getCoords(expression1, expression2, intensity1, intensity2, MPEG4ExpressionCoordinates.PUPIL_RIGHT, 6);
		eyelidTopLeftExpression = expressionCoords.getCoords(expression1, expression2, intensity1, intensity2, MPEG4ExpressionCoordinates.EYELID_TOP_LEFT, 12);
		eyelidTopRightExpression = expressionCoords.getCoords(expression1, expression2, intensity1, intensity2, MPEG4ExpressionCoordinates.EYELID_TOP_RIGHT, 12);
		eyelidBottomLeftExpression = expressionCoords.getCoords(expression1, expression2, intensity1, intensity2, MPEG4ExpressionCoordinates.EYELID_BOTTOM_LEFT, 12);
		eyelidBottomRightExpression = expressionCoords.getCoords(expression1, expression2, intensity1, intensity2, MPEG4ExpressionCoordinates.EYELID_BOTTOM_RIGHT, 12);
		browExpression = expressionCoords.getCoords(expression1, expression2, intensity1, intensity2, MPEG4ExpressionCoordinates.BROW, 10);

		//Check low level bitmasks for animation of face
		if (frame.lowLevelMaskSet(49)) {
			if (((frame.getLowLevelValue(49) * FAPU.Au()) >= ((Math.PI) / 4f)) &&
					((frame.getLowLevelValue(49) * FAPU.Au()) <= ((3f * Math.PI) / 4f))) {
				// Facing left
				facing = LEFT;
			} else if (((frame.getLowLevelValue(49) * FAPU.Au()) >= ((3f * Math.PI) / 4f)) &&
					((frame.getLowLevelValue(49) * FAPU.Au()) <= ((5f * Math.PI) / 4f))) {
				// Facing backwards
				facing = BACKWARDS;
			} else if (((frame.getLowLevelValue(49) * FAPU.Au()) >= ((5f * Math.PI) / 4f)) &&
					((frame.getLowLevelValue(49) * FAPU.Au()) <= ((7f * Math.PI) / 4f))) {
				// Facing right
				facing = RIGHT;
			} else if (((frame.getLowLevelValue(49) * FAPU.Au()) <= ((-1f * Math.PI) / 4f)) &&
					((frame.getLowLevelValue(49) * FAPU.Au()) >= ((-3f * Math.PI) / 4f))) {
				// Facing right
				facing = RIGHT;
			} else if (((frame.getLowLevelValue(49) * FAPU.Au()) <= ((-3f * Math.PI) / 4f)) &&
					((frame.getLowLevelValue(49) * FAPU.Au()) >= ((-5f * Math.PI) / 4f))) {
				// Facing backwards
				facing = BACKWARDS;
			} else if (((frame.getLowLevelValue(49) * FAPU.Au()) <= ((-5f * Math.PI) / 4f)) &&
					((frame.getLowLevelValue(49) * FAPU.Au()) >= ((-7f * Math.PI) / 4f))) {
				// Facing left
				facing = LEFT;
			} else {
				// Facing forward
				facing = FORWARDS;
			}
		}
		if (frame.lowLevelMaskSet(50)) {
			theta = frame.getLowLevelValue(50) * FAPU.Au();
		} else {
			theta = 0.0;
		}
		if (frame.lowLevelMaskSet(48)) {
			theta2 = frame.getLowLevelValue(48) * FAPU.Au();
		} else {
			theta2 = 0.0;
		}
		for (i = 0; i < 4; i++) {
			if (frame.lowLevelMaskSet(i + 19)) {
				eyelid[i] = frame.getLowLevelValue(i + 19) * irisD;
			} else {
				eyelid[i] = 0.0f;
			}
		}
		for (i = 0; i < 6; i++) {
			if (frame.lowLevelMaskSet(i + 31)) {
				eyebrow_raise[i] = frame.getLowLevelValue(i + 31) * FAPU.Ens();
			} else {
				eyebrow_raise[i] = 0;
			}
		}
		for (i = 0; i < 2; i++) {
			if (frame.lowLevelMaskSet(i + 37)) {
				eyebrow_squeeze[i] = frame.getLowLevelValue(i + 37) * FAPU.Es();
			} else {
				eyebrow_squeeze[i] = 0;
			}
		}
		for (i = 0; i < 2; i++) {
			if (frame.lowLevelMaskSet(i + 4)) {
				mouth_vertical[i] = frame.getLowLevelValue(i + 4) * FAPU.Mns();
			} else {
				mouth_vertical[i] = 0;
			}
			if (frame.lowLevelMaskSet(i + 51)) {
				if (frame.getLowLevelValue(i + 51) * FAPU.Mns() > mouth_vertical[i]) {
					mouth_vertical[i] = frame.getLowLevelValue(i + 51) * FAPU.Mns();
				}
			}
		}
		for (i = 2; i < 8; i++) {
			if (frame.lowLevelMaskSet(i + 6)) {
				mouth_vertical[i] = frame.getLowLevelValue(i + 6) * FAPU.Mns();
			} else {
				mouth_vertical[i] = 0;
			}
			if (frame.lowLevelMaskSet(i + 53)) {
				if (frame.getLowLevelValue(i + 53) * FAPU.Mns() > mouth_vertical[i]) {
					mouth_vertical[i] = frame.getLowLevelValue(i + 53) * FAPU.Mns();
				}
			}
			if ((i == 6) || (i == 7)) {
				if (frame.lowLevelMaskSet(i + 6)) {
					corner_mouth_vertical[i - 6] = frame.getLowLevelValue(i + 6) * FAPU.Mns();
				} else if (frame.lowLevelMaskSet(i + 53)) {
					corner_mouth_vertical[i - 6] = frame.getLowLevelValue(i + 53) * FAPU.Mns();
				} else {
					corner_mouth_vertical[i - 6] = 0;
				}
			}
		}
		for (i = 0; i < 2; i++) {
			if (frame.lowLevelMaskSet(i + 6)) {
				mouth_horizontal[i] = frame.getLowLevelValue(i + 6) * FAPU.Mw();
			} else {
				mouth_horizontal[i] = 0;
			}
			if (frame.lowLevelMaskSet(i + 53)) {
				if (frame.getLowLevelValue(i + 53) * FAPU.Mns() > mouth_horizontal[i]) {
					mouth_horizontal[i] = frame.getLowLevelValue(i + 53) * FAPU.Mns();
				}
			}
			visemeDraw = false;
		}

		//If viseme bit mask is set
		if (frame.visemeMaskSet()) {
			visemeDraw = true;
			viseme1 = frame.getViseme1();
			viseme2 = frame.getViseme2();
			visemeBlend = frame.getVisemeBlend();

			//Look up table fopr mouth displacements based on expressions
			visemeDispl = expressionCoords.getViseme(expression1, expression2, intensity1, intensity2, viseme1, viseme2, visemeBlend, 16);

			//If talking (visemes) animate a head bob
			if ((viseme1 != 0) && (viseme2 != 0)) {
				if (head_bob[0] < 10) {
					head_bob[0]++;
					head_bob[1] = head_bob[1] + 0.005f;
					//((float)Math.random()/80f);
				} else {
					if (head_bob[0] < 15) {
						head_bob[0]++;
					} else {
						head_bob[1] = head_bob[1] - 0.005f;
					}
					//((float)Math.random()/80f);
					if (head_bob[1] < 0) {
						head_bob[1] = 0;
						head_bob[0] = 0;
					}
				}
			} else {
				if (head_bob[1] > 0) {
					head_bob[1] = head_bob[1] - 0.005f;
					//((float)Math.random()/80f);
				} else {
					head_bob[0] = 0;
				}
				head_bob[1] = 0;
			}
		} else {
			head_bob[0] = 0;
			head_bob[1] = 0;
			visemeDraw = false;
		}

		//Check if low level bit masks are set
		for (i = 0; i < 2; i++) {
			if (frame.lowLevelMaskSet(i + 29)) {
				pupil_dilation[i] = frame.getLowLevelValue(i + 29) * irisD;
			} else {
				pupil_dilation[i] = 0.0f;
			}
		}
		for (i = 0; i < 2; i++) {
			if (frame.lowLevelMaskSet(i + 23)) {
				eye_yaw[i] = frame.getLowLevelValue(i + 23) * FAPU.Au();
				if (((eye_yaw[i] >= 0) && (eye_yaw[i] <= PI.floatValue() / 2f)) || ((eye_yaw[i] <= 0) && (eye_yaw[i] >= (-3f * PI.floatValue()) / 2f))) {
					eye_yaw[i] = (eye_yaw[i] / (PI.floatValue() / 2f)) * 0.9f;
				} else if (((eye_yaw[i] >= (3f * PI.floatValue()) / 2f) && (eye_yaw[i] <= PI.floatValue() * 2f)) || ((eye_yaw[i] <= 0) && (eye_yaw[i] >= PI.floatValue() / -2f))) {
					eye_yaw[i] = (eye_yaw[i] - ((3f * PI.floatValue()) / 2f)) - (PI.floatValue() / 2f);
					eye_yaw[i] = (eye_yaw[i] / (PI.floatValue() / 2f)) * 0.9f;
				} else if ((eye_yaw[i] > 4f * PI.floatValue()) || (eye_yaw[i] < -4f * PI.floatValue())) {
					eye_yaw[i] = 0f;
				} else {
					eye_yaw[i] = 1.8f;
				}
			} else {
				eye_yaw[i] = 0.0f;
			}
		}
		for (i = 0; i < 2; i++) {
			if (frame.lowLevelMaskSet(i + 25)) {
				eye_pitch[i] = frame.getLowLevelValue(i + 25) * FAPU.Au();
			} else {
				eye_pitch[i] = 0f;
			}
			eye_pitch[i] = (eye_pitch[i] + (float) theta2) / 2f;
			if (((eye_pitch[i] >= 0) && (eye_pitch[i] <= PI.floatValue() / 2f)) || ((eye_pitch[i] <= 0) && (eye_pitch[i] >= (-3f * PI.floatValue()) / 2f))) {
				eye_pitch[i] = (eye_pitch[i] / (PI.floatValue() / 2f)) * 1.2f;
			} else if (((eye_pitch[i] >= (3f * PI.floatValue()) / 2f) && (eye_pitch[i] <= PI.floatValue() * 2f)) || ((eye_pitch[i] <= 0) && (eye_pitch[i] >= PI.floatValue() / -2f))) {
				eye_pitch[i] = (eye_pitch[i] - ((3f * PI.floatValue()) / 2f)) - (PI.floatValue() / 2f);
				eye_pitch[i] = (eye_pitch[i] / (PI.floatValue() / 2f)) * 1.2f;
			} else if ((eye_pitch[i] > 2f * PI.floatValue()) || (eye_pitch[i] < -2f * PI.floatValue())) {
				eye_pitch[i] = 0f;
			} else {
				eye_pitch[i] = 2.4f;
			}
		}

		brow.reset();
		brow.moveTo(browExpression[0], browExpression[1]);
		brow.lineTo(browExpression[2], browExpression[3]);
		brow.lineTo(browExpression[4], browExpression[5]);
		brow.lineTo(browExpression[6], browExpression[7]);
		brow.lineTo(browExpression[8], browExpression[9]);
		brow.closePath();

		left_brow.reset();
		left_brow.moveTo(browExpression[0] + 3f, browExpression[1]);
		left_brow.lineTo(browExpression[6] + 3f, browExpression[7]);
		left_brow.lineTo(browExpression[8] + 3f, browExpression[9]);
		left_brow.closePath();

		right_brow.reset();
		right_brow.moveTo(browExpression[6] - 3f, browExpression[7]);
		right_brow.lineTo(browExpression[2] - 3f, browExpression[3]);
		right_brow.lineTo(browExpression[4] - 3f, browExpression[5]);
		right_brow.closePath();

		left_eyelid.reset();
		left_eyelid.moveTo(eyelidTopLeftExpression[0], eyelidTopLeftExpression[1] + eyelid[0]);
		left_eyelid.lineTo(eyelidTopLeftExpression[2], eyelidTopLeftExpression[3]);
		left_eyelid.lineTo(eyelidTopLeftExpression[4], eyelidTopLeftExpression[5]);
		left_eyelid.lineTo(eyelidTopLeftExpression[6], eyelidTopLeftExpression[7] + eyelid[0]);
		left_eyelid.quadTo(eyelidTopLeftExpression[8], eyelidTopLeftExpression[9] + eyelid[0],
				eyelidTopLeftExpression[10], eyelidTopLeftExpression[11] + eyelid[0]);
		left_eyelid.closePath();

		right_eyelid.reset();
		right_eyelid.moveTo(eyelidTopRightExpression[0], eyelidTopRightExpression[1] + eyelid[1]);
		right_eyelid.lineTo(eyelidTopRightExpression[2], eyelidTopRightExpression[3]);
		right_eyelid.lineTo(eyelidTopRightExpression[4], eyelidTopRightExpression[5]);
		right_eyelid.lineTo(eyelidTopRightExpression[6], eyelidTopRightExpression[7] + eyelid[1]);
		right_eyelid.quadTo(eyelidTopRightExpression[8], eyelidTopRightExpression[9] + eyelid[1],
				eyelidTopRightExpression[10], eyelidTopRightExpression[11] + eyelid[1]);
		right_eyelid.closePath();

		left_bottom_eyelid.reset();
		left_bottom_eyelid.moveTo(eyelidBottomLeftExpression[0], eyelidBottomLeftExpression[1] - eyelid[2]);
		left_bottom_eyelid.lineTo(eyelidBottomLeftExpression[2], eyelidBottomLeftExpression[3]);
		left_bottom_eyelid.lineTo(eyelidBottomLeftExpression[4], eyelidBottomLeftExpression[5]);
		left_bottom_eyelid.lineTo(eyelidBottomLeftExpression[6], eyelidBottomLeftExpression[7] - eyelid[2]);
		left_bottom_eyelid.quadTo(eyelidBottomLeftExpression[8], eyelidBottomLeftExpression[9] - eyelid[2],
				eyelidBottomLeftExpression[10], eyelidBottomLeftExpression[11] - eyelid[2]);
		left_bottom_eyelid.closePath();

		right_bottom_eyelid.reset();
		right_bottom_eyelid.moveTo(eyelidBottomRightExpression[0], eyelidBottomRightExpression[1] - eyelid[3]);
		right_bottom_eyelid.lineTo(eyelidBottomRightExpression[2], eyelidBottomRightExpression[3]);
		right_bottom_eyelid.lineTo(eyelidBottomRightExpression[4], eyelidBottomRightExpression[5]);
		right_bottom_eyelid.lineTo(eyelidBottomRightExpression[6], eyelidBottomRightExpression[7] - eyelid[3]);
		right_bottom_eyelid.quadTo(eyelidBottomRightExpression[8], eyelidBottomRightExpression[9] - eyelid[3],
				eyelidBottomRightExpression[10], eyelidBottomRightExpression[11] - eyelid[3]);
		right_bottom_eyelid.closePath();

		face_left_eyelid.reset();
		face_left_eyelid.moveTo(eyelidTopLeftExpression[0] + 3f, eyelidTopLeftExpression[1] + eyelid[0]);
		face_left_eyelid.lineTo(eyelidTopLeftExpression[2] + 3f, eyelidTopLeftExpression[3]);
		face_left_eyelid.lineTo(eyelidTopLeftExpression[4] + 3f, eyelidTopLeftExpression[5]);
		face_left_eyelid.lineTo(eyelidTopLeftExpression[6] + 3f, eyelidTopLeftExpression[7] + eyelid[0]);
		face_left_eyelid.quadTo(eyelidTopLeftExpression[8] + 3f, eyelidTopLeftExpression[9] + eyelid[0],
				eyelidTopLeftExpression[10] + 3f, eyelidTopLeftExpression[11] + eyelid[0]);
		face_left_eyelid.closePath();

		face_right_eyelid.reset();
		face_right_eyelid.moveTo(eyelidTopRightExpression[0] - 3f, eyelidTopRightExpression[1] + eyelid[1]);
		face_right_eyelid.lineTo(eyelidTopRightExpression[2] - 3f, eyelidTopRightExpression[3]);
		face_right_eyelid.lineTo(eyelidTopRightExpression[4] - 3f, eyelidTopRightExpression[5]);
		face_right_eyelid.lineTo(eyelidTopRightExpression[6] - 3f, eyelidTopRightExpression[7] + eyelid[1]);
		face_right_eyelid.quadTo(eyelidTopRightExpression[8] - 3f, eyelidTopRightExpression[9] + eyelid[1],
				eyelidTopRightExpression[10] - 3f, eyelidTopRightExpression[11] + eyelid[1]);
		face_right_eyelid.closePath();

		face_left_bottom_eyelid.reset();
		face_left_bottom_eyelid.moveTo(eyelidBottomLeftExpression[0] + 3f, eyelidBottomLeftExpression[1] - eyelid[2]);
		face_left_bottom_eyelid.lineTo(eyelidBottomLeftExpression[2] + 3f, eyelidBottomLeftExpression[3]);
		face_left_bottom_eyelid.lineTo(eyelidBottomLeftExpression[4] + 3f, eyelidBottomLeftExpression[5]);
		face_left_bottom_eyelid.lineTo(eyelidBottomLeftExpression[6] + 3f, eyelidBottomLeftExpression[7] - eyelid[2]);
		face_left_bottom_eyelid.quadTo(eyelidBottomLeftExpression[8] + 3f, eyelidBottomLeftExpression[9] - eyelid[2],
				eyelidBottomLeftExpression[10] + 3f, eyelidBottomLeftExpression[11] - eyelid[2]);
		face_left_bottom_eyelid.closePath();

		face_right_bottom_eyelid.reset();
		face_right_bottom_eyelid.moveTo(eyelidBottomRightExpression[0] - 3f, eyelidBottomRightExpression[1] - eyelid[3]);
		face_right_bottom_eyelid.lineTo(eyelidBottomRightExpression[2] - 3f, eyelidBottomRightExpression[3]);
		face_right_bottom_eyelid.lineTo(eyelidBottomRightExpression[4] - 3f, eyelidBottomRightExpression[5]);
		face_right_bottom_eyelid.lineTo(eyelidBottomRightExpression[6] - 3f, eyelidBottomRightExpression[7] - eyelid[3]);
		face_right_bottom_eyelid.quadTo(eyelidBottomRightExpression[8] - 3f, eyelidBottomRightExpression[9] - eyelid[3],
				eyelidBottomRightExpression[10] - 3f, eyelidBottomRightExpression[11] - eyelid[3]);
		face_right_bottom_eyelid.closePath();

		left_pupil.setFrame((pupilLeftExpression[0] + eye_yaw[0]) - (pupil_dilation[0] / 20),
				(pupilLeftExpression[1] + eye_pitch[0]) - (pupil_dilation[0] / 20),
				(pupilLeftExpression[2]) + (pupil_dilation[0] / 10),
				(pupilLeftExpression[2]) + (pupil_dilation[0] / 10));
		left_pupil_center[0] = (pupilLeftExpression[3] + eye_yaw[0]) + (pupil_dilation[0] / 20);
		left_pupil_center[1] = (pupilLeftExpression[4] + eye_pitch[0]) + (pupil_dilation[0] / 20);
		left_pupil_radius = pupilLeftExpression[5] + (pupil_dilation[0] / 20);

		right_pupil.setFrame((pupilRightExpression[0] + eye_yaw[1]) - (pupil_dilation[1] / 20),
				(pupilRightExpression[1] + eye_pitch[1]) - (pupil_dilation[1] / 20),
				(pupilRightExpression[2]) + (pupil_dilation[1] / 10),
				(pupilRightExpression[2]) + (pupil_dilation[1] / 10));
		right_pupil_center[0] = (pupilRightExpression[3] + eye_yaw[1]) + (pupil_dilation[1] / 20);
		right_pupil_center[1] = (pupilRightExpression[4] + eye_pitch[1]) + (pupil_dilation[1] / 20);
		right_pupil_radius = pupilRightExpression[5] + (pupil_dilation[1] / 20);

		face_left_pupil.setFrame((pupilLeftExpression[0] + eye_yaw[0]) - (pupil_dilation[0] / 20) + 3.5f,
				(pupilLeftExpression[1] + eye_pitch[0]) - (pupil_dilation[0] / 20),
				(pupilLeftExpression[2]) + (pupil_dilation[0] / 10),
				(pupilLeftExpression[2]) + (pupil_dilation[0] / 10));

		face_right_pupil.setFrame((pupilRightExpression[0] + eye_yaw[1]) - (pupil_dilation[1] / 20) - 3.5f,
				(pupilLeftExpression[1] + eye_pitch[1]) - (pupil_dilation[1] / 20),
				(pupilLeftExpression[2]) + (pupil_dilation[1] / 10),
				(pupilLeftExpression[2]) + (pupil_dilation[1] / 10));

		left_eyebrow.setCurve(
				eyebrowLeftExpression[0] + eyebrow_squeeze[0], eyebrowLeftExpression[1] - eyebrow_raise[4],
				eyebrowLeftExpression[2] + eyebrow_squeeze[0], eyebrowLeftExpression[3] - eyebrow_raise[2],
				eyebrowLeftExpression[4] + eyebrow_squeeze[0], eyebrowLeftExpression[5] - eyebrow_raise[0]
				);

		right_eyebrow.setCurve(
				eyebrowRightExpression[0] + eyebrow_squeeze[0], eyebrowRightExpression[1] - eyebrow_raise[1],
				eyebrowRightExpression[2] - eyebrow_squeeze[1], eyebrowRightExpression[3] - eyebrow_raise[3],
				eyebrowRightExpression[4] - eyebrow_squeeze[1], eyebrowRightExpression[5] - eyebrow_raise[5]
				);

		face_left_eyebrow.setCurve(
				eyebrowLeftExpression[0] + eyebrow_squeeze[0] + 3, eyebrowLeftExpression[1] - eyebrow_raise[4],
				eyebrowLeftExpression[2] + eyebrow_squeeze[0] + 3, eyebrowLeftExpression[3] - eyebrow_raise[2],
				eyebrowLeftExpression[4] + eyebrow_squeeze[0] + 3, eyebrowLeftExpression[5] - eyebrow_raise[0]
				);

		face_right_eyebrow.setCurve(
				eyebrowRightExpression[0] - eyebrow_squeeze[1] - 3, eyebrowRightExpression[1] - eyebrow_raise[1],
				eyebrowRightExpression[2] - eyebrow_squeeze[1] - 3, eyebrowRightExpression[3] - eyebrow_raise[3],
				eyebrowRightExpression[4] - eyebrow_squeeze[1] - 3, eyebrowRightExpression[5] - eyebrow_raise[5]
				);

		if (visemeDraw == true) {
			float a;
			float b;

			a = visemeBlend / 63f;
			b = 1f - (visemeBlend / 63f);

			tounge = a * tounge_poke[viseme1] + b * tounge_poke[viseme2];

			mouth.reset();

			mouth.moveTo(visemeDispl[0], visemeDispl[1] - corner_mouth_vertical[0]);
			mouth.lineTo(visemeDispl[2], visemeDispl[3]);
			mouth.lineTo(visemeDispl[4], visemeDispl[5]);
			mouth.lineTo(visemeDispl[6], visemeDispl[7]);
			mouth.lineTo(visemeDispl[8], visemeDispl[9] - corner_mouth_vertical[1]);
			mouth.lineTo(visemeDispl[10], visemeDispl[11]);
			mouth.lineTo(visemeDispl[12], visemeDispl[13]);
			mouth.lineTo(visemeDispl[14], visemeDispl[15]);
			mouth.lineTo(visemeDispl[0], visemeDispl[1] - corner_mouth_vertical[0]);

			left_mouth.reset();
			left_mouth.moveTo(visemeDispl[0] + 2, visemeDispl[1] - corner_mouth_vertical[0]);
			left_mouth.lineTo(visemeDispl[4] + 2, visemeDispl[5]);
			left_mouth.lineTo(6.5f, 7.4f);
			left_mouth.lineTo(visemeDispl[12] + 2, visemeDispl[13]);
			left_mouth.lineTo(visemeDispl[0] + 2, visemeDispl[1] - corner_mouth_vertical[0]);

			right_mouth.reset();
			right_mouth.moveTo(visemeDispl[8] - 2f, visemeDispl[9] - corner_mouth_vertical[1]);
			right_mouth.lineTo(visemeDispl[12] - 2f, visemeDispl[13]);
			right_mouth.lineTo(1.4f, 7.4f);
			right_mouth.lineTo(visemeDispl[4] - 2f, visemeDispl[5]);
			right_mouth.lineTo(visemeDispl[8] - 2f, visemeDispl[9] - corner_mouth_vertical[1]);

			tounge_semi = new QuadCurve2D.Float(2.84f, 8.73f, 3.84f, 8.73f - (tounge * 3.63f), 4.84f, 8.73f);
		} else {
			mouth.reset();
			mouth.moveTo(mouthExpression[0] - mouth_horizontal[0], mouthExpression[1] - mouth_vertical[6] - corner_mouth_vertical[0]);
			mouth.lineTo(mouthExpression[2], mouthExpression[3] + mouth_vertical[2]);
			mouth.lineTo(mouthExpression[4], mouthExpression[5] + mouth_vertical[0]);
			mouth.lineTo(mouthExpression[6], mouthExpression[7] + mouth_vertical[3]);
			mouth.lineTo(mouthExpression[8] + mouth_horizontal[1], mouthExpression[9] - mouth_vertical[7] - corner_mouth_vertical[1]);
			mouth.lineTo(mouthExpression[10], mouthExpression[11] - mouth_vertical[5]);
			mouth.lineTo(mouthExpression[12], mouthExpression[13] - mouth_vertical[1]);
			mouth.lineTo(mouthExpression[14], mouthExpression[15] - mouth_vertical[4]);
			mouth.closePath();

			left_mouth.reset();
			left_mouth.moveTo(mouthExpression[0] - mouth_horizontal[0] + 2f, mouthExpression[1] - mouth_vertical[6] - corner_mouth_vertical[0]);
			left_mouth.lineTo(mouthExpression[4] + 2f, mouthExpression[5] + mouth_vertical[0]);
			left_mouth.lineTo(6.5f, 7.4f);
			left_mouth.lineTo(mouthExpression[12] + 2f, mouthExpression[13] - mouth_vertical[1]);
			left_mouth.closePath();

			right_mouth.reset();
			right_mouth.moveTo(mouthExpression[8] + mouth_horizontal[1] - 2f, mouthExpression[9] - mouth_vertical[7] - corner_mouth_vertical[1]);
			right_mouth.lineTo(mouthExpression[12] - 2f, mouthExpression[13] - mouth_vertical[1]);
			right_mouth.lineTo(1.4f, 7.4f);
			right_mouth.lineTo(mouthExpression[4] - 2f, mouthExpression[5] + mouth_vertical[0]);
			right_mouth.closePath();
		}
	}


	/**
	 *  Draw the face based on the current geometric objects
	 *
	 *@param  g2d     The Graphics2D object for rendering
	 *@param  width   The width of the face for drawing
	 *@param  height  The height of the face for drawing
	 */
	public final void drawFace(Graphics2D g2d, int width, int height) {
		float scaleFactorX;
		float scaleFactorY;
		int i;
		Color head_colour = new Color(headColour[0], headColour[1], headColour[2]);

		//Scale the face according to the height and width
		scaleFactorX = (float) width / 8.55f;
		scaleFactorY = (float) height / 9.7f;

		if (scaleFactorX < scaleFactorY) {
			g2d.translate(0.0f, (((float) height / 2.0f) - (0.5f * scaleFactorX)) - (scaleFactorX * 4.850f));
			g2d.scale(scaleFactorX, scaleFactorX);
		} else {
			g2d.translate(((float) width / 2.0f) - (scaleFactorY * 4.275f), 0.0f);
			g2d.scale(scaleFactorY, scaleFactorY);
		}

		//Begin painting geometric objects
		g2d.setPaint(neck_colour);
		g2d.fill(neck);

		g2d.translate(0.4f, 1.1f - head_bob[1]);
		if (facing == FORWARDS) {
			g2d.rotate(theta, 3.9, 6.2);
		} else if (facing == BACKWARDS) {
			g2d.rotate(-1 * theta, 3.9, 6.2);
		} else if (facing == LEFT) {
			g2d.rotate(theta2, 3.9, 6.2);
		} else if (facing == RIGHT) {
			g2d.rotate(-1 * theta2, 3.9, 6.2);
		}
		g2d.setPaint(head_colour);
		g2d.fill(head);
		if (facing == FORWARDS) {
			g2d.setPaint(hair_colour1);
			g2d.fill(hair[0]);
			g2d.setPaint(hair_colour2);
			g2d.fill(hair[1]);
			g2d.setPaint(hair_colour3);
			g2d.fill(hair[2]);
		} else if (facing == BACKWARDS) {
			g2d.setPaint(hair_colour3);
			g2d.fill(hair[0]);
			g2d.setPaint(hair_colour2);
			g2d.fill(hair[1]);
			g2d.setPaint(hair_colour1);
			g2d.fill(hair_back[2]);
		} else if (facing == LEFT) {
			g2d.setPaint(hair_colour3);
			g2d.fill(hair_left[0]);
			g2d.setPaint(hair_colour2);
			g2d.fill(hair_left[1]);
			g2d.setPaint(hair_colour1);
			g2d.fill(hair_left[2]);
		} else if (facing == RIGHT) {
			g2d.setPaint(hair_colour3);
			g2d.fill(hair_right[0]);
			g2d.setPaint(hair_colour2);
			g2d.fill(hair_right[1]);
			g2d.setPaint(hair_colour1);
			g2d.fill(hair_right[2]);
		}

		g2d.setPaint(white);
		if (facing == FORWARDS) {
			g2d.fill(left_eye);
			g2d.fill(right_eye);
			g2d.setPaint(head_colour);
			g2d.fill(brow);
			g2d.setPaint(black);
			g2d.setClip(left_eye);
			g2d.fill(left_pupil);
			g2d.setClip(null);
			g2d.setClip(right_eye);
			g2d.fill(right_pupil);
			g2d.setClip(null);
			g2d.setPaint(head_colour);
			g2d.fill(left_eyelid);
			g2d.fill(right_eyelid);
			g2d.fill(left_bottom_eyelid);
			g2d.fill(right_bottom_eyelid);
			g2d.setPaint(black);
			g2d.setStroke(eyebrow_stroke);
			g2d.draw(left_eyebrow);
			g2d.draw(right_eyebrow);
			g2d.setStroke(mouth_stroke);
			g2d.setColor(black);
			g2d.setClip(mouth);
			g2d.fill(black_mouth);
			g2d.setColor(tounge_colour);
			g2d.fill(tounge_semi);
			g2d.setColor(gum_colour);
			g2d.fill(gums);
			g2d.setColor(white);
			g2d.fill(teeth);
			g2d.setColor(black);
			for (i = 0; i < 15; i++) {
				g2d.draw(teeth_lines[i]);
			}
			g2d.draw(teeth);
			g2d.setClip(null);
			g2d.setPaint(mouth_colour);
			g2d.setStroke(lip_stroke);
			g2d.draw(mouth);
		} else if (facing == LEFT) {
			g2d.fill(face_left_eye);
			g2d.setPaint(head_colour);
			g2d.fill(left_brow);
			g2d.setPaint(black);
			g2d.setClip(face_left_eye);
			g2d.fill(face_left_pupil);
			g2d.setClip(null);
			g2d.setPaint(head_colour);
			g2d.fill(face_left_eyelid);
			g2d.fill(face_left_bottom_eyelid);
			g2d.setPaint(black);
			g2d.setStroke(eyebrow_stroke);
			g2d.draw(face_left_eyebrow);
			g2d.setStroke(mouth_stroke);
			g2d.setColor(black);
			g2d.fill(left_mouth);
			g2d.setPaint(mouth_colour);
			g2d.setStroke(lip_stroke);
			g2d.draw(left_mouth);
		} else if (facing == RIGHT) {
			g2d.fill(face_right_eye);
			g2d.setPaint(head_colour);
			g2d.fill(right_brow);
			g2d.setPaint(black);
			g2d.setClip(face_right_eye);
			g2d.fill(face_right_pupil);
			g2d.setClip(null);
			g2d.setPaint(head_colour);
			g2d.fill(face_right_eyelid);
			g2d.fill(face_right_bottom_eyelid);
			g2d.setPaint(black);
			g2d.setStroke(eyebrow_stroke);
			g2d.draw(face_right_eyebrow);
			g2d.setStroke(mouth_stroke);
			g2d.setColor(black);
			g2d.fill(right_mouth);
			g2d.setPaint(mouth_colour);
			g2d.setStroke(lip_stroke);
			g2d.draw(right_mouth);
		}
	}
}

