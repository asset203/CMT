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

    $(document).ready(function(){

    if ($('.DelimiterTD select').val() == "OTHER") {
    $('.DelimiterTD select').next('input').show();
    }

    // check for other Delimiter
    $('.DelimiterTD select').change(function(){
    if ($(this).val() == "OTHER") {
    $(this).next('input').show();
    }
    else {
    $(this).next('input').val('');
    $(this).next('input').hide();
    }});

    });

</script>
[@wizard.body]

<form id="extractSourceForm" method="POST" action="javascript:void(0);" enctype="multipart/form-data">

    [@wizard.defineDataCollectionMappingFormBeanHiddenFields/]
    [@wizard.defineOutputTableHiddenFields/]
    [@wizard.dataCollectionWizardHiddenFields/]
    [#-- Type data --]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionName"/]

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

    [@spring.bind path="dataCollectionWizardFormBean.extractSourceDataFormBean.fileName"/]
    [#assign extractSourceFileError][@spring.showErrors "<br>" "validationerror"/][/#assign]

    [@spring.bind path="dataCollectionWizardFormBean.extractSourceDataFormBean.otherDelimiter"/]
    [#assign otherDelimiterError][@spring.showErrors "<br>" "validationerror"/][/#assign]

    [@spring.bind path="dataCollectionWizardFormBean.extractSourceDataFormBean.ignoreLines"/]
    [#assign ignoreLinesError][@spring.showErrors "<br>" "validationerror"/][/#assign]

    <div class="CustomContent2">
        Data Collection Type:
        <span>${dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType.description}</span> <span
            class="Space50"></span> Generic Process Name:
        <span>${dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionName}</span>
    </div>
    <h2>Extract source Data </h2>

    [#if extractSourceFileError?has_content || otherDelimiterError?has_content ||ignoreLinesError?has_content]
    <div class="CustomContent3 ErrorMessages">
        <h4>Error Message </h4>

        [#if extractSourceFileError?has_content]
        <p>${extractSourceFileError}</p>
        [/#if]

        [#if otherDelimiterError?has_content]
        <p>${otherDelimiterError}</p>
        [/#if]

        [#if ignoreLinesError?has_content]
        <p>${ignoreLinesError}</p>
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
            <p>Please upload a sample file and configure data extraction parameters.</p>
        </div>
    </div>
    <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
        <tr>
            <td class="Labeltd"> <label for="">Upload file</label> <span class="RequiredField">*</span></td>
            <td colspan="2" class="inputltd">
			<span class="text largeInput FileUpload">
				<input type="text" id="" >
				[@spring.formInput path="dataCollectionWizardFormBean.extractSourceDataFormBean.uploadFile"
                    fieldType="file" attributes="accept=\".csv,text/plain\""/]
			</span>
			<span class="SubmitButtons">
				 <a class="SubmitButton BtnWidth1" href="#D">Browse</a>
			</span>
            </td>
        </tr>

        <tr>
            <td class="Labeltd"></td>
            <td colspan="2" class="inputltd">
                File size must not be larger than 4 MB<br><br>
                <span class="SubmitButtons FloatLeft">
                    <span class="SubmitButton BtnWidth1">
                        <input type="submit" value="Upload" class="submit BtnWidth1"
                               onclick="updateForm('[@spring.url '/dataCollection/define/extractDataSource/uploadSource'/]', 'POST', 'extractSourceForm');">
                    </span>
                </span>
            </td>
        </tr>
        <tr>
            <td class="Labeltd textareaLable"><label for="">Sample Lines</label>
            </td>
            <td colspan="2" class="inputltd">
                [@spring.formTextarea "dataCollectionWizardFormBean.extractSourceDataFormBean.sampleLines" "readonly"/]
            </td>
        </tr>
        <tr>
            <td class="Labeltd"><label for="">Delimiter</label> <span class="RequiredField">*</span></td>
            <td colspan="2" class="inputltd DelimiterTD">

                [@spring.formSingleSelect "dataCollectionWizardFormBean.extractSourceDataFormBean.delimiter"
                dataCollectionWizardFormBean.extractSourceDataFormBean.delimiterTypes "class=\"cd-select required error
                cd-dropdown\""/]

                [@spring.formInput "dataCollectionWizardFormBean.extractSourceDataFormBean.otherDelimiter"
                "style=\"display:none\" class=\"CustomInput\""/]
            </td>
        </tr>
        <tr>
            <td class="Labeltd"><label for="">Ignore lines</label></td>
            <td colspan="2" class="inputltd">
                <span class="text"> [@spring.formInput "dataCollectionWizardFormBean.extractSourceDataFormBean.ignoreLines" "maxlength=\"10\""/] </span>
            </td>
        </tr>
        <tr>
            <td class="Labeltd"><label for="">Headers usage</label> <span class="RequiredField">*</span></td>
            <td colspan="2" class="inputltd RadioInputs WithHint">

                [#list dataCollectionWizardFormBean.extractSourceDataFormBean.headerTypes?keys as headerType]

                [#assign className]
                [#if dataCollectionWizardFormBean.extractSourceDataFormBean.header?has_content &&
                dataCollectionWizardFormBean.extractSourceDataFormBean.header == headerType]selectedlable[#else][/#if]
                [/#assign]

                <label class="${className}">
                    <input type="radio" minlength="2" class="${className}" size="25"
                           name="extractSourceDataFormBean.header"
                           id="extractSourceDataFormBean.header${headerType_index}"
                    [#if className?has_content]checked="checked"[/#if]
                    value="${headerType}">
                    ${dataCollectionWizardFormBean.extractSourceDataFormBean.headerTypes[headerType].description}
                    <div class="SelectionHint" style="display:none">
                        <div class="SelectionHintInner">
                            <div>Hint</div>
                            ${dataCollectionWizardFormBean.extractSourceDataFormBean.headerTypes[headerType].hint}
                        </div>
                    </div>
                </label>

                [/#list]
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <div class="Height100"></div>
            </td>
        </tr>

    </table>
    <div class="WizardBtns">

        <span class="SubmitButtons FloatLeft Gray">
            <span class="SubmitButton BtnWidth1">
                <input type="submit" value="Back" class="submit BtnWidth1"
                       onclick="updateForm('[@spring.url '/dataCollection/define/type?back=true'/]', 'POST', 'extractSourceForm');">
            </span>
        </span>

        <span class="SubmitButtons FloatLeft Gray">
             <a href="[@spring.url '/dataCollection/manage'/]" class="SubmitButton BtnWidth1">Cancel</a>
        </span>

        <span class="SubmitButtons FloatRight">
            <span class="SubmitButton BtnWidth1">
                <input type="submit" value="Next" class="submit BtnWidth1"
                       onclick="updateForm('[@spring.url '/dataCollection/define/extractDataSource'/]', 'POST', 'extractSourceForm');">
            </span>
        </span>
    </div>
</form>
[/@wizard.body]