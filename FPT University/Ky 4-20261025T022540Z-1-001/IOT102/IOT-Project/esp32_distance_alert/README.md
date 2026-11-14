# Smart Distance Alert System - ESP32

## 1. Chuẩn bị phần cứng
- Board ESP32
- Cảm biến siêu âm HC-SR04
- LED (bất kỳ màu nào)
- Buzzer (chuông báo)
- Dây nối

## 2. Sơ đồ nối dây
| Linh kiện | Chân trên ESP32 |
|-----------|-----------------|
| HC-SR04 (Trig) | GPIO 5 |
| HC-SR04 (Echo) | GPIO 18 |
| LED (+) | GPIO 2 |
| Buzzer (+) | GPIO 4 |
| GND | GND chung |

*Lưu ý: Với HC-SR04, chân Echo nên dùng mạch phân áp nếu lo ngại về điện áp 5V, nhưng nhiều board ESP32 vẫn hoạt động ổn định ở mức này.*

## 3. Cấu hình Firebase
1. Truy cập [Firebase Console](https://console.firebase.google.com/).
2. Tạo Project mới.
3. Vào **Project Settings** > **Service accounts** > Chọn **Database secrets** (hoặc tạo Realtime Database).
4. Lấy **API Key** và **Database URL**.
5. Mở file `esp32_distance_alert.ino` và thay thế:
   - `TEN_WIFI_CUA_BAN`: Tên Wi-Fi nhà bạn.
   - `MAT_KHAU_WIFI`: Mật khẩu Wi-Fi.
   - `API_KEY_FIREBASE_CUA_BAN`: API Key đã lấy ở bước 4.
   - `URL_DATABASE_FIREBASE`: URL database (ví dụ: `https://ten-project.firebaseio.com/`).

## 4. Cài đặt thư viện
Trong Arduino IDE, vào **Tools** > **Manage Libraries**, tìm và cài đặt:
- `Firebase ESP32 Client` by Mobizt

## 5. Nạp code
Chọn đúng board ESP32 trong **Tools** > **Board** và cổng COM tương ứng, sau đó nhấn Upload.