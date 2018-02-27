package org.apache.giraph.comm.messages;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;

public class KeyPointMessage extends DoubleWritable {
	
	private long sourceID;
	private int messageType; //0 default distance 1-- 2++
	
	public static final int DISMESSAGE = 0;
	public static final int ADDMESSAGE = 1;
	public static final int DECMESSAGE = 2;
	
	public KeyPointMessage () {
		super();
	}
	
	public KeyPointMessage (long sourceID, int messageType, double distance) {
		super(distance);
		this.messageType = messageType;
		this.sourceID = sourceID;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		super.readFields(in);
		this.messageType = in.readInt();
		this.sourceID = in.readLong();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		super.write(out);
		out.writeInt(messageType);
		out.writeLong(sourceID);
	}

	public long getSourceID() {
		return sourceID;
	}
	
	public int getMessageType() {
		return messageType;
	}
	
	
}
