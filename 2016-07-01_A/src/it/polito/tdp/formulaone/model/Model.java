package it.polito.tdp.formulaone.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {
	
	private FormulaOneDAO dao;
	private SimpleDirectedWeightedGraph<Driver, DefaultWeightedEdge> grafo;
	private DriverIdMap driverIdMap;
	private List<Driver> bestDreamTeam;
	private int bestDreamTeamValue;
	
	public Model() {
		dao = new FormulaOneDAO();
		driverIdMap = new DriverIdMap();
	
	}
	
	public List<Season> getAllSeasons(){
		return dao.getAllSeasons();
	}

	public void creaGrafo(Season s) {
		// grafo orientato e pesato
		
		grafo = new SimpleDirectedWeightedGraph<Driver, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		List<Driver> drivers = dao.getAllDriversBySeason(s, driverIdMap);
		Graphs.addAllVertices(grafo, drivers);
		for(DriverSeasonResult drs: dao.getDriverSeasonResults(s, driverIdMap)) {
			Graphs.addEdgeWithVertices(grafo, drs.getD1(), drs.getD2(), drs.getCount());
		}
		System.out.format("Grafo aggiunto con %d vertici e %d archi" ,grafo.vertexSet().size(), grafo.edgeSet().size());
	}
	public Driver getBestDriver() {
		
		if(grafo == null) {
			new RuntimeException("Creare il grafo!!");
		}
		Driver bestDriver = null;
		int best = Integer.MAX_VALUE;
		
		for(Driver d: grafo.vertexSet()) {
			int sum = 0;
			
			// Itero sugli archi uscenti
			for(DefaultWeightedEdge e : grafo.outgoingEdgesOf(d)) {
				sum+= grafo.getEdgeWeight(e);
			}
			// Itero sugli archi entranti
			for(DefaultWeightedEdge e : grafo.incomingEdgesOf(d))
				sum -= grafo.getEdgeWeight(e);
				
			if(sum > best || bestDriver == null) {
				bestDriver = d;
				best = sum;
			}
		}
		return bestDriver;
	}
	
	public List<Driver> getDreamTeam(int k){
		bestDreamTeam = new ArrayList<>();
		bestDreamTeamValue = Integer.MAX_VALUE;
		recursive(0,  new ArrayList<Driver>() , k);
		return bestDreamTeam;
	}

	private void recursive(int step, ArrayList<Driver> tempDreamTeam, int k) {
		// condizione di terminazione
		if(step >= k) {
			if(evaluate(tempDreamTeam)> bestDreamTeamValue) {
				bestDreamTeamValue = evaluate(tempDreamTeam);
				bestDreamTeam =new ArrayList<>(tempDreamTeam);
			}
		}
		
		for(Driver d: grafo.vertexSet()) {
			if(!tempDreamTeam.contains(d)) {
				tempDreamTeam.add(d);
				recursive(step+1, tempDreamTeam, k);
				tempDreamTeam.remove(d);
			}
		}
		
	}

	private int evaluate(ArrayList<Driver> tempDreamTeam) {
		int sum = 0; 
		Set<Driver>tempDreamTeamSet = new HashSet<>(tempDreamTeam);
		for(DefaultWeightedEdge e : grafo.edgeSet()) {
			if(tempDreamTeamSet.contains(grafo.getEdgeTarget(e)))
				sum += grafo.getEdgeWeight(e);
		}
		return sum;
	}

}
