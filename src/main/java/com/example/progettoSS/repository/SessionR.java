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

import java.sql.PreparedStatement;


// @Repository
// public class SessionR{
//     private final static String DB_URL = "jdbc:mysql://localhost:3307/plantcommerce";
// 	private final static String DB_USERNAME = "root";

// public void deleteSessionDataFromDatabase(String sessionId) {

//     // Esegui la query per eliminare i dati della sessione dal database
//     String sql = "DELETE FROM SPRING_SESSION WHERE session_id = ?";
    
// try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, "");
//      PreparedStatement stmt = conn.prepareStatement(sql)) {
    
//     stmt.setString(1, sessionId);
//     stmt.executeUpdate();
    
//     System.out.println("Dati della sessione eliminati dal database.");
    
// } catch (SQLException e) {
//     e.printStackTrace();
//     // Gestisci l'eccezione in base alle tue esigenze
// }


// }
//}

@Repository
public class SessionR {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void deleteSessionDataFromDatabase(String sessionId) {
        // Esegui la query per eliminare i dati della sessione dal database
        String sql = "DELETE FROM SPRING_SESSION WHERE session_id = ?";
        jdbcTemplate.update(sql, sessionId);
        System.out.println("Dati della sessione eliminati dal database.");
    }
}
