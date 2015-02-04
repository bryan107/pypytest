package mdfr.core.emd;

import java.util.LinkedList;

import mdfr.develop.toos.Print;
import mdfr.math.emd.CalculateEnvelopes;
import mdfr.math.emd.Envelopes;
import mdfr.math.emd.LocalExtremas;
import mdfr.math.emd.Tools;
import junit.framework.TestCase;

public class TestCalculateEnvelopes extends TestCase {
/*
	public void testGetEnvelopes(){
		LinkedList<Double> residual = new LinkedList<Double>();
		LocalExtremas le = new LocalExtremas();
		generateResidual(residual, 20);
		le = Tools.getInstance().getLocalExtremas(residual);
		
		//
		Print.getInstance().setupFormat("0.00");
		Print.getInstance().printLinkedList(residual);
		
		//
		Print.getInstance().setupFormat("0");
		System.out.println("Local Max");
		Print.getInstance().printLinkedList(le.localMaxima());
		System.out.println("Local Min");
		Print.getInstance().printLinkedList(le.localMinima());
		
		//
		Print.getInstance().setupFormat("0.00");
		Envelopes envelope = CalculateEnvelopes.getInstance().getEnvelopes(residual, le);
		System.out.println("Upper Envelope");
		Print.getInstance().printLinkedList(envelope.upperEnvelope());
		System.out.println("Lower Envelope");
		Print.getInstance().printLinkedList(envelope.lowerEnvelope());
		
		//
		System.out.println("Mean");
		LinkedList<Double> mean = Tools.getInstance().getMean(envelope.upperEnvelope(), envelope.lowerEnvelope());
		Print.getInstance().printLinkedList(mean);
		System.out.println("Difference:");
		LinkedList<Double> difference = Tools.getInstance().getDifference(residual, mean);
		Print.getInstance().printLinkedList(difference);
	}
	*/
	private void generateResidual(LinkedList<Double> residual, int size){
		for(int i = 0 ; i < size ; i++){
			double value;
			if(i % 5 == 0)
				value = 4.5 + 1*Math.random();
			else
				value = 1.5 + 1*Math.random();
			residual.add(value);	
		}
	}
	
}
