$(function(){
	$('.auth').data({'s':0});

	$('input[name=username]').blur(function(){
		val=this.value;
		if (isNaN(val)){
			$('.null1').hide();
			$('.error1').hide();
		 	$('.num').show();              
		}
		else{
			$('.num').hide();
			if (!val.length) {
				$('.null1').show();
			}
			else if (val.length<4||val.length>20) {
				$('.null1').hide();
				$(this).data({'s':0});
				$('.error1').show();
			}
			else {
				$('.null1').hide();
				$(this).data({'s':1});
				$('.error1').hide();
			}
		}
	});

	$('input[name=password]').blur(function(){
		val=this.value;
		if (!val.length) {
			$('.null2').show();
		}
		else if (val.length<6||val.length>20) {
			$('.null2').hide();
			$(this).data({'s':0});
			$('.error2').show();
		}
		else{
			$('.null2').hide();
			$(this).data({'s':1});
			$('.error2').hide();
		}
	});

	$('form').submit(function(){
		$('input.auth').blur();

		tot=0;
		$('.auth').each(function(){
			tot+=$(this).data('s');
		});
		if(tot!=2){
			return false;
		}
	});
});