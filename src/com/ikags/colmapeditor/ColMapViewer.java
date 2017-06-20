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
	String titlename = "Ika 碰撞预览器    "+mVerion;
	// 关于
	private String aboutStr = " IKA 碰撞预览器\n  " + "Creator by airzhangfish \n " + mVerion+"\n " + " E-mail:52643971@qq.com\n http://www.ikags.com/";

	private static final long serialVersionUID = 1L;
	private JTabbedPane jtp;
	
	private JMenuBar jMenuBar1 = new JMenuBar();
	private JMenu jMenuFile = new JMenu("文件");
	private JMenuItem jMenuFileLoadImage = new JMenuItem("读取图片文件");
	private JMenuItem jMenuFileLoadColfile = new JMenuItem("读取碰撞文件");
	private JMenuItem jMenuFileSaveColMapImage = new JMenuItem("导出碰撞图");
	private JMenuItem jMenuFileExit = new JMenuItem("退出");

	private JMenu jMenuEdit = new JMenu("编辑");
	private JMenuItem jMenuEditRefresh = new JMenuItem("刷新资源/重加载资源");
	private JMenuItem jMenuEditIsfill = new JMenuItem("碰撞区域填充/线框切换");
	
	private JMenu jMenuHelp = new JMenu("帮助");
	private JMenuItem jMenuHelpAbout = new JMenuItem("关于");
	private JMenuItem jMenuHelpHomepage = new JMenuItem("开发者主页");
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
		
		// 保存图片文件
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

		// 关于
		if (source == jMenuHelpAbout) {
			JOptionPane.showMessageDialog(this, aboutStr, "关于", JOptionPane.INFORMATION_MESSAGE);
		}
		// 软件退出
		if (source == jMenuFileExit) {
			System.exit(0);
		}
		// 打开作者主页
		if (source == jMenuHelpHomepage) {
			CommonUtil.browserURL("http://www.ikags.com");
		}
	}

	
	/**
	 * 读取图片
	 * @param path
	 */
	public void loadMapImage(String path){
		try{
		ColDef.mMapImage = ImageIO.read(new File(path));
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this, "读取图片错误,信息为:\r\n"+ex.getMessage(), "错误", JOptionPane.INFORMATION_MESSAGE);
			ex.printStackTrace();
		}
	}
	
	/**
	 * 读取碰撞文件
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
			JOptionPane.showMessageDialog(this, "读取文本错误,信息为:\r\n"+ex.getMessage(), "错误", JOptionPane.INFORMATION_MESSAGE);
			ex.printStackTrace();
		}
		
	}
	



	//保存单帧图片
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
			JOptionPane.showMessageDialog(this, "保存PNG出错", "出错", JOptionPane.ERROR_MESSAGE);
		}

	}
	

	public ColMapViewer() {

		this.setSize(800, 600); // 窗体的大小
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true); // 窗体
		this.setTitle(titlename); // 设置标题

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
		// 总工具栏

		jMenuBar1.add(jMenuFile);
		jMenuBar1.add(jMenuEdit);
		jMenuBar1.add(jMenuHelp);
		this.setJMenuBar(jMenuBar1);

		Container contents = getContentPane();
		jtp = new JTabbedPane(JTabbedPane.TOP);
		jtp.addTab("碰撞地图显示", mapPanel);
		contents.add(jtp);
		setVisible(true);
	}

	public static void main(String args[]) {
		CommonUtil.setMySkin(2);
		new ColMapViewer();
	}
	

}
