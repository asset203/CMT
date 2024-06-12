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

	// tooltip
				function dropDownContent() {
					jQuery("a.HintTrigger").dropDownContent({
						dropContainer: ".SelectionHint3", /***** the drop-down container name *****/
						dropContainerAlignment: "right", /***** center/right/left *****/
						rightPush: 10,
						topPush: 5
					});
				}
				dropDownContent();

    //action when click table row to update data
    $('.EditableRow .EditableField').click(function () {
    $(this).parent().addClass("ActiveEdit");
    var selection = $(this).parent().find('.CustomFormInputs select').val();
    if ((selection == "DATE") || (selection == "STRING")) {
    $(this).parent().find('.CustomFormInputs select').next('input').show();
    } else {
    $(this).parent().find('.CustomFormInputs select').next('input').hide();
    }

    });

    if ($('.UseDateColumn').is(':checked')) {
    $('.UseDateColumn').parents().find('.DateColumnName').show();
    } else {
    $('.UseDateColumn').parents().find('.DateColumnName').hide();
    }
    $('.UseDateColumn').click(function () {
    if ($('.UseDateColumn').is(':checked')) {
    $('.UseDateColumn').parents().find('.DateColumnName').show();
    } else {
    $('.UseDateColumn').parents().find('.DateColumnName').hide();
    }
    });

    });

	function changeHint(selectElement,index){
		hintTrigger='.HintTrigger'+index;
		stringHint = '.strinHint'+index;
		dateHint =  '.dateHint'+index;
		if (($(selectElement).val() == "DATE") || ($(selectElement).val() == "STRING")) {
	    $(selectElement).next('input').val('');
	    $(selectElement).next('input').show();
		$(selectElement).parents().find(hintTrigger).show();
		if($(selectElement).val() == "DATE"){
			$(selectElement).parents().find(stringHint).hide();
			$(selectElement).parents().find(dateHint).show();
		}
		else
		{
			$(selectElement).parents().find(stringHint).show();
			$(selectElement).parents().find(dateHint).hide();
		}
    } else {
	    $(selectElement).parents().find(hintTrigger).hide();
	    $(selectElement).next('input').val('');
	    $(selectElement).next('input').hide();
		$(selectElement).parents().find(stringHint).hide();
		$(selectElement).parents().find(dateHint).hide();
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
[@wizard.defineDBExtractionSQLHiddenFields/]
[@wizard.extractXMLSourceColumnsHiddenFields/]

[#-- Extract Source --]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.fileName"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.sampleLines"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.delimiter"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.header"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.otherDelimiter"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.ignoreLines"/]

[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.tableCreated"/]

[#-- define SQL column --]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.whereClause"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.groupByClause"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.havingClause"/]
[#if dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns?has_content]
[#list dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns as column]

[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].selected"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].sampleValue"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].name"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].defaultValue"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].type"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].customType"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].sqlExpression"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].outputColumnName"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].index"/]

[/#list]
[/#if]

[@spring.bind path="dataCollectionWizardFormBean.extractSourceColumnFormBean.errors"/]
[#assign columnsErrors][@spring.showErrors "<br>" "validationerror"/][/#assign]

[#-- Use Data Columns validation --]
[@spring.bind path="dataCollectionWizardFormBean.extractSourceColumnFormBean.dateColumnName"/]
[#assign dateColumnErrors][@spring.showErrors "<br>" "validationerror"/][/#assign]


<div class="CustomContent2">
    Data Collection Type:
    <span>${dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType.description}</span> <span
        class="Space50"></span> Generic Process Name:
    <span>${dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionName}</span>
</div>
<h2>Define Plain columns </h2>

[#if columnsErrors?has_content || db_error?has_content || dateColumnErrors?has_content]
<div class="CustomContent3 ErrorMessages">
    <h4>Error Message </h4>

    [#if columnsErrors?has_content]
    <p>${columnsErrors}</p>
    [/#if]

    [#if db_error?has_content]
    <p>${db_error}</p>
    [/#if]
    [#if dateColumnErrors?has_content]
    <p>${dateColumnErrors}</p>
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
        <p>Please select the columns you want to include in the Data Collection output.</p>
    </div>
</div>

<h2>Extracted Columns ( [#if
    dataCollectionWizardFormBean.extractSourceColumnFormBean.columns?has_content]${dataCollectionWizardFormBean.extractSourceColumnFormBean.columns?size}[#else]
    0[/#if] ) </h2>

<div class="GridContainer1">
    <div class="Grid SmallGrid">
        <table cellspacing="0" border="0" cellpadding="0" width="100%" class="FormTable">
			<col style="width:5%">
	        <col style="width:20%">
	        <col style="width:15%">
	        <col style="width:35%">
	        <col style="width:25%">
			<thead>
            <tr>
                <th></th>
                <th>Sample Value</th>
                <th>Name</th>
                <th>Type</th>
                <th>Default value</th>
            </tr>
            </thead>
            <tbody>
            [#list dataCollectionWizardFormBean.extractSourceColumnFormBean.columns as column]
            [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].outputColumnName"/]
            [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].index"/]
            <tr class="EditableRow [#if column.hasError]error[/#if]">
                <td>
                 [#if dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType == 'DB' ]
                 <input type="checkbox" id="extractSourceColumnFormBean.columns[${column_index}].selected"
		 			                           name="extractSourceColumnFormBean.columns[${column_index}].selected"
		                     [#if column.selected]checked[/#if] disabled/>
		                     [@spring.formHiddenInput
                        "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].selected"/]
                 [#else]
                 <input type="checkbox" id="extractSourceColumnFormBean.columns[${column_index}].selected"
		                            name="extractSourceColumnFormBean.columns[${column_index}].selected"
                    [#if column.selected]checked[/#if]/>
                 [/#if]
                </td>
                <td class="[#if !dataCollectionWizardFormBean.editMode]EditableField[/#if]" style="word-break: break-all;">
                    <div>
                        <span>[#if column.sampleValue?has_content]${column.sampleValue}[/#if]</span>
                        [#if column.sampleValue?has_content && !dataCollectionWizardFormBean.editMode]
                        [@spring.formInput
                        "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].sampleValue"
                        "class=\"CustomInput\" value=\"${column.sampleValue}\" readonly=\"readonly\""/]
                        [#elseif column.sampleValue?has_content]
                        [@spring.formInput
                        "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].sampleValue"
                        "class=\"CustomInput\" value=\"${column.sampleValue}\" disabled=\"disabled\""/]
                        [@spring.formHiddenInput
                        "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].sampleValue"/]
                        [/#if]
                    </div>
                </td>
                <td class="EditableField [#if !dataCollectionWizardFormBean.editMode]EditableField[/#if]"
                    style="word-break: break-all;">
                    <div>
                        <span>${column.name}</span>
                        [#if !dataCollectionWizardFormBean.editMode && dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType != 'DB']
                        [@spring.formInput
                        "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].name"
                        "class=\"CustomInput specialChars\" maxlength=\"30\""/]
                        [#else]
                        [@spring.formInput
                        "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].name"
                        "class=\"CustomInput\" disabled=\"disabled\""/]
                        [@spring.formHiddenInput
                        "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].name"/]
                        [/#if]

                    </div>
                </td>
                <td class="EditableField [#if !dataCollectionWizardFormBean.editMode]EditableField[/#if] CustomFormInputs"
                    style="word-break: break-all;">
                    <div>
                        <span>[#if column.type?has_content]${column.type.description}[/#if]</span>
                        [#if !dataCollectionWizardFormBean.editMode || dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType == 'DB']
                        <select class="CustomSelect" name="extractSourceColumnFormBean.columns[${column_index}].type"
                                id="extractSourceColumnFormBean.columns[${column_index}].type" onload= "changeHint(this,${column_index});" onchange="changeHint(this,${column_index});">
                            [#list dataCollectionWizardFormBean.extractSourceColumnFormBean.dataTypes?keys as type]
                            <option value="${type}"
                            [#if column.type?has_content && type == column.type]selected="selected"[/#if]>
                            ${dataCollectionWizardFormBean.extractSourceColumnFormBean.dataTypes[type].description}
                            </option>
                            [/#list]
                        </select>
                        <input type="text" id="extractSourceColumnFormBean.columns[${column_index}].customType"
                               name="extractSourceColumnFormBean.columns[${column_index}].customType"
                               value="[#if column.customType?has_content]${column.customType}[/#if]"
                               style="display:none width:150px;" class="CustomInput CustomInputExclude specialChars"/>
						<div class="HintBlock" style="word-wrap:break-word">
							<a href="#D" class="HintTrigger HintTrigger${column_index}" [#if column.type?has_content&& (column.type='NUMBER' ||column.type='FLOAT')]style="display:none"[/#if]></a>
							<div class="SelectionHint3" style="display:none">
								<div class="SelectionHintInner3">
									<div>Hint</div>
										<span class="strinHint${column_index}" [#if column.type?has_content&& !(column.type='STRING')]style="display:none"[/#if]>
											Enter the maximum number of characters excpected
										</span>
										<span class="dateHint${column_index}" [#if column.type?has_content&& !(column.type='DATE')]style="display:none"[/#if]>
											Enter the date format for date values in file.<br>
											For detailed description of valid format characters,please refer to:
											<a href="http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html" target="_blank"><b>Simple Date Format</b></a>
										</span>
									</div>
								</div>
						</div>

                        [#else]
                        <select class="EditableField CustomSelect"
                                name="extractSourceColumnFormBean.columns[${column_index}].type"
                                id="extractSourceColumnFormBean.columns[${column_index}].type"
                                disabled="disabled">
                            [#list dataCollectionWizardFormBean.extractSourceColumnFormBean.dataTypes?keys as type]
                            <option value="${type}"
                            [#if column.type?has_content && type == column.type]selected="selected"[/#if]>
                            ${dataCollectionWizardFormBean.extractSourceColumnFormBean.dataTypes[type].description}
                            </option>
                            [/#list]
                        </select>
                        <input type="text" id="extractSourceColumnFormBean.columns[${column_index}].customType"
                               name="extractSourceColumnFormBean.columns[${column_index}].customType"
                               value="[#if column.customType?has_content]${column.customType}[/#if]"
                               class="CustomInput CustomInputExclude" disabled="disabled"/>
					<div class="HintBlock" style="word-wrap:break-word">
							<a href="#D" class="HintTrigger" [#if column.type?has_content&& (column.type='NUMBER' ||column.type='FLOAT')]style="display:none"[/#if]></a>
							<div class="SelectionHint3" style="display:none">
								<div class="SelectionHintInner3">
									<div>Hint</div>
										<span class="strinHint" [#if column.type?has_content&& !(column.type='STRING')]style="display:none"[/#if]>
											Enter the maximum number of characters excpected
										</span>
										<span class="dateHint" [#if column.type?has_content&& !(column.type='DATE')]style="display:none"[/#if]>
											Enter the date format for date values in file.<br>
											For detailed description of valid format characters,please refer to:
											<a href="http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html" target="_blank"><b>Simple Date Format</b></a>
										</span>
									</div>
								</div>
						</div>
                    </div>
                        [@spring.formHiddenInput
                        "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].type"/]
                        [@spring.formHiddenInput
                        "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].customType"/]
                        [/#if]
                </td>
                <td class="EditableField" style="word-break: break-all;">
                    <div>
                        <span>[#if column.defaultValue?has_content] ${column.defaultValue} [/#if]</span>
                        [@spring.formInput
                        "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].defaultValue"
                        "class=\"CustomInput specialChars\" maxlength=\"50\" "/]
                    </div>
                </td>
            </tr>
            [/#list]

            </tbody>
        </table>
    </div>
    <div>
    [#if dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType != 'DB']
        <table cellspacing="0" border="0" cellpadding="0" width="100%" class="tablesorter FormTable">
            <tr>
                <td class="Labeltd">
                    [#assign checkStatus=""/]
                    [#if dataCollectionWizardFormBean?? && dataCollectionWizardFormBean.extractSourceColumnFormBean.useDateColumn??]
                    [#if dataCollectionWizardFormBean.extractSourceColumnFormBean.useDateColumn?string == 'true']
                    [#assign checkStatus="checked"/]
                    [/#if]
                    [/#if]
                    <label>
                        [@spring.bind path="dataCollectionWizardFormBean.extractSourceColumnFormBean.useDateColumn"/]
                        <input type="checkbox" name="${spring.status.expression}"
                               id="dataCollectionWizardFormBean.extractSourceColumnFormBean.useDateColumn"
                               value="true" ${checkStatus}  class="UseDateColumn" />
                        Use date column
                    </label>
                </td>
            </tr>

            <tr class="DateColumnName"
            [#if dataCollectionWizardFormBean.extractSourceColumnFormBean.useDateColumn == false]style="display:none"[/#if]>
            <td class="Labeltd">
                <label>Date column name</label>
            </td>

            <td colspan="2" class="inputltd">
				<span class="text DateColumnName">
					[@spring.formInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.dateColumnName" "maxlength=\"30\" class=\"specialChars\""/]
				</span>
            </td>
            </tr>
			 <tr>
                <td class="Labeltd DateColumnName">
                               <label>Date Precession</label>
                </td>
                <td  class="inputltd DateColumnName">
					  <select class="EditableField CustomSelect"
                                name="extractSourceColumnFormBean.dateColumnPrecession"
                                id="extractSourceColumnFormBean.dateColumnPrecession">
                            [#list dataCollectionWizardFormBean.extractSourceColumnFormBean.dateColumnPrecessionTypes?keys as type]
                            <option value="${type}"
                            [#if dataCollectionWizardFormBean.extractSourceColumnFormBean.dateColumnPrecession?has_content && type == dataCollectionWizardFormBean.extractSourceColumnFormBean.dateColumnPrecession]selected="selected"[/#if]>
                            ${dataCollectionWizardFormBean.extractSourceColumnFormBean.dateColumnPrecessionTypes[type]}
                            </option>
                            [/#list]
                        </select>
                </td>
            </tr>
        </table>
        [/#if]
    </div>

    <div class="WizardBtns">

        [#if !dataCollectionWizardFormBean.editMode || dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType == 'DB' ]

      <span class="SubmitButtons FloatLeft Gray">
            <span class="SubmitButton BtnWidth1">
              [#if dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType == 'TEXT']
                <input type="submit" value="Back" class="submit BtnWidth1"
                       onclick="updateForm('[@spring.url '/dataCollection/define/extractDataSource?back=true'/]', 'POST', 'myform');">
              [#elseif dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType == 'XML']
                <input type="submit" value="Back" class="submit BtnWidth1"
                       onclick="updateForm('[@spring.url '/dataCollection/define/extractXMLSourceColumns?back=true'/]', 'POST', 'myform');">
              [#elseif dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType == 'DB']
                  <input type="submit" value="Back" class="submit BtnWidth1"
                        onclick="updateForm('[@spring.url '/dataCollection/define/defineDBExtractionSQL?back=true'/]', 'POST', 'myform');">
              [/#if]
            </span>
        </span>
        [/#if]

    <span class="SubmitButtons FloatLeft Gray">
            <span class="SubmitButton BtnWidth1">
            <input type="submit" value="Cancel" class="submit BtnWidth1"
                   onclick="updateForm('[@spring.url '/dataCollection/define/cancel'/]', 'POST', 'myform');">
        </span>
        </span>

        <span class="SubmitButtons FloatRight">
            <span class="SubmitButton BtnWidth1">
            <input type="submit" value="Next" class="submit BtnWidth1"
                   onclick="updateForm('[@spring.url '/dataCollection/define/extractSourceColumns'/]', 'POST', 'myform');">
            </span>
        </span>
    </div>

</form>

[/@wizard.body]