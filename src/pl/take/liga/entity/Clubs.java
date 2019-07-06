/**
 * 
 */
package pl.take.liga.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents list of clubs entities
 * Used for easier marshaling
 * @author Bartek
 * @version 1.1
 */
@XmlRootElement
public class Clubs {
	/**
	 * List of clubs 
	 * It contains list of clubs returned by entityManager
	 */
	private List<Club> clubs = new ArrayList<Club>();
	public Clubs(){}
	/**
	 * 
	 * @param clubs list of clubs
	 */
	public Clubs(List<Club> clubs){
		this.clubs = clubs;
	}
	/**
	 * ContaMiner for one club
	 * @param club one club
	 */
	public Clubs(Club club) {
		this.clubs.add(club);
	}
	/**
	 * used by xmlMarshal 
	 * @return clubs
	 */
	@XmlElement(name ="club")
	public List<Club> getClubs() {
		return clubs;
	}
	/**
	 * Used by xmlUnmarshal
	 * @param clubs
	 */
	public void setClubs(List<Club> clubs) {
		this.clubs = clubs;
	}
}
