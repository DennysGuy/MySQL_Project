/*****************************
Query the Books Database
*****************************/
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.util.*;
import java.lang.String;

public class MyQuery {

    private Connection conn = null;
	 private Statement statement = null;
	 private ResultSet resultSet = null;
    
    public MyQuery(Connection c)throws SQLException
    {
        conn = c;
        // Statements allow to issue SQL queries to the database
        statement = conn.createStatement();
    }
    
    public void findAuthorJuanAdams() throws SQLException
    {
        String query  = "select title from BOOKS natural join BOOKAUTHOR natural join AUTHOR" 
            + " where fname = \'JUAN\' and lname = \'ADAMS\';";

        resultSet = statement.executeQuery(query);
    }
    
    public void printAuthorJuanAdams() throws IOException, SQLException
    {
	    System.out.println("******** Query 0 ********");
        System.out.println("Book_Title");
        while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number which starts at 1
			String name = resultSet.getString(1);
            System.out.println(name);
   		}  
        System.out.println();      
    }

    public void findCustomerOrder() throws SQLException
    {
        String query = "SELECT CONCAT(FirstName,\" \" ,LastName) as Full_Name, COUNT(DISTINCT Order_num) as Num_of_Orders, SUM(Quantity) as Num_of_Books " +
                "FROM CUSTOMERS NATURAL JOIN ORDERS NATURAL JOIN ORDERITEMS " +
                "GROUP BY Customer_num " +
                "ORDER BY SUM(Quantity) DESC;";

        resultSet = statement.executeQuery(query);
    }
    
    public void printCustomerOrder() throws IOException, SQLException
    {
		   System.out.println("******** Query 1 ********");
           System.out.println("Full_Name        Num_of_Orders       Num_of_Books");


           while (resultSet.next()) {
               String name = resultSet.getString(1);
               String orderCount = resultSet.getString(2);
               String bookCount = resultSet.getString(3);
               System.out.println(name + "\t\t  "+ orderCount + "\t\t\t\t\t " +bookCount);

           }


           System.out.println();
    }

    public void findBusyAuthor() throws SQLException
    {

    }

    public void printBusyAuthor() throws IOException, SQLException
    {
	   	System.out.println("******** Query 2 ********");
    }

    public void findBookProfit() throws SQLException
    {
 
    }

    public void printBookProfit() throws IOException, SQLException
    {
		   System.out.println("******** Query 3 ********");
    }

    public void findHighestProfitPerCategory() throws SQLException
    {

    }

    public void printHighestProfitPerCategory() throws IOException, SQLException
    {
		   System.out.println("******** Query 4 ********");
    }

    public void findMinMaxOrderDate() throws SQLException
    {

    }

    public void printMinMaxOrderDate() throws IOException, SQLException
    {
		   System.out.println("******** Query 5 ********");
    }
	
    public void updateDiscount() throws SQLException
    {
 
    }

    public void printUpdatedDiscount() throws IOException, SQLException
    {
        System.out.println("******** Query 6 ********");
    }

    public void findHighestProfit() throws SQLException
	{
		  System.out.println("******** Query 7 ********");	
	}

}
