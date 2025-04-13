<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Tạo Đơn Thuốc</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <script>
            let medicineList = [];
            window.onload = function () {
                document.querySelectorAll("#medicineTemplate option").forEach(opt => {
                    if (opt.value) {
                        medicineList.push({
                            id: opt.value,
                            name: opt.text
                        });
                    }
                });
            };

            function getSelectedMedicineIds() {
                const selects = document.querySelectorAll("select[name='medicineName[]']");
                const selected = [];
                selects.forEach(sel => {
                    if (sel.value) {
                        selected.push(sel.value);
                    }
                });
                return selected;
            }

            function generateMedicineOptions() {
                const selected = getSelectedMedicineIds();
                return medicineList
                        .filter(med => !selected.includes(med.id))
                        .map(med => `<option value="${med.id}">${med.name}</option>`)
                        .join("");
            }

            function addMedicineRow() {
                const options = generateMedicineOptions();
                if (!options) {
                    alert("Tất cả các loại thuốc đã được chọn.");
                    return;
                }

                const tableBody = document.getElementById("medicineTableBody");
                const newRow = document.createElement("tr");

                newRow.innerHTML = `
                    <td>
                        <select name="medicineName[]" class="form-select" onchange="updateAllDropdowns()">
                            <option value="" disabled selected>---Chọn thuốc---</option>
            ${options}
                        </select>
                    </td>
                    <td><input type="text" name="dosage[]" class="form-control"></td>
                    <td><input type="number" name="quantity[]" class="form-control" min="1"></td>
                    <td><input type="number" name="duration[]" class="form-control" min="1"></td>
                    <td><button type="button" class="btn btn-danger btn-sm" onclick="deleteRow(this)">Xóa</button></td>
                `;
                tableBody.appendChild(newRow);
                updateAllDropdowns();
            }

            function deleteRow(btn) {
                const row = btn.closest("tr");
                row.remove();
                updateAllDropdowns();
            }
            function updateAllDropdowns() {
                const selected = getSelectedMedicineIds();

                const selects = document.querySelectorAll("select[name='medicineName[]']");
                selects.forEach(select => {
                    const currentValue = select.value;
                    select.innerHTML = `<option value="" disabled ${currentValue ? '' : 'selected'}>---Chọn thuốc---</option>`;

                    medicineList.forEach(med => {
                        if (!selected.includes(med.id) || med.id === currentValue) {
                            const opt = document.createElement("option");
                            opt.value = med.id;
                            opt.text = med.name;
                            if (med.id === currentValue) {
                                opt.selected = true;
                            }
                            select.appendChild(opt);
                        }
                    });
                });
            }
        </script>
    </head>
    <body class="bg-light">
        <div class="container mt-5 w-50">
            <h2 class="mb-4 text-primary text-center">Tạo Đơn Thuốc</h2>
            <div class="d-flex justify-content-center">
                <table class="table table-bordered w-75">
                    <tbody>
                        <tr>
                            <th scope="row" class="col-4">Bệnh nhân</th>
                            <td class="col-8">${p.appointmentID.patientID.fullName}</td>
                        </tr>
                        <tr>
                            <th scope="row" class="col-4">Triệu chứng</th>
                            <td class="col-8">${p.symptoms}</td>
                        </tr>
                        <tr>
                            <th scope="row" class="col-4">Chuẩn đoán</th>
                            <td class="col-8">${p.diagnosis}</td>
                        </tr>
                        <tr>
                            <th scope="row" class="col-4">Bác sĩ</th>
                            <td class="col-8">${p.appointmentID.doctorID.fullName}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <c:if test="${not empty error}">
                <p class="text-center text-danger">${error}</p>
            </c:if>
            <form action="AppointmentServlet" method="post">
                <input type="hidden" name="recordID" value="${p.recordID}" />
                <table class="table table-bordered table-striped align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th scope="col" style="width: 35%;">Tên thuốc</th>
                            <th scope="col" style="width: 25%;">Liều lượng</th>
                            <th scope="col" style="width: 15%;">Số lượng</th>
                            <th scope="col" style="width: 15%;">Số ngày</th>
                            <th scope="col" style="width: 10%;"></th>
                        </tr>
                    </thead>
                    <tbody id="medicineTableBody">
                        <c:choose>
                            <c:when test="${not empty medicineIDs}">
                                <c:forEach var="i" begin="0" end="${fn:length(medicineIDs)-1}">
                                    <tr>
                                        <td>
                                            <select name="medicineName[]" class="form-select" onchange="updateAllDropdowns()">
                                                <option value="" disabled>---Chọn thuốc---</option>
                                                <c:forEach var="med" items="${medicines}">
                                                    <option value="${med.medicineID}"
                                                            <c:if test="${medicineIDs[i] == med.medicineID}">selected</c:if>>
                                                        ${med.medicineName}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td><input type="text" name="dosage[]" class="form-control" value="${dosages[i]}"></td>
                                        <td><input type="number" name="quantity[]" class="form-control" min="1" value="${quantities[i]}"></td>
                                        <td><input type="number" name="duration[]" class="form-control" min="1" value="${durations[i]}"></td>
                                        <td><button type="button" class="btn btn-danger btn-sm" onclick="deleteRow(this)">Xóa</button></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td>
                                        <select name="medicineName[]" class="form-select" onchange="updateAllDropdowns()">
                                            <option value="" disabled selected>---Chọn thuốc---</option>
                                            <c:forEach var="p" items="${medicines}">
                                                <option value="${p.medicineID}">${p.medicineName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td><input type="text" name="dosage[]" class="form-control"></td>
                                    <td><input type="number" name="quantity[]" class="form-control" min="1"></td>
                                    <td><input type="number" name="duration[]" class="form-control" min="1"></td>
                                    <td><button type="button" class="btn btn-danger btn-sm" onclick="deleteRow(this)">Xóa</button></td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
                <div id="medicineTemplate" style="display: none;">
                    <c:forEach var="p" items="${medicines}">
                        <option value="${p.medicineID}">${p.medicineName}</option>
                    </c:forEach>
                </div>

                <div class="d-flex justify-content-between">
                    <button type="button" class="btn btn-success" onclick="addMedicineRow()">+ Thêm thuốc</button>
                    <button type="submit" name="action" value="CreatePrescription" class="btn btn-primary">Lưu Đơn Thuốc</button>
                </div>
            </form>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
