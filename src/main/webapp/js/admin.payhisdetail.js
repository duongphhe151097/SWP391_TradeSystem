import {formatCurrency, formatVnPayStatus, formatVnPayTime, formatVnPayTxnRef, urlBuilder} from "./common.js";

$(document).ready(() => {
    let tempJson = {};
    $("#payment-check").off().click((e) => {
        e.preventDefault()
        const hrefUrl = e?.currentTarget?.href;

        $("#modal-title").html("Bạn có chắc?")
        $("#modal-body").html("Truy vấn thông tin thanh toán trên hệ thống vnpay, chỉ truy vấn được 5 phút 1 lần / 1 mã giao dịch")
        $('#myModal').modal('show')

        let debounce = null
        $("#modal-confirm").off().click(() => {
            clearTimeout(debounce)
            debounce = setTimeout(() => {
                $.ajax({
                    type: 'get',
                    url: hrefUrl,
                    xhrFields: {
                        withCredentials: true
                    },
                    success: (resp) => {
                        tempJson = JSON.parse(resp?.data)
                        const vnpaySuccess = vnpayQueryTemplate(tempJson)
                        $("#query-response").html(wrapperTemplate(vnpaySuccess))

                        $('#myModal').modal('hide')

                        $('#toast-title').html('Thành công!')
                        $('#toast-body').html(resp.message)
                        const liveToast = $('#liveToast')
                        liveToast.toast('show')

                        $("#payment-sync").off().click((e) => {
                            e.preventDefault()

                            const updateUrl = e?.currentTarget?.href;
                            $("#modal-title").html("Bạn có chắc?")
                            $("#modal-body").html("Cập nhật lại thông tin vào hệ thống!")
                            $('#myModal').modal('show')

                            let debounce = null
                            $("#modal-confirm").off().click(() => {
                                clearTimeout(debounce)
                                debounce = setTimeout(() => {
                                    $.ajax({
                                        type: 'post',
                                        url: updateUrl,
                                        xhrFields: {
                                            withCredentials: true
                                        },
                                        data: {
                                            data: JSON.stringify(tempJson)
                                        },
                                        success: (resp) => {
                                            $('#myModal').modal('hide')
                                            $('#toast-title').html('Thành công!')
                                            $('#toast-body').html(resp?.message)
                                            const liveToast = $('#liveToast')
                                            liveToast.toast('show')

                                            liveToast.on('hide.bs.toast', () => {
                                                location.reload()
                                            })
                                        },
                                        error: (err) => {
                                            $('#myModal').modal('hide')
                                            $('#toast-title').html('Không thành công!')
                                            $('#toast-body').html(err?.responseJSON?.message)
                                            $('#liveToast').toast('show')
                                        }
                                    })
                                })
                            })
                        })
                    },
                    error: (err) => {
                        console.log(err?.responseJSON?.message)
                        const queryFailed = vnpayQueryFailed(err?.responseJSON);
                        $("#query-response").html(wrapperTemplate(queryFailed))
                        $('#myModal').modal('hide')
                        $('#toast-title').html('Không thành công!')
                        $('#toast-body').html(err?.responseJSON?.message)
                        $('#liveToast').toast('show')
                    }
                })
            }, 1000)
        })
    })

    $("#payment-refund").off().click((e) => {
        e.preventDefault()

        $("#modal-title").html("Bạn có chắc?")
        $("#modal-body").html("Hoàn tiền cho người dùng!")
        $('#myModal').modal('show')


    })
})

const wrapperTemplate = (body) => {
    return `
        <div class="card">
            <div class="card-header bg-secondary">
                <h5 class="text-white">Thông tin truy xuất từ VNPAY</h5>
            </div>
            <div class="card-body">
                ${body}
            </div>
        </div>`
}

const vnpayQueryTemplate = (data) => {
    let templateBuilder = []
    templateBuilder.push("<div class=\"d-flex\">")
    templateBuilder.push("<div class=\"col-md-6\">")
    templateBuilder.push("<div class=\"row\">")
    templateBuilder.push("<table class=\"table table-borderless\">")
    templateBuilder.push("<thead>")
    templateBuilder.push("<tr>")
    templateBuilder.push("<th scope=\"col\"></th>")
    templateBuilder.push("<th scope=\"col\"></th>")
    templateBuilder.push("</tr>")
    templateBuilder.push("</thead>")
    templateBuilder.push("<tbody>")
    templateBuilder.push("<tr>")
    templateBuilder.push("<th>Mã giao dịch:</th>")
    templateBuilder.push(`<td>${formatVnPayTxnRef(data?.vnp_TxnRef)}</td>`)
    templateBuilder.push("</tr>")
    templateBuilder.push("<tr>")
    templateBuilder.push("<th>Trạng thái:</th>")
    templateBuilder.push(`<td>${formatVnPayStatus(data?.vnp_TransactionStatus)}</td>`)
    templateBuilder.push("</tr>")
    templateBuilder.push("<tr>")
    templateBuilder.push("<th>Nội dung thanh toán:</th>")
    templateBuilder.push(`<td>${data?.vnp_OrderInfo}</td>`)
    templateBuilder.push("</tr>")
    templateBuilder.push("</tbody>")
    templateBuilder.push("</table>")
    templateBuilder.push("</div>")
    templateBuilder.push("</div>")

    templateBuilder.push("<div class=\"col-md-6\">")
    templateBuilder.push("<div class=\"row\">")
    templateBuilder.push("<table class=\"table table-borderless\">")
    templateBuilder.push("<thead>")
    templateBuilder.push("<tr>")
    templateBuilder.push("<th scope=\"col\"></th>")
    templateBuilder.push("<th scope=\"col\"></th>")
    templateBuilder.push("</tr>")
    templateBuilder.push("</thead>")
    templateBuilder.push("<tbody>")
    templateBuilder.push("<tr>")
    templateBuilder.push("<th>Số tiền:</th>")
    templateBuilder.push(`<td>${formatCurrency(data?.vnp_Amount / 100)}</td>`)
    templateBuilder.push("</tr>")
    templateBuilder.push("<tr>")
    templateBuilder.push("<th>Thời gian thanh toán:</th>")
    templateBuilder.push(`<td>${formatVnPayTime(data?.vnp_PayDate)}</td>`)
    templateBuilder.push("</tr>")
    templateBuilder.push("<tr>")
    templateBuilder.push("<th>Mã giao dịch (cổng thanh toán):</th>")
    templateBuilder.push(`<td>${data?.vnp_TransactionNo}</td>`)
    templateBuilder.push("</tr>")
    templateBuilder.push("</tbody>")
    templateBuilder.push("</table>")
    templateBuilder.push("</div>")
    templateBuilder.push("</div>")
    templateBuilder.push("</div>")

    if (data?.vnp_TransactionStatus !== "00") {
        templateBuilder.push("<div class=\"col-md-12 d-flex\">")
        templateBuilder.push("<div class=\"mr-2\">")
        templateBuilder.push(`<a id=\"payment-sync\" class=\"btn btn-primary\" href=\"${urlBuilder("/payment/vnpay/query")}\" role=\"button\">Đồng bộ dữ liệu</a>`)
        templateBuilder.push("</div>")
        templateBuilder.push("</div>")
    }

    const template = templateBuilder.join("\n")
    return template;
}

const vnpayQueryFailed = (data) => {
    let templateBuilder = [];

    templateBuilder.push("<div>");
    templateBuilder.push(`<p>Lỗi!</p>`);
    templateBuilder.push(`<p>Code: ${data?.code} </p>`);
    templateBuilder.push(`<p>Message: ${data?.message} </p>`);
    templateBuilder.push("</div>");

    return templateBuilder.join("\n")
}