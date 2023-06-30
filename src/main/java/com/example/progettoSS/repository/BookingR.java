package com.example.progettoSS.repository;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;
import com.example.progettoSS.entity.Booking;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import java.util.UUID;
import java.io.IOException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/*
@Repository
public class BookingR{


    private final static String DB_URL = "jdbc:mysql://localhost:3307/plantcommerce";
	private final static String DB_USERNAME = "root";

     public void addBooking(String idUser, String namePlant, String quantity){


        Statement statement = null;
        Connection connection = null;
        String id = UUID.randomUUID().toString();

        try{
         connection = DriverManager.getConnection(DB_URL, DB_USERNAME,"");
         statement = connection.createStatement();
    
        final String query = "INSERT INTO `booking` (`id`, `namePlant`, `idUser`, `quantity`) VALUES ('"+id+"','"+namePlant+"','"+idUser+"','"+quantity+"')";
        System.out.println(query);
        statement.executeUpdate(query);
        }
        catch (final SQLException e) {
        e.printStackTrace();
        } 


    }

    public List<Booking> getAllBooking(){

        List<Booking> list =new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;
        String result = null;
        try{
         connection = DriverManager.getConnection(DB_URL, DB_USERNAME,"");
         statement = connection.createStatement();
 
        final String query = "SELECT * FROM `booking`";
        System.out.println(query);
        resultSet = statement.executeQuery(query);
  
       
            while(resultSet.next()){
           
            Booking b = new Booking(resultSet.getString("id"), resultSet.getString("namePlant"), resultSet.getString("idUser"), resultSet.getString("quantity"));
            list.add(b);
   
            }
            
            }
            catch (final SQLException e) {
            e.printStackTrace();
            
            } 
            return list;
    }


    public List<Booking> getUserBooking(String id){

        List<Booking> list =new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;
        String result = null;
        try{
         connection = DriverManager.getConnection(DB_URL, DB_USERNAME,"");
         statement = connection.createStatement();
 
        final String query = "SELECT * FROM `booking` WHERE `idUser` LIKE '"+id+"'";
        System.out.println(query);
        resultSet = statement.executeQuery(query);
  
       
            while(resultSet.next()){
           
            Booking b = new Booking(resultSet.getString("id"), resultSet.getString("namePlant"), resultSet.getString("idUser"), resultSet.getString("quantity"));
            list.add(b);
   
            }
            
            }
            catch (final SQLException e) {
            e.printStackTrace();
            
            } 
            return list;
    }

     public int deleteBooking(String id){

            int rowsAffected = 0;
            Statement statement = null;
            Connection connection = null;
             try{
                connection = DriverManager.getConnection(DB_URL, DB_USERNAME,"");
                statement = connection.createStatement();
        
                final String query = "DELETE FROM `booking` WHERE `id` LIKE '"+id+"'";
                System.out.println(query);
                rowsAffected = statement.executeUpdate(query);
        
            }
            catch (final SQLException e) {
            e.printStackTrace();
            
            } 
        
            return rowsAffected;
        }

}*/

/**************************STATEMENT NON PREPARED********************************/
@Repository
public class BookingR {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addBooking(String idUser, String namePlant, String quantity) {
        String id = UUID.randomUUID().toString();
        final String query = "INSERT INTO `booking` (`id`, `namePlant`, `idUser`, `quantity`) VALUES ('" + id + "', '" + namePlant + "', '" + idUser + "', '" + quantity + "')";
        jdbcTemplate.execute(query);
    }

    public List<Booking> getAllBookings() {
        final String query = "SELECT * FROM `booking`";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                new Booking(resultSet.getString("id"), resultSet.getString("namePlant"),
                        resultSet.getString("idUser"), resultSet.getString("quantity"))
        );
    }

    public List<Booking> getUserBookings(String id) {
        final String query = "SELECT * FROM `booking` WHERE `idUser` LIKE '" + id + "'";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                new Booking(resultSet.getString("id"), resultSet.getString("namePlant"),
                        resultSet.getString("idUser"), resultSet.getString("quantity"))
        );
    }

    public int deleteBooking(String id) {
        final String query = "DELETE FROM `booking` WHERE `id` LIKE '" + id + "'";
        return jdbcTemplate.update(query);
    }

    public int updateBookingQuantity(String id, String quantity) {
        final String query = "UPDATE `booking` SET `quantity` = '" + quantity + "' WHERE `id` LIKE '" + id + "'";
        return jdbcTemplate.update(query);
    }

    public Booking getBookingById(String id) {
        final String query = "SELECT * FROM `booking` WHERE `id` LIKE '" + id + "'";
        List<Booking> bookings = jdbcTemplate.query(query, (resultSet, rowNum) ->
                new Booking(resultSet.getString("id"), resultSet.getString("namePlant"),
                        resultSet.getString("idUser"), resultSet.getString("quantity"))
        );
        return bookings.isEmpty() ? null : bookings.get(0);
    }
}


/**************************STATEMENT  PREPARED********************************/
/*
@Repository
public class BookingR {

      @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addBooking(String idUser, String namePlant, String quantity) {
        String id = UUID.randomUUID().toString();
        final String query = "INSERT INTO `booking` (`id`, `namePlant`, `idUser`, `quantity`) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, id, namePlant, idUser, quantity);
    }

    public List<Booking> getAllBookings() {
        final String query = "SELECT * FROM `booking`";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                new Booking(resultSet.getString("id"), resultSet.getString("namePlant"),
                        resultSet.getString("idUser"), resultSet.getString("quantity"))
        );
    }

    public List<Booking> getUserBookings(String id) {
        final String query = "SELECT * FROM `booking` WHERE `idUser` LIKE ?";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                new Booking(resultSet.getString("id"), resultSet.getString("namePlant"),
                        resultSet.getString("idUser"), resultSet.getString("quantity")),
                id
        );
    }

    public int deleteBooking(String id) {
        final String query = "DELETE FROM `booking` WHERE `id` LIKE ?";
        return jdbcTemplate.update(query, id);
    }

    public int updateBookingQuantity(String id, String quantity) {
        final String query = "UPDATE `booking` SET `quantity` = ? WHERE `id` LIKE ?";
        return jdbcTemplate.update(query, quantity, id);
    }

    public Booking getBookingById(String id) {
        final String query = "SELECT * FROM `booking` WHERE `id` LIKE ?";
        return jdbcTemplate.queryForObject(query, (resultSet, rowNum) ->
                new Booking(resultSet.getString("id"), resultSet.getString("namePlant"),
                        resultSet.getString("idUser"), resultSet.getString("quantity")),
                id
        );
    }
}
*/