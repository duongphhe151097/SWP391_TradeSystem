import {urlBuilder} from "./common.js";

$(document).ready(() => {
    $(".a-ban-click").click((e) => {
        e.preventDefault();
        const hrefUrl = e?.currentTarget?.href;

        $("#modal-title").html("Chặn tài khoản!")
        $("#modal-body").html("Bạn có chắc chắn muốn chặn tài khoản!")
        $('#myModal').modal('show')

        $("#modal-confirm").off().click(() => {
            $.ajax({
                type: "get",
                url: hrefUrl,
                xhrFields: {
                    withCredentials: true
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
                    console.log(err)
                    $('#myModal').modal('hide')
                    $('#toast-title').html('Không thành công!')
                    $('#toast-body').html(err?.responseJSON?.message)
                    $('#liveToast').toast('show')
                }
            });
        })

    })
})