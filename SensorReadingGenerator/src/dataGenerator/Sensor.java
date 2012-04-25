package dataGenerator;
public class Sensor {
	//TODO change distance to Map for multiple events
	double distance;
	double noise; //reading = value +- value*noise

	public Sensor(double distance, double noise){
		updateDistance(distance);
	}
	
	public void updateDistance(double distance){
		this.distance = distance;
	}
	
	public void updateNoise(double noise){
		this.noise = noise;
	}
	
	public double getValue(double[][] eventpack){
		double value = 0;
		for(int i = 0 ; i < eventpack.length ; i++){
			value += getValue(eventpack[i][0], eventpack[i][1], eventpack[i][2]);
		}
		
		if(Math.random() > 0.5){
			value = value + value * noise;
		}else{
			value = value - value * noise;
		}
		return value;
	}
	
	public double getValue(double eventvalue, double diffusion, double constant){
		return eventvalue/Math.pow((constant*distance + 1), diffusion);
	}
}
