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
$(document).ready(function() {
	$('.DBTypeSelection select').change(function(){
		$(this).parents().find('.CustomContent1').hide();
		if ($(this).val() == "ORACLE") {
			$(this).parents().find('.DBtype0').show();
		}
		else
		if ($(this).val() == "MS_SQL_SERVER") {
			$(this).parents().find('.DBtype1').show();
		}
		else
		if ($(this).val() == "MY_SQL") {
			$(this).parents().find('.DBtype2').show();
		}
		else
		if ($(this).val() == "SYBASE") {
			$(this).parents().find('.DBtype3').show();
		}
		else
		if ($(this).val() == "ORACLE_RAC") {
			$(this).parents().find('.DBtype4').show();
		}

	});
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

});
</script>
[@wizard.body]

<form id="defineDBExtractionSql" method="POST" action="javascript:void(0);">

    [@wizard.dataCollectionWizardHiddenFields/]
    [@wizard.dataCollectionTypeHiddenFields/]
    [@wizard.defineOutputTableHiddenFields/]
    [@wizard.defineDataCollectionMappingFormBeanHiddenFields/]

    [#if dataCollectionWizardFormBean.extractSourceDataFormBean?has_content]
    [#-- Extract Source --]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.fileName"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.sampleLines"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.delimiter"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.header"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.otherDelimiter"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.ignoreLines"/]
    [/#if]

    [#-- Define Plain Columns --]
    [#if dataCollectionWizardFormBean.extractSourceColumnFormBean?has_content]
    [#if dataCollectionWizardFormBean.extractSourceColumnFormBean.columns?has_content]
    [#list dataCollectionWizardFormBean.extractSourceColumnFormBean.columns as column]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].selected"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].sampleValue"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].defaultValue"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].type"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].name"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].customType"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].outputColumnName"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].index"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.tableCreated"/]
    [/#list]
    [/#if]
    [/#if]

    [@spring.bind path="dataCollectionWizardFormBean.defineDBExtractionSQL.columns"/]
    [#assign columnsError][@spring.showErrors "<br>" "validationerror"/][/#assign]
    [@spring.bind path="dataCollectionWizardFormBean.defineDBExtractionSQL.query"/]
    [#assign queryError][@spring.showErrors "<br>" "validationerror"/][/#assign]

    <div class="CustomContent2">
        Data Collection Type:
        <span>${dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType.description}</span> <span
            class="Space50"></span> Generic Process Name:
        <span>${dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionName}</span>
    </div>

    <h2>Define the Extraction SQL statement for DB process </h2>

    [#if columnsError?has_content || queryError?has_content]
    <div class="CustomContent3 ErrorMessages">
        <h4>Error Message </h4>

        [#if columnsError?has_content]
        <p>${columnsError}</p>
        [/#if]

        [#if queryError?has_content]
         <p>${queryError}</p>
        [/#if]
    </div>
   [/#if]

    [#list dataCollectionWizardFormBean.defineDBExtractionSQL.dbTypes?keys as dbType]
    <div class="CustomContent1 DBtype${dbType_index}" [#if dataCollectionWizardFormBean.defineDBExtractionSQL.selectedDBType != dbType]style="display:none"[/#if]>
        <h4>Instructions</h4>
        <a href="#d" class="Expandable">
            <span class="HideContent"> [ - ] Hide</span>
            <span class="ShowContent" style="display:none"> [ + ] Show</span>
        </a>
        <div class="ExpandableContent ">
            <p>* The drop-down list "Databse Type" enables you to select the source database engine.<br>
               * Please type in "Columns" text area the Data Collection output column names separated by commas.<br>
			   * please type in "Query" text area the SQL statements you need to extract data from the source databse.</p>
        </div>
    </div>
    [/#list]

    <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
        <tr >
            <td class="Labeltd"> <label for="name">Database Type</label> <span class="RequiredField">*</span></td>
            <td class="inputltd DBTypeSelection" colspan="2">
                [@spring.formSingleSelect "dataCollectionWizardFormBean.defineDBExtractionSQL.selectedDBType"
                         dataCollectionWizardFormBean.defineDBExtractionSQL.dbTypes "class=\"cd-select required error\""/]
            </td>
        </tr>
        <tr>
            <td class="Labeltd textareaLable"> <label for="">Columns </label> <span class="RequiredField">*</span></td>
            <td class="inputltd" colspan="2">
                [@spring.formTextarea "dataCollectionWizardFormBean.defineDBExtractionSQL.columns"/]
            </td>
        </tr>
        <tr>
            <td class="Labeltd textareaLable"> <label for="">Query </label><span class="RequiredField">*</span> </td>
            <td class="inputltd" colspan="2">
                [@spring.formTextarea "dataCollectionWizardFormBean.defineDBExtractionSQL.query"/]
                	<div class="HintBlock" style="word-wrap:break-word">
				<a href="#D" class="HintTrigger"></a>
				<div class="SelectionHint3" >
				<div class="SelectionHintInner3" style="word-wrap:break-word"> <div>Paramtarized Date Condition</div>
					use syntax :
					where to_date(to_char(&lt;DATE_COLUMN_NAME&gt;,'mm/dd/yyyy'),'mm/dd/yyyy') = to_date('$date(mm/dd/yyyy)$','mm/dd/yyyy'))
				</div>
				</div>
			</div>
            </td>
        </tr>
    </table>

    <div class="WizardBtns">

     [#if !dataCollectionWizardFormBean.editMode]
     <span class="SubmitButtons FloatLeft Gray">
         <span class="SubmitButton BtnWidth1">
             <input type="submit" value="Back" class="submit BtnWidth1"
                    onclick="updateForm('[@spring.url '/dataCollection/define/type?back=true'/]', 'POST', 'defineDBExtractionSql');">
         </span>
     </span>
     [/#if]

     <span class="SubmitButtons FloatLeft Gray">
          <a href="[@spring.url '/dataCollection/manage'/]" class="SubmitButton BtnWidth1">Cancel</a>
     </span>

     <span class="SubmitButtons FloatRight">
         <span class="SubmitButton BtnWidth1">
             <input type="submit" value="Next" class="submit BtnWidth1"
                    onclick="updateForm('[@spring.url '/dataCollection/define/defineDBExtractionSQL'/]', 'POST', 'defineDBExtractionSql');">
         </span>
     </span>
 </div>

</form>

[/@wizard.body]