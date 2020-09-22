import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.postgresql.core.JavaVersion;

/**
 * ItemSetLatticeGeneration.java
 * 
 * Version: $1.0$
 */

public class ItemSetLatticeGeneration {
	/**
	 * Program to generate all levels of the lattice and store them as tables in the
	 * database.
	 * 
	 * @author Aravind Vicinthangal Prathivaathi
	 *
	 */
	@SuppressWarnings("resource")
	public static void main(String args[]) {
		/**
		 * Main method : Checks for the command line arguments required and combines are
		 * multiple statements to form a query for each level of the lattice to generate
		 * those respective levels.
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

		// forming the connection url
		String url = "jdbc:postgresql://" + args[0] + "/" + args[1];
		System.out.println(url);

		// try with resources to connect to the database
		try (Connection con = DriverManager.getConnection(url, args[2], args[3])) {

			PreparedStatement st;
			int count = 1;
			// The create table statement used in the queries
			String table = "";
			table = createTable(count, table);

			// The select statement used in the queries
			String selectStatement = "";
			selectStatement = createSelection(count, selectStatement);

			// The from statement used in the queries
			String from = "";
			from = createFrom(count, from);

			// The where clause used in the queries
			String where = "";
			where = createWhere(count, where);

			// Part of the query which check if the actors are present in the previous level
			// of the lattice
			String previousLevel = "";
			previousLevel = checkInPreviousLevel(count, previousLevel);

			// Group by statement of the query
			String groupBy = "";
			groupBy = createGroupBy(count, groupBy);

			// The having clause of the query, takes the minimum support as 5
			String groupCondition = "";
			groupCondition = createGroupingCondition(groupCondition, 5);

			// The whole query made from all the statements mentioned above
			String query = table + selectStatement + from + where + previousLevel + groupBy + groupCondition;

			System.out.println("Query for L" + count + ":\n" + query);

			// run the query
			st = con.prepareStatement(query);
			int rs = st.executeUpdate();

			query = "Select * from l" + count;

			PreparedStatement st1 = con.prepareStatement(query);

			// Get the result set of the lattice level 1
			ResultSet result = st1.executeQuery();

			count++;

			// iterate through the results of each lattice that has been generated
			while (result.next()) {

				// Update the create table statement used in the queries

				table = createTable(count, table);

				// update the select statement for the current level
				selectStatement = createSelection(count, selectStatement);

				// update the from statement for the current level

				from = createFrom(count, from);

				// update the where statement for the current level

				where = createWhere(count, where);

				// update the query for the current level to check if the actors are present in
				// the previous level
				previousLevel = checkInPreviousLevel(count, previousLevel);

				// update the group by statement for the current level

				groupBy = createGroupBy(count, groupBy);

				// the having clause remains the same
				groupCondition = createGroupingCondition(groupCondition, 5);

				query = table + selectStatement + from + where + previousLevel + groupBy + groupCondition;

				System.out.println("L" + count + ":\n" + query);

				// run the updated query for the current lattice level
				st = con.prepareStatement(query);
				rs = st.executeUpdate();

				query = "Select * from l" + count;

				// Get the result set for the generated lattice level
				st1 = con.prepareStatement(query);
				result = st1.executeQuery();

				count++;

			}

			long endTime = System.currentTimeMillis();

			System.out.println(
					"Total time taken to generate all levels: " + (((endTime - startTime) / 1000)) + " Seconds");

			System.out.println("Total number of levels:" + (count - 1));

		} catch (SQLException ex) {

			Logger lgr = Logger.getLogger(JavaVersion.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	static String createTable(int count, String substatement) {
		/**
		 * Method which creates the table statement of the main query
		 * 
		 * 
		 * @param 		 count	 Lattice level 
		 * 		  substatement	 the create table statement used for the previous level
		 */

		substatement = "CREATE TABLE L" + count + " AS ";
		return substatement;
	}

	static String createSelection(int count, String substatement) {
		/**
		 * Method which creates the SELECT statement of the main query for the current
		 * lattice level
		 * 
		 * 
		 * @param 		 count	 Lattice level 
		 * 		  substatement	 The SELECT statement of the previous level
		 */

		if (count == 1) {
			substatement += " SELECT pm" + count + ".actor as actor" + count + ", COUNT(pm" + count
					+ ".movie) AS count ";
		} else {
			substatement = substatement + ", pm" + count + ".actor as actor" + count + " ";
		}

		return substatement;
	}

	static String createFrom(int count, String substatement) {
		/**
		 * Method which creates the FROM statement of the main query for the current
		 * lattice level
		 * 
		 * 
		 * @param 		 count	 Lattice level 
		 * 		  substatement	 The FROM statement of the previous level
		 */

		if (count == 1) {
			substatement += " From popular_movie_actors as pm" + count + " ";

		} else {
			substatement = substatement + ", popular_movie_actors as pm" + count + " ";

		}

		return substatement;
	}

	static String createWhere(int count, String substatement) {
		/**
		 * Method which creates the WHERE clause of the main query for the current
		 * lattice level
		 * 
		 * 
		 * @param 		count 	Lattice level 
		 * 		  substatement 	The WHERE statement of the previous level
		 */

		if (count == 1) {
			substatement += " WHERE 1 = 1 ";
		}

		else {
			substatement = substatement + " AND pm" + (count - 1) + ".actor < " + "pm" + count + ".actor " + " AND pm"
					+ (count - 1) + ".movie = " + "pm" + count + ".movie ";
		}

		return substatement;

	}

	static String checkInPreviousLevel(int count, String substatement) {
		/**
		 * Method which creates the subquery to check whether the actors in the current
		 * level itemset are present in the previous level
		 * 
		 * @param 		 count: 	Lattice level 
		 * 		  substatement: 	the query for the current level that
		 *              			checks if the actors to added in the itemset are present in the
		 *              			previous level
		 */

		if (count > 1) {
			int i = count;
			String values = " and (";
			String select = " in (Select ";
			while (i > 1) {
				if (i == count) {
					values += " pm" + (i - 1) + ".actor ";

					select += " actor" + (i - 1);
				} else {
					values += ", pm" + (i - 1) + ".actor ";

					select += ", actor" + (i - 1);
				}

				i--;
			}
			values += " ) ";
			select += " from l" + (count - 1) + " ) ";
			substatement = values + select;
		}

		return substatement;
	}

	static String createGroupBy(int count, String substatement) {
		/**
		 * Method which creates the GROUP BY statement of the main query
		 * 
		 * 
		 * @param 		 count	 Lattice level 
		 * 		  substatement	 The GROUP BY statement of the previous level for the current lattice level
		 */

		if (count == 1) {
			substatement += " GROUP BY pm" + count + ".actor ";
		}

		else {
			substatement = substatement + " , pm" + count + ".actor ";
		}

		return substatement;
	}

	static String createGroupingCondition(String substatement, int support) {
		/**
		 * Method which creates the HAVING clause of the main query for the current
		 * lattice level
		 * 
		 * 
		 * @param 		 count 	Lattice level
		 * 		  substatement 	The GROUP BY statement of the previous level
		 */

		substatement = "HAVING count(pm1.movie) >= " + support;

		return substatement;
	}

}
