package com.datkar.hospital_management.service;

import com.datkar.hospital_management.Repo.AppintmentRepo;
import com.datkar.hospital_management.Repo.DoctorRepo;
import com.datkar.hospital_management.Repo.PatientRepo;
import com.datkar.hospital_management.exceptions.ResourceNotFoundException;
import com.datkar.hospital_management.model.Appointment;
import com.datkar.hospital_management.model.Patient;
import com.datkar.hospital_management.model.dto.AppointmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppintmentRepo appintmentRepo;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    public Appointment saveAppointment(Appointment appointment){
        return appintmentRepo.save(appointment);
    }

    public Page<AppointmentDTO> getAllAppointmentsDTO(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return appintmentRepo.findAllWithPatientAndDoctor(pageable)
                .map(this::convertToDTO);
    }

    // For DOCTOR - get appointments where doctor.userId = userId
    public Page<AppointmentDTO> getAppointmentsByDoctorUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return appintmentRepo.findByDoctorUserId(userId, pageable).map(this::convertToDTO);
    }

    // For PATIENT - get appointments created by user OR patient name matches
    public Page<AppointmentDTO> getAppointmentsByPatientUserId(Long userId, String patientName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return appintmentRepo.findByPatientUserIdOrName(userId, patientName, pageable).map(this::convertToDTO);
    }

    private AppointmentDTO convertToDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setStatus(appointment.getStatus().name());
        dto.setCreatedBy(appointment.getCreatedBy());

        if (appointment.getDoctor() != null) {
            dto.setDoctorId(appointment.getDoctor().getDoctorId().longValue());
            dto.setDoctorName(appointment.getDoctor().getDoctorName());
            dto.setDoctorSpecialization(appointment.getDoctor().getSpecialization());
        }

        if (appointment.getPatient() != null) {
            dto.setPatientId(appointment.getPatient().getPatientId());
            dto.setPatientName(appointment.getPatient().getPatientName());
            dto.setPatientPhone(appointment.getPatient().getPhone());
            dto.setPatientAge(appointment.getPatient().getAge());
            dto.setPatientBloodGroup(appointment.getPatient().getBloodGroup());
        }

        return dto;
    }

    public Appointment getAppointmentById(Long id) {
        return appintmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
    }

    public void deleteAppointment(Long id){
        if(!appintmentRepo.existsById(id)){
            throw new ResourceNotFoundException("Appointment not found with id: " + id);
        }
        appintmentRepo.deleteById(id);
    }
}
