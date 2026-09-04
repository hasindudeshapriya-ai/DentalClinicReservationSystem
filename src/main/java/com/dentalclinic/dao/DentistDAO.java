/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.dao;

import com.dentalclinic.model.Dentist;
import com.dentalclinic.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hasin
 */
public class DentistDAO {



    public boolean saveDentist(Dentist dentist) {

        boolean saved = false;

        try {

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO dentists "
                    + "(dentist_name, specialization, phone, email) "
                    + "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, dentist.getDentistName());
            ps.setString(2, dentist.getSpecialization());
            ps.setString(3, dentist.getPhone());
            ps.setString(4, dentist.getEmail());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                saved = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return saved;
    }


  public List<Dentist> getAllDentists() {

        List<Dentist> dentists = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM dentists ORDER BY dentist_id";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Dentist dentist = new Dentist();

                dentist.setDentistId(
                        rs.getInt("dentist_id"));

                dentist.setDentistName(
                        rs.getString("dentist_name"));

                dentist.setSpecialization(
                        rs.getString("specialization"));

                dentist.setPhone(
                        rs.getString("phone"));

                dentist.setEmail(
                        rs.getString("email"));

                dentists.add(dentist);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dentists;
    }

    public Dentist getDentistById(int dentistId) {

        Dentist dentist = null;

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM dentists "
                    + "WHERE dentist_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, dentistId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                dentist = new Dentist();

                dentist.setDentistId(
                        rs.getInt("dentist_id"));

                dentist.setDentistName(
                        rs.getString("dentist_name"));

                dentist.setSpecialization(
                        rs.getString("specialization"));

                dentist.setPhone(
                        rs.getString("phone"));

                dentist.setEmail(
                        rs.getString("email"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dentist;
    }


    public boolean updateDentist(Dentist dentist) {

        boolean updated = false;

        try {

            Connection con = DBConnection.getConnection();

            String sql = "UPDATE dentists SET "
                    + "dentist_name=?, "
                    + "specialization=?, "
                    + "phone=?, "
                    + "email=? "
                    + "WHERE dentist_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, dentist.getDentistName());
            ps.setString(2, dentist.getSpecialization());
            ps.setString(3, dentist.getPhone());
            ps.setString(4, dentist.getEmail());
            ps.setInt(5, dentist.getDentistId());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                updated = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return updated;
    }



    public boolean deleteDentist(int dentistId) {

        boolean deleted = false;

        try {

            Connection con = DBConnection.getConnection();

            String sql = "DELETE FROM dentists "
                    + "WHERE dentist_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, dentistId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                deleted = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return deleted;
    }
}