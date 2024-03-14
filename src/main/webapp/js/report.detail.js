import {getLocation, getParams} from "./common.js";

$(document).ready(() => {
    const disableConf = {
        lang: 'vi-VN',
        placeholder: 'Nhập nội dung vào đây',
        tabsize: 2,
        height: 200,
        disableGrammar: false,
        disableDragAndDrop: true,
        toolbar: []
    }

    const reportDescription = $("#report-description");
    reportDescription.summernote(disableConf)
    reportDescription.summernote('disable')

    const adminResponse = $("#admin-response");
    adminResponse.summernote(disableConf)
    adminResponse.summernote('disable')

    $("#abort-report").off().click((e) => {
        e.preventDefault()
        const hrefUrl = e?.currentTarget?.href;
        const params = getParams(hrefUrl.split("?")[1]);

        $("#modal-title").html("Bạn có chắc?")
        $("#modal-body").html("Bạn có chắc chắn muốn hủy khiếu nại này, nếu hủy bạn sẽ mất 10% số tiền khiếu nại!")
        $('#myModal').modal('show')

        let debounce = null
        $("#modal-confirm").off().click(() => {
            clearTimeout(debounce)
            debounce = setTimeout(() => {
                $.ajax({
                    type: 'post',
                    url: getLocation(hrefUrl)?.pathname,
                    xhrFields: {
                        withCredentials: true
                    },
                    data: {
                        id: params?.id,
                    },
                    success: (resp) => {
                        $('#myModal').modal('hide')

                        $('#toast-title').html('Thành công!')
                        $('#toast-body').html(resp.message)
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