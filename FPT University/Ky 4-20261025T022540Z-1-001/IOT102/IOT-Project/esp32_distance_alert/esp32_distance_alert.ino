/*
 * SMART DISTANCE ALERT SYSTEM - ESP32
 * 
 * SƠ ĐỒ NỐI DÂY:
 * 1. HC-SR04:
 *    - VCC -> 5V (hoặc 3.3V)
 *    - GND -> GND
 *    - Trig -> GPIO 5
 *    - Echo -> GPIO 18
 * 
 * 2. LED:
 *    - Cực dương (+) -> Điện trở 220 Ohm -> GPIO 2
 *    - Cực âm (-) -> GND
 * 
 * 3. Buzzer:
 *    - Chân (+) -> GPIO 4
 *    - Chân (-) -> GND
 */

#include <WiFi.h>
#include <Firebase_ESP_Client.h>

// --- CẤU HÌNH WI-FI & FIREBASE ---
#define WIFI_SSID "TEN_WIFI_CUA_BAN"
#define WIFI_PASSWORD "MAT_KHAU_WIFI"

#define API_KEY "API_KEY_FIREBASE_CUA_BAN"
#define DATABASE_URL "URL_DATABASE_FIREBASE" 

// --- ĐỊNH NGHĨA CHÂN PHẦN CỨNG ---
#define TRIG_PIN 5
#define ECHO_PIN 18
#define LED_PIN 2
#define BUZZER_PIN 4

// --- KHỞI TẠO ĐỐI TƯỢNG ---
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

unsigned long sendDataPrevMillis = 0;
int distance = 0;

void setup() {
  Serial.begin(115200);
  
  // Cấu hình chân I/O
  pinMode(TRIG_PIN, OUTPUT);
  pinMode(ECHO_PIN, INPUT);
  pinMode(LED_PIN, OUTPUT);
  pinMode(BUZZER_PIN, OUTPUT);

  // Kết nối Wi-Fi
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Dang ket noi Wi-Fi");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Da ket noi, IP: ");
  Serial.println(WiFi.localIP());

  // Cấu hình Firebase
  config.api_key = API_KEY;
  config.database_url = DATABASE_URL;

  Firebase.reconnectWiFi(true);
  Firebase.begin(&config, &auth);
}

void loop() {
  // 1. Đo khoảng cách bằng HC-SR04
  digitalWrite(TRIG_PIN, LOW);
  delayMicroseconds(2);
  digitalWrite(TRIG_PIN, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG_PIN, LOW);
  
  long duration = pulseIn(ECHO_PIN, HIGH);
  distance = duration * 0.034 / 2; // Tính ra cm

  Serial.print("Khoang cach: ");
  Serial.print(distance);
  Serial.println(" cm");

  // 2. Xử lý cảnh báo (LED và Buzzer)
  if (distance < 10) {
    // Vùng nguy hiểm: LED sáng liên tục, Buzzer kêu liên tục
    digitalWrite(LED_PIN, HIGH);
    tone(BUZZER_PIN, 1000); 
  } else if (distance < 30) {
    // Vùng cẩn thận: LED nhấp nháy, Buzzer im lặng
    digitalWrite(LED_PIN, HIGH);
    noTone(BUZZER_PIN);
    delay(100);
    digitalWrite(LED_PIN, LOW);
    delay(100);
  } else {
    // Vùng an toàn: Tắt hết
    digitalWrite(LED_PIN, LOW);
    noTone(BUZZER_PIN);
  }

  // 3. Gửi dữ liệu lên Firebase mỗi 5 giây
  if (Firebase.ready() && (millis() - sendDataPrevMillis > 5000 || sendDataPrevMillis == 0)) {
    sendDataPrevMillis = millis();
    
    if (Firebase.setInt(fbdo, "sensor/distance", distance)) {
      Serial.println("Da gui du lieu thanh cong");
    } else {
      Serial.print("Loi gui du lieu: ");
      Serial.println(fbdo.errorReason());
    }
  }

  delay(100);
}