[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]
[#global title="Capacity Management Tool"/]
[#global isKPISelected="selected"/]

[#assign javaScript]
<script>
    $(document).ready(function() {
    collectiveScripts();
    });

    $(document).ajaxComplete(function(){

    $.ajaxSetup({
    cache: false
    });
    collectiveScripts();
    });

    function collectiveScripts(){

    $("#addExtForm").validate();
    $("#addRegForm").validate();
    $("#editExtForm").validate();
    $("#editRegForm").validate();

    $("a.DeleteUser").fancybox({
    padding : 0,
    closeBtn : false,
    });
    $(".DeleteBtns .SubmitButtons.Gray").click(function() {
    parent.$.fancybox.close();
    });

    $("#UserForm2").validate();

    $('#Pager').smartpaginator({
    totalrecords: [#if  formBean.kpiNotificationList??
    &&  formBean.kpiNotificationList.contactList??]
    ${ formBean.kpiNotificationList.contactList?size}[#else]0[/#if],
    recordsperpage: 10,
    datacontainer: 'tablePager',
    dataelement: 'tr', initval: 0,
    next: 'Next', prev: 'Prev',
    first: 'First',
    last: 'Last',
    length:5
    });

    loadAutoComplete();

    //The table sorter must be after the AutoComplete fields else it won't work
    $("table.tablesorter").tablesorter({
    widgets: ['zebra','repeatHeaders']
    });
    }

    function closeWindow(divID){
    $("#" + divID).hide();
    }

    function deleteUser(id, userName){
    $("#userID").val(id);
    $("#deletedUserName").text('user ' + userName);
    }

    function deleteUsers(){

    var ids   = new Array()
    jQuery("input:checkbox[name=userChkbox]:checked").each(function(){
    ids.push(this.value)
    })

    $("#userID").val(ids);
    $("#deletedUserName").text('the selected User\s');

    }

    function submitDeleteUsers(){
    $.fancybox.close();
    var deleteID = $("#userID").val();
    if(deleteID != ""){
    var url = '[@spring.url '/kpiNotifications/deleteUsers'/]';

    var htmlHidden = "<input type='hidden' name='systemName' value='" + $("._systemName").val() +"'/>"
    + "<input type='hidden' name='nodeID' value='" + $("._nodeID").val() +"'/>"
    + "<input type='hidden' name='deleteID' value='" + deleteID +"'/>";

    $('#postForm').attr("action", url);
    $('#hiddenField').html(htmlHidden);
    $('#postForm').submit();


    }else{
    $(".noSelDel").show();
    }
    }

    function editUser(contactID, isExternal){

    $.ajaxSetup({
    cache: false
    });

    if(isExternal == 'true'){
    jQuery("#editExtUserAjax")
    .load('[@spring.url '/kpiNotifications/editExtUser/'/]' + contactID, "",
    function(response){
    if(response) {
    jQuery("#editExtUserAjax").show();
    } else {
    jQuery("#editExtUserAjax").hide();
    }
    });
    }else{
    jQuery("#editRegUserAjax")
    .load('[@spring.url '/kpiNotifications/editRegUser/'/]' + contactID, "",
    function(response){
    if(response) {
    jQuery("#editRegUserAjax").show();
    } else {
    jQuery("#editRegUserAjax").hide();
    }
    });
    }
    }

    function loadAutoComplete(){
    var availableTags0 = [
    [#if systemList?? && systemList?size > 0]
    [#list systemList as system]"${system}"[#if system_has_next],[/#if][/#list]
    [/#if]
    ];

    $( ".AutoCompleteInput0" ).autocomplete({
    source: availableTags0
    });

    var availableTags1 = [
    [#if usersList?? && usersList?size > 0]
    [#list usersList as user]"${user.username}"[#if user_has_next],[/#if][/#list]
    [/#if]
    ];

    $( ".AutoCompleteInput1" ).autocomplete({
    source: availableTags1
    });

    $(".AutoCompleteInput0").autocomplete({
    change: function(event, ui) {

    var jqxhr = $.post("[@spring.url '/kpiNotifications/loadNodes'/]",
    {systemName:$(".AutoCompleteInput0").val()}, function() {
    $("#viewNodes").show();
    })
    .done(function(data) {
    $( "#viewNodes" ).html(data);
    })
    .fail(function(data) {
    $( "#viewNodes" ).html("<div class='ErrorMsg'>An error has occurred</div>");
    });
    }
    });

    $(".AutoCompleteInput1").autocomplete({
    change: function(event, ui) {

    var jqxhr = $.post("[@spring.url '/kpiNotifications/loadRegisteredUser'/]",
    {username:$(".AutoCompleteInput1").val(),
    systemName:$("._systemName").val() , nodeID:$("._nodeID").val()}, function() {
    $("#addRegUser").show();
    $(".FirstRadioOptionRow").show();
    $(".SecondRadioOptionRow").hide();
    })
    .done(function(data) {
    $( "#addRegUser" ).html(data);
    })
    .fail(function(data) {
    $( "#addRegUser" ).html("<div class='ErrorMsg'>An error has occurred</div>" + data);
    });
    }
    });
    }
</script>
[/#assign]
[@cmt.htmlHead javaScript/]
[@cmt.navigation/]

[@cmt.dummyForm/]
<div class="TabContent ManageUserTabContent">
    <h1>Manage Notifications list </h1>
    <div class="CustomContent4">
        <a href="[@spring.url '/manageKpis/systemKpis'/]" class="bluelable"> System KPIs </a>  &nbsp; &nbsp;   |   &nbsp; &nbsp; Manage Notifications list
    </div>

    [#-- Success/Error Message location --]
    [#if successMsg??]${successMsg}[/#if]
    [#if errorMsg??]${errorMsg}[/#if]

    <h2>Manage Notification List </h2>
    [#-- Hidden values section --]
    <input class="_contactListID" type="hidden"
           value="[#if formBean.contactListID??]${formBean.contactListID}[/#if]"/>

    <input class="_systemName" type="hidden"
           value="[#if formBean.systemName??]${formBean.systemName}[/#if]"/>

    <input class="_nodeID" type="hidden"
           value="[#if formBean.nodeID??]${formBean.nodeID?c}[/#if]"/>

    [#-- end of hidden values section --]
    [@cmt.loadingTxt/]
    <form action="[@spring.url '/kpiNotifications/findList'/]" method="post" id="myform">
        <div class="GridHeader2">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
                <tr>
                    <td class="Labeltd"> <label for="cd-dropdown">System Name</label> <span class="RequiredField">*</span></td>
                    <td class="inputltd" colspan="2">
                        [@spring.formInput path="formBean.systemName"
                        attributes="
                        class=\"CustomInput AutoCompleteInput AutoCompleteInput0 specialChars error\"" /]
                        [@spring.showErrors "<br>" "error"/]
                    </td>
                </tr>
                <tr class="viewNodes" id="viewNodes">
                    [#if nodeList??]
                    [#if nodeList?has_content]
                    <td class="Labeltd"> <label for="cd-dropdown1">Select Node</label> <span class="RequiredField">*</span></td>
                    <td colspan="2" class="inputltd">
                        [#assign selectedNode][#if formBean.nodeID??]${formBean.nodeID?c}[#else]-1[/#if][/#assign]

                        [@spring.bind path="formBean.nodeID"/]
                        <select class="cd-select required error" name="${spring.status.expression}">
                            <option value="-1">Please Select</option>
                            [#list nodeList as node]
                            [#if selectedNode == ((node.id?c)?string)]selected[#else]not equal[/#if]
                            <option value="${node.id?c}" [#if selectedNode == ((node.id?c)?string)]selected[/#if]>${node.name}</option>
                            [/#list]
                        </select>
                        [@spring.showErrors "<br>" "error"/]
                    </td>
                    [#else]
                    <td class="Labeltd"> <label for="cd-dropdown1">Select Node</label> <span class="RequiredField">*</span></td>
                    <td class="inputltd RadioInputs" colspan="2">
                        <div class="NoResults">The selected system has no nodes</div>
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
                    <input type="submit" class="submit" value="Manage Users List">
                </span>
            </span>
        </div>
    </form>
    [#if formBean.kpiNotificationList??]
    [#-- Add User section --]
    <h2>Add User</h2>
    <div class="GridHeader2">
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
            <tr>
                <td class="Labeltd"> <label for="">User Type</label> <span class="RequiredField">*</span></td>
                <td class="inputltd RadioInputsSmall" colspan="2">
                    <label class="FirstRadioOption">
                        <input id="" name="radio3" type="radio" checked>Existing user
                    </label>
                    <label class="SecondRadioOption">
                        <input id="" name="radio3" type="radio"
                        [#if VALIDATION_ERROR_ADD_EXT??]checked[/#if]>
                        External user
                    </label>
                </td>
            </tr>
        </table>
        <div id="addRegUser" class="FirstRadioOptionRow" [#if VALIDATION_ERROR_ADD_EXT??]style="display:none"[/#if]>
        [#include "addRegisteredUser.ftl"]
    </div>
    <div id="addExtUser" class="SecondRadioOptionRow"
    [#if VALIDATION_ERROR_ADD_EXT??]style="display:block"[#else]style="display:none"[/#if]>
    [#include "addExternalUser.ftl"]
</div>
</div>
[#-- end of add user section --]
[#-- Edit User section --]
<div id="editRegUserAjax" style="display:[#if VALIDATION_ERROR_EDIT_REGISTERED??]block[#else]none[/#if];">
    [#if VALIDATION_ERROR_EDIT_REGISTERED?? && VALIDATION_ERROR_EDIT_REGISTERED == 'true']
    [#include "editRegisteredUser.ftl"]
    [#else]<div class="loading"></div>
    [/#if]
</div>
<div id="editExtUserAjax" style="display:[#if VALIDATION_ERROR_EDIT_EXTERNAL??]block[#else]none[/#if];">
    [#if VALIDATION_ERROR_EDIT_EXTERNAL?? && VALIDATION_ERROR_EDIT_EXTERNAL == 'true']
    [#include "editExternalUser.ftl"]
    [#else]<div class="loading"></div>
    [/#if]
</div>
[#-- end of edit user section --]
[#-- contacts table section --]
[#if formBean.kpiNotificationList.contactList?has_content]
<h2>Selected Users  </h2>
<div class="CustomContent4">
    <a onClick="deleteUsers();" class="DeleteUser" href="#DeleteUser"> Delete Selected</a>
    <span style="display:none;" class="noSelDel"><label class="error">No users were selected.</label></span>
</div>
<div class="Grid">
    <table cellspacing="0" border="0" cellpadding="0" width="100%" class="tablesorter" id="tablePager">
        <col style="width:3%">
        <col style="width:18%">
        <col style="width:18%">
        <col style="width:15%">
        <col style="width:17%">
        <col style="width:17%">
        <thead>
        <tr>
            <th>  </th>
            <th>Username</th>
            <th>Email</th>
            <th>Mobile Number</th>
            <th class="{sorter: false}" style="cursor:default;">Notify By</th>
            <th class="{sorter: false}" style="cursor:default;">Actions</th>
        </tr>
        </thead>
        <tbody>
        [#list formBean.kpiNotificationList.contactList as contact]
        <tr>
            [#if contact.registeredContact??]
            <td>
                <input id="userChkbox" name="userChkbox"
                       type="checkbox" value="${contact.id?c}"/>
            </td>
            <td>${contact.registeredContact.username}</td>
            <td>
                [#if contact.registeredContact.email??]
                ${contact.registeredContact.email}
                [/#if]
            </td>
            <td>
                [#if contact.registeredContact.mobile??]
                ${contact.registeredContact.mobile}
                [/#if]
            </td>
            <td>
                [#assign emailStatus=" "/]
                [#assign smsStatus=" "/]
                [#if contact.notificationType == "Email"
                || contact.notificationType?index_of("Email") != -1]
                [#assign emailStatus="checked"/]
                [/#if]
                [#if contact.notificationType == "SMS"
                || contact.notificationType?index_of("SMS") != -1]
                [#assign smsStatus="checked"/]
                [/#if]
                <div class="RadioInputsSmall">
                    <label>
                        <input id="" name="chkBox${contact_index}" size="25" class="" minlength="2"
                               type="checkbox" value="email" disabled ${emailStatus}/>
                        Email
                    </label>
                    <label>
                        <input id="" name="chkBox${contact_index}" size="25" class="" minlength="2"
                               type="checkbox" value="Mobile" disabled ${smsStatus}/>
                        SMS
                    </label>
                </div>
            </td>
            <td>
                <a  href="javascript:editUser(${contact.id?c}, 'false');"
                    class="EditUser"> Edit</a>
                <a class="DeleteUser" href="#DeleteUser"
                   onclick="javascript:deleteUser('${contact.id?c}',
                                                                '${contact.registeredContact.username}');">
                    Delete</a>
            </td>
            [#elseif contact.externalContact??]
            <td>
                <input id="userChkbox" name="userChkbox"
                       type="checkbox" value="${contact.id?c}"/>
            </td>
            <td>${contact.externalContact.userName}</td>
            <td>
                [#if contact.externalContact.eMail??]
                ${contact.externalContact.eMail}
                [/#if]
            </td>
            <td>
                [#if contact.externalContact.mobileNum??]
                ${contact.externalContact.mobileNum}
                [/#if]
            </td>
            <td>
                [#assign emailStatus=" "/]
                [#assign smsStatus=" "/]
                [#if contact.notificationType == "Email"
                || contact.notificationType?index_of("Email") != -1]
                [#assign emailStatus="checked"/]
                [/#if]
                [#if contact.notificationType == "SMS"
                || contact.notificationType?index_of("SMS") != -1]
                [#assign smsStatus="checked"/]
                [/#if]
                <div class="RadioInputsSmall">
                    <label>
                        <input id="" name="chkBox${contact_index}" size="25" class=""
                               minlength="2" type="checkbox" value="email" disabled ${emailStatus}/>
                        Email
                    </label>
                    <label>
                        <input id="" name="radio1" size="25" class=""
                               minlength="2" type="checkbox" value="Mobile" disabled ${smsStatus}/>
                        SMS
                    </label>
                </div>
            </td>
            <td>
                <a href="javascript:editUser(${contact.id?c}, 'true');"
                   class="EditUser"> Edit</a>
                <a class="DeleteUser" href="#DeleteUser"
                   onclick="javascript:deleteUser('${contact.id?c}',
                                                                    '${contact.externalContact.userName}');">
                    Delete</a>
            </td>
            [/#if]
        </tr>
        [/#list]
        </tbody>
    </table>
    <div id="Pager" class="Paging" > </div>
</div>
[/#if]
[#-- end of contacts table section--]
[/#if]
</div> [#-- end of main page DIV--]
[#-- Delete popup --]
<div style="display:none">
    <div id="DeleteUser" class="delteUserPopUP">
        <div class="delteUserPopUPTitle">Delete User</div>
        <div class="delteUserPopUPInner">
            <div class="delteUserPopUPInner2">
                Are you sure you want to delete <span id="deletedUserName"></span>?
                <div class="DeleteBtns">
                    [@spring.bind path="formBean.userIDToDelete"/]
                    <input type="hidden" id="userID"
                           name="${spring.status.expression}" class="userID" style="" />

					<span class="SubmitButtons ">
						<span class="SubmitButton BtnWidth1">
						   <input type="submit"  value="Yes" onclick="submitDeleteUsers();">
						</span>
					</span>
					<span class="SubmitButtons Gray ">
						<span class="SubmitButton BtnWidth1">
							<input type="reset"  value="Cancel">
						</span>
					</span>
                </div>
            </div>
        </div>
    </div>
</div>
[#-- End of delete popup --]
[@cmt.footer/]