package it.polimi.awt.jpa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static Date parseDate(String date) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date parseYear(String year) {
		return parseDate("01/01/" + year);
	}
	
	public static String formatDate(Date date) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").format(date);
		} catch (NullPointerException e) {
			return null;
		}
	}

}
