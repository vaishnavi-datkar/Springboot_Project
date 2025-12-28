package com.datkar.hospital_management.service;

import com.datkar.hospital_management.Repo.AppintmentRepo;
import com.datkar.hospital_management.exceptions.ResourceNotFoundException;
import com.datkar.hospital_management.model.Appointment;
import com.datkar.hospital_management.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppintmentRepo appintmentRepo;


    public Appointment saveAppointment(Appointment appointment){
        return appintmentRepo.save(appointment);
    }


    //method to return all the appointment
    public List<Appointment> getAllAppointments(){

        return appintmentRepo.findAll();
    }
//method to add a appointment
    public void addAppointment(Appointment appointment){

        appintmentRepo.save(appointment);
    }

    //method to get appoint by  id
    public Appointment getAppointmentById(Long id) {
        return appintmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
    }
    public void updateAppointment(Appointment appointment) {
        if (!appintmentRepo.existsById(appointment.getId())) {
            throw new ResourceNotFoundException("Appointment not found with id: " + appointment.getId());
        }
        appintmentRepo.save(appointment);
    }
    public void deleteAppointment(Long id){
        if(!appintmentRepo.existsById(id)){
            throw new ResourceNotFoundException("Appointment not found with id: " + id);
        }
        appintmentRepo.deleteById(id);
    }


}
