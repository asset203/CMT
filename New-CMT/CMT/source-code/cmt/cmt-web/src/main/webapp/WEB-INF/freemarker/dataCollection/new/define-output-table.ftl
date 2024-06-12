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

    $('.AddNewColumnTableRow').click(function(){
    if($('.AddNewColumnTableRow').attr('disabled') != 'disabled') {
    $(this).parent().prev().find('table').append('<tr class="NewRow"><td class="firstChild"> <input type="text" name="defineOutputTableFormBean.sourceColumn.name" id="defineOutputTableFormBean.sourceColumn.name" class="CustomInput specialChars"> </td><td> <select class="CustomSelect" name="defineOutputTableFormBean.sourceColumn.type" id="defineOutputTableFormBean.sourceColumn.type">[#list dataCollectionWizardFormBean.defineOutputTableFormBean.dataTypes?keys as type]<option value="${type}">${dataCollectionWizardFormBean.defineOutputTableFormBean.dataTypes[type].description}</option>[/#list]</select></td><td> <input type="text" name="defineOutputTableFormBean.sourceColumn.defaultValue" id="defineOutputTableFormBean.sourceColumn.defaultValue" class="CustomInput specialChars"> </td><td class="lastChild"><a href="#" class="UpdateAction" onclick="validateAndNavigat(' + '\'[@spring.url '/dataCollection/define/outputTable/addColumn'/]\');"> Update</a><a href="#" onclick="cancelAddNewColumnTableRow();" class="CancelAction"> Cancel</a></td></tr>');
    $('.AddNewColumnTableRow').attr('disabled','disabled');
    }
    });

    [#if dataCollectionWizardFormBean.defineOutputTableFormBean.sourceColumn?has_content]
    $('.AddNewColumnTableRow').attr('disabled','disabled');
    [/#if]

	 if ($('.TruncateOption').is(':checked')) {
    $('.TruncateOption').parents().find('.DateColumnName').show();
    } else {
    $('.TruncateOption').parents().find('.DateColumnName').hide();
    }

     if ($('.AutoFilledDateOption').is(':checked')) {
        $('.AutoFilledDateOption').parents().find('.AutoFilledDateColumnName').show();
        } else {
        $('.AutoFilledDateOption').parents().find('.AutoFilledDateColumnName').hide();
        }
		
		
	 if ($('.PartitionedOption').is(':checked')) {
    $('.PartitionedOption').parents().find('.PartitionColumnName').show();
    } else {
    $('.PartitionedOption').parents().find('.PartitionColumnName').hide();
    }

    $('.TruncateOption').click(function () {
    if ($('.TruncateOption').is(':checked')) {
    $('.TruncateOption').parents().find('.DateColumnName').show();
    } else {
    $('.TruncateOption').parents().find('.DateColumnName').hide();
    }
    });
       $('.AutoFilledDateOption').click(function () {
            if ($('.AutoFilledDateOption').is(':checked')) {
            $('.AutoFilledDateOption').parents().find('.AutoFilledDateColumnName').show();
            } else {
            $('.AutoFilledDateOption').parents().find('.AutoFilledDateColumnName').hide();
            }
            });
			
			  $('.PartitionedOption').click(function () {
    if ($('.PartitionedOption').is(':checked')) {
    $('.PartitionedOption').parents().find('.PartitionColumnName').show();
    } else {
    $('.PartitionedOption').parents().find('.PartitionColumnName').hide();
    }
    });



    });

    function validateAndNavigat(url){
    //alert('validateAnNavigat');
    var isvalidate=$("#myform").valid();
    //alert(isvalidate);
    if(isvalidate){
    navigateTo( url, 'POST', 'myform');
    }
    }
    function cancelAddNewColumnTableRow() {

    $('.AddNewColumnTableRow').parent().prev().find('table').each(function(){
    if($('tbody', this).length > 0){
    $('tbody tr:last', this).remove();
    }else {
    $('tr:last', this).remove();
    }
    });

    $('.AddNewColumnTableRow').removeAttr('disabled');
    }




</script>

[@wizard.body]
[@cmt.loadingTxt/]
<form id="myform" method="POST" action="javascript:void(0);">

[@wizard.defineDataCollectionMappingFormBeanHiddenFields/]
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
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].outputColumnName"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].sampleValue"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].name"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].defaultValue"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].type"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].customType"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.tableCreated"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.extractSourceColumnFormBean.columns[${column_index}].index"/]
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
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].outputColumnName"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].sampleValue"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].name"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].defaultValue"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].type"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].customType"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].sqlExpression"/]
[@spring.formHiddenInput "dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns[${column_index}].index"/]

[/#list]
[/#if]
[#-- End Hidden Fields --]
[#assign errorMessage]
[#if dataCollectionWizardFormBean.defineOutputTableFormBean.errorMessage?has_content]
${dataCollectionWizardFormBean.defineOutputTableFormBean.errorMessage}
[/#if]
[/#assign]

[@spring.bind path="dataCollectionWizardFormBean.defineOutputTableFormBean.sourceColumn"/]
[#assign newColumnError][@spring.showErrors "<br>" "error"/][/#assign]

[@spring.bind path="dataCollectionWizardFormBean.defineOutputTableFormBean.dateColumn"/]
[#assign dateColumnError][@spring.showErrors "<br>" "error"/][/#assign]

[@spring.bind path="dataCollectionWizardFormBean.defineOutputTableFormBean.partitionColumnName"/]
[#assign partitionColumnNameError][@spring.showErrors "<br>" "error"/][/#assign]

[@spring.bind path="dataCollectionWizardFormBean.defineOutputTableFormBean.autoFilledDateColumnName"/]
[#assign autoFilledDateColumnNameError][@spring.showErrors "<br>" "error"/][/#assign]

<div class="CustomContent2">
    Data Collection Type:
    <span>${dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType.description}</span> <span
        class="Space50"></span> Generic Process Name:
    <span>${dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionName}</span>
</div>

<h2>Define output table for generic process </h2>

<div class="CustomContent3 ErrorMessages"  style="[#if !errorMessage?has_content && !newColumnError?has_content
&& !dateColumnError?has_content && !partitionColumnNameError?has_content && !autoFilledDateColumnNameError?has_content]display:none[/#if]">
    <h4>Error Message </h4>

    [#if errorMessage?has_content]
    <p>${errorMessage}</p>
    [/#if]

    [#if newColumnError?has_content]
    <p>${newColumnError}</p>
    [/#if]

	[#if dateColumnError?has_content]
    <p>${dateColumnError}</p>
    [/#if]
	
	
	[#if partitionColumnNameError?has_content]
    <p>${partitionColumnNameError}</p>
    [/#if]

     [#if autoFilledDateColumnNameError?has_content]
            <p>${autoFilledDateColumnNameError}</p>
        [/#if]

</div>


<div class="CustomContent1">
    <h4>Instructions</h4>
    <a href="#d" class="Expandable">
        <span class="HideContent"> [ - ] Hide</span>
        <span class="ShowContent" style="display:none"> [ + ] Show</span>
    </a>

    <div class="ExpandableContent">
        <p>Please select the destination database table for the Data Collection output.</p>
    </div>
</div>

<br>
<table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
    <tr>
        <td class="Labeltd"><label for="">Output Table Option </label> <span class="RequiredField">*</span></td>
        <td class="inputltd RadioInputs" colspan="2">
            [#if !dataCollectionWizardFormBean.editMode]
            <label class="FirstRadioOption">
                <input id="defineOutputTableFormBean.outputTableOption0"
                       name="defineOutputTableFormBean.outputTableOption"
                       value="CREATE_NEW"
                       type="radio" 
                [#if dataCollectionWizardFormBean.defineOutputTableFormBean.outputTableOption == 'CREATE_NEW']checked[/#if]>Define new table
            </label>
            [/#if]
            <label class="SecondRadioOption">
                <input id="defineOutputTableFormBean.outputTableOption1"
                       name="defineOutputTableFormBean.outputTableOption"
                       value="USER_EXISTING"
                [#if dataCollectionWizardFormBean.defineOutputTableFormBean.outputTableOption == 'USER_EXISTING']checked[/#if]
                type="radio">Use existing table </label>
        </td>
    </tr>
</table>

<div class="" >
    [#if dataCollectionWizardFormBean.extractSourceColumnFormBean.columns?has_content]
    [#assign selectedColumns = 0/]
    [#list dataCollectionWizardFormBean.extractSourceColumnFormBean.columns as column]
    [#if column.selected]
    [#assign selectedColumns = selectedColumns + 1/]
    [/#if]
    [/#list]
    [/#if]

    [#if dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns?has_content]
    [#assign expressionColumns = dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns?size/]
    [#else]
    [#assign expressionColumns = 0/]
    [/#if]
    <div class="FirstRadioOptionRow"
         style="[#if dataCollectionWizardFormBean.defineOutputTableFormBean.outputTableOption != 'CREATE_NEW']display:none[/#if]"	>
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
            <tr>
                <td class="Labeltd"><label for="">New Table Name</label></td>
                <td class="inputltd " colspan="2">
                    <font color="grey"> ${dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionName}</font>
                </td>
            </tr>
        </table>
    </div>

    <h2 class = "ExpandTrigger">Data Collection Columns ( ${selectedColumns + expressionColumns} )</h2>

    <div class="Grid SmallGrid">
        <table cellspacing="0" border="0" cellpadding="0" width="100%" class="FormTable">
            <thead>
            <tr>
                <th width="40%"> Name</th>
                <th width="40%"> Type</th>
                <th width="20%"> Default value</th>
            </tr>
            </thead>
            <tbody>
            [#if dataCollectionWizardFormBean.extractSourceColumnFormBean.columns?has_content]
            [#list dataCollectionWizardFormBean.extractSourceColumnFormBean.columns as column]
            [#if column.selected]
            <tr>
                <td> ${column.name} </td>
                <td> [#if column.type?has_content]${column.type.description}[#else]Unknown[/#if]</td>
                <td> [#if column.defaultValue?has_content]${column.defaultValue}[/#if]</td>
            </tr>
            [/#if]
            [/#list]
            [/#if]

            [#if dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns?has_content]
            [#list dataCollectionWizardFormBean.defineSQLColumnsFormBean.sqlExpressionColumns as column]
            <tr>
                <td> ${column.name} </td>
                <td> [#if column.type?has_content]${column.type.description}[#else]Unknown[/#if]</td>
                <td> [#if column.defaultValue?has_content]${column.defaultValue}[/#if]</td>
            </tr>
            [/#list]
            [/#if]
            </tbody>
        </table>
    </div>

</div>

<div class="SecondRadioOptionRow" style="[#if dataCollectionWizardFormBean.defineOutputTableFormBean.outputTableOption != 'USER_EXISTING']display:none[/#if]">
    <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
        <tr>
            <td class="Labeltd"> <label for="cd-dropdown">Select Table</label> <span class="RequiredField">*</span></td>
            <td class="inputltd" colspan="2">
                [#assign onChangeEvent][@spring.url '/dataCollection/define/outputTable/existingTableColumns'/][/#assign]
                [@spring.formSingleSelect
                "dataCollectionWizardFormBean.defineOutputTableFormBean.existingTable"
                dataCollectionWizardFormBean.defineOutputTableFormBean.existingTablesNames
                "class=\"cd-select required error cd-dropdown\"  name=\"cd-dropdown\" onchange=\"navigateTo('" + onChangeEvent + "', 'POST', 'myform');\""/]
            </td>
        </tr>
    </table>
    <br>

    [#-- selected table columns --]
    <h2 class="ExpandTrigger">Output Table Columns ( [#if dataCollectionWizardFormBean.defineOutputTableFormBean.existingTableColumns?has_content]${dataCollectionWizardFormBean.defineOutputTableFormBean.existingTableColumns?size}[#else]0[/#if] ) </h2>
    <div class="Grid SmallGrid">
        <table cellspacing="0" border="0" cellpadding="0" width="100%" class="FormTable">
            <thead>
            <tr>
                <th width="30%"> Name </th>
                <th width="40%"> Type </th>
                <th width="30%"> Default value </th>
            </tr>
            </thead>
            <tbody>
            [#if dataCollectionWizardFormBean.defineOutputTableFormBean.existingTableColumns?has_content]
            [#list dataCollectionWizardFormBean.defineOutputTableFormBean.existingTableColumns as existingTableColumn]
            <tr>
                <td> ${existingTableColumn.name} </td>
                <td> ${dataCollectionWizardFormBean.defineOutputTableFormBean.databaseDataTypes[existingTableColumn.typeCode+""]}</td>
                <td>
                    [#if existingTableColumn.defaultValue?has_content]${existingTableColumn.defaultValue}[/#if]
                    [@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.existingTableColumns[${existingTableColumn_index}].name"/]
                    [@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.existingTableColumns[${existingTableColumn_index}].typeCode"/]
                    [@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.existingTableColumns[${existingTableColumn_index}].defaultValue"/]
                    [@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.existingTableColumns[${existingTableColumn_index}].kpiType"/]
                </td>
            </tr>
            [/#list]
            [/#if]
            </tbody>
        </table>
    </div>
    [@spring.formHiddenInput "dataCollectionWizardFormBean.defineOutputTableFormBean.tableSelected"/]
    [#if dataCollectionWizardFormBean.defineOutputTableFormBean.tableSelected || newColumnError?has_content]
    <br>

    <h2>New Columns ( [#if dataCollectionWizardFormBean.defineOutputTableFormBean.newOutputTableColumns?has_content]${dataCollectionWizardFormBean.defineOutputTableFormBean.newOutputTableColumns?size}[#else]0[/#if] )</h2>

    <div class="Grid SmallGrid">
        <table cellspacing="0" border="0" cellpadding="0" width="100%" class="FormTable">
            <thead>
            <tr>
                <th width="25%"> Name</th>
                <th width="25%"> Type</th>
                <th width="25%"> Default value</th>
                <th width="15%"> Actions</th>
            </tr>
            </thead>
            <tbody>
            [#if dataCollectionWizardFormBean.defineOutputTableFormBean.newOutputTableColumns?has_content]
            [#list dataCollectionWizardFormBean.defineOutputTableFormBean.newOutputTableColumns as newOutputCol]
            <tr>
                <td><span>${newOutputCol.name}</span>
                    <input type="hidden"
                           name="defineOutputTableFormBean.newOutputTableColumns[${newOutputCol_index}].name"
                           id="defineOutputTableFormBean.newOutputTableColumns[${newOutputCol_index}].name"
                           value="${newOutputCol.name}"
                           class="CustomInput specialChars"/>

                </td>
                <td>
                    <span>${newOutputCol.type.description} </span>
                    <input type="hidden"
                           name="defineOutputTableFormBean.newOutputTableColumns[${newOutputCol_index}].type"
                           id="defineOutputTableFormBean.newOutputTableColumns[${newOutputCol_index}].type"
                           value="${newOutputCol.type}"
                           class="CustomInput"/>
                </td>
                <td>
                    <span>${newOutputCol.defaultValue}</span>
                    <input type="hidden"
                           name="defineOutputTableFormBean.newOutputTableColumns[${newOutputCol_index}].defaultValue"
                           id="defineOutputTableFormBean.newOutputTableColumns[${newOutputCol_index}].defaultValue"
                           value="${newOutputCol.defaultValue}"
                           class="CustomInput specialChars"/>
                </td>
                <td>
                    <a href="#" class="DeleteUser" onclick="navigateTo('[@spring.url '/dataCollection/define/outputTable/deleteColumn?index=' + newOutputCol_index/]' , 'POST', 'myform');"> Delete</a>
                </td>
            </tr>
            [/#list]
            [/#if]
            [#if dataCollectionWizardFormBean.defineOutputTableFormBean.sourceColumn?has_content]
            <tr class="NewRow">
                <td class="firstChild">
                    <input type="text" name="defineOutputTableFormBean.sourceColumn.name"
                           id="defineOutputTableFormBean.sourceColumn.name"
                           value="${dataCollectionWizardFormBean.defineOutputTableFormBean.sourceColumn.name}"
                           class="CustomInput specialChars">
                </td>
                <td>
                    <select class="CustomSelect" name="defineOutputTableFormBean.sourceColumn.type" id="defineOutputTableFormBean.sourceColumn.type">
                        [#list dataCollectionWizardFormBean.defineOutputTableFormBean.dataTypes?keys as type]
                        <option value="${type}">${dataCollectionWizardFormBean.defineOutputTableFormBean.dataTypes[type].description}</option>
                        [/#list]</select>
                </td>
                <td>
                    <input type="text" name="defineOutputTableFormBean.sourceColumn.defaultValue"
                           id="defineOutputTableFormBean.sourceColumn.defaultValue"
                           value="${dataCollectionWizardFormBean.defineOutputTableFormBean.sourceColumn.defaultValue}"
                           class="CustomInput specialChars">
                </td>
                <td class="lastChild">
                    <a href="#" class="UpdateAction" onclick="validateAndNavigat('[@spring.url '/dataCollection/define/outputTable/addColumn'/]');"> Update</a>
                    <a href="#" onclick="cancelAddNewColumnTableRow();" class="CancelAction"> Cancel</a>
                </td>
            </tr>
            [/#if]

            </tbody>
        </table>
    </div>
    <div class="GridHeader">
            <span class="SubmitButtons AddAction AddNewColumnTableRow">
              <span class="SubmitButton "> <a href="#Add"> Add Column</a> </span>
            </span>
    </div>
    <br>
    [/#if]
</div>

<div>
    <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
        <tr>
            <td colspan="2">
            </td>
        </tr>
        <tr>
        	 <td colspan="2">
        	 </td>
        </tr>
       [#if  dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType != 'DB' ]
       <tr>
          <td colspan="2" class="inputltd RadioInputs WithHint">
        		  <input type="checkbox" class="AutoFilledDateOption"
        		       id="defineOutputTableFormBean.addAutoFilledDateColumn"
        		       name="defineOutputTableFormBean.addAutoFilledDateColumn"
        		       [#if dataCollectionWizardFormBean.defineOutputTableFormBean.addAutoFilledDateColumn]checked[/#if]/>
        		           <label for="">Add Auto Filled Date column</label>
          </td>
       </tr>
       <tr class="AutoFilledDateColumnName"
       [#if dataCollectionWizardFormBean.defineOutputTableFormBean.addAutoFilledDateColumn== false]style="display:none"[/#if]>
          <td class="Labeltd" nowarp="nowarp"><label for="">Column Name</label><span class="RequiredField">*</span></td>
             <td class="inputltd" >
                 <span class="text AutoFilledDateColumnName specialChars">
                       [@spring.formInput "dataCollectionWizardFormBean.defineOutputTableFormBean.autoFilledDateColumnName" "maxlength=\"30\" class=\"specialChars\""/]
                  </span>
              </td>
       </tr>
       <tr style="border-bottom: 1px #ccc solid;">
                	<td></td>
                	<td></td>
       </tr>
       [/#if]
       <tr>
        	<td colspan="2">
        	</td>
       </tr>
       <tr>
              <td colspan="2" class="inputltd RadioInputs WithHint">
                        <input type="checkbox" class="TruncateOption"
                           id="defineOutputTableFormBean.truncateBeforeInsertion"
                           name="defineOutputTableFormBean.truncateBeforeInsertion"
                       [#if dataCollectionWizardFormBean.defineOutputTableFormBean.truncateBeforeInsertion]checked[/#if]/>
                               <label for="">Truncate Before Insertion</label>
              </td>
      </tr>
      <tr class="DateColumnName"
       		[#if dataCollectionWizardFormBean.defineOutputTableFormBean.truncateBeforeInsertion == false]style="display:none"[/#if]>
               <td class="Labeltd" nowarp="nowarp"><label for="">Based on date column</label></td>
               <td class="inputltd">
                       <span class="text DateColumnName specialChars">
       					[@spring.formInput "dataCollectionWizardFormBean.defineOutputTableFormBean.dateColumn" "maxlength=\"30\" class=\"specialChars\""/]
       				</span>
               </td>

      </tr>
	  
	     <tr style="border-bottom: 1px #ccc solid;">
                	<td></td>
                	<td></td>
       </tr>
	   
	    <tr>
        	<td colspan="2">
        	</td>
       </tr>
	  
<tr class="SecondRadioOptionRow" style="[#if dataCollectionWizardFormBean.defineOutputTableFormBean.outputTableOption != 'USER_EXISTING']display:none[/#if]">
              <td colspan="2" class="inputltd RadioInputs WithHint">
                        <input type="checkbox" class="PartitionedOption0"
                           id="defineOutputTableFormBean.isPartitioned0" readonly="readonly"
                           name="defineOutputTableFormBean.isPartitioned" disabled="disabled"
                       [#if dataCollectionWizardFormBean.defineOutputTableFormBean.isPartitioned]checked[/#if]    />
                               <label for="">Is Partitioned</label>
              </td>
</tr>
       


<tr class="EnabledPartition" style="[#if dataCollectionWizardFormBean.defineOutputTableFormBean.outputTableOption == 'USER_EXISTING' || dataCollectionWizardFormBean.editMode]display:none[/#if]">
              <td colspan="2" class="inputltd RadioInputs WithHint">
                        <input type="checkbox" class="PartitionedOption"
                           id="defineOutputTableFormBean.isPartitioned1"
                           name="defineOutputTableFormBean.isPartitioned"
                       [#if dataCollectionWizardFormBean.defineOutputTableFormBean.isPartitioned]checked[/#if]    />
                               <label for="">Is Partitioned</label>
              </td>
 </tr>
       
  
       

      <tr class="PartitionColumnName"
       		[#if dataCollectionWizardFormBean.defineOutputTableFormBean.isPartitioned == false]style="display:none"[/#if]>
               <td class="Labeltd" nowarp="nowarp"><label for="">Partition Column Name</label></td>
               <td class="inputltd">
                       <span class="text PartitionColumnName specialChars">
       					[@spring.formInput "dataCollectionWizardFormBean.defineOutputTableFormBean.partitionColumnName" "maxlength=\"30\" class=\"specialChars\""/]
       				</span>
               </td>

      </tr>
	     
	  
     [#if  dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType != 'DB' ]
      <tr style="border-bottom: 1px #ccc solid;">
               	<td></td>
               	<td></td>
      </tr>
      [/#if]
      <tr>
            <td colspan="2">
            </td>
        </tr>
       <tr>
            <td class="Labeltd" style="white-space:nowrap"><label for="">"NODE_NAME" Column option</label><span class="RequiredField">*</span>
            </td>
            <td colspan="2" class="inputltd RadioInputs WithHint">

                    [#list dataCollectionWizardFormBean.defineOutputTableFormBean.nodeNameTypes?keys as nodeNameType]

                                [#assign className]
                                [#if dataCollectionWizardFormBean.defineOutputTableFormBean.nodeName?has_content &&
                                    dataCollectionWizardFormBean.defineOutputTableFormBean.nodeName == nodeNameType]selectedlable[#else][/#if]
                                [/#assign]
      			    			<label class="${className}">
                                    <input type="radio"  class="${className}" size="25"
                                    name="defineOutputTableFormBean.nodeName"
                                    id="defineOutputTableFormBean.nodeName${nodeNameType_index}"
                                    [#if className?has_content]checked="checked"[/#if]
                                    [#if dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType == 'XML' &&
                                    dataCollectionWizardFormBean.extractXMLSourceColumns.xmlComplexity == 'VENDOR_SPECIFIC' && dataCollectionWizardFormBean.defineOutputTableFormBean.nodeNameTypes[nodeNameType].description == 'Mapped']
                                    disabled='disabled'[/#if]
                                    value="${nodeNameType}">
                                    ${dataCollectionWizardFormBean.defineOutputTableFormBean.nodeNameTypes[nodeNameType].description}
                                    <div class="SelectionHint" style="display:none">
                                        <div class="SelectionHintInner">
                                            <div>Hint</div>
                                            ${dataCollectionWizardFormBean.defineOutputTableFormBean.nodeNameTypes[nodeNameType].hint}
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
</div>

<div class="WizardBtns">

    [#if dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType == 'TEXT']

    <span class="SubmitButtons FloatLeft Gray">
        <span class="SubmitButton BtnWidth1">
        <input type="submit" value="Back" class="submit BtnWidth1"
               onclick="updateForm('[@spring.url '/dataCollection/define/sqlColumns?back=true'/]', 'POST', 'myform');">
        </span>
    </span>

    [#elseif dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType == 'DB']
    <span class="SubmitButtons FloatLeft Gray">
        <span class="SubmitButton BtnWidth1">
            <input type="submit" value="Back" class="submit BtnWidth1"
                   onclick="updateForm('[@spring.url '/dataCollection/define/extractSourceColumns?back=true'/]', 'POST', 'myform');">
        </span>
    </span>
    [#elseif dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType == 'XML' &&
    dataCollectionWizardFormBean.extractXMLSourceColumns?has_content &&
    dataCollectionWizardFormBean.extractXMLSourceColumns.xmlComplexity == 'SIMPLE']
    <span class="SubmitButtons FloatLeft Gray">
        <span class="SubmitButton BtnWidth1">
            <input type="submit" value="Back" class="submit BtnWidth1"
                   onclick="updateForm('[@spring.url '/dataCollection/define/sqlColumns?back=true'/]', 'POST', 'myform');">
         </span>
    </span>
    [#elseif dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType == 'XML' &&
    dataCollectionWizardFormBean.extractXMLSourceColumns?has_content &&
    dataCollectionWizardFormBean.extractXMLSourceColumns.xmlComplexity == 'VENDOR_SPECIFIC' &&
    !dataCollectionWizardFormBean.editMode]
    <span class="SubmitButtons FloatLeft Gray">
        <span class="SubmitButton BtnWidth1">
            <input type="submit" value="Back" class="submit BtnWidth1"
                   onclick="updateForm('[@spring.url '/dataCollection/define/extractXMLSourceColumns?back=true'/]', 'POST', 'myform');">
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
               onclick="updateForm('[@spring.url '/dataCollection/define/outputTable'/]', 'POST', 'myform');">
        </span>
    </span>
</div>

</form>

[/@wizard.body]