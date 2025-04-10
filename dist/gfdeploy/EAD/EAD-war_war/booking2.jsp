<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Đặt lịch khám</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .section-header {
                font-weight: bold;
                margin-top: 15px;
            }
            #datepicker {
                width: 100%;
            }
            .time-slot {
                padding: 10px;
                border: 1px solid #007bff;
                border-radius: 5px;
                text-align: center;
                cursor: pointer;
                transition: all 0.3s ease;
                background-color: #28a745;
                color: white;
            }

            .time-slot.booked {
                background-color: #dc3545 !important;
                pointer-events: none;
            }

            .time-slot.active {
                background-color: orange !important;
                color: white;
            }
        </style>
    </head>
    <body>
        <div class="container py-5">
            <form action="BookAppointmentServlet" method="post">
                <div class="row g-4">
                    <div class="col-md-6">
                        <div class="p-4 bg-white rounded shadow">
                            <h2 class="text-success text-center">👤 Thông tin bệnh nhân</h2>
                            <hr>
                            <table class="table table-bordered">
                                <tbody>
                                    <tr>
                                        <th scope="row" class="col-4">Họ và tên</th>
                                        <td class="col-8">Nguyễn Văn A</td>
                                    </tr>
                                    <tr>
                                        <th scope="row" class="col-4">Giới tính</th>
                                        <td class="col-8">Nam</td>
                                    </tr>
                                    <tr>
                                        <th scope="row" class="col-4">Ngày sinh</th>
                                        <td class="col-8">1990-01-01</td>
                                    </tr>
                                    <tr>
                                        <th scope="row" class="col-4">Số điện thoại</th>
                                        <td class="col-8">0901234567</td>
                                    </tr>
                                    <tr>
                                        <th scope="row" class="col-4">Địa chỉ</th>
                                        <td class="col-8">123 Nguyễn Trãi, Hà Nội</td>
                                    </tr>
                                    <tr>
                                        <th scope="row" class="col-4">Email</th>
                                        <td class="col-8">nguyenvana@gmail.com</td>
                                    </tr>
                                </tbody>
                            </table>
                            <br>
                            <hr>
                            <div class="mb-3 row align-items-center">
                                <label for="department" class="col-sm-4 col-form-label fs-5">Ngành khám</label>
                                <div class="col-sm-8">
                                    <select name="department" id="department" class="form-select" required onchange="loadDoctorsByDepartment()">
                                        <option value="">-- Chọn ngành khám --</option>
                                        <option value="Noi tong quat">Nội tổng quát</option>
                                        <option value="Nhi khoa">Nhi khoa</option>
                                        <option value="Da lieu">Da liễu</option>
                                        <option value="Mat">Mắt</option>
                                    </select>
                                </div>
                            </div>

                            <div class="mb-3 row align-items-center">
                                <label for="doctor" class="col-sm-4 col-form-label fs-5">Bác sĩ</label>
                                <div class="col-sm-8">
                                    <select name="doctorId" id="doctor" class="form-select" required onchange="updateTimeSlotColors()">
                                        <option value="">-- Chọn bác sĩ --</option>
                                    </select>
                                </div>
                            </div>

                            <div class="mb-3 row">
                                <label for="notes" class="col-sm-4 col-form-label fs-5">Ghi chú / Triệu chứng</label>
                                <div class="col-sm-8">
                                    <textarea name="notes" id="notes" class="form-control" rows="3" placeholder="Nhập triệu chứng hoặc ghi chú..."></textarea>
                                </div>
                            </div>


                            <input type="hidden" name="selectedTime" id="selectedTimeInput">
                            <input type="hidden" name="selectedDate" id="selectedDateInput">

                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="p-4 bg-white rounded shadow mb-4">
                            <label for="datepicker" class="form-label" id="selectedDateLabel">Chọn ngày khám:</label>
                            <input type="text" id="datepicker" class="form-control" placeholder="Pick a date">
                        </div>

                        <div class="p-4 bg-white rounded shadow" id="timeSlots">
                            <h5 class="section-header">Sáng</h5>
                            <div class="row row-cols-3">
                                <div class="col mb-2"><div class="time-slot">08:00</div></div>
                                <div class="col mb-2"><div class="time-slot">08:30</div></div>
                                <div class="col mb-2"><div class="time-slot">09:00</div></div>
                                <div class="col mb-2"><div class="time-slot">09:30</div></div>
                                <div class="col mb-2"><div class="time-slot">10:00</div></div>
                                <div class="col mb-2"><div class="time-slot">10:30</div></div>
                                <div class="col mb-2"><div class="time-slot">11:00</div></div>
                                <div class="col mb-2"><div class="time-slot">11:30</div></div>
                                <div class="col mb-2"><div class="time-slot">12:00</div></div>
                            </div>

                            <h5 class="section-header">Chiều</h5>
                            <div class="row row-cols-3">
                                <div class="col mb-2"><div class="time-slot">12:30</div></div>
                                <div class="col mb-2"><div class="time-slot">13:00</div></div>
                                <div class="col mb-2"><div class="time-slot">13:30</div></div>
                                <div class="col mb-2"><div class="time-slot">14:00</div></div>
                                <div class="col mb-2"><div class="time-slot">14:30</div></div>
                                <div class="col mb-2"><div class="time-slot">15:00</div></div>
                                <div class="col mb-2"><div class="time-slot">15:30</div></div>
                                <div class="col mb-2"><div class="time-slot">16:00</div></div>
                                <div class="col mb-2"><div class="time-slot">16:30</div></div>
                            </div>
                            <button type="submit" class="btn btn-primary w-100 mt-3">Đặt lịch khám</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
        <script>
                                        flatpickr("#datepicker", {
                                            minDate: "today",
                                            dateFormat: "Y-m-d",
                                            defaultDate: new Date(),
                                            onChange: function (selectedDates, dateStr) {
                                                const label = document.getElementById("selectedDateLabel");
                                                label.innerText = "Đã chọn Ngày khám: " + dateStr;
                                                label.className = "text-center fw-bold text-primary fs-5 mb-1";
                                                document.getElementById("selectedDateInput").value = dateStr;
                                                updateTimeSlotColors();
                                            }
                                        });

                                        document.querySelectorAll('.time-slot').forEach(slot => {
                                            slot.addEventListener('click', function () {
                                                document.querySelectorAll('.time-slot').forEach(s => s.classList.remove('active'));
                                                this.classList.add('active');
                                                document.getElementById("selectedTimeInput").value = this.textContent.trim();
                                            });
                                        });

                                        function loadDoctorsByDepartment() {
                                            const department = document.getElementById("department").value;
                                            const doctorSelect = document.getElementById("doctor");
                                            doctorSelect.innerHTML = '<option value="">-- Chọn bác sĩ --</option>';
                                            if (department) {
                                                fetch("get-doctors?department=" + encodeURIComponent(department))
                                                        .then(response => response.text())
                                                        .then(data => {
                                                            doctorSelect.innerHTML += data;
                                                        })
                                                        .catch(error => {
                                                            console.error("Lỗi khi load danh sách bác sĩ:", error);
                                                        });
                                            }
                                        }

                                        function updateTimeSlotColors() {
                                            document.querySelectorAll('.time-slot').forEach(slot => slot.classList.remove('active'));
                                            const doctorIdStr = document.getElementById("doctor").value;
                                            const doctorId = parseInt(doctorIdStr);
                                            const selectedDate = document.getElementById("selectedDateInput").value;
                                            if (!doctorId || !selectedDate)
                                                return;
                                            fetch("get-booked-time?doctorId=" + doctorId + "&date=" + selectedDate)
                                                    .then(res => res.json())
                                                    .then(bookedTimes => {
                                                        const formattedTimes = bookedTimes.map(time => time.substring(0, 5));
                                                        document.querySelectorAll('.time-slot').forEach(slot => {
                                                            const timeText = slot.textContent.trim();
                                                            if (formattedTimes.includes(timeText)) {
                                                                slot.classList.remove("available");
                                                                slot.classList.add("booked");
                                                                slot.style.backgroundColor = "#dc3545";
                                                                slot.style.color = "white";
                                                                slot.style.pointerEvents = "none";
                                                            } else {
                                                                slot.classList.add("available");
                                                                slot.classList.remove("booked");
                                                                slot.style.backgroundColor = "#28a745";
                                                                slot.style.color = "white";
                                                                slot.style.pointerEvents = "auto";
                                                            }
                                                        });
                                                    });
                                        }
        </script>
    </body>
</html>