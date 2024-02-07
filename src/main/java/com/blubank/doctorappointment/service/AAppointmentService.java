//package com.blubank.doctorappointment.service;
//
//import com.blubank.doctorappointment.domain.enumeration.ActionTypeEnum;
//import com.blubank.doctorappointment.domain.model.dto.AAppointmentWsDto;
//import com.blubank.doctorappointment.domain.model.dto.request.AddAppointmentRequestDtoRequest;
//import com.blubank.doctorappointment.domain.model.repository.AppointmentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//
//public abstract class AAppointmentService implements IAppointmentService {
//
//
//
//    private final AppointmentRepository appointmentRepository;
//    @Autowired
//    protected AAppointmentService(AppointmentRepository appointmentRepository) {
//        this.appointmentRepository = appointmentRepository;
//    }
//
//    @Override
//    public <R extends AAppointmentWsDto> boolean startProcess(ActionTypeEnum actionType, R request) {
//        boolean result = true;
//        LocalTime timeOfStart = LocalTime.parse(request.getTimeOfStart());
//        LocalTime timeOfFinish = LocalTime.parse(request.getTimeOfFinish());
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        LocalDate dateOfAppointment = LocalDate.parse(request.getDate(), formatter);
//        appointmentRepository.findAppointmentByDateAndStartTimeAndFinishTime();
//        switch (actionType) {
//            case Insert:
//                return addOpenAppointment((AddAppointmentRequestDtoRequest) request);
//            case Update:
//                return updateAppointment(request);
//            case Delete:
//                return deleteAppointment(request);
//        }
//        return false;
//    }
//}
