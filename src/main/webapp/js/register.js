
$(document).ready(function () {

    /**
     * Posts the selected option together with the wanted amount of accounts.
     */
    function submitSelection(selectId, numId, optionKey, numKey) {
        $("#" + selectId + " option").each(function () {
            if($(this).attr("selected")){
                var data = {};
                data[optionKey] = $(this).val();
                data[numKey] = $("#" + numId).val();
                $.ajax({
                    type:"post",
                    url:"",
                    dataType:"json",
                    data:data,
                    success:function (msg) {
                        location.href="../html/result.html";
                    }
                });
            }
        });
    }

    /**
     * Refuses empty, zero and non numeric amounts before the form is sent.
     */
    function validateAmount(numId, authClass, errorClass, buttonClass) {
        $('.' + authClass).data({'s':0});

        $('#' + numId).blur(function(){
            var val=this.value;
            if (isNaN(val)||val==0||!val.length){
                $('.' + errorClass).show();
            }
            else{
                $('.' + errorClass).hide();
                $('.' + authClass).data({'s':1});
            }
        });

        $('.' + buttonClass).click(function(){
            $('.' + authClass).blur();

            var tot=0;
            $('.' + authClass).each(function(){
                tot+=$(this).data('s');
            });
            if(tot!=1){
                return false;
            }
        });
    }

    function check1() {
        submitSelection("academy", "num_stu", "major", "num");
    }

    function check2() {
        submitSelection("position", "num_manage", "val", "number");
    }

    validateAmount("num_stu", "auth", "error", "btn-stu");
    validateAmount("num_manage", "auth2", "error2", "btn-mana");
});
