package mfdr.math.emd;
//package mdfr.core.emd;
//
//import java.util.Iterator;
//import java.util.LinkedList;
//
//import mdfr.math.emd.EMD;
//import mdfr.math.emd.datastructure.Data;
//import mdfr.math.emd.datastructure.IMFs;
//import mdfr.utility.Print;
//import junit.framework.TestCase;
//
//public class TestCalculateEnvelopes extends TestCase {
//
//	final long datasize = 100;
//	
//	public void testEMD(){
//		final int round = 5;
//		LinkedList<Data> residual = new LinkedList<Data>();
//		generateResidual(residual, datasize);
//		System.out.println("Original Data");
//		Print.getInstance().printDataLinkedList(residual);
//		EMD emd = new EMD(residual, 0.0001, 4,2,1);
//		IMFs imfs = emd.getIMFs(round);
//		Iterator<LinkedList<Data>> it = imfs.getIMFs().iterator();
//		for(int i = 0 ; it.hasNext();i++){
//			System.out.println("Level[" + i + "]");
//			Print.getInstance().printDataLinkedList(it.next());
//		}
//	}
//	
//	public void testGetEnvelopes(){
//		/*
//		LinkedList<Data> residual = new LinkedList<Data>();
//		LocalExtremas le = new LocalExtremas();
//		generateResidual(residual, datasize);
//		Print.getInstance().printDataLinkedList(residual);
//
//		//
//		le = Tools.getInstance().getLocalExtremas(residual);
//
//		//
//		System.out.println("Local Max");
//		Print.getInstance().printDataLinkedList(le.localMaxima());
//		System.out.println("Local Min");
//		Print.getInstance().printDataLinkedList(le.localMinima());
//		
//		//
//		Envelopes envelope = CalculateEnvelopes.getInstance().getEnvelopes(residual, le);
//		System.out.println("Upper Envelope");
//		Print.getInstance().printDataLinkedList(envelope.upperEnvelope());
//		System.out.println("Lower Envelope");
//		Print.getInstance().printDataLinkedList(envelope.lowerEnvelope());
//		
//		
//		System.out.println("Mean");
//		LinkedList<Data> mean = Tools.getInstance().getMean(envelope.upperEnvelope(), envelope.lowerEnvelope());
//		Print.getInstance().printDataLinkedList(mean);
//		
//		System.out.println("Difference:");
//		LinkedList<Data> difference = Tools.getInstance().getDifference(residual, mean);
//		Print.getInstance().printDataLinkedList(difference);
//		
//		System.out.println("Sum:");
//		LinkedList<Data> sum = Tools.getInstance().getSum(difference, mean);
//		Print.getInstance().printDataLinkedList(sum);
//		*/
//	}
//	
//	private void generateResidual(LinkedList<Data> residual, long size){
//		for(double i = 0 ; i < size ; i++){
//			double value = 9.5 * Math.sin(i/3) + Math.random();
//			residual.add(new Data(i,value));	
//		}
//	}
//	
//}
