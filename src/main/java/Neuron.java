import java.util.Random;

public class Neuron {
	private double learningParam; 
	private double momentumParam;
	private double[] weights;
	
	public double getLearningParam() {
		return learningParam;
	}
	public void setLearningParam(double learningParam) {
		this.learningParam = learningParam;
	}
	public double getMomentumParam() {
		return momentumParam;
	}
	public void setMomentumParam(double momentumParam) {
		this.momentumParam = momentumParam;
	}
	public double[] getWeights() {
		return weights;
	}
	public void setWeights(double[] weights) {
		this.weights = weights;
	}
	
	public Neuron (int inputs, double learningParam, double momentumParam){
		Random r = new Random();
		this.learningParam = learningParam; 
		this.momentumParam = momentumParam;
		this.weights = new double[inputs];
		for (int i = 0; i < weights.length; i++){
			weights[i] = r.nextDouble();
		}
	}
	
	public double getOutput(double[] x){
		double sum = 0.0d;
		for (int i = 0; i < weights.length; i++){
			sum += weights[i] * x[i];
		}
		return sum;
	}
	
	public void updateWeights(double[] input, double error){
		for (int i = 0; i < weights.length; i++){
			weights[i] = weights[i] + learningParam * error * input[i];
		}
	}
}
