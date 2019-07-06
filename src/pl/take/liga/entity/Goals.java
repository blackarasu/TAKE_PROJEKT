/**
 * 
 */
package pl.take.liga.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents list of goals entities
 * Used for easier marshaling 
 * @author Bartek
 * @version 1.1
 */
@XmlRootElement
public class Goals {
	/**
	 * list of goals returned by entity manager
	 */
	private List<Goal> goals = new ArrayList<Goal>();

	public Goals(){}
	/**
	 * Set list of goals to object
	 * @param goals list of goals
	 */
	public Goals(List<Goal> goals){
		this.goals = goals;
	}
	/**
	 * used by xmlMarshal
	 * @return list of goals
	 */
	@XmlElement(name ="goal")
	public List<Goal> getGoals() {
		return goals;
	}
	/**
	 * Used by xmlUnMarshal
	 * @param goals list of goals
	 */
	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}
}
