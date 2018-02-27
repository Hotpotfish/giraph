package org.apache.giraph.combiner;

import org.apache.hadoop.io.LongWritable;

/**
 * {@link Combiner} that finds the minimum {@link LongWritable}
 */
public class MinimumLongCombiner
    extends Combiner<LongWritable, LongWritable> {
  @Override
  public void combine(LongWritable vertexIndex, LongWritable originalMessage,
		  LongWritable messageToCombine) {
    if (originalMessage.get() > messageToCombine.get()) {
      originalMessage.set(messageToCombine.get());
    }
  }

  @Override
  public LongWritable createInitialMessage() {
    return new LongWritable(Long.MAX_VALUE);
  }
}