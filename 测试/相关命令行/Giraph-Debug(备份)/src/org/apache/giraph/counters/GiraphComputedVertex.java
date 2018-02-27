package org.apache.giraph.counters;  
  
import java.util.Iterator;  
import java.util.Map;  
  
import org.apache.hadoop.mapreduce.Mapper.Context;  
import com.google.common.collect.Maps;  
  
/** 
 * Hadoop Counters in group "Giraph Messages" for counting every superstep 
 * message count. 
 */  
  
public class GiraphComputedVertex extends HadoopCountersBase {  
    /** Counter group name for the giraph Messages */  
    public static final String GROUP_NAME = "Giraph Computed Vertex";  
  
    /** Singleton instance for everyone to use */  
    private static GiraphComputedVertex INSTANCE;  
  
    /** superstep time in msec */  
    private final Map<Long, GiraphHadoopCounter> superstepVertexCount;  
  
    private GiraphComputedVertex(Context context) {  
        super(context, GROUP_NAME);  
        superstepVertexCount = Maps.newHashMap();  
    }  
  
    /** 
     * Instantiate with Hadoop Context. 
     *  
     * @param context 
     *            Hadoop Context to use. 
     */  
    public static void init(Context context) {  
        INSTANCE = new GiraphComputedVertex(context);  
    }  
  
    /** 
     * Get singleton instance. 
     *  
     * @return singleton GiraphTimers instance. 
     */  
    public static GiraphComputedVertex getInstance() {  
        return INSTANCE;  
    }  
  
    /** 
     * Get counter for superstep messages 
     *  
     * @param superstep 
     * @return 
     */  
    public GiraphHadoopCounter getSuperstepVertexCount(long superstep) {  
        GiraphHadoopCounter counter = superstepVertexCount.get(superstep);  
        if (counter == null) {  
            String counterPrefix = "Superstep: " + superstep+" ";  
            counter = getCounter(counterPrefix);  
            superstepVertexCount.put(superstep, counter);  
        }  
        return counter;  
    }  
  
    @Override  
    public Iterator<GiraphHadoopCounter> iterator() {  
        return superstepVertexCount.values().iterator();  
    }  
}  
