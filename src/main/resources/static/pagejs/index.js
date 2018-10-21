$(function() {

    $("#index_refresh").on("click", refresh);
    $("#index_create").on("click", create_page);

    // 刷新页面
    function refresh() {
        location.href = "/index";
    }

    // 创建页面
    function create_page() {
        location.href = "/create";
    }

});