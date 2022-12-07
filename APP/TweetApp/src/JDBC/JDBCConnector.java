package JDBC;
import java.sql.*;
import java.util.Scanner;

public class JDBCConnector {
	
	static final String dburl = "jdbc:mysql://localhost:3306/tweet";
	static final String user="root";
	static final String password="pass@word1";
	static final String queryRetrieve ="select * from person;";
	static final String queryInsert = "INSERT INTO person (`Id`, `Fname`, `Lname`, `Address`) VALUES(?,?,?,?);";
	static final String queryUpdate = "UPDATE person SET Fname = ?, Lname = ?,Address= ? WHERE (Id = ?);";

	public void demoRetrieve()
	{
		try(Connection con = DriverManager.getConnection(dburl, user, password);
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(queryRetrieve);
				)
			{
				while(rs.next())
				{
					System.out.print("ID = "+rs.getInt("id"));
					System.out.print(" : Fname ="+rs.getString("Fname"));
					System.out.print(" : Lname ="+rs.getString("Lname"));
					System.out.println(" : Address ="+rs.getString("Address"));
				}
				
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		
	}
	public void demoInsert()
	{
		try(Connection con = DriverManager.getConnection(dburl, user, password);
				PreparedStatement ps = con.prepareStatement(queryInsert);
				)
			{
			Scanner sc = new Scanner(System.in);
			
			System.out.println("Enter Person id");
			int pid = sc.nextInt();
			System.out.println("Enter First Name");
			String fname = sc.next();
			System.out.println("Enter Last Name");
			String lname = sc.next();
			System.out.println("Enter Address Name");
			String address = sc.next();
			
			ps.setInt(1, pid);
			ps.setString(2, fname);
			ps.setString(3, lname);
			ps.setString(4, address);
			System.out.println(ps.executeUpdate());
				
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		
	}
	public void demoUpdate()
	{
		try(Connection con = DriverManager.getConnection(dburl, user, password);
				PreparedStatement ps = con.prepareStatement(queryUpdate);
				)
			{
			Scanner sc = new Scanner(System.in);
			
			System.out.println("Enter Person id");
			int pid = sc.nextInt();
			System.out.println("Enter First Name");
			String fname = sc.next();
			System.out.println("Enter Last Name");
			String lname = sc.next();
			System.out.println("Enter Address Name");
			String address = sc.next();

			
			ps.setInt(1, pid);
			ps.setString(2, fname);
			ps.setString(3, lname);
			ps.setString(4, address);
			
			System.out.println(ps.executeUpdate());
				
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		
	}
	public static void main(String args[]) {
		JDBCConnector jdbc = new JDBCConnector();
		//jdbc.demoRetrieve();
		//jdbc.demoInsert();
		jdbc.demoUpdate();
	}
}