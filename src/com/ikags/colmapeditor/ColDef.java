package com.ikags.colmapeditor;

import java.awt.image.BufferedImage;

public class ColDef {

	public static int BG_R = 144;
	public static int BG_G = 144;
	public static int BG_B = 144;
	
	public static int SG_R = 160;
	public static int SG_G = 96;
	public static int SG_B = 160;
	
	public static int CG_R1 = 255;
	public static int CG_G1 = 255;
	public static int CG_B1 = 00;
	
	public static int CG_R2 = 255;
	public static int CG_G2 = 0;
	public static int CG_B2 = 0;
	
	public static String colFilepath=null;
	public static String ImageFilepath=null;
	
	public static BufferedImage mMapImage=null; 
	public static int[][] colmaplist=null;
	
	public static boolean isRunning=true;
	
	
	public static boolean isFill=false;// ÊµÏß/ÐéÏß
	
}
