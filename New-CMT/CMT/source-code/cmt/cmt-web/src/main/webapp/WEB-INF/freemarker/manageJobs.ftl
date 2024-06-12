[#ftl/]
[#import "spring.ftl" as spring /]
[#import "macros.ftl" as cmt /]
[#import "grid-component.ftl" as component/]
[#global title="Capacity Management Tool"/]

[#global isDCJobsSelected="selected"/]
[#assign javaScript]
<script>
    $(document).ready(function () {
    collectiveScript();
    });

    $(document).ajaxComplete(function(){
    collectiveScript();
    });


    function deleteJob(jobName) {
    $("#deletedJob").text(jobName);
    }

    function submitDeleteJob(){
    var url = '[@spring.url '/jobManagement/deleteJob'/]';
    $('#postForm').attr("action", url);
    $('#hiddenField').html("<input type='hidden' name='jobName' value='" + $("#deletedJob").text() +"'/>");
    $('#postForm').submit();
    }

    function updateJob(jobName){
    var url = '[@spring.url '/jobManagement/initUpdateJob'/]';
    $('#postForm').attr("action", url);
    $('#hiddenField').html("<input type='hidden' name='jobName' value='" + jobName +"'/>");
    $('#postForm').submit();
    }

    function forceJob(jobName){
    var url = '[@spring.url '/jobManagement/goToForceJobExecution'/]';

    $('#postForm').attr("action", url);
    $('#hiddenField').html("<input type='hidden' name='jobName' value='" + jobName +"'/>");
    $('#postForm').submit();
    }

    function collectiveScript(){

    $("a.DeleteUser").fancybox({
    padding:0,
    closeBtn:false,
    });
    $(".DeleteBtns .SubmitButtons.Gray").click(function () {
    parent.$.fancybox.close();
    });
    $("table.tablesorter").tablesorter({
    widgets: ['zebra','repeatHeaders']
    });


    }

    function hideDataTable(){
        $('#datatable-load').hide();
    }

</script>

[/#assign]
[@cmt.htmlHead javaScript /]
[@cmt.navigation/]
[#import "grid-component.ftl" as component/]

[@cmt.dummyForm/]

<div class="TabContent ManageUserTabContent">
    <h1>Manage Data Collections jobs</h1>
    [#if successMsg??]${successMsg}[/#if]
    [#if errorMsg??]${errorMsg}[/#if]
    <div class="GridHeader">
    <span class="SubmitButtons"> <a href="[@spring.url '/jobManagement/jobDetails'/]" class="SubmitButton">Define New
        job</a> </span>
    </div>
    <h2>Search jobs</h2>
    [@cmt.loadingTxt/]
    <form action="[@spring.url '/jobManagement/list'/]" id="myform">
        <div class="GridHeader2">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
                <tbody>
                <tr>
                    <td class="Labeltd">
                        <label>Job Name</label>
                    </td>
                    <td class="inputltd">
                        [@spring.formInput path="searchCriteria.keyword" attributes=" class=\"CustomInput AutoCompleteInput error specialChars\"" /]
                        [@spring.showErrors "<br>" "errors"/]
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="GridHeader">
                <span class="SubmitButtons ">
                    <span class="SubmitButton BtnWidth1">
                         <input type="submit" value="Search"  onclick="javascript:this.disabled='disabled';hideDataTable();" class="submit">
                    </span>
                </span>
        </div>
    </form>

    [#-- Grid Display --]
    <div id="datatable-load">
        [@component.datatable/]
    </div>

</div>

<div style="display:none">
    <div id="DeleteUser" class="delteUserPopUP">
        <div class="delteUserPopUPTitle">Delete Job</div>
        <div class="delteUserPopUPInner">
            <div class="delteUserPopUPInner2">
                Are you sure you want to delete "<span id="deletedJob"></span>"?
                <div class="DeleteBtns">
					<span class="SubmitButtons "> <span class="SubmitButton BtnWidth1">
						<a id="deleteUserLink" href="javascript:submitDeleteJob();" onclick="javascript:this.disabled='disabled'">Confirm</a>
					</span>
					</span>
					<span class="SubmitButtons Gray ">
						<span class="SubmitButton BtnWidth1">
							<input type="submit"  value="Cancel">
						</span>
					</span>
                </div>
            </div>
        </div>
    </div>
</div>

[@cmt.footer/]

