[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]
[#global title="Capacity Management Tool"/]
[#global isKPISelected="selected"/]

[#assign javaScript]
<script type="text/javascript" src="[@spring.url '/js/common-validation.js'/]"></script>
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


    $("a.DeleteUser").fancybox({
    padding:0,
    closeBtn:false
    });
    $(".DeleteBtns .SubmitButtons.Gray").click(function () {
    parent.$.fancybox.close();
    });

    $("#UserForm2").validate();


    loadAutoComplete();

    }
    //The table sorter must be after the AutoComplete fields else it won't work
    $("table.tablesorter").tablesorter({
    widgets:['zebra', 'repeatHeaders']
    });

    function closeWindow(divID) {
    $("#" + divID).hide();
    }

    function deleteUser(id, userName) {
    $("#userID").val(id);
    $("#deletedUserName").text(userName);
    }

    function deleteUsers() {

    var ids = new Array()
    jQuery("input:checkbox[name=userChkbox]:checked").each(function () {
    ids.push(this.value)
    })

    $("#userID").val(ids);
    $("#deletedUserName").text('the selected User\s');

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
    })
    .fail(function (data) {
    $("#viewNodes").html("<div class='ErrorMsg'>An error has occurred</div>");
    });
    }
    });


    }

    function deleteNodeProperties(nodeId) {
    systemNameValue = $('#systemName').val();
    deleteUrl = $('#deleteUserLink').attr('href') + nodeId + '/' + systemNameValue;
    $("#deleteUserLink").attr("href", deleteUrl);


    }

</script>
[/#assign]
[@cmt.htmlHead javaScript/]
[@cmt.navigation/]


<div class="TabContent ManageUserTabContent">
    <h1>KPI Notifications </h1>
    [#if successMsg??]${successMsg}[/#if]
    [#if errorMsg??]${errorMsg}[/#if]
    <div class="CustomContent4">
        System KPIs &nbsp; &nbsp; | &nbsp; &nbsp; <a href="[@spring.url '/kpiNotifications/manageLists'/]"
                                                     class="bluelable">Manage Notifications list </a>
    </div>
    <div class="GridHeader">
				<span class="SubmitButtons DisplayLogBtn">
					 <a href="[@spring.url '/manageKpis/initNewKpi'/]" class="SubmitButton"> Add New Thresholds & KPIs</a>
				</span>
    </div>
    <h2>System KPIs</h2>

   [@spring.bind path="formBean.systemName"/]
   [#assign invalidSystemErr][@spring.showErrors "<br>" "error"/][/#assign]

    <form action="[@spring.url '/manageKpis/showThresholdAndKpis'/]"
          onSubmit="return validateTextNotContainigSpecialCharactersOnSubmit('.systemName', '.systemNameError');"
          method="post">
        <div class="GridHeader2">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
                <tr>
                    <td class="Labeltd"><label for="cd-dropdown">System Name</label> <span class="RequiredField">*</span></td>
                    <td class="inputltd" colspan="2">
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
                            [#if selectedNode == (node.systemNodeId?c)]selected[/#if]>${node.nodeName}</option>
                            [/#list]
                        </select>
                        [@spring.showErrors "<br>" "error"/]
                    </td>
                    [#else]
                    [#if (formBean.systemName?? && formBean.systemName?has_content && (invalidSystemErr == ''))]
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
            </table>
        </div>

        <div class="GridHeader">
                <span class="SubmitButtons ">
                    <span class="SubmitButton BtnWidth1">
                        <input type="submit" class="submit" value="Show Thresholds & KPIs">
                    </span>
                </span>
        </div>
    </form>
    [#if formBean.nodePropertiesList??]
    [#if formBean.nodePropertiesList?has_content]
    <div id="kpiDetailsContainer">
        <h2>KPI Details</h2>

        <div class="CustomContainer5">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="DataTable">
                <tr>
                    <td class="Labeltd"><label>In Use</label></td>
                    <td> [#if formBean.inUse]Yes[#else]No[/#if] </td>
                </tr>
            </table>

            <h2>Node Properties</h2>

            <div class="Grid SmallGrid fixedGrid">
                <table width="100%" cellspacing="0" cellpadding="0" border="0" class="tablesorter NodePropertyTable">
                    <thead>

                    <tr class="TableHeader">
                        <th width="15%"> Property</th>
                        <th width="25%"> Property Value</th>
                        <th width="15%"> Grain</th>
                        <th width="15%"> Traffic Table</th>
                        <th width="25%"> Notification Threshold %</th>
                    </tr>
                    </thead>
                    <tbody>
                    [#list formBean.nodePropertiesList as nodeProperties]
                    <tr>
                        <td> ${nodeProperties.propertyName}</td>
                        <td> ${nodeProperties.value}</td>
                        <td> ${nodeProperties.grain}</td>
                        <td> ${nodeProperties.trafficTableName}</td>
                        <td> ${nodeProperties.notificationThreshold?c}</td>
                    </tr>
                    [/#list]
                    </tbody>
                </table>
            </div>
        </div>

        <div class="GridHeader">
				<span class="SubmitButtons DisplayLogBtn">
					 <a class="SubmitButton BtnWidth1" href="[@spring.url '/manageKpis/editKpi/${formBean.nodeID?c}'/]"> Edit
                         KPI</a>
				</span>
				<span class="SubmitButtons DisplayLogBtn">
					 <a class="SubmitButton BtnWidth1 DeleteUser" href="#DeleteUser"
                        onclick="deleteNodeProperties(${formBean.nodeID?c})"> Delete</a>
				</span>
        </div>
    </div>


    [/#if]

    [/#if]
</div>
<div style="display:none">
    <div id="DeleteUser" class="delteUserPopUP">
        <div class="delteUserPopUPTitle">Delete KPI Threshold</div>
        <div class="delteUserPopUPInner">
            <div class="delteUserPopUPInner2">
                Are you sure you want to delete all KPI Thresholds?
                <br>Note: Associated Notification List will also be deleted.
                <div class="DeleteBtns">
        							<span class="SubmitButtons "> <span class="SubmitButton BtnWidth1">
        								<a id="deleteUserLink" href="[@spring.url '/manageKpis/deleteAllNodeProperties/'/]">Confirm</a>
        							</span>
        							</span>
        							<span class="SubmitButtons Gray ">
        								<span class="SubmitButton BtnWidth1">
        									<input type="submit" value="Cancel">
        								</span>
        							</span>
                </div>
            </div>
        </div>
    </div>
</div>
[@cmt.footer/]