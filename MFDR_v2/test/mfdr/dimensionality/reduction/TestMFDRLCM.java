package mfdr.dimensionality.reduction;

import java.util.LinkedList;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.PLAData;
import junit.framework.TestCase;

public class TestMFDRLCM extends TestCase {

	
	public void testLCM(){
		MFDRLCM mfdr = new MFDRLCM(2, 2);
		System.out.println(mfdr.lcm(2, 4));
		TimeSeries ts = new TimeSeries();
		generateResidual(ts, 2, 2, 60);
		LinkedList<PLAData> list1 = new LinkedList<PLAData>();
		LinkedList<PLAData> list2 = new LinkedList<PLAData>();
		list1.add(new PLAData(1, 1, 3));
		list1.add(new PLAData(31, 40, 1));
		list2.add(new PLAData(1, 1, 2));
		list2.add(new PLAData(21, 30, 3));
		list2.add(new PLAData(41, 60, 4));
		
		LinkedList<LinkedList<PLAData>> data = mfdr.getLCMPLADataList(list1, list2, 6, ts.timeLength());
		System.out.println("LIST1:");
		print(list1);
		System.out.println("LIST1-1:");
		print(data.get(0));
		System.out.println("LIST2:");
		print(list2);
		System.out.println("LIST2-2:");
		print(data.get(1));
	}
	
	private void print(LinkedList<PLAData> list){
		for(int i =0 ; i < list.size() ; i++){
           System.out.print("["+ i + "]" + " T:" + list.get(i).time() + " A0:" + list.get(i).a0()+ " A1:" + list.get(i).a1());
		}
		System.out.println();
	}
	
	private void generateResidual(LinkedList<Data> residual, double trendvariation, double noisevariation , long size) {
		for (double i = 0; i < size; i+=1) {
			java.util.Random r = new java.util.Random();
			double noise = r.nextGaussian() * Math.sqrt(noisevariation);
			double trend = trendvariation * Math.pow(i, 0.5);
			if(i > size/2){
				trend = -trend;
			}
//			double value = trend * Math.sin(i*Math.PI / 3);
			double value = noise + trend;
			residual.add(new Data(i, value));
		}
	}
}
