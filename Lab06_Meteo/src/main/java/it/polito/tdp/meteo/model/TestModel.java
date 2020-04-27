package it.polito.tdp.meteo.model;

import java.util.LinkedList;
import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		
		//System.out.println(m.getUmiditaMedia(12));
		
		for (int i=1; i<=12 ; i++) {
		System.out.println(m.trovaSequenza(i));
		System.out.println(m.calcolaCosto(m.trovaSequenza(i)));	
		}
		

	}

}
