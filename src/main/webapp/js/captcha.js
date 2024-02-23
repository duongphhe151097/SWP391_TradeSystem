import {urlBuilder, BASE_URL} from "./common.js";

// const BASE_URL = "/tradesys_war_exploded"
$(document).ready(() => {
    $.ajax({
        url: urlBuilder("/captcha"),
        type: 'get',
        success: (response) => {
            console.log(response)
            let captchaDiv = $("#captcha")
            captchaDiv
                .append(`<input type=\"hidden\" value=\"${response.captchaId}" name=\"hidden_id\">`)
                .append(`<img src=\"${response.captchaImg}\" alt=\"Captcha\">`);
        }
    })
})