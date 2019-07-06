/**
 * 
 */
package pl.take.liga.entity;


import static java.nio.charset.StandardCharsets.*;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity for one record of club in database 
 * All methods are used by persistencem xmlMarhsal
 * and xmlUnMarshal 
 * @author Bartek
 * @version 1.0
 */
@Entity
@XmlRootElement(name="club")
@NamedQueries({
    @NamedQuery(name = "Club.findByName",
            query = "SELECT c FROM Club c WHERE c.name = :name"),
    @NamedQuery(name = "Club.findByCity",
    		query = "SELECT c FROM Club c WHERE c.city = :city"),
    @NamedQuery(name = "Club.findByFullName",
	query = "SELECT c FROM Club c WHERE c.name = :name AND c.city = :city")
})
public class Club implements Serializable{
	/**
	 * generated serjalVersion
	 */
	private static final long serialVersionUID = 1436651903383221321L;
	/**
	 * Identity for Club table
	 */
	@Id
	@Column(name="CLUB_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@XmlAttribute
	private Integer id;
	/**
	 * XML element 
	 * Name of club
	 */
	private String name;
	/**
	 * XML element
	 * City where club belongs
	 */
	private String city;
	/**
	 * Club's players
	 */
	@OneToMany(mappedBy = "club",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Player> players = new ArrayList<Player>();
	/**
	 * Matches in which the club played as host
	 */
	@OneToMany(mappedBy="homeTeam",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<LigaMatch> homeMatches = new ArrayList<LigaMatch>();
	/**
	 * Matches in which the club played as guest
	 */
	@OneToMany(mappedBy="guestTeam",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<LigaMatch> guestMatches = new ArrayList<LigaMatch>();

	/**
	 * XML attribute and club's id
	 * @return Integer club's id 
	 */
	public Integer getId()
	{
		return this.id;
	}
	/**
	 * 
	 * @return String - club's name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Setting new club's name
	 * @param name - new club's name
	 */
	public void setName(String name) {
		byte ptext[] = name.getBytes(StandardCharsets.UTF_8);
		this.name = new String(ptext, UTF_8);
	}
	/**
	 * 
	 * @return String - city where club belongs
	 */
	public String getCity() {
		return city;
	}
	/**
	 * Setting new city of club
	 * @param city - new city
	 */
	public void setCity(String city) {
		byte ptext[] = city.getBytes(StandardCharsets.UTF_8);
		this.city = new String(ptext, UTF_8);
	}
	/**
	 * get list of players who play in the club
	 * @return list of players
	 */
	public List<Player> getPlayers() {
		return players;
	}
	/**
	 * Ignored by xmlMarshal
	 * Setting new list of players
	 * @param players - list of new players
	 */
	@XmlTransient
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	/**
	 * Setting one player to the list of players
	 * @param player - new player in team
	 */
	public void setPlayer(Player player){
		this.players.add(player);
	}
	/**
	 * Get all matches in which the club was host
	 * @return list of matches
	 */
	public List<LigaMatch> getHomeMatches() {
		return homeMatches;
	}
	/**
	 * Ignored by xmlMarshal
	 * Set matches into entity in which the club played as host
	 * @param homeMatches - matches in which club played as host
	 */
	@XmlTransient
	public void setHomeMatches(List<LigaMatch> homeMatches) {
		this.homeMatches = homeMatches;
	}
	/**
	 * Set new match in which the club played as host team
	 * @param homeMatch - new homeMatch
	 */
	public void setHomeMatch(LigaMatch homeMatch){
		this.homeMatches.add(homeMatch);
	}
	/**
	 * Get all matches in which the club was guest
	 * @return list of matches
	 */
	public List<LigaMatch> getGuestMatches() {
		return guestMatches;
	}
	/**
	 * Ignored by xmlMarshal
	 * Set matches into entity in which the club played as guest
	 * @param guestMatches - matches in which club played as guest
	 */
	@XmlTransient
	public void setGuestMatches(List<LigaMatch> guestMatches) {
		this.guestMatches = guestMatches;
	}
	/**
	 * Set new match in which the club played as guest team
	 * @param guestMatch - new guest match
	 */
	public void setGuestMatch(LigaMatch guestMatch){
		this.guestMatches.add(guestMatch);
	}
}
