/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.model;

/**
 *
 * @author hasin
 */
public class Dentist {

    private int dentistId;
    private String dentistName;
    private String specialization;
    private String phone;
    private String email;

    
    public Dentist() {
    }

   
    public Dentist(int dentistId, String dentistName,
                   String specialization, String phone, String email) {

        this.dentistId = dentistId;
        this.dentistName = dentistName;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
    }

    
    public int getDentistId() {
        return dentistId;
    }

    public void setDentistId(int dentistId) {
        this.dentistId = dentistId;
    }

    
    public String getDentistName() {
        return dentistName;
    }

    public void setDentistName(String dentistName) {
        this.dentistName = dentistName;
    }

    
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

   
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

 
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}