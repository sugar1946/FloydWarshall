
package org.cisc475.shortestpath.hamr;
import java.util.Comparator;

public class Weight implements Comparable<Weight> {

	/**
	 * @param args
	 */
	public int destination;
	public double weight;
	
	public Weight(int destination, double weight){
		this.destination = destination;
		this.weight = weight;
	}


	@Override
	public int compareTo(Weight object) {
		// TODO Auto-generated method stub
		if (object.weight-this.weight > 0 ){return 1;}
		else if (object.weight-this.weight < 0 ){return -1;}
		else return 0;

	}
	
	public static Comparator<Weight> WeightComparator = new Comparator<Weight>(){
		public int compare(Weight w1,Weight w2){
			return w1.compareTo(w2);
		}
	};

}
