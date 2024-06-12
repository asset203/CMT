[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]
[#import "../grid-component.ftl" as component/]
[#global title="Capacity Management Tool"/]
[#global isDCSelected="selected"/]

[@cmt.htmlHead ""/]
[@cmt.navigation/]

<script type="text/javascript" src="[@spring.url '/js/common.js'/]"></script>
<script>

    $(document).ready(function () {

    $("a.DeleteUser").fancybox({
    padding:0,
    closeBtn:false
    });
    $(".DeleteBtns .SubmitButtons.Gray").click(function () {
    parent.$.fancybox.close();
    });
    });

    function deleteDataCollectionButKeepTable(dataCollectionName) {

        var url = '[@spring.url '/dataCollection/delete/'/]' + dataCollectionName+'/false';
        //    $.post(url, function (data) {
        //      var newDoc = document.open("text/html", "replace");
        //      newDoc.write(data);
        //      newDoc.close();
        //      return false;
        //    });
        $("#deleteDataCollectionName").text(dataCollectionName);
        $("#deleteDataCollectionLink").attr("href", url);

        }
    function deleteDataCollection(dataCollectionName) {

    var url = '[@spring.url '/dataCollection/delete/'/]' + dataCollectionName+'/true';
    //    $.post(url, function (data) {
    //      var newDoc = document.open("text/html", "replace");
    //      newDoc.write(data);
    //      newDoc.close();
    //      return false;
    //    });
    $("#deleteDataCollectionName").text(dataCollectionName);
    $("#deleteDataCollectionLink").attr("href", url);

    }

    function editDataCollection(dataCollectionName) {

    var url = '[@spring.url '/dataCollection/edit'/]';
    $('#postForm').attr("action", url);
    $('#hiddenField').html("<input type='hidden' name='dataCollectionName' value='" + dataCollectionName +"'/>");
    $('#postForm').submit();

    }

</script>
[@cmt.dummyForm/]
<div class="TabContent ManageUserTabContent" id="content">
    <h1>Manage Data Collections</h1>
    [#if delete_error?has_content]
    <div class="CustomContent3 ErrorMessages">
        <h4>Error Message</h4>

        <p>${delete_error}</p>
    </div>
    [/#if]
    [#if create?has_content && create]
    <div class="SuccessMsg"> Data collection has been saved successfully</div>
    [/#if]

    [#if delete?has_content && delete]
    <div class="SuccessMsg"> Data collection was deleted successfully</div>
    [/#if]

    <div class="GridHeader">
    <span class="SubmitButtons"> <a href="[@spring.url '/dataCollection/define/type'/]" class="SubmitButton">Define Data
        Collections</a> </span>
    </div>
    <h2>Search Data Collections</h2>
    [@cmt.loadingTxt/]
    <form id="myform" action="[@spring.url '/dataCollection/manage'/]" method="POST">
        <div class="GridHeader2">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
                <tbody>
                <tr>
                    <td class="Labeltd">
                        <label for="">Data Collection Name</label>
                    </td>
                    <td class="inputltd">
                        [@spring.formInput path="searchCriteria.keyword" attributes="maxlength=\"150\" class = \"specialChars CustomInput AutoCompleteInput\""/]
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="GridHeader">
    <span class="SubmitButtons">
		<span class="SubmitButton BtnWidth1">
               <input type="submit" value="Search" class="submit">
        </span>

	</span>
        </div>
    </form>
    [#--style="display:none"--]
    <div class="NextContent">
        <div class="GridHeader">

        </div>
        <div class="Grid">
            [#-- Grid Display --]
            <div id="dataCollectionGrid">
                [@component.datatable/]
            </div>
        </div>
    </div>

</div>
<div style="display:none">
    <div id="DeleteUser" class="delteUserPopUP">
        <div class="delteUserPopUPTitle">Delete Data Collection</div>
        <div class="delteUserPopUPInner">
            <div class="delteUserPopUPInner2">
                Are you sure you want to delete the data collection <span id="deleteDataCollectionName"></span>?
                <div class="DeleteBtns">
                        <span class="SubmitButtons "> <span class="SubmitButton BtnWidth1">
                            <a id="deleteDataCollectionLink" href="#">Confirm</a>
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