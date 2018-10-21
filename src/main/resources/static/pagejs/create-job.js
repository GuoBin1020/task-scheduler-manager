$(function () {

    // 是否延迟
    $("#is_delay").on("change", function () {
        var flag = $(this).is(":checked");
        var $delay_time = $("#delay_time").parent().parent();
        if (flag) {
            $delay_time.removeClass("hidden");
            $delay_time.addClass("show");
        } else {
            $delay_time.removeClass("show");
            $delay_time.addClass("hidden");
        }
    });

    // 执行方式
    $("input[name='time_type']").on("change", function () {
        var flag = $(this).val();
        var $interval_time = $("#interval_time").parent().parent();
        var $cron_expression = $("#cron_expression").parent().parent();
        if (flag === "1") {
            // 周期显示
            $interval_time.removeClass("hidden");
            $interval_time.addClass("show");
            // 表达式隐藏
            $cron_expression.removeClass("show");
            $cron_expression.addClass("hidden");
        } else if (flag === "2") {
            // 周期隐藏
            $interval_time.removeClass("show");
            $interval_time.addClass("hidden");
            // 表达式显示
            $cron_expression.removeClass("hidden");
            $cron_expression.addClass("show");
        }
    });

    //设置无穷大
    $("#default_count").on("click", function () {
        $("#execute_count").val("∞");
    });

});