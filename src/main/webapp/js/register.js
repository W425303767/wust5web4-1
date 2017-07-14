$(document).ready(function () {
    $("#academy").change(function () {
        $("#academy option").each(function (i,o) {
            if($(this).attr("selected")){
                $(".major").hide();
                $(".major").eq(i).show();
            }
        });
    });
    $("#academy").change();
});