package org.apache.giraph.examples;

import java.io.IOException;

import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;

/**
 * Weakly Connected Components Algorithm
 * 
 * @author baisong
 *
 */
public class WeaklyConnectedComponentsVertex extends Vertex<LongWritable,
    LongWritable, NullWritable, LongWritable> {
  /**
   * Propagates the smallest vertex id to all neighbors. Will always choose to
   * halt and only reactivate if a smaller id has been sent to it.
   *
   * @param messages Iterator of messages from the previous superstep.
   * @throws IOException
   */
  @Override
  public void compute(Iterable<LongWritable> messages) throws IOException {
	  if(getSuperstep()==0) {
		  setValue(getId());
		}
	  long minValue=getValue().get();
	  for(LongWritable msg:messages) {
		  if(msg.get()<minValue) {
			  minValue=msg.get();
		  }
	  }
	  if(getSuperstep()==0 || minValue<getValue().get()) {
		  setValue(new LongWritable(minValue));
		  sendMessageToAllEdges(new LongWritable(minValue));
	  }
	  voteToHalt();    
  }
}