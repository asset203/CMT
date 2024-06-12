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
    </script>

[@wizard.body]

<form id="defineDataCollectionMappingForm" method="POST" action="javascript:void(0);">

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

    [#-- Define Plain Columns --]
    [#if dataCollectionWizardFormBean.extractSourceColumnFormBean.columns?has_content]
    [#list dataCollectionWizardFormBean.extractSourceColumnFormBean.columns as column]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].selected"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].sampleValue"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].defaultValue"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].type"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].name"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].customType"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].index"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].kpiValue"/]

    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.tableCreated"/]
    [/#list]
    [/#if]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.useDateColumn"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.dateColumnName"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.dateColumnPrecession"/]
    [#-- define SQL column --]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.whereClause"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.groupByClause"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.havingClause"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.extractionSql"/]
    [#if dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns?has_content]
    [#list dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns as column]

    [@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].selected"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].sampleValue"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].name"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].defaultValue"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].type"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].customType"/]
    [@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].sqlExpression"/]
	[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].index"/]
    [/#list]
    [/#if]

    [@wizard.defineOutputTableHiddenFields/]
    [@wizard.defineDataCollectionMappingFormBeanHiddenFields/]
    [#-- End Hidden Fields --]

    <div class="CustomContent2">
        Data Collection Type:
        <span>${dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType.description}</span> <span
            class="Space50"></span> Generic Process Name:
        <span>${dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionName}</span>
        </div>
    <h2>Define Data Collection Mapping</h2>

    [#assign errorMessage]
       [#if dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.errorMessage?has_content]
       ${dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.errorMessage}
       [/#if]
     [/#assign]

    [@spring.bind path="dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.outputColumns"/]
    [#assign outputColumnsError][@spring.showErrors "<br>" "validationerror"/][/#assign]

    [#if errorMessage?has_content || outputColumnsError?has_content]
    <div class="CustomContent3 ErrorMessages">
        <h4>Error Message </h4>

       [#if errorMessage?has_content]
        <p>${errorMessage}</p>
       [/#if]

       [#if outputColumnsError?has_content]
        <p>${outputColumnsError}</p>
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
            <p>Please select the destination database table columns to map those of the Data Collection output.</p>
            </div>
        </div>
    <h2>Mapped Columns <span class="RequiredField">*</span> </h2>

    <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
        <tr>
            <td class="Labeltd" style="white-space:nowrap"><label for="cd-dropdown">Select Data Collection KPI Type </label> <span
                    class="RequiredField">*</span></td>
            <td colspan="2" class="inputltd">

              [@spring.formSingleSelect "dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.kpiType"
              dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.dataCollectionKpiTypes "class=\"cd-select required error\" \""/]
                </td>
            </tr>
        </table>

    <div class="Grid SmallGrid">
        <table cellspacing="0" border="0" cellpadding="0" width="100%" class="tablesorter FormTable">
            <thead>
                <tr>
                    <th width="40%"> Data Collecetion Columns  </th>
                    <th width="30%"> Output Table Columns</th>
                    <th width="30%"> Kpi Columns Types </th>
                    </tr>
                </thead>
            <tbody>
    [#if dataCollectionWizardFormBean.extractSourceColumnFormBean.columns?has_content]
    [#assign addNodeNameColumn]true[/#assign]
    [#list dataCollectionWizardFormBean.extractSourceColumnFormBean.columns as column]
    [#if column.selected]
                <tr>
                    <td> ${column.name} </td>
                    <td>
                        <select class="CustomSelect CustomSelect2"
                                name="extractSourceColumnFormBean.columns[${column_index}].outputColumnName"
                                id="extractSourceColumnFormBean.columns[${column_index}].outputColumnName">
                [#list dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.outputColumns as outputColumn]
                [#if (!(column.type?has_content) || column.type == outputColumn.type) ][#-- && !(outputColumn.name?lower_case=='node_name') --]
                            <option value="${outputColumn.name}" [#if outputColumn.name?lower_case == column.outputColumnName?lower_case]selected="selected"[/#if]>${outputColumn.name}</option>
                [/#if]
                [#if (outputColumn.name?lower_case=='node_name')][#assign addNodeNameColumn]false[/#assign][/#if]
				[/#list]								
                [#if (dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.mappedNodeName == true) && (addNodeNameColumn=='true')]  
[#if !(column.type?has_content) || column.type == dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.nodeNameColumn.type]				
                            <option value="${dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.nodeNameColumn.name}">${dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.nodeNameColumn.name}</option>                
				[/#if] 
				[/#if]				
                            </select>				
                        </td>

                    <td>
                        <select class="CustomSelect CustomSelect2"
                                name="extractSourceColumnFormBean.columns[${column_index}].outputColumnName"
                                id="extractSourceColumnFormBean.columns[${column_index}].outputColumnName">     
                [#list dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.kpiTypesColumns as kpiType]
                            <option value="${kpiType.value}" [#if kpiType.value == column.kpiValue ]selected="selected"[/#if]>${kpiType.description}</option>
                [/#list]								
                            </select>				
                        </td>

                    </tr>
    [/#if]
    [/#list]
    [/#if]

    [#if dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns?has_content]
    [#assign addNodeNameColumn]true[/#assign]
    [#list dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns as column]
                <tr>
                    <td> ${column.name} </td>
                    <td>
                        <select class="CustomSelect CustomSelect2"
                                name="defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].outputColumnName"
                                id="defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].outputColumnName">
             [#list dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.outputColumns as outputColumn]
             [#if (!(column.type?has_content) || column.type == outputColumn.type) ][#-- && !(outputColumn.name?lower_case=='node_name')--]
                            <option value="${outputColumn.name}" [#if outputColumn.name?lower_case == column.outputColumnName?lower_case]selected="selected"[/#if]>${outputColumn.name}</option>
             [/#if]
             [#if (outputColumn.name?lower_case=='node_name')][#assign addNodeNameColumn]false[/#assign][/#if]
	           [/#list]	  			   
             [#if (dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.mappedNodeName == true) && (addNodeNameColumn == 'true')]         
[#if !(column.type?has_content) || column.type == dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.nodeNameColumn.type]			 
                            <option value="${dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.nodeNameColumn.name}">${dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.nodeNameColumn.name}</option>
             [/#if]
            [/#if]
                            </select>   					 
                        </td>

                    <td>
                        <select class="CustomSelect CustomSelect2"
                                name="defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].outputColumnName"
                                id="defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].outputColumnName">     
                [#list dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.kpiTypesColumns as kpiType]
                            <option value="${kpiType.value}"  [#if kpiType.value == column.kpiValue ]selected="selected"[/#if] >${kpiType.description}</option>
                [/#list]								
                            </select>				
                        </td>

                    </tr>
    [/#list]
    [/#if]

                </tbody>
            </table>
        </div>

    <div class="WizardBtns">
        <span class="SubmitButtons FloatLeft Gray">
            <span class="SubmitButton BtnWidth1">
                <input type="submit" value="Back" class="submit BtnWidth1"
                       onclick="updateForm('[@spring.url '/dataCollection/define/outputTable?back=true'/]', 'POST', 'defineDataCollectionMappingForm');">
                </span>
            </span>

        <span class="SubmitButtons FloatLeft Gray">
            <span class="SubmitButton BtnWidth1">
                <input type="submit" value="Cancel" class="submit BtnWidth1"
                       onclick="updateForm('[@spring.url '/dataCollection/define/cancel'/]', 'POST', 'defineDataCollectionMappingForm');">
                </span>
            </span>

        <span class="SubmitButtons BigGreen FloatRight SaveProcess">
            <span class="SubmitButton ">
                <a href="#" onclick="navigateTo('[@spring.url '/dataCollection/define/dataCollectionMapping'/]', 'POST', 'defineDataCollectionMappingForm');javascript:this.disabled='disabled'">Save Process</a>
                </span>
            </span>

        </div>
    </form>
[/@wizard.body]