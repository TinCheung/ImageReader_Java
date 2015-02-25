public class ImageManipulation {
	public static int[] convertRGBtoYUV (int r, int g, int b) {
		int result[] = new int[3]; // Record the result in YUV format.
		
		result[0] = (int)((double)r * 0.299 + (double)g * 0.587 + (double)b * 0.114);
		result[1] = (int)((double)r * -0.147 + (double)g * -0.289 + (double)b * 0.436);
		result[2] = (int)((double)r * 0.615 + (double)g * -0.515 + (double)b * -0.100);
		//System.out.println("The origin RGB: " + r + " " + g + " " + b + 
				//" turned into " + result[0] + " " + result[1] + " " + result[2]);
		
		return result;
	}
	
	public static int[] convertYUVtoRGB(int y, int u, int v) {
		int result[] = new int[3]; // Record the result in RGB format.
		
		result[0] = (int)((double)y * 0.999 + (double)u * 0.000 + (double)v * 1.140);
		result[1] = (int)((double)y * 1.000 + (double)u * -0.395 + (double)v * -0.581);
		result[2] = (int)((double)y * 1.000 + (double)u * 2.032 + (double)v * -0.000);
		
		int i;
		for(i = 0; i < 3; i++) {
			result[i] = result[i] < 0 ? 0 : result[i];
			result[i] = result[i] > 255 ? 255 : result[i];
		}
		
		//System.out.println("The origin RGB: " + y + " " + u + " " + v + 
				//" turned into " + result[0] + " " + result[1] + " " + result[2]);
		
		return result;
	}
	
	// Subsample one row by one row.
	public static int[] subsampleSpace(int space[], int begin, int size, int step) {
		// System.out.println("space size: " + space.length + " begin: " + begin);
		if (step == 0) {
			int result[] = new int[1];
			return  result;
		}
		int subsampleResult[] = new int[size/step + (size % step > 0 ? 1 : 0)];
		int i, j;
		
		for(i = 0, j = 0; i < size; i+=step) {
			// System.out.println("i: " + i + " j: " + j);
			subsampleResult[j] = space[i + begin];
			j++;
		}
		
		return subsampleResult;
	}
	
	// Ajust the subsample result.
	public static int[] interpolation(int subsample[], int step, int width) {
		int result[] = new int[width];
		int i, j;
		double increment;
		
		if (step != 0) {
			for(i = 0; i < subsample.length; i++) {
				increment = i == subsample.length - 1 ? 0 : (((double)subsample[i + 1] - (double)subsample[i])) / (double) step;
				for(j = 0; j < step && i * step + j < width; j++) {
					result[i * step + j] = (int)(increment * j + (double)subsample[i]);
				}
			}
		}
		else {
			for (i = 0; i < width; i++) {
				result[i] = 0;
			}
		}
		
		return result;
	}
	
	public static int[] quantizationRGB(int data[], int q) {
		int result[] = new int[data.length];
		int quantizedValues[] = new int[q];
		
		double step = ((double)255) / (double)(q-1);
		int i;
		
		quantizedValues[0] = 0;
		for(i = 1; i < q; i++) {
			quantizedValues[i] = (int) Math.round(((double)i) * step);
			// System.out.println("Quantized Value: " + quantizedValues[i]);
		}
		
		int base;
		double rest;
		for(i = 0; i < data.length; i++) {
			base = (int)((double)data[i] / step);
			rest = (data[i] - base * step);
			//System.out.println("rest " + rest);
			if(rest > (step / 2)) {
				base++;
			}
			
			//System.out.println(data[i] + " " + step + " " + base + " " + rest + " " + q);
			//if(data[i] > 192) System.out.println("The value " + data[i] + " turned into " + quantizedValues[base]);
			result[i] = quantizedValues[base];
			// System.out.println(data[i]);
		}
		
		return result;
	}
	/*
	public static void imageFilter(int filterHeight, int filterWidth, int filter[], int imageData[], int height, int width, int channel, boolean scale) {
		int x, y, z, i, j, min, max, sum, sub, count, result;
		max = Integer.MIN_VALUE;
		min = Integer.MAX_VALUE;
		
		// Filtering
		for(z = 0; z < channel; z++) {
			for(y = 1; y < height-filterHeight/2; y++) {
				for(x = 1; x < width-filterWidth/2; x++) {
					count = sub = sum = 0;
					
					for(j = y - filterHeight / 2; j <= y + filterHeight / 2; j++) {
						for(i = x - filterWidth / 2; i <= x + filterWidth / 2; i++) {
							if (j < 0 || j >= height || i < 0 || i >= width) {
								
							}
							else {
								sum += filter[sub] * imageData[height * width * z + j * width + i];
								count++;
							}
							sub++;
						}
					}
					result = sum / count;
					imageData[height * width * z + y * width + x] = result;
					min = min > result ? result : min;
					max = max < result ? result : max;
				}
			}
			
			System.out.println("max: " + max + " min: " + min);
			if(scale) {
				for(y = 1; y < height - filterHeight/2; y++) {
					for(x = 1; x < width- filterWidth/2; x++) {
						imageData[height * width * z + y * width + x] = (int)((double)
								(imageData[height * width * z + y * width + x] - min) / (double)(max - min) * (double)255);
					}
				}
			}
		}
	}*/
}

