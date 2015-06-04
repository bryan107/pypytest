package mfdr.math.emd.datastructure;

import java.util.Iterator;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class IMFS extends LinkedList<IMF> {

	public double totalEnergy(){
		double e = 0;
		Iterator<IMF> it = this.iterator();
		while (it.hasNext()) {
			IMF imf = (IMF) it.next();
			e += imf.energy();
		}
		return e;
	}
	
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
