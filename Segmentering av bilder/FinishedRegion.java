package u2;

public class FinishedRegion {

	 private int width,height,colorValue,posX,posY;

	    /**
	     * Constructor
	     * @param width
	     * @param height
	     * @param colorValue
	     * @param posX
	     * @param posY
	     */
	    public FinishedRegion(int width,int height,int colorValue,int posX,int posY){
	        this.width=width;
	        this.height=height;
	        this.colorValue=colorValue; 
	        this.posX=posX;
	        this.posY=posY;
	    }

	    /**
	     * Returnerar width
	     * @return int
	     */
	    public int getWidth(){
	        return width;
	    }

	    /**
	     * Returnerar height
	     * @return int
	     */
	    public int getHeight(){
	        return height;
	    }

	    /**
	     * Returnerar the color value.
	     * @return int
	     */
	    public int getColorValue(){
	        return colorValue;
	    }

	    /**
	     * Returnerar X coordinate.
	     * @return int
	     */
	    public int getPosX(){
	        return posX;
	    }

	    /**
	     * Returnerar Y coordinate.
	     * @return int
	     */
	    public int getPosY(){
	        return posY;
	    }
	
}
