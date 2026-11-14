# Kịch bản thuyết trình C1 kèm lệnh chạy tương ứng – Đề tài 2: Hash Table

**Phạm vi:** Separate Chaining (`sc`), generator khóa/truy vấn, runner/bộ đo, RQ1.
**Thời lượng:** 5 phút nói + 2 phút hỏi đáp.
**Cách đọc file:** mỗi slide có hai phần: **🎤 Kịch bản** (nội dung và lời nói) và **⌨️ Lệnh tương ứng** (lệnh PowerShell tạo ra hoặc kiểm tra số liệu của slide đó). `[điền]` là số phải lấy từ `results/raw/rq1_runner_results_lab.csv`. Không đọc số nào chưa kiểm với CSV lab.

---

## 0. Chuẩn bị chung (chạy một lần trước khi làm mọi slide)

```powershell
cd D:\csd1\Nhom1_CSD
$cp  = "HashTableProject\build\classes"
$f   = "results\raw\rq1_runner_results.csv"
$inv = [Globalization.CultureInfo]::InvariantCulture

# Biên dịch (sources.txt và tests.txt cũ ghi đường dẫn C:\tmp nên không dùng)
cd HashTableProject
Remove-Item -Recurse -Force build\classes, build\test\classes -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force build\classes, build\test\classes | Out-Null
javac -encoding UTF-8 -d build\classes (Get-ChildItem src -Recurse -Filter *.java).FullName
javac -encoding UTF-8 -cp build\classes -d build\test\classes (Get-ChildItem test -Recurse -Filter *.java).FullName
cd ..

# Sửa sources.txt và tests.txt thành đường dẫn tương đối (tùy chọn)
cd HashTableProject
(Get-ChildItem src  -Recurse -Filter *.java | % { Resolve-Path -Relative $_.FullName }) -replace '^\.\\','' -replace '\\','/' | Set-Content sources.txt -Encoding ASCII
(Get-ChildItem test -Recurse -Filter *.java | % { Resolve-Path -Relative $_.FullName }) -replace '^\.\\','' -replace '\\','/' | Set-Content tests.txt -Encoding ASCII
cd ..
```

---

## Slide 1 – Vai trò của Cặp 1 (khoảng 30 giây)

### 🎤 Kịch bản
**Nội dung slide:** 3 gạch đầu dòng: `sc` · Generator + Runner · RQ1.

**Lời nói:**
"Cặp 1 chịu trách nhiệm ba phần. Một là cài đặt Separate Chaining. Hai là generator sinh khóa và truy vấn có thể tái lập bằng seed. Ba là runner và bộ đo dùng chung cho cả ba cặp, đồng thời chủ trì câu hỏi nghiên cứu RQ1."

### ⌨️ Lệnh tương ứng
Kiểm tra repo biên dịch được với đủ các lớp của C1 (mục 0), rồi xác nhận runner chạy:
```powershell
java -cp $cp hashtable.bench.BenchmarkRunner --header
```
Kết quả đúng: in ra header 21 cột có `build_ms`.

---

## Slide 2 – Câu hỏi RQ1 và thiết kế thí nghiệm (khoảng 45 giây)

### 🎤 Kịch bản
**Nội dung slide:**
- Thí nghiệm A: α = 0,50; 0,75; 0,90; 0,95 tại M = 2²⁰.
- Thí nghiệm B: M = 2¹⁸ … 2²³ tại α = 0,75.
- Bộ dồn cụm: 10% khóa có h(k) < M/64, α = 0,75, M = 2¹⁸ và 2²⁰.
- So sánh `sc` và `lp-tomb`, không xóa, không mở rộng bảng.

**Lời nói:**
"RQ1 hỏi: Separate Chaining và Linear Probing khác nhau thế nào về số lần dò và thời gian tra cứu khi hệ số tải và kích thước bảng tăng. Chúng em đổi một yếu tố mỗi lần. Thí nghiệm A đổi α, thí nghiệm B đổi M. Hàm băm Fibonacci, bộ truy vấn và seed giữ cố định. Ngoài ra có một cấu hình dồn cụm để kiểm tra giới hạn của Linear Probing."

### ⌨️ Lệnh tương ứng
Chạy toàn bộ thiết kế trên (22 cấu hình × 4 lượt = 88 dòng). Luôn dùng `-Out` sang file mới, không ghi đè file lab đang có:
```powershell

Remove-Item results\raw\rq1_pilot.csv -ErrorAction SilentlyContinue
powershell -ExecutionPolicy Bypass -File .\bench\run_rq1.ps1 -Pilot

# Chạy thử M nhỏ trước (ghi vào results\raw\rq1_pilot.csv)
powershell -ExecutionPolicy Bypass -File .\bench\run_rq1.ps1 -Pilot

# Chạy thật
powershell -ExecutionPolicy Bypass -File .\bench\run_rq1.ps1 -Out results\raw\rq1_runner_results_lab_v3.csv -MachineId PC_LAB_01

# Chạy một cấu hình lẻ
java -Xmx2g -cp $cp hashtable.bench.BenchmarkRunner --rq RQ1 --algo lp-tomb --m 1048576 --alpha 0.95 --seed 42 --query miss --count --out results\raw\rq1_single.csv
```
Kiểm tra số dòng và cấu hình:
```powershell
@(Import-Csv $f).Count                                        # 88
Import-Csv $f | Group-Object algorithm,status | Select Name,Count   # sc,ok 44 và lp-tomb,ok 44
```
Lưu ý: 8 dòng thuộc α = 0,75, M = 2²⁰ bị lặp giữa thí nghiệm A và B.

---

## Slide 3 – Cài đặt và phương pháp đo (khoảng 50 giây)

### 🎤 Kịch bản
**Nội dung slide:**
- `sc`: `Node[M]`, `Node{long key; int value; Node next}`, chèn đầu danh sách; `put` phải duyệt danh sách để kiểm tra khóa trùng.
- Số lần dò: lượt `--count` riêng. Thời gian: lượt riêng, chia cho tổng số thao tác.
- `output_check` để chống JIT loại bỏ code và kiểm chéo giữa `sc` và `lp-tomb`.
- Mỗi lượt là một tiến trình JVM riêng, `-Xmx2g`, timeout 60 giây.

**Lời nói:**
"Chúng em tách hai lượt đo. Lượt đếm bước dò chạy riêng để bộ đếm không làm sai thời gian. Lượt đo thời gian đo cả lô rồi chia, không gọi nanoTime từng thao tác, và có warm-up 1000 lượt tra cứu trước khi bấm giờ. Mỗi dòng CSV có `output_check`, là tổng các giá trị tìm thấy cộng số lần không tìm thấy. Hai cài đặt cùng seed phải ra cùng giá trị này, đó là cách chúng em kiểm chéo tính đúng trong lượt đo thật."

### ⌨️ Lệnh tương ứng
**Smoke test runner** (lượt `--count` có `ops_count` mà không có `elapsed_ms`, lượt thời gian thì ngược lại):
```powershell
java -cp $cp hashtable.bench.BenchmarkRunner --rq RQ1 --algo sc --m 4096 --alpha 0.75 --count --csv-only
java -cp $cp hashtable.bench.BenchmarkRunner --rq RQ1 --algo sc --m 4096 --alpha 0.75 --csv-only
```
**Test generator** (cùng seed phải cùng `input_sha256`, đổi seed hoặc dataset thì SHA khác):
```powershell
1..2 | % { java -cp $cp hashtable.bench.BenchmarkRunner --rq RQ1 --algo sc --m 16384 --alpha 0.75 --seed 42 --count --csv-only }
java -cp $cp hashtable.bench.BenchmarkRunner --rq RQ1 --algo sc --m 16384 --alpha 0.75 --seed 43 --count --csv-only
java -cp $cp hashtable.bench.BenchmarkRunner --rq RQ1 --algo sc --m 16384 --alpha 0.75 --seed 42 --dataset clustered --count --csv-only
```
**Kiểm chéo `output_check` giữa `sc` và `lp-tomb`** (không in gì là khớp):
```powershell
$d = Import-Csv $f
$d | Group-Object { "$($_.dataset)|$($_.params)|$([bool]$_.ops_count)" } |
  Where-Object { @($_.Group.output_check | Select-Object -Unique).Count -gt 1 } |
  Select Name
```

---

## Slide 4 – Số lần dò so với lý thuyết (khoảng 60 giây)

### 🎤 Kịch bản
**Nội dung slide:** bảng lý thuyết và số đo, theo α = 0,50 / 0,75 / 0,90 / 0,95.

| | LP có mặt | LP vắng mặt | SC có mặt | SC vắng mặt |
|---|---|---|---|---|
| Lý thuyết α = 0,50 | 1,5 | 2,5 | 1,25 | 0,50 |
| Lý thuyết α = 0,75 | 2,5 | 8,5 | 1,375 | 0,75 |
| Lý thuyết α = 0,90 | 5,5 | 50,5 | 1,45 | 0,90 |
| Lý thuyết α = 0,95 | 10,5 | 200,5 | 1,475 | 0,95 |
| **Số đo (điền từ CSV lab)** | `[điền]` | `[điền]` | `[điền]` | `[điền]` |

**Lời nói:**
"Đây là so sánh giữa công thức của Knuth và số đo thực tế. Với Separate Chaining, số đo bám sát lý thuyết, vì số node phải duyệt phụ thuộc chủ yếu vào α. Với Linear Probing, số lần dò khóa vắng mặt tăng rất nhanh khi α tiến gần 1, từ khoảng 2,5 ở α = 0,5 lên khoảng 200 ở α = 0,95 theo lý thuyết. Độ lệch nhỏ giữa số đo và lý thuyết đến từ việc hàm băm Fibonacci không hoàn toàn đều, và đó là một điểm chúng em nêu trong Threats to validity."

*Ghi chú cho người nói:* bảng so sánh chạy trên file lab cho `sc` vắng mặt khoảng 0,749 / 0,900 / 0,950, rất sát lý thuyết. Mỗi α có nhiều dòng theo M, nên khi báo cáo phải chọn đúng dòng M = 2²⁰. Chưa có số `lp-tomb`, phải đọc phần `lp-tomb` của lệnh bên dưới.

### ⌨️ Lệnh tương ứng
Số lần dò đo được so với lý thuyết, chỉ lấy thí nghiệm A (M = 2²⁰):
```powershell
$d = Import-Csv $f
$d | Where-Object { $_.ops_count -and $_.dataset -eq 'uniform' -and $_.params -match 'M=1048576' } | ForEach-Object {
  $a = [double]::Parse(([regex]::Match($_.params,'alpha=([\d.]+)').Groups[1].Value), $inv)
  $miss = $_.params -match 'query=miss'
  if ($_.algorithm -eq 'sc') { $t = if ($miss) { $a } else { 1 + $a/2 } }
  else { $t = if ($miss) { 0.5*(1+1/[math]::Pow(1-$a,2)) } else { 0.5*(1+1/(1-$a)) } }
  [pscustomobject]@{ algo=$_.algorithm; alpha=$a; query=$(if($miss){'miss'}else{'hit'});
    do=[double]::Parse($_.ops_count,$inv); ly_thuyet=[math]::Round($t,3) }
} | Sort-Object algo,query,alpha | Format-Table -AutoSize
```

---

## Slide 5 – Thời gian và chi phí cấp phát Node (khoảng 75 giây, phần bắt buộc của đề)

### 🎤 Kịch bản
**Nội dung slide:** hai cột so sánh:
- `sc`: mỗi khóa là một đối tượng `Node` trên heap (header, key, value, con trỏ `next`), phân tán, mỗi bước dò là một lần nhảy con trỏ.
- `lp-tomb`: ba mảng nguyên thủy liên tiếp (`long[]`, `int[]`, `byte[]`), dò tuần tự theo dòng cache.
- Bảng số đo: `ns/thao tác` (có mặt, vắng mặt, dựng bảng) và `heap_peak − heap_before` của hai cài đặt `[điền từ CSV lab]`.

**Lời nói:**
"Điều quan trọng là không được kết luận chỉ từ số lần dò. Separate Chaining ít lần dò hơn, nhưng mỗi lần dò đắt hơn. Mỗi Node là một đối tượng riêng trong Java, có header, khóa, giá trị và con trỏ, nên vừa tốn bộ nhớ vừa nằm rải rác trên heap. Đi theo con trỏ thường gây cache miss, còn dựng bảng thì phải cấp phát hàng trăm nghìn đối tượng, tạo áp lực lên bộ gom rác. Linear Probing dùng mảng nguyên thủy liên tiếp nên dò tuần tự rất thân thiện với cache, dù số bước dò nhiều hơn. Vì vậy ở α thấp Linear Probing thường nhanh hơn dù dò nhiều hơn, còn ở α cao, số bước dò tăng vọt làm lợi thế này mất dần."

*Ghi chú:* câu cuối chỉ nói nếu số đo xác nhận. Nếu số đo khác, nói theo số đo và giải thích bằng cùng các cơ chế trên.

### ⌨️ Lệnh tương ứng
Tính ns/thao tác, thời gian dựng bảng và bộ nhớ phụ từ lượt đo thời gian (runner dùng n truy vấn nên chia cho `n`):
```powershell
Import-Csv $f | Where-Object { $_.elapsed_ms -and $_.dataset -eq 'uniform' -and $_.params -match 'M=1048576' } | ForEach-Object {
  $q = if ($_.params -match 'query=miss') { 'miss' } else { 'hit' }
  $n = [double]$_.n
  [pscustomobject]@{
    algo = $_.algorithm; params = $_.params -replace ';query=miss','';
    query = $q;
    ns_per_op = [math]::Round([double]::Parse($_.elapsed_ms,$inv) * 1e6 / $n, 1);
    build_ns_per_key = [math]::Round([double]::Parse($_.build_ms,$inv) * 1e6 / $n, 1);
    heap_extra_MB = [math]::Round(([double]$_.heap_peak_bytes - [double]$_.heap_before_bytes) / 1MB, 1)
  }
} | Sort-Object params, algo, query | Format-Table -AutoSize
```

---

## Slide 6 – Ảnh hưởng của M và bộ dồn cụm (khoảng 45 giây)

### 🎤 Kịch bản
**Nội dung slide:**
- Biểu đồ thí nghiệm B: ns/thao tác theo M (2¹⁸ … 2²³).
- Bảng bộ dồn cụm: số lần dò `lp-tomb` và `sc` ở α = 0,75, M = 2¹⁸ và 2²⁰ `[điền]`.

**Lời nói:**
"Khi M tăng và α giữ nguyên, số lần dò gần như không đổi, nhưng thời gian mỗi thao tác tăng, vì bảng lớn hơn bộ nhớ đệm nên cache miss nhiều hơn. Ở bộ dồn cụm, 10% khóa dồn vào 1/64 đầu bảng. Linear Probing suy giảm mạnh hơn Separate Chaining, vì các khóa đụng độ nối thành cụm dài và lan sang các ô lân cận, còn Separate Chaining chỉ làm dài vài danh sách."

*Ghi chú:* chỉ khẳng định xu hướng theo M nếu số đo từ lệnh bên dưới cho thấy đúng như vậy.

### ⌨️ Lệnh tương ứng
**Thí nghiệm B** (α = 0,75, M thay đổi): số lần dò và ns/thao tác theo M:
```powershell
# Số lần dò theo M
Import-Csv $f | Where-Object { $_.ops_count -and $_.dataset -eq 'uniform' -and $_.params -match 'alpha=0.75' } |
  Select algorithm, params, ops_count | Sort-Object params, algorithm | Format-Table -AutoSize

# ns/thao tác theo M
Import-Csv $f | Where-Object { $_.elapsed_ms -and $_.dataset -eq 'uniform' -and $_.params -match 'alpha=0.75' } | ForEach-Object {
  [pscustomobject]@{ algo=$_.algorithm; params=$_.params;
    ns_per_op=[math]::Round([double]::Parse($_.elapsed_ms,$inv)*1e6/[double]$_.n,1) }
} | Sort-Object params, algo | Format-Table -AutoSize
```
**Bộ dồn cụm** (chỉ dòng `clustered`, 1 lượt):
```powershell
Import-Csv $f | Where-Object { $_.dataset -eq 'clustered' -and $_.ops_count } |
  Select algorithm, params, ops_count | Sort-Object params, algorithm | Format-Table -AutoSize
```

---

## Slide 7 – Kiểm tra tính đúng (khoảng 30 giây)

### 🎤 Kịch bản
**Nội dung slide:**
- Oracle `HashMap`: M = 2¹², 10⁶ thao tác (40% put, 40% get, 20% remove), qua trên cả 5 cấu hình. **(Chỉ nói câu này sau khi `TestRunner` báo 100% qua; log lưu ở `results/notes/c1_test_log.txt`.)**
- 6 ca bắt buộc; ca 2, 3, 4 áp dụng cho `sc`.
- Cùng seed thì `input_sha256` và `output_check` khớp giữa các cài đặt.

**Lời nói:**
"Trước khi đo, mọi cài đặt phải qua oracle so với HashMap của Java và sáu ca bắt buộc. Chúng em chỉ đo khi toàn bộ test qua, và dùng `output_check` cùng `input_sha256` để chắc rằng các cài đặt được chạy trên cùng dữ liệu."

### ⌨️ Lệnh tương ứng
Chạy test và lưu log làm bằng chứng cho checklist C1:
```powershell
cd D:\csd1\Nhom1_CSD\HashTableProject
New-Item -ItemType Directory -Force ..\results\notes | Out-Null
java -cp "build\classes;build\test\classes" hashtable.test.TestRunner *>&1 | Tee-Object ..\results\notes\c1_test_log.txt
java -cp "build\classes;build\test\classes" hashtable.test.TableTests
Get-Content ..\results\notes\c1_test_log.txt -Tail 30
cd ..
```
Kiểm tra file kết quả đủ và đồng nhất (cho dòng "cùng seed" của slide):
```powershell
(Get-Content $f -TotalCount 1).Split(",").Count        # 21
Import-Csv $f | Group-Object machine_id,status | Select Name,Count
```
Phần kiểm chéo `output_check` nằm ở lệnh của slide 3.

---

## Slide 8 – Kết luận và hạn chế (khoảng 30 giây)

### 🎤 Kịch bản
**Nội dung slide:**
- Số lần dò: SC thấp và ổn định theo α; LP tăng mạnh khi α gần 1, nhất là truy vấn vắng mặt.
- Thời gian: bị chi phối thêm bởi cấp phát Node và cache, không chỉ số lần dò.
- Hạn chế: JIT, GC, một máy duy nhất, hàm băm Fibonacci, chỉ 1 seed (42) và 1 lần lặp mỗi cấu hình. **(Nói "máy lab" chỉ khi đúng là máy lab: nhãn `PC_LAB_01` là giá trị mặc định của runner, không chứng minh máy chạy.)**

**Lời nói:**
"Tóm lại, Separate Chaining ổn định hơn theo hệ số tải, còn Linear Probing nhanh trên mỗi bước dò nhưng nhạy với α cao và dồn cụm. Các hạn chế chúng em ghi trong Threats to validity: ảnh hưởng của JIT và GC, kết quả đo trên một máy, hàm băm không hoàn toàn đều và số lượng seed hạn chế."

### ⌨️ Lệnh tương ứng
Lấy thông tin máy và JVM thật để ghi vào paper và nói đúng:
```powershell
$env:COMPUTERNAME
Get-CimInstance Win32_Processor | Select Name,NumberOfCores
[math]::Round((Get-CimInstance Win32_ComputerSystem).TotalPhysicalMemory/1GB,1)
java -version
```
Chốt dữ liệu để khỏi mất lần nữa:
```powershell
git add results/raw/rq1_runner_results_lab.csv results/notes/c1_test_log.txt
git commit -m "RQ1: lab results and C1 test log"
```

---

## Chuẩn bị hỏi đáp (khoảng 2 phút)

| Câu hỏi | Trả lời ngắn | Lệnh/số liệu để dẫn chứng |
|---|---|---|
| Vì sao SC ít lần dò hơn nhưng không nhanh hơn? | Mỗi lần dò của SC là một lần theo con trỏ tới Node rải rác trên heap, dễ cache miss. LP đọc ô kế tiếp trong mảng liên tiếp. SC còn phải cấp phát nhiều đối tượng, tốn bộ nhớ và gây áp lực GC. | Lệnh slide 5 (ns/thao tác, heap) |
| Vì sao tách lượt `--count` khỏi lượt thời gian? | Bộ đếm thêm lệnh vào vòng lặp nên làm sai thời gian. | Smoke test ở slide 3 |
| Vì sao cần `output_check`? | Để JIT không loại bỏ vòng `get`, và để kiểm chéo giữa các cài đặt cùng seed. | Lệnh kiểm chéo ở slide 3 |
| Vì sao khóa vắng mặt ở LP đắt khi α cao? | Phải dò đến khi gặp EMPTY; khi bảng gần đầy, EMPTY còn rất ít. Lý thuyết: ½(1 + 1/(1−α)²) ≈ 200 lần dò ở α = 0,95. | Lệnh slide 4 |
| Mỗi cấu hình chạy mấy lần? Có warm-up không? | 4 lượt: {có mặt, vắng mặt} × {đếm, thời gian}, mỗi lượt một JVM riêng, 1 lần lặp, seed 42. Lượt thời gian có warm-up 1000 lượt tra cứu ngoài vùng đo. | `run_rq1.ps1`, `BenchmarkRunner.java` |
| Dữ liệu chính thức là file nào? | `rq1_runner_results_lab.csv`: 88 dòng, `status=ok`, 21 cột; 8 dòng thuộc α = 0,75, M = 2²⁰ có mặt ở cả thí nghiệm A và B. | Lệnh đếm ở slide 2 |

---

## Việc phải xác nhận trước giờ báo cáo

1. Điền mọi ô `[điền]` bằng số từ CSV lab và đối chiếu Table I của paper với cùng số đó.
2. **Số truy vấn:** runner dùng n truy vấn (khoảng 786 nghìn ở M = 2²⁰, α = 0,75), không phải 5×10⁶ như đề. Thống nhất cách ghi trong paper và slide để cả cặp trả lời giống nhau.
3. Slide RQ1 không còn số SANDBOX hoặc số từ file 44 dòng cũ.
4. `TestRunner` đã báo 100% qua và log đã lưu (slide 7).
5. Chốt người nói (gợi ý: người A slide 1–4, người B slide 5–8, chia nhau phần hỏi đáp) và chạy thử đọc to một lần để canh 5 phút.
