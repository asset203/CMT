[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]
[#global title="Capacity Management Tool"/]
[#global isKPISelected="selected"/]
[#assign javaScript]
<script>
    $(document).ready(function () {
    collectiveScripts();
    });

    $(document).ajaxComplete(function () {
    $.ajaxSetup({
    cache:false
    });
    collectiveScripts();
    });

    function collectiveScripts() {


    loadAutoComplete();

    //The table sorter must be after the AutoComplete fields else it won't work
    //      $("table.tablesorter").tablesorter({
    //        widgets:['zebra', 'repeatHeaders']
    //      });

    bindDeleteClickHandler();
    $("#DeleteUser").fancybox({
    padding:0,
    closeBtn:false
    });
    $(".DeleteBtns .SubmitButtons.Gray").click(function () {
    parent.$.fancybox.close();
    });

    }


    function loadAutoComplete() {
    var availableTags0 = [
    [#if systemList?? && systemList?size > 0]
    [#list systemList as system]"${system}"[#if system_has_next],[/#if][/#list]
    [/#if]
    ];

    $(".AutoCompleteInput0").autocomplete({
    source:availableTags0
    });


    $(".AutoCompleteInput0").autocomplete({
    select:function (event, ui) {

    var jqxhr = $.post("[@spring.url '/manageKpis/loadNodes'/]",
    {systemName:ui.item.value}, function () {
    $("#viewNodes").show();
    })
    .done(function (data) {
    $("#viewNodes").html(data);

    $("#cd-dropdown1").change(function () {

    verifyNoThresholdsExists();
    resetNodeProperties();
    removePropertiesTableRows();

    })
    .change();

    })
    .fail(function (data) {
    $("#viewNodes").html("<div class='ErrorMsg'>An error has occurred</div>");
    });
    }
    });
    }

    function verifyNoThresholdsExists() {

    var systemName = $("#systemName").val();
    var nodeId = $("#cd-dropdown1").val();

    $.getJSON("[@spring.url '/manageKpis/verifyNoThresholdExists'/]",
    {systemName:systemName, nodeId:nodeId}, function (exist) {

    if (exist) {
    // TODO add a confirm box to confirm overite or cancel and call resetNodeDropDown


    $('.nodeError').text('This node already has previously saved properties and will be replaced!');
    $('.nodeError').addClass("error");

    } else {

    $('.nodeError').text('');
    $('.nodeError').removeClass("error");
    }
    });


    $.getJSON("[@spring.url '/manageKpis/isNodeUsed'/]",
            {nodeId:nodeId},
            function (used) {
                if (used) {
                  $('input:checkbox[name=inUse]').prop('checked',true);
                } else
                {
                     $('input:checkbox[name=inUse]').prop('checked',false);
                }
            });




    }

    function resetNodeProperties() {

    document.getElementById('nodeProperties.propertyName').value = '';
    document.getElementById('nodeProperties.value').value = '0';
    document.getElementById('nodeProperties.grain').selectedIndex = 0;
    document.getElementById('nodeProperties.trafficTableName').value = '';
    document.getElementById('nodeProperties.notificationThreshold').value = '0.0';

    }
    function removePropertiesTableRows() {
    if ($("#propertiesTable tbody tr")) {
    $("#propertiesTable tbody tr").remove();
    }
    if ($("#propertiesTable")) {
    $("#propertiesTable").css("display", "none");
    }

    listElementsCounter = 0;
    }
    function hidePropertyForm(){
    $("#propertyForm").html("");
    $("#loadingTxt").show();
    }
</script>


<script type="text/javascript" src="[@spring.url '/js/system-kpi-properties-edit.js'/]"></script>
<script type="text/javascript" src="[@spring.url '/js/common-validation.js'/]"></script>
[/#assign]
[@cmt.htmlHead javaScript/]
[@cmt.navigation/]
<div class="TabContent">
    <h1>Add New Thresholds & KPIs </h1>

[@spring.bind path="formBean.systemName"/]
[#assign invalidSystemErr][@spring.showErrors "<br>" "validationerror"/][/#assign]
    <div class="FormContainer">
        <form action="[@spring.url '/manageKpis/saveNewKpi'/]"
              onSubmit="hidePropertyForm();return validateTextNotContainigSpecialCharactersOnSubmit('.systemName', '.systemNameError');"
              method="post">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
                <tr>
                    <td class="Labeltd"><label for="">System Name </label> <span class="RequiredField">*</span></td>
                    <td class="inputltd">
                        [@spring.formInput path="formBean.systemName"
                        attributes=" size=\"25\"  minlength=\"2\"
                        class=\"CustomInput AutoCompleteInput AutoCompleteInput0 systemName \"" /]
                        <br>
                        <label class=" systemNameError" id="systemNameError"></label>
                        <br>
                        [#if invalidSystemErr?has_content]
                        	${invalidSystemErr}
                        [/#if]
                    </td>
                </tr>
                <tr class="viewNodes" id="viewNodes">
                    [#if nodeList?? && nodeList?has_content]
                    <td class="Labeltd"><label for="cd-dropdown1">Select Node</label> <span class="RequiredField">*</span></td>
                    <td class="inputltd RadioInputs" colspan="2">
                        [#assign selectedNode][#if formBean.nodeID??]${formBean.nodeID?c}[#else]-1[/#if][/#assign]
                        [@spring.bind path="formBean.nodeID"/]
                        <select id="cd-dropdown1" class="cd-select required error " name="${spring.status.expression}">
                            <option value="-1">Please Select</option>
                            [#list nodeList as node]
                                <option value="${node.systemNodeId?c}"
                                    [#if selectedNode == (node.systemNodeId?c)]selected[/#if] >${node.nodeName}
                                </option>
                            [/#list]
                        </select>

                        [@spring.showErrors "<br>" "error"/]
                    </td>
                    [#else]
                    [#if formBean.systemName?? && formBean.systemName?has_content && invalidSystemErr == '']
                    <td class="Labeltd"><label for="cd-dropdown1">Select Node</label> <span class="RequiredField">*</span>
                    </td>
                    <td class="inputltd RadioInputs" colspan="2">
                        <div class="NoResults" id="noNode">The selected system has no nodes</div>
                        [@spring.formHiddenInput path="formBean.nodeID"/]
                        [@spring.showErrors "<br>" "error"/]
                    </td>
                    [/#if]
                    [/#if]
                </tr>
                <tr>
                    <td colspan="2"><label id="nodeError" class="nodeError"></label></td>
                </tr>
                <tr>
                    <td class="Labeltd"><label for="">In Use</label> <span class="RequiredField">*</span></td>
                    <td class="inputltd RadioInputsSmall">
                        <label> [@cmt.formCheckbox path="formBean.inUse" /]Yes</label>
                    </td>
                </tr>
            </table>
            <hr>
            <div id="nodePropertiesDiv">
                [#include "nodeProperties.ftl"/]
            </div>


            <div class="WizardBtns">
					<span class="SubmitButtons ">
						<span class="SubmitButton BtnWidth1"><input type="submit" value="Save" class="submit"> </span></span>
					<span class="SubmitButtons Gray "> <span class="SubmitButton BtnWidth1">
            <input type="reset" value="Cancel" onClick="location.href='[@spring.url '/manageKpis/systemKpis'/]'"></span></span>
            </div>

        </form>
    </div>

</div>
<div class="clear"></div>

[@cmt.footer/]