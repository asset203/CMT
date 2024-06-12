[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]
[#import "../grid-component.ftl" as component/]
[#global title="Capacity Management Tool"/]
[#global isDCSystemSelected="selected"/]
[#assign javascript]
<script>
	 $(document).ready(function () {
		$("a.DeleteUser").fancybox({
					padding : 0,
					closeBtn : false,
				});
				$(".DeleteBtns .SubmitButtons.Gray").click(function() {
					parent.$.fancybox.close();
		});
  });

function editSystem(systemName) {
     var url = '[@spring.url '/manageSystem/editSystem/'/]' + systemName;
	  window.location.href = url;
    }

		function deleteSystem(systemName)
        {
			var deleteURL = '[@spring.url '/manageSystem/deleteSystem/'/]' + systemName;
            $("#deletedSystemName").text(systemName);
            $("#deleteSystemLink").attr("href", deleteURL);
        }

</script>
<script src="[@spring.url '/js/common-validation.js'/]" type="text/javascript"></script>
[/#assign]
[@cmt.htmlHead javascript/]
[@cmt.navigation/]
  <div class="TabContent ManageUserTabContent" id="content">
    <h1>Manage Data Collections Systems</h1>
      [#if create?has_content && create]
      		<div class="SuccessMsg">  Data collection System has been saved successfully  </div>
      	[/#if]
      	[#if delete?has_content && delete]
      		<div class="SuccessMsg">  Data collection System was deleted successfully  </div>
      	[/#if]
      	[#if delete_jobs_error?has_content && delete_jobs_error]
              		<div >  <label class="error">Jobs may not be deleted successfully, please try to delete them using manage jobs page</label> </div>
        [/#if]
     <div class="GridHeader">
        <span class="SubmitButtons">
        <a href="[@spring.url '/manageSystem/defineDcSystem/'/]" class="SubmitButton">Define System</a> </span>
     </div>
     <h2>Search Systems</h2>
       <form action="[@spring.url '/manageSystem/list'/]"
	   onSubmit="return validateTextNotContainigSpecialCharactersOnSubmit('.systemName', '.systemNameError');">
       <div class="GridHeader2">
         <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
           <tbody>
           <tr>
             <td class="Labeltd">
               <label for="">System Name</label>
             </td>
             <td class="inputltd">
               [@spring.formInput path="searchCriteria.keyword"
                    attributes=" class=\"systemName CustomInput AutoCompleteInput\" onblur=\"validateTextNotContainigSpecialCharactersOnSubmit('.systemName', '.systemNameError');\""/]
			   <label class=" systemNameError" id="systemNameError"></label>
             </td>
           </tr>
           </tbody>
         </table>
       </div>
      <div class="GridHeader">
        <span class="SubmitButtons ">
            <span class="SubmitButton BtnWidth1">
                 <input type="submit"  value="Search" onclick="javascript:this.disabled='disabled'" class="submit">
            </span>
        </span>
      </div>
      </form>
       <div class="NextContent">
         <div class="GridHeader">
           <h2>Search Systems</h2>
         </div>
         <div class="Grid">
         [#-- Grid Display --]
           <div id="systemGrid">
           [@component.datatable/]
           </div>
         </div>
       </div>

     </div>
	<div style="display:none">
	<div id="DeleteUser" class="delteUserPopUP">
		<div class="delteUserPopUPTitle">Delete System</div>
		<div class="delteUserPopUPInner">
		    <div class="delteUserPopUPInner2">
				Are you sure you want to delete the System <span id="deletedSystemName"></span> ?
				<br>
				Note: all system jobs will be deleted as well!
				<div class="DeleteBtns">
                        <span class="SubmitButtons "> <span class="SubmitButton BtnWidth1">
                            <a id="deleteSystemLink" href="#" onclick="javascript:this.disabled='disabled'">Yes</a>
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