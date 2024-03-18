$(document).ready(function() {
    $('#searchForm').submit(function(event) {
        event.preventDefault();
        searchProducts();
    });
});

function searchProducts() {
    var keyword = $('#keyword').val();
    var minPrice = $('#minPrice').val();
    var maxPrice = $('#maxPrice').val();

    $.ajax({
        type: 'GET',
        url: 'search',
        data: {
            keyword: keyword,
            minPrice: minPrice,
            maxPrice: maxPrice
        },
        success: function(products) {
            displayProducts(products);
        },
        error: function(xhr, status, error) {
            console.error('Lỗi khi tìm kiếm sản phẩm:', error);
        }
    });
}

function displayProducts(products) {
    var productTable = $('#productBody');
    productTable.empty();

    $.each(products, function(index, product) {
        var row = $('<tr>');
        row.append('<td>' + product.id + '</td>');
        row.append('<td>' + product.status + '</td>');
        row.append('<td>' + product.intermediary + '</td>');
        row.append('<td>' + product.contactMethod + '</td>');
        row.append('<td>' + product.price + '</td>');
        row.append('<td>' + product.totalFee + '</td>');
        row.append('<td>' + product.createTime + '</td>');
        row.append('<td>' + product.lastUpdateTime + '</td>');
        productTable.append(row);
    });
}
