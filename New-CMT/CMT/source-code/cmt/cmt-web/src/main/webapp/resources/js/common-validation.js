function removeReservedChars(inputField) {

			var text = $(inputField).val();
			text = text.replace(/\"/g,'');
			text = text.replace(/\'/g,'');
			text = text.replace(/\>/g,'');
			text = text.replace(/\</g,'');
			$(inputField).val(text);
  }

function validateTextNotContainigSpecialCharactersOnSubmit(inputField, errorField) {
  var re = /^[a-z0-9_\s-]+$/i;
 if($(inputField).val() != null && $(inputField).val().length >0){
		if (!re.test($(inputField).val())) {
		    $(errorField).addClass("error");
		    $(errorField).text("This field cannot accept special characters");
		   //$(inputField).val('');
		   removeReservedChars(inputField);
		    return false;
		  }
		  else {
		    $(errorField).text("");
		    $(errorField).removeClass("error");
		    return true;
		  }
  }
  else
  {
		 return true;
  }
}

function reservedCharsError(inputField,errorField) {
	//alert('replaceQuets ' +$(inputField).val());
	var re =/(?=(\>|\<|\"|\'))/;
	if($(inputField).val() != null && $(inputField).val().length >0){
		if (re.test($(inputField).val())) {
		    $(errorField).addClass("error");
		    $(errorField).text("Reserved characters \(\' \" \> \< \) cannot be used ");
			removeReservedChars(inputField);
		    return false;
		  }
		  else {
		    $(errorField).text("");
		    $(errorField).removeClass("error");
		    return true;
		  }
  }
  else
  {
		 return true;
  }



}