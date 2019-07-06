/**
 * 
 */
package pl.take.liga.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents list of players entities
 * Used for easier marshaling 
 * @author Bartek
 * @version 1.1
 */
@XmlRootElement
public class Players {
/**
 * list of players returned from entity manager	
 */
private List<Player> players = new ArrayList<Player>();
public Players(){}
/**
 * Set players to Players object
 * @param players list of players
 */
public Players(List<Player> players){
	super();
	this.players = players;
}
/**
 * Used by xmlMarshal
 * @return list of players
 */
@XmlElement(name ="player")
public List<Player> getPlayers() {
	return players;
}

/**
 * Used by xmlUnMarshal
 * @param players list of players
 */
public void setPlayers(List<Player> players) {
	this.players = players;
}
}
