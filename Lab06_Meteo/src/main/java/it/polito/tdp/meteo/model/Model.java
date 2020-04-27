package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	private MeteoDAO dao;
	public List <Citta> soluzione;
	private int bestCosto = 0;
	private List <Citta> citta;
	
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	public Model() {
		dao = new MeteoDAO();
		
	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		String medie= "";
		medie = "Torino: " + dao.getMedieUmidita(mese, "Torino") + " Milano: " + dao.getMedieUmidita(mese, "Milano")+ " Genova: " + dao.getMedieUmidita(mese, "Genova");
		
		return medie;
	}
	
	
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		 Citta Torino = new Citta ("Torino", dao.getAllRilevamentiLocalitaMese(mese, "Torino"));
		 Citta Milano = new Citta ("Milano", dao.getAllRilevamentiLocalitaMese(mese, "Milano"));
		 Citta Genova = new Citta ("Genova", dao.getAllRilevamentiLocalitaMese(mese, "Genova"));
		 
		 citta = new ArrayList <>();
		 citta.add(Genova);
		 citta.add(Milano);
		 citta.add(Torino);
		 
		 bestCosto=0;
		 soluzione = new ArrayList <>();
		 List <Citta> parziale = new ArrayList <>();
		 
		 cerca(parziale, 0);
		
		return soluzione;
	}
	
	private void cerca(List <Citta> parziale, int livello) {
		
		//caso terminale 
		if (parziale.size()==NUMERO_GIORNI_TOTALI) {
			if (bestCosto==0 ) {
				this.soluzione= new ArrayList <>(parziale);
				bestCosto = this.calcolaCosto(parziale);
			}
			else if  (this.calcolaCosto(parziale)<bestCosto){
				this.soluzione= new ArrayList <>(parziale);
				bestCosto = this.calcolaCosto(parziale);
			
			}
		}
		else {
		//caso intermedio 
		for (int i=0; i<citta.size(); i++) {
			
			
			if (controlloMinimo(citta.get(i), parziale)) {
			
			int flag=0;
			
			Citta c = citta.get(i);
			
			if (c.getCounter()>=NUMERO_GIORNI_CITTA_MAX) {
				flag=1; //non faccio la ricorsione e devo cambiare citta
			}
			
			if (flag==0) {
					citta.get(i).increaseCounter();
					parziale.add(c);
					cerca (parziale, livello+1);
					parziale.remove(c);
					citta.get(i).setCounter(c.getCounter()-1);
				}
				
			}
				
			}
		}
	}
	
	
	public boolean controlloMinimo (Citta citta, List <Citta> parziale) {
		
		// verifica dei giorni minimi
				if (parziale.size()==0) //primo giorno posso inserire qualsiasi città
						return true;
				
				if (parziale.size() < NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN && parziale.size()!=0) {//(parziale.size()==1 || parziale.size()==2) {
					//siamo al secondo o terzo giorno, non posso cambiare
					//quindi l'aggiunta è valida solo se la città di prova coincide con la sua precedente
					return parziale.get(parziale.size()-1).equals(citta); 
				}
				
				//nel caso generale, se ho già passato i controlli sopra, non c'è nulla che mi vieta di rimanere nella stessa città
				//quindi per i giorni successivi ai primi tre posso sempre rimanere
				if (parziale.get(parziale.size()-1).equals(citta))
					return true; 
				
				// se cambio città mi devo assicurare che nei giorni precedenti sono rimasto fermo 
				for(int i=1; i <= NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN; i++) {
					if (parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-i))) {
						return true;
					}
				}
					
				return false;
		
	}
	
	
	public int calcolaCosto (List <Citta> sequenza) {
		int costo=0;
		
		for (int i=0; i<sequenza.size(); i++) {
			if (i>=1) { 
				if (sequenza.get(i).getNome().compareTo(sequenza.get(i-1).getNome())!=0) {
				costo += COST;
				}
			}
			costo += sequenza.get(i).getRilevamenti().get(i).getUmidita();
		}
			
		return costo;
	}
	
	/*public boolean controllo (List<Rilevamento> quindiciGiorni){
		return false;
		
	}*/
	

}
