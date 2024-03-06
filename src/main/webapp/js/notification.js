import { urlBuilder } from "./common.js";
$(document).ready(() => {
    // Load notifications when the document is ready
    loadNotifications();
});

function loadNotifications() {
    // Send GET request to fetch notifications
    $.ajax({
        url: "/notification",
        type: 'get',
        success: (response) => {
            console.log(response);
            displayNotifications(response);
        },
        error: (xhr, status, error) => {
            console.error(error);
            // Handle error here, for example: display error message to the user
            alert("Đã có lỗi xảy ra!");
        }
    });
}

function displayNotifications(notifications) {
    let notificationDropdown = $("#notificationDropdown");
    notificationDropdown.empty(); // Clear old notifications before adding new ones

    notifications.forEach(notification => {
        let formattedCreateAt = moment(notification.createAt).format("YYYY-MM-DD HH:mm:ss");
        // Create a new element to display the notification and add it to the dropdown
        let listItem = $("<a>")
            .addClass("dropdown-item notification") // Add a class to identify notification items
            .data("notification-id", notification.id) // Attach notification ID as data attribute
            .data("is-seen", notification.isSeen) // Attach isSeen status as data attribute
            .data("notification-message", notification.message) // Attach message as data attribute
            .data("create-at", formattedCreateAt) // Attach createAt as data attribute
        if (!notification.isSeen) {
            listItem.append("<span class='dot'></span>");
        }
        listItem.append(notification.message + " - " + formattedCreateAt);
        notificationDropdown.append(listItem);
    });
    // Attach click event handler for notifications
    notificationDropdown.on("click", "a.notification", function() {
        let notificationId = $(this).data("notification-id");
        let isSeen = $(this).data("is-seen");

        // Toggle isSeen status and update notification in the database
        updateNotificationStatus(notificationId, !isSeen);
    });
}

function updateNotificationStatus(notificationId, isSeen) {
    // Prepare data for POST request
    let data = {
        notificationId: notificationId,
        isSeen: isSeen
    };

    // Send POST request to update notification status
    $.ajax({
        url: "/notification",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function(response) {
            loadNotifications();
        },
        error: function(xhr, status, error) {
            alert("Đã có lỗi xảy ra!");
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