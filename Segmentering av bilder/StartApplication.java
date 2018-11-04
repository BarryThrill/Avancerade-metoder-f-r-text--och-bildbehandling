package u2;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class StartApplication {

	 private BufferedImage inputImg; //outputImg
	 private WritableRaster inPutRaster,outputRaster;

	    /**
	     * startOne() startar första delen av applikationen
	     */
	 
	 public void startOne(){
		  try {
              String file = "/Users/mikaelhorvath/Desktop/people.jpg";
              this.inputImg = ImageIO.read(new File(file));
          } catch (IOException e) {
              e.printStackTrace();
          }
          Algorithm g = new Algorithm();
          //Gråskala
          inPutRaster = g.convertToGray(inputImg); 
          new ImagePanel(inPutRaster,"1. Gray");
          //Gaussian Blur
          outputRaster = g.gaussFilter(inPutRaster);
          new ImagePanel(outputRaster,"2. Gaussian Filter");
          //Sobel
          outputRaster = g.convolution(inPutRaster);
          new ImagePanel(outputRaster,"3. Sobel");
          //Edge Thinning
          outputRaster = g.edgeThinning(outputRaster);
          new ImagePanel(outputRaster,"4. Edge Thinning");
          //Hysteresis
          outputRaster = g.hysteresis(outputRaster);
          new ImagePanel(outputRaster,"5. Hysteresis");
	 }
	 
	 /**
	  * startTwo() startar andra delen av applikationen (segmentering)
	  */
	 
	 public void startTwo(){
		 try {   
         	String file = "/Users/mikaelhorvath/Desktop/people.jpg";
             this.inputImg = ImageIO.read(new File(file));
         } catch (IOException e) {
             e.printStackTrace();
         }
         Algorithm gg = new Algorithm();
         //Gråskala
         inPutRaster = gg.convertToGray(inputImg);
         new ImagePanel(inPutRaster,"Step 1-To Gray");
         //Segmentering
         SplitAndMerge sm=new SplitAndMerge();
         outputRaster=sm.SplitAndMergeAlgorithm(inPutRaster);
         new ImagePanel(outputRaster,"Step 2-Split&Merge");
	 }
	   

	    /**
	     * Visar bilden i JPanel
	     * Used to show the different results and progress.
	     */
	    private class ImagePanel extends JPanel {
	        /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private final BufferedImage image;

	        /**
	         * Får tillbaka en raster och visar denna bilden i JPanel.
	         * @param raster
	         * @param title
	         */
	        public ImagePanel(WritableRaster raster,String title) {
	            image=new BufferedImage(raster.getWidth(),raster.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
	            image.setData(raster);
	            JFrame frame = new JFrame(title);
	            frame.add(this);
	            frame.pack();
	            frame.setSize(512, 512);
	            frame.setVisible(true);
	            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            setSize(512, 512);
	        }

	        /**
	         * Ritar upp bilden med paintComponent 
	         * @param g
	         */
	        public void paintComponent(Graphics g) {
	            Rectangle rect = this.getBounds();
	            if (image != null) {
	                g.drawImage(image, 0, 0, rect.width, rect.height, this);
	            }
	        }
	    }

	    /**
	     * Main method.
	     * @param args
	     */
	    public static void main(String[] args) {
	        StartApplication app = new StartApplication();
	        app.startOne();
	        //app.startTwo();

	    }
	}
	

