/**
 * Created by 涵小庆 on 2017/7/13.
 */
$(document).ready(function () {
    /*$("#academy").change(function () {
     $("#academy option").each(function (i,o) {
     if($(this).attr("selected")){
     $(".major").hide();
     $(".major").eq(i).show();
     }
     });
     });
     $("#academy").change();*/
    function check1() {
        $("#academy option").each(function () {
            if($(this).attr("selected")){
                var value=$(this).val();
                var num=$("#num_stu").val();
                $.ajax({
                    type:"post",
                    url:"",
                    dataType:"json",
                    data:{major:vlaue,num:num},
                    success:function (msg) {
                        location.href="../html/result.html";
                    }
                });
            }
        })
    }

    function check2() {
        $("#position option").each(function () {
            if($(this).attr("selected")){
                var val=$(this).val();
                var number=$("#num_manage").val();
                $.ajax({
                    type:"post",
                    url:"",
                    dataType:"json",
                    data:{val:val,number:number},
                    success:function(msg){
                        location.href="../html/result.html";
                    }
                });
            }
        });
    }
});