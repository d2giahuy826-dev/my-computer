# 📐 Phân tích Phụ thuộc hàm (Functional Dependencies)
## Healthcare Appointment & Patient Record System

---

## I. Phụ thuộc hàm (Functional Dependency) là gì?

### Định nghĩa:
Cho quan hệ R(A, B, C, ...). Ta nói **thuộc tính B phụ thuộc hàm vào thuộc tính A** (ký hiệu: `A → B`) nếu và chỉ nếu:

> **Mỗi khi hai dòng (tuple) có cùng giá trị ở cột A, thì chúng BẮT BUỘC phải có cùng giá trị ở cột B.**

### Cách đọc ký hiệu:
* `A → B` đọc là: *"A xác định B"* hoặc *"B phụ thuộc hàm vào A"*
* Nghĩa là: Nếu biết giá trị của A, ta có thể **suy ra duy nhất** giá trị của B

### Ví dụ trực quan:
* `employee_id → first_name` : Biết mã nhân viên là `3` → xác định duy nhất tên là `"Hùng"`. (Không thể có 2 nhân viên cùng ID `3` nhưng khác tên).
* `{start_time, end_time} → duration` : Biết giờ bắt đầu và giờ kết thúc → tính được duy nhất thời lượng.

### Các loại phụ thuộc hàm quan trọng:

| Loại | Ký hiệu | Giải thích | Ảnh hưởng đến chuẩn hóa |
|---|---|---|---|
| **Phụ thuộc hàm đầy đủ** | `{A, B} → C` | C phụ thuộc vào **toàn bộ** tập {A, B}, không thể bỏ A hoặc B | ✅ Đạt chuẩn 2NF |
| **Phụ thuộc hàm bộ phận** | `A → C` (trong khi PK là {A, B}) | C chỉ phụ thuộc vào **một phần** của khóa chính | ❌ Vi phạm 2NF |
| **Phụ thuộc hàm bắc cầu** | `A → B → C` | A xác định B, B xác định C (C phụ thuộc gián tiếp vào A thông qua B) | ❌ Vi phạm 3NF |

---

## II. Phân tích Phụ thuộc hàm cho từng Entity

---

### Entity 1: EMPLOYEE (Nhân viên — Supertype)

**Khóa chính (PK):** `employee_id`
**Khóa ứng viên (CK):** `employee_code`, `email`

#### Các phụ thuộc hàm:

```
F = {
  employee_id → employee_code, first_name, last_name, phone, email, 
                department_id, hire_date, status, role

  employee_code → employee_id, first_name, last_name, phone, email, 
                  department_id, hire_date, status, role

  email → employee_id, employee_code, first_name, last_name, phone, 
          department_id, hire_date, status, role
}
```

#### Giải thích:
* **`employee_id → tất cả`**: Biết mã nhân viên (ID) → xác định duy nhất toàn bộ thông tin của nhân viên đó. Đây là phụ thuộc hàm từ Khóa chính.
* **`employee_code → tất cả`**: Vì `employee_code` là Khóa ứng viên (UNIQUE), nên nó cũng xác định duy nhất mọi thuộc tính khác. Ví dụ: Biết mã `NV-2026-001` → chỉ có duy nhất 1 nhân viên.
* **`email → tất cả`**: Tương tự, email cũng là UNIQUE nên cũng xác định duy nhất.

> **Nhận xét:** Không có phụ thuộc bộ phận (vì PK chỉ có 1 thuộc tính). Không có phụ thuộc bắc cầu (vì `department_id` chỉ là FK tham chiếu, các thuộc tính của Department không nằm trong bảng này). → **Đạt chuẩn 3NF** ✅

---

### Entity 2: DOCTOR (Bác sĩ — Subtype)

**Khóa chính (PK):** `employee_id` (đồng thời là FK → EMPLOYEE)
**Khóa ứng viên (CK):** `license_number`

#### Các phụ thuộc hàm:

```
F = {
  employee_id → license_number, specialization, consultation_fee

  license_number → employee_id, specialization, consultation_fee
}
```

#### Giải thích:
* **`employee_id → tất cả`**: Biết ID nhân viên (bác sĩ) → xác định duy nhất chứng chỉ hành nghề, chuyên khoa, phí khám của bác sĩ đó.
* **`license_number → tất cả`**: Số chứng chỉ hành nghề là duy nhất trên toàn quốc → biết số chứng chỉ là biết chính xác bác sĩ nào.

> **Nhận xét:** Không có phụ thuộc bộ phận hay bắc cầu. → **Đạt chuẩn 3NF** ✅

---

### Entity 3: PATIENT (Bệnh nhân)

**Khóa chính (PK):** `patient_id`
**Khóa ứng viên (CK):** `medical_record_number`, `email`

#### Các phụ thuộc hàm:

```
F = {
  patient_id → medical_record_number, first_name, last_name, 
               date_of_birth, gender, phone, email, 
               street_address, ward, district, city,
               blood_type, insurance_number, status, registration_date

  medical_record_number → patient_id, first_name, last_name, 
                          date_of_birth, gender, phone, email, 
                          street_address, ward, district, city,
                          blood_type, insurance_number, status, registration_date

  email → patient_id, medical_record_number, first_name, last_name, ...

  date_of_birth → age              ← (Phụ thuộc hàm dẫn xuất)
}
```

#### Giải thích:
* **`patient_id → tất cả`**: Phụ thuộc hàm chính từ Khóa chính.
* **`medical_record_number → tất cả`**: Khóa ứng viên — mã hồ sơ bệnh án duy nhất.
* **`date_of_birth → age`**: Thuộc tính dẫn xuất — tuổi tính được từ ngày sinh. Thuộc tính `age` không được lưu trong bảng vật lý.

> [!NOTE]
> **Về thuộc tính đa trị `allergies`:** Thuộc tính này đã được tách thành bảng riêng `PATIENT_ALLERGY` khi chuyển sang lược đồ quan hệ (để đạt chuẩn 1NF). Do đó, nó không xuất hiện trong tập phụ thuộc hàm của bảng PATIENT.

> **Nhận xét:** Sau khi loại bỏ thuộc tính dẫn xuất (`age`) và thuộc tính đa trị (`allergies`), không còn phụ thuộc bộ phận hay bắc cầu. → **Đạt chuẩn 3NF** ✅

---

### Entity 4: DEPARTMENT (Khoa / Phòng ban)

**Khóa chính (PK):** `department_id`
**Khóa ứng viên (CK):** `department_name`

#### Các phụ thuộc hàm:

```
F = {
  department_id → department_name, description, location

  department_name → department_id, description, location
}
```

#### Giải thích:
* **`department_id → tất cả`**: Biết ID khoa → xác định duy nhất tên khoa, mô tả, vị trí.
* **`department_name → tất cả`**: Tên khoa là duy nhất trong bệnh viện (UNIQUE) → cũng xác định được toàn bộ thông tin.

> **Nhận xét:** Bảng rất đơn giản, không có phụ thuộc bộ phận hay bắc cầu. → **Đạt chuẩn 3NF** ✅

---

### Entity 5: APPOINTMENT (Lịch hẹn khám)

**Khóa chính (PK):** `appointment_id`
**Ràng buộc nghiệp vụ:** `{doctor_id, appointment_date, start_time}` → `appointment_id` (Business Rule: không được overlap cho cùng bác sĩ)

#### Các phụ thuộc hàm:

```
F = {
  appointment_id → patient_id, doctor_id, appointment_date, 
                   start_time, end_time, status, appointment_type, 
                   reason, cancellation_reason

  {doctor_id, appointment_date, start_time} → appointment_id
                   ← (Ràng buộc nghiệp vụ: mỗi bác sĩ, mỗi ngày, mỗi giờ 
                      chỉ có tối đa 1 lịch hẹn)

  {start_time, end_time} → duration       ← (Phụ thuộc hàm dẫn xuất)
}
```

#### Giải thích:
* **`appointment_id → tất cả`**: Phụ thuộc hàm chính từ Khóa chính.
* **`{doctor_id, appointment_date, start_time} → appointment_id`**: Đây là phụ thuộc hàm đặc biệt suy ra từ Business Rule *"Appointments must not overlap for the same doctor"*. Nếu biết bác sĩ nào, ngày nào, giờ nào → xác định được duy nhất lịch hẹn nào.
* **`{start_time, end_time} → duration`**: Thuộc tính dẫn xuất, không lưu trong bảng.

> **Nhận xét:** Không có phụ thuộc bộ phận (PK đơn). Không có phụ thuộc bắc cầu. → **Đạt chuẩn 3NF** ✅

---

### Entity 6: DOCTOR_SCHEDULE (Lịch làm việc bác sĩ)

**Khóa chính (PK):** `schedule_id`
**Ràng buộc nghiệp vụ:** `{doctor_id, day_of_week, start_time}` là UNIQUE (mỗi bác sĩ, mỗi thứ, mỗi giờ chỉ có 1 ca)

#### Các phụ thuộc hàm:

```
F = {
  schedule_id → doctor_id, day_of_week, start_time, end_time, 
                slot_duration, max_patients, is_available

  {doctor_id, day_of_week, start_time} → schedule_id, end_time, 
                                         slot_duration, max_patients, is_available
}
```

#### Giải thích:
* **`schedule_id → tất cả`**: Phụ thuộc hàm chính.
* **`{doctor_id, day_of_week, start_time} → tất cả`**: Biết bác sĩ nào, thứ mấy, bắt đầu lúc mấy giờ → xác định duy nhất ca trực đó và mọi thông tin đi kèm (giờ kết thúc, thời lượng slot, số bệnh nhân tối đa...).

> **Nhận xét:** Không có phụ thuộc bộ phận hay bắc cầu. → **Đạt chuẩn 3NF** ✅

---

### Entity 7: MEDICAL_RECORD (Hồ sơ bệnh án)

**Khóa chính (PK):** `record_id`

#### Các phụ thuộc hàm:

```
F = {
  record_id → patient_id, chief_complaint, medical_history, family_history,
              current_medications, height, weight, blood_pressure,
              access_level, last_updated_by

  {height, weight} → BMI              ← (Phụ thuộc hàm dẫn xuất: BMI = weight / height²)
}
```

#### Giải thích:
* **`record_id → tất cả`**: Biết ID hồ sơ → xác định duy nhất toàn bộ thông tin bệnh án.
* **`{height, weight} → BMI`**: Thuộc tính dẫn xuất, không lưu trong bảng vật lý.

> **Nhận xét:** Không có phụ thuộc bộ phận hay bắc cầu. → **Đạt chuẩn 3NF** ✅

---

### Entity 8: CONSULTATION (Lượt khám)

**Khóa chính (PK):** `consultation_id`
**Khóa ứng viên (CK):** `appointment_id` (UNIQUE — quan hệ 1:1 với APPOINTMENT)

#### Các phụ thuộc hàm:

```
F = {
  consultation_id → appointment_id, patient_id, doctor_id,
                    consultation_date, symptoms, examination_notes,
                    vital_signs, follow_up_required, follow_up_date

  appointment_id → consultation_id, patient_id, doctor_id,
                   consultation_date, symptoms, examination_notes,
                   vital_signs, follow_up_required, follow_up_date
}
```

#### Giải thích:
* **`consultation_id → tất cả`**: Phụ thuộc hàm chính.
* **`appointment_id → tất cả`**: Vì mỗi lịch hẹn chỉ có tối đa 1 lượt khám (quan hệ 1:0..1), nên `appointment_id` cũng là Khóa ứng viên.

> [!NOTE]
> **Về `patient_id` và `doctor_id`:** Hai thuộc tính này có thể suy ra từ `appointment_id` (thông qua bảng APPOINTMENT). Việc lưu chúng ở đây là **phi chuẩn hóa có chủ đích (Denormalization)** để tối ưu truy vấn, tránh phải JOIN nhiều bảng. Trong bối cảnh môn học, bạn có thể ghi chú: *"Chấp nhận dư thừa có kiểm soát để tối ưu hiệu năng truy vấn"*.

> **Nhận xét:** Nếu loại bỏ `patient_id` và `doctor_id` (vì chúng phụ thuộc bắc cầu qua `appointment_id`), bảng đạt chuẩn 3NF. Nếu giữ lại (denormalization), cần ghi chú lý do. → **Đạt chuẩn 3NF** ✅ (với ghi chú denormalization)

---

### Entity 9: DIAGNOSIS (Chẩn đoán)

**Khóa chính (PK):** `diagnosis_id`

#### Các phụ thuộc hàm:

```
F = {
  diagnosis_id → consultation_id, icd_code, diagnosis_name, 
                 diagnosis_type, severity, notes

  icd_code → diagnosis_name        ← ⚠️ Phụ thuộc hàm BẮC CẦU tiềm ẩn
}
```

#### Giải thích:
* **`diagnosis_id → tất cả`**: Phụ thuộc hàm chính.
* **`icd_code → diagnosis_name`**: Đây là một **phụ thuộc hàm bắc cầu tiềm ẩn**. Về lý thuyết, mã ICD `J00` luôn tương ứng với tên bệnh *"Viêm mũi họng cấp"*. Tức là:
  * `diagnosis_id → icd_code → diagnosis_name` (Bắc cầu!)

> [!WARNING]
> **Phụ thuộc bắc cầu (Transitive Dependency):**
> Chuỗi `diagnosis_id → icd_code → diagnosis_name` cho thấy `diagnosis_name` phụ thuộc **gián tiếp** vào Khóa chính thông qua `icd_code`. Về lý thuyết chuẩn 3NF, ta cần tách `icd_code` và `diagnosis_name` ra thành một bảng riêng `ICD_DISEASE(icd_code, diagnosis_name)`.
>
> **Tuy nhiên, trong thực tế y tế**, bác sĩ thường gõ `diagnosis_name` theo ngôn ngữ tự nhiên (Ví dụ: *"Viêm mũi họng cấp do thay đổi thời tiết"*), khác với tên chuẩn của ICD (*"Acute nasopharyngitis"*). Do đó, `icd_code` không hoàn toàn xác định `diagnosis_name` trong mọi trường hợp, nên việc giữ nguyên thiết kế hiện tại là **chấp nhận được**.

> **Nhận xét:** Có phụ thuộc bắc cầu tiềm ẩn nhưng chấp nhận được vì lý do nghiệp vụ. → **Đạt chuẩn 2NF, tiệm cận 3NF** ⚠️

---

### Entity 10: PRESCRIPTION (Đơn thuốc)

**Khóa chính (PK):** `prescription_id`

#### Các phụ thuộc hàm:

```
F = {
  prescription_id → consultation_id, patient_id, doctor_id,
                    prescription_date, status, pharmacy_notes
}
```

#### Giải thích:
* **`prescription_id → tất cả`**: Biết ID đơn thuốc → xác định duy nhất toàn bộ thông tin.

> [!NOTE]
> **Tương tự Entity 8:** `patient_id` và `doctor_id` có thể suy ra từ `consultation_id` (thông qua bảng CONSULTATION). Đây là **denormalization có chủ đích** để Patient có thể truy vấn trực tiếp danh sách đơn thuốc mà không cần JOIN qua CONSULTATION.

> **Nhận xét:** Không có phụ thuộc bộ phận. Phụ thuộc bắc cầu do denormalization có kiểm soát. → **Đạt chuẩn 3NF** ✅ (với ghi chú denormalization)

---

### Entity 11: PRESCRIPTION_DETAIL (Chi tiết đơn thuốc)

**Khóa chính (PK):** `detail_id`

#### Các phụ thuộc hàm:

```
F = {
  detail_id → prescription_id, medication_name, dosage, frequency,
              duration, quantity, route, instructions
}
```

#### Giải thích:
* **`detail_id → tất cả`**: Biết ID dòng chi tiết → xác định duy nhất thuốc gì, liều bao nhiêu, uống mấy lần...

> **Nhận xét:** Bảng rất đơn giản, không có phụ thuộc bộ phận hay bắc cầu. → **Đạt chuẩn 3NF** ✅

---

### Entity 12: BILLING (Hóa đơn)

**Khóa chính (PK):** `billing_id`
**Khóa ứng viên (CK):** `invoice_number`, `appointment_id` (UNIQUE — mỗi appointment chỉ có tối đa 1 hóa đơn)

#### Các phụ thuộc hàm:

```
F = {
  billing_id → appointment_id, patient_id, invoice_number,
               consultation_fee, medication_fee, lab_test_fee, other_charges,
               discount, payment_method, payment_status, billing_date, processed_by

  invoice_number → billing_id, appointment_id, patient_id, ...

  appointment_id → billing_id, patient_id, invoice_number, ...

  {consultation_fee, medication_fee, lab_test_fee, other_charges} → total_amount
                                                         ← (Dẫn xuất)

  {total_amount, discount} → final_amount                ← (Dẫn xuất)
}
```

#### Giải thích:
* **`billing_id → tất cả`**: Phụ thuộc hàm chính.
* **`invoice_number → tất cả`**: Số hóa đơn là UNIQUE → cũng xác định duy nhất.
* **`appointment_id → tất cả`**: Mỗi lịch hẹn hoàn thành chỉ có 1 hóa đơn.
* **Hai phụ thuộc hàm dẫn xuất**: `total_amount` và `final_amount` tính được từ các khoản phí → không cần lưu trong bảng vật lý (hoặc lưu có chủ đích để tối ưu báo cáo).

> **Nhận xét:** Không có phụ thuộc bộ phận hay bắc cầu. → **Đạt chuẩn 3NF** ✅

---

### Entity 13: NOTIFICATION (Thông báo)

**Khóa chính (PK):** `notification_id`

#### Các phụ thuộc hàm:

```
F = {
  notification_id → recipient_type, recipient_id, title, message,
                    notification_type, channel, is_read, sent_at
}
```

#### Giải thích:
* **`notification_id → tất cả`**: Biết ID thông báo → xác định duy nhất nội dung, người nhận, trạng thái đã đọc...

> **Nhận xét:** Bảng đơn giản, không có phụ thuộc bộ phận hay bắc cầu. → **Đạt chuẩn 3NF** ✅

---

### Entity 14: SYSTEM_LOG (Nhật ký hệ thống)

**Khóa chính (PK):** `log_id`

#### Các phụ thuộc hàm:

```
F = {
  log_id → user_type, user_id, action, entity_type, entity_id,
           description, ip_address, timestamp
}
```

#### Giải thích:
* **`log_id → tất cả`**: Biết ID dòng log → xác định duy nhất ai làm gì, lúc nào, ở đâu.

> **Nhận xét:** Bảng đơn giản nhất trong hệ thống, không có phụ thuộc bộ phận hay bắc cầu. → **Đạt chuẩn 3NF** ✅

---

## III. Bảng tổng kết Dạng chuẩn của toàn hệ thống

| Entity | PK | Khóa ứng viên | Phụ thuộc bộ phận | Phụ thuộc bắc cầu | Dạng chuẩn |
|---|---|---|:---:|---|:---:|
| **EMPLOYEE** | `employee_id` | `employee_code`, `email` | Không | Không | **3NF** ✅ |
| **DOCTOR** | `employee_id` | `license_number` | Không | Không | **3NF** ✅ |
| **PATIENT** | `patient_id` | `medical_record_number`, `email` | Không | Không (sau khi loại bỏ thuộc tính dẫn xuất `age`) | **3NF** ✅ |
| **DEPARTMENT** | `department_id` | `department_name` | Không | Không | **3NF** ✅ |
| **APPOINTMENT** | `appointment_id` | `{doctor_id, date, start_time}` | Không | Không | **3NF** ✅ |
| **DOCTOR_SCHEDULE** | `schedule_id` | `{doctor_id, day, start_time}` | Không | Không | **3NF** ✅ |
| **MEDICAL_RECORD** | `record_id` | — | Không | Không | **3NF** ✅ |
| **CONSULTATION** | `consultation_id` | `appointment_id` | Không | `patient_id`, `doctor_id` (denorm.) | **3NF** ✅ * |
| **DIAGNOSIS** | `diagnosis_id` | — | Không | `icd_code → diagnosis_name` (tiềm ẩn) | **2NF~3NF** ⚠️ |
| **PRESCRIPTION** | `prescription_id` | — | Không | `patient_id`, `doctor_id` (denorm.) | **3NF** ✅ * |
| **PRESCRIPTION_DETAIL** | `detail_id` | — | Không | Không | **3NF** ✅ |
| **BILLING** | `billing_id` | `invoice_number`, `appointment_id` | Không | Không | **3NF** ✅ |
| **NOTIFICATION** | `notification_id` | — | Không | Không | **3NF** ✅ |
| **SYSTEM_LOG** | `log_id` | — | Không | Không | **3NF** ✅ |

> **Ghi chú:**
> * ✅ * = Đạt 3NF nhưng có denormalization có chủ đích (lưu thêm `patient_id`, `doctor_id` để tối ưu truy vấn).
> * ⚠️ = Có phụ thuộc bắc cầu tiềm ẩn (`icd_code → diagnosis_name`) nhưng chấp nhận được vì lý do nghiệp vụ y tế.

---

## IV. Tóm tắt các Phụ thuộc hàm đặc biệt đáng chú ý

### 1. Phụ thuộc hàm dẫn xuất (Derived FDs) — Không lưu trong bảng vật lý:
| Phụ thuộc hàm | Entity | Ý nghĩa |
|---|---|---|
| `date_of_birth → age` | PATIENT | Tuổi tính từ ngày sinh |
| `{start_time, end_time} → duration` | APPOINTMENT | Thời lượng khám |
| `{height, weight} → BMI` | MEDICAL_RECORD | Chỉ số thể trọng |
| `{consultation_fee, medication_fee, lab_test_fee, other_charges} → total_amount` | BILLING | Tổng tiền |
| `{total_amount, discount} → final_amount` | BILLING | Thành tiền cuối cùng |

### 2. Phụ thuộc hàm từ ràng buộc nghiệp vụ (Business Rule FDs):
| Phụ thuộc hàm | Entity | Business Rule |
|---|---|---|
| `{doctor_id, appointment_date, start_time} → appointment_id` | APPOINTMENT | *"Appointments must not overlap for the same doctor"* |
| `{doctor_id, day_of_week, start_time} → schedule_id` | DOCTOR_SCHEDULE | Mỗi bác sĩ, mỗi thứ, mỗi giờ chỉ có 1 ca trực |

### 3. Phụ thuộc hàm bắc cầu (Transitive FDs) — Cần lưu ý:
| Chuỗi bắc cầu | Entity | Lý do chấp nhận |
|---|---|---|
| `diagnosis_id → icd_code → diagnosis_name` | DIAGNOSIS | Bác sĩ gõ tên bệnh tự do, có thể khác tên chuẩn ICD |
| `prescription_id → consultation_id → patient_id` | PRESCRIPTION | Denormalization để Patient truy vấn trực tiếp |
| `prescription_id → consultation_id → doctor_id` | PRESCRIPTION | Denormalization để truy vấn nhanh |
| `consultation_id → appointment_id → patient_id` | CONSULTATION | Denormalization để truy vấn nhanh |
| `consultation_id → appointment_id → doctor_id` | CONSULTATION | Denormalization để truy vấn nhanh |
