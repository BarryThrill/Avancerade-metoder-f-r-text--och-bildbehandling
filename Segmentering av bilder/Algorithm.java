package u2;

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Algorithm {

	/**
	 * Första sektionen hanterar Gaussian Blur
	 */
 
	public WritableRaster gaussFilter(WritableRaster inPutRaster) {
		WritableRaster outPutRaster = inPutRaster;
		int[][] matrix = new int[3][3];
		for (int row = 1; row < inPutRaster.getWidth() - 1; row++) {
			for (int col = 1; col < inPutRaster.getHeight() - 1; col++) {
				matrix[0][0] = inPutRaster.getSample(row - 1, col - 1, 0);
				matrix[0][1] = inPutRaster.getSample(row - 1, col, 0);
				matrix[0][2] = inPutRaster.getSample(row - 1, col + 1, 0);
				matrix[1][0] = inPutRaster.getSample(row, col - 1, 0);
				matrix[1][1] = inPutRaster.getSample(row, col, 0);
				matrix[1][2] = inPutRaster.getSample(row, col + 1, 0);
				matrix[2][0] = inPutRaster.getSample(row + 1, col - 1, 0);
				matrix[2][1] = inPutRaster.getSample(row + 1, col, 0);
				matrix[2][2] = inPutRaster.getSample(row + 1, col + 1, 0);

				int blur = (int) gaussK(matrix);
				outPutRaster.setSample(row, col, 0, blur);
			}
		}
		return outPutRaster;
	}

	public double gaussK(int[][] matrix) {
		double gy = (matrix[0][0] * 1) + (matrix[0][1] * 2) + (matrix[0][2] * 1) + (matrix[1][0] * 2)
				+ (matrix[1][1] * 4) + (matrix[1][2] * 2) + (matrix[2][0] * 1) + (matrix[2][1] * 2)
				+ (matrix[2][2] * 1);
		gy = 0.0625 * gy;
		return gy;
	}

	/**
	 * andra sektionen hanterar sobel
	 */

	public WritableRaster convolution(WritableRaster inPutRaster) {
		BufferedImage temp = new BufferedImage(inPutRaster.getWidth(), inPutRaster.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster outPutRaster = temp.getRaster();
		int[][] matrix = new int[3][3];

		for (int col = 1; col < inPutRaster.getWidth() - 1; col++) {
			for (int row = 1; row < inPutRaster.getHeight() - 1; row++) {
				matrix[0][0] = inPutRaster.getSample(col - 1, row - 1, 0);
				matrix[0][1] = inPutRaster.getSample(col - 1, row, 0);
				matrix[0][2] = inPutRaster.getSample(col - 1, row + 1, 0);
				matrix[1][0] = inPutRaster.getSample(col, row - 1, 0);
				matrix[1][2] = inPutRaster.getSample(col, row + 1, 0);
				matrix[2][0] = inPutRaster.getSample(col + 1, row - 1, 0);
				matrix[2][1] = inPutRaster.getSample(col + 1, row, 0);
				matrix[2][2] = inPutRaster.getSample(col + 1, row + 1, 0);

				int edge = (int) convolutionKernels(matrix);
				outPutRaster.setSample(col, row, 0, edge);
			}
		}
		return outPutRaster;
	}

	public double convolutionKernels(int[][] matrix) {
		int gy = (matrix[0][0] * -1) + (matrix[0][1] * -2) + (matrix[0][2] * -1) + (matrix[2][0]) + (matrix[2][1] * 2)
				+ (matrix[2][2]);
		int gx = (matrix[0][0] * -1) + (matrix[0][2]) + (matrix[1][0] * -2) + (matrix[1][2] * 2) + (matrix[2][0] * -1)
				+ (matrix[2][2]);
		return Math.sqrt(Math.pow(gy, 2) + Math.pow(gx, 2));
	}

	/**
	 * Edge Thinning
	 */

	public WritableRaster edgeThinning(WritableRaster raster) {
		ArrayList<Double> degrees = new ArrayList<Double>();

		WritableRaster outRaster = raster;

		// Jämför med 3x3 mask för varje pixel
		int[][] matrix = new int[3][3];
		for (int row = 1; row < raster.getWidth() - 1; row++) {
			for (int col = 1; col < raster.getHeight() - 1; col++) {

				matrix[0][0] = raster.getSample(row - 1, col - 1, 0);
				matrix[0][1] = raster.getSample(row - 1, col, 0);
				matrix[0][2] = raster.getSample(row - 1, col + 1, 0);
				matrix[1][0] = raster.getSample(row, col - 1, 0);
				matrix[1][2] = raster.getSample(row, col + 1, 0);
				matrix[2][0] = raster.getSample(row + 1, col - 1, 0);
				matrix[2][1] = raster.getSample(row + 1, col, 0);
				matrix[2][2] = raster.getSample(row + 1, col + 1, 0);

				// Vertical
				double gy = (matrix[0][0] * -1) + (matrix[0][1] * -2) + (matrix[0][2] * -1) + (matrix[2][0])
						+ (matrix[2][1] * 2) + (matrix[2][2]);
				// Horizontal
				double gx = (matrix[0][0] * -1) + (matrix[0][2]) + (matrix[1][0] * -2) + (matrix[1][2] * 2)
						+ (matrix[2][0] * -1) + (matrix[2][2]);
				
				Double direction = Math.atan2(gy, gx);
				direction = Math.toDegrees(direction);
				degrees.add(direction);

				// Om vinkel är negativ, konvertera till positiv
				if (direction < 0) {
					direction = 180 + direction;
				}
				
				int closestAngleDifference = 200; 
				double closestDirection = 0;
				int[] angles = { 0, 45, 90, 135, 180 };
				for (int i = 0; i < angles.length; i++) {
					int difference = 0;
					difference = angles[i] - direction.intValue();
					if (difference < 0) {
						difference = difference - difference * 2;
					}
					
					closestAngleDifference = Math.min(closestAngleDifference, difference);
					if (closestAngleDifference == difference) {
						closestDirection = angles[i];
					}
				}
				direction = closestDirection;
			
				// Vinkel 0 or 180-> North/South
				if (direction == 0 || direction == 180) {
					if (raster.getSample(row, col, 0) < raster.getSample(row - 1, col, 0)
							|| raster.getSample(row, col, 0) < raster.getSample(row + 1, col, 0)) {
						outRaster.setSample(row, col, 0, 0);
					} else {
						outRaster.setSample(row, col, 0, raster.getSample(row, col, 0));
					}
				}
				// Vinkel 45-> NorthWest/SouthEast.
				else if (direction == 45) {
					if (raster.getSample(row, col, 0) < raster.getSample(row - 1, col - 1, 0)
							|| raster.getSample(row, col, 0) < raster.getSample(row + 1, col + 1, 0)) {
						outRaster.setSample(row, col, 0, 0);
					} else {
						outRaster.setSample(row, col, 0, raster.getSample(row, col, 0));
					}
				}
				// Vinkel = 90,West/East.
				else if (direction == 90) {
					if (raster.getSample(row, col, 0) < raster.getSample(row, col - 1, 0)
							|| raster.getSample(row, col, 0) < raster.getSample(row, col + 1, 0)) {
						outRaster.setSample(row, col, 0, 0);
					} else {
						outRaster.setSample(row, col, 0, raster.getSample(row, col, 0));
					}
				}
				// Vinkel= 135 -> NorthEast/SouthWest.
				else if (direction == 135) {

					if (raster.getSample(row, col, 0) < raster.getSample(row - 1, col + 1, 0)
							|| raster.getSample(row, col, 0) < raster.getSample(row + 1, col - 1, 0)) {
						outRaster.setSample(row, col, 0, 0);
					} else {
						outRaster.setSample(row, col, 0, raster.getSample(row, col, 0));
					}
				}
			}
		}
		return outRaster;
	}

	/**
	 * Metoden genomför hysteresis
	 * @param raster
	 * @return WritableRaster
	 */
	public WritableRaster hysteresis(WritableRaster raster) {
		int lowThreshold, highTreshold;
		// Frågar användaren om tröskelvärden
		lowThreshold = Integer.parseInt(JOptionPane.showInputDialog("Low Threshold: 0-255"));
		highTreshold = Integer.parseInt(JOptionPane.showInputDialog(
				"High Threshold: 0-255"));

		for (int row = 0; row < raster.getHeight(); row++) {
			for (int col = 0; col < raster.getWidth(); col++) {
				
				if (raster.getSample(col, row, 0) >= highTreshold) {
					// Inget
				}
				
				else if (raster.getSample(col, row, 0) <= lowThreshold) {
					raster.setSample(col, row, 0, 0);
				}
				
				else {
					int i = row - 1, k = col - 1;
					boolean neighbour = false;
					while (i <= row + 1) {
						while (k <= col + 1) {
							
							if (i > 0 && i < raster.getHeight() && k > 0 && k < raster.getWidth()) {
								if (raster.getSample(k, i, 0) >= highTreshold) {
									neighbour = true;
								}
							}
							k++;
						}
						i++;
					}
					
					if (!neighbour) {
						raster.setSample(col, row, 0, 0);
					}

				}
			}
		}
		return raster;
	}

	/**
	 * Gråskala
	 */

	/**
	 * Konverterar till Gråskala
	 * 
	 * @param image-input
	 * @return WritableRaster
	 */
	public WritableRaster convertToGray(BufferedImage image) {
		BufferedImage grayscale = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		ColorConvertOp op = new ColorConvertOp(image.getColorModel().getColorSpace(),
				grayscale.getColorModel().getColorSpace(), null);
		op.filter(image, grayscale);
		return grayscale.getRaster();
	}

}
