package org.apache.giraph.examples;

import java.util.ArrayList;
import java.util.List;

import org.apache.giraph.comm.messages.KeyPointMessage;
import org.apache.giraph.conf.LongConfOption;
import org.apache.giraph.edge.Edge;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.log4j.Logger;

public class KeyPointVertex extends
		Vertex<LongWritable, DoubleWritable, FloatWritable, KeyPointMessage> {

	private long NFID = -1; // -1 means init
	private long OFID = -1;
	// private long count = 0;

	// o
	public static List<LongWritable> vertexIdArray = new ArrayList<LongWritable>();

	

	public static LongConfOption SOURCE_ID;// = new LongConfOption(
//			"SimpleShortestPathsVertex.sourceId", 0);

	private static final Logger LOG = Logger
			.getLogger(SimpleShortestPathsVertex.class);

	private boolean isSource() {
		return getId().get() == SOURCE_ID.get(getConf());
	}
	
	public static void setSourceId(long sourceId) {
		SOURCE_ID = new LongConfOption("SimpleShortestPathsVertex.sourceId", sourceId);
	}
	
	private void reset() {
		NFID = -1; // -1 means init
		OFID = -1;
	}

	@Override
	public void compute(Iterable<KeyPointMessage> messages) {
		if (0 == getSuperstep()) {
			vertexIdArray.add(getId());
			voteToHalt();
			return;
		}
		if (isInital) {
			setValue(new DoubleWritable(Double.MAX_VALUE));
			reset();
			isInital = false;
		}
		long tem = -1;// temp for d
		double minDist = isSource() ? 0d : Double.MAX_VALUE;
		for (KeyPointMessage message : messages) {
			switch (message.getMessageType()) {
			case KeyPointMessage.DISMESSAGE:
				double te = Math.min(minDist, message.get());
				if (te < minDist) {
					minDist = te;
					tem = message.getSourceID();
				}
				break;
			case KeyPointMessage.ADDMESSAGE:
				++count;
				if (-1 != NFID && SOURCE_ID.get(getConf()) != NFID) {
					sendMessage(new LongWritable(NFID), new KeyPointMessage(
							getId().get(), KeyPointMessage.ADDMESSAGE, 0));
				}
				break;
			case KeyPointMessage.DECMESSAGE:
				--count;
				if (-1 != OFID && SOURCE_ID.get(getConf()) != OFID) {
					sendMessage(new LongWritable(OFID), new KeyPointMessage(
							getId().get(), KeyPointMessage.DECMESSAGE, 0));
				}
				break;
			}
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Vertex " + getId() + " got minDist = " + minDist
					+ " vertex value = " + getValue());
		}
		if (minDist < getValue().get()) {
			if (tem > -1) {
				// update father
				OFID = NFID;
				NFID = tem;
			}

			setValue(new DoubleWritable(minDist));
			for (Edge<LongWritable, FloatWritable> edge : getEdges()) {
				double distance = minDist + edge.getValue().get();
				if (LOG.isDebugEnabled()) {
					LOG.debug("Vertex " + getId() + " sent to "
							+ edge.getTargetVertexId() + " = " + distance);
				}
				sendMessage(edge.getTargetVertexId(), new KeyPointMessage(
						getId().get(), KeyPointMessage.DISMESSAGE, distance));
			}
			if (-1 != OFID && SOURCE_ID.get(getConf()) != OFID) {
				sendMessage(new LongWritable(OFID), new KeyPointMessage(getId()
						.get(), KeyPointMessage.DECMESSAGE, 0));
			}
			if (-1 != NFID && SOURCE_ID.get(getConf()) != NFID) {
				sendMessage(new LongWritable(NFID), new KeyPointMessage(getId()
						.get(), KeyPointMessage.ADDMESSAGE, 0));
			}
		}
		voteToHalt();
	}
}
