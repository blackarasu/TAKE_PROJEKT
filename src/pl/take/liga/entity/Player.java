package pl.take.liga.entity;

import static java.nio.charset.StandardCharsets.UTF_8;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity for one record of player in database 
 * All methods are used by persistencem xmlMarhsal
 * and xmlUnMarshal 
 * @author Bartek
 * @version 0.9
 */
@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Player.findByFullName",
	    	query = "SELECT p FROM Player p WHERE p.firstName = :firstName AND p.lastName = :lastName"),
    @NamedQuery(name = "Player.findByfName",
    	query = "SELECT p FROM Player p WHERE p.firstName = :firstName"),
    @NamedQuery(name = "Player.findBylName",
		query = "SELECT p FROM Player p WHERE p.lastName = :lastName"),
    @NamedQuery(name = "Player.findByAge",
		query = "SELECT p FROM Player p WHERE p.age = :age"),
    @NamedQuery(name = "Player.findByPosition",
		query = "SELECT p FROM Player p WHERE p.position = :position"),
})
public class Player implements Serializable {
	/**
	 * generated serialVersion
	 */
	private static final long serialVersionUID = 8165076399463134156L;
	/**
	 * Identity for Player table
	 */
	@Id
	@Column(name="PLAYER_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@XmlAttribute
	private Integer id;
	/**
	 * Player's first name
	 */
	private String firstName = new String();
	/**
	 * Player's last name
	 */
	private String lastName;
	/**
	 * Player's age
	 */
	private Integer age;
	/**
	 * Player's position
	 */
	@Column(name="POSITION")
	private String position;
	/**
	 * Player's club (entity)
	 */
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="CLUB_ID")
	private Club club;
	/**
	 * used for creating object with relationship it must be set in XML file before sending to server
	 */
	@Transient
	private Integer club_id;
	/**
	 * all player's assists
	 */
	@OneToMany(mappedBy = "assist",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Goal> assists=new ArrayList<Goal>();
	/**
	 * all goals scored by player
	 */
	@OneToMany(mappedBy = "scorer",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Goal> goals = new ArrayList<Goal>();
	/**
	 * XML attribute
	 * @return player's id
	 */
	public Integer getId()
	{
		return this.id;
	}
	/**
	 * get player's position
	 * @return player's position
	 */
	public String getPosition() {
		return position;
	}
	/**
	 * sets a new position to player
	 * @param position it should be three char long
	 */
	public void setPosition(String position) {
		byte ptext[] = position.getBytes(StandardCharsets.UTF_8);
		this.position = new String(ptext, UTF_8);
	}
	/**
	 * 
	 * @return player's first name
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * set first name
	 * @param firstName - String
	 */
	public void setFirstName(String firstName) {
		byte ptext[] = firstName.getBytes(StandardCharsets.UTF_8);
		this.firstName = new String(ptext, UTF_8);
	}
	/**
	 * 
	 * @return player's last name (String)
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * Sets new player's last name
	 * @param lastName - String
	 */
	public void setLastName(String lastName) {
		byte ptext[] = lastName.getBytes(StandardCharsets.UTF_8);
		this.lastName = new String(ptext,StandardCharsets.UTF_8);
	}
	/**
	 * 
	 * @return player's age
	 */
	public Integer getAge() {
		return age;
	}
	/**
	 * 
	 * @param age Integer - player's age
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
	/**
	 * Get player's club (object - entity)
	 * @return player's club
	 */
	@XmlElement
	public Club getClub(){
		return this.club;
	}
	//@XmlTransient
	public void setClub(Club club){
		this.club = club;
	}
	/**
	 * used for creating object with relationship it 
	 * must be set in XML file before sending to server
	 * @return the club_id
	 */
	public Integer getClub_id() {
		return club_id;
	}

	/**
	 * used for creating object with relationship it 
	 * must be set in XML file before sending to server
	 * @param club_id the club_id to set
	 */
	public void setClub_id(Integer club_id) {
		this.club_id = club_id;
	}

	//@XmlElementWrapper(name="assists")
    //@XmlElement(name="assist")
	//@XmlElementRef
	/**
	 * get all assists get by player
	 * @return list of goals 
	 */
	public List<Goal> getAssists() {
		return assists;
	}
	/**
	 * 
	 * @param assists set assists to player
	 */
	@XmlTransient
	public void setAssists(List<Goal> assists) {
		this.assists = assists;
	}
	/**
	 * set new assist to player
	 * @param assist - new Goal object
	 */
	public void setAssist(Goal assist){
		this.assists.add(assist);
	}
	/**
	 * @return the goals
	 */
	//@XmlElementWrapper(name = "goals")
    //@XmlElement(name = "goal")
	//@XmlElementRef
	/**
	 * get all goals scored by player
	 * @return list of goals
	 */
	public List<Goal> getGoals() {
		return goals;
	}
	/**
	 * @param goals the goals to set
	 */
	@XmlTransient
	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}
	/**
	 * Set new goal to player
	 * @param goal new goal object
	 */
	public void setGoal(Goal goal){
		this.goals.add(goal);
	}
}
