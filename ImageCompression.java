import java.util.Random;

public class ImageCompression {
	private int imageData1[][];
	private int imageData2[][];
	private int width;
	private int height;
	private int codebook1[];
	private int codebook2[];
	
	public ImageCompression(int initial[][], int w, int h, int vectorNum)
	{
		imageData1 = new int[w/2][h];
		imageData2 = new int[w/2][h];
		codebook1 = new int[vectorNum];
		codebook2 = new int[vectorNum];
		width = w;
		height = h;
		
		int i, j;
		for(i = 0; i < w; i+=2) {
			for(j = 0; j < h; j++) {
				imageData1[i/2][j] = initial[i][j];
				imageData2[i/2][j] = initial[i+1][j];
			}
		}
		
		Random random = new Random();
		for (i = 0; i < vectorNum; i++) {
			codebook1[i] = random.nextInt(255);
			codebook2[i] = random.nextInt(255);
		}
		//printCodeBook();
	}
	
	public int[][] getCompressionData()
	{
		int imageData[][] = new int[width][height];
		
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i+=2) {
				imageData[i][j] = this.imageData1[i/2][j];
				imageData[i+1][j] = this.imageData2[i/2][j];
			}
		}
		
		return imageData;
	}
	
	public void compress(double error)
	{
		double currentError = 0;
		int vectorNum = codebook1.length;
		int sumOfEuclideanDistance1[] = new int[vectorNum];
		int sumOfEuclideanDistance2[] = new int[vectorNum];
		int clausterVectorCount[] = new int[vectorNum];
		Random rand = new Random();

		do {
			int i, j, minVector;
			for (i = 0; i < vectorNum; i++) {
				clausterVectorCount[i] = sumOfEuclideanDistance1[i] = sumOfEuclideanDistance2[i] = 0;
			}
			
			for (i = 0; i < width / 2; i++) {
				for (j = 0; j < height; j++) {
					minVector = findTheNearestVector(imageData1[i][j], imageData2[i][j]);
					sumOfEuclideanDistance1[minVector] += imageData1[i][j];
					sumOfEuclideanDistance2[minVector] += imageData2[i][j];
					clausterVectorCount[minVector] += 1;
				}
			}
			
			// Update codebook.
			int temp1, temp2;
			currentError = 0;
			for (i = 0; i < vectorNum; i++) {
				temp1 = codebook1[i];
				temp2 = codebook2[i];
				codebook1[i] = clausterVectorCount[i] != 0 ? sumOfEuclideanDistance1[i] / clausterVectorCount[i] : rand.nextInt(255);
				codebook2[i] = clausterVectorCount[i] != 0 ? sumOfEuclideanDistance2[i] / clausterVectorCount[i] : rand.nextInt(255);
				currentError += Math.abs(codebook1[i] - temp1);
				currentError += Math.abs(codebook2[i] - temp2);
			}
			System.out.println("Current Error: " + currentError);
			//this.printCodeBook();
		} while(currentError > error);
		quantization();
		printCodeBook();
	}
	
	private void quantization()
	{
		int i, j, minVector;
		
		for (i = 0; i < width / 2; i++) {
			for (j = 0; j < height; j++) {
				minVector = findTheNearestVector(imageData1[i][j], imageData2[i][j]);
				imageData1[i][j] = codebook1[minVector];
				imageData2[i][j] = codebook2[minVector];
			}
		}
	}
	
	private int findTheNearestVector(int x, int y)
	{
		int minVector = 0;
		double minDistance = this.EuclideanDistance(x, y, codebook1[0], codebook2[0]);
		double tempDistance;
		
		for (int i = 1; i < codebook1.length; i++) {
			tempDistance = this.EuclideanDistance(x, y, codebook1[i], codebook2[i]);
			if (tempDistance < minDistance) {
				minDistance = tempDistance;
				minVector = i;
			}
		}
		
		return minVector;
	}
	
	private double EuclideanDistance(int x1, int y1, int x2, int y2)
	{
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	private void printCodeBook()
	{
		for (int i = 0; i < codebook1.length; i++) {
			System.out.println("1: " + codebook1[i] + " 2: " + codebook2[i]);
		}
	}
}
