package sn.iage.isi.employeejavafx.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static sn.iage.isi.employeejavafx.config.Constants.*;

public class DB {
    private Connection cnx; //Pour la connexion à la base de données
    private PreparedStatement pstm; //Pour les requetes préparées
    private ResultSet rs; //Pour les résultats des requétes de type consultation (SELECT)
    private int ok; //Pour les résultats des requétes de type mise à jour (INSERT INTO, UPDATE et DELETE)

    private void connect(){
        try {
            Class.forName(DRIVER);
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
        }catch(Exception e){
            System.out.println("Error in connecting to database."+e.getMessage());
            e.printStackTrace();
        }
    }

    public void initPrepar(String sql){
        try{
            connect();
            pstm = cnx.prepareStatement(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ResultSet executeSelect(){
        rs = null;
        try{
            rs = pstm.executeQuery();
        }catch (Exception e){
            e.printStackTrace();
        }
        return rs;
    }

    public int executeMaj(){
        try{
            ok = pstm.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ok;
    }

    public void closeConnection(){
        try{
            if (cnx != null)
                cnx.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }
}
