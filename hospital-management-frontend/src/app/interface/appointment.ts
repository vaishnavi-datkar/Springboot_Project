export interface Appointment {
  id?: number;
  appointmentDate: string;
  patientId: number;
  patientName?: string;
  doctorId: number;
  doctorName?: string;
  status: string;
  prescriptionId?: number;
}