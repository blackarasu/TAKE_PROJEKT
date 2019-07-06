/**
 * 
 */
package pl.take.liga;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;



/**
 * @author Bartek
 * @version 1.0
 */
@ApplicationPath("rest")
public class StartApplication extends Application {
	/**
	 * Send which options server allows web application to use
	 * @return Response for web application - you can use my API
	 */
	@OPTIONS
	@Path("{path: .*}")
	public Response options() 
	{ 
		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Accept-Charset","utf-8")
				.header("Accept-Language", "pl en").build();
	}
}
