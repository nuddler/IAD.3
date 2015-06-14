import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.ImageTerminal;

public class Main {
	private static final String textExtension = ".txt";
	private static final String chartImageExtension = "_chart.png";
	private static final String imageExtension = ".png";
	private static final String output = "output";
	private static final double learningFactory = 0.5;
	private static final int mainIterations = 10;
	private static final int hiddenLayerNeurons = 5;
	private static final String trainingSetFile = "zbior1.txt";

	public static void main(String[] args) throws IOException {
		for(int j = 1; j <= hiddenLayerNeurons; j++){
			for(int i = 0; i < mainIterations; i++){
				testNetwork(j, learningFactory, i, trainingSetFile);
			}
		}
		System.out.println("Koniec");
	}
	
	private static void drawErrorsChart(double error[][], int count, int testNumber, double learningParam){
		PlotStyle errorStyle = new PlotStyle();
		errorStyle.setStyle(Style.LINES);
		errorStyle.setLineType(NamedPlotColor.RED);
		DataSetPlot errorDS = new DataSetPlot(error);
		errorDS.setTitle("Blad sredniokwadratowy");
		errorDS.setPlotStyle(errorStyle);

		JavaPlot p = new JavaPlot();

		ImageTerminal png = new ImageTerminal();
		File file = new File(output + "/" + count + "/" + testNumber + imageExtension);
		try {
			file.createNewFile();
			png.processOutput(new FileInputStream(file));
		} catch (FileNotFoundException ex) {
			System.err.print(ex);
		} catch (IOException ex) {
			System.err.print(ex);
		}

		p.setTerminal(png);
		p.setTitle("N=" + String.valueOf(count) + " n=" + String.valueOf(learningParam));
		p.set("xlabel", "\"Iteracje\"");
		p.set("ylabel", "\"Blad sredniokwadratowy\"");
		p.set("key", "off");
		p.set("terminal", "png size 1024,768");

		p.addPlot(errorDS);
		p.plot();

		try {
			ImageIO.write(png.getImage(), "png", file);
		} catch (IOException ex) {
			System.err.print(ex);
		}
	}

	public static void testNetwork(int count, double learningParam, int testNumber, String trainingSetFile) throws IOException{
		ArrayList<double[]> errors = new ArrayList<double[]>();

		RadialNetwork network = new RadialNetwork(count, learningParam, trainingSetFile);
		double[][] trainingSet = network.getTrainingSet();

		File dir = new File(output);
		dir.mkdir();
		File dir1 = new File(output + "/" + count);
		dir1.mkdir();

		PrintWriter wr = new PrintWriter(output + "/" + count + "/" + testNumber + textExtension);
		wr.println("Test nr " + testNumber + " sieci z " + count + " neuronami wastwy ukrytej");
		wr.println("Wspolczynnik nauki: " + learningParam);

		double error = 0.0d;
		for (int i = 0; i < trainingSet.length; i++){
			error += (trainingSet[i][1] - network.getOutput(trainingSet[i][0]))*(trainingSet[i][1] - network.getOutput(trainingSet[i][0]));
		}
		double errorToListB[] = new double[2];
		errorToListB[0] = 0;
		errorToListB[1] = error/trainingSet.length;
		errors.add(errorToListB);

		wr.println("Błąd początkowy: " + errorToListB[1]);

		int iterations = 1;

		do {
			error = 0.0d;
			for (int i = 0; i < trainingSet.length; i++){
				network.teachOutput(trainingSet[i][0], trainingSet[i][1] - network.getOutput(trainingSet[i][0]));
				error += (trainingSet[i][1] - network.getOutput(trainingSet[i][0])) * (trainingSet[i][1] - network.getOutput(trainingSet[i][0]));
			}
			double errorToList[] = new double[2];
			errorToList[0] = iterations;
			errorToList[1] = error/trainingSet.length;
			errors.add(errorToList);

			if (iterations>=10000){
				break;
			}

			iterations++;

		} while (error > 0.001);

		wr.println();
		wr.println("WYNIKI PO NAUCE");
		wr.println();
		wr.println("Ilosc iteracji: " + iterations);
		wr.println("Blad sredniokwadratowy: " + errors.get(errors.size()-1)[1]);

		wr.close();

		double errorsForGnuplot[][] = new double[errors.size()][2];
		for (double[] tab : errors){
			errorsForGnuplot[errors.indexOf(tab)][0] = tab[0];
			errorsForGnuplot[errors.indexOf(tab)][1] = tab[1];
		}
		double[][] function = new double[10000][2];
		for(int i = 0; i<10000; i++){
			function[i][0] = -4 + (8.0/10000) * i;
			function[i][1] = network.getOutput(function[i][0]);
		}
		double[][] centers = new double[network.getIndexes().size()][2];
		int counter = 0;
		for (Integer i : network.getIndexes()){
			centers[counter][0] = network.getTrainingSet()[i][0];
			centers[counter][1] = network.getTrainingSet()[i][1];
			counter++;
		}
		drawFunctionChart(trainingSet, function, centers, count, testNumber, learningParam);
		drawErrorsChart(errorsForGnuplot, count, testNumber, learningParam);
	}

	private static void drawFunctionChart(double testSet[][], double[][] function, double[][] centers, int count, int testNumber, double learningParam){
		PlotStyle testSetStyle = new PlotStyle();
		testSetStyle.setStyle(Style.POINTS);
		testSetStyle.setLineType(NamedPlotColor.RED);
		DataSetPlot testSetDS = new DataSetPlot(testSet);
		testSetDS.setTitle("Zbior testowy");
		testSetDS.setPlotStyle(testSetStyle);

		PlotStyle centersStyle = new PlotStyle();
		centersStyle.setStyle(Style.POINTS);
		centersStyle.setLineType(NamedPlotColor.BLUE);
		DataSetPlot centersDS = new DataSetPlot(centers);
		centersDS.setTitle("Centra");
		centersDS.setPlotStyle(centersStyle);
		
		PlotStyle functionStyle = new PlotStyle();
		functionStyle.setStyle(Style.LINES);
		functionStyle.setLineType(NamedPlotColor.GREEN);
		DataSetPlot functionDS = new DataSetPlot(function);
		functionDS.setTitle("funkcja");
		functionDS.setPlotStyle(functionStyle);
		
		JavaPlot p = new JavaPlot();

		ImageTerminal png = new ImageTerminal();
		File file = new File(output + "/" + count + "/" + testNumber + chartImageExtension);
		try {
			file.createNewFile();
			png.processOutput(new FileInputStream(file));
		} catch (FileNotFoundException ex) {
			System.err.print(ex);
		} catch (IOException ex) {
			System.err.print(ex);
		}

		p.setTerminal(png);
		p.setTitle("N=" + String.valueOf(count) + " n=" + String.valueOf(learningParam));
		p.set("xlabel", "\"Iteracje\"");
		p.set("ylabel", "\"Blad sredniokwadratowy\"");
		p.set("key", "off");
		p.set("terminal", "png size 1024,768");

		p.addPlot(functionDS);
		p.addPlot(testSetDS);
		p.addPlot(centersDS);
		p.plot();

		try {
			ImageIO.write(png.getImage(), "png", file);
		} catch (IOException ex) {
			System.err.print(ex);
		}
	}
}
