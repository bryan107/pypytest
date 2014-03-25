package kernelfunction.core;

import java.util.LinkedList;

import smartgrid.resultanalyser.AttackInfo;
import junit.framework.TestCase;

public class TestDataParcer extends TestCase {

	public void test(){
		String str = "[1]:65 [5]:309 [7]:312 [12]:342";
		String[] strsplit = str.split(" ");
		LinkedList<AttackInfo> strmap = new LinkedList<AttackInfo>();
		for(String s : strsplit){
			String[] temp = s.split(":");
			int nodeid = Integer.valueOf(temp[0].substring(1, temp[0].length()-1));
			int round = Integer.valueOf(temp[1]);
			strmap.add(new AttackInfo(nodeid, round));
		}
		for(AttackInfo atinfo: strmap){
			System.out.println("[" + atinfo.nodeid() + "]: " + atinfo.round());
		}
	}
}
