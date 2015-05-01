package mfdr.utility;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.Data;
import mfdr.dimensionality.datastructure.PAAData;
import mfdr.dimensionality.datastructure.PLAData;

public class Print {
	private static Log logger = LogFactory.getLog(Print.class);
	DecimalFormat valuedf = new DecimalFormat("0.0000");
	DecimalFormat timedf = new DecimalFormat("0.00");

	private static Print self = new Print();

	private Print() {

	}

	public static Print getInstance() {
		return self;
	}

	public void setupValueFormat(String st) {
		this.valuedf = new DecimalFormat(st);
	}

	public void setupTimeFormat(String st) {
		this.timedf = new DecimalFormat(st);
	}

	public void printDataLinkedList(LinkedList<Data> list, long size) {
		try {
			Iterator<Data> it = list.iterator();
			int count = 0;
			while (it.hasNext()) {
				if (count > size)
					break;
				count++;
				Data data = it.next();
				System.out.print("[" + timedf.format(data.time()) + "]:"
						+ valuedf.format(data.value()) + " ");
			}
			System.out.println();
		} catch (Exception e) {
			logger.info("The Listlist is empty, not printable" + e);
		}
	}

	public void printDoubleLinkedList(LinkedList<Double> list, long size) {
		Iterator<Double> it = list.iterator();
		int count = 0;
		while (it.hasNext()) {
			if (count > size)
				break;
			count++;
			Double data = it.next();
			System.out.print(valuedf.format(data) + " ");
		}
		System.out.println();
	}

	public void printPLADataLinkedList(LinkedList<PLAData> list, long size) {
		try {
			Iterator<PLAData> it = list.iterator();
			int count = 0;
			while (it.hasNext()) {
				if (count > size)
					break;
				count++;
				PLAData data = it.next();
				System.out.print("[" + timedf.format(data.time()) + "]:"
						+ valuedf.format(data.a0()) + " , "
						+ valuedf.format(data.a1()) + " ");
			}
			System.out.println();
		} catch (Exception e) {
			logger.info("The Listlist is empty, not printable" + e);
		}
	}
	
	public void printPAADataLinkedList(LinkedList<PAAData> list, long size) {
		try {
			Iterator<PAAData> it = list.iterator();
			int count = 0;
			while (it.hasNext()) {
				if (count > size)
					break;
				count++;
				PAAData data = it.next();
				System.out.print("[" + timedf.format(data.time()) + "]:"
						+ valuedf.format(data.average()) + " ");
			}
			System.out.println();
		} catch (Exception e) {
			logger.info("The Listlist is empty, not printable" + e);
		}
	}

	public void printArray(double[] array, long size) {
		for (int i = 0; i < array.length && i < size; i++) {
			System.out.print(array[i] + ", ");
		}
		System.out.println();
	}

}
