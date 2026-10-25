



/* SCALAR FUNCTION */

IF OBJECT_ID('fn_PatientOutstandingBalance') IS NOT NULL
    DROP FUNCTION fn_PatientOutstandingBalance;
GO
CREATE FUNCTION fn_PatientOutstandingBalance (@patient_id INT)
RETURNS DECIMAL(12,2)
AS
BEGIN
    DECLARE @balance DECIMAL(12,2);

    SELECT @balance = ISNULL(SUM(
        (b.consultation_fee + b.medication_fee + b.lab_test_fee + b.other_charges)
        * (1 - b.discount)
    ), 0)
    FROM BILLING b
    JOIN CONSULTATION c ON b.consultation_id = c.consultation_id
    JOIN APPOINTMENT a  ON c.appointment_id  = a.appointment_id
    WHERE a.patient_id = @patient_id
      AND b.payment_status IN ('Unpaid', 'Partially Paid');

    RETURN @balance;
END;
GO



-- Test function
SELECT dbo.fn_PatientOutstandingBalance(4) AS no_con_lai_benh_nhan;
GO







/* STORED PROCEDURE  */

IF OBJECT_ID('sp_CancelAppointment') IS NOT NULL
    DROP PROCEDURE sp_CancelAppointment;
GO
CREATE PROCEDURE sp_CancelAppointment
    @appointment_id   INT,
    @cancelled_by_emp INT,          -- employee_id của người thực hiện hủy lịch
    @reason           NVARCHAR(255) = NULL
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @current_status VARCHAR(20);
    DECLARE @patient_id     INT;

    SELECT @current_status = status, @patient_id = patient_id
    FROM APPOINTMENT
    WHERE appointment_id = @appointment_id;

    IF @current_status IS NULL
    BEGIN
        RAISERROR(N'Không tìm thấy lịch hẹn.', 16, 1);
        RETURN;
    END

    IF @current_status IN ('Completed', 'Cancelled')
    BEGIN
        RAISERROR(N'Lịch hẹn đã hoàn thành hoặc đã hủy trước đó, không thể hủy lại.', 16, 1);
        RETURN;
    END

    UPDATE APPOINTMENT
    SET status = 'Cancelled',
        reason = ISNULL(@reason, reason)
    WHERE appointment_id = @appointment_id;

    INSERT INTO NOTIFICATION (title, message, notification_type, channel, is_read, sent_at, employee_id, patient_id)
    VALUES (
        N'Lịch hẹn đã bị hủy',
        N'Lịch hẹn #' + CAST(@appointment_id AS VARCHAR(10)) + N' của bạn đã được hủy. Vui lòng liên hệ đặt lại nếu cần.',
        'Alert', 'SMS', 0, GETDATE(), @cancelled_by_emp, @patient_id
    );
END;
GO




-- Test procedure: hủy lịch hẹn số 1 (đang Scheduled) do nhân viên số 8 thực hiện
EXEC sp_CancelAppointment
    @appointment_id = 1,
    @cancelled_by_emp = 8,
    @reason = N'Bệnh nhân xin dời lịch';
GO
SELECT appointment_id, status, reason FROM APPOINTMENT WHERE appointment_id = 1;
GO




/* TRIGGER */

IF OBJECT_ID('trg_Appointment_PreventOverlap', 'TR') IS NOT NULL
    DROP TRIGGER trg_Appointment_PreventOverlap;
GO
CREATE TRIGGER trg_Appointment_PreventOverlap
ON APPOINTMENT
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS (
        SELECT 1
        FROM INSERTED i
        JOIN APPOINTMENT a
            ON a.doctor_id = i.doctor_id
           AND a.appointment_date = i.appointment_date
           AND a.appointment_id <> i.appointment_id
           AND a.status <> 'Cancelled'
           AND i.start_time < a.end_time
           AND i.end_time   > a.start_time
    )
    BEGIN
        RAISERROR(N'Trùng lịch: bác sĩ đã có lịch hẹn khác trong khung giờ này.', 16, 1);
        ROLLBACK TRANSACTION;
    END
END;
GO




-- Test trigger: thử insert 1 appointment trùng giờ với appointment_id = 1
-- (bac si 1, 2026-07-01, 08:00-08:30 da co san) -> ky vong bi loi/rollback
BEGIN TRY
    INSERT INTO APPOINTMENT (appointment_date, start_time, end_time, status, appointment_type, reason, doctor_id, patient_id)
    VALUES ('2026-07-01', '08:15', '08:45', 'Scheduled', 'Kham benh', N'Test trung lich', 1, 1);
END TRY
BEGIN CATCH
    PRINT N'Trigger da chan thanh cong: ' + ERROR_MESSAGE();
END CATCH
GO


