package u2;

public class Region {

	  private int [][] array;
	    private int posX,posY;

	    /**
	     * Constructor med array och position för x & y
	     * @param array -array with color values.
	     * @param posX -int with x coordinate
	     * @param posY-int with y coordinate
	     */
	    public Region(int[][] array, int posX,int posY){
	        this.array=array;
	        this.posX=posX; 
	        this.posY=posY;
	    }

	    /**
	     * Returnerar the array
	     * @return int[][]
	     */
	    public int[][] getArray(){
	        return array;
	    }

	    /**
	     * Returnerar position X
	     * @return int
	     */
	    public int getPosX(){
	        return posX;
	    }

	    /**
	     * Returnerar position Y
	     * @return int
	     */
	    public int getPosY(){
	        return posY;
	    }
	
}
