[#ftl/]
[#import "../../spring.ftl" as spring /]
[#import "../../macros.ftl" as cmt /]
[#import "wizard-macros.ftl" as wizard /]
[#global title="Capacity Management Tool"/]
[#global isDCSelected="selected"/]

[@cmt.htmlHead ""/]
[@cmt.navigation/]

<script src="[@spring.url '/js/data-collection.js'/]" type="text/javascript"></script>

<script>

    $(document).ready(function () {

    $('.AddSqlExpressionTableRow').click(function () {
    if ($('.AddSqlExpressionTableRow').attr('disabled') != 'disabled') {
    $(this).parent().prev().find('table').append('<tr class="NewRow"><td class="firstChild"> <input type="text" name="defineSQLColumnsFormBean.sourceColumn.name" id="defineSQLColumnsFormBean.sourceColumn.name" class="CustomInput specialChars"> </td><td> <select class="CustomSelect" name="defineSQLColumnsFormBean.sourceColumn.type" id="defineSQLColumnsFormBean.sourceColumn.type">[#list dataCollectionWizardFormBean.defineSQLColumnsFormBean.dataTypes?keys as type]<option value="${type}">${dataCollectionWizardFormBean.defineSQLColumnsFormBean.dataTypes[type].description}</option>[/#list]</select></td><td> <input type="text" name="defineSQLColumnsFormBean.sourceColumn.defaultValue" id="defineSQLColumnsFormBean.sourceColumn.defaultValue" class="CustomInput specialChars"> </td><td> <input type="text" name="defineSQLColumnsFormBean.sourceColumn.sqlExpression" id="defineSQLColumnsFormBean.sourceColumn.sqlExpression" class="CustomInput" maxlength="500"> </td><td class="lastChild"><a href="#" class="UpdateAction" onclick="validateAndNavigat(\'[@spring.url '/dataCollection/define/sqlColumns/addColumn'/]\');"> Update</a><a href="#" onclick="cancelAddSqlExpressionTableRow();" class="CancelAction"> Cancel</a></td></tr>');
    $('.AddSqlExpressionTableRow').attr('disabled', 'disabled');
    }
    });

    [#if dataCollectionWizardFormBean.defineSQLColumnsFormBean.sourceColumn?has_content]
    $('.AddSqlExpressionTableRow').attr('disabled', 'disabled');
    [/#if]




    }

    );

    function validateAndNavigat(url){
    //alert('validateAnNavigat');
    var isvalidate=$("#myform").valid();
    //alert(isvalidate);
    if(isvalidate){
    navigateTo( url, 'POST', 'myform');
    }
    }
    function cancelAddSqlExpressionTableRow() {
    $('.AddSqlExpressionTableRow').parent().prev().find('table').each(function () {
    if ($('tbody', this).length > 0) {
    $('tbody tr:last', this).remove();
    } else {
    $('tr:last', this).remove();
    }
    });

    $('.AddSqlExpressionTableRow').removeAttr('disabled');
    }


    function checkMaxLength(textareaID) {
    maxLength = $(textareaID).attr("maxlength");
    currentLengthInTextarea = $(textareaID).val().length;

    if (currentLengthInTextarea > (maxLength)) {

    // Trim the field current length over the maxlength.
    $(textareaID).val($(textareaID).val().slice(0, maxLength));


    }
    }
</script>

[@wizard.body]
[@cmt.loadingTxt/]
<form id="myform" method="POST" action="javascript:void(0);">

[@wizard.defineDataCollectionMappingFormBeanHiddenFields/]
[@wizard.defineOutputTableHiddenFields/]
[@wizard.dataCollectionWizardHiddenFields/]
[@wizard.dataCollectionTypeHiddenFields/]
[@wizard.extractXMLSourceColumnsHiddenFields/]
[#-- Extract Source --]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.fileName"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.sampleLines"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.delimiter"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.header"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.otherDelimiter"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.ignoreLines"/]

[#-- Define Plain Columns --]
[#assign selectedColumn = 0/]
[#list dataCollectionWizardFormBean.extractSourceColumnFormBean.columns as column]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].selected"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].sampleValue"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].name"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].defaultValue"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].type"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].customType"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].outputColumnName"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.tableCreated"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].index"/]
[#if column.selected][#assign selectedColumn = selectedColumn + 1/][/#if]
[/#list]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.useDateColumn"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.dateColumnName"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.dateColumnPrecession"/]

[@spring.bind path="dataCollectionWizardFormBean.defineSQLColumnsFormBean.sourceColumn"/]
[#assign sourceColumnErrors][@spring.showErrors "<br>" "validationerror"/][/#assign]

[@spring.bind path="dataCollectionWizardFormBean.defineSQLColumnsFormBean.groupByClause"/]
[#assign groupByErrors][@spring.showErrors "<br>" "validationerror"/][/#assign]

[@spring.bind path="dataCollectionWizardFormBean.defineSQLColumnsFormBean.havingClause"/]
[#assign havingErrors][@spring.showErrors "<br>" "validationerror"/][/#assign]

[@spring.bind path="dataCollectionWizardFormBean.defineSQLColumnsFormBean.whereClause"/]
[#assign whereErrors][@spring.showErrors "<br>" "validationerror"/][/#assign]

[#if dataCollectionWizardFormBean.defineSQLColumnsFormBean.queryError?has_content]
[#assign queryError = dataCollectionWizardFormBean.defineSQLColumnsFormBean.queryError/]
[/#if]

<div class="CustomContent2">
    Data Collection Type:
    <span>${dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType.description}</span> <span
        class="Space50"></span> Generic Process Name:
    <span>${dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionName}</span>
</div>

<h2>Define SQL Columns </h2>
[#if sourceColumnErrors?has_content || queryError?has_content || groupByErrors?has_content || havingErrors?has_content || whereErrors?has_content]
<div class="CustomContent3 ErrorMessages">
    <h4>Error Message </h4>

    [#if sourceColumnErrors?has_content]
    <p>${sourceColumnErrors}</p>
    [/#if]

    [#if queryError?has_content]
    <div style="word-wrap:break-word"><p>${queryError}</p></div>
    [/#if]

    [#if groupByErrors?has_content]
    <p>${groupByErrors}</p>
    [/#if]

    [#if havingErrors?has_content]
    <p>${havingErrors}</p>
    [/#if]

    [#if whereErrors?has_content]
    <p>${whereErrors}</p>
    [/#if]

</div>
[/#if]

<div class="CustomContent1">
    <h4>Instructions</h4>
    <a href="#d" class="Expandable">
        <span class="HideContent"> [ - ] Hide</span>
        <span class="ShowContent" style="display:none"> [ + ] Show</span>
    </a>

    <div class="ExpandableContent">
        <p>Please compose the SQL statements you need to manipulate the Data Collection output columns (optional).</p>
    </div>
</div>

<h2>Selected Extracted Columns ( ${selectedColumn} )</h2>

[#if selectedColumn > 0]
<div class="Grid SmallGrid">
    <table cellspacing="0" border="0" cellpadding="0" width="100%" class="FormTable">
        <thead>
        <tr>
            <th width="30%"> Name</th>
            <th width="40%"> Type</th>
            <th> Default value</th>
        </tr>
        </thead>
        <tbody>
        [#list dataCollectionWizardFormBean.extractSourceColumnFormBean.columns as column]
        [#if column.selected]
        <tr>
            <td>${column.name}</td>
            <td>${column.type.description}</td>
            <td>[#if column.defaultValue?has_content]${column.defaultValue}[/#if]</td>
        </tr>
        [/#if]
        [/#list]
        </tbody>
    </table>
</div>
<br>
[#else]
No columns were selected
[/#if]

<h2>SQL Expression Fields
    ( [#if dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns?has_content]${dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns?size}[#else]
    0[/#if] ) </h2>

<div class="Grid SmallGrid">
    <table cellspacing="0" border="0" cellpadding="0" width="100%" class=" ">
        <thead>
        <tr>
            <th width="26%"> Name</th>
            <th width="14%"> Type</th>
            <th width="26%"> Default value</th>
            <th width="26%"> Sql Expression</th>
            <th width="8%"> Actions</th>
        </tr>
        </thead>
        <tbody>
        [#if dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns?has_content]
        [#list dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns as expressionCol]
        <tr>
            <td><span>${expressionCol.name}</span>
                <input type="hidden"
                       name="defineSQLColumnsFormBean.sqlExpressionColumns[${expressionCol_index}].name"
                       id="defineSQLColumnsFormBean.sqlExpressionColumns[${expressionCol_index}].name"
                       value="${expressionCol.name}"
                       class="CustomInput"/>
                [@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${expressionCol_index}].selected"/]
                [@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${expressionCol_index}].outputColumnName"/]
				[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${expressionCol_index}].index"/]
            </td>
            <td>
                <span>${expressionCol.type.description} </span>
                <input type="hidden"
                       name="defineSQLColumnsFormBean.sqlExpressionColumns[${expressionCol_index}].type"
                       id="defineSQLColumnsFormBean.sqlExpressionColumns[${expressionCol_index}].type"
                       value="${expressionCol.type}"
                       class="CustomInput"/>
            </td>
            <td>
                <span>${expressionCol.defaultValue}</span>
                <input type="hidden"
                       name="defineSQLColumnsFormBean.sqlExpressionColumns[${expressionCol_index}].defaultValue"
                       id="defineSQLColumnsFormBean.sqlExpressionColumns[${expressionCol_index}].defaultValue"
                       value="${expressionCol.defaultValue}"
                       class="CustomInput"/>
            </td>
            <td>
                <span>${expressionCol.sqlExpression}  </span>
                <input type="hidden"
                       name="defineSQLColumnsFormBean.sqlExpressionColumns[${expressionCol_index}].sqlExpression"
                       id="defineSQLColumnsFormBean.sqlExpressionColumns[${expressionCol_index}].sqlExpression"
                       value="${expressionCol.sqlExpression}"
                       class="CustomInput"/>
            </td>
            <td>
                <a href="#" class="DeleteUser"
                   onclick="navigateTo('[@spring.url '/dataCollection/define/sqlColumns/deleteColumn?index=' + expressionCol_index/]' , 'POST', 'myform');">
                    Delete</a>
            </td>
        </tr>
        [/#list]
        [/#if]

        [#if dataCollectionWizardFormBean.defineSQLColumnsFormBean.sourceColumn?has_content]
        <tr class="NewRow">
            <td class="firstChild">
                <input type="text" name="defineSQLColumnsFormBean.sourceColumn.name"
                       id="defineSQLColumnsFormBean.sourceColumn.name"
                       value="${dataCollectionWizardFormBean.defineSQLColumnsFormBean.sourceColumn.name}"
                       class="CustomInput">
            </td>
            <td>
                <select class="CustomSelect" name="defineSQLColumnsFormBean.sourceColumn.type"
                        id="defineSQLColumnsFormBean.sourceColumn.type">
                    [#list dataCollectionWizardFormBean.defineSQLColumnsFormBean.dataTypes?keys as type]
                    <option
                            value="${type}">${dataCollectionWizardFormBean.defineSQLColumnsFormBean.dataTypes[type].description}</option>
                    [/#list]</select>
            </td>
            <td>
                <input type="text" name="defineSQLColumnsFormBean.sourceColumn.defaultValue"
                       id="defineSQLColumnsFormBean.sourceColumn.defaultValue"
                       value="${dataCollectionWizardFormBean.defineSQLColumnsFormBean.sourceColumn.defaultValue}"
                       class="CustomInput">
            </td>
            <td>
                <input type="text" name="defineSQLColumnsFormBean.sourceColumn.sqlExpression"
                       id="defineSQLColumnsFormBean.sourceColumn.sqlExpression"
                       value="${dataCollectionWizardFormBean.defineSQLColumnsFormBean.sourceColumn.sqlExpression}"
                       class="CustomInput" maxlength="500">
            </td>
            <td class="lastChild">
                <a href="#" class="UpdateAction"
                   onclick="navigateTo('[@spring.url '/dataCollection/define/sqlColumns/addColumn'/]', 'POST', 'myform');">
                    Update</a>
                <a href="#" onclick="cancelAddSqlExpressionTableRow();" class="CancelAction"> Cancel</a>
            </td>
        </tr>
        [/#if]

        </tbody>
    </table>
</div>
<div class="GridHeader">
        <span class="SubmitButtons AddAction AddSqlExpressionTableRow">
            <span class="SubmitButton "> <a href="#Add" class="" onChange> Add Column</a> </span>
        </span>
</div>


<table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
    <tr>
        <td class="Labeltd textareaLable"><label for="">WHERE clause </label></td>
        <td colspan="2" class="inputltd">
            [@spring.formTextarea path="dataCollectionWizardFormBean.defineSQLColumnsFormBean.whereClause" attributes="maxlength=\"3000\" onkeyup=\"checkMaxLength(this);\" onchange=\"checkMaxLength(this);\" "/]
        </td>
    </tr>
    <tr>
        <td class="Labeltd textareaLable"><label for="">GROUP BY clause </label></td>
        <td colspan="2" class="inputltd">
            [@spring.formTextarea path="dataCollectionWizardFormBean.defineSQLColumnsFormBean.groupByClause" attributes="maxlength=\"3000\" onkeyup=\"checkMaxLength(this);\" onchange=\"checkMaxLength(this);\" "/]
        </td>
    </tr>
    <tr>
        <td class="Labeltd textareaLable"><label for="">HAVING clause </label></td>
        <td colspan="2" class="inputltd">
            [@spring.formTextarea path="dataCollectionWizardFormBean.defineSQLColumnsFormBean.havingClause" attributes="maxlength=\"3000\" onkeyup=\"checkMaxLength(this);\" onchange=\"checkMaxLength(this);\" "/]
        </td>
    </tr>
</table>


<div class="WizardBtns">
        <span class="SubmitButtons FloatLeft Gray">
            <span class="SubmitButton BtnWidth1">
            <input type="submit" value="Back" class="submit BtnWidth1"
                   onclick="updateForm('[@spring.url '/dataCollection/define/extractSourceColumns?back=true'/]', 'POST', 'myform');">
        </span>
        </span>

        <span class="SubmitButtons FloatLeft Gray">
            <span class="SubmitButton BtnWidth1">
            <input type="submit" value="Cancel" class="submit BtnWidth1"
                   onclick="updateForm('[@spring.url '/dataCollection/define/cancel'/]', 'POST', 'myform');">
        </span>
        </span>

        <span class="SubmitButtons FloatRight">
            <span class="SubmitButton BtnWidth1">
            <input type="submit" value="Next" class="submit BtnWidth1"
                   onclick="updateForm('[@spring.url '/dataCollection/define/sqlColumns'/]', 'POST', 'myform');">
            </span>
        </span>
</div>
</form>
[/@wizard.body]