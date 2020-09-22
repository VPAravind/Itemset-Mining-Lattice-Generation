import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
/**
 * FunctionalDependencies.java
 * 
 * Version:
 * $1.0$
 */

public class FunctionalDependencies{ 
	/**
	 * Program to find functional dependencies using Naive Approach.
	 * 
	 * @author Aravind Vicinthangal Prathivaathi
	 *
	 */
static List<String> fd = new ArrayList<String>();  
static List<String> f = new ArrayList<String>();  
/**
 * Method which creates multiple combinations of elements from a given set.	 
 * 
 * @throws SQLException
 * @param args command line arguments
 */

	static void combinations(String str[], int n) { 
		for (int l = 1; l <= n; l++) { 
			for (int i = 0; i <= n - l; i++) { 				
				int j = i + l - 1; 
				String st = "";
				for (int k = i; k <= j; k++) { 
					st+=str[k]; 
				} 
				st = st.replaceAll(", $", "");
				fd.add(st);

			} 
		} 
	} 

	/**
	 * Main method : Checks for the command line arguments required  and
	 * determines all functional dependencies, trivial and non-trivial.	 
	 * 
	 * @throws SQLException
	 * @param args command line arguments
	 */

	public static void main(String[] args) throws SQLException { 
		if(args.length !=4) {
    		System.out.println("Error!! Syntax for the command : ");
    		System.out.println("hostName:port databaseName username password");
    		System.exit(1);
    	} 
    	long startTime = System.currentTimeMillis();
        String url = "jdbc:postgresql://"+ args[0]+"/" + args[1];
        System.out.println(url);

        try (Connection con = DriverManager.getConnection(url, args[2], args[3]);
        		 Statement statement = con.createStatement();){
        
        
		String str[] = {"movieId, ", "type, ", "startYear, ", "runtime, ", "avgRating, ", "genreId, ", "genre, ",
			   	   "memberId, ", "birthYear, ", "role, " }; 
		combinations(str, str.length); 
		
		
		
		String fds[] = fd.toArray(new String[0]);
		int dependency_count = 0;
		for(int i = 0; i < fds.length; i++) {
			System.out.println(fds[i]);
			for (int j = 0; j < str.length; j++) {
				String subQuery = "Count(Distinct " + str[j].replaceAll(", $", "")
						+ ") > 1;";

				ResultSet rs = statement.executeQuery("SELECT " +
						" FROM movie_genre_actor_role " + 
						" GROUP BY " + fds[i] + " having " + subQuery ) ;
						if(rs.next() == false) {
							dependency_count ++;
						}
			}
		}
		
//		ResultSet rs = statement.executeQuery("select  FROM movie_genre_actor_role GROUP BY movieId having count(Distinct type) > 1;");
//		if(rs.next()== false) {
//			System.out.println("FD");
//		}
        
		long endTime = System.currentTimeMillis();
        

        System.out.println("The total time taken is: " + (((endTime - startTime)/1000)) + " Seconds");
        
		System.out.println("The total number of functional dependency: " + dependency_count);
	      
				
		
	
	
	} 
	
	} 
}