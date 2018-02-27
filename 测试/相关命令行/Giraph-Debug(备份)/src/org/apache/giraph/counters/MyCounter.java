package org.apache.giraph.counters;

import java.util.Iterator;  
import java.util.Map;  
  
import org.apache.hadoop.mapreduce.Mapper.Context;  
import com.google.common.collect.Maps;  
import java.util.Iterator;

public class MyCounter extends HadoopCountersBase {
	/** Counter group name for the giraph Messages */  
    public static final String GROUP_NAME = "Giraph Messages";  
  
    /** Singleton instance for everyone to use */  
    private static MyCounter INSTANCE;  
  
    /** superstep time in msec */  
    private final Map<Long, GiraphHadoopCounter> superstepMessages;  
  
    private MyCounter(Context context) {  
        super(context, GROUP_NAME);  
        superstepMessages = Maps.newHashMap();  
    }  
  
    /** 
     * Instantiate with Hadoop Context. 
     *  
     * @param context 
     *            Hadoop Context to use. 
     */  
    public static void init(Context context) {  
        INSTANCE = new MyCounter(context);  
    }  
  
    /** 
     * Get singleton instance. 
     *  
     * @return singleton GiraphTimers instance. 
     */  
    public static MyCounter getInstance() {  
        return INSTANCE;  
    }  
  
    /** 
     * Get counter for superstep messages 
     *  
     * @param superstep 
     * @return 
     */  
    public GiraphHadoopCounter getSuperstepMessages(long superstep) {  
        GiraphHadoopCounter counter = superstepMessages.get(superstep);  
        if (counter == null) {  
            String counterPrefix = "Superstep- " + superstep+" ";  
            counter = getCounter(counterPrefix);  
            superstepMessages.put(superstep, counter);  
        }  
        return counter;  
    }  
  
    @Override  
    public Iterator<GiraphHadoopCounter> iterator() {  
        return superstepMessages.values().iterator();  
    }  

}

  
