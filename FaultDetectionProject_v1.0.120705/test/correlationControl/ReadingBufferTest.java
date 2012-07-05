package correlationControl;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import faultDetection.correlationControl.Correlation;
import faultDetection.correlationControl.CorrelationManager;
import faultDetection.correlationControl.ReadingBuffer;
import junit.framework.TestCase;

public class ReadingBufferTest extends TestCase {

	public void testReadingBuffer(){
		ReadingBuffer buffer = new ReadingBuffer();
		buffer.putBufferData(1, 20);
		buffer.putBufferData(2, 21);
		buffer.putBufferData(3, 22);
		buffer.putBufferData(4, 23);
		buffer.putBufferData(5, 24);
		buffer.putBufferData(6, 25);
		
		Map<Integer, Double> bufferdata = buffer.getBufferData();
		
		System.out.println(bufferdata.get(1));
		
		System.out.println("Buffer Data = ");
		for(double i : bufferdata.values()){
			System.out.print(i + " ");
		}
		System.out.println();
		System.out.println("BufferTestFinish");
		
	}
	public void testCorrelationManager(){
		
		double[] reading1 = {20,21,19,20,19};
		double[] reading2 = {20,20,20,20,20};
		double[] reading3 = {22,24,23,20,21};
		
		CorrelationManager manager = new CorrelationManager(5,1,0.5, 0.1);
		for(int i = 0 ; i < 5 ; i++){
			manager.putReading(1, reading1[i]);
			manager.putReading(2, reading2[i]);
			manager.putReading(3, reading3[i]);
			manager.updateCorrelations();
		}
		Map<Integer,Map<Integer,Double>> correlationtable = manager.getCorrelationTrendTable();
		
		Correlation correlation1 = new Correlation(5);
		Correlation correlation2 = new Correlation(5);
		Correlation correlation3 = new Correlation(5);
		for(int i = 0 ; i < 5 ; i++){
			correlation1.addPair(reading1[i], reading2[i]);
			correlation2.addPair(reading2[i], reading3[i]);
			correlation3.addPair(reading1[i], reading3[i]);
		}
		
		Set<Integer> key = correlationtable.keySet();
		Iterator<Integer> iterator = key.iterator();
		for(Map<Integer,Double> i : correlationtable.values()){
//			System.out.println("Size Y:" + i.size());
			int l = iterator.next();
			Set<Integer> key2 = i.keySet();
			Iterator<Integer> iterator2 = key2.iterator();
			int x = 0;
			for(Double j : i.values()){
				System.out.println("Node" + l + " + Node" + iterator2.next() + " = " + j);
			}
		}
		System.out.println("Direct1: " + correlation1.getCorrelation());
		System.out.println("Direct2: " + correlation2.getCorrelation());
		System.out.println("Direct3: " + correlation3.getCorrelation());
	}
}
