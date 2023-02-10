

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

class DbConn{

  Connection connection = null;

  Statement statement = null;

  String table_name = "users";
  
  String current_user = null;
  int current_userid = 0;



  DbConn(){
      try{
          Class.forName("com.mysql.cj.jdbc.Driver");

          
          try{
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/clg","root","admin");
          }
          catch(Exception e){
            Scanner sc =new Scanner(System.in);
            System.out.println("Enter database name:");
            String sb = sc.nextLine();
            System.out.println("Enter db user name:");
            String us = sc.nextLine();
            System.out.println("Enter pass: ");
            String pa = sc.nextLine();

            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + sb,us,pa);
          }

          this.statement = connection.createStatement();

          


          System.out.println("Connected to sql");

          if (!tableExists("users")){
            statement.executeUpdate("create table users(name varchar(50) primary key,password varchar(50))");
            
          }

          if (!tableExists("data")){
            statement.executeUpdate("create table data(Sender varchar(50),Receiver Varchar(50),Message varchar(250))");
            
          }

      }catch (Exception e){
          System.out.println("Connection Failed");
          e.printStackTrace();
      }
  }

  boolean tableExists(String tableName) throws SQLException {
    DatabaseMetaData meta = connection.getMetaData();
    ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"TABLE"});

    return resultSet.next();
}

  boolean add_user(int id,String name,String pass){
      try {
          PreparedStatement statement1 = this.connection.prepareStatement("insert into users values(?,?)");

          //statement1.setInt(1,id);
          statement1.setString(1,name);
          statement1.setString(2,pass);
          
          statement1.executeUpdate();
          
          connection.commit();
          
          return true;
      }
      catch (Exception e){
          System.out.println(e);
           
          return false;
      }
  }
  
  boolean check_login(String name,String pass) {
	  try {
		  PreparedStatement statement2 = this.connection.prepareStatement("select * from users where name = ? and password = ?");
		  
		  statement2.setString(1, name);
		  statement2.setString(2,pass);
		  
		  ResultSet rs = statement2.executeQuery();
		  
		  if (rs.next()) {
            
            current_user = rs.getString("name");
            //current_userid = name;

			return true;

		  }else {
			return false;
		  }
		  
	  }
	  catch(Exception e) {
		  return false;
	  }
  }
  
  

  void ShowTable(){
      try {
          ResultSet resultSet = statement.executeQuery("select * from "+ table_name);

          while (resultSet.next()) {
              System.out.println( resultSet.getString("id")+" " +resultSet.getString("name") + " " + resultSet.getString("password"));
          }
      }
      catch (Exception e){
          System.out.println(e);
      }
  }

  void getUser(){
    System.out.println(current_user);
  }



  public ArrayList<ArrayList<String>> getMessages(){

    ArrayList<ArrayList<String>> list = new ArrayList<>();

    try{

        //current_user = "Jam";
        PreparedStatement stmt = this.connection.prepareStatement("select * from data where Sender = ? or Receiver = ?");

        stmt.setString(1, current_user);
        stmt.setString(2, current_user);
        
        ResultSet rs = stmt.executeQuery();

        
        
        while(rs.next()){
            ArrayList<String> ls = new ArrayList<>();

            ls.add(rs.getString(1));
            ls.add(rs.getString(2));
            ls.add(rs.getString(3));

            list.add(ls);
        }

    }catch(Exception e){
        System.out.print(e);
    }

    return list;
  }

  public boolean sendMsg(String to,String mesString){

    try{

        PreparedStatement stmt = this.connection.prepareStatement("insert into data values(?,?,?)");

        stmt.setString(1, current_user);
        stmt.setString(2, to);
        stmt.setString(3, mesString);

        stmt.executeUpdate();
        
        return true;
    }catch(Exception e){

        System.out.println(e);
        return false;
    }
  }

  public void logout(){
    current_user = "";
    current_userid = 0;
  }

}