package com.ikags.colmapeditor;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

public class MapImagePanel extends JPanel {
	/**
	 * 
	 */

	
	public int count=0;
	private static final long serialVersionUID = 1L;
	public Thread thread;

	public MapImagePanel() {
		this.setAutoscrolls(true);
		MyListener myListener = new MyListener();
		addMouseListener(myListener);
		addMouseMotionListener(myListener);
		thread=new Thread(){
			public void run(){
			 try{
				 while(ColDef.isRunning){
					 update_Srceen(); 
					 Thread.sleep(34);
					 count++;
				 }
			 }catch(Exception ex){
				 ex.printStackTrace();
			 }
			}
		};
		thread.start();
	}



	class MyListener extends MouseInputAdapter {

		public void mouseMoved(MouseEvent e) {
			update_Srceen();
		}

		public void mousePressed(MouseEvent e) {
			// 拖拽功能，方便整体移动。
			 if (SwingUtilities.isRightMouseButton(e)) {
				change_xxx = e.getX() - xxx;
				change_yyy = e.getY() - yyy;
				update_Srceen();
			}
		}

		// 鼠标拖拽
		public void mouseDragged(MouseEvent e) {
			
			// 右键拖拽全局
			if (SwingUtilities.isRightMouseButton(e)) {
				xxx = e.getX() - change_xxx;
				yyy = e.getY() - change_yyy;
				update_Srceen();
			}
		}

		public void mouseReleased(MouseEvent e) {
		}

	}

	int xxx = 32;
	int yyy = 32;
	int change_xxx = 0;
	int change_yyy = 0;

	int colsize=0;
	
	public void updata() {



	}

	
	Color bgcolor=new Color(ColDef.BG_R, ColDef.BG_G, ColDef.BG_B);
	Color linecolor=new Color(ColDef.SG_R, ColDef.SG_G, ColDef.SG_B);
	Color colcolor1=new Color(ColDef.CG_R1, ColDef.CG_G1, ColDef.CG_B1);
	Color colcolor2=new Color(ColDef.CG_R2, ColDef.CG_G2, ColDef.CG_B2);
	Color blackcolor=new Color(0, 0, 0);
	public void paint(Graphics g) {
		g.setColor(bgcolor);
		g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(linecolor);
		int maxlength = Math.max(g.getClipBounds().width, g.getClipBounds().height);
		for (int i = 0; i < (maxlength / 32) + 1; i++) {
			g2.drawLine(0, 32 * i, maxlength, 32 * i);
			g2.drawLine(32 * i, 0, 32 * i, maxlength);
		}

		updata();
		// 图像开始

		if (ColDef.mMapImage != null) {
			g2.drawImage(ColDef.mMapImage, xxx, yyy, null);
		}
		
      if(ColDef.colmaplist!=null){
    	  if(count%40<20){
    			g2.setColor(colcolor1);    		  
    	  }else{
    			g2.setColor(colcolor2);    	
    	  }
    	  colsize=ColDef.colmaplist.length;
  		for (int i = 0; i < ColDef.colmaplist.length; i++) {
  			int[] colints=ColDef.colmaplist[i];
  			if(ColDef.isFill==true){
  				g2.fillRect(xxx+colints[0], yyy+colints[1], colints[2], colints[3]);
  			}else{
  				g2.drawRect(xxx+colints[0], yyy+colints[1], colints[2], colints[3]);
  			}
  		}
     }

		g2.setColor(blackcolor);
		g2.drawString(xxx+","+yyy,15,15);
		g2.drawString("col="+ColDef.colFilepath+"  ("+colsize+")",15,30);
		g2.drawString("img="+ColDef.ImageFilepath,15,45);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public void update_Srceen() {
		repaint();
	}

}
