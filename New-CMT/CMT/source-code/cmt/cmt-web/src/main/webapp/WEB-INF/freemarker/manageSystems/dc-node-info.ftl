[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]
[#import "../grid-component.ftl" as component/]
[#global title="Capacity Management Tool"/]
[#global isDCSystemSelected="selected"/]
[#assign javaScript]
<script>
    $(document).ready(function() {
	$("a.DeleteUser").fancybox({
  				padding : 0,
  				closeBtn : false,
  			});
  			$(".DeleteBtns .SubmitButtons.Gray").click(function() {
  				parent.$.fancybox.close();
  			});
  			$("table.tablesorter").tablesorter({
  				widgets: ['zebra','repeatHeaders']
  			});


  		});
  			$('#Pager').smartpaginator({ totalrecords: [#if dcNodeFormBean?? && dcNodeFormBean.node?? && dcNodeFormBean.node.inputsList??]${dcNodeFormBean.node.inputsList?size}[#else]0[/#if], recordsperpage:5,
  			datacontainer: 'tablePager', dataelement: 'tr', initval: 0, next: 'Next', prev: 'Prev', first: 'First', last: 'Last', length:5 });




function editNodeInput(inputName) {

          var url = '[@spring.url '/manageInput/editInput/'/]' + inputName;
          $.post(url, function (data) {
            var newDoc = document.open("text/html", "replace");
            newDoc.write(data);
            newDoc.close();
            return false;
          });

        } ;
		function deleteNodeInput(inputName)
        {
			var deleteURL = '[@spring.url '/manageInput/deleteNodeInput/'/]' + inputName;
            $("#deletedName").text('the Input ' + inputName);
			$("#deleteTitel").text('Delete Input');
            $("#deleteLink").attr("href", deleteURL);
        }

		function validateAllFormSpecialChars(){
			var field1 = validateTextNotContainigSpecialCharactersOnSubmit('.nodeName', '.nodeNameError');
			var field2 = validateTextNotContainigSpecialCharactersOnSubmit('.nodeDes', '.nodeDesError');
			return field1&&field2;
		}
</script>
<script src="[@spring.url '/js/data-collection.js'/]" type="text/javascript"></script>
<script src="[@spring.url '/js/common-validation.js'/]" type="text/javascript"></script>
[/#assign]
[@cmt.htmlHead javaScript/]
[@cmt.navigation/]
<div class="TabContent">
            <h1>[#if dcNodeFormBean.editMode == true]Edit[#else] Define[/#if] Node</h1>
            [#if create?has_content && create]
             		<div class="SuccessMsg">  Data collection Input has been saved successfully  </div>
             	[/#if]
             	[#if delete?has_content && delete]
             		<div class="SuccessMsg">  Data collection Input was deleted successfully  </div>
             	[/#if]
            [@spring.bind path="dcNodeFormBean.node.inputsList"/]
            [#assign inputsListError][@spring.showErrors "<br>" "validationerror"/][/#assign]
		     [#if errors?has_content || inputsListError?has_content]
                          <div class="CustomContent3 ErrorMessages">
                               <h4>Error Message </h4>

                              [#if errors?has_content]
                                   <p>${errors}</p>
                              [/#if]

                             [#if inputsListError?has_content]
                                <p>${inputsListError}</p>
                              [/#if]

                         </div>
            [/#if]

			<div class="FormContainer">
			  <form id="nodeForm" method="POST"
			  onSubmit="return validateAllFormSpecialChars();"
			  action ="[@spring.url '/manageNodes/saveDcNode'/]" >
				[@spring.formHiddenInput "dcNodeFormBean.editMode" /]
				[@spring.formHiddenInput "dcNodeFormBean.systemName" /]
				[@spring.formHiddenInput "dcNodeFormBean.systemDescription" /]
				[@spring.formHiddenInput "dcNodeFormBean.newSystem" /]
				<table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
					<tr>
						<td class="Labeltd">
							<label for="">Node Name </label>
							<span class="RequiredField">*</span>
						</td>
						<td class="inputltd">
							<span class="text">
							    [@spring.formInput path="dcNodeFormBean.node.name" attributes=" maxlength=\"50\" class=\"nodeName\" onblur=\"validateTextNotContainigSpecialCharactersOnSubmit('.nodeName', '.nodeNameError');\""/]
								[@spring.showErrors "<br>" "error"/]
								<label class=" nodeNameError" id="nodeNameError"></label>
							 </span>
						</td>
					</tr>
					<tr>
						<td class="Labeltd">
							<label for="">Node Description  </label>
						</td>
						<td class="inputltd  WithHint">
							<span class="text">
							    [@spring.formInput path="dcNodeFormBean.node.description" attributes=" maxlength=\"50\" class=\"nodeDes\" onblur=\"validateTextNotContainigSpecialCharactersOnSubmit('.nodeDes', '.nodeDesError');\""/]
							    [@spring.showErrors "<br>" "error"/]
								<label class=" nodeDesError" id="nodeDesError"></label>
							</span>

						</td>
					</tr>

					<tr>
						<td class="Labeltd textareaLable"><label for=""> Node Inputs <span class="RequiredField">*</span></label> </td>
						<td class="inputltd">
							<span class="SubmitButtons">
								<a href="#"
onclick="navigateTo('[@spring.url '/manageInput/defineNodeInput'/]', 'POST', 'nodeForm');"
								class="SubmitButton">Create Input</a>
							</span>
							<div class="Grid SmallGrid fixedGrid" style="padding:10px 0 0">
							[#if dcNodeFormBean.node?? && dcNodeFormBean.node.inputsList?? && dcNodeFormBean.node.inputsList?has_content]
                                   <table cellspacing="0" border="0" cellpadding="0" width="100%" class="tablesorter" id="tablePager">
                                            <thead>
                                                    <tr>
                                                        <th><span>Input Name  </span></th>
                                                        <th><span>Actions</span></th>
                                                    </tr>
                                            </thead>
                                            <tbody>
                                                    [#list dcNodeFormBean.node.inputsList as input]
                                             <tr>
                                                            <td>${input.id}</td>
                                               <td>
                                                                <a class="EditUser"
																onclick="navigateTo('[@spring.url '/manageInput/editNodeInput/${input.id}'/]', 'POST', 'nodeForm');"
                                                                    href="#"> Edit</a>
                                                                <a class="DeleteUser" href="#DeleteUser"
                                                                   onclick="javascript:deleteNodeInput('${input.id}')"> Delete</a>
                                              </td>
                                          </tr>
                                          [/#list]
                                          </tbody>
                                   </table>
							[#else]
								<div class=" Grid SmallGrid fixedGrid NoResults" >
									No Inputs defined
                            	</div>
                            [/#if]
                                <div id="Pager" class="Paging" > </div>
                            </div>
						</td>
					</tr>
					<tr>
						<td class="Labeltd "><label for=""> </label>  </td>
						<td class="inputltd">
							<span class="SubmitButtons ">
								<span class="SubmitButton BtnWidth1">
                                		<input type="submit"  value="Save" class="submit" onclick="javascript:this.disabled='disabled'">
                                </span>
							</span>
							<span class="SubmitButtons Gray ">
                            	<a href="[@spring.url '/manageSystem/editSystem'/][#if Session.dcSystemFormBean.system.name??]/${Session.dcSystemFormBean.system.name}[/#if]" class="SubmitButton BtnWidth1">Cancel</a>
                            </span>
						</td>
					</tr>
				</table>
			  </form>
			</div>
		</div>
		<div style="display:none">
        	<div id="DeleteUser" class="delteUserPopUP">
        		<div class="delteUserPopUPTitle"><span id="deleteTitel"></span></div>
        		<div class="delteUserPopUPInner">
        		    <div class="delteUserPopUPInner2">
        				Are you sure you want to delete  <span id="deletedName"></span> ?
        				<div class="DeleteBtns">
                                <span class="SubmitButtons "> <span class="SubmitButton BtnWidth1">
                                    <a id="deleteLink" href="#" onclick="javascript:this.disabled='disabled'">Yes</a>
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