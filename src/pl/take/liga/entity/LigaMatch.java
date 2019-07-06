package pl.take.liga.entity;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity for one record of match in database 
 * All methods are used by persistencem xmlMarhsal
 * and xmlUnMarshal 
 * @author Bartek
 * @version 1.0
 */
@Entity
@XmlRootElement(name="match")
@NamedQueries({
    @NamedQuery(name = "LigaMatch.findByScore",
            query = "SELECT lm FROM LigaMatch lm WHERE lm.homeGoals = :homeGoals AND lm.guestGoals = :guestGoals"),
    @NamedQuery(name = "LigaMatch.findByHomeGoals",
    		query = "SELECT lm FROM LigaMatch lm WHERE lm.homeGoals = :homeGoals"),
    @NamedQuery(name = "LigaMatch.findByGuestGoals",
			query = "SELECT lm FROM LigaMatch lm WHERE lm.guestGoals = :guestGoals"),
    @NamedQuery(name = "LigaMatch.findBetweenTeams",
    		query = "SELECT lm FROM LigaMatch lm WHERE (lm.homeTeam.id = :clubId1 AND lm.guestTeam.id = :clubId2) OR (lm.homeTeam.id = :clubId2 AND lm.guestTeam.id = :clubId1)")
})
public class LigaMatch implements Serializable {

	/**
	 * generated serialVersion
	 */
	private static final long serialVersionUID = 2301276816183473324L;
	/**
	 * Identity for LigaMatch table
	 */
	@Id
	@Column(name="MATCH_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@XmlAttribute
	private Integer id;
	/**
	 * Home team goals
	 */
	private Integer homeGoals;
	/**
	 * Guest team goals
	 */
	private Integer guestGoals;
	/**
	 * List of all goals in current match.
	 * Every goal has scorer, assistant and at what minute was scored.
	 */
	@OneToMany(mappedBy="match",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Goal> goals = new ArrayList<Goal>();
	/**
	 * host team - contains list of players, name, city
	 */
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="HOME_ID")
	private Club homeTeam;
	/**
	 * guest team - contains list of players, name, city
	 */
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="GUEST_ID")
	private Club guestTeam;
	/**
	 * home_id - used for match-club relationship while creating a match
	 */
	@Transient
	private Integer home_id;
	/**
	 * guest_id - used for match-club relationship while creating a match
	 */
	@Transient
	private Integer guest_id;
	
	public Integer getId()
	{
		return this.id;
	}
	/**
	 * 
	 * @return amount of goals scored by home team
	 */
	public Integer getHomeGoals() {
		return homeGoals;
	}
	/**
	 * set how many goals was scored in current match
	 * @param homeGoals Integer
	 */
	public void setHomeGoals(Integer homeGoals) {
		this.homeGoals = homeGoals;
	}
	/**
	 * 
	 * @return amount of goals scored by guest team
	 */
	public Integer getGuestGoals() {
		return guestGoals;
	}
	/**
	 * 
	 * @param guestGoals Integer
	 */
	public void setGuestGoals(Integer guestGoals) {
		this.guestGoals = guestGoals;
	}
	/**
	 * get all goals connected with current match
	 * @return all goals in this match
	 */
	public List<Goal> getGoals() {
		return goals;
	}
	/**
	 * not included in xml(use /goals to get goals for match)
	 * @param goals set goals for current match (check goal.setMatch())
	 */
	@XmlTransient
	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}
	/**
	 * not included in xml(use /goals to get goals for match)
	 * @param goal set goal for current match (check goal.setMatch())
	 */
	public void setGoal(Goal goal){
		this.goals.add(goal);
	}
	/**
	 * 
	 * @return whole object for whole team (referenced)
	 */
	public Club getHomeTeam() {
		return homeTeam;
	}
	/**
	 * 
	 * @param homeTeam home team for match
	 */
	public void setHomeTeam(Club homeTeam) {
		this.homeTeam = homeTeam;
	}
	/**
	 * 
	 * @return whole object for guest team (referenced)
	 */
	public Club getGuestTeam() {
		return guestTeam;
	}
	/**
	 * 
	 * @param guestTeam guest team for match
	 */
	public void setGuestTeam(Club guestTeam) {
		this.guestTeam = guestTeam;
	}
	/**
	 * @return the home_id
	 */
	public Integer getHome_id() {
		return home_id;
	}

	/**
	 * @param home_id the home_id to set
	 */
	public void setHome_id(Integer home_id) {
		this.home_id = home_id;
	}

	/**
	 * 
	 * @return the guest_id
	 */
	public Integer getGuest_id() {
		return guest_id;
	}

	/**
	 * @param guest_id the guest_id to set
	 */
	public void setGuest_id(Integer guest_id) {
		this.guest_id = guest_id;
	}
	/**
	 * used for adding goal to current match for guest team
	 */
	public void increseGuestGoal(){
		this.guestGoals+=1;
	}
	/**
	 * used for deleting goal from current match for guest team
	 */
	public void decreaseGuestGoal(){
		this.guestGoals = this.guestGoals ==0 ? 0 : this.guestGoals-1;
	}
	/**
	 * used for adding goal to current match for home team
	 */
	public void increseHomeGoal(){
		this.homeGoals+=1;
	}
	/**
	 * used for deleting goal from current match home team
	 */
	public void decreaseHomeGoal(){
		this.homeGoals = this.homeGoals ==0 ? 0 : this.homeGoals-1;
	}
}
