package com.example.attendance.service.impl;

import com.example.attendance.dto.AttendanceRequest;
import com.example.attendance.dto.AttendanceResponse;
import com.example.attendance.entity.Attendance;
import com.example.attendance.entity.User;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.UserRepository;
import com.example.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceServiceImpl implements AttendanceService {
    
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    
    @Override
    public AttendanceResponse checkIn(AttendanceRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        LocalDate today = LocalDate.now();
        
        // Check if already checked in
        if (attendanceRepository.findByUserIdAndAttendanceDate(user.getId(), today).isPresent()) {
            throw new RuntimeException("Already checked in today");
        }
        
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setAttendanceDate(today);
        attendance.setCheckInTime(LocalTime.now());
        attendance.setStatus("PRESENT");
        
        Attendance saved = attendanceRepository.save(attendance);
        return mapToResponse(saved);
    }
    
    @Override
    public AttendanceResponse checkOut(Long attendanceId, AttendanceRequest request) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
            .orElseThrow(() -> new RuntimeException("Attendance not found"));
        
        attendance.setCheckOutTime(LocalTime.now());
        
        // Calculate hours
        if (attendance.getCheckInTime() != null) {
            long minutes = java.time.Duration.between(
                attendance.getCheckInTime(), 
                attendance.getCheckOutTime()
            ).toMinutes();
            attendance.setTotalHours(minutes / 60.0);
        }
        
        Attendance saved = attendanceRepository.save(attendance);
        return mapToResponse(saved);
    }
    
    @Override
    public List<AttendanceResponse> getAttendanceByUserId(Long userId) {
        return attendanceRepository.findByUserId(userId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<AttendanceResponse> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByAttendanceDate(date).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    private AttendanceResponse mapToResponse(Attendance attendance) {
        AttendanceResponse response = new AttendanceResponse();
        response.setId(attendance.getId());
        response.setEmployeeId(attendance.getUser().getEmployeeId());
        response.setEmployeeName(attendance.getUser().getFirstName() + " " + 
                               attendance.getUser().getLastName());
        response.setAttendanceDate(attendance.getAttendanceDate());
        response.setCheckInTime(attendance.getCheckInTime());
        response.setCheckOutTime(attendance.getCheckOutTime());
        response.setStatus(attendance.getStatus());
        response.setTotalHours(attendance.getTotalHours());
        response.setOvertimeHours(attendance.getOvertimeHours());
        response.setRemarks(attendance.getRemarks());
        response.setDepartment(attendance.getUser().getDepartment());
        return response;
    }
}