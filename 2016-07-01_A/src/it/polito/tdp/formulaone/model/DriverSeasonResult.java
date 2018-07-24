package it.polito.tdp.formulaone.model;

public class DriverSeasonResult {
	private Driver d1;
	private Driver d2;
	private int count;
	
	public DriverSeasonResult(Driver d1, Driver d2, int count) {
		super();
		this.d1 = d1;
		this.d2 = d2;
		this.count = count;
	}

	public Driver getD1() {
		return d1;
	}

	public void setD1(Driver d1) {
		this.d1 = d1;
	}

	public Driver getD2() {
		return d2;
	}

	public void setD2(Driver d2) {
		this.d2 = d2;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	} 
	
	
	
	
	
	
}
