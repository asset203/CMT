[#ftl/]
[#import "../../spring.ftl" as spring /]
[#import "../../macros.ftl" as cmt /]
[#import "wizard-macros.ftl" as wizard /]
[#global title="Capacity Management Tool"/]
[#global isDCSelected="selected"/]

[@cmt.htmlHead ""/]
[@cmt.navigation/]

<script src="[@spring.url '/js/data-collection.js'/]" type="text/javascript"></script>

[@wizard.body]

<h2>Select Data Collection Type </h2>

[@spring.bind path="dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType"/]
[#assign dataCollectionTypeError][@spring.showErrors  "<br>" "validationerror"/][/#assign]

[@spring.bind path="dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionName"/]
[#assign dataCollectionNameError][@spring.showErrors  "<br>" "validationerror"/][/#assign]

[#if dataCollectionTypeError?has_content || dataCollectionNameError?has_content]
<div class="CustomContent3 ErrorMessages">
    <h4>Error Message </h4>

    [#if dataCollectionTypeError?has_content]
    <p>${dataCollectionTypeError}</p>
    [/#if]

    [#if dataCollectionNameError?has_content]
    <p>${dataCollectionNameError}</p>
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
        <p>Please define the Data Collection Type as per the source.</p>
    </div>
</div>

<form id="dataTypeForm" name="dataTypeForm" action="" method="POST">

    [@wizard.defineOutputTableHiddenFields/]
    [@wizard.defineDataCollectionMappingFormBeanHiddenFields/]
    [@wizard.extractXMLSourceColumnsHiddenFields/]
    [@wizard.dataCollectionWizardHiddenFields/]

    [#-- Extract Source --]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.fileName"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.sampleLines"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.delimiter"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.header"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.header"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.otherDelimiter"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceDataFormBean.ignoreLines"/]

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


    <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
        <tr>
            <td class="Labeltd" style="white-space:nowrap"><label for="cd-dropdown">Select Data Collection Type</label> <span
                    class="RequiredField">*</span></td>
            <td colspan="2" class="inputltd">
              [#assign url][@spring.url '/dataCollection/define/type/steps'/][/#assign]
              [@spring.formSingleSelect "dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType"
              dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionTypes "class=\"cd-select required error\" onchange=\"navigateTo('" + url + "', 'POST', 'dataTypeForm');\""/]
            </td>
        </tr>

        <tr>
            <td class="Labeltd"><label for="cd-dropdown">Data Collection Name</label> <span
                    class="RequiredField">*</span></td>
            <td colspan="2" class="inputltd">
           <span
                   class="text"> [@spring.formInput "dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionName"/] </span>
            </td>
        </tr>
    </table>
    <div class="WizardBtns">
				<span class="SubmitButtons FloatLeft Gray">
					<span class="SubmitButton BtnWidth1"> <a href="[@spring.url '/dataCollection/manage'/]">Cancel</a> </span>
				</span>

				<span class="SubmitButtons FloatRight">
					<span class="SubmitButton BtnWidth1"> <a href="#"
                                                             onclick="navigateTo('[@spring.url '/dataCollection/define/type'/]', 'POST', 'dataTypeForm');">Next</a> </span>
				</span>
    </div>
</form>
[/@wizard.body]