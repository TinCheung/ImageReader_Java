You can run the program by
javac *.java
java imageReader x x x x x

The quantization scheme I use is that
step — the interval between two adjacent quantized values
X    - the value we are going to quantize
Xnew = (X/step + round(X%step)) * interval_length