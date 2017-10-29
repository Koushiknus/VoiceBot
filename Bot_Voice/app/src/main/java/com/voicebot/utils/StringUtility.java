package com.voicebot.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


public class StringUtility {

	public static boolean isEmpty(String string) {
		if (string == null) {
			return true;
		}
		if ("".equals(string.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public static Date converStringTimeToDate(String time) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(getHour(time)));
		calendar.set(Calendar.MINUTE, Integer.valueOf(getMinute(time)));
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		if (calendar.getTime().before(new Date())) {
			calendar.add(Calendar.DATE, 1);
		}
		return calendar.getTime();
	}
	
	/**
	 * hh:mm ex: 24:50;
	 * @param time
	 * @return
	 */
	public static int getHour(String time) {
		String hour= time.substring(0, time.indexOf(":"));
		if (isLong(hour)) {
			return Long.valueOf(hour).intValue();
		}
		return 0;
	}
	
	/**
	 * hh:mm ex: 24:50;
	 * @param time
	 * @return
	 */
	public static int getMinute(String time) {
		String min= time.substring(time.indexOf(":")+1);
		if (isLong(min)) {
			return Long.valueOf(min).intValue();
		}
		return 0;
	}
	
	/**
	 * Check to see if the date is end of month
	 * @param date
	 */
	public static boolean isBeginOfMonth(final Date date) {
		if (date == null) {
			return false;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DATE) == 1) {
			return true;
		} 
		return false;
	}
	
	/**
	 * Check to see if the date is end of month
	 * @param date
	 */
	public static boolean isBeginOfWeek(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			return true;
		}
		return false;
	}
	
	public static String dateToString(Date date, String format) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String ret="";
		
		try {
			ret =sdf.format(date); 
		} catch (Exception e) {
			ret="";
		}
		return ret;
	}
	
	public static boolean isLong(String source) {
		boolean ret = true;
		
		try {
			Long.parseLong(source);
		} catch (Exception ex) {
			ret= false;
		}
		return ret;
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	public static  boolean validateTimeString(String input) {
		if (input.indexOf(":")<0) {
			return false;
		}
		String min= input.substring(input.indexOf(":")+1);
		String hour= input.substring(0,input.indexOf(":"));
		if (!isLong(min) || !isLong(hour)) {
			return false;
		}
		if (Long.valueOf(hour).longValue() >23) {
			return false;
		}
		if (Long.valueOf(min).longValue() >59) {
			return false;
		}
		return isLong(min) && isLong(hour);
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isAllDegit(String input) {
		if (input !=null) {
			input=input.trim();
		}
		char[] charArray = input.toCharArray();
		boolean valid= true;
		if (charArray !=null && charArray .length>0) {
			for (int i=0; i<charArray.length;i++) {
				if (!Character.isDigit(charArray[i])) {
					valid=false;
					break;
				}
			}
		}
		return valid;
	}
	
	/**
	 * Get duration in minutes
	 * 
	 * @param starttime
	 * @param endTime
	 * @param defaultVal
	 * @return
	 */
	public static int durationMinute(Date starttime, Date endTime, int defaultVal) {
		
		if (endTime == null) {
			return defaultVal;
		}
		long duration = endTime.getTime() - starttime.getTime();
		duration = duration / 1000; //second
		return (int)duration / 60;
	}
	
	/**
	 * Finding exactly matched value in two list
	 * @param listOne
	 * @param listTwo
	 * @return the values in listOne that contain in listTwo
	 */
	public static Collection<String> similarValue(List<String> listOne, List<String> listTwo) {
		return similarValue(listOne, listTwo, false);
	}
	
	/**
	 * Finding similar matched value in two list
	 * @param listOne
	 * @param listTwo
	 * @param optimizeValue true/false (true is exact match, false is similar match)
	 * @return the values in listOne that contain in listTwo
	 */
	public static Collection<String> similarValue(List<String> listOne, List<String> listTwo, boolean optimizeValue) {
		Collection<String> similar = null;
		if (!optimizeValue) {
			similar = new HashSet<String>(listOne);
	        similar.retainAll(listTwo);
		} else {
			similar = new HashSet<String>();
			for (String s1: listOne) {
				for (String s2: listTwo) {
					if (calculateDistance(s1, s2) >= 0.5) {
						similar.add(s1);
						break;
					}
				}
			}
		}
		
		similar.removeAll(Arrays.asList("", null));
		return similar;
	}
	
	/**
	 * Calculate distance between two strings
	 * @param a
	 * @param b
	 * @return distance 1.0 is closed, 0.0 is almost different
	 */
	private static double calculateDistance(String a, String b) {
		double distance = 1.0; /* both strings are same */
		String longer = a, shorter = b;
		if (a.length() < b.length()) { // longer should always have greater length
			longer = b;
			shorter = a;
		}
		int longerLength = longer.length();
		if (longerLength != 0) {
			distance = (longerLength - levenshteinDistance(longer, shorter)) / (double) longerLength;
		}
		return distance;
	}
	
	/**
	 * Implementing follow Levenshtein's algorithm
	 * @param a
	 * @param b
	 * @return the distance (0 is closest)
	 */
	private static int levenshteinDistance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        int [] costs = new int [b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }
}