/**
 * 
 */
package pl.take.liga.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents list of ligaMatch entities
 * Used for easier marshaling 
 * @author Bartek
 * @version 1.11
 */
@XmlRootElement(name="matches")
public class LigaMatches {
	/**
	 * list of matches returned by entity manager
	 */
	private List<LigaMatch> matches = new ArrayList<LigaMatch>();
	public LigaMatches(){}
	/**
	 * Set matches to LigaMatches object
	 * @param matches list of matches
	 */
	public LigaMatches(List<LigaMatch> matches){
		this.matches = matches;
	}
	/**
	 * used by xmlMarshal
	 * @return list of goals
	 */
	@XmlElement(name ="match")
	public List<LigaMatch> getMatches() {
		return matches;
	}
	/**
	 * used by xmlUnMarshal
	 * @param matches list of matches
	 */
	public void setMatches(List<LigaMatch> matches) {
		this.matches = matches;
	}
}
