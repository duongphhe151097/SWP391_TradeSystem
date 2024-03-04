const BASE_URL = "";

const urlBuilder = (router) => {
    return BASE_URL + router
}

const formatCurrency = (value) => {
    try {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)
    } catch (err) {
        return 0;
    }
}

export {urlBuilder, formatCurrency, BASE_URL}
