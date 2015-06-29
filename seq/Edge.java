package org.cisc475.shortestpath.seq;

public class Edge {

	int nodeOne;
	int nodeTwo;
	double weight;
	
	Edge(){
		
	}

	Edge(int nodeOne, int nodeTwo, double weight){
		this.nodeOne = nodeOne;
		this.nodeTwo = nodeTwo;
		this.weight = weight;
	}
	
}
