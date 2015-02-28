package mdfr.math.emd.datastructure;

import java.util.Iterator;
import java.util.LinkedList;

public class IMFS extends LinkedList<IMF> {

	public double totalEnergyDensity(){
		double ed = 0;
		Iterator<IMF> it = this.iterator();
		while (it.hasNext()) {
			IMF imf = (IMF) it.next();
			ed += imf.energyDensity();
		}
		return ed;
	}
}
