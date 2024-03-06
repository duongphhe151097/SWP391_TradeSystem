const BASE_URL = "/tradesys_war_exploded";

const urlBuilder = (router) => {
    return BASE_URL + router
}

const formatCurrency = (value) => {
    return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
}

export {urlBuilder, formatCurrency, BASE_URL}
