import {getLocation, getParams, urlBuilder} from "./common.js";

$(document).ready(() => {
    const disableConf = {
        lang: 'vi-VN',
        placeholder: 'Nhập nội dung vào đây',
        tabsize: 2,
        height: 200,
        disableGrammar: true,
        disableDragAndDrop: true,
        toolbar: []
    }

    const enableConf = {
        lang: 'vi-VN',
        placeholder: 'Nhập nội dung vào đây',
        tabsize: 2,
        minHeight: 200,
        height: 200,
        disableGrammar: true,
        disableDragAndDrop: true
    }

    const reportDescription = $("#report-description");
    reportDescription.summernote(disableConf)
    reportDescription.summernote('disable')

    const adminResponse = $("#admin-response");
    adminResponse.summernote(disableConf)
    adminResponse.summernote('disable')

    const sellerResponse = $("#seller-response");
    if (sellerResponse.hasClass("editor-disabled")) {
        sellerResponse.summernote(disableConf)
        sellerResponse.summernote('disable')
    } else {
        sellerResponse.summernote(enableConf)
    }

    const sellerReponseModal = $("#modal-seller-response");
    sellerReponseModal.summernote(enableConf)

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
                        type: 'ABORTED'
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

    $("#denied-report").off().click((e) => {
        e.preventDefault()
        const hrefUrl = e?.currentTarget?.href;
        const params = getParams(hrefUrl.split("?")[1]);

        $("#modal-title").html("Bạn có chắc?")
        $("#modal-body").html("Bạn có chắc chắn không đồng ý với khiếu nại này!")
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
                        type: 'DENIED'
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

    $("#accept-report").off().click((e) => {
        e.preventDefault()
        const hrefUrl = e?.currentTarget?.href;
        const params = getParams(hrefUrl.split("?")[1]);

        $('#report-acp-seller').modal('show')

        let debounce = null
        $("#report-acp-seller-modal-confirm").off().click(() => {
            clearTimeout(debounce)

            const resp = $("#modal-seller-response").summernote('code');
            debounce = setTimeout(() => {
                $.ajax({
                    type: 'post',
                    url: getLocation(hrefUrl)?.pathname,
                    xhrFields: {
                        withCredentials: true
                    },
                    data: {
                        id: params?.id,
                        type: 'ACCEPTED',
                        seller_resp: resp
                    },
                    success: (resp) => {
                        $('#report-acp-seller').modal('hide')

                        $('#toast-title').html('Thành công!')
                        $('#toast-body').html(resp.message)
                        const liveToast = $('#liveToast')
                        liveToast.toast('show')

                        liveToast.on('hide.bs.toast', () => {
                            location.reload()
                        })
                    },
                    error: (err) => {
                        $('#report-acp-seller').modal('hide')

                        $('#toast-title').html('Không thành công!')
                        $('#toast-body').html(err?.responseJSON?.message)
                        const liveToast = $('#liveToast')
                        liveToast.toast('show')
                    }
                })
            }, 1000)
        })
    })

    $("#request-admin").off().click((e) => {
        e.preventDefault()
        const hrefUrl = e?.currentTarget?.href;
        const params = getParams(hrefUrl.split("?")[1]);

        $("#modal-title").html("Bạn có chắc?")
        $("#modal-body").html("Bạn có chắc chắn muốn gọi admin và tốn 50.000đ phí tạm thời, nếu report đúng sẽ được hoàn trả lại số tiền!")
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
                        type: 'REQUEST_ADMIN'
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

                        if (err?.responseJSON?.message === "NOT_ENOUGH_MONEY") {
                            const url = urlBuilder("/payment/vnpay/create");

                            $("#modal-title").html("Không đủ tiền trong tài khoản?")
                            $("#modal-body").html(`Bạn không có đủ tiền trong tài khoản, vui lòng <a href="${url}">nạp thêm 50.000 đ</a> để tiếp tục!`)
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

    $("#acp-seller-response").off().click((e) => {
        e.preventDefault()
        const hrefUrl = e?.currentTarget?.href;
        const params = getParams(hrefUrl.split("?")[1]);

        $("#modal-title").html("Bạn có chắc?")
        $("#modal-body").html("Bạn có chắc chắn đồng ý với phản hồi của người bán!")
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
                        type: 'BUYER_ACCEPT_SELLER'
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