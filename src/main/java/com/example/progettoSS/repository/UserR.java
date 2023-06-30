package com.example.progettoSS.repository;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;
import com.example.progettoSS.entity.User;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.mindrot.jbcrypt.BCrypt;


/*
@Repository
public class UserR{
   
    private final static String DB_URL = "jdbc:mysql://localhost:3307/plantcommerce";
	private final static String DB_USERNAME = "root";



    public User findUser(String email, String password){
        ResultSet resultSet = null;
        Statement statement = null;
         Connection connection = null;
        try{
         connection = DriverManager.getConnection(DB_URL, DB_USERNAME,"");
         statement = connection.createStatement();
        //final String query = "SELECT * FROM `userregister` WHERE `username` LIKE '" + username+"'"; //qualcosa' or '1=1
        final String query = "SELECT * FROM `user` WHERE `email` LIKE '"+email+"' AND `password` LIKE '"+password+"'"; //(qualcosa' or '1=1) se lo metto nella password mi fa fare login, uguale se lo metto nell'username e non conosco la password --> mario1' or '1'='1, anche se metto una password sbagliata entra, mettendo questo si bypassa il controllo della password vedi screenshot di chatgpt
        System.out.println(query);
        resultSet = statement.executeQuery(query);
  
         User user = null;
   
      if (resultSet.next()) {
       
          user = new User(resultSet.getString("id"), resultSet.getString("name"),resultSet.getString("surname"), resultSet.getString("email"), resultSet.getString("phone"), resultSet.getString("password"), resultSet.getString("cpassword"), resultSet.getString("role"));
            System.out.println(user);
      }
            return user;
    }
     catch (final SQLException e) {
      e.printStackTrace();
    } 
    return null;
    }


    public void registerUser(User user){


        Statement statement = null;
         Connection connection = null;
        try{
         connection = DriverManager.getConnection(DB_URL, DB_USERNAME,"");
         statement = connection.createStatement();
         String id = UUID.randomUUID().toString();
        final String query = "INSERT INTO `user` (`id`,`cpassword`, `email`, `name`, `password`, `phone`, `surname`, `role`) VALUES ('"+id+"','"+user.getCpassword()+"','"+user.getEmail()+"','"+user.getName()+"','"+user.getPassword()+"','"+user.getPhone()+"','"+user.getSurname()+"','"+user.getRole()+"')";
        System.out.println(query);
        statement.executeUpdate(query);
        }
        catch (final SQLException e) {
        e.printStackTrace();
        } 


    }

    public User findUserById(String id){
        ResultSet resultSet = null;
        Statement statement = null;
         Connection connection = null;
        try{
         connection = DriverManager.getConnection(DB_URL, DB_USERNAME,"");
         statement = connection.createStatement();

        final String query = "SELECT * FROM `user` WHERE `id` LIKE '"+id+"'";
        System.out.println(query);
        resultSet = statement.executeQuery(query);
  
         User user = null;
   
      if (resultSet.next()) {
       
          user = new User(resultSet.getString("id"), resultSet.getString("name"),resultSet.getString("surname"), resultSet.getString("email"), resultSet.getString("phone"), resultSet.getString("password"), resultSet.getString("cpassword"), resultSet.getString("role"));
            System.out.println(user);
      }
            return user;
    }
     catch (final SQLException e) {
      e.printStackTrace();
    } 
    return null;
    
    }
} */

/**************************STATEMENT NON PREPARED********************************/

@Repository
public class UserR {
   
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User findUser(String email, String password) {
        final String query = "SELECT * FROM `user` WHERE `email` LIKE '" + email + "' AND `password` LIKE '" + password + "'";
        System.out.println(query);

        return jdbcTemplate.query(query, resultSet -> {
                System.out.println(resultSet);
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"),
                        resultSet.getString("cpassword"),
                        resultSet.getString("role")
                );
            }
            return null;
        });
    }

    public void registerUser(User user) {
        String id = UUID.randomUUID().toString();
        final String query = "INSERT INTO `user` (`id`,`cpassword`, `email`, `name`, `password`, `phone`, `surname`, `role`) VALUES ('" + id + "','" + user.getCpassword() + "','" + user.getEmail() + "','" + user.getName() + "','" + user.getPassword() + "','" + user.getPhone() + "','" + user.getSurname() + "','" + user.getRole() + "')";
        System.out.println(query);

        jdbcTemplate.update(query);
    }

    public User findUserById(String id) {
        final String query = "SELECT * FROM `user` WHERE `id` LIKE '" + id + "'";
        System.out.println(query);

        return jdbcTemplate.query(query, resultSet -> {
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"),
                        resultSet.getString("cpassword"),
                        resultSet.getString("role")
                );
            }
            return null;
        });
    }
}


/**************************PREPARED STATEMENT********************************/
/*
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Repository
public class UserR {
    private final static String DB_URL = "jdbc:mysql://localhost:3307/plantcommerce";
    private final static String DB_USERNAME = "root";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User findUser(String email, String password) {
        final String query = "SELECT * FROM `user` WHERE `email` = ?";
        
        return jdbcTemplate.query(query, new Object[]{email}, resultSet -> {
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                System.out.println(hashedPassword);
                if (verifyPassword(password, hashedPassword)) {
                    return new User(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            resultSet.getString("surname"),
                            resultSet.getString("email"),
                            resultSet.getString("phone"),
                            hashedPassword,
                            resultSet.getString("cpassword"),
                            resultSet.getString("role")
                    );
                }
            }
            return null;
        });
    }

    public void registerUser(User user) {
        String id = UUID.randomUUID().toString();
        String hashedPassword = hashPassword(user.getPassword());
        String hashedCPassword = hashPassword(user.getCpassword());

        final String query = "INSERT INTO `user` (`id`, `cpassword`, `email`, `name`, `password`, `phone`, `surname`, `role`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(query, id, hashedCPassword, user.getEmail(), user.getName(), hashedPassword, user.getPhone(), user.getSurname(), user.getRole());
    }

    public User findUserById(String id) {
        final String query = "SELECT * FROM `user` WHERE `id` = ?";

        return jdbcTemplate.query(query, new Object[]{id}, resultSet -> {
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"),
                        resultSet.getString("cpassword"),
                        resultSet.getString("role")
                );
            }
            return null;
        });
    }

    private boolean verifyPassword(String password, String hashedPassword) {
        String hashedInputPassword = hashPassword(password);
        System.out.println(hashedInputPassword);
        System.out.println(hashedPassword);
        return hashedInputPassword.equals(hashedPassword);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Gestisci l'errore di algoritmo di hashing non disponibile
            e.printStackTrace();
        }

        return null;
    }
}
*/