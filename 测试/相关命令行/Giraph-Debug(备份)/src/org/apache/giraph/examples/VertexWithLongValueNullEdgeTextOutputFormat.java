package org.apache.giraph.examples;

import java.io.IOException;

import org.apache.giraph.graph.Vertex;
import org.apache.giraph.io.formats.TextVertexOutputFormat;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 * Output format for vertices with a long as id, a double as value and
 * null edges
 */
public class VertexWithLongValueNullEdgeTextOutputFormat extends
    TextVertexOutputFormat<LongWritable, LongWritable, NullWritable> {
  @Override
  public TextVertexWriter createVertexWriter(TaskAttemptContext context)
    throws IOException, InterruptedException {
    return new VertexWithDoubleValueWriter();
  }

  /**
   * Vertex writer used with
   * {@link VertexWithLongValueNullEdgeTextOutputFormat}.
   */
  public class VertexWithDoubleValueWriter extends TextVertexWriter {
    @Override
    public void writeVertex(
        Vertex<LongWritable, LongWritable, NullWritable, ?> vertex)
      throws IOException, InterruptedException {
      StringBuilder output = new StringBuilder();
      output.append(vertex.getId().get());
      output.append('\t');
      output.append(vertex.getValue().get());
      getRecordWriter().write(new Text(output.toString()), null);
    }
  }
}
