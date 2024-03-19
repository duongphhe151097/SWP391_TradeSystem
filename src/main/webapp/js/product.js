import {urlBuilder} from "./common.js";

$(document).ready(() => {
    const enableConf = {
        lang: 'vi-VN',
        placeholder: 'Nhập nội dung vào đây',
        tabsize: 2,
        minHeight: 200,
        height: 200,
        disableGrammar: true,
        disableDragAndDrop: true
    }

    const productSecret = $("#product-secret")
    const productDescription = $("#product-description")

    productSecret.summernote(enableConf)
    productDescription.summernote(enableConf)

    $("#add-product").off().click((e) => {
        $("#addProductModal").modal('show')

        let debounce = null
        $("#product-modal-confirm").off().click((e) => {
            clearTimeout(debounce)
            debounce = setTimeout(() => {
                const data = $("#product-data-form").serialize();
                console.log(data)

                $.ajax({
                    type: 'post',
                    url: urlBuilder("/product/sale"),
                    xhrFields: {
                        withCredentials: true
                    },
                    data: data,
                    success: (resp) => {
                        $('#addProductModal').modal('hide')

                        $('#toast-title').html('Thành công!')
                        $('#toast-body').html(resp.message)

                        const liveToast = $('#liveToast')
                        liveToast.toast('show')

                        liveToast.on('hide.bs.toast', () => {
                            location.reload()
                        })
                    },
                    error: (err) => {
                        $('#addProductModal').modal('hide')

                        if (err?.responseJSON?.message === "NOT_ENOUGH_MONEY") {
                            const url = urlBuilder("/payment/vnpay/create");

                            $("#modal-title").html("Không đủ tiền trong tài khoản?")
                            $("#modal-body").html(`Bạn không có đủ tiền trong tài khoản, vui lòng <a href="${url}">nạp thêm</a> để tiếp tục!`)
                            $('#myModal').modal('show')

                            $("#modal-confirm").off().click(() => {
                                window.location.href = url;
                            })
                        } else {
                            $('#toast-title').html('Không thành công!')
                            $('#toast-body').html(err?.responseJSON?.message)
                            const liveToast = $('#liveToast')
                            liveToast.toast('show')
                        }
                    }
                })
            }, 1000)
        })
    })
})