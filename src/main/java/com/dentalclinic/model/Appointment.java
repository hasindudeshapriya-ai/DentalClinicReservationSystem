/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.model;

/**
 *
 * @author hasin
 */
public class Appointment {
    
    private int appointmentId;
    private int patientId;
    private int dentistId;

    private String appointmentDate;
    private String appointmentTime;

    private String treatment;
    private String status;
    private String patientName;
    private String dentistName;
    
    public Appointment() {
    }

    public Appointment(int appointmentId, int patientId, int dentistId,
            String appointmentDate, String appointmentTime,
            String treatment, String status) {

        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.treatment = treatment;
        this.status = status;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDentistId() {
        return dentistId;
    }

    public void setDentistId(int dentistId) {
        this.dentistId = dentistId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getPatientName() {
    return patientName;
}

public void setPatientName(String patientName) {
    this.patientName = patientName;
}

public String getDentistName() {
    return dentistName;
}

public void setDentistName(String dentistName) {
    this.dentistName = dentistName;
}
    
}
