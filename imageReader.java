import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.swing.*;

public class imageReader {

   /**
 * @param args
 */
public static void main(String[] args) 
   {
	String fileName = args[0];
	int width = 352;
	int height = 288;
	int numberOfVector = Integer.valueOf(args[1]);
	int imageData[][] = new int[width][height];
	
	System.out.println("The num of vector is: " + numberOfVector);
	
	/* Declare new vars to record the data of the image. */
	
    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    // The new image after modification.
    BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    try {
	    File file = new File(args[0]);
	    InputStream is = new FileInputStream(file);

	    long len = file.length();
	    byte[] bytes = new byte[(int)len];
	    
	    int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
        
    	int ind = 0;
    	
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				byte a = 0;
				byte intensity = bytes[ind]; 
				imageData[x][y] = Byte.toUnsignedInt(intensity);
				int pix = 0xff000000 | ((intensity & 0xff) << 16) | ((intensity & 0xff) << 8) | (intensity & 0xff);
				img.setRGB(x,y,pix);
				ind++;
			}
		}
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
	
    // Initialize the compression setting.
    ImageCompression compression = new ImageCompression(imageData, width, height, numberOfVector);
    compression.compress(0);
    imageData = compression.getCompressionData();
    // Show the image
    for(int y = 0; y < height; y++){
		for(int x = 0; x < width; x++){
			byte intensity = (byte) imageData[x][y];
			int pix = 0xff000000 | ((intensity & 0xff) << 16) | ((intensity & 0xff) << 8) | (intensity & 0xff);
			img2.setRGB(x,y,pix);
		}
	}
    
    // Use a panel and label to display the image
    JPanel  panel = new JPanel ();
    panel.add (new JLabel (new ImageIcon (img)));
    panel.add (new JLabel (new ImageIcon (img2)));
    
    JFrame frame = new JFrame("Display images");
    
    frame.getContentPane().add (panel);
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
   }
}