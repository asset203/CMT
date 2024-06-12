[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]
[#global title="Capacity Management Tool"/]

[#global isLogMgrSelected="selected"/]


[#assign javaScript]
<script type="text/javascript">


function deleteUser(id, userName){
$("#userID").val(id);
$("#deletedUserName").text(userName);
}

function showDesc(){
var errorCode = $('.errorSel').val();
if(errorCode == '-1'){
$('.HintBlock').hide();
}else{
var desc= $('.desc'+errorCode).val();
if(desc != null && desc != ''){
$('.hintErrorDesc').html(desc);
$('.HintBlock').show();
}

}
}

function editUser(userID, isExternal){

$.ajaxSetup({
cache: false
});

if(isExternal == 'true'){
jQuery("#editExtUserAjax")
.load('[@spring.url '/logNotification/editExtUser/'/]' + userID, "",
function(response){
if(response) {
jQuery("#editExtUserAjax").css('display', 'block');
} else {
jQuery("#editExtUserAjax").css('display', 'none');
}
});
}else{

jQuery("#editRegUserAjax")
.load('[@spring.url '/logNotification/editRegUser/'/]' + userID, "",
function(response){
if(response) {
jQuery("#editRegUserAjax").css('display', 'block');
} else {
jQuery("#editRegUserAjax").css('display', 'none');
}
});
}
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
var url = '[@spring.url '/logNotification/deleteUsers'/]';

var htmlHidden = "<input type='hidden' name='notificationID' value='" + $(".notificationID").val() +"'/>"
+ "<input type='hidden' name='deleteID' value='" + deleteID +"'/>";

$('#postForm').attr("action", url);
$('#hiddenField').html(htmlHidden);
$('#postForm').submit();


}else{
$(".noSelDel").show();
}
}


function closeWindow(divID){
$("#" + divID).hide();
}


$(document).ready(
function() {

$("#addExtForm").validate();
$("#addRegForm").validate();
$("#editExtForm").validate();
$("#editRegForm").validate();

$.ajaxSetup({
cache: false
});


$(".AutoCompleteInput1").autocomplete({
change: function(event, ui) {

var jqxhr = $.post("[@spring.url '/logNotification/loadRegisteredUser'/]",
{username:$(".AutoCompleteInput1").val(), notificationListID:$(".notificationID").val(),
systemName:$("._systemName").val() , logType:$("._logType").val(), errorCodeID:$("._errorCodeID").val()}, function() {
$("#addRegUser").css('display', 'block');
$(".FirstRadioOptionRow").show();
$(".SecondRadioOptionRow").hide();
})
.done(function(data) {
$( "#addRegUser" ).html(data);
})
.fail(function(data) {
$( "#addRegUser" ).html("<div class='ErrorMsg'>An error has occurred</div>");
});
}
});



$("a.DeleteUser").fancybox({
padding : 0,
closeBtn : false,
});
$(".DeleteBtns .SubmitButtons.Gray").click(function() {
parent.$.fancybox.close();
});

$('#Pager').smartpaginator({
totalrecords: [#if dcLogNotificationForm.dcLogNotificationList??
&& dcLogNotificationForm.dcLogNotificationList.contactsList??]
${dcLogNotificationForm.dcLogNotificationList.contactsList?size}[#else]0[/#if],
recordsperpage: 10,
datacontainer: 'tablePager', dataelement: 'tr', length:5 });

loadAutoComplete();

$("table.tablesorter").tablesorter({
widgets: ['zebra','repeatHeaders']
});



[#if dcLogNotificationForm?? && dcLogNotificationForm.errorCodeID?? && dcLogNotificationForm.errorCodeID?has_content]
var desc= $('.desc'+${dcLogNotificationForm.errorCodeID}).val();
if(desc != null && desc != ''){
$('.hintErrorDesc').html(desc);
$('.HintBlock').show();
}
[/#if]
});

$(document).ajaxComplete(function(){

$("#addExtForm").validate();
$("#addRegForm").validate();
$("#editExtForm").validate();
$("#editRegForm").validate();

$.ajaxSetup({
cache: false
});


$(".AutoCompleteInput1").autocomplete({
change: function(event, ui) {

var jqxhr = $.post("[@spring.url '/logNotification/loadRegisteredUser'/]",
{username:$(".AutoCompleteInput1").val(), notificationListID:$(".notificationID").val(),
systemName:$("._systemName").val() , logType:$("._logType").val(), errorCodeID:$("._errorCodeID").val()}, function() {
$("#addRegUser").css('display', 'block');
$(".FirstRadioOptionRow").show();
$(".SecondRadioOptionRow").hide();
})
.done(function(data) {
$( "#addRegUser" ).html(data);
})
.fail(function(data) {
$( "#addRegUser" ).html("<div class='ErrorMsg'>An error has occurred</div>");
});
}
});

loadAutoComplete();

$("table.tablesorter").tablesorter({
widgets: ['zebra','repeatHeaders']
});

});
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
}


</script>
[/#assign]
[@cmt.htmlHead javaScript/]
[@cmt.navigation/]
[@cmt.dummyForm/]

<div class="TabContent ManageUserTabContent">
    <h1>Log Manager</h1>
    <div class="CustomContent4">
        <a href="[@spring.url '/logManager/viewLogs'/]" class="bluelable">Display Log</a> &nbsp; &nbsp;   |   &nbsp; &nbsp; Manage Log Notifications list
    </div>
    [#if successMsg??]${successMsg}[/#if]
    [#if errorMsg??]${errorMsg}[/#if]
    <h2>Manage Notification List </h2>
    [#if dcLogNotificationForm.systemName??][@spring.formHiddenInput path="dcLogNotificationForm.systemName"/][/#if]
    [#if dcLogNotificationForm.errorCodeID??][@spring.formHiddenInput path="dcLogNotificationForm.errorCodeID"/][/#if]
    [@cmt.loadingTxt/]
    <form id="myform" action="[@spring.url '/logNotification/findList'/]" method="post">
        <div class="GridHeader2">
            [#if dcLogNotificationForm.logType??]
            [@spring.formHiddenInput path="dcLogNotificationForm.logType"/]
            [#else]
            [@spring.bind path="dcLogNotificationForm.logType"/]
            <input type="hidden" name="${spring.status.expression}" value="ERROR">
            [/#if]
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
                <tr>
                    <td class="Labeltd"> <label for="cd-dropdown">System Name</label> <span class="RequiredField">*</span></td>
                    <td class="inputltd" colspan="2">
                        [@spring.formInput path="dcLogNotificationForm.systemName"
                        attributes="
                        class=\"CustomInput AutoCompleteInput AutoCompleteInput0 specialChars error\"" /]
                        [@spring.showErrors "<br>" "error"/]
                    </td>
                </tr>
                <tr>
                    <td class="Labeltd WithHint"> <label for="cd-dropdown2">Error Code List</label> <span class="RequiredField">*</span></td>
                    <td class="inputltd" colspan="2">
                        [#assign selectedError][#if dcLogNotificationForm.errorCodeID??]${dcLogNotificationForm.errorCodeID?c}[#else]-1[/#if][/#assign]
                        [@spring.bind path="dcLogNotificationForm.errorCodeID"/]
                        <select id="cd-dropdown2" class="cd-select required error errorSel" name="${spring.status.expression}"  onChange="showDesc();">
                            <option value="-1">All</option>
                            [#list errorCodes as errorCode]
                            <option value="${errorCode.id?c}" [#if (errorCode.id?c) == selectedError]selected[/#if]>
                            ${errorCode.shortDesc}
                            </option>
                            [/#list]
                        </select>
                        [@spring.showErrors "<br>" "error"/]
                        <div class="HintBlock" style="display:none;">
                            <div class="SelectionHint2">
                                <div class="SelectionHintInner2"> <div>Hint</div>
                                    <p class="hintErrorDesc"></p>
                                </div>
                            </div>
                        </div>
                        [#list errorCodes as errorCode]
                        <input type="hidden" value="${errorCode.description}" class="desc${errorCode.id}"/>
                        [/#list]
                    </td>
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

    <input class="notificationID" type="hidden"
           value="[#if dcLogNotificationForm.dcLogNotificationList??
			    && dcLogNotificationForm.dcLogNotificationList.id??]${dcLogNotificationForm.dcLogNotificationList.id}[/#if]"/>
    <input class="_systemName" type="hidden"
           value="[#if dcLogNotificationForm.systemName??]${dcLogNotificationForm.systemName}[/#if]"/>
    <input class="_logType" type="hidden"
           value="[#if dcLogNotificationForm.logType??]${dcLogNotificationForm.logType}[/#if]"/>
    <input class="_errorCodeID" type="hidden"
           value="[#if dcLogNotificationForm.errorCodeID??]${dcLogNotificationForm.errorCodeID}[#else]-1[/#if]"/>


    [#if dcLogNotificationForm.dcLogNotificationList??]
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

[#if dcLogNotificationForm.dcLogNotificationList.contactsList?has_content]
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
        [#list dcLogNotificationForm.dcLogNotificationList.contactsList as contact]
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
[/#if]
</div>
<script>
    [#if dcLogNotificationForm?? && dcLogNotificationForm.errorCodeID?? && dcLogNotificationForm.errorCodeID?has_content]
    var desc= $('.desc'+${dcLogNotificationForm.errorCodeID}).val();
    if(desc != null && desc != ''){
    $('.hintErrorDesc').html(desc);
    $('.HintBlock').show();
    }
    [/#if]
</script>
<div style="display:none">
    <div id="DeleteUser" class="delteUserPopUP">
        <div class="delteUserPopUPTitle">Delete User</div>
        <div class="delteUserPopUPInner">
            <div class="delteUserPopUPInner2">
                Are you sure you want to delete the user <span id="deletedUserName"></span>?
                <div class="DeleteBtns">
                    [@spring.bind path="dcLogNotificationForm.userIDToDelete"/]
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
[@cmt.footer/]