package org.apache.giraph.examples;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.giraph.conf.ImmutableClassesGiraphConfigurable;
import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.giraph.edge.Edge;
import org.apache.giraph.edge.EdgeFactory;
import org.apache.giraph.graph.Vertex;
import org.apache.giraph.io.formats.TextVertexInputFormat;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import com.google.common.collect.Lists;

/**
 * Input format for unweighted graphs with long ids and double vertex values
 */
public class LLNTIF
    extends TextVertexInputFormat<LongWritable, LongWritable, NullWritable>
    implements ImmutableClassesGiraphConfigurable<LongWritable, LongWritable,
    NullWritable, Writable> {
  /** Configuration. */
  private ImmutableClassesGiraphConfiguration<LongWritable, LongWritable,
      NullWritable, Writable> conf;

  @Override
  public TextVertexReader createVertexReader(InputSplit split,
                                             TaskAttemptContext context)
    throws IOException {
    return new LongLongNullLongVertexReader();
  }

  @Override
  public void setConf(ImmutableClassesGiraphConfiguration<LongWritable,
		  LongWritable, NullWritable, Writable> configuration) {
    this.conf = configuration;
  }

  @Override
  public ImmutableClassesGiraphConfiguration<LongWritable, LongWritable,
      NullWritable, Writable> getConf() {
    return conf;
  }

  /**
   * Vertex reader associated with
   * {@link LongLongNullTextInputFormat}.
   */
  public class LongLongNullLongVertexReader extends
      TextVertexInputFormat<LongWritable, LongWritable,
          NullWritable>.TextVertexReader {
    /** Separator of the vertex and neighbors */
    private final Pattern separator = Pattern.compile("\t");

    @Override
    public Vertex<LongWritable, LongWritable, NullWritable, ?>
    getCurrentVertex() throws IOException, InterruptedException {
      Vertex<LongWritable, LongWritable, NullWritable, ?>
          vertex = conf.createVertex();

      String[] tokens =
          separator.split(getRecordReader().getCurrentValue().toString());
      List<Edge<LongWritable, NullWritable>> edges =
          Lists.newArrayListWithCapacity(tokens.length - 1);
      for (int n = 1; n < tokens.length; n++) {
        edges.add(EdgeFactory.create(
            new LongWritable(Long.parseLong(tokens[n])),
            NullWritable.get()));
      }

      LongWritable vertexId = new LongWritable(Long.parseLong(tokens[0]));
      vertex.initialize(vertexId, new LongWritable(), edges);

      return vertex;
    }

    @Override
    public boolean nextVertex() throws IOException, InterruptedException {
      return getRecordReader().nextKeyValue();
    }
  }
}
