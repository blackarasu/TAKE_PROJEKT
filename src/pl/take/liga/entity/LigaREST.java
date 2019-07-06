/**
 * 
 */
package pl.take.liga.entity;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * Whole REST API class
 * @author Bartek
 * @version 1.0
 */
@Path("/liga")
//@Consumes({ "application/json" })
//@Produces({ "application/json" })

@Consumes({ "application/xml; charset=UTF-8" })
@Produces({ "application/xml; charset=UTF-8" })
public class LigaREST implements Liga {
	
	Player player = new Player();
	Goal goal = new Goal();
	Club club = new Club();
	LigaMatch match = new LigaMatch();
	@EJB
	LigaEJB bean;
	
	@Override
	@POST
	@Path("/player")
	public String create(Player player) {
		String msg="ERROR: Could not add player to database.\n";
		String admin_msg = "Please make sure you send correct id or contact with administration.";
		if(player.getFirstName().equals("") || player.getFirstName() == null)
		{
			return "First Name is required.";
		}
		if(player.getLastName().equals("") || player.getLastName() == null){
			return "Last Name is required.";
		}
		if(player.getAge()==null || player.getAge().equals(0)){
			return "Age is required.";
		}
		if(player.getPosition()==null || player.getPosition().equals(""))
		{
			return "Position is required.";
		}
		Club c = null;
		if(player.getClub_id()!=null){
			c = bean.get(player.getClub_id(), Club.class);
		}
		else{
			return msg+"You did NOT send match_id as an Element of 'player' XmlRootElement."+admin_msg;
		}
		if(c!=null){
			System.out.println(player.getFirstName()+player.getLastName()+player.getPosition());
			player.setClub(c);
			bean.create(player);
			return "Added player "+player.getFirstName()+" "+player.getLastName()+" to "+c.getName()+" "+c.getCity();
		}
		return "I could NOT add player to club because it does NOT exist. Creating player has been stopped.";
	}

	@Override
	@POST
	@Path("/goal")
	public String create(Goal goal) {
		String msg="ERROR: Could not add goal to database.\n";
		String admin_msg = "Please make sure you send correct id or contact with administration.";
		System.out.println("Jak to nullpointer");
		Integer match_id = goal.getMatch_id();
		Integer scorer_id = goal.getScorer_id();
		Integer assist_id = goal.getAssist_id();
		if(match_id==null || scorer_id==null){
			if(match_id==null){
				msg+="Did NOT find match_id element. ";
			}
			if(scorer_id==null){
				msg+="Did NOT find scorer_id element. ";
			}
			return msg+admin_msg;
		}
		if(scorer_id.equals(assist_id)){
			return msg+"Scorer and assistant can NOT be same person";
		}
		LigaMatch m = bean.get(match_id, LigaMatch.class);
		Player s = bean.get(scorer_id, Player.class);
		Player a = null;
		if(assist_id != null && !assist_id.equals(0)){
			a = bean.get(assist_id, Player.class);
		}
		if(m==null || s==null || (!assist_id.equals(0) && assist_id!=null && a==null)){
			if(m==null){
				msg+="Did NOT find match in database. ";
			}
			if(s==null){
				msg+="Did NOT find scorer in database. ";
			}
			if(a==null){
				msg+="Did NOT find assistant in database. ";
			}
			return msg+admin_msg;
		}
		Club scorer_club = s.getClub();
		if(scorer_club == m.getGuestTeam()){
			m.increseGuestGoal();
		}
		else if(scorer_club == m.getHomeTeam()){
			m.increseHomeGoal();
		}
		else{
			return msg+"Scorer's team did NOT take participation in current match "+admin_msg;
		}
		goal.setMatch(m);
		goal.setScorer(s);
		if(assist_id != null && !assist_id.equals(0)){
			goal.setAssist(a);
		}
		bean.create(goal);
		return "Goal has been added succesfully";
	}

	@Override
	@POST
	@Path("/club")
	public String create(Club club) {	
		//add id if you want check some club's fields
		//String msg="ERROR: Could not add club to database.\n";
		//String admin_msg = "Please make sure you send correct id or contact with administration.";
		if(club.getCity().equals("")||club.getCity()==null){
			return "City is required.";
		}
		if(club.getName()==null||club.getName().equals("")){
			return "Name is required.";
		}
		bean.create(club);
		return "Club has been added succesfully";
	}
	
	@Override
	@POST
	@Path("/match")
	public String create(LigaMatch match) {
		String msg="ERROR: Could not add match to database.\n";
		String admin_msg = "Please make sure you send correct id or contact with administration.";
		Integer home_id = match.getHome_id();
		Integer guest_id = match.getGuest_id();
		match.setGuestGoals(0);
		match.setHomeGoals(0);
		if(home_id == null || guest_id==null){
			if(home_id == null){
				msg+="Did NOT find home_id element in XML. ";
			}
			if(guest_id == null){
				msg+="Did NOT find guest_id element in XML.";
			}
			return msg+admin_msg;
		}
		if(home_id.equals(guest_id)){
			return msg+"Guest team and home team cannot be the same team.";
		}
		Club h = bean.get(home_id, Club.class);
		Club g = bean.get(guest_id, Club.class);
		if(h==null || g == null){
			if(h==null){
				msg+="Did not find home team in database. ";
			}
			if(g==null){
				msg+="Did not find guest team in database";
			}
			return msg+admin_msg;
		}
		match.setGuestTeam(g);
		match.setHomeTeam(h);
		bean.create(match);
		return "Match has been added succesfully";
	}

	@Override
	@GET
	@Path("/player/{idc}")
	public Player findPlayer(@PathParam("idc") Integer id) {
		return bean.get(id, Player.class);
	}

	@Override
	@GET
	@Path("/goal/{idc}")
	public Goal findGoal(@PathParam("idc") Integer id) {
		return bean.get(id, Goal.class);
	}

	@Override
	@GET
	@Path("/club/{idc}")
	public Club findClub(@PathParam("idc") Integer id) {
		return bean.get(id, Club.class);
	}

	@Override
	@GET
	@Path("/match/{idc}")
	public LigaMatch findMatch(@PathParam("idc") Integer id) {
		return bean.get(id, LigaMatch.class);
	}

	@Override
	@GET
	@Path("/players")
	public Players getPlayers() {
		List<Player> lplayers = bean.findAll(this.player);
		if(lplayers.isEmpty()){
			return null;
		}
		Players players = new Players(lplayers);
		return players;
	}
	/*
	 * used in getPlayers(params)
	 * (non-Javadoc)
	 * @see pl.take.liga.entity.Liga#getPlayersByFirstName(java.lang.String)
	 */
	@Override
	public Players getPlayersByFirstName(String firstName) {
		List<Player> lplayers = bean.findByFirstName(firstName);
		if(lplayers.isEmpty()){
			return null;
		}
		Players players = new Players(lplayers);
		return players;
	}
	/*
	 * used in getPlayers(params)
	 * (non-Javadoc)
	 * @see pl.take.liga.entity.Liga#getPlayersByLastName(java.lang.String)
	 */
	@Override
	public Players getPlayersByLastName(String lastName) {
		List<Player> lplayers = bean.findByLastName(lastName);
		if(lplayers.isEmpty()){
			return null;
		}
		Players players = new Players(lplayers);
		return players;
	}
	/*
	 * used in getPlayers(params)
	 * (non-Javadoc)
	 * @see pl.take.liga.entity.Liga#getPlayersByFullName(java.lang.String, java.lang.String)
	 */
	@Override
	public Players getPlayersByFullName(String firstName,String lastName) {
		List<Player> lplayers = bean.findByFullName(firstName, lastName);
		if(lplayers.isEmpty()){
			return null;
		}
		Players players = new Players(lplayers);
		return players;
	}
	/*
	 * used in getPlayers(params)
	 * (non-Javadoc)
	 * @see pl.take.liga.entity.Liga#getPlayersByAge(java.lang.Integer)
	 */
	@Override
	public Players getPlayersByAge(Integer age) {
		List<Player> lplayers = bean.findByAge(age);
		if(lplayers.isEmpty()){
			return null;
		}
		Players players = new Players(lplayers);
		return players;
	}
	/*
	 * used in getPlayers(params)
	 * (non-Javadoc)
	 * @see pl.take.liga.entity.Liga#getPlayersByPosition(java.lang.String)
	 */
	@Override
	public Players getPlayersByPosition(String position) {
		List<Player> lplayers = bean.findByPosition(position);
		if(lplayers.isEmpty()){
			return null;
		}
		Players players = new Players(lplayers);
		return players;
	}

	@Override
	@GET
	@Path("/player/{idc}/goals")
	public Goals getPlayersGoals(@PathParam("idc") Integer id) {
		List<Goal> lgoals = bean.findByScorerId(id);
		if(lgoals.isEmpty()){
			return null;
		}
		Goals goals = new Goals(lgoals);
		return goals;
	}

	@Override
	@GET
	@Path("/player/{idc}/assists")
	public Goals getPlayersAssists(@PathParam("idc") Integer id) {
		List<Goal> lgoals = bean.findByAssistantId(id);
		if(lgoals.isEmpty()){
			return null;
		}
		Goals goals = new Goals(lgoals);
		return goals;
	}

	@Override
	@GET
	@Path("/player/{idc}/participation")
	public Goals getPlayersParticipation(@PathParam("idc") Integer id) {
		List<Goal> lgoals = bean.findByParticipation(id);
		if(lgoals.isEmpty()){
			return null;
		}
		Goals goals = new Goals(lgoals);
		return goals;
	}

	@Override
	@GET
	@Path("/goals")
	public Goals getGoals() {
		List<Goal> lgoals = bean.findAll(goal);
		if(lgoals.isEmpty()){
			return null;
		}
		Goals goals = new Goals(lgoals);
		return goals;
	}

	@Override
	@GET
	@Path("/clubs")
	public Clubs getClubs() {
		List<Club> lclubs = bean.findAll(club);
		if(lclubs.isEmpty()){
			return null;
		}
		Clubs clubs = new Clubs(lclubs);
		return clubs;
	}

	@Override
	@GET
	@Path("/matches")
	public LigaMatches getMatches() {
		List<LigaMatch> lmatches = bean.findAll(match);
		if(lmatches.isEmpty()){
			return null;
		}
		LigaMatches matches = new LigaMatches(lmatches);
		return matches;
	}
	
	@Override
	@PUT
	@Path("/player")
	public String update(Player player) {
		String msg="ERROR: Could not update player to database.\n";
		String admin_msg = "Please make sure you send correct id or contact with administration.";
		Player tmp = bean.get(player.getId(), Player.class);
		if(tmp==null){
			return msg+"There is no such a player in database. "+admin_msg;
		}
		player.setAssists(tmp.getAssists());
		player.setGoals(tmp.getGoals());
		player.setClub(tmp.getClub());
		Integer club_id = player.getClub_id();
		if(club_id!=null){
			Club club = bean.get(club_id, Club.class);
			if(club!=null){
				player.setClub(club);
			}
			else{
				msg+="Club did NOT find to transfer player to new club. ";
			}
		}
		try {
			bean.update(player);
			return "Player has been updated!";
		} catch (Exception e) {
			e.printStackTrace();
			return msg+"Player has NOT been updated"+admin_msg;
		}
	}

	@Override
	@PUT
	@Path("/goal")
	public String update(Goal goal) {
		String msg="ERROR: Could not update goal to database.\n";
		String admin_msg = "Please make sure you send correct id or contact with administration.";
		Goal tmp = bean.get(goal.getId(), Goal.class);
		if(tmp==null){
			return msg+"There is no such a goal in database. "+admin_msg;
		}
		goal.setAssist(tmp.getAssist());
		goal.setScorer(tmp.getScorer());
		goal.setMatch(tmp.getMatch());
		Integer match_id = goal.getMatch_id();
		Integer scorer_id = goal.getScorer_id();
		Integer assist_id = goal.getAssist_id();
		if(match_id!=null){
			LigaMatch match = bean.get(match_id, LigaMatch.class);
			if(match!=null){
				goal.setMatch(match);
			}
			else{
				msg+="Match did NOT find in database. ";
			}
		}
		if(scorer_id!=null){
			Player scorer = bean.get(scorer_id, Player.class);
			if(scorer != null){
				goal.setScorer(scorer);
			}
			else{
				msg+="Scorer did NOT find in database. ";
			}
		}
		if(assist_id!=null){
			Player assist = bean.get(assist_id, Player.class);
			if(assist != null){
				goal.setAssist(assist);
			}
			else{
				msg+="Assistant did NOT find in database. ";
			}
		}
		try {
			bean.update(goal);
			return "Goal has been updated!";
		} catch (Exception e) {
			e.printStackTrace();
			return msg+"Goal has NOT been updated"+admin_msg;
		}
	}

	@Override
	@PUT
	@Path("/club")
	public String update(Club club) {
		String msg="ERROR: Could not update club to database.\n";
		String admin_msg = "Please make sure you send correct id or contact with administration.";
		Club tmp = bean.get(club.getId(), Club.class);
		if(tmp==null){
			return msg+"There is no such a club in database. "+admin_msg;
		}
		club.setGuestMatches(tmp.getGuestMatches());
		club.setHomeMatches(tmp.getHomeMatches());
		club.setPlayers(tmp.getPlayers());
		try {
			bean.update(club);
			return "Club has been updated!";
		} catch (Exception e) {
			e.printStackTrace();
			return msg+"Club has NOT been updated"+admin_msg;
		}
	}

	@Override
	@PUT
	@Path("/match")
	public String update(LigaMatch match) {
		String msg="ERROR: Could not update player to database.\n";
		String admin_msg = "Please make sure you send correct id or contact with administration.";
		LigaMatch tmp = bean.get(match.getId(), LigaMatch.class);
		if(tmp == null){
			return msg+"There is no such match in database. "+admin_msg;
		}
		match.setGoals(tmp.getGoals());
		match.setGuestGoals(tmp.getGuestGoals());
		match.setHomeGoals(tmp.getHomeGoals());
		match.setGuestTeam(tmp.getGuestTeam());
		match.setHomeTeam(tmp.getHomeTeam());
		Integer home_id = match.getHome_id();
		Integer guest_id = match.getGuest_id();
		if(home_id.equals(guest_id)){
			return msg+"You can NOT match one team eachother. "+admin_msg;
		}
		if(home_id != null){
			Club homeTeam = bean.get(home_id, Club.class);
			if(homeTeam!=null){
				match.setHomeTeam(homeTeam);
			}
			else{
				msg+="Home team did NOT find in database. ";
			}
		}
		if(guest_id != null){
			Club guestTeam = bean.get(guest_id, Club.class);
			if(guestTeam != null){
				match.setGuestTeam(guestTeam);
			}
			else{
				msg+="Guest team did NOT find in database. ";
			}
		}
		try {
			bean.update(match);
			return "Match has been updated!";
		} catch (Exception e) {
			e.printStackTrace();
			return "Match has NOT been updated";
		}
	}

	@Override
	@DELETE
	@Path("/player/{idc}")
	public String deletePlayer(@PathParam("idc") Integer id) {
		try{
			Player toDelete = bean.get(id, Player.class);
			bean.delete(toDelete);
			return "Player has been deleted";
		}catch(Exception e){
			e.printStackTrace();
			return "Player could not be deleted";
		}
	}

	@Override
	@DELETE
	@Path("/goal/{idc}")
	public String deleteGoal(@PathParam("idc") Integer id) {
		try{
			Goal toDelete = bean.get(id, Goal.class);
			bean.delete(toDelete);
			return "Goal has been deleted";
		}catch(Exception e){
			e.printStackTrace();
			return "Goal could not be deleted";
		}
	}

	@Override
	@DELETE
	@Path("/club/{idc}")
	public String deleteClub(@PathParam("idc") Integer id) {
		try{
			Club toDelete = bean.get(id, Club.class);
			bean.delete(toDelete);
			return "Club has been deleted";
		}catch(Exception e){
			e.printStackTrace();
			return "Club could not be deleted";
		}
	}

	@Override
	@DELETE
	@Path("/match/{idc}")
	public String deleteMatch(@PathParam("idc") Integer id) {
		try{
			LigaMatch toDelete = bean.get(id, LigaMatch.class);
			bean.delete(toDelete);
			return "Match has been deleted";
		}catch(Exception e){
			e.printStackTrace();
			return "Match could not be deleted";
		}
	}
	/*
	 * used in getClubs(params)
	 * (non-Javadoc)
	 * @see pl.take.liga.entity.Liga#getClubByFullName(java.lang.String, java.lang.String)
	 */
	@Override
	public Club getClubByFullName(String name, String city) {
		return bean.findByFullNameClub(name, city);
	}
	/*
	 * used in getClubs(params)
	 * (non-Javadoc)
	 * @see pl.take.liga.entity.Liga#getClubsByCity(java.lang.String)
	 */
	@Override
	public Clubs getClubsByCity(String city) {
		List<Club> lclubs = bean.findByCity(city);
		if(lclubs.isEmpty()){
			return null;
		}
		Clubs clubs = new Clubs(lclubs);
		return clubs;
	}

	@Override
	@GET
	@Path("/player")
	public Players getPlayers(@QueryParam("fn") String firstName, @QueryParam("ln") String lastName, @QueryParam("p") String position, @QueryParam("a") String age) {
		if(firstName !=null && !firstName.equals("") && lastName != null && !lastName.equals("")){
			return this.getPlayersByFullName(firstName, lastName);
		}
		else if(firstName != null && !firstName.equals("")){
			return this.getPlayersByFirstName(firstName);
		}
		else if(lastName !=null && !lastName.equals("")){
			return this.getPlayersByLastName(lastName);
		}
		if(age!=null && !age.equals("")){
			Integer qAge=null;
			try{
				qAge = Integer.parseInt(age);
				return this.getPlayersByAge(qAge);
			}
			catch(Exception e){
				//System.out.println(e.getMessage());
			}
		}
		if(position!=null && !position.equals("")){
			return this.getPlayersByPosition(position);
		}
		return this.getPlayers();
	}

	@Override
	@GET
	@Path("/club")
	public Clubs getClubs(@QueryParam("n") String name,@QueryParam("c") String city) {
		if(name!=null && !name.equals("") && city!=null && !city.equals("")){
			Club tmp = this.getClubByFullName(name, city);
			if(tmp==null){
				return null;
			}
			Clubs club = new Clubs(tmp);
			return club;
		}
		else if(name!=null && !name.equals("")){
			return this.getClubsByName(name);
		}
		else if(city != null && !city.equals("")){
			return this.getClubsByCity(city);
		}
		return this.getClubs();
	}

	@Override
	@GET
	@Path("/club/{idc}/matches")
	public LigaMatches getClubsMatches(@PathParam("idc")Integer id) {
		List<LigaMatch> lmatches = bean.findMatchesByClubId(id);
		if(lmatches.isEmpty()){
			return null;
		}
		LigaMatches matches = new LigaMatches(lmatches);
		return matches;
	}
	
	/**
	 * query params are for club not for match
	 */
	@Override
	@GET
	@Path("/club/matches")
	public LigaMatches getClubsMatches(@QueryParam("n") String name,@QueryParam("c") String city) {
		Club club = this.getClubByFullName(name, city);
		List<LigaMatch> lmatches = new ArrayList<LigaMatch>();
		if(!club.getGuestMatches().isEmpty());
			lmatches.addAll(club.getGuestMatches());
		if(!club.getHomeMatches().isEmpty()){
			lmatches.addAll(club.getHomeMatches());
		}
		if(lmatches.isEmpty()){
			return null;
		}
		LigaMatches matches = new LigaMatches(lmatches);
		return matches;
	}
	/*
	 * used in getClubs(params)
	 * (non-Javadoc)
	 * @see pl.take.liga.entity.Liga#getClubsByName(java.lang.String)
	 */
	@Override
	public Clubs getClubsByName(String name) {
		List<Club> lclubs = bean.findByName(name);
		if(lclubs.isEmpty()){
			return null;
		}
		Clubs clubs = new Clubs(lclubs);
		return clubs;
	}

	@Override
	@GET
	@Path("/club/{idc}/players")
	public Players getClubsPlayers(@PathParam("idc")Integer id) {
		List<Player> lplayers = bean.findPlayersByClubId(id);
		if(lplayers.isEmpty()){
			return null;
		}
		Players players = new Players(lplayers);
		return players;
	}

	@Override
	@GET
	@Path("/match/{idc}/goals")
	public Goals getMatchesGoals(@PathParam("idc")Integer id) {
		List<Goal> lgoals = bean.findGoalsByMatch(id);
		if(lgoals.isEmpty()){
			return null;
		}
		Goals goals = new Goals(lgoals);
		return goals;
	}
	
	@Override
	@POST
	@Path("/club/{idc}/player")
	public String addPlayerToClub(@PathParam("idc")Integer id, Player player) {
		player.setClub_id(id);
		return this.create(player);
	}

	@Override
	@GET
	@Path("/clubs/{c1},{c2}/matches")
	public LigaMatches findMatchesBetweenClubs(@PathParam("c1") Integer clubId1,@PathParam("c2") Integer clubId2) {
		if(clubId1 == null || clubId2 == null){
			return null;
		}
		if(clubId1.equals(0)||clubId2.equals(0)){
			return this.getMatches();
		}
		System.out.println(clubId1+clubId2);
		Club club1 = bean.get(clubId1, Club.class);
		Club club2 = bean.get(clubId2, Club.class);
		if(club1 == null || club2 == null){
			return null;
		}
		List<LigaMatch> lmatches = bean.findMatchesBetweenClubs(clubId1, clubId2);
		if(lmatches.isEmpty()){
			return null;
		}
		LigaMatches matches = new LigaMatches(lmatches);
		// TODO Auto-generated method stub
		return matches;
	}
}
