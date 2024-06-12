[#ftl/]
[#import "spring.ftl" as spring /]
[#import "macros.ftl" as cmt /]
[#global title="Capacity Management Tool"/]

[#global isUserMgmtSelected="selected"/]

[#assign javascript]
<script>
    $(document).ready(function () {
    $('#Pager').smartpaginator({ totalrecords: [#if users??]${users?size}[#else]0[/#if], recordsperpage:10,
    datacontainer:'tablePager', dataelement:'tr', initval:0, next:'Next', prev:'Prev', first:'First', last:'Last', length:5 });


    $("a.DeleteUser").fancybox({
    padding:0,
    closeBtn:false,
    });
    $(".DeleteBtns .SubmitButtons.Gray").click(function () {
    parent.$.fancybox.close();
    });
    $("table.tablesorter").tablesorter({
    widgets:['zebra', 'repeatHeaders']
    });


    });
</script>
[/#assign]
[@cmt.htmlHead javascript/]
[@cmt.navigation/]

<script>
    function deleteUser(userName, deleteURL) {
    $("#deletedUserName").text(userName);
    $("#deleteUserLink").attr("href", deleteURL);
    }
</script>
<div class="TabContent ManageUserTabContent">
    <h1>Users Management </h1>
    [#if successMsg??]${successMsg}[/#if]
    [#if errorMsg??]${errorMsg}[/#if]
    [#assign logoutUrl]= [@spring.url '/logout'/][/#assign]
    [#if isCurrentUserRoleChanged?? && isCurrentUserRoleChanged]${response.sendRedirect(logoutUrl)}[[/#if]
    <div class="GridHeader">          
		<span class="SubmitButtons"> 
			<a href="[@spring.url '/userManagement/addUser'/]" class="SubmitButton"> Add New User</a> 
		</span>
        <div class="GridControl">
            <a href="[@spring.url '/userManagement/userManagement'/]" class="RefreshGrid">Refresh Grid</a>
            [#--<div> Display <input type="text"  value="10"> per page </div>--]
        </div>
    </div>
    [#if users??]
    <div class="Grid">
        <table cellspacing="0" border="0" cellpadding="0" width="100%" class="tablesorter" id="tablePager">
            <col width="20%">
            <col width="30%">
            <col width="20">
            <col width="10">
            <col width="10%">
            <col width="20%">
            <thead>
            <tr>
                <th><span>Username  </span></th>
                <th><span>Email</span></th>
                <th><span>Mobile</span></th>
                <th><span>Role</span></th>
                <th><span>Applications</span></th>
                <th class="{sorter: false}" style="cursor:default;"><span>Actions</span></th>
            </tr>
            </thead>
            <tbody>
            [#list users as user]
            <tr>
                <td>${user.username}</td>
                <td>[#if user.email??] ${user.email} [/#if]</td>
                <td> [#if user.mobile??] ${user.mobile} [/#if] </td>
                <td>${user.authority}</td>
                <td>${user.appsToAccess}</td>
                <td>
                    <a class="EditUser"
                       href="[@spring.url '/userManagement/editUser/${user.id?c}'/]"> Edit</a>
                    <a class="DeleteUser" href="#DeleteUser"
                       onclick="javascript:deleteUser('${user.username}',
                 '[@spring.url '/userManagement/deleteUser/${user.id?c}'/]');"> Delete</a>
                </td>
            </tr>
            [/#list]
            </tbody>
        </table>
        <div id="Pager" class="Paging"></div>
    </div>
    [#else] No registered users exist in the system
</div>
[/#if]
<div style="display:none">
    <div id="DeleteUser" class="delteUserPopUP">
        <div class="delteUserPopUPTitle">Delete User</div>
        <div class="delteUserPopUPInner">
            <div class="delteUserPopUPInner2">
                Are you sure you want to delete the user <span id="deletedUserName"></span>?
                <div class="DeleteBtns">
                        <span class="SubmitButtons "> <span class="SubmitButton BtnWidth1">
                            <a id="deleteUserLink" href="#">Confirm</a>
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