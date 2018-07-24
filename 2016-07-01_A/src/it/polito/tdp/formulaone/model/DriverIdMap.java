package it.polito.tdp.formulaone.model;

import java.util.HashMap;

public class DriverIdMap extends HashMap<Integer, Driver>{
	
	public Driver get(Driver driver) {
		Driver old = super.get(driver.getDriverId());
		if(old != null) 
			return old;
		else
			old = driver;
	
		super.put(old.getDriverId(), old);
		return old;
	
	}
}
