You will find
	1) A program to display and manipulate two images side by side. This has been
given as java code to read an rgb image of the specified format explained in the homework 
and then displays it and the processed image on screen(As reference code, both should be 
the same as the original).
	2) Example images, which are in the native RGB formats. They are
as follows with dimensions width*height and brief description
image1.rgb -> 352x288 shows a foreman
image2.rgb -> 352x288 shows a golf playing scene
image3.rgb -> 352x288 shows a man in swimming pool
image4.rgb -> 352x288 shows a construction site

Each image is different in the colors, frequency content etc. used and should
server as good examples for playing with subsampling, color space transformations,
quantization, compression etc.

Unzip the folder to where you want. To run the code from command line, first compile with:

>> javac imageReader.java

and then, you can run to read a sample image (image1.rgb) as:

>> java imageReader image1.rgb 352 288
>> java imageReader image2.rgb 352 288
>> java imageReader image3.rgb 352 288
>> java imageReader image4.rgb 352 288

where, the first parameter is the image file name, second is the width and third is the height.

Or, you can install IDE for Java such as eclipse to run a Java project.