erDiagram
    RoomType {
        PK_ room_type_id
        _ type_name
        _ description
        _ hourly_price
        _ overnight_price
    }

    Customer {
        PK_ customer_id
        _ full_name
        _ phone
        _ email
        _ identity_number
        _ created_at
    }

    Capsule {
        PK_ capsule_id
        _ capsule_code
        FK_ room_type_id
        _ floor
        _ zone
        _ status
    }

    Staff {
        PK_ staff_id
        _ full_name
        _ phone
        _ email
        _ role
        _ status
    }

    Booking {
        PK_ booking_id
        FK_ customer_id
        FK_ capsule_id
        FK_ staff_id
        _ start_time
        _ end_time
        _ actual_checkin_time
        _ actual_checkout_time
        _ booking_type
        _ booking_status
        _ total_amount
        _ created_at
    }

    Payment {
        PK_ payment_id
        FK_ booking_id
        _ amount
        _ payment_method
        _ payment_status
        _ paid_at
        _ transaction_code
    }

    CleaningLog {
        PK_ cleaning_id
        FK_ capsule_id
        FK_ staff_id
        _ start_time
        _ end_time
        _ cleaning_status
        _ note
    }

    MaintenanceLog {
        PK_ maintenance_id
        FK_ capsule_id
        FK_ staff_id
        _ issue_description
        _ start_time
        _ end_time
        _ maintenance_status
        _ cost
    }

    IoTDevice {
        PK_ device_id
        FK_ capsule_id
        _ device_code
        _ device_type
        _ status
        _ installed_at
        _ last_check_time
    }

    %% Relationships
    RoomType ||--o{ Capsule : "has"
    Customer ||--o{ Booking : "makes"
    Capsule ||--o{ Booking : "is booked in"
    Staff ||--o{ Booking : "processes"
    Booking ||--o{ Payment : "has"
    Capsule ||--o{ CleaningLog : "undergoes"
    Staff ||--o{ CleaningLog : "performs"
    Capsule ||--o{ MaintenanceLog : "undergoes"
    Staff ||--o{ MaintenanceLog : "performs"
    Capsule ||--o{ IoTDevice : "equipped with"
