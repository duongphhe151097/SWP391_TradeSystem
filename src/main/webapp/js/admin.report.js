import {getParams, getLocation} from "./common.js";

$(document).ready(() => {
    const reportDescription = $("#report-description");
    const disableConf = {
        lang: 'vi-VN',
        placeholder: 'Nhập nội dung vào đây',
        tabsize: 2,
        height: 200,
        disableGrammar: false,
        disableDragAndDrop:true,
        toolbar: []
    }

    const enableConf = {
        lang: 'vi-VN',
        placeholder: 'Nhập nội dung vào đây',
        tabsize: 2,
        height: 200,
        disableGrammar: false,
        disableDragAndDrop:true,
    }

    reportDescription.summernote(disableConf)
    reportDescription.summernote('disable')

    const reportAdminResponse = $("#report-adminres");

    if (reportAdminResponse.hasClass("editor-disable")) {
        reportAdminResponse.summernote(disableConf)
        reportAdminResponse.summernote('disable')
    } else {
        reportAdminResponse.summernote(enableConf)
    }

    $("#admin-report-processing").off().click((e) => {
        e.preventDefault()
        const hrefUrl = e?.currentTarget?.href;
        const params = getParams(hrefUrl.split("?")[1]);

        $("#modal-title").html("Bạn có chắc?")
        $("#modal-body").html("Bạn có chắc chắn muốn xử lý khiếu nại này!")
        $('#myModal').modal('show')

        let debounce = null
        $("#modal-confirm").off().click(() => {
            clearTimeout(debounce)
            debounce = setTimeout(() => {
                $.ajax({
                    type: 'post',
                    contentType: 'application/x-www-form-urlencoded',
                    url: getLocation(hrefUrl)?.pathname,
                    xhrFields: {
                        withCredentials: true
                    },
                    data: {
                        id: params?.rid,
                        type: params?.type
                    },
                    success: (resp) => {
                        console.log(resp)
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

                        liveToast.on('hide.bs.toast', () => {
                            location.reload()
                        })
                    }
                })
            }, 1000)
        })
    })

    $("#adm-response").off().click((e) => {
        e.preventDefault()
        const hrefUrl = e?.currentTarget?.href;
        const postData = $("#adm-resp-form").serialize()
        console.log(postData);
        $("#modal-title").html("Bạn có chắc?")
        $("#modal-body").html("Bạn có chắc chắn muốn xử lý khiếu nại này!")
        $('#myModal').modal('show')

        let debounce = null
        $("#modal-confirm").off().click(() => {
            clearTimeout(debounce)
            debounce = setTimeout(() => {
                $.ajax({
                    type: 'post',
                    contentType: 'application/x-www-form-urlencoded',
                    url: getLocation(hrefUrl)?.pathname,
                    xhrFields: {
                        withCredentials: true
                    },
                    data: postData,
                    success: (resp) => {
                        console.log(resp)
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

                        liveToast.on('hide.bs.toast', () => {
                            location.reload()
                        })
                    }
                })
            }, 1000)
        })
    })
})