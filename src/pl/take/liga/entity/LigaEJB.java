/**
 * 
 */
package pl.take.liga.entity;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

/**
 * EJB for LigaREST
 * All operations on database are executed here.
 * @author Bartek
 * @version 0.8
 */
@Stateful
public class LigaEJB {

	/**
	 * EntityManager
	 */
	@PersistenceContext(name="liga", type=PersistenceContextType.EXTENDED)
	EntityManager manager;
	public LigaEJB(){
		
	}
	/**
	 * Add object T which is Entity to database
	 * @param t all kind of Entity
	 */
	public <T> void create(T t)
	{
		manager.persist(t);
	}
	/**
	 * Generic method to find entity by Id
	 * @param id prime key of Entity to find
	 * @param type it is a Class type ie. Player.class
	 * @return Entity
	 */
	public <T> T get(Object id, Class<T> type)
	{
		return manager.find(type, id);
	}
	/**
	 * Update entity in database
	 * @param t Entity object
	 */
	public <T> void update(T t)
	{
		manager.merge(t);
	}
	/**
	 * Delete Entity from database
	 * @param t Entity object
	 */
	public <T> void delete(T t)
	{
		Object ref = manager.merge(t);
		manager.remove(ref);
	}
	
	/**
	 * Finds all elements in table t - where t is the class wich contains the name of the table
	 * ie. class name is Car and the same table in database is called Car
	 * @param t is an empty object of an entity
	 * @return list of any objects 
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(T t)
	{
		return manager.createQuery("from "+t.getClass().getSimpleName()).getResultList();
	}
	/**
	 * Find players by a first name and a last name
	 * @param firstName String first name
	 * @param lastName String last name
	 * @return List of player who are with exact first name and last name as firstName and lastName params
	 */
	public List<Player> findByFullName(String firstName, String lastName){
		 TypedQuery<Player> q = manager.createNamedQuery("Player.findByFullName", Player.class);
		 q.setParameter("firstName", firstName);
		 q.setParameter("lastName", lastName);
		 return q.getResultList();
	}
	/**
	 * Find players by a first name
	 * @param firstName first name to find
	 * @return list of players who first name is excact as firstName
	 */
	public List<Player> findByFirstName(String firstName){
		return manager.createNamedQuery("Player.findByfName",Player.class).setParameter("firstName", firstName).getResultList();
	}
	/**
	 * Find players by a last name
	 * @param lastName last name to find
	 * @return list of players who last name is exact as lastName
	 */
	public List<Player> findByLastName(String lastName){
		return manager.createNamedQuery("Player.findBylName",Player.class).setParameter("lastName", lastName).getResultList();
	}
	/**
	 * Find player by an age
	 * @param age age to find
	 * @return list of players with exact age as age parameter
	 */
	public List<Player> findByAge(Integer age){
		return manager.createNamedQuery("Player.findByAge", Player.class).setParameter("age", age).getResultList();
	}
	/**
	 * Find players who plays at exact position
	 * @param position position to find
	 * @return list of players with the same position as parameter
	 */
	public List<Player> findByPosition(String position){
		return manager.createNamedQuery("Player.findByPosition", Player.class).setParameter("position", position).getResultList();
	}
	/*
	 * Not implemented in REST
	 */
	/**
	 * Find matches by amount of goals
	 * @param homeGoals Integer
	 * @param guestGoals Integer
	 * @return list of matches where score is the same as homeGoals:guestGoals
	 */
	public List<LigaMatch> findByScore (Integer homeGoals, Integer guestGoals){
		TypedQuery<LigaMatch> q = manager.createNamedQuery("LigaMatch.findByScore", LigaMatch.class);
		q.setParameter("homeGoals", homeGoals);
		q.setParameter("guestGoals", guestGoals);
		return q.getResultList();
	}
	/*
	 * Not implemented in REST
	 */
	/**
	 * Find matches by homeGoals
	 * @param homeGoals Integer
	 * @return list of matches where score is homeGoals:?
	 */
	public List<LigaMatch> findByHomeGoals(Integer homeGoals){
		return manager.createNamedQuery("LigaMatch.findByHomeGoals", LigaMatch.class).setParameter("homeGoals", homeGoals).getResultList();
	}
	/*
	 * NOT implemented in REST
	 */
	/**
	 * Find matches by guest goals
	 * @param guestGoals Integer goals scored by guest to find
	 * @return list of matches where score is ?:guestGoals
	 */
	public List<LigaMatch> findByGuestGoals(Integer guestGoals){
		return manager.createNamedQuery("LigaMatch.findByGuestGoals", LigaMatch.class).setParameter("guestGoals", guestGoals).getResultList();
	}
	/**
	 * Find clubs by city
	 * @param city String
	 * @return list of clubs from city
	 */
	public List<Club> findByCity(String city){
		return manager.createNamedQuery("Club.findByCity",Club.class).setParameter("city", city).getResultList();
	}
	/**
	 * Find clubs by name
	 * @param name String 
	 * @return list of clubs with name in their 'fullName'
	 */
	public List<Club> findByName(String name){
		return manager.createNamedQuery("Club.findByName", Club.class).setParameter("name", name).getResultList();
	}
	/**
	 * Find club by full name (city and name)
	 * @param name String
	 * @param city String 
	 * @return club where his full name is exact as name and city combined
	 */
	public Club findByFullNameClub(String name, String city){
		TypedQuery<Club> q = manager.createNamedQuery("Club.findByFullName", Club.class);
		q.setParameter("name", name);
		q.setParameter("city", city);
		Club club = null;
		try{
			club = q.getSingleResult();
		}catch(NoResultException er){
		}
		return club;
	}
	/*
	 * NOT implemented in REST
	 */
	/**
	 * Find goals by minute
	 * @param atMinute Integer
	 * @return list of goals scored atMinute
	 */
	public List<Goal> findByMinute(Integer atMinute){
		return manager.createNamedQuery("Goal.findByMinute", Goal.class).setParameter("atMinute", atMinute).getResultList();
	}
	
	/*public List<Goal> findByScorerId(Integer id){
		TypedQuery<Goal> q = manager.createNamedQuery("Goal.findByScorer", Goal.class);
		q.setParameter("score_id", id);
		return q.getResultList();
	}
	
	public List<Goal> findByAssistantId(Integer id){
		TypedQuery<Goal> q = manager.createNamedQuery("Goal.findByAssistant", Goal.class);
		q.setParameter("assist_id", id);
		return q.getResultList();
	}
	
	public List<Goal> findParticipation(Integer id){
		TypedQuery<Goal> q = manager.createNamedQuery("Goal.findByParticipation", Goal.class);
		q.setParameter("id", id);
		return q.getResultList();
	}
	*/
	/**
	 * 
	 * @param id - player id
	 * @return all player's goals
	 */
	public List<Goal> findByScorerId(Integer id){
		Player p = this.get(id, Player.class);
		return p.getGoals();
	}
	/**
	 * 
	 * @param id - player id
	 * @return all player's assists
	 */
	public List<Goal> findByAssistantId(Integer id){
		Player p = this.get(id, Player.class);
		return p.getAssists();
	}
	/**
	 * 
	 * @param id - player identificator
	 * @return all goals which are participated by player
	 */
	public List<Goal> findByParticipation(Integer id){
		Player p = this.get(id, Player.class);
		List<Goal> participation = new ArrayList<>();
		if(!p.getAssists().isEmpty())
			participation.addAll(p.getAssists());
		if(!p.getGoals().isEmpty())
			participation.addAll(p.getGoals());
		return participation; 
	}
	/**
	 * 
	 * @param id - player identificator
	 * @return player's club
	 */
	public Club findyByPlayerId(Integer id){
		Player p = this.get(id, Player.class);
		return p.getClub();
	}
	/**
	 * 
	 * @param id - club identificator
	 * @return all players from club
	 */
	public List<Player> findPlayersByClubId(Integer id){
		Club c = this.get(id, Club.class);
		return c.getPlayers();
	}
	/**
	 * 
	 * @param id - club identificator
	 * @return all club's matches
	 */
	public List<LigaMatch> findMatchesByClubId(Integer id){
		Club c = this.get(id, Club.class);
		List<LigaMatch> matches = new ArrayList<LigaMatch>();
		if(!c.getHomeMatches().isEmpty()){
			matches.addAll(c.getHomeMatches());
		}
		if(!c.getGuestMatches().isEmpty()){
			matches.addAll(c.getGuestMatches());
		}
		return matches;
	}
	/**
	 * 
	 * @param id - match identificator
	 * @return List of goals in match
	 */
	public List<Goal> findGoalsByMatch(Integer id){
		LigaMatch lm = this.get(id, LigaMatch.class);
		return lm.getGoals();
	}
	/**
	 * 
	 * @param id - goal identificator
	 * @return match in which goal was scored
	 */
	public LigaMatch findMatchByGoal(Integer id){
		Goal g = this.get(id, Goal.class);
		return g.getMatch();
	}
	public List<LigaMatch> findMatchesBetweenClubs(Integer clubId1,Integer clubId2){
		TypedQuery<LigaMatch> q = manager.createNamedQuery("LigaMatch.findBetweenTeams", LigaMatch.class);
		q.setParameter("clubId1", clubId1);
		q.setParameter("clubId2", clubId2);
		return q.getResultList();
	}
}
