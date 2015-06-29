
package org.cisc475.shortestpath.hamr;
import java.io.IOException;

import com.etinternational.hamr.io.Serialization;
import com.etinternational.hamr.io.SerializedInput;
import com.etinternational.hamr.io.SerializedOutput;


public class WeightSerialization extends Serialization<Weight>{

	/**
	 * @param args
	 */
	public WeightSerialization(){
		super(Weight.class);
	}

	@Override
	public void serialize(Weight weight, SerializedOutput output)
			throws IOException {
		// TODO Auto-generated method stub
		output.writeInt(weight.destination);
		output.writeDouble(weight.weight);
		
	}

	@Override
	public Weight deserialize(SerializedInput input) throws IOException {
		// TODO Auto-generated method stub
		Integer destination = input.readInt();
		Double weight =input.readDouble();
		return new Weight(destination,weight);
		
	}

	@Override
	public WeightSerialization clone() {
		// TODO Auto-generated method stub
		return new WeightSerialization();
	}

}
