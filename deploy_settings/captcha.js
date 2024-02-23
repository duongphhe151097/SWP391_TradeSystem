
const BASE_URL = ""
$(document).ready(() => {
    $.ajax({
        url: `${BASE_URL}/captcha`,
        type: 'get',
        success: (response) => {
            console.log(response)
            let captchaDiv = $("#captcha")
            captchaDiv.html(`<input type=\"hidden\" value=\"${response.captchaId}" name=\"hidden_id\">\n` +
                `<img src=\"${response.captchaImg}\" alt=\"Captcha\">`)
        }
    })
})