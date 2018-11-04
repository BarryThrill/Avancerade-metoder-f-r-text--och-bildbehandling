package u2;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class SplitAndMerge {

	  private ArrayList<Region> regionList =new ArrayList<Region>();  //List with regions to test for possible split.
	    private ArrayList<FinishedRegion> finRegionList=new ArrayList<FinishedRegion>(); //List with finished regions to merge.

	    /**
	     * Input bild med gråskala
	     * @param inputRaster- bild som raster
	     * @return WritableRaster
	     */
	    public WritableRaster SplitAndMergeAlgorithm(WritableRaster inputRaster){
	        //Användaren väljer tröskel
	        int threshold=Integer.parseInt(JOptionPane.showInputDialog(null,"Mata in ett tröskelvärde för regionsdelning. 0-255" ));

	        int [][] imagevalues =new int [inputRaster.getHeight()][inputRaster.getWidth()];
	        for (int row=0;row<imagevalues.length;row++){
	            for (int col=0;col<imagevalues[row].length;col++){
	                imagevalues[row][col]=inputRaster.getSample(col,row,0);
	            }
	        }
	        //Lägger till första regin
	        regionList.add(new Region(imagevalues,0,0)); 

	       //Så länge det finns regioner
	       while(regionList.size()>0){
	            int average=0;
	            int [][] workingRegion=regionList.get(0).getArray();
	            int totalNbrOfPixels = workingRegion.length * workingRegion[0].length;
	            for (int row = 0; row < workingRegion.length; row++) {
	                for (int col = 0; col < workingRegion[row].length; col++) {
	                    average += workingRegion[row][col];
	                }
	            }
	            average = average / totalNbrOfPixels;
	            int highThres, lowThres;
	            //Sätt tröskelvärde i förhållande till medelvärde
	            highThres = average + threshold;
	            lowThres = average - threshold;

	            boolean withinThres=true;
	            for (int row = 0; row < workingRegion.length; row++) {
	                for (int col = 0; col < workingRegion[0].length; col++) {
	                    int value=workingRegion[row][col];
	                    if (value >= lowThres && value <= highThres) {
	                      } else {
	                        withinThres=false;
	                        row=workingRegion.length;
	                        col=workingRegion[0].length;
	                    }
	                }
	            }
	           //Om region är ok sparar vi den till att mergas
	           if(withinThres){
	               finRegionList.add(new FinishedRegion(workingRegion[0].length, workingRegion.length, average,
	                       regionList.get(0).getPosX(),regionList.get(0).getPosY() ));
	           }
	          
	           else{
	               split(regionList.get(0));
	           }
	           //Klart, kan ta bort
	           regionList.remove(0);
	        }
	        //Returnera resultatet.
	        return merge(inputRaster.getWidth(), inputRaster.getHeight());
	    }

	    /**
	     * Metoden splittar och adderar nya regioner
	     * @param region- the region to split.
	     */
	    public void split(Region region) {
	        int[][] regionArray = region.getArray();
	        int[][] newRegionSizes = calculateRegionSizes(regionArray);
	        int width1, width2, height1, height2;
	        
	        width1 = newRegionSizes[0][0];
	        width2 = newRegionSizes[0][1];
	        height1 = newRegionSizes[1][0];
	        height2 = newRegionSizes[1][1];

	     
	        if (width1 > 0 && width2 > 0 && height1 > 0 && height2 > 0) {

	            int[][] newRegion1 = new int[height1][width1];
	            int[][] newRegion2 = new int[height1][width2];
	            int[][] newRegion3 = new int[height2][width1];
	            int[][] newRegion4 = new int[height2][width2];

	         
	            for (int row = 0; row < regionArray.length; row++) {
	                for (int col = 0; col < regionArray[row].length; col++) {
	                    //Region 2
	                    if (row < height1 && col >= width1) {
	                        newRegion2[row][col - width1] = regionArray[row][col];
	                    }
	                    //Region 3
	                    else if (row >= height1 && col < width1) {
	                        newRegion3[row - height1][col] = regionArray[row][col];
	                    }
	                    //Region 4
	                    else if (row >= height1 && col >= width1) {
	                        newRegion4[row - height1][col - width1] = regionArray[row][col];
	                    }
	                    //Region 1
	                    else {
	                        newRegion1[row][col] = regionArray[row][col];
	                    }
	                }
	            }
	            //Adderar till listan
	            regionList.add(new Region(newRegion1, region.getPosX() + 0, region.getPosY() + 0));
	            regionList.add(new Region(newRegion2, region.getPosX() + width1, region.getPosY() + 0));
	            regionList.add(new Region(newRegion3, region.getPosX() + 0, region.getPosY() + height1));
	            regionList.add(new Region(newRegion4, region.getPosX() + width1, region.getPosY() + height1));
	        }
	          else {
	            for(int row=0;row<regionArray.length;row++){
	                for (int col=0;col<regionArray[row].length;col++){
	                    finRegionList.add(new FinishedRegion(1,1,regionArray[row][col]
	                            ,region.getPosX()+col,region.getPosY()+row));
	                }
	            }
	        }
	    }

	    /**
	     * Räknar på storlek
	     * @param 
	     * @return 
	     */
	    public int[][] calculateRegionSizes(int[][] regionArray){
	         int[][] regionSize=new int[2][2];

	        //Width
	        if (regionArray[0].length % 2 == 0) {
	            regionSize[0][0] = regionArray[0].length / 2;
	            regionSize[0][1] = regionArray[0].length / 2;
	        } else {
	            regionSize[0][0] = regionArray[0].length / 2;
	            regionSize[0][1]= (regionArray[0].length / 2) + 1;
	        }

	        //Height
	        if (regionArray.length % 2 == 0) {
	            regionSize[1][0] = regionArray.length / 2;
	            regionSize[1][1] = regionArray.length / 2;
	        } else {
	            regionSize[1][0]= regionArray.length / 2;
	            regionSize[1][1] = (regionArray.length / 2) + 1;
	        }
	        return regionSize;
	    }

	    /**
	     * Merging
	     * @param width
	     * @param height
	     * @return WritableRaster
	     */
	    public WritableRaster merge (int width,int height){
	        BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
	        WritableRaster outputRaster=img.getRaster();
	        System.out.println("färdiga regioner:"+finRegionList.size());

	        //Merge
	        for (int i=0;i<finRegionList.size();i++) {
	            FinishedRegion temp=finRegionList.get(i);
	            int x,y,colorVal;
	            x=temp.getPosX();
	            y=temp.getPosY();
	            colorVal=temp.getColorValue();
	            for (int row = 0; row < temp.getHeight(); row++) {
	                for (int col = 0; col < temp.getWidth(); col++) {
	                    outputRaster.setSample(col+x,row+y,0,colorVal);
	                }
	            }
	        }
	        return outputRaster;
	    }

	
}
