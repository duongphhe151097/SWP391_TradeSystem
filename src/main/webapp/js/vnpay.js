$(document).ready(() => {
    $("#submitVnpOrder").click(() => {
        let postData = $("#frmCreateOrder").serialize();
        let submitUrl = $("#frmCreateOrder").attr("action");

        $.ajax({
            type: "POST",
            url: submitUrl,
            data: postData,
            dataType: "application/json",
            success: function (x) {
                alert(x);
                if (x.code === "00") {
                    if (window.vnpay) {
                        vnpay.open({width: 768, height: 600, url: x.data});
                    } else {
                        location.href = x.data;
                    }

                    return false;
                } else {
                    alert(x.Message);
                }
            }
        });
        return false;
    })
})