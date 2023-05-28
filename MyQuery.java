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
        String query = "SELECT CONCAT(Fname, \" \", Lname) as Author_Name, COUNT(AuthorID) as Num_of_Books FROM " +
                "AUTHOR NATURAL JOIN BOOKAUTHOR GROUP BY AuthorID " +
                "HAVING COUNT(AuthorID) >= ALL (SELECT COUNT(AuthorID) FROM BOOKAUTHOR GROUP BY AuthorID);";
        resultSet = statement.executeQuery(query);
    }

    public void printBusyAuthor() throws IOException, SQLException
    {
	   	System.out.println("******** Query 2 ********");
        System.out.println("Author_Name\t\tNum_of_Books");

        while (resultSet.next()) {
            String name = resultSet.getString(1);
            String bookCount = resultSet.getString(2);
            System.out.println(name +"\t\t\t" + bookCount);

        }
        System.out.println();
    }

    public void findBookProfit() throws SQLException
    {
        String query = "SELECT ISBN, Title, Category, SUM((retail-cost)*quantity) as Profit\n" +
                "FROM BOOKS JOIN ORDERITEMS USING(ISBN) GROUP BY ISBN \n" +
                "ORDER BY SUM((retail-cost)*quantity);";

        resultSet = statement.executeQuery(query);
    }

    public void printBookProfit() throws IOException, SQLException
    {
		   System.out.println("******** Query 3 ********");
           System.out.println("ISBN\t\tTitle\t\t\t\t\t\t\tCategory\t\tProfit");
           while(resultSet.next()) {
               String isbn = resultSet.getString(1);
               String bookCount = resultSet.getString(2);
               String catagory = resultSet.getString(3);
               String profit   = resultSet.getString(4);

               System.out.println(isbn + "\t" + bookCount + "\t\t\t" + catagory + "\t" + profit);
           }
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
        String query = "SELECT ISBN, Title, Name, COALESCE(MIN(OrderDate), 'NA') as Earliest_Order_Date, COALESCE(MAX(OrderDate), 'NA') \n" +
                "as Latest_Order_Date, COALESCE(SUM(Quantity),0) as Total_Quantity\n" +
                "FROM ORDERS JOIN ORDERITEMS USING(Order_num) RIGHT JOIN BOOKS USING(ISBN)\n" +
                "JOIN PUBLISHER USING(PubID) GROUP BY ISBN ORDER BY Total_Quantity desc;";
        resultSet = statement.executeQuery(query);
    }

    public void printMinMaxOrderDate() throws IOException, SQLException
    {
		   System.out.println("******** Query 5 ********");

           System.out.println("ISBN\t\tTitle\t\t\t\t\t\t\tName\t\t\t\t\tEarliest Order Date\t\tLatest Order Date\tTotal Quantity");
           while(resultSet.next()) {
            String isbn = resultSet.getString(1);
            String title = resultSet.getString(2);
            String name = resultSet.getString(3);
            String eDate   = resultSet.getString(4);
            String lDate = resultSet.getString(5);
            String tQuantity = resultSet.getString(6);

            //System.out.println(isbn + "\t" + title + "\t" + name + "\t\t" + eDate + "\t\t\t\t" + lDate + "\t\t\t\t\t" + tQuantity);
            System.out.format("%1$-12s",isbn);System.out.format("%1$-32s",title);
            System.out.format("%1$-26s",name);System.out.format("%1$-22s",eDate);
            System.out.format("%1$-21s",lDate);System.out.format("%1$-2s",tQuantity);
            System.out.println();

            }




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

    //helper method
    public StringBuilder columnBuilder(String title, ResultSet resultSet, int padNumber, int index) throws IOException, SQLException{
        StringBuilder column = new StringBuilder();

        column.append(title + "\n");

        while (resultSet.next()) {
            String element = resultSet.getString(index);
            String.format("%1$-"+padNumber+"s",element);
            column.append(element +"\n");
            //System.out.format("%1$-"+padNumber+"s",element);
            //System.out.println();

        }
        return column;
    }
}
