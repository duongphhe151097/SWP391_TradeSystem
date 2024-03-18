import { urlBuilder } from "./common.js";
$(document).ready(() => {
    loadNotificationList();
    $('#notificationDropdown').on('shown.bs.dropdown', function () {
        $('#notificationBadge').hide(); // Ẩn badge khi dropdown được mở ra
    });
});
let debounce = null
function loadNotificationList() {
    clearTimeout(debounce)
    debounce = setTimeout(() => {
    $.ajax({
        url: urlBuilder("/notification"),
        type: 'get',
        success: function(response) {
                console.log(response);
                displayNotificationList(response);
        },
        error: function(xhr, status, error) {
            console.error(error);
            alert("Đã có lỗi xảy ra!");
        }
    });
    },1000)
    }


function displayNotificationList(notificationList) {
    let notificationDropdown = $("#notificationDropdown");
    notificationDropdown.empty();

    notificationList.forEach(notification => {
       let createAt =null;
        if (notification.createAt instanceof Date && !isNaN(notification.createAt)) {
             createAt = notification.createAt;

        } else {

          createAt = new Date(notification.createAt);
        }
        let formattedCreateAt = createAt.toLocaleString();
        let messageDiv = $("<div>")
            .addClass("d-flex w-100 justify-content-between align-items-center");


        // Thêm tiêu đề thông điệp
        let title = $("<h5>")
            .addClass("mb-1")
            .text(notification.message);
        let messageContainer = $("<div>")
            .addClass("message-container");


        messageContainer.append(title);
        if (!notification.isSeen) {
            let isSeenStatus = $("<span>")
                .addClass("is-seen-status")
                .text("Chưa đọc");
            messageContainer.append(isSeenStatus);
        } else {
            let isSeenStatus = $("<span>")
                .addClass("is-seen-status")
                .text("Đã đọc");
            messageContainer.append(isSeenStatus);
        }
        messageDiv.append(messageContainer);

        messageDiv.append(title);

        // Tạo phần tử p chứa thời gian tạo
        let timeParagraph = $("<p>")
            .addClass("mb-1")
            .text(formattedCreateAt);


        // Thêm phần tử p vào phần tử div
        messageDiv.append(timeParagraph);
        // Tạo listItem và thêm messageDiv vào đó
        let listItem = $("<a>")
            .addClass("dropdown-item notification")
            .data("notification-id", notification.id)
            .data("is-seen", notification.isSeen)
            .data("notification-message", notification.message)
            .data("create-at", formattedCreateAt)
            .attr("href", "#")
            .click(function() {
                let notificationId = $(this).data("notification-id");
                let isSeen = $(this).data("is-seen");
                if(!isSeen){
                updateNotificationStatus(notificationId, true);
                }else{

                }
            })
            .append(messageDiv);

        // Thêm listItem vào notificationDropdown
        notificationDropdown.append(listItem);
    });
}

function updateNotificationStatus( notificationId, isSeen) {
    clearTimeout(debounce)
    debounce = setTimeout(() => {
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
        data:JSON.stringify(data),
        success: function (response) {
            console.log(response.message);
        },
        error: function (xhr, status, error) {
            console.error("Error updating notification status:", error);
            alert("Đã có lỗi xảy ra khi cập nhật trạng thái thông báo!");
        }
    });
    },1000)
    }
let submitCount = 0;
function updateSubmitCount() {
    submitCount++;
    $("#notificationBadge").text(submitCount).show();
}

function hideBadge() {
    if (submitCount === 0) {
        $("#notificationBadge").hide();
    }
}
$("#dropdownMenuButton").on("click", function() {
    submitCount = 0; // Reset số lần click submit
    hideBadge(); // Ẩn badge nếu số lần click submit là 0
});
