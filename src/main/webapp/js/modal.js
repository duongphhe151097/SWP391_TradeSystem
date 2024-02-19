let MODAL_TITLE = "title";
let MODAL_BODY = "body"

$(document).ready(() => {
    $("body")
        .append("<div class=\"modal\" tabindex=\"-1\" id=\"myModal\">")
        .append("<div class=\"modal-dialog\">")
        .append("<div class=\"modal-content\">")
        .append("<div class=\"modal-header\">")
        .append(`<h5 class=\"modal-title\">${MODAL_TITLE}</h5>`)
        .append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">")
        .append("<span aria-hidden=\"true\">&times;</span>")
        .append("</button>")
        .append("</div>")
        .append("<div class=\"modal-body\">")
        .append(`<p>${MODAL_BODY}</p>`)
        .append("</div>")
        .append("<div class=\"modal-footer\">")
        .append("<button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\">Close</button>")
        .append("<button type=\"button\" class=\"btn btn-primary\">Save changes</button>")
        .append("</div>")
        .append("</div>")
        .append("</div>")
        .append("</div>");


})

const modalConfig = () => {

}

export {modalConfig}