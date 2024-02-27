import {urlBuilder} from "./common.js";
$(document).ready(() => {
    let canEdit = true;

    $("#edit").click(() => {
        if (canEdit === false) {
            canEdit = true
            $("#edit-save").addClass("hidden-item")
            $(".can-edit").prop("disabled", true)
        } else {
            canEdit = false
            $("#edit-save").removeClass("hidden-item")
            $(".can-edit").prop("disabled", false)
        }
    })

    $("#edit-save").off().click((e) => {
        e.preventDefault()
        const hrefUrl = e?.currentTarget?.href;
        const postData = $("#user-info").serialize()

        $("#modal-title").html("Cập nhật thông tin!")
        $("#modal-body").html("Bạn có chắc chắn muốn cập nhật những thông tin đã thay đổi!")
        $('#myModal').modal('show')

        let debounce = null
        $("#modal-confirm").off().click((e) => {
            clearTimeout(debounce)
            debounce = setTimeout(() => {
                $.ajax({
                    type: "post",
                    url: hrefUrl,
                    data: postData,
                    xhrFields: {
                        withCredentials: true
                    },
                    success: (resp) => {
                        console.log(resp)
                        $('#myModal').modal('hide')
                        $('#toast-title').html('Thành công!')
                        $('#toast-body').html(resp.message)
                        const liveToast = $('#liveToast')
                        canEdit = true
                        $("#edit-save").addClass("hidden-item")
                        liveToast.toast('show')
                    },
                    error: (err) => {
                        console.log(err)
                        $('#myModal').modal('hide')
                        $('#toast-title').html('Không thành công!')
                        $('#toast-body').html(err?.responseJSON?.message)
                        canEdit = true
                        $("#edit-save").addClass("hidden-item")
                        $('#liveToast').toast('show')
                    }
                });
            }, 1000)
        });
    })

    $(".remove-role").off().click((e) => {
        e.preventDefault();
        const hrefUrl = e?.currentTarget?.href;
        const rid = (new URL(hrefUrl)).searchParams.get("rid");
        const roleGroup = $("#role-group");

        $("#modal-title").html("Cập nhật thông tin!")
        $("#modal-body").html("Bạn có chắc chắn muốn xóa!")
        $('#myModal').modal('show')

        let debounce = null
        $("#modal-confirm").off().click((e) => {
            clearTimeout(debounce)
            debounce = setTimeout(() => {
                $.ajax({
                    type: "get",
                    url: hrefUrl,
                    xhrFields: {
                        withCredentials: true
                    },
                    success: (resp) => {
                        console.log(resp)

                        if (rid) {
                            const removeEle = roleGroup.children().filter(`[data-rid="${rid}"]`);
                            removeEle.remove();
                        }

                        $('#myModal').modal('hide')
                        $('#toast-title').html('Thành công!')
                        $('#toast-body').html(resp.message)
                        const liveToast = $('#liveToast')
                        liveToast.toast('show')
                    },
                    error: (err) => {
                        console.log(err)
                        $('#myModal').modal('hide')
                        $('#toast-title').html('Không thành công!')
                        $('#toast-body').html(err?.responseJSON?.message)
                        $('#liveToast').toast('show')
                    }
                });
            }, 1000)
        })
    })

    $("#confirm_add-role").off().click((e) => {
        const addRoleForm = $("#add-role");
        const postData = addRoleForm.serialize()
        const submitUrl = addRoleForm.attr("action")

        $("#modal-title").html("Cập nhật thông tin!")
        $("#modal-body").html("Bạn có chắc chắn muốn thêm vài trò!")
        $('#myModal').modal('show')

        let debounce = null
        $("#modal-confirm").off().click((e) => {
            clearTimeout(debounce)
            debounce = setTimeout(() => {
                const inputSelected03 = $("#inputGroupSelect03");
                const rName = inputSelected03.find(":selected").text();
                const rid = inputSelected03.find(":selected").val();
                const uid = $('input[name="uid"]').val();


                $.ajax({
                    type: "post",
                    url: submitUrl,
                    data: postData,
                    xhrFields: {
                        withCredentials: true
                    },
                    success: (resp) => {
                        console.log(resp)
                        $('#myModal').modal('hide')
                        $('#toast-title').html('Thành công!')
                        $('#toast-body').html(resp.message)

                        $('#role-group')
                            .append(`<li class="list-group-item" data-rid="${uid}">
                                                            <div class="d-flex justify-content-between align-items-center">
                                                                <span class="mr-2">${rName}</span>
                                                                <a class="btn btn-outline-danger remove-role" href="${urlBuilder("/admin/account/role?uid=${uid}&rid=${rid}&type=remove")}" role="button">X</a>
                                                            </div>
                                                        </li>`)

                        const liveToast = $('#liveToast')
                        liveToast.toast('show')
                    },
                    error: (err) => {
                        console.log(err)
                        $('#myModal').modal('hide')
                        $('#toast-title').html('Không thành công!')
                        $('#toast-body').html(err?.responseJSON?.message)
                        $('#liveToast').toast('show')
                    }
                });
            }, 1000)
        })
    })

    $("#cp-bsession").off().click((e) => {
        e.preventDefault();
        const hrefUrl = e?.currentTarget?.href;

        $("#modal-title").html("Cập nhật thông tin!")
        $("#modal-body").html("Bạn có chắc chắn hủy session hiện tại!")
        $('#myModal').modal('show')

        let debounce = null
        $("#modal-confirm").off().click((e) => {
            clearTimeout(debounce)
            debounce = setTimeout(() => {
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
                    },
                    error: (err) => {
                        console.log(err)
                        $('#myModal').modal('hide')
                        $('#toast-title').html('Không thành công!')
                        $('#toast-body').html(err?.responseJSON?.message)
                        $('#liveToast').toast('show')
                    }
                });
            }, 1000)
        })
    })
})