package org.apache.giraph.examples;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;

public class MyMessageWritable extends DoubleWritable {
	private long sourceID = -2;
	static private int i = 0;
	private int j = -1;
	
	public MyMessageWritable() {
		super();
	}
	
	public MyMessageWritable(long ID, double distance) {
		super(distance);
		sourceID = ID;
		j = i;
		i++;
		System.out.println("used " + ID + "    " + j);
	}
	public long getSourceID() {
		System.out.println("used get ID " + sourceID + "    " + j);
		return sourceID;
	}
	

	
}
