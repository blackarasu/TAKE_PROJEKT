/**
 * 
 */
package pl.take.liga.entity;

import javax.ejb.Local;
/**
 * Interface for LigaREST
 * NOTE: All GET REST functions return response.status = 204 if element wasn NOT found
 * @author Bartek
 * @version 0.8
 */
@Local
public interface Liga {
	/**
	 * path: /liga/rest/liga/player method: POST
	 * You can send here XmlRootElement named player
	 * with elements such as:
	 * firstName - String  
	 * lastName - String
	 * age - Integer
	 * position - 3 characters represents position
	 * club_id - id of club used to create relationship
	 * @param player xmlUnmarhsalled object
	 * @return String if everything went ok or ERROR String
	 */
	public abstract String create(Player player);
	/**
	 * path: /liga/rest/liga/goal method: POST
	 * You can send here XmlRootElement named goal
	 * with elements such as:
	 * atMinute - Integer represents in which minute goal was scored
	 * match_id - id of match used to create relationship
	 * scorer_id - id of player who scored the goal
	 * assist_id - id of player who assisted the goal
	 * NOTE: assist_id can be ignored if no one assisted
	 * @param goal xmlUnmarshalled object
	 * @return String if everything went OK or ERROR String
	 */
	public abstract String create(Goal goal);
	/**
	 * path: /liga/rest/liga/club method: POST
	 * You can send here XmlRootElement named club
	 * with elements such as:
	 * name - String represents name of club
	 * city - String represents city of club
	 * @param club club xmlUnmarshalled object
	 * @return String if everything went OK or ERROR String
	 */
	public abstract String create(Club club);
	/**
	 * path: /liga/rest/liga/match method: POST
	 * You can send here XmlRootElement named match
	 * with elements such as:
	 * home_id - id of club who played as host
	 * guest_id id of club who played as guest
	 * NOTE: home_id cannot be the same as guest_id
	 * @param match match xmlUnmarshalled object
	 * @return String if everything went ok or ERROR String
	 */
	public abstract String create(LigaMatch match);
	/**
	 * path: /liga/rest/liga/player/{idc} method: GET
	 * where idc is id to find a player
	 * @param id identity (Integer) of player to find
	 * @return XmlElementRoot named Player with elements such as:
	 * XmlAttribute id - id of an element
	 * firstName - String first name of player
	 * lastName - String last name of player
	 * age - Integer age of player
	 * position - String of 3 characters as abbreviation of position 
	 * XmlRootElement called club with elements such as:
	 * city - String club's city where the player belongs
	 * name - String club's name where the player belongs
	 */
	public abstract Player findPlayer(Integer id);
	/**
	 * path: /liga/rest/liga/goal/{idc} method: GET
	 * where idc is id to find a goal
	 * @param id identity (Integer) of goal to find
	 * @return XmlRootElement called goal with elements such as:
	 * XmlAttribute id - id of an element
	 * atMinute - Integer says at which minute goal was scored
	 * scorer which contains same information as player has(look at path: /liga/rest/liga/player/{idc} method: GET)
	 * assist same as scorer
	 * match which contains same informations as match (xmlElement)
	 */
	public abstract Goal findGoal(Integer id);
	/**
	 * path: /liga/rest/liga/player/{idc} method: GET
	 * where idc is id to find a club
	 * @param id identity (Integer) of club to find
	 * @return XmlRootElement called club with elements such as:
	 * XmlAttribute id - id of an element
	 * city - club's city
	 * name - club's name
	 */
	public abstract Club findClub(Integer id);
	/**
	 * path: /liga/rest/liga/match/{idc} method: GET
	 * where idc is id to find a match
	 * @param id identity (Integer of match to find
	 * @return XmlRootElement called match with elements such as
	 * XmlAttribute id - id of an element
	 * homeGoals - Integer amount of goals scored by host
	 * guestGoals - Integer amount of goals scored by guest
	 * homeTeam - contains same informations as club
	 * guestTeam - contains same informations as club
	 */
	public abstract LigaMatch findMatch(Integer id);
	/**
	 * path: /liga/rest/liga/players method: GET
	 * @return XmlRootElement called players with elements such as:
	 * player - same informations like in player but multiplied
	 */
	public abstract Players getPlayers();
	/**
	 * path: "/liga/rest/liga/player?fn&ln&p&a" method: GET
	 * where:
	 * fn is a first name of player
	 * ln is a last name of player
	 * p is a position of player
	 * a is an age of player
	 * If first name and last name are not null it returns
	 * players who has first name and last name exact as 'fn' and 'ln'
	 * If age is not null you will got players who has exact age as 'a'
	 * If position is not null you will got players who has exact position as 'p'
	 * IF all parameters are null it will return all players
	 * @param firstName String 
	 * @param lastName String
	 * @param position String
	 * @param age Integer
	 * @return XmlRootElement called players with elements such as:
	 * player - same informations like in player but multiplied
	 */
	public abstract Players getPlayers(String firstName, String lastName, String position, String age);
	/**
	 * Used by getPlayers()
	 * @param firstName String
	 * @return object Players who have same first name as parameter firstName
	 */
	public abstract Players getPlayersByFirstName(String firstName);
	/**
	 * Used by getPlayers()
	 * @param lastName String
	 * @return object Players who have same last name as parameter lastName
	 */
	public abstract Players getPlayersByLastName(String lastName);
	/**
	 * Used by getPlayers()
	 * @param firstName String
	 * @param lastName String
	 * @return object Players who have same first and last name as parameters lastName and firstName
	 */
	public abstract Players getPlayersByFullName(String firstName, String lastName);
	/**
	 * Used by getPlayers
	 * @param age Integer
	 * @return object Players who have same age as parameter age
	 */
	public abstract Players getPlayersByAge(Integer age);
	/**
	 * Used by getPlayers()
	 * @param position String
	 * @return object Players who have same position as parameter position
	 */
	public abstract Players getPlayersByPosition(String position);
	/**
	 * path: liga/rest/liga/player/{idc}/goals method: GET
	 * where idc is identity of player you want to get all goals 
	 * @param id Integer
	 * @return XmlRootElement goals which contains:
	 * goal XmlElement which contains same informations as goal(look at /liga/rest/liga/goal/{idc})
	 */
	public abstract Goals getPlayersGoals(Integer id);
	/**
	 * path: liga/rest/liga/player/{idc}/assists method: GET
	 * where idc is identity of player you want to get all assists 
	 * @param id Integer
	 * @return XmlRootElement goals which contains:
	 * goal XmlElement which contains same informations as goal(look at /liga/rest/liga/goal/{idc})
	 */
	public abstract Goals getPlayersAssists(Integer id);
	/**
	 * path: liga/rest/liga/player/{idc}/participation method: GET
	 * where idc is identity of player you want to get all goals and assists 
	 * @param id Integer
	 * @return XmlRootElement goals which contains:
	 * goal XmlElement which contains same informations as goal(look at /liga/rest/liga/goal/{idc})
	 */
	public abstract Goals getPlayersParticipation(Integer id); //goals+assists
	/**
	 * path: liga/rest/liga/goals method: GET
	 * @return XmlRootElement goals which contains:
	 * goal XmlElement which contains same informations as goal(look at /liga/rest/liga/goal/{idc})
	 */
	public abstract Goals getGoals();
	/**
	 * path: liga/rest/liga/clubs method: GET
	 * @return XmlRootElement clubs which contains:
	 * club XmlElement which contains same informations as club(look at /liga/rest/liga/club/{idc})
	 */
	public abstract Clubs getClubs();
	/**
	 * Used by Clubs getClubs(String name, String city);
	 * @param name String
	 * @param city String
	 * @return object Club
	 */
	public abstract Club getClubByFullName(String name, String city);
	/**
	 * Used by Clubs getClubs(String name, String city);
	 * @param city String
	 * @return object Clubs
	 */
	public abstract Clubs getClubsByCity(String city);
	/**
	 * Used by Clubs getClubs(String name, String city);
	 * @param name String
	 * @return object Clubs
	 */
	public abstract Clubs getClubsByName(String name);
	/**
	 * Get all clubs defined by n and/or c
	 * path: "liga/rest/liga/club?n&c" method: GET
	 * where:
	 * n is a club's name
	 * c is a club's city
	 * If city and name is not null it will get Clubs (one) which have
	 * city and name same as parameters name and city
	 * If both parameters are null it will return all clubs
	 * @param name String
	 * @param city String 
	 * @return XmlRootElement named Clubs which contains:
	 * XmlElement Club which have same elements as Club look at /liga/rest/liga/club/{idc}
	 */
	public abstract Clubs getClubs(String name, String city);
	/**
	 * Get all club's matches identified by id
	 * path /liga/rest/liga/club/{idc}/matches method: GET
	 * where idc is a identity of club
	 * @param Id Integer
	 * @return XmlRootElement named matches which contains:
	 * XmlElement match which contains same elements as match (look at /liga/rest/liga/match/{idc})
	 */
	public abstract LigaMatches getClubsMatches(Integer Id);
	/**
	 * Get all club's matches with the fullName
	 * path "/liga/rest/liga/club/matches?n&c" method: GET
	 * where:
	 * n is a name of club to find
	 * c is a city of club to find
	 * NOTE: both parameters cannot be empty
	 * @param name String
	 * @param city String
	 * @return XmlRootElement named matches which contains:
	 * XmlElement match which contains same elements as match (look at /liga/rest/liga/match/{idc})
	 */
	public abstract LigaMatches getClubsMatches(String name, String city);
	/**
	 * Get all players who belong to the club
	 * path /liga/rest/liga/club/{idc}/players method: GET
	 * where idc is an Identity of club
	 * @param id Integer
	 * @return XmlRootElement named players which contains:
	 * XmlElement player which contains same elements as player (look at /liga/rest/liga/player/{idc})
	 */
	public abstract Players getClubsPlayers(Integer id);
	
	/**
	 * Get all matches played in current league
	 * path: /liga/rest/liga/matches method: GET
	 * @return XmlRootElement named matches which contains:
	 * XmlElement match which contains same elements as match (look at /liga/rest/liga/match/{idc})
	 */
	public abstract LigaMatches getMatches();
	/**
	 * Get all goals scored in match defined by idc
	 * path /liga/rest/liga/match/{idc}/goals method: GET
	 * where idc is an Identity of a match
	 * @param id Integer
	 * @return XmlRootElement named goals which contains:
	 * XmlElement goal which contains same elements as goal (look at /liga/rest/liga/goal/{idc})
	 */
	public abstract Goals getMatchesGoals(Integer id);
	/**
	 * Update information about player
	 * path /liga/rest/liga/player method: PUT
	 * NOTE: IF even after successful update your player
	 * did not change relationship club
	 * you need to check if club exists in database.
	 * send request to /liga/rest/liga/club/{idc} where idc is a Identity of club
	 * NOTE 2: Do NOT send any goals objects or assists or club object because 
	 * it will be overwritten if object is found in database.
	 * @param player XmlRootElement player which contains
	 * same informations as player (look at /liga/rest/liga/player method: POST)
	 * @return String whether player was updated or not
	 */
	public abstract String update(Player player);
	/**
	 * Update information about goal
	 * path /liga/rest/liga/goal method: PUT
	 * NOTE: IF even after successful update your match
	 * did not change relationship for scorer or assist
	 * you need to check if players exist in database.
	 * send request to /liga/rest/liga/player/{idc} where idc is a Identity of player
	 * similar thing for match
	 * @param goal XmlRootElement goal which contains
	 * same informations as goal (look at /liga/rest/liga/goal method: POST)
	 * @return String whether goal was updated or not
	 */
	public abstract String update(Goal goal);
	/**
	 * Update information about club
	 * path /liga/rest/liga/club method: PUT
	 * NOTE: Do NOT send any guest/home matches objects or players because
	 * it will be overwritten if object is found in database.
	 * @param club XmlRootElement club which contains
	 * same informations as club (look at /liga/rest/liga/club method: POST)
	 * @return String whether club was updated or not
	 */
	public abstract String update(Club club);
	/**
	 * Update information about match
	 * path /liga/rest/liga/match method: PUT
	 * NOTE: IF even after successful update your match
	 * did not change relationship between two teams it means
	 * you need to check if teams exist in database.
	 * send request to /liga/rest/liga/club/{idc} where idc is a Identity of club
	 * NOTE 2: Do NOT send any goals objects or scores because
	 * it will be overwritten if object is found in database.
	 * @param match XmlRootElement match which contains
	 * same informations as match (look at /liga/rest/liga/match method: POST)
	 * @return String whether match was updated or not
	 */
	public abstract String update(LigaMatch match);
	/**
	 * Delete player identified by idc
	 * path: /liga/rest/liga/player/{idc} method: DELETE
	 * where idc is an player Identity
	 * @param id Integer
	 * @return whether element was deleted or not
	 */
	public abstract String deletePlayer(Integer id);
	/**
	 * Delete goal identified by idc
	 * path: /liga/rest/liga/goal/{idc} method: DELETE
	 * where idc is an goal Identity
	 * @param id Integer
	 * @return whether element was deleted or not
	 */
	public abstract String deleteGoal(Integer id);
	/**
	 * Delete club identified by idc
	 * path: /liga/rest/liga/club/{idc} method: DELETE
	 * where idc is an club Identity
	 * @param id Integer
	 * @return whether element was deleted or not
	 */
	public abstract String deleteClub(Integer id);
	/**
	 * Delete match identified by idc
	 * path: /liga/rest/liga/match/{idc} method: DELETE
	 * where idc is an match Identity
	 * @param id Integer
	 * @return whether element was deleted or not
	 */
	public abstract String deleteMatch(Integer id);
	/**
	 * path /liga/rest/liga/club/{idc}/player method: POST
	 * where idc is an Identity of club
	 * You can send here XmlRootElement Player same as
	 * path /liga/rest/liga/player method: POST
	 * NOTE: as you go to path where you chose to which club you want 
	 * to add a new player, you don't need to send it in XML 
	 * @param id Integer
	 * @return String Error if something went wrong or some fancy information
	 */
	public abstract String addPlayerToClub(Integer id, Player player);
	/**
	 * path /liga/rest/liga/clubs/c1,c2/matches
	 * where c1 and c2 are club identity 1 and 2
	 * @param clubId1 Integer clubId
	 * @param clubId2 Integer clubId
	 * @return LigaMatches if matches are found, otherwise it returns null
	 */
	public abstract LigaMatches findMatchesBetweenClubs(Integer clubId1, Integer clubId2);
}
