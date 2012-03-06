package correlationControl;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import faultDetection.correlationControl.Correlation;
import faultDetection.correlationControl.CorrelationManager;
import faultDetection.correlationControl.ReadingBuffer;
import faultDetection.tools.Caculator;
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
		CorrelationManager manager = new CorrelationManager(6);
		manager.putReading(1, 20);
		manager.putReading(2, 20);
		manager.updateCorrelations();
		manager.putReading(1, 21);
		manager.putReading(2, 20);
		manager.updateCorrelations();
		manager.putReading(1, 19);
		manager.putReading(2, 20);
		manager.updateCorrelations();
		manager.putReading(1, 20);
		manager.putReading(2, 20);
		manager.updateCorrelations();
		manager.putReading(1, 19);
		manager.putReading(2, 20);
		manager.updateCorrelations();
		Map<Integer,Map<Integer,Double>> correlationtable = manager.getCorrelationTable();
		
		
		Correlation correlation1 = new Correlation(5);
		correlation1.addPair(20, 20);
		correlation1.addPair(20, 21);
		correlation1.addPair(20, 19);
		correlation1.addPair(20, 20);
		correlation1.addPair(20, 19);
		
		Set<Integer> key = correlationtable.keySet();
		Iterator<Integer> iterator = key.iterator();
		for(Map<Integer,Double> i : correlationtable.values()){
			System.out.println("Size Y:" + i.size());
			int l = iterator.next();
			Set<Integer> key2 = i.keySet();
			Iterator<Integer> iterator2 = key2.iterator();
			for(Double j : i.values()){
				System.out.println("Node" + l + " + Node" + iterator2.next() + " = " + j);
				System.out.println("Direct: " + correlation1.getCorrelation());
			}
		}
	}
}
