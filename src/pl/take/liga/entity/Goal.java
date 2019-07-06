/**
 * 
 */
package pl.take.liga.entity;

import java.io.Serializable;

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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity for one record of goal in database 
 * All methods are used by persistencem xmlMarhsal
 * and xmlUnMarshal 
 * @author Bartek
 * @version 0.9
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Goal.findByMinute",
    		query = "SELECT g FROM Goal g WHERE g.atMinute = :atMinute"),
    /*@NamedQuery(name = "Goal.findByScorer",
    		query = "SELECT g FROM Goal g WHERE g.SCORER_ID = :score_id"),
    @NamedQuery(name = "Goal.findByAssistant",
    		query = "SELECT g FROM Goal g WHERE g.ASSISTANT_ID = :assist_id"),
    @NamedQuery(name = "Goal.findByParticipation",
    		query = "Select g From Goal g WHERE g.SCORER_ID = :id OR g.ASSISTANT_ID = :id")
*/})
public class Goal implements Serializable{

	/**
	 * generated serial
	 */
	private static final long serialVersionUID = 2066873018976827950L;
	/**
	 * Identity for Player table
	 */
	@Id
	@Column(name="GOAL_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@XmlAttribute
	private Integer id;
	/**
	 * Reference to match in which goal was scored
	 */
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="MATCH_ID")
	private LigaMatch match;
	/**
	 * Reference to player who scored the goal
	 */
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="SCORER_ID")
	private Player scorer;
	/**
	 * Reference to player who assisted the goal
	 */
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="ASSISTANT_ID")
	private Player assist;
	/**
	 * Minute in which goal was scored 
	 */
	private Integer atMinute;
	/**
	 * Identity of match in which goal was scored
	 * Ignored by persistence and used by xmlUnmarshal
	 * and create method to set relationship
	 */
	@Transient
	private Integer match_id;
	/**
	 * Identity of scorer by who goal was scored
	 * Ignored by persistence and used by xmlUnmarshal
	 * and create method to set relationship
	 */
	@Transient
	private Integer scorer_id;
	/**
	 * Identity of assistant by who goal was assisted
	 * Ignored by persistence and used by xmlUnmarshal
	 * and create method to set relationship
	 */
	@Transient
	private Integer assist_id;
	/**
	 * 
	 * @return Goal's identity
	 */
	
	public Integer getId()
	{
		return this.id;
	}
	/**
	 * 
	 * @return reference to match
	 */
	public LigaMatch getMatch() {
		return match;
	}
	/**
	 * Used to create relationship
	 * @param match new match reference
	 */
	public void setMatch(LigaMatch match) {
		this.match = match;
	}
	/**
	 * 
	 * @return reference to scorer
	 */
	public Player getScorer() {
		return scorer;
	}
	/**
	 * Set reference to scorer
	 * @param scorer reference to player who scored the goal
	 */
	public void setScorer(Player scorer) {
		this.scorer = scorer;
	}
	/**
	 * 
	 * @return reference to player who assisted the goal
	 */
	public Player getAssist() {
		return assist;
	}
	/**
	 * 
	 * @param assist player who assisted the goal
	 */
	public void setAssist(Player assist) {
		this.assist = assist;
	}
	/**
	 * 
	 * @return Integer (minute) in which goal was scored
	 */
	public Integer getAtMinute() {
		return atMinute;
	}
	/**
	 * Used to set at which minute goal was scored
	 * @param atMinute Integer
	 */
	public void setAtMinute(Integer atMinute) {
		this.atMinute = atMinute;
	}

	/**
	 * @return the match_id
	 */
	public Integer getMatch_id() {
		return match_id;
	}

	/**
	 * @param match_id the match_id to set
	 */
	public void setMatch_id(Integer match_id) {
		this.match_id = match_id;
	}

	/**
	 * @return the scorer_id
	 */
	public Integer getScorer_id() {
		return scorer_id;
	}

	/**
	 * @param scorer_id the scorer_id to set
	 */
	public void setScorer_id(Integer scorer_id) {
		this.scorer_id = scorer_id;
	}

	/**
	 * @return the assist_id
	 */
	public Integer getAssist_id() {
		return assist_id;
	}

	/**
	 * @param assist_id the assist_id to set
	 */
	public void setAssist_id(Integer assist_id) {
		this.assist_id = assist_id;
	}
}
