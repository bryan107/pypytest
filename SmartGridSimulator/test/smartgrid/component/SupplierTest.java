package smartgrid.component;

import fileAccessInterface.PropertyAgent;
import junit.framework.TestCase;

public class SupplierTest extends TestCase {

	public void testSupplyValue(){
		Supplier s = new Supplier(9999999, 0, 5, 200, new PatternStable(), 20, null, 0.01, new FaultNull());
		for(int i = 0 ; i < 200 ; i+=10){
			System.out.println("Round[" + i + "]: " + s.supplyValue(365, i));
		}
		double impactvalue = Double.valueOf(PropertyAgent.getInstance().getProperties("SET", "Supplier.Fault.ImpactValue"));
		s.updateFault(new FaultDeviationProportion(impactvalue));
		for(int i = 200 ; i < 365 ; i+=10){
			System.out.println("Round[" + i + "]: " + s.supplyValue(365, i));
		}
	}
}
