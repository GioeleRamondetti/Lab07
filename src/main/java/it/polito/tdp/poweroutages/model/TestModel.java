package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		int x=0;
		Model2 model = new Model2();
		//System.out.println(model.getNercList());
		Nerc n=new Nerc(12,"MRO");
		List<PowerOutages> l= new ArrayList<PowerOutages>(model.getWorstCase(4, 200,n));
		for(PowerOutages p:l) {
			System.out.println(p.getAffectedPeople());
			x=x+p.getAffectedPeople();
		}
		System.out.println("tot: "+x);
	}
}
