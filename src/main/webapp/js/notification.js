import { urlBuilder } from "./common.js";
$(document).ready(() => {
    loadNotificationList();
    $('#notificationDropdown').on('shown.bs.dropdown', function () {
        $('#notificationBadge').hide(); // Ẩn badge khi dropdown được mở ra
    });
});

function loadNotificationList() {
    $.ajax({
        url: urlBuilder("/notification"),
        type: 'get',
        success: function(response) {
            try {
                if (!response.trim()) { // Kiểm tra xem dữ liệu phản hồi có trống không
                    throw new Error("Empty response from server");
                }
                let data = JSON.parse(response); // Phân tích dữ liệu JSON
                console.log(data);
                displayNotificationList(data);
            } catch (error) {
                console.error("Error parsing data:", error);
                alert("Đã có lỗi xảy ra!");
            }
        },
        error: function(xhr, status, error) {
            console.error(error);
            alert("Đã có lỗi xảy ra!");
        }
    });
}

function displayNotificationList(notificationList) {
    let notificationDropdown = $("#notificationDropdown");
    notificationDropdown.empty();

    notificationList.forEach(notification => {
        let formattedCreateAt = moment(notification.createAt).format("YYYY-MM-DD HH:mm:ss");
        // Create a new element to display the notification and add it to the dropdown
        let listItem = $("<a>")
            .addClass("dropdown-item notification") // Add a class to identify notification items
            .data("notification-id", notification.id) // Attach notification ID as data attribute
            .data("is-seen", notification.isSeen) // Attach isSeen status as data attribute
            .data("notification-message", notification.message) // Attach message as data attribute
            .data("create-at", formattedCreateAt) // Attach createAt as data attribute
            .attr("href", "#") // Add href attribute for better accessibility
            .click(function() {
                let notificationId = $(this).data("notification-id");
                let isSeen = $(this).data("is-seen");

                // Toggle isSeen status and update notification in the database
                updateNotificationStatus(notificationId, !isSeen);
            });
        if (!notification.isSeen) {
            listItem.append("<span class='dot'></span>");
        }
        listItem.append(notification.message + " - " + formattedCreateAt);
        notificationDropdown.append(listItem);
    });
}

function updateNotificationStatus(notificationId, isSeen) {
    // Tạo một đối tượng JSON chứa dữ liệu bạn muốn gửi
    var data = {
        notificationId: notificationId,
        isSeen: isSeen
    };

    // Sử dụng AJAX để gửi yêu cầu POST
    $.ajax({
        url: urlBuilder("/notification"),
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (response) {
            console.log(response.message); // In ra thông báo từ máy chủ
            // Nếu bạn muốn cập nhật giao diện người dùng sau khi cập nhật trạng thái thông báo, bạn có thể thực hiện ở đây
        },
        error: function (xhr, status, error) {
            console.error("Error updating notification status:", error);
            alert("Đã có lỗi xảy ra khi cập nhật trạng thái thông báo!");
        }
    });
}
let submitCount = 0;

// Cập nhật số lần click submit và hiển thị badge
function updateSubmitCount() {
    submitCount++;
    $("#notificationBadge").text(submitCount).show(); // Hiển thị badge và cập nhật số lần click submit
}

// Ẩn badge nếu số lần click submit là 0
function hideBadge() {
    if (submitCount === 0) {
        $("#notificationBadge").hide();
    }
}

// Thêm sự kiện click vào dropdown
$("#dropdownMenuButton").on("click", function() {
    submitCount = 0; // Reset số lần click submit
    hideBadge(); // Ẩn badge nếu số lần click submit là 0
});
