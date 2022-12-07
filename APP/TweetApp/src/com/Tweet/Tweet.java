package com.Tweet;
import JDBC.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Tweet {

	Scanner sc;
	String newLine = "";
	Connection dbCon = null;
	String fname = "";
	String email = "";
	ValidateData valid = null;

	Tweet() {
		System.out.println("Welcome to Tweet!!" + "\n");
		newLine = System.lineSeparator();
		sc = new Scanner(System.in);
		dbCon = (Connection) DatabaseConnection.getConnection();
		valid = new ValidateData();
	}

	public void print(String str) {
		System.out.println(str);
	}

	public void registration(boolean flag) {
		if (flag) {
			print("Welcome to Registration!!" + newLine);
		}

		print("Enter First Name :");
		String fname = sc.nextLine().trim();
		if (!valid.validateCredential(fname, "fname").equals("Validated")) {
			print(valid.validateCredential(fname, "First Name") + newLine);
			registration(false);
		}
		print("Enter Last Name :");
		String lname = sc.nextLine().trim();
		if (!valid.validateCredential(lname, "lname").equals("Validated")) {
			print(valid.validateCredential(lname, "Last Name") + newLine);
			registration(false);
		}
		print("Enter Email :");
		String email = sc.nextLine().trim().toLowerCase();
		if (!valid.validateCredential(email, "email").equals("Validated")) {
			print(valid.validateCredential(email, "email") + newLine);
			registration(false);
		}
		print("Enter Password :");
		String password = sc.nextLine().trim();
		if (!valid.validateCredential(password, "password").equals("Validated")) {
			print(valid.validateCredential(password, "email") + newLine);
			registration(false);
		} else {
			String query = "INSERT INTO user (`userId`, `Fname`, `Lname`, `email`, `password`, `status`) VALUES (?,?,?,?,?,?);";

			try {

				Statement st = dbCon.createStatement();
				ResultSet rs = st.executeQuery("Select Count(*) AS count from user;");
				int count = 0;
				while (rs.next()) {
					count = rs.getInt("count");
				}

				PreparedStatement ps = dbCon.prepareStatement(query);
				ps.setInt(1, count + 1);
				ps.setString(2, fname);
				ps.setString(3, lname);
				ps.setString(4, email);
				ps.setString(5, password);
				ps.setString(6, "OFF");

				if (ps.executeUpdate() > 0) {
					print("Registration complete!!");
					doTweeting();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public void login() {
		print("Enter Email :");
		String email = sc.nextLine().trim().toLowerCase();
		if (!valid.validateCredential(email, "email").equals("Validated")) {
			print(valid.validateCredential(email, "email"));
			login();
		}
		print("Enter Password :");
		String password = sc.nextLine().trim();
		if (!valid.validateCredential(password, "password").equals("Validated")) {
			print(valid.validateCredential(password, "email"));
			login();
		} else {

			try {
				Statement st = dbCon.createStatement();
				String isPresent = "select count(email) AS users from user where email='" + email + "';";
				ResultSet rs = st.executeQuery(isPresent);

				int count = 0;
				while (rs.next()) {
					count = count + rs.getInt("users");
				}
				if (count == 0) {
					print("Error!! Please Enter Correct Email.");
					doTweeting();
				} else {
					String query = "select * from user where email='" + email + "';";
					rs = st.executeQuery(query);
					while (rs.next()) {
						if (email.equals(rs.getString("email")) && password.equals(rs.getString("password"))) {
							this.email = email;
							this.fname = rs.getString("fname");

							st = dbCon.createStatement();
							query = "UPDATE user SET Status='ON' WHERE email='" + email + "'";
							int res = st.executeUpdate(query);
							if (res > 0) {
								print("Login Success!!");
								print("Welcome " + rs.getString("Fname") + "!!" + newLine
										+ "Please choose below options to continue :");
							}

							loginAction(this.email, this.fname);
						} else {
							print("Error!! Please submit correct Password." + newLine);
							doTweeting();
						}
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void resetPassword() {
		print("Please Enter Email to reset Password:");
		String email = sc.nextLine().trim().toLowerCase();
		if (!valid.validateCredential(email, "email").equals("Validated")) {
			print(valid.validateCredential(email, "email"));
			doTweeting();
		}
		print("Enter Password :");
		String password = sc.nextLine().trim();
		if (!valid.validateCredential(password, "password").equals("Validated")) {
			print(valid.validateCredential(password, "password"));
			doTweeting();
		} else {

			Statement st;
			try {
				st = dbCon.createStatement();
				String query = "UPDATE user SET password='" + password + "' WHERE email='" + email + "'";
				int rs = st.executeUpdate(query);

				if (rs > 0) {
					print("Password Reset!!" + newLine);
					doTweeting();
				} else {
					print("Error! Please enter correct email." + newLine);
					doTweeting();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void loginAction(String email, String fname) {
		// print("Please select correct option to continue :" );
		this.email = email;
		this.fname = fname;
		print("1 : Post Tweet" + newLine + "2 : See Your Post " + newLine + "3 : See Other Users" + newLine
				+ "4 : See All Tweets" + newLine + "5 : Reset Password" + newLine + "6 : Logout" + newLine);

		String input = sc.nextLine();

		switch (input) {
		case "1":
			postTweet(this.email, this.fname);
			break;
		case "2":
			seeOwnTweets(email, fname);
			break;
		case "3":
			seeOtherUser();
			break;
		case "4":
			seeOtherTweets();
			break;
		case "5":
			resetPassword();
			break;
		case "6":
			logout();
			break;
		default:
			print("Enter Choose Correct option!!");
			loginAction(this.email, this.fname);
		}
	}

	private void logout() {
		// TODO Auto-generated method stub
		try {
			Statement st = dbCon.createStatement();
			String query = "UPDATE user SET Status='OFF' WHERE email='" + email + "'";
			int res = st.executeUpdate(query);
			if (res > 0) {
				print("Logout Success!!" + newLine);
				this.email = null;
				this.fname = null;
				doTweeting();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void seeOtherUser() {
		// TODO Auto-generated method stub
		String getUser = "select user,count(email) as tweets from tweets Group by email;";
		Statement st;
		try {
			st = dbCon.createStatement();
			ResultSet rs = st.executeQuery(getUser);
			// String user = "";
			while (rs.next()) {
				print("User name :" + rs.getString("user") + " :: Tweets : " + rs.getString("tweets") + newLine);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print(newLine);
		loginAction(this.email, this.fname);

	}

	private void seeOtherTweets() {
		String getTweet = "select * from tweets order by email;";
		// print(getTweet);
		Statement st;
		try {
			st = dbCon.createStatement();
			ResultSet rs = st.executeQuery(getTweet);

			String user = "";
			while (rs.next()) {
				if (!user.equals(rs.getString("user"))) {
					user = rs.getString("user");
					print("Tweet by User :" + user);
				}
				print("Tweet#");
				print(rs.getString("tweet") + newLine);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print(newLine);
		loginAction(this.email, this.fname);

	}

	private void seeOwnTweets(String email, String fname) {
		print("email : " + email + " ::: Name " + fname + newLine);
		print("Please see your tweets :");

		String getTweet = "select * from tweets where email='" + email + "';";
		Statement st;
		try {
			st = dbCon.createStatement();
			ResultSet rs = st.executeQuery(getTweet);

			print("User :" + fname);
			int i = 1;
			while (rs.next()) {
				print("Tweet#" + i);
				print(rs.getString("tweet"));
				i++;

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		print(newLine);
		loginAction(this.email, this.fname);

	}

	private void postTweet(String email, String fname) {
		// TODO Auto-generated method stub
		print("Please type your Tweet : ");
		String tweet = sc.nextLine();
		String sql = "INSERT INTO tweets(id,user,email,tweet) VALUES(?,?,?,?)";
		PreparedStatement ps;
		try {
			Statement st = dbCon.createStatement();
			ResultSet rs = st.executeQuery("Select Count(*) AS total from tweets;");
			int count = 0;
			while (rs.next()) {
				count = rs.getInt("total");
			}

			ps = dbCon.prepareStatement(sql);
			ps.setInt(1, count + 1);
			ps.setString(2, fname);
			ps.setString(3, email);
			ps.setString(4, tweet);
			int result = ps.executeUpdate();

			if (result > 0) {
				print("Tweeting done!!");
				loginAction(this.email, this.fname);
			} else {
				print("Error! Please try again :");
				loginAction(this.email, this.fname);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void doTweeting() {
		print("Please select option to continue :");
		print("1 : Register" + newLine + "2 : Login " + newLine + "3 : Reset Password" + newLine);

		String input = sc.nextLine();

		switch (input) {
		case "1":
			registration(true);
			break;
		case "2":
			login();
			break;
		case "3":
			resetPassword();
			break;
		default:
			print("Enter Correct option!!");
			doTweeting();
		}
	}

	public static void main(String[] args) {
		Tweet tweet = new Tweet();
		tweet.doTweeting();
	}
}
