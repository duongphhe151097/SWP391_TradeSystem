$(document).ready(() => {
    $("#refresh-balance").off().click((e) => {
        e.preventDefault()
        const hrefUrl = e?.currentTarget?.href;

        $.ajax({
            type: "get",
            url: hrefUrl,
            xhrFields: {
                withCredentials: true
            },
            success: (resp) => {
                if(resp.code === 200){
                    $("#balance-amount").html(`Số dư: ${resp.message}đ`)
                }
            },
            error: (err) => {
                console.log(err)
            }
        });
    })
})