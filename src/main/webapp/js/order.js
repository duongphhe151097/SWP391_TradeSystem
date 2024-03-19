import {getParams, urlBuilder} from "./common.js";

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

    const reportCreateDescription = $("#report-description")
    reportCreateDescription.summernote(enableConf)

    $("#order-report").off().click((e) => {
        e.preventDefault()

        $('#reportProductModal').modal('show')
        let debounce = null
        $("#report-modal-confirm").off().click(() => {
            const data = $("#report-data-form").serialize();
            clearTimeout(debounce)
            debounce = setTimeout(() => {
                $.ajax({
                    url: urlBuilder("/report"),
                    type: "post",
                    xhrFields: {
                        withCredentials: true
                    },
                    data: data,
                    success: (resp) => {
                        $('#reportProductModal').modal('hide')

                        $('#toast-title').html('Thành công!')
                        $('#toast-body').html(resp?.message)
                        const liveToast = $('#liveToast')
                        liveToast.toast('show')

                        liveToast.on('hide.bs.toast', () => {
                            location.reload()
                        })
                    },
                    error: (err) => {
                        $('#reportProductModal').modal('hide')
                        $('#toast-title').html('Không thành công!')
                        $('#toast-body').html(err?.responseJSON?.message)
                        const liveToast = $('#liveToast')
                        liveToast.toast('show')
                    }
                })
            }, 1000)
        })
    })

    $("#order-confirm").off().click((e) => {
        e.preventDefault()
        const hrefUrl = e?.currentTarget?.href
        const params = getParams(hrefUrl.split("?")[1]);

        $("#modal-title").html("Bạn có chắc?")
        $("#modal-body").html("Bạn có chắc chắn giao dịch này đã hoàn thành!")
        $('#myModal').modal('show')

        let debounce = null
        $("#modal-confirm").off().click(() => {
            clearTimeout(debounce)
            debounce = setTimeout(() => {
                $.ajax({
                    url: hrefUrl,
                    type: 'post',
                    xhrFields: {
                        withCredentials: true
                    },
                    data: {
                        id: params?.id,
                        type: 'CONFIRM'
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
                        const liveToast = $('#liveToast')
                        liveToast.toast('show')
                    }
                })
            }, 1000)
        })
    })
})