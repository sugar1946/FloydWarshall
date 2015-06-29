
package org.cisc475.shortestpath.hamr;

import java.awt.List;

import java.io.IOException;
import java.io.PrintStream;

import org.kohsuke.args4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.etinternational.hamr.HAMR;
import com.etinternational.hamr.core.*;
import com.etinternational.hamr.resource.file.*;
import com.etinternational.hamr.resource.hdfs.*;
import com.etinternational.hamr.resource.ResourceWriter;
import com.etinternational.hamr.resource.file.FileResourceReader;
import com.etinternational.hamr.resource.util.NewLineRecordReader;
import com.etinternational.hamr.transform.Transform;



import com.etinternational.hamr.io.Serialization;

import com.etinternational.hamr.resource.file.FileResourceWriter;
import com.etinternational.hamr.resource.util.NewLineRecordReader;
import com.etinternational.hamr.store.list.HashListContainer;
import com.etinternational.hamr.store.list.TreeListContainer;
import com.etinternational.hamr.store.list.ValueList;
import com.etinternational.hamr.store.list.ValueListContainer;
import com.etinternational.hamr.store.list.ValueListStore;
import com.etinternational.hamr.store.number.NumberStore;
import com.etinternational.hamr.store.single.HashContainer;
import com.etinternational.hamr.store.single.ValueStore;
import com.etinternational.hamr.transform.Transform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;


@SuppressWarnings("unused")
public class FloydWarshall {

    
	private String outputFile = "TestOutput.txt";
	static final public int numHosts = 4; 
	static public ValueList<Weight> shareValue = null;
	static public int i = 0;
   	private static final String HADOOP_HOME = System.getenv("HADOOP_HOME");
	private static final String HDFS_SITE_CONFIG = "file://" + HADOOP_HOME + "/etc/hadoop/core-site.xml";
	private static Map<Integer, Double> shareValueMap = new HashMap<>();
	private static final String KV_OUTPUT_FORMAT = "%2$s : %1$s\n"; // Value : Key
	private double distancematrix[][];
    public static final int INFINITY = 999;
    private static enum FileSystemType {
        HDFS,
        POSIX
    };

  
    
    /**
     * Execute 
     */
    
    @Option(name="-f",usage="input file (REQUIRED)",required=true,metaVar="FILE")
    private String inputFile = null;
    
  //@Option(name="-v",usage="number of vertices (REQUIRED)",required=true,metaVar="NUMBER")
    private int numberofvertices = 0;

    @Option(name="-h",usage="print this message",help=true,aliases={"--help"})
    private boolean displayUsage = false;
    
//    @Option(name="-o",usage="output file (REQUIRED)",required=true,metaVar="FILE")
//    private String outputFile = null;

    @Option(name="-z",usage="value of $HADOOP_HOME",aliases={"--hadoopHome="},metaVar="STRING")
    private String hadoopHome = HADOOP_HOME;

    @Option(name="-t",usage="file system type, default=HDFS",aliases={"--fileSystemType="})
    private FileSystemType fileSystem = FileSystemType.HDFS;
    
    public void run(){
    	org.apache.hadoop.conf.Configuration hdfsConfig = null;

    	    	
    	try{
    		//Create the instance of HAMR
    		HAMR hamr = HAMR.initialize();
    		    		
    		//Create a weight class
    		//Create a new seiralizer
    		//Register serializer
    		hamr.getSerializationRegistry().register(new WeightSerialization());
    		
    		
    		//Create and name the job to run.
    		Job initJob = new Job("Initialization");
    		initJob.setPartitionCount(1);
    		
    		//Create a domain for reading.
    		Domain inputDomain = new Domain(initJob);
    		//inputDomain.setPartitionCount(1);
    		
    		//Create the Resource Reader.
    		NewLineRecordReader lineRecordReader = new NewLineRecordReader();
    		FileResourceReader<Long, String> reader = new FileResourceReader<>(inputDomain,lineRecordReader,inputFile);
    		
    		//Define and create a Transform 
    		//Produce a graph
    		final Transform<Long,String,Integer,Weight> parse = new Transform<Long,String,Integer,Weight>(inputDomain){
    			@Override
    			public void apply(Long key,String value,PartitionConnector context) throws IOException{
    				String[] temp = value.split(" ");	
    				int source = Integer.parseInt(temp[0]);
    				double weight = Double.parseDouble(temp[1]);
    				int destination = Integer.parseInt(temp[2]);
    				
    				//Create a structure that store the data
    				Weight valueSource = new Weight(destination, weight);
    				    				
    				// Put the value into the transform
    				context.push(out(), source, valueSource);    				
    			}
    		};
    				
    		// Create a Number Store to perform a partial sum of the occurrences of the keys
    		// (words) locally as a combiner would.
    		Domain edgesDomain = new Domain(initJob, "edges");
		
    		ValueListContainer<Integer, Weight> edgesContainer = new HashListContainer<Integer,Weight>();
    		ValueListStore<Integer, Weight> Edges = new ValueListStore<>(edgesDomain, edgesContainer);
    		
    		final Transform<Integer,ValueList<Weight>,Integer, ValueList<Weight>> broadcast = new Transform<Integer,ValueList<Weight>,Integer, ValueList<Weight>>(edgesDomain){
    			@Override
    			public void apply(Integer key,ValueList<Weight> value,PartitionConnector context) throws IOException{
    				if (key == 0)
    					for (int i = 0; i < numHosts; i++)
    						context.push(out(), i, value); // value won't be store in the same node
    			}
    		};
    		
    		Domain broadcastDomain = new Domain(initJob,"broadcast");
    		
    		final Transform<Integer,ValueList<Weight>,Integer, ValueList<Weight>> receiver = new Transform<Integer,ValueList<Weight>,Integer, ValueList<Weight>>(broadcastDomain){
    			@Override
    			public void apply(Integer key,ValueList<Weight> value,PartitionConnector context) throws IOException{
                    shareValue = value;
    			}
    		};
    		
    		reader.out().bind(parse.in());
    		parse.out().bind(Edges.add());
    		Edges.list().bind(broadcast.in());
    		broadcast.out().bind(receiver.in());
            hamr.runJob(initJob);
    		
    	    for (i = 0; i < numberofvertices; i++) { // i = intermediate node
    	    	Job iterJob = new Job("Iteration " + i);
                iterJob.setPartitionCount(1);
                for (int q=0;q<shareValue.size();q++){
                    shareValueMap.put(shareValue.get(q).destination, shareValue.get(q).weight);
                }
                Domain algorithmDomain = new Domain(iterJob,"algorithmDomain");
    	    	ValueListStore<Integer, Weight> iterEdges = new ValueListStore<>(algorithmDomain, edgesContainer);
    	    	Transform<Integer,ValueList<Weight>,Integer, Weight> algorithm = new Transform<Integer,ValueList<Weight>,Integer, Weight>(algorithmDomain){
        			@Override
        			public void apply(Integer key,ValueList<Weight> value,PartitionConnector context) throws IOException{
                        Map<Integer, Double> valueMap = new HashMap<>();
                        for (int q=0;q<value.size();q++){
        					valueMap.put(value.get(q).destination, value.get(q).weight);
                        }
					
        				for (int j = 0; j < numberofvertices; j++) {
                            if(key!=j){
                                if(valueMap.get(i)==null || shareValueMap.get(j)==null){
                                    if(valueMap.get(j) != null){
                                        context.push(out(),key,new Weight(j,valueMap.get(j)));
                                    }
				///Mingzhi Yu Last change //////
				    else if(valueMap.get(j) == null){
                                        context.push(out(),key,new Weight(j,999));
                                    }
				////////////////////////////////////////////////
                                }
                                else{
                                    if(valueMap.get(j)!= null){
                                        if(valueMap.get(i) + shareValueMap.get(j) < valueMap.get(j)){
                                            System.out.println("if flag two");
                                            Weight newweight = new Weight(j,valueMap.get(i) + shareValueMap.get(j));
                                            context.push(out(),key,newweight);
                                        }
                                        else {
                                            context.push(out(),key,new Weight(j,valueMap.get(j)));
                                        }
                                    }
                            
                                    else {
                                    	Weight newweight = new Weight(j,valueMap.get(i) + shareValueMap.get(j));
                                    	context.push(out(),key,newweight);
                                    }
                                }
                            }                      
                            else{
                            	context.push(out(),key, new Weight(j,0));
                            }
        			    }
        			}			
        		};        	
        		
        		
        		//update the value
        		Domain newDomain = new Domain(iterJob,"newDomain");
        		ValueListContainer<Integer, Weight> updateEdgesContainer = new HashListContainer<Integer,Weight>();
        		ValueListStore<Integer, Weight> updateEdges = new ValueListStore<>(newDomain, updateEdgesContainer);
        	
       		
        		//update the shareValue
        		Transform<Integer,ValueList<Weight>,Integer, ValueList<Weight>> updateShareValue = new Transform<Integer,ValueList<Weight>,Integer, ValueList<Weight>>(newDomain){
        			@Override
        			public void apply(Integer key,ValueList<Weight> value,PartitionConnector context) throws IOException{        			
        				if (key == i+1){
        					for (int m = 0; m < numHosts; m++){
        						context.push(out(), m, value); 
        					}
        				}// value won't be store in the same node
        			}
        		};
        	
        		Domain newbroadcastDomain = new Domain(iterJob,"newbroadcast");
    		
        		Transform<Integer,ValueList<Weight>,Integer, ValueList<Weight>> newreceiver = new Transform<Integer,ValueList<Weight>,Integer, ValueList<Weight>>(newbroadcastDomain){
        			@Override
        			public void apply(Integer key,ValueList<Weight> value,PartitionConnector context) throws IOException{
        				shareValue = value; 
        			}
        		};


        		iterEdges.list().bind(algorithm.in());
        		algorithm.out().bind(updateEdges.add());


        		updateEdges.list().bind(updateShareValue.in());
        		updateShareValue.out().bind(newreceiver.in());
        		hamr.runJob(iterJob);
        		edgesContainer = updateEdgesContainer;
        		
    	    }
    	    
    	    // Create the Resource Writer.
            // The format for converting the key/value pair 
            // into a String is specified.
            Job outputJob = new Job("outputJob");	
    	    Domain outputDomain = new Domain(outputJob,"outputDomain");


            Transform<Integer,ValueList<Weight>,Integer, String> translator = new Transform<Integer,ValueList<Weight>,Integer,String>(outputDomain){
    			@Override
    			public void apply(Integer key,ValueList<Weight> value,PartitionConnector context) throws IOException{
    				for (Weight weight : value){
    					//System.out.println(weight.weight + " " +  weight.destination);
    					String temp = key.toString()+ " " + weight.weight + " " +  weight.destination;	
    					//System.out.println(temp);
    			    		context.push(out(), key, temp);
    				}
    			}
    		};

          FileResourceWriter<Integer,String> writer = new FileResourceWriter<>(outputDomain,outputFile,KV_OUTPUT_FORMAT);   	    
           
          ValueListStore<Integer, Weight> newEdges = new ValueListStore<>(outputDomain, edgesContainer);
	      newEdges.list().bind(translator.in());
	      translator.out().bind(writer.in()); 
    	  hamr.runJob(outputJob);	
    		
    			
    	}catch(Exception e){
    		HAMR.abort(e);
    	}
    	finally{
    		HAMR.shutdown();
    	}
    }
    
    	
    	
    /**
     * Parses the command line and then runs the example.
     * 
     * @param args command line
     */
    public static void main(String[] args) {        	
	
    	LastLine last = new LastLine();
    	FloydWarshall example = new FloydWarshall();
    	CmdLineParser parser = new CmdLineParser(example);
    	parser.setUsageWidth(80);

    	try {
    		parser.parseArgument(args);
    		if (example.displayUsage) {
    			example.printUsage(parser, System.out, null);
    			System.exit(0);
    		}
        
    		String temp = example.inputFile;
    		int max = last.Maximum(temp);
    		example.numberofvertices = max+1;
		example.run();

    	}catch (CmdLineException e) {                
    		example.printUsage(parser, System.err, "ERROR: " + e.getMessage());
    		System.exit(1);
    	}
    }
    
        	
    /**
     * Print usage and optional message to specified stream.
     * 
     * @param parser instance of command line parser
     * @param stream output stream to use (system out or err)
     */
    void printUsage(CmdLineParser parser, PrintStream stream, String message) {
    	if (message != null) {
    		stream.println(message);
    	}
    	stream.println(this.getClass().getSimpleName() + " [options...] arguments...");
    	parser.printUsage(stream);
    	stream.println();
    }

    /**
      * Returns the HDFS site configuration file using HADOOP_HOME from
      * either the environment or the command line.
      * 
      * @return the Hadoop core-site xml file
      */
    String getHdfsSiteConfig(){

    	if (HADOOP_HOME == null || !HADOOP_HOME.equals(hadoopHome)) {
    		return "file://" + hadoopHome + "/etc/hadoop/core-site.xml";
        } 
    	else {
            return HDFS_SITE_CONFIG;
        }
     }

}


