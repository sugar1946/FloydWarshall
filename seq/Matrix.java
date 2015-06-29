package org.cisc475.shortestpath.seq;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Matrix {

	String[] inputLine;
	Edge e = new Edge();
	HashMap<Integer, HashMap<Integer, Double>> edgeMatrix = new HashMap<Integer, HashMap<Integer,Double>>();
	int numberofvertices=0;
	class Edge{
		int nodeOne;
		int nodeTwo;
		double weight;
	}
		
	
	//Creates a file object based on filename.
	File acceptFile(String filename){ //Creates a file based on a filename
		File f = new File(filename);
		return f;
	}
	
	
	//Given a file, parse and map.
	void mapper(File f){
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(f));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null){ // Print the content on the console
			      
				edgeChanger(strLine, e);
				edgeFactory(e);
				
			}
		} catch(Exception e){
			e.printStackTrace();
		};
		
	}
	
	//Given a string, change edge e to reflect new input
	void edgeChanger(String s, Edge e){
		inputLine = s.split(" ");		
		int i1 = Integer.parseInt(inputLine[0]);
		double i2 = Double.parseDouble(inputLine[1]);
		int i3 = Integer.parseInt(inputLine[2]);
		
		e.nodeOne = i1;
		e.nodeTwo = i3;
		e.weight = i2;
	
	}
	
	
	//Given a vectors, adds input to the map of maps of edge weights.
	void edgeFactory(Edge e){
		if(! (edgeMatrix.containsKey(e.nodeOne)) ){ //If the first node in the triple does not have an entry, make one
			edgeMatrix.put( e.nodeOne, new HashMap<Integer,Double>() );
		}

		(edgeMatrix.get(e.nodeOne)).put(e.nodeTwo, e.weight); //Places the weight in the correct entry in the 2D Map
		if (e.nodeOne>numberofvertices){numberofvertices=e.nodeOne;}
		if (e.nodeTwo>numberofvertices){numberofvertices=e.nodeTwo;}

	}	
	//Given a vector list, adds to the map of maps of edge weights
	void mapping(List<Edge> elist){
		for(Edge e: elist){
			edgeFactory(e);
		}
	}
	
	
	void completeMap(String filename){
		mapper( acceptFile(filename) );
	}
	
}
