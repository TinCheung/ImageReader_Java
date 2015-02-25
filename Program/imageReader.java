import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.swing.*;

import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory.Zephyr;


public class imageReader {

  
   /**
 * @param args
 */
public static void main(String[] args) 
   {
	String fileName = args[0];
   	
	int width = 352;
	int height = 288;
	int subsampleYUV[] = new int[3];
	
	int inputQ; 
	subsampleYUV[0] = Integer.parseInt(args[1]);
	subsampleYUV[1] = Integer.parseInt(args[2]);
	subsampleYUV[2] = Integer.parseInt(args[3]);
	inputQ = Integer.parseInt(args[4]);
	
	/* Declare new vars to record the data of the image. */
    int red, green, blue;
	int imageData[] = new int[height * width * 3];
	int YUV[] = new int[3]; // The YUV result after convertion.
	int RGB[] = new int[3]; // The RGB result after convertion.
	int imageSub = 0;
	
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
				byte r = bytes[ind];
				byte g = bytes[ind+height*width];
				byte b = bytes[ind+height*width*2]; 
				
				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
				//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
				img.setRGB(x,y,pix);
				
				// Record the rgb data for the image.
				red = Byte.toUnsignedInt(r);
				green = Byte.toUnsignedInt(g);
				blue = Byte.toUnsignedInt(b);
				
				imageData[imageSub] = red;
				imageData[imageSub + width * height] = green;
				imageData[imageSub + width * height * 2] = blue;
				
				imageSub++;

				ind++;
			}
		}
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    /*
    int filter[] = new int[49];
	for (int i = 0; i < 49; i++) filter[i] = 1;
	int Lfilter[] = new int[9];
	Lfilter[0] = Lfilter[2] = Lfilter[6] = Lfilter[8] = 0;
	Lfilter[1] = Lfilter[3] = Lfilter[5] = Lfilter[7] = -1;
	Lfilter[4] = 7;
	*/
    /* Manipulate the image. */
    // The convertion to YUV.
	int x, y, channel;
	
	for(y = 0; y < height; y++) {
    	for(x = 0; x < width; x++) {
    		YUV = ImageManipulation.convertRGBtoYUV(imageData[y * width + x], 
    				imageData[height * width + y * width + x], imageData[height * width * 2+ y * width + x]);
    		imageData[y * width + x] = YUV[0];
    		imageData[height * width + y * width + x] = YUV[1];
    		imageData[height * width * 2 + y * width + x] = YUV[2]; 
    	}
    }
    
	// Make the image smoother
	
	int adjustedSubsample[] = new int[width];
	//ImageManipulation.imageFilter(5, 5, filter, imageData, height, width, 3, true);
	for(x = 0, y = 0; y < height; y++) {		
    	// Subsample and adjust the result.
    	for(channel = 0; channel < 3; channel++) {
    		adjustedSubsample = ImageManipulation.interpolation(
    				ImageManipulation.subsampleSpace(imageData, height * width * channel + y * width, width, subsampleYUV[channel]),
    				subsampleYUV[channel], width);
    		int i;
        	for(i = 0; i < width; i++) {
        		imageData[height * width * channel + y * width + i] = adjustedSubsample[i];
        	}
    	} 
    }
	
	// Convert back to the RGB space.
    for(y = 0; y < height; y++) {
    	for(x = 0; x < width; x++) {
    		RGB = ImageManipulation.convertYUVtoRGB(imageData[x + y * width], imageData[x + y * width + height * width], 
    				imageData[x + y * width + height * width * 2]);
    		imageData[x + y * width] = RGB[0];
    		imageData[x + y * width + height * width] = RGB[1];
    		imageData[x + y * width + height * width * 2] = RGB[2];
    	}
    }
    
    int maskData[] = new int[width * height * 3];
    for(int z = 0; z < 3; z++) {
		for(y = 0; y < height; y++) {
	    	for(x = 0; x < width; x++) {
	    		maskData[y * width + x + z * width * height] = imageData[y * width + x + z * width * height];
	    	}
	    }
    }
	
    //ImageManipulation.imageFilter(3, 3, filter, maskData, height, width, 1, true);
	for(int z = 0; z < 3; z++) {
		for(y = 0; y < height; y++) {
	    	for(x = 0; x < width; x++) {
	    		imageData[y * width + x + z * width * height] = maskData[y * width + x + z * width * height];
	    	}
	    }
	}
	
    // Quantization.
    int quantizationResult[] = new int[width * height * 3];
    quantizationResult = ImageManipulation.quantizationRGB(imageData, inputQ);
    for(y = 0; y < quantizationResult.length; y++) {
    	imageData[y] = quantizationResult[y];
    }
    
    // Show the image
    for(y = 0; y < height; y++) {
    	for(x = 0; x < width; x++) {
    		red = imageData[x + y * width];
    		green = imageData[x + y * width + height * width];
    		blue = imageData[x + y * width + height * width * 2];
    		
    		byte r, g, b;
    		r = (byte)red;
    		g = (byte)green;
    		b = (byte)blue;
    		
    		int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
    		img2.setRGB(x, y, pix);
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