package it.polito.tdp.poweroutages.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutages;
import it.polito.tdp.poweroutages.model.events;

public class PowerOutageDAO {
	
	public List<Nerc> getNercList() {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}
	
	public List<events> getEvnts() {

		String sql = "SELECT customers_affected,date_event_began,date_event_finished FROM poweroutages ";
		List<events> eventsList = new ArrayList<>();
		List<events> FinaleventsList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String s=res.getString("date_event_began");
				//s=s.substring(0, s.length()-1).replace(".", "");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
				LocalDateTime dStart = LocalDateTime.parse(s, formatter);
				
				String s1=res.getString("date_event_finished");
				//s1=s1.substring(0, s.length()-1).replace(".", "");
				LocalDateTime dEnd = LocalDateTime.parse(s1, formatter);
		        //Period period = Period.between(dStart,dEnd);
		        Date d1=res.getDate("date_event_began");
		        Date d2=res.getDate("date_event_finished");
		        
		        

		        LocalDateTime fromTemp = LocalDateTime.from(dStart);
		        long years = fromTemp.until(dEnd, ChronoUnit.YEARS);
		        fromTemp = fromTemp.plusYears(years);

		        long months = fromTemp.until(dEnd, ChronoUnit.MONTHS);
		        fromTemp = fromTemp.plusMonths(months);

		        long days = fromTemp.until(dEnd, ChronoUnit.DAYS);
		        fromTemp = fromTemp.plusDays(days);

		        long hours = fromTemp.until(dEnd, ChronoUnit.HOURS);
		        fromTemp = fromTemp.plusHours(hours);

		        long minutes = fromTemp.until(dEnd, ChronoUnit.MINUTES);
		        fromTemp = fromTemp.plusMinutes(minutes);

		        long seconds = fromTemp.until(dEnd, ChronoUnit.SECONDS);
		        fromTemp = fromTemp.plusSeconds(seconds);

		        long millis = fromTemp.until(dEnd, ChronoUnit.MILLIS);

		        
		        
		        
		        long diffInHours = years*365*24+months*30*24+days*24+hours+minutes/60;
		        diffInHours=Math.round(diffInHours);
		        //System.out.println(diffInHours+"  ");
		        //long ore=period.getYears()*365*24;
		        //System.out.println(ore+"  ");
		        //ore=ore+period.getMonths()*30*24;
		        //System.out.println(ore+"  ");
		        //ore=ore+period.getDays()*24;
				events n = new events(res.getInt("customers_affected"),diffInHours,dStart.getYear(),dEnd.getYear());
				eventsList.add(n);
				//System.out.println(" customers "+n.getCustomers_affected());
			}


			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return eventsList;
	}
	
	public List<PowerOutages> getPowerOutagesByNerc(Nerc nerc) {

		String sql = "SELECT id, nerc_id, date_event_began, date_event_finished, customers_affected "
				+ "FROM poweroutages "
				+ "WHERE nerc_id = ?";
		
		List<PowerOutages> powerOutageList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, nerc.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				PowerOutages poe = new PowerOutages(res.getInt("id"), nerc,
						res.getTimestamp("date_event_began").toLocalDateTime(),
						res.getTimestamp("date_event_finished").toLocalDateTime(),
						res.getInt("customers_affected"));

				powerOutageList.add(poe);
				
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return powerOutageList;
	}

}
