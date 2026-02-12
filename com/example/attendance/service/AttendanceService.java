package com.example.attendance.service;

import com.example.attendance.dto.AttendanceRequest;
import com.example.attendance.dto.AttendanceResponse;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    AttendanceResponse checkIn(AttendanceRequest request);
    AttendanceResponse checkOut(Long attendanceId, AttendanceRequest request);
    List<AttendanceResponse> getAttendanceByUserId(Long userId);
    List<AttendanceResponse> getAttendanceByDate(LocalDate date);
}