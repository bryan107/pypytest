package processcontainers;

import cores.DataParser;
import cores.VAcore;

public class Container_1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] labels = {"G114D Cmortar 1", "G114D Cmortar 2", "Cellini Medusa", "G114E", "G114D", "G114C","G117 Bridge", "G117 C7"};
		
		DataParser p = new DataParser(
				"C:\\My Imperial (IRIS)\\Paper Writing\\SenSys_2013\\V&A Trace\\vam_trace_original\\vam trace\\2009\\",
				"C:\\My Imperial (IRIS)\\Paper Writing\\SenSys_2013\\V&A Trace\\vam_trace_original\\vam trace\\2009OceanData.csv",
				labels);
		p.run();
	}
}
