var listElementsCounter = 0;


function addNodeProperties() {
  propertyName = document.getElementById("nodeProperties.propertyName").value;
  propertValue = document.getElementById("nodeProperties.value").value;
  //propertyGrain = $("#nodeProperties.grain :selected").;
  propertyGrainElement = document.getElementById("nodeProperties.grain");
  propertyGrain = propertyGrainElement.options[propertyGrainElement.selectedIndex].value;
  tableName = document.getElementById("nodeProperties.trafficTableName").value;

  threshold = document.getElementById("nodeProperties.notificationThreshold").value;
  nodeIDValue = document.getElementById("nodeID").value;

  var valid = validateNodeProperties();

  if (valid) {
    $("#propertiesTable").css("display", "block");
    propertyNameInputName = 'nodePropertiesList[' + listElementsCounter + '].propertyName';
    propertyNameInputId = 'nodePropertiesList' + listElementsCounter + '.propertyName';

    propertyValueInputName = 'nodePropertiesList[' + listElementsCounter + '].value';
    propertyValueInputId = 'nodePropertiesList' + listElementsCounter + '.value';

    propertyGrainInputName = 'nodePropertiesList[' + listElementsCounter + '].grain';
    propertyGrainInputId = 'nodePropertiesList' + listElementsCounter + '.grain';

    propertyTrafficTableNameId = 'nodePropertiesList' + listElementsCounter + '.trafficTableName';
    propertyTrafficTableNameName = 'nodePropertiesList[' + listElementsCounter + '].trafficTableName';

    notificationThresholdId = 'nodePropertiesList' + listElementsCounter + '.notificationThreshold';
    notificationThresholdName = 'nodePropertiesList[' + listElementsCounter + '].notificationThreshold';

    $("#propertiesTable").append('<tr class="EditableRow">' +
      '<td class="firstChild EditableField">' +
      '<span>' + propertyName + '</span>' +
      ' <input type="text" name="' + propertyNameInputName + '" id="' + propertyNameInputId + '" value="' + propertyName + '" class="CustomInput" maxlength="50"> </td>' +
      '<td class="EditableField">' +
      '<span>' + propertValue + '</span>' +
      ' <input type="text" name="' + propertyValueInputName + '" id="' + propertyValueInputId + '" value="' + propertValue + '" class="CustomInput" maxlength="10"> </td>' +
      '<td class="EditableField"> ' +
      '<span>' + propertyGrain + '</span>' +
      '<input type="text"  value="' + propertyGrain + '" class="CustomInput" disabled>' + '<input type="hidden" name="' + propertyGrainInputName + '" id="' + propertyGrainInputId + '" value="' + propertyGrain + '" > </td> ' +
      '<td class="EditableField">' +
      '<span>' + tableName + '</span>' +
      ' <input type="text" name="' + propertyTrafficTableNameName + '" id="' + propertyTrafficTableNameId + '" value="' + tableName + '" class="CustomInput" maxlength="30"> </td>' +

      '<td class="EditableField"> ' +
      '<span>' + threshold + '</span>' +
      '<input type="text" name="' + notificationThresholdName + '" id="' + notificationThresholdId + '" value="' + threshold + '" class="CustomInput" maxlength="10"> </td>' +

      '<td >' +
      '<a class="DeleteUser" href="#Delete"> Delete</a>' +
      '</td></tr>');

    ++listElementsCounter;
    attachApplicationClasses();

    bindDeleteClickHandler();

    $("table.tablesorter").tablesorter({
      widgets:['zebra', 'repeatHeaders']
    });
  }
  else {

  }
}

function bindDeleteClickHandler() {
  $(".DeleteUser").unbind('click').click(function () {

    $(this).parent().parent('.EditableRow').remove();
    $(".nodePropertiesErrorDiv").hide();
    --listElementsCounter;
    if (listElementsCounter == 0) {
      $("#propertiesTable").css("display", "none");
    }
    else {
      $("#propertiesTable").css("display", "block");
    }
  });
}

function validateNodeProperties() {

  var validName = validateTextInputField(propertyName, $("#propertyNameError"));
  if (validName) {
    validName = validateTextNotContainigSpecialCharacters(propertyName, $("#propertyNameError"));
  }

  var validTableName = validateTextInputField(tableName, $("#trafficTableNameError"));
  if (validTableName) {
    validTableName = validateTextNotContainigSpecialCharacters(tableName, $("#trafficTableNameError"));
  }

  var validThreshold = (validateNumericValue(threshold, $("#notificationThresholdError"))
  && validateThresholdField(threshold, $("#notificationThresholdError")));

  var validPropertValue = validateNumericValue(propertValue, $("#valueError"));


  var validPropertyGrain = validateDropDownListSelection(propertyGrain, $("#grainError"));

  validInputs = (validName && validTableName && validThreshold && validPropertValue && validPropertyGrain);
  return validInputs;
}
function validateNumericValue(inputField, errorField) {
  var re = /^[0-9]+[.(0-9)+]*$/;
  if(inputField == null || inputField == ''){
      errorField.addClass("error");
      errorField.text("This field is required and its value should be numeric!");

      return false;

  }else if (!re.test(inputField)) {

    errorField.addClass("error");
    errorField.text("This field is required and its value should be numeric!");

    return false;
  }
  else {
    errorField.text("");
    errorField.removeClass("error");

    return true;
  }
}
function validateTextNotContainigSpecialCharacters(inputField, errorField) {

  var re = /^[a-z0-9,\.,_]+$/i;
  if (!re.test(inputField)) {
    errorField.addClass("error");
    errorField.text("This field cannot accept special characters");
    return false;
  }
  else {
    errorField.text("");
    errorField.removeClass("error");
    return true;
  }
}

function validateDropDownListSelection(inputField, errorField) {

  if (inputField == "-1") {
    errorField.addClass("error");
    errorField.text("This field is required !");
    return false;
  }
  else {
    errorField.text("");
    errorField.removeClass("error");

    return true;
  }

}

function validateThresholdField(inputField, errorField) {
  //if it's NOT valid
  if (inputField != null) {
    if (inputField < 0 || inputField > 100) {

      errorField.addClass("error");
      errorField.text("This field should be between 0 and 100!");

      return false;
    }
    //if it's valid
    else {

      errorField.text("");
      errorField.removeClass("error");

      return true;
    }
  }
  else {
    errorField.addClass("error");
    errorField.text("This field should be between 0 and 100!");
    return false;
  }


}

function validateTextInputField(inputField, errorField) {
  //if it's NOT valid
  if (inputField != null) {
    if (inputField.length < 1) {

      errorField.addClass("error");
      errorField.text("This field is required and it's min length should be 1!");

      return false;
    }
    //if it's valid
    else {

      errorField.text("");
      errorField.removeClass("error");

      return true;
    }
  }
  else {
    errorField.addClass("error");
    errorField.text("This field is required and it's min length should be 1!");
    return false;
  }


}

function deleteRow(deleteCell) {
  $(deleteCell).parent().parent('.EditableRow').remove();
}