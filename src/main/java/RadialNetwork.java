import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class RadialNetwork {
	public static final String SET_FILENAME = "zbior1.txt";
	private Neuron output;
	private List<RadialNeuron> hidden;
	private double[][] trainingSet;
	private List<Integer> indexes;
	
	public RadialNetwork(int neurons, double learningParam) throws IOException {
		trainingSet = getTrainingSetFromFile(SET_FILENAME);
		output = new Neuron(neurons, learningParam, 0);
		hidden = new ArrayList<RadialNeuron>();
		indexes = new ArrayList<Integer>();

		for(int i = 0; i < neurons; i++){
			Random random = new Random();
			boolean added = false;
			while(added == false) {
				int index = random.nextInt(trainingSet.length);
				if (indexNotInList(indexes, index)){
					indexes.add(index);
					added = true;
				}
			}
		}

		for (int i = 0; i < neurons; i++) {
			hidden.add(new RadialNeuron(trainingSet[indexes.get(i)][0], getParam(trainingSet[indexes.get(i)][0])));
		}
	}

    public void teachOutput(double x, double error){
        output.updateWeights(getHiddenLayerOutput(x), error);
    }

	private double[] getHiddenLayerOutput(double x){
		double[] output = new double[hidden.size()];
		for (int i = 0; i < hidden.size(); i++){
			output[i] = hidden.get(i).getOutput(x);
		}
		return output;
	}

	public double getOutput(double x){
		return output.getOutput(getHiddenLayerOutput(x));
	}

	private boolean indexNotInList(List<Integer> indexes, int index){
		for (Integer i : indexes){
			if (i == index){
				return false;
			}
		}
		return true;
	}

	private double getParam(double c){
		double[] ci = getTwoNearestNeurons(c);
		return 0.5 * Math.sqrt(Math.abs(c-ci[0]) * Math.abs(c-ci[0]) + Math.abs(c-ci[1]) * Math.abs(c-ci[1]));
	}

	private double[] getTwoNearestNeurons(double c){
		double[] nearest = new double[2];
		nearest[0] = trainingSet[0][0];
		for (int i = 0; i < trainingSet.length; i++){
			if(Math.abs(c - trainingSet[i][0]) < Math.abs(c - nearest[0]) && c != trainingSet[i][0]){
				nearest[0] = trainingSet[i][0];
			}
		}
		if(nearest[0] == trainingSet[0][0]){
			nearest[1] = trainingSet[1][0];
		}else{
			nearest[1] = trainingSet[0][0];
		}
		for (int i = 0; i < trainingSet.length; i++){
			if(Math.abs(c - trainingSet[i][0]) < Math.abs(c - nearest[0]) && nearest[0] != trainingSet[i][0] && c != trainingSet[i][0]){
				nearest[1] = trainingSet[i][0];
			}
		}
		return nearest;
	}

	private double[][] getTrainingSetFromFile(String filePath) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		int lines = 0;
		while (reader.readLine() != null){
			lines++;
		}
		reader.close();

		Scanner input = null;
		try {
			input = new Scanner (new File(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		double[][] trainingSet = new double[lines][2];
		int i = 0;
		while(input.hasNextLine())
		{
			Scanner colReader = new Scanner(input.nextLine());
			colReader.useLocale(Locale.UK);
			int j = 0;
			while(colReader.hasNextDouble()){
				trainingSet[i][j] = colReader.nextDouble();
				j++;
			}
			i++;
		}
		return trainingSet;
	}

	public Neuron getOutput() {
		return output;
	}

	public void setOutput(Neuron output) {
		this.output = output;
	}

	public List<RadialNeuron> getHidden() {
		return hidden;
	}

	public void setHidden(List<RadialNeuron> hidden) {
		this.hidden = hidden;
	}

	public double[][] getTrainingSet() {
		return trainingSet;
	}

	public void setTrainingSet(double[][] trainingSet) {
		this.trainingSet = trainingSet;
	}

    public List<Integer> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Integer> indexes) {
        this.indexes = indexes;
    }
}
