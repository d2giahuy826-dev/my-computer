# Kịch bản thuyết trình – Smart Reverse Parking System (IOT102, Nhóm 3)

Deck: `Smart_Reverse_Parking_System_IoT_Group3_updated.pptx` (17 slide)

Cách dùng: đọc thành lời, không cần học thuộc từng chữ. Phần trong **[ngoặc vuông]** là hành động trên màn hình.

---

# NGƯỜI 1 – Slide 1 đến 4

## Slide 1 – Tiêu đề

Xin chào thầy/cô và các bạn. Nhóm 3 xin trình bày đề tài **Smart Reverse Parking System**, tức là hệ thống hỗ trợ lùi xe an toàn, thuộc môn IOT102.

Hệ thống của nhóm dùng **Arduino Uno** với **cảm biến siêu âm HC-SR04** để đo khoảng cách, kết hợp **camera điện thoại** truyền hình ảnh qua WebRTC. Dữ liệu được lưu vào **SQL Server** và đồng bộ lên **Firebase**.

Bài thuyết trình chia làm 4 phần cho 4 thành viên. Em là Người 1, sẽ giới thiệu ý tưởng, nguyên lý đo và sơ đồ mạch.

## Slide 2 – Ý tưởng: cảnh báo khoảng cách khi lùi xe

Em đi vào ba ý: vấn đề, giải pháp và phần mở rộng.

**Vấn đề:** khi lùi xe, người lái rất khó quan sát điểm mù phía sau, nên rất dễ va chạm với vật cản.

**Giải pháp:** nhóm dùng sóng siêu âm để đo khoảng cách tới vật cản, rồi cảnh báo theo **3 mức** bằng đèn LED, còi buzzer và màn hình LCD.

**Phần mở rộng:** nhóm làm thêm một màn hình lùi trên laptop, hiển thị hình ảnh từ camera điện thoại. Lịch sử đo được lưu vào SQL Server, đồng bộ lên Firebase và xem được trên một dashboard chạy 24/7 từ xa.

## Slide 3 – HC-SR04 đo khoảng cách như thế nào

Cảm biến HC-SR04 đo theo **4 bước**.

Bước 1, **Trigger**: Arduino đặt chân TRIG, là chân số 9, lên mức HIGH trong 10 micro giây.

Bước 2, **phát sóng**: cảm biến phát một chùm sóng siêu âm về phía vật cản.

Bước 3, **nhận ECHO**: sóng phản xạ quay lại, chân ECHO, là chân số 10, giữ mức HIGH trong khoảng thời gian t.

Bước 4, **tính d**: Arduino đo t bằng hàm `pulseIn()` rồi đổi ra khoảng cách.

**[Chỉ vào công thức]** Công thức là: d tính bằng cm bằng t tính bằng micro giây nhân 0.034 rồi chia 2. Số 0.034 là tốc độ âm thanh tính theo cm trên micro giây. Phải chia 2 vì sóng đi tới vật cản rồi dội lại, tức là đi quãng đường gấp đôi khoảng cách thật.

## Slide 4 – Sơ đồ mạch

**[Chiếu mạch Tinkercad tổng hợp]**

Đây là sơ đồ mạch nhóm dựng trên Tinkercad, gồm Arduino Uno, cảm biến HC-SR04, 3 đèn LED và một còi buzzer.

Cách nối chân như sau: HC-SR04 có **TRIG nối chân 9**, **ECHO nối chân 10**. Ba đèn LED xanh, vàng, đỏ nối các chân **2, 3, 4**. Còi buzzer nối chân **8**.

Sơ đồ này là mạch tổng hợp. Phần giải thích chi tiết về logic cảnh báo sẽ do bạn Người 2 trình bày tiếp.

**Chuyển:** Cảm biến đo được khoảng cách rồi, vậy Arduino quyết định cảnh báo thế nào? Xin mời Người 2.

---

# NGƯỜI 2 – Slide 5 đến 7

## Slide 5 – Logic cảnh báo theo 3 vùng khoảng cách

Em là Người 2, em trình bày cách Arduino quyết định cảnh báo. **[Chỉ vào bảng]**

Hệ thống chia khoảng cách thành 3 vùng và một trường hợp ngoài tầm.

- **Vùng An toàn**, từ 100 đến 200 cm: đèn **xanh** (chân 2) sáng liên tục, còi tắt.
- **Vùng Cảnh báo**, từ 50 đến 100 cm: đèn **vàng** (chân 3) sáng liên tục, còi vẫn tắt.
- **Vùng Nguy hiểm**, từ 0 đến 50 cm: đèn **đỏ** (chân 4) **nháy mỗi 70 mili giây**, còi kêu ở tần số **1500 Hz** theo đúng nhịp nháy của đèn.
- **Ngoài tầm**, trên 200 cm: cả 3 đèn đều tắt, còi tắt.

Ngoài ra, màn hình **LCD 16x2 giao tiếp I2C** hiển thị khoảng cách và trạng thái, cập nhật mỗi 100 mili giây.

**[Nếu có thể, demo trên Tinkercad: kéo cảm biến ở vài khoảng cách, ví dụ 150 cm, 80 cm, 30 cm để đèn, còi và LCD đổi theo]**

## Slide 6 – Code Arduino: đọc, quyết định, xuất ra

Chương trình Arduino gồm 3 phần chính.

Thứ nhất, hàm **`setup()`**: khai báo các chân TRIG, ECHO, 3 LED và buzzer, mở Serial ở tốc độ 9600, khởi tạo LCD và in tên nhóm.

Thứ hai, hàm **`getDistance()`**: gửi xung TRIG rồi đọc ECHO bằng `pulseIn()` với thời gian chờ tối đa 20 mili giây. Nếu không có tiếng vọng thì hàm trả về 999, nghĩa là ngoài tầm.

Thứ ba, hàm **`loop()`**: đọc khoảng cách d, chọn vùng bằng cấu trúc `if / else if`, bật tắt LED và còi tương ứng, rồi cập nhật LCD mỗi 100 mili giây.

**[Chỉ vào ô ghi chú]** Điểm quan trọng là nhóm dùng **`millis()` thay cho `delay()`** để nháy đèn đỏ và làm mới LCD. Nhờ vậy việc đọc cảm biến không bao giờ bị chặn.

## Slide 7 – Code chi tiết: khoảng cách và vùng nguy hiểm

**[Chỉ vào đoạn code trên]** Đây là hàm `getDistance()`. Arduino kéo TRIG lên HIGH 10 micro giây rồi hạ xuống. Sau đó `pulseIn()` đo thời gian ECHO ở mức HIGH, tối đa 20 mili giây. Nếu `duration` bằng 0 nghĩa là không nhận được tiếng vọng nên trả về 999. Còn nếu có thì đổi micro giây ra cm theo công thức nhân 0.034 chia 2.

**[Chỉ vào đoạn code dưới]** Đây là nhánh xử lý vùng nguy hiểm, từ 0 đến 50 cm. Mỗi lần `millis()` trừ thời điểm nháy trước đạt 70 mili giây thì đảo trạng thái đèn đỏ. Khi đèn sáng thì còi phát tiếng ở 1500 Hz, khi đèn tắt thì còi tắt. Toàn bộ không dùng `delay()`.

**Chuyển:** Arduino cảnh báo tại chỗ xong rồi, vậy dữ liệu đo được còn đi đâu nữa? Xin mời Người 3.

---

# NGƯỜI 3 – Slide 8 đến 11

## Slide 8 – Kiến trúc phần mềm và backend

Em là Người 3, em trình bày phần phần mềm phía sau. Trước hết là đường đi của dữ liệu:

**vật cản → cảm biến HC-SR04 → Arduino → trang web trên laptop → server Node.js → SQL Server và Firebase.**

Dữ liệu mỗi bản ghi gồm ba thông tin: **khoảng cách (distance), mức cảnh báo (level) và thời gian (timestamp)**.

Kiến trúc có 3 khối:

- **Màn hình lùi** (`monitor.html` trên laptop): đọc Arduino qua **Web Serial**, hiển thị số cm, vẽ vạch hướng dẫn lùi xe bằng **Canvas**, phát tiếng bíp bằng **Web Audio**, lấy hình từ Iriun Webcam hoặc WebRTC.
- **Server Node.js**: dùng Express chạy HTTPS ở cổng **3443** với chứng chỉ tự ký sinh tự động, có WebSocket cho WebRTC, có mã QR mở trang camera và API `/api/logs`.
- **Lưu trữ**: bảng **ParkingLogs** trong SQL Server, có **SQLite dự phòng**, và đồng bộ **Firebase**. Chỉ ghi khi đổi mức cảnh báo hoặc lệch 5 cm, giữ 500 bản ghi mới nhất.

Các thư viện chính: express, ws, selfsigned, qrcode, mssql.

## Slide 9 – WebRTC signaling qua WebSocket

Camera điện thoại và màn hình laptop cần "làm quen" với nhau trước khi truyền video. Việc này gọi là **signaling**, và server của nhóm làm qua WebSocket.

**[Chỉ vào code]** Có 4 loại tin nhắn:

- `register`: camera hoặc monitor đăng ký vai trò của mình.
- `offer`: camera gửi lời mời kết nối tới màn hình lùi.
- `answer`: màn hình lùi trả lời lại cho camera.
- `candidate`: chuyển tiếp thông tin đường truyền ICE sang phía bên kia.

**[Chỉ vào ô ghi chú]** Cần nhấn mạnh: server **chỉ chuyển tin nhắn**. Luồng video đi **trực tiếp giữa điện thoại và laptop**, không đi qua server.

## Slide 10 – Bộ lọc spam và giới hạn 500 bản ghi

Cảm biến đo liên tục, nếu ghi hết thì database sẽ đầy rất nhanh. Vì vậy nhóm có bộ lọc ở server.

**[Chỉ vào code trên]** Có hai quy tắc. Một: nếu **mức cảnh báo không đổi và khoảng cách lệch dưới 5 cm** thì xem như xe đứng yên, **không ghi**. Hai: nếu mức cảnh báo đổi nhưng chưa tới 2 giây kể từ lần ghi trước thì bỏ qua, vì đó là cảm biến chập chờn ở ranh giới giữa hai vùng.

**[Chỉ vào code dưới]** Khi ghi vào SQL Server, ngay sau lệnh `INSERT` là lệnh `DELETE` để chỉ giữ lại **500 bản ghi mới nhất**. Nhờ vậy database luôn nhỏ gọn.

Bộ lọc này chạy trước khi ghi vào cả SQL Server, SQLite và Firebase.

## Slide 11 – Đồng bộ Firebase Realtime Database

Phần cuối là đồng bộ lên đám mây. Nhóm dùng **Firebase Realtime Database** và hàm `fetch` có sẵn của Node.js, nên không cần cài thêm thư viện.

**[Chỉ vào code]** Có hai đường dẫn:

- `/live.json` dùng phương thức **PUT**: mỗi lần có dữ liệu mới thì ghi đè, nên luôn chứa **trạng thái mới nhất**.
- `/logs.json` dùng phương thức **POST**: mỗi sự kiện thêm một bản ghi mới, nên lưu **lịch sử**.

Mỗi bản ghi gồm khoảng cách, mức cảnh báo, chữ trạng thái và thời gian.

**Chuyển:** Vậy người dùng thực sự nhìn thấy và thao tác ra sao? Xin mời Người 4.

---

# NGƯỜI 4 – Slide 12 đến 17

## Slide 12 – Tính năng hệ thống

Em là Người 4, em trình bày các tính năng và phần demo. Hệ thống có 6 nhóm tính năng.

- **Camera lùi**: dùng Iriun Webcam (tự nhận diện) hoặc quét mã QR để phát video từ điện thoại qua WebRTC.
- **Đọc cảm biến**: kết nối Arduino qua cổng USB bằng Web Serial và hiển thị khoảng cách theo thời gian thực.
- **Hiển thị**: số cm lớn, thanh màu và các vạch hướng dẫn phối cảnh 3D vẽ bằng Canvas.
- **Âm thanh**: còi mô phỏng bằng Web Audio, nhịp bíp đổi theo khoảng cách.
- **Tiện ích**: nút lật gương, và Wake Lock giữ màn hình điện thoại luôn sáng.
- **Lưu trữ**: lưu lịch sử vào SQL Server hoặc SQLite, đồng bộ Firebase và xem trên Cloud dashboard.

## Slide 13 – Ba trang web, một hệ thống

Hệ thống có ba trang web, mỗi trang một vai trò.

**[Chỉ từng ảnh]**

- **Màn hình lùi** trên laptop: hiển thị hình camera, số cm trực tiếp, vạch hướng dẫn, nút lịch sử SQL, Cloud và âm thanh.
- **Trang camera điện thoại**: chọn độ phân giải rồi bấm bật camera sau, màn hình điện thoại luôn sáng.
- **Cloud IoT dashboard**: xem khoảng cách trực tiếp và lịch sử từ bất kỳ đâu qua Internet.

## Slide 14 – Màn hình lùi: thanh điều khiển

**[Chỉ vào thanh công cụ phía dưới ảnh]** Thanh điều khiển có các nút sau:

- **Nguồn camera**: chọn Iriun Webcam hoặc camera khác, và có hiển thị FPS.
- **Baud rate**: chọn tốc độ cổng nối tiếp, ở đây là 115200, trước khi kết nối.
- **Kết nối Arduino**: dùng Web Serial, có dòng RAW hiển thị dữ liệu thô và nhãn trạng thái.
- **Lịch sử SQL**: mở lịch sử đã lưu. Nút **Cloud 24/7** mở dashboard.
- **Âm thanh, Lật gương, Toàn màn hình**.

Các vạch hướng dẫn trên hình được vẽ ở **30 cm (màu vàng)** và **15 cm (màu đỏ)**. Biểu tượng chiếc xe ở góc trên bên trái là cảm biến phía sau.

## Slide 15 – Cloud IoT dashboard 24/7

Dashboard này cho phép giám sát từ xa.

- **Trạng thái trực tiếp**: đọc `/live.json`, được ghi đè mỗi khi có số liệu hợp lệ.
- **Lịch sử**: đọc `/logs.json`, mỗi sự kiện là một bản ghi.
- **Cấu hình**: bấm nút Cấu hình, dán URL Firebase Realtime Database vào. URL được lưu trong file `firebase-config.json`.
- **Truy cập từ xa**: trang được đưa lên Vercel nên ai có đường dẫn đều xem được.

**[Chỉ vào ô ghi chú]** Cách làm này khớp với đề xuất ban đầu của nhóm: cảm biến, Arduino, Firebase rồi đến giao diện web.

## Slide 16 – API lịch sử và cài đặt database

Phía server có 3 API cho lịch sử:

- **`GET /api/logs`**: trả về 50 bản ghi gần nhất và cho biết nguồn dữ liệu là SQL Server hay SQLite dự phòng.
- **`POST /api/logs`**: nhận distance và level, rồi bộ lọc spam quyết định có lưu và đồng bộ hay không.
- **`DELETE /api/logs`**: xóa toàn bộ lịch sử trong SQL Server và SQLite.

Về database: file **`setup_database.sql`** tạo cơ sở dữ liệu **CarReverseDB** và bảng **ParkingLogs** gồm các cột Id, CreatedAt, Distance, Level, StatusText. File **`Lenh.sql`** dùng để xem 20 dòng mới nhất. Thêm API `/api/firebase-config` để đọc và lưu URL Firebase.

## Slide 17 – Cách chạy và demo

**[Bắt đầu demo, làm theo đúng 3 bước]**

Bước 1, **chạy server**: nhấp đúp `start.bat` hoặc chạy `npm start`. Server HTTPS cổng 3443 tự sinh chứng chỉ.

Bước 2, **chọn camera**: mở `monitor.html` và chọn Iriun Webcam, hoặc quét mã QR bằng điện thoại để phát video qua WebRTC.

Bước 3, **kết nối Arduino**: bấm "Kết nối Arduino", chọn cổng COM. Số cm, vạch hướng dẫn và tiếng bíp sẽ cập nhật theo thời gian thực. **[Đưa vật cản lại gần cảm biến để cho thấy vạch và tiếng bíp đổi]**

**[Mở Cloud 24/7 để cho thấy số liệu hiện bên dashboard, nếu đã cấu hình Firebase]**

Lưu ý nhỏ: cần **đóng Serial Monitor của Arduino IDE** trước khi kết nối, nếu không cổng COM sẽ bị khóa.

## Kết

Tóm lại, hệ thống của nhóm đo khoảng cách bằng cảm biến siêu âm, cảnh báo 3 mức tại chỗ, hiển thị hình ảnh và vạch lùi trên laptop, lưu lịch sử vào SQL Server và giám sát từ xa qua Firebase. Phần trình bày của nhóm đến đây là hết. Xin cảm ơn thầy/cô và các bạn đã lắng nghe, nhóm sẵn sàng trả lời câu hỏi.

---

# Chuẩn bị trả lời câu hỏi (gợi ý)

| Câu hỏi có thể gặp | Ai trả lời | Ý chính |
|---|---|---|
| Vì sao chia 2 trong công thức? | Người 1 | Sóng đi và về, nên quãng đường gấp đôi khoảng cách thật. |
| Vì sao dùng `millis()` thay `delay()`? | Người 2 | `delay()` chặn chương trình, làm trễ việc đọc cảm biến; `millis()` cho phép làm nhiều việc cùng lúc. |
| Vì sao vùng ở slide 5 (100/50 cm) khác vạch ở slide 14 (30/15 cm)? | Người 2 hoặc 4 | Vùng 100/50 là ngưỡng trong code Arduino; vạch 30/15 là vạch hướng dẫn vẽ trên màn hình web. Hai phần độc lập, có thể chỉnh cho thống nhất. |
| Video có đi qua server không? | Người 3 | Không, server chỉ làm signaling; video đi trực tiếp giữa hai thiết bị. |
| Làm sao tránh ghi dữ liệu trùng? | Người 3 | Bộ lọc: cùng mức và lệch dưới 5 cm thì bỏ; đổi mức trong dưới 2 giây thì bỏ; giữ 500 bản ghi. |
| Mất kết nối SQL Server thì sao? | Người 3 | Tự động ghi vào SQLite dự phòng. |
| Mất Internet thì sao? | Người 4 | Hệ thống tại chỗ (Arduino, màn hình lùi, SQL cục bộ) vẫn chạy; chỉ phần Firebase và dashboard từ xa bị gián đoạn. |
