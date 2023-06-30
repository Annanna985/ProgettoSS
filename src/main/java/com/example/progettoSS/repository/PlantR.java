package com.example.progettoSS.repository;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;
import com.example.progettoSS.entity.Plant;
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
public class PlantR{
   
    private final static String DB_URL = "jdbc:mysql://localhost:3307/plantcommerce";
	private final static String DB_USERNAME = "root";

    public List<Plant> getAllPlants(){
        List<Plant> list =new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;
        String result = null;
        try{
         connection = DriverManager.getConnection(DB_URL, DB_USERNAME,"");
         statement = connection.createStatement();
 
        final String query = "SELECT * FROM `plant`";
        System.out.println(query);
        resultSet = statement.executeQuery(query);
  
       
            while(resultSet.next()){
            String hexString = resultSet.getString("immagine");
            byte[] content = hexStringToByteArray(hexString);
          //  result = Base64.getEncoder().encodeToString(content);
            Plant p = new Plant(resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("prezzo"), resultSet.getString("altezzaPianta"),content);
               list.add(p);
   
            }
            
            }
            catch (final SQLException e) {
            e.printStackTrace();
            
            } 
            return list;
        }

          @Autowired
            private JdbcTemplate jdbcTemplate;

        public void addPlant(String name, String prezzo, String altezzaPianta, MultipartFile file) {
           String fileName = StringUtils.cleanPath(file.getOriginalFilename());
          // Statement statement = null;
        
           Connection connection = null;
            String id = UUID.randomUUID().toString();
            System.out.println("sono in addplantrepository");
            try{
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME,"");
            //statement = connection.createStatement();
     
           byte[] content = file.getBytes();
         String hexString = byteArrayToHexString(content);
            final String query = "INSERT INTO `plant` (`id`,`name`, `prezzo`,`immagine`, `altezzaPianta`) VALUES ('"+id+"','"+name+"','"+prezzo+"','"+hexString+"','"+altezzaPianta+"')";
           System.out.println(query);
           jdbcTemplate.execute(query);
            
            }  catch (final SQLException e) {
             e.printStackTrace();
            } 
            catch (IOException e) {
    System.out.println("Can't read a"); // Or something more intellegent
}
    }




        public static String byteArrayToHexString(byte[] byteArray) {
    StringBuilder sb = new StringBuilder();
    for (byte b : byteArray) {
        sb.append(String.format("%02X", b));
    }
    return sb.toString();
}

public static byte[] hexStringToByteArray(String hexString) {
    int length = hexString.length();
    byte[] byteArray = new byte[length / 2];
    for (int i = 0; i < length; i += 2) {
        byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                                + Character.digit(hexString.charAt(i + 1), 16));
    }
    return byteArray;
}

  public int deletePlant(String id){

            int rowsAffected = 0;
           // Statement statement = null;
            Connection connection = null;
             try{
                connection = DriverManager.getConnection(DB_URL, DB_USERNAME,"");
               // statement = connection.createStatement();
        
                final String query = "DELETE FROM `plant` WHERE `id` LIKE '" + id + "'";
                System.out.println(query);
                //rowsAffected = statement.executeUpdate(query);
                jdbcTemplate.execute(query);
        
            }
            catch (final SQLException e) {
            e.printStackTrace();
            
            } 
        
            return rowsAffected;
        }
}*/

/**************************NO PREPARED STATEMENT********************************/

@Repository
public class PlantR {
   
    private final static String DB_URL = "jdbc:mysql://localhost:3307/plantcommerce";
    private final static String DB_USERNAME = "root";
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<Plant> getAllPlants() {
        List<Plant> list = new ArrayList<>();
        
        String query = "SELECT * FROM `plant`";
        System.out.println(query);
        
        jdbcTemplate.query(query, (resultSet, rowNum) -> {
            String hexString = resultSet.getString("immagine");
            byte[] content = hexStringToByteArray(hexString);
            Plant p = new Plant(resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("prezzo"), resultSet.getString("altezzaPianta"), content);
            list.add(p);
            return null;
        });
        
        return list;
    }

    public void addPlant(String name, String prezzo, String altezzaPianta, MultipartFile file) {
        //SPOTBUGS: Mi segnala che filename non viene utilizzata-effettivamente era di ua implementazione precedente
       // String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        byte[] content;
        
        try {
            content = file.getBytes();
            String hexString = byteArrayToHexString(content);
            String id = UUID.randomUUID().toString();
            
            String query = "INSERT INTO `plant` (`id`,`name`, `prezzo`,`immagine`, `altezzaPianta`) VALUES ('" + id + "','" + name + "','" + prezzo + "','" + hexString + "','" + altezzaPianta + "')";
            System.out.println(query);
            
            jdbcTemplate.execute(query);
        } catch (IOException e) {
            System.out.println("Can't read file");
        }
    }

    public int deletePlant(String id) {
        int rowsAffected = 0;
        
        String query = "DELETE FROM `plant` WHERE `id` LIKE '" + id + "'";
        System.out.println(query);
        
        rowsAffected = jdbcTemplate.update(query);
        
        return rowsAffected;
    }
    
    public static String byteArrayToHexString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder();
        for (byte b : byteArray) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return byteArray;
    }
}



/**************************PREPARED STATEMENT********************************/
/*
@Repository
public class PlantR {
    private final static String DB_URL = "jdbc:mysql://localhost:3307/plantcommerce";
    private final static String DB_USERNAME = "root";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Plant> getAllPlants() {
        List<Plant> list = new ArrayList<>();
        final String query = "SELECT * FROM `plant`";
        System.out.println(query);
        jdbcTemplate.query(query, resultSet -> {
            while (resultSet.next()) {
                String hexString = resultSet.getString("immagine");
                byte[] content = hexStringToByteArray(hexString);
                Plant p = new Plant(resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("prezzo"), resultSet.getString("altezzaPianta"), content);
                list.add(p);
            }
        });
        return list;
    }

    public void addPlant(String name, String prezzo, String altezzaPianta, MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String id = UUID.randomUUID().toString();
        System.out.println("sono in addplantrepository");
        try {
            byte[] content = file.getBytes();
            String hexString = byteArrayToHexString(content);
            final String query = "INSERT INTO `plant` (`id`, `name`, `prezzo`, `immagine`, `altezzaPianta`) VALUES (?, ?, ?, ?, ?)";
            System.out.println(query);
            jdbcTemplate.update(query, id, name, prezzo, hexString, altezzaPianta);
        } catch (IOException e) {
            System.out.println("Can't read file");
        }
    }

    public int deletePlant(String id) {
        final String query = "DELETE FROM `plant` WHERE `id` LIKE ?";
        System.out.println(query);
        return jdbcTemplate.update(query, id);
    }

    public static String byteArrayToHexString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder();
        for (byte b : byteArray) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return byteArray;
    }
}*/
