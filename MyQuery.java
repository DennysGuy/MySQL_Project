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
     private Scanner input = null;
    
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
           String[] titleArray = {"Full Name", "Number of Orders", "Number of Books"};
		   System.out.println("******** Query 1 ********");
           System.out.print(tableBuilder(titleArray,resultSet));
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
        String[] titleArray = {"Author Name", "Number of Books"};
	   	System.out.println("******** Query 2 ********");
        System.out.println(tableBuilder(titleArray,resultSet));
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
           String[] titleArray = {"ISBN", "Title", "Category", "Profit"};
		   System.out.println("******** Query 3 ********");
           System.out.println(tableBuilder(titleArray,resultSet));
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

            String[] titleArray = {"ISBN", "Title", "Name", "Earliest Order Date",
                    "Latest Order Date", "Total Quantity"};
		    System.out.println("******** Query 5 ********");

            System.out.println(tableBuilder(titleArray, resultSet));
    }
	
    public void updateDiscount() throws SQLException
    {
        resultSet.beforeFirst();
        String query = "SELECT * FROM BookCopy";
        resultSet = statement.executeQuery(query);
    }

    public void printUpdatedDiscount() throws IOException, SQLException
    {
        String[] titleArray = {"ISBN", "TITLE", "PubDate", "PubID", "Cost", "Retail", "Discount", "Category"};
        System.out.println("******** Query 6 ********");
        System.out.println(tableBuilder(titleArray,resultSet));
    }

    public void findHighestProfit() throws SQLException
	{
		  System.out.println("******** Query 7 ********");
          input = new Scanner(System.in);
          String category;

          System.out.println("Please enter the category name:");
          category = input.nextLine().toUpperCase().trim();

          String query = "SELECT ISBN, Title, Category, SUM((retail-cost)*quantity) as Profit\n" +
                  "FROM BOOKS NATURAL JOIN ORDERITEMS  GROUP BY ISBN, Category\n" +
                  "HAVING Category = '"+ category +"' AND Profit >= ALL(SELECT SUM((retail-cost)*quantity)\n" +
                  "FROM BOOKS NATURAL JOIN ORDERITEMS GROUP BY ISBN, Category\n" +
                  "HAVING Category = '"+ category +"');";

          resultSet = statement.executeQuery(query);
          while (resultSet.next()) {
              System.out.println("Book " + resultSet.getString(2) + " (ISBN: " + resultSet.getString(1)
              + ") has the highest profit $" + resultSet.getString(4) + " in " + resultSet.getString(3) + " category");
          }
	}

    public int[] buildPadArray(ResultSet resultSet, String[] titleArray) throws SQLException {
        int[] newPadArray = new int[titleArray.length];
        int extraSpacing = 2;
        //set the new pad array index to the size of the title
        for (int i = 0; i < titleArray.length;i++) {
            newPadArray[i] = titleArray[i].length() + extraSpacing;
        }

        //check each entry in column against the title length and every consecutive entry to see if longer than previous entry
        for (int i = 1, j = 0; i <= newPadArray.length; i++, j++) {
            while (resultSet.next()) {
                if (resultSet.getString(i) != null && resultSet.getString(i).length() > newPadArray[j] )
                {
                    newPadArray[j] = resultSet.getString(i).length() + extraSpacing;
                }
            }
            resultSet.beforeFirst();
        }
        return newPadArray;
    }

    public String elementBuilder(String element, int padding)
    {
        return String.format("%1$-"+padding+"s",element);
    }

    public String titleBuilder(String[] titleArray, int[] paddingArray)
    {
        String newTitle = "";
        for(int i = 0; i <= paddingArray.length; i++) {
            if (i == paddingArray.length)
                newTitle += "\n";
            else
                newTitle += elementBuilder(titleArray[i],paddingArray[i]);

        }
        return newTitle;
    }

    public String tableBuilder(String[] titleArray, ResultSet resultSet) throws SQLException
    {
        String newTable = "";
        int[] padArray = buildPadArray(resultSet, titleArray);
        resultSet.beforeFirst();

        newTable += titleBuilder(titleArray, padArray);
        while(resultSet.next()) {
            for (int i = 1, j = 0; i <= titleArray.length+1; i++,j++) {
                if (j == titleArray.length) {
                    j = 0;
                    newTable += "\n";

                } else {
                    newTable += elementBuilder(resultSet.getString(i), padArray[j]);
                }
            }
        }
        return newTable;
    }
}
