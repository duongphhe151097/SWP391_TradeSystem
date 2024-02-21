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
    })

    $(".remove-role").off().click((e) => {
        e.preventDefault();
        const hrefUrl = e?.currentTarget?.href;
        const rid = (new URL(hrefUrl)).searchParams.get("rid");
        const roleGroup = $("#role-group");

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
    })

    $("#confirm_add-role").off().click((e) => {
        const addRoleForm = $("#add-role");
        const postData = addRoleForm.serialize()
        const submitUrl = addRoleForm.attr("action")

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
    })
})