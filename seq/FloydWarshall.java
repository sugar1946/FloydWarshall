package org.cisc475.shortestpath.seq;

import org.cisc475.shortestpath.seq.Matrix;
import java.util.HashMap;
import java.util.Scanner;
import java.io.PrintStream;
import java.io.File;

@SuppressWarnings("unused")
public class FloydWarshall {

    public final static PrintStream out = System.out;
	
    private double distancematrix[][];

    private int numberofvertices;

    // why is 999 infinity?  Does not seem very big.
    public static final int INFINITY = 999;
 
    //Instantiates an instance of the FloydWarshall object.
    public FloydWarshall(int numberofvertices)
    {
        distancematrix = new double[numberofvertices][numberofvertices];
        this.numberofvertices = numberofvertices;
    }
   
   /////////////Mingzhi Yu///////////////
    public double[][] getMatrix(){

       return distancematrix;

   }
   /////////////////////////////////////
 
    //Given a Map of <K, <K, V> > pairings, performs the Floyd-Warshall algorithm and
    //produces a 2D matrix of the result.
    void floydwarshall(HashMap<Integer, HashMap<Integer, Double>> adjacencymatrix){
        for (int source = 0; source <numberofvertices; source++){
        	if(adjacencymatrix.get(source)!=null){
        		for (int destination = 0; destination < numberofvertices; destination++){
        			if((adjacencymatrix.get(source).get(destination)) == null){
        				distancematrix[source][destination] = INFINITY;
        			}
        			
        			else{
        				distancematrix[source][destination] = (adjacencymatrix.get(source)).get(destination);
        			}           
        		}
        		}
        }
 
        for (int source  = 0; source < numberofvertices; source ++) {
            for (int destination  = 0; destination  < numberofvertices; destination ++) {
                for (int intermediate = 0; intermediate < numberofvertices; intermediate++) {
                    if (distancematrix[source][intermediate] + distancematrix[intermediate][destination]
                         < distancematrix[source][destination])
                        distancematrix[source][destination] = distancematrix[source][intermediate] 
                            + distancematrix[intermediate][destination];
                }
            }
        }
 
   }



    public void printFloydWarshall(){
	for (int source = 0; source < numberofvertices; source++){            
             for (int destination = 0; destination < numberofvertices; destination++){
            	 if(distancematrix[source][destination]!=999 ){
            		 System.out.print(source + " "+ distancematrix[source][destination] + " "+ destination + " " +":" + " " + source + "\n");
            	 }
            }
         }



    }



 
    public static void main(String[] args) {
	int numArgs = args.length;

	if (numArgs != 1) {
	    out.println("Expected one argument, saw "+numArgs+" arguments.");
	    out.println("Usage: java org.cisc475.shortestpath.seq.FloydWarshall <filename>");
	    out.println("  filename : name of file containing graph description");
	    System.exit(1);
	}
    	
	String filename=args[0];
	File file = new File(filename);

	if (!file.exists()) {
	    out.println("File "+file+" not found");
	    System.exit(2);
	}
	
    	HashMap<Integer, HashMap<Integer, Double>> adjacency_matrix;
        int numberofvertices;
        Matrix M = new Matrix();

        M.completeMap(filename);
        adjacency_matrix = M.edgeMatrix;
        numberofvertices = (M.edgeMatrix.size() - 1);
        
        
        FloydWarshall floydwarshall = new FloydWarshall(numberofvertices);
        floydwarshall.floydwarshall(adjacency_matrix);

	floydwarshall.printFloydWarshall();
    }

}
