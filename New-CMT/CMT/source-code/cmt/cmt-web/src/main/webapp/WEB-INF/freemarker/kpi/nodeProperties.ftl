[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]


<h2>Node Properties </h2>
[@cmt.loadingTxt/]
<table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable" id="propertyForm">


    [@spring.formHiddenInput "formBean.nodeID"/]
    <tr>
        <td class="Labeltd"><label for="">Property Name </label> <span class="RequiredField">*</span></td>
        <td class="inputltd">
            [@spring.formInput path="formBean.nodeProperties.propertyName" attributes=" maxlength=\"20\" class=\"required error CustomInput \" " /]
            <label id="propertyNameError" class="propertyNameError"></label>
        </td>
    </tr>
    <tr>
        <td class="Labeltd"><label for="">Property Value </label> <span class="RequiredField">*</span></td>
        <td class="inputltd">
            [@spring.formInput path="formBean.nodeProperties.value" attributes=" maxlength=\"10\" class=\"required error CustomInput \"  /" /]
            <label id="valueError" class="valueError"></label>
        </td>
    </tr>
    <tr>
        <td class="Labeltd"><label>Grain </label> <span class="RequiredField">*</span></td>
        <td class="inputltd">
            [@spring.formSingleSelect path="formBean.nodeProperties.grain" options=grain attributes="class=\"cd-select required error \" " /]
            <label id="grainError"></label>
        </td>
    </tr>

    <tr>
        <td class="Labeltd"><label for="">Traffic Table Name </label> <span class="RequiredField">*</span></td>
        <td class="inputltd">
            [@spring.formInput path="formBean.nodeProperties.trafficTableName" attributes=" maxlength=\"30\" class=\"required error CustomInput \"" /]
            <label id="trafficTableNameError"></label>
        </td>
    </tr>
    <tr>
        <td class="Labeltd"><label for="">Notification Threshold</label> <span class="RequiredField">*</span><br>(Percentage)</td>
        <td class="inputltd">
            [@spring.formInput path="formBean.nodeProperties.notificationThreshold" attributes=" maxlength=\"10\" class=\"required error CustomInput \"" /]
            <label id="notificationThresholdError"></label>
        </td>
    </tr>
    <tr>
        <td class="Labeltd"></td>
        <td class="inputltd">
              <span class="SubmitButtons AddTableRow3">
              								<span class="SubmitButton "> <a href="#Add" onClick="addNodeProperties();"> add node
                                                Property</a> </span>
              							</span>
        </td>
    </tr>

</table>

[@spring.bind path="formBean.nodePropertiesList"/]
[#assign validationError][@spring.showErrors "<br>" "error"/][/#assign]


[#assign hideErrorsList]false[/#assign]
[#if (errorsList?? && errorsList?has_content && errorsList?size > 0) ]
[#list errorsList as objectError]
[#if objectError.field == "systemName" || objectError.field == "nodeID" || objectError.field == "nodePropertiesList"]
[#assign hideErrorsList]true[/#assign]
[/#if]
[/#list]
[/#if]

[#if (errorsList?? && errorsList?has_content && hideErrorsList=='false') || (validationError?? && validationError?has_content)]

<div class="CustomContent3 ErrorMessages nodePropertiesErrorDiv">
    <h4>Error Messages </h4>

    <table>
        [#list errorsList as objectError]
        [#if objectError.field != "systemName" && objectError.field != "nodeID" && objectError.field != "nodePropertiesList"]
        <tr>

            [#assign msg][@spring.messageText code="${objectError.code}"  text="${objectError.defaultMessage}"/][/#assign]
            [#assign rowIndex][@cmt.getItemIndex itemName="${objectError.field}" /][/#assign]
            [#assign columnName][@cmt.getColumnName itemName="${objectError.field}"  /][/#assign]

            <td style="color: #ff0000; font-size: 14px;">
                <p> At Row# ${rowIndex} and ${columnName}
                    column [@spring.messageText "${objectError.code}"  "${msg}" /] </p>
            </td>


        </tr>
        [/#if]
        [/#list]
        [#if validationError?? && validationError?has_content]
        <tr>
            <td style="color: #ff0000; font-size: 14px;">
                <p>${validationError}</p>
            </td>
        </tr>
        [/#if]
    </table>
</div>

[/#if]
[#--[@spring.showErrors "<br>" "validationerror"/]--]

<div class="Grid SmallGrid fixedGrid" style="padding:10px 0">
    <table width="100%" cellspacing="0" cellpadding="0" border="0" class="tablesorter NodePropertyTable"
           id="propertiesTable"
    [#if formBean.nodePropertiesList??]
    [#if formBean.nodePropertiesList?has_content]
    style="display: block;"
    [#else]
    style="display: none;"
    [/#if]
    [/#if]>
    <col style="width:15%">
    <col style="width:20%">
    <col style="width:5%">
    <col style="width:25%">
    <col style="width:30%">
    <col style="width:5%">

    <thead>
    <tr class="TableHeader">
        <th> Property</th>
        <th> Property Value</th>
        <th> Grain</th>
        <th> Traffic Table</th>
        <th> Notification Threshold %</th>
        <th> Actions</th>
    </tr>
    </thead>

    <tbody>
    [#if formBean.nodePropertiesList??]
    [#if formBean.nodePropertiesList?has_content]
    [#list formBean.nodePropertiesList as nodeProperties]


    <tr class="EditableRow">
        <td class="EditableField">

            <span>${nodeProperties.propertyName}</span>

            [@spring.formInput path="formBean.nodePropertiesList[${nodeProperties_index}].propertyName" attributes="class=\"CustomInput \" maxlength=\"50\"" /]
            [@spring.bind path="formBean.nodePropertiesList[${nodeProperties_index}].propertyName"/]

        </td>
        <td class="EditableField">

            <span>${nodeProperties.value?c}</span>
            [@spring.formInput path="formBean.nodePropertiesList[${nodeProperties_index}].value" attributes="class=\"CustomInput \" maxlength=\"10\"" /]
            [@spring.bind path="formBean.nodePropertiesList[${nodeProperties_index}].value"/]

        </td>
        <td class="EditableField">

            <span>${nodeProperties.grain}</span>
            [@spring.formInput path="formBean.nodePropertiesList[${nodeProperties_index}].grain" attributes="class=\"CustomInput \" disabled" /]
            [@spring.formHiddenInput "formBean.nodePropertiesList[${nodeProperties_index}].grain"/]
            [@spring.bind path="formBean.nodePropertiesList[${nodeProperties_index}].grain"/]

        </td>
        <td class="EditableField">

            <span>${nodeProperties.trafficTableName}</span>
            [@spring.formInput path="formBean.nodePropertiesList[${nodeProperties_index}].trafficTableName" attributes="class=\"CustomInput \" maxlength=\"30\"" /]
            [@spring.bind path="formBean.nodePropertiesList[${nodeProperties_index}].trafficTableName"/]

        </td>
        <td class="EditableField">

            <span>${nodeProperties.notificationThreshold?c}</span>
            [@spring.formInput path="formBean.nodePropertiesList[${nodeProperties_index}].notificationThreshold" attributes="class=\"CustomInput \" maxlength=\"10\" " /]
            [@spring.bind path="formBean.nodePropertiesList[${nodeProperties_index}].notificationThreshold"/]

        </td>
        <td>
            <a class="DeleteUser" href="#Delete">
                Delete</a>
        </td>
    </tr>

    <script>
        listElementsCounter++;
    </script>


    [/#list]
    [/#if]
    [/#if]
    </tbody>

    </table>
</div>