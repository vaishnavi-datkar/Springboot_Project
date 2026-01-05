package com.datkar.hospital_management.service;

import com.datkar.hospital_management.Repo.AppintmentRepo;
import com.datkar.hospital_management.exceptions.ResourceNotFoundException;
import com.datkar.hospital_management.model.Appointment;
import com.datkar.hospital_management.model.Patient;
import com.datkar.hospital_management.model.dto.AppointmentDTO;
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

    public List<Appointment> getAllAppointments() {
        return appintmentRepo.findAll();
    }

    //method to return all the appointment
    public List<AppointmentDTO> getAllAppointmentsDTO(){
        return appintmentRepo.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }
//method to add a appointment
//    public void addAppointment(Appointment appointment){
//
//        appintmentRepo.save(appointment);
//    }

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
    private AppointmentDTO mapToDTO(Appointment a) {
        AppointmentDTO dto = new AppointmentDTO();

        dto.setId(a.getId());
        dto.setAppointmentDate(a.getAppointmentDate());
        dto.setStatus(a.getStatus().name());

        if (a.getPatient() != null) {
            dto.setPatientId(a.getPatient().getPatientId());
            dto.setPatientName(a.getPatient().getPatientName());
        }

        if (a.getDoctor() != null) {
            dto.setDoctorId(Long.valueOf(a.getDoctor().getDoctorId()));
            dto.setDoctorName(a.getDoctor().getDoctorName());
        }

        return dto;
    }


}
