package ethos.util.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;

/**
 * The purpose of this class is to represent a current date in time, and request for it to be updated every certain amount of cycles.
 * 
 * @author Jason MacKeigan
 * @date Oct 26, 2014, 10:57:39 PM
 */
public class GameCalendar {

	/**
	 * The current date, represented with the use of the DateFormat class
	 */
	private Calendar date = Calendar.getInstance();

	/**
	 * Representation of the current date
	 */
	DateFormat formatter;

	/**
	 * The time zone of the calendar
	 */
	TimeZone timeZone;

	/**
	 * Constructs a new calendar and tells it to automatically update however often we need it to.
	 */
	public GameCalendar(SimpleDateFormat formatter, String timeZone) {
		this.formatter = formatter;
		this.timeZone = TimeZone.getTimeZone(timeZone);
		this.date.setTimeZone(this.timeZone);
		this.formatter.setTimeZone(this.timeZone);
	}

	/**
	 * Determines the amount of milliseconds between now and a certain point in the same day, or a different day.
	 * 
	 * @param dayOfMonth the day of the month
	 * @param hourOfDay the hour of the day ranging from 0-23.
	 * @param minuteOfHour the minute of the hour ranging from 0-59.
	 * @param secondOfMinute the second of the minute ranging from 0-59.
	 * @return the difference in milliseconds or -1 if the future date has already passed.
	 */
	public long getMillisecondsRemaining(int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute) {
		date = getInstance();

		Date future = date.getTime();
		future = DateUtils.setDays(future, dayOfMonth);
		future = DateUtils.setHours(future, hourOfDay);
		future = DateUtils.setMinutes(future, minuteOfHour);
		future = DateUtils.setSeconds(future, secondOfMinute);

		if (future.getTime() < date.getTimeInMillis()) {
			return -1;
		}

		return future.getTime() - date.getTimeInMillis();
	}

	/**
	 * Determines the amount of milliseconds between now and a certain point in the same day.
	 * 
	 * @param hourOfDay the hour of the day ranging from 0-23.
	 * @param minuteOfHour the minute of the hour ranging from 0-59.
	 * @param secondOfMinute the second of the minute ranging from 0-59.
	 * @return the difference in milliseconds or -1 if the future date has already passed.
	 */
	public long getMillisecondsRemaining(int hourOfDay, int minuteOfHour, int secondOfMinute) {
		date = getInstance();
		int day = date.get(Calendar.DAY_OF_MONTH);

		return getMillisecondsRemaining(day, hourOfDay, minuteOfHour, secondOfMinute);
	}

	/**
	 * Determines the amount of milliseconds between now and a certain point in the same day.
	 * 
	 * @param minuteOfHour the minute of the hour ranging from 0-59.
	 * @param secondOfMinute the second of the minute ranging from 0-59.
	 * @return the difference in milliseconds or -1 if the future date has already passed.
	 */
	public long getMillisecondsRemaining(int minuteOfHour, int secondOfMinute) {
		date = getInstance();
		int hour = date.get(Calendar.HOUR_OF_DAY);

		return getMillisecondsRemaining(hour, minuteOfHour, secondOfMinute);
	}

	/**
	 * Returns the date representation in years, months, and days
	 * 
	 * @return the date in years, months, and days
	 */
	public String getYMD() {
		date = getInstance();
		return reformat(new SimpleDateFormat("yyyy/MM/dd"));
	}

	/**
	 * Returns the date representation in hours, minutes, and seconds
	 * 
	 * @return the hour, minute and second of the day
	 */
	public String getHMS() {
		date = getInstance();
		return reformat(new SimpleDateFormat("HH:mm:ss"));
	}

	/**
	 * Returns the day of the month in the form of a string
	 * 
	 * @return the day
	 */
	public String getDay() {
		return Integer.toString(date.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * Returns a string representation of the current date and time
	 * 
	 * @return the formatted date and time
	 */
	@Override
	public String toString() {
		date = getInstance();
		return formatter.format(date.getTime());
	}

	/**
	 * Returns a new unfamiliar representation of the date
	 * 
	 * @param formatter the format of the date
	 * @return the date reformatted
	 */
	public String reformat(SimpleDateFormat formatter) {
		date = getInstance();
		return formatter.format(date.getTime());
	}

	/**
	 * Returns the date object for this calendar
	 * 
	 * @return the date
	 */
	public Calendar getInstance() {
		date = Calendar.getInstance(timeZone);
		return date;
	}

}
