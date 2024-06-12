[#ftl/]
[#import "../../spring.ftl" as spring /]
[#import "../../macros.ftl" as cmt /]

[#macro body]
<div class="TabContent ManageUserTabContent">
  <h1>[#if dataCollectionWizardFormBean.editMode]Edit Data Collections[#else]Define Data Collections[/#if]</h1>

  <div class="clear"></div>
  <div class="LeftSideInner">
    <div class="WizardSteps">
      <h4>Steps</h4>
      <ul class="StepsList">
      [#assign finishedSteps]true[/#assign]
      [#list dataCollectionWizardFormBean.steps as step]
        [#if step == dataCollectionWizardFormBean.currentStep]
          [#assign currentStep]true[/#assign]
          [#assign finishedSteps]false[/#assign]
        [#else]
          [#assign currentStep]false[/#assign]
        [/#if]
        <li class="[#if finishedSteps == 'true']FinishedSteps[/#if] [#if currentStep == 'true']currentStep[/#if]">
        ${step_index + 1}. ${step.description}
        </li>
      [/#list]
      </ul>
    </div>
  </div>
  <div class="RightSideInner">
     [#nested /]
  </div>
  <div class="clear"></div>
</div>

[@cmt.footer/]
[/#macro]

[#macro dataCollectionWizardHiddenFields]
[@spring.formHiddenInput "dataCollectionWizardFormBean.uuid"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.editMode"/]
[/#macro]

[#macro dataCollectionTypeHiddenFields]
[#-- Type data --]
[#if dataCollectionWizardFormBean.dataCollectionTypeFormBean?has_content]
[@spring.formHiddenInput "dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionName"/]

[/#if]
[/#macro]

[#macro defineOutputTableHiddenFields]
[#if dataCollectionWizardFormBean.defineOutputTableFormBean?has_content]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.nodeName"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.outputTableOption"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.truncateBeforeInsertion"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.isPartitioned"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.existingTable"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.tableSelected"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.dateColumn"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.addAutoFilledDateColumn"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.partitionColumnName"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.autoFilledDateColumnName"/]

[#if dataCollectionWizardFormBean.defineOutputTableFormBean.existingTableColumns?has_content]
[#list dataCollectionWizardFormBean.defineOutputTableFormBean.existingTableColumns as column]

[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.existingTableColumns[${column_index}].name"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.existingTableColumns[${column_index}].defaultValue"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.existingTableColumns[${column_index}].type"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.existingTableColumns[${column_index}].kpiType"/]

[/#list]
[/#if]

[#if dataCollectionWizardFormBean.defineOutputTableFormBean.newOutputTableColumns?has_content]
[#list dataCollectionWizardFormBean.defineOutputTableFormBean.newOutputTableColumns as column]

[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.newOutputTableColumns[${column_index}].selected"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.newOutputTableColumns[${column_index}].sampleValue"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.newOutputTableColumns[${column_index}].name"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.newOutputTableColumns[${column_index}].defaultValue"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.newOutputTableColumns[${column_index}].type"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.newOutputTableColumns[${column_index}].customType"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.newOutputTableColumns[${column_index}].sqlExpression"/]

[/#list]
[/#if]

[/#if]
[/#macro]

[#macro defineDataCollectionMappingFormBeanHiddenFields]

[@spring.formHiddenInput "dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.mappedNodeName"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.nodeNameColumn.selected"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.nodeNameColumn.sampleValue"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.nodeNameColumn.name"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.nodeNameColumn.defaultValue"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.nodeNameColumn.type"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.nodeNameColumn.customType"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineDataCollectionMappingFormBean.nodeNameColumn.sqlExpression"/]

[/#macro]

[#macro defineDBExtractionSQLHiddenFields]
[#if dataCollectionWizardFormBean.defineDBExtractionSQL?has_content]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineDBExtractionSQL.columns"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineDBExtractionSQL.query"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineDBExtractionSQL.selectedDBType"/]
[/#if]
[/#macro]

[#macro extractSourceColumnFormBeanHiddenFields]
[#if dataCollectionWizardFormBean.extractSourceColumnFormBean?has_content]
[#if dataCollectionWizardFormBean.extractSourceColumnFormBean.columns?has_content]
[#list dataCollectionWizardFormBean.extractSourceColumnFormBean.columns as column]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].selected"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].sampleValue"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].name"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].defaultValue"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].type"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].customType"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].outputColumnName"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].index"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.tableCreated"/]
[/#list]
[/#if]
[/#if]
[/#macro]

[#macro extractXMLSourceColumnsHiddenFields]
[#if dataCollectionWizardFormBean.extractXMLSourceColumns?has_content]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractXMLSourceColumns.xmlComplexity"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractXMLSourceColumns.xmlVendor"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractXMLSourceColumns.xmlConverter"/]
[/#if]
[/#macro]