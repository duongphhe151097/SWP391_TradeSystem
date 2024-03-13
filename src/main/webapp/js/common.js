const BASE_URL = "/tradesys_war_exploded";

const urlBuilder = (router) => {
    return BASE_URL + router
}

const formatCurrency = (value) => {
    return value.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'})
}

const formatVnPayTime = (value) => {
    const year = value.substring(0, 4);
    const month = value.substring(4, 6);
    const day = value.substring(6, 8);
    const hour = value.substring(8, 10);
    const minute = value.substring(10, 12);
    const second = value.substring(12, 14);

    return `${day}/${month}/${year} ${hour}:${minute}:${second}`;
}

const formatVnPayTxnRef = (value) => {
    const data = [
        value.substring(0, 8),
        value.substring(8, 12),
        value.substring(12, 16),
        value.substring(16, 20),
        value.substring(20)
    ];

    // Join the groups with hyphens
    return data.join('-');
}

const formatVnPayStatus = (value) => {
    switch (value) {
        case "00":
            return "Thành công"

        case "01":
            return "Không thành công"

        case "08":
            return "Không thành công"

        default:
            return "Không rõ"
    }
}

const getParams = (url) => {
    return new Proxy(new URLSearchParams(url), {
        get: (searchParams, prop) => searchParams.get(prop),
    });
}

const getLocation = (href) => {
    let l = document.createElement("a");
    l.href = href;
    return l;
};

export {
    urlBuilder,
    formatVnPayTime,
    formatVnPayTxnRef,
    formatVnPayStatus,
    formatCurrency,
    getParams,
    getLocation,
    BASE_URL
}
