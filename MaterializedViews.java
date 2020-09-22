import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.postgresql.core.JavaVersion;

/**
 * MaterializedViews.java
 * 
 * Version: $1.0$
 */

public class MaterializedViews{
	/**
	 * Program to create the given sources as materialized view and non-materialized
	 * view.
	 * 
	 * @author Aravind Vicinthangal Prathivaathi
	 *
	 */
	public static void main(String args[]) {
		/**
		 * Main method : Checks for the command line arguments required and generates the popular_movie_actors table.
		 * 
		 * @throws SQLException
		 * @param args command line arguments
		 */
		if (args.length != 4) {
			System.out.println("Error!! Syntax for the command : ");
			System.out.println("hostName:port databaseName username password");
			System.exit(1);
		}

		long startTime = System.currentTimeMillis();
		// generates the url for connecting to the database
		String url = "jdbc:postgresql://" + args[0] + "/" + args[1];
		System.out.println(url);
		// connecting to the database
		try (Connection con = DriverManager.getConnection(url, args[2], args[3]);
				Statement st = con.createStatement();) {

			// runs the query to generate popular_movie_actors table
			st.execute(" Create table popular_movie_actors as select ma.actor, ma.movie "
					+ " from movie_actor as ma JOIN movie as m on ma.movie = m.id "
					+ " where type like 'movie' and avgrating > 5; ");

			long endTime = System.currentTimeMillis();

			System.out.println("The total time taken is: " + (((endTime - startTime) / 1000)) + " Seconds");

		} catch (SQLException ex) {

			Logger lgr = Logger.getLogger(JavaVersion.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

}
