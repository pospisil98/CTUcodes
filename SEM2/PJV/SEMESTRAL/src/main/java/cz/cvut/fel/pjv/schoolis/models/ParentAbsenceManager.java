/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv.schoolis.models;

import cz.cvut.fel.pjv.schoolis.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Karel
 */
public class ParentAbsenceManager {
    
    public ParentAbsenceManager() {
    }
    public void apologize(String ID_absence) {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://den1.mysql3.gear.host:3306/pjvdb?user=" + Config.DBUSER +
                "&password="+ Config.DBPASSWORD +"&" + Config.DBSETINGS)) {
            PreparedStatement q = con.prepareStatement("UPDATE absences SET `apologized` = true WHERE `ID_absence` = ?");
            q.setString(1, ID_absence);
            q.executeUpdate();
            q = con.prepareStatement("SELECT * FROM absences LIMIT 100");
            ResultSet results = q.executeQuery();
            ResultSetMetaData md = results.getMetaData();
            int nCol = md.getColumnCount();
            while(results.next() ) {
                String[] row = new String[md.getColumnCount()];
                for(int i = 1; i <= nCol; i++) {
                    Object obj = results.getObject(i);
                    row[i-1] = (obj == null) ? null:obj.toString();
                    if(i == 3) {
                        if(row[i-1] == "true") {
                            row[i-1] = "omluveno";
                        } else {
                            row[i-1] = "neomluveno";
                        }
                    }
                }
                data.add(row);
                System.out.println("Row: " + Arrays.toString(row));
            }
            System.out.println("Data: " + data);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    public ObservableList<String[]> getAbsences() {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        
        try (Connection con = DriverManager.getConnection("jdbc:mysql://den1.mysql3.gear.host:3306/pjvdb?user=" + Config.DBUSER +
                "&password="+ Config.DBPASSWORD +"&" + Config.DBSETINGS)) {
            PreparedStatement q = con.prepareStatement("SELECT * FROM absences LIMIT 100");
            ResultSet results = q.executeQuery();
            ResultSetMetaData md = results.getMetaData();
            int nCol = md.getColumnCount();
            while(results.next() ) {
                String[] row = new String[md.getColumnCount()];
                for(int i = 1; i <= nCol; i++) {
                    Object obj = results.getObject(i);
                    row[i-1] = (obj == null) ? null:obj.toString();
                    if(i == 3) {
                        if(row[i-1] == "true") {
                            row[i-1] = "omluveno";
                        } else {
                            row[i-1] = "neomluveno";
                        }
                    }
                }
                data.add(row);
                System.out.println("Row: " + Arrays.toString(row));
            }
            System.out.println("Data: " + data);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return data;
    }
}
