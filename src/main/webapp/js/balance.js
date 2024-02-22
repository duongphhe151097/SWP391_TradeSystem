$(document).ready(() => {
    let debounce = null
    $("#refresh-balance").off().click((e) => {
        e.preventDefault()
        clearTimeout(debounce)
        const hrefUrl = e?.currentTarget?.href;

        debounce = setTimeout(() => {
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
        }, 1000)

    })
})