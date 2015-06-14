public class RadialNeuron {
	private double c;
	private double param;
	
	public RadialNeuron(double c, double param){
		this.c = c;
		this.param = param;
	}
	
	public double getOutput(double x){
		return Math.exp(-0.5 * (Math.abs(x - c)/param) * (Math.abs(x - c)/param));
	}
}
