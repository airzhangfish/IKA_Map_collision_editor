package com.ikags.colmapeditor;
import java.awt.Color;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ikags.util.CommonUtil;


public class ColMapViewer extends JFrame implements ActionListener {

	String mVerion="-Version 1.0.0 in 2013-12-27";
	String titlename = "Ika ��ײԤ����    "+mVerion;
	// ����
	private String aboutStr = " IKA ��ײԤ����\n  " + "Creator by airzhangfish \n " + mVerion+"\n " + " E-mail:52643971@qq.com\n http://www.ikags.com/";

	private static final long serialVersionUID = 1L;
	private JTabbedPane jtp;
	
	private JMenuBar jMenuBar1 = new JMenuBar();
	private JMenu jMenuFile = new JMenu("�ļ�");
	private JMenuItem jMenuFileLoadImage = new JMenuItem("��ȡͼƬ�ļ�");
	private JMenuItem jMenuFileLoadColfile = new JMenuItem("��ȡ��ײ�ļ�");
	private JMenuItem jMenuFileSaveColMapImage = new JMenuItem("������ײͼ");
	private JMenuItem jMenuFileExit = new JMenuItem("�˳�");

	private JMenu jMenuEdit = new JMenu("�༭");
	private JMenuItem jMenuEditRefresh = new JMenuItem("ˢ����Դ/�ؼ�����Դ");
	private JMenuItem jMenuEditIsfill = new JMenuItem("��ײ�������/�߿��л�");
	
	private JMenu jMenuHelp = new JMenu("����");
	private JMenuItem jMenuHelpAbout = new JMenuItem("����");
	private JMenuItem jMenuHelpHomepage = new JMenuItem("��������ҳ");
	MapImagePanel mapPanel = new MapImagePanel();

	public void actionPerformed(ActionEvent actionEvent) {
		Object source = actionEvent.getSource();


		if (source == jMenuFileLoadImage) {
			FileDialog xs = new FileDialog(this, "load png file", FileDialog.LOAD);
			xs.setFile("*.png*");
			xs.setVisible(true);
			String f = xs.getFile();
			String lastDir = xs.getDirectory();
			if (f != null) {
			ColDef.ImageFilepath=lastDir + f;
			loadMapImage(ColDef.ImageFilepath);
			}
		}
		
		
		if (source == jMenuFileLoadColfile) {
			FileDialog xs = new FileDialog(this, "load txt file", FileDialog.LOAD);
			xs.setFile("*.txt*");
			xs.setVisible(true);
			String f = xs.getFile();
			String lastDir = xs.getDirectory();
			if (f != null) {
				ColDef.colFilepath=lastDir + f;
				loadColMapFile(ColDef.colFilepath);
			}
		}

		if (source == jMenuEditRefresh) {
			if(ColDef.ImageFilepath!=null){
			loadMapImage(ColDef.ImageFilepath);
			}
			if(ColDef.colFilepath!=null){
			loadColMapFile(ColDef.colFilepath);
			}
		}
		
		if (source == jMenuEditIsfill) {
			ColDef.isFill=!ColDef.isFill;
		}	
		
		// ����ͼƬ�ļ�
		if (source == jMenuFileSaveColMapImage) {
			FileDialog xs = new FileDialog(this, "save PNG file", FileDialog.SAVE);
			xs.setFile("*.*");
			xs.setVisible(true);
			String f = xs.getFile();
			String lastDir = xs.getDirectory();
			if (f != null) {
				savePNG(lastDir + f);
			}
		}

		// ����
		if (source == jMenuHelpAbout) {
			JOptionPane.showMessageDialog(this, aboutStr, "����", JOptionPane.INFORMATION_MESSAGE);
		}
		// ����˳�
		if (source == jMenuFileExit) {
			System.exit(0);
		}
		// ��������ҳ
		if (source == jMenuHelpHomepage) {
			CommonUtil.browserURL("http://www.ikags.com");
		}
	}

	
	/**
	 * ��ȡͼƬ
	 * @param path
	 */
	public void loadMapImage(String path){
		try{
		ColDef.mMapImage = ImageIO.read(new File(path));
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this, "��ȡͼƬ����,��ϢΪ:\r\n"+ex.getMessage(), "����", JOptionPane.INFORMATION_MESSAGE);
			ex.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ��ײ�ļ�
	 * @param path
	 */
	public void loadColMapFile(String path){
		Vector<String> vec=null;
		try{
			vec=StringUtil.getInputstreamVector(new FileInputStream(path), "UTF-8");
		
		if(vec.size()>0){
			ColDef.colmaplist=new int[vec.size()][4];
		}
		if(ColDef.colmaplist!=null){
			for(int i=0;i<ColDef.colmaplist.length;i++){
				String data=(String) vec.elementAt(i);
				String[] strs=data.split(",");
				ColDef.colmaplist[i][0]=StringUtil.getStringToInt(strs[0]);
				ColDef.colmaplist[i][1]=StringUtil.getStringToInt(strs[1]);
				ColDef.colmaplist[i][2]=StringUtil.getStringToInt(strs[2]);
				ColDef.colmaplist[i][3]=StringUtil.getStringToInt(strs[3]);
			}	
		}
		
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this, "��ȡ�ı�����,��ϢΪ:\r\n"+ex.getMessage(), "����", JOptionPane.INFORMATION_MESSAGE);
			ex.printStackTrace();
		}
		
	}
	



	//���浥֡ͼƬ
	public void savePNG(String path) {
		try {
			if(ColDef.mMapImage==null){
				return;
			}
			
			BufferedImage bfimg = new BufferedImage(ColDef.mMapImage.getWidth(), ColDef.mMapImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
				Graphics gg = bfimg.getGraphics();
				gg.drawImage(ColDef.mMapImage, 0, 0, null);
			      if(ColDef.colmaplist!=null){
			    	 gg.setColor(new Color(ColDef.CG_R1, ColDef.CG_G1, ColDef.CG_B1));
			  		for (int i = 0; i < ColDef.colmaplist.length; i++) {
			  			int[] colints=ColDef.colmaplist[i];
			  			if(ColDef.isFill==true){
			  				gg.fillRect(colints[0], colints[1], colints[2], colints[3]);
			  			}else{
			  				gg.drawRect(colints[0],colints[1], colints[2], colints[3]);
			  			}
			  		}
			     }

				File pngfile=new File(path+".png");
			    ImageIO.write(bfimg, "png", pngfile);
				
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "����PNG����", "����", JOptionPane.ERROR_MESSAGE);
		}

	}
	

	public ColMapViewer() {

		this.setSize(800, 600); // ����Ĵ�С
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true); // ����
		this.setTitle(titlename); // ���ñ���

		enableInputMethods(true);

		jMenuFile.add(jMenuFileLoadImage);
		jMenuFile.add(jMenuFileLoadColfile);
		jMenuFile.add(jMenuFileSaveColMapImage);
		jMenuFile.add(jMenuFileExit);
		jMenuFileLoadImage.addActionListener(this);
		jMenuFileLoadColfile.addActionListener(this);
		jMenuFileSaveColMapImage.addActionListener(this);
		jMenuFileExit.addActionListener(this);

		jMenuEdit.add(jMenuEditRefresh);
		jMenuEdit.add(jMenuEditIsfill);
		jMenuEditRefresh.addActionListener(this);
		jMenuEditIsfill.addActionListener(this);	
		
		jMenuHelp.add(jMenuHelpAbout);
		jMenuHelpAbout.addActionListener(this);
		jMenuHelp.add(jMenuHelpHomepage);
		jMenuHelpHomepage.addActionListener(this);
		// �ܹ�����

		jMenuBar1.add(jMenuFile);
		jMenuBar1.add(jMenuEdit);
		jMenuBar1.add(jMenuHelp);
		this.setJMenuBar(jMenuBar1);

		Container contents = getContentPane();
		jtp = new JTabbedPane(JTabbedPane.TOP);
		jtp.addTab("��ײ��ͼ��ʾ", mapPanel);
		contents.add(jtp);
		setVisible(true);
	}

	public static void main(String args[]) {
		CommonUtil.setMySkin(2);
		new ColMapViewer();
	}
	

}
