USE HealthcareDB;
GO

-- Query 1: Danh sách bệnh nhân cần TÁI KHÁM (follow-up)
-- kèm tên bác sĩ phụ trách và khoa, sắp xếp theo ngày tái khám gần nhất
SELECT
    p.fname + ' ' + p.lname        AS patient_name,
    p.phone,
    c.consultation_date,
    c.follow_up_date,
    e.fname + ' ' + e.last_name    AS doctor_name,
    dep.department_name
FROM CONSULTATION c
JOIN APPOINTMENT a  ON c.appointment_id = a.appointment_id
JOIN PATIENT p      ON a.patient_id      = p.patient_id
JOIN DOCTOR d        ON a.doctor_id       = d.doctor_id
JOIN EMPLOYEE e       ON d.employee_id     = e.employee_id
JOIN DEPARTMENT dep   ON e.department_id   = dep.department_id
WHERE c.follow_up_required = 1
ORDER BY c.follow_up_date ASC;
GO










-- Query 2 (cá nhân): Danh sách lịch hẹn SẮP TỚI (Scheduled) kèm
-- thông tin bác sĩ, bệnh nhân, khoa - dùng cho lễ tân theo dõi lịch
SELECT
    a.appointment_id,
    a.appointment_date,
    a.start_time,
    a.end_time,
    a.appointment_type,
    p.fname + ' ' + p.lname     AS patient_name,
    p.phone                      AS patient_phone,
    e.fname + ' ' + e.last_name AS doctor_name,
    dep.department_name
FROM APPOINTMENT a
JOIN PATIENT p       ON a.patient_id = p.patient_id
JOIN DOCTOR d        ON a.doctor_id  = d.doctor_id
JOIN EMPLOYEE e       ON d.employee_id = e.employee_id
JOIN DEPARTMENT dep   ON e.department_id = dep.department_id
WHERE a.status = 'Scheduled'
ORDER BY a.appointment_date, a.start_time;
GO
