package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		final String sql = "SELECT Data, Umidita FROM situazione WHERE Localita=? AND MONTH (data)=? ORDER BY data ASC LIMIT 15";
		List <Rilevamento> rilevamenti = new LinkedList <>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, localita);
			st.setInt(2, mese);

			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				Rilevamento r = new Rilevamento(localita, rs.getDate("Data"), rs.getInt("Umidita"));
		
				rilevamenti.add(r);
			}
				
			conn.close();
			return rilevamenti;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			throw new RuntimeException(e);
			
		}

	}
	
	/*public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese) {
		final String sql = "SELECT Data, Umidita, Localita FROM situazione WHERE MONTH (data)=? ORDER BY data ASC LIMIT 45";
		List <Rilevamento> rilevamenti = new LinkedList <>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			//st.setString(1, localita);
			st.setInt(1, mese);

			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
		
				rilevamenti.add(r);
			}
				
			conn.close();
			return rilevamenti;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			throw new RuntimeException(e);
			
		}

	}*/
	
	public Double getMedieUmidita (int mese, String localita){
		final String sql ="SELECT AVG(Umidita) AS media FROM situazione WHERE MONTH(data)=? AND Localita=?";
		
		Double media = 0.0;
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			st.setString(2, localita);

			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				media= rs.getDouble("media");
			}
			
			conn.close();
			return media;
			
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
			
		}
	}

	

}
