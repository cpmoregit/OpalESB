package org.opalesb.engine.object;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

public class MovieInfo implements Serializable{
	
	String id;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the hashTag
	 */
	public String getHashTag() {
		return HashTag;
	}
	/**
	 * @param hashTag the hashTag to set
	 */
	public void setHashTag(String hashTag) {
		HashTag = hashTag;
	}
	/**
	 * @return the year
	 */
	public String getYear() {
		return Year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		Year = year;
	}
	/**
	 * @return the movieName
	 */
	public String getMovieName() {
		return MovieName;
	}
	/**
	 * @param movieName the movieName to set
	 */
	public void setMovieName(String movieName) {
		MovieName = movieName;
	}
	/**
	 * @return the releasedAgainOn
	 */
	public String getReleasedAgainOn() {
		return ReleasedAgainOn;
	}
	/**
	 * @param releasedAgainOn the releasedAgainOn to set
	 */
	public void setReleasedAgainOn(String releasedAgainOn) {
		ReleasedAgainOn = releasedAgainOn;
	}
	String HashTag;
	String Year;
	String MovieName;
	String ReleasedAgainOn;
	

}
