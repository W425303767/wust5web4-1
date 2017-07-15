
$(document).ready(function () {
    
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

      $('.auth').data({'s':0});

    $('#num_stu').blur(function(){
        val=this.value;
        if (isNaN(val)||val==0||!val.length){
            $('.error').show();              
        }
        else{
            $('.error').hide();
            $('.auth').data({'s':1});
        }
    });
    $('.btn-stu').click(function(){
        $('.auth').blur();

        tot=0;
        $('.auth').each(function(){
            tot+=$(this).data('s');
        });
        if(tot!=1){
            return false;
        }
    });

     $('.auth2').data({'s':0});

    $('#num_manage').blur(function(){
        val2=this.value;
        if (isNaN(val2)||val2==0||!val2.length){
            $('.error2').show();              
        }
        else{
            $('.error2').hide();
            $('.auth2').data({'s':1});
        }
    });
    $('.btn-mana').click(function(){
        $('.auth2').blur();

        tot2=0;
        $('.auth2').each(function(){
            tot2+=$(this).data('s');
        });
        if(tot2!=1){
            return false;
        }
    });

});