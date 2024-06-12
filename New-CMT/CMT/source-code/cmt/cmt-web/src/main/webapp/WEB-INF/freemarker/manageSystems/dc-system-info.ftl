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
        			$('#NodePager').smartpaginator({ totalrecords: [#if dcSystemFormBean?? && dcSystemFormBean.systemNodes??]${dcSystemFormBean.systemNodes?size}[#else]0[/#if], recordsperpage: 5,
        			datacontainer: 'tablePagerNode', dataelement: 'tr', initval: 0, next: 'Next', prev: 'Prev', first: 'First', last: 'Last', length:5 });

        			$('#InputsPager').smartpaginator({ totalrecords: [#if dcSystemFormBean?? && dcSystemFormBean.systemInputs??]${dcSystemFormBean.systemInputs?size}[#else]0[/#if], recordsperpage: 5,
                    datacontainer: 'tablePagerInput', dataelement: 'tr', initval: 0, next: 'Next', prev: 'Prev', first: 'First', last: 'Last', length:5 });



        		});
	  function deleteNode(nodeName)
        {
			var deleteURL = '[@spring.url '/manageNodes/deleteNode/'/]'+nodeName;
            $("#deletedName").text('the Node ' + nodeName);
			$("#deleteTitel").text('Delete Node');
            $("#deleteLink").attr("href", deleteURL);
            $("#jobConfirm").text('Note: all node jobs will be deleted as well!');
        }
		function editSystemInput(inputName) {

          var url = '[@spring.url '/manageInput/editInput/'/]' + inputName;
          $.post(url, function (data) {
            var newDoc = document.open("text/html", "replace");
            newDoc.write(data);
            newDoc.close();
            return false;
          });

        } ;
function deleteSystemInput(inputName)
        {
			var deleteURL = '[@spring.url '/manageInput/deleteSystemInput/'/]' + inputName;
            $("#deletedName").text('the Input ' + inputName);
			$("#deleteTitel").text('Delete Input');
            $("#deleteLink").attr("href", deleteURL);
             $("#jobConfirm").text('');
        }

		function validateAllFormSpecialChars(){
			var field1 = validateTextNotContainigSpecialCharactersOnSubmit('.systemName', '.systemNameError');
			var field2 = validateTextNotContainigSpecialCharactersOnSubmit('.systemDesc', '.systemDescError');
			return (field1&&field2);
		}
  </script>
<script src="[@spring.url '/js/data-collection.js'/]" type="text/javascript"></script>
<script src="[@spring.url '/js/common-validation.js'/]" type="text/javascript"></script>
[/#assign]
[@cmt.htmlHead javaScript/]
[@cmt.navigation/]
<div class="TabContent">

            [@spring.bind path="dcSystemFormBean.system.nodesList"/]
            [#assign emptySystemError][@spring.showErrors "<br>" "validationerror"/][/#assign]
			[@spring.formHiddenInput path="dcSystemFormBean.editMode"/]
			<h1>[#if dcSystemFormBean.editMode == true]Edit[#else] Define[/#if] System</h1>
			[#if create_node?has_content && create_node]
            		<div class="SuccessMsg">  Data collection Node has been saved successfully  </div>
            	[/#if]
            	[#if delete_node?has_content && delete_node]
            		<div class="SuccessMsg">  Data collection Node was deleted successfully  </div>
            	[/#if]
            	[#if create_input?has_content && create_input]
                		<div class="SuccessMsg">  Data collection Input has been saved successfully  </div>
                	[/#if]
                	[#if delete_input?has_content && delete_input]
                		<div class="SuccessMsg">  Data collection Input was deleted successfully  </div>
                	[/#if]
                [#if delete_jobs_error?has_content && delete_jobs_error]
                     <div > <label class="error"> Node job may not  be deleted successfully, please try to delete it using manage jobs page </label> </div>
                [/#if]
			[#if errors?has_content || emptySystemError?has_content]
                      <div class="CustomContent3 ErrorMessages">
                            <h4>Error Message </h4>

                        [#if errors?has_content]
                             <p>${errors}</p>
                       [/#if]

                      [#if emptySystemError?has_content]
                            <p>${emptySystemError}</p>
                      [/#if]

                  </div>
               [/#if]
			<div class="FormContainer">
			  <form id="systemForm"  method="POST"
			    onSubmit="return validateAllFormSpecialChars();"
			  action ="[@spring.url '/manageSystem/saveDcSystem'/]">
			  [@spring.formHiddenInput path="dcSystemFormBean.editMode"/]
				 [#if dcSystemFormBean?? && dcSystemFormBean.editMode  == true
								&& dcSystemFormBean.system?? && dcSystemFormBean.system.name??]
								[@spring.formHiddenInput path="dcSystemFormBean.system.name"/]
				[/#if]
				<table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
					<tr>
						<td class="Labeltd"><label for="">System Name </label> <span class="RequiredField">*</span> </td>
						<td class="inputltd">  <span class="text">
                            [#if dcSystemFormBean?? && dcSystemFormBean.editMode == true ]
                                [@spring.formInput path="dcSystemFormBean.system.name"  attributes="disabled" /]
                            [#else]
                                  [@spring.formInput path="dcSystemFormBean.system.name" attributes=" maxlength=\"50\" class=\"systemName\" onblur=\"validateTextNotContainigSpecialCharactersOnSubmit('.systemName', '.systemNameError');\""    /]
                            [/#if]
							[@spring.showErrors "<br>" "error"/]

							 <label class=" systemNameError" id="systemNameError"></label>
                        </span>
						</td>
					</tr>
					<tr>
						<td class="Labeltd"><label for="">System Description  </label></td>
						<td class="inputltd  WithHint"> <span class="text">
                                [@spring.formInput path="dcSystemFormBean.system.description" attributes=" maxlength=\"100\" class=\"systemDesc\" onblur=\"validateTextNotContainigSpecialCharactersOnSubmit('.systemDesc', '.systemDescError');\""/]
                                [@spring.showErrors "<br>" "error"/]
								 <label class=" systemDescError" id="systemDescError"></label>
							</span>
						</td>
					</tr>
					<tr>
						<td class="Labeltd textareaLable"><label for=""> System Nodes <span class="RequiredField">*</span></label> </td>
						<td class="inputltd">
							<span class="SubmitButtons">
							 <a href="#"
							 onclick="navigateTo('[@spring.url '/manageNodes/defineDcNode'/]', 'POST', 'systemForm');"
							 class="SubmitButton BtnWidth1">Create Node</a>
							</span>
						     [#if delete_node_error?has_content]
                                                  <div class="CustomContent3 ErrorMessages">
                                                    <h4>Error Message</h4>

                                                    <p>${delete_node_error}</p>
                                                  </div>
                              [/#if]
							[#if dcSystemFormBean.system?? && dcSystemFormBean.system.nodesList?? && dcSystemFormBean.system.nodesList? has_content]
							<div class="Grid SmallGrid fixedGrid" style="padding:10px 0 0">
								<table width="100%" cellspacing="0" cellpadding="0" border="0" class="tablesorter FormTable" id="tablePagerNode">
								<thead>
									<tr class="TableHeader">
										<th width="60%"> Node Name</th>
										<th width="40%"> Actions</th>
									</tr>
								</thead>
								<tbody>
								[#list dcSystemFormBean.system.nodesList as node]
									<tr >
										<td > <span>${node.name}</span>
										</td>
										<td>
											<a href="[@spring.url '/manageNodes/editNode/${node.name}'/]"
											class="EditUser" style="display:inline-block;width:5%"> Edit</a>
											[#if node.inUse?? && node.inUse == 'Y']
											    <a href="[@spring.url '/manageSystem/deActivateNode/${node.name}'/]"  class="Deactivate" style="display:inline-block;width:22%" >Deactivate</a>
										   [#else]
                                                  <a href="[@spring.url '/manageSystem/activateNode/${node.name}'/]"   class="Activate" style="display:inline-block;width:22%" >Activate</a>
                                           [/#if]
										   <a href="#DeleteUser" class="DeleteUser" style="display:inline-block;width:13%"
											 onclick="javascript:deleteNode('${node.name}')"> Delete</a>
										</td>
									</tr>
								 [/#list]
								</tbody>
								</table>
								<div id="NodePager" class="Paging" > </div>
							</div>
							[#else]  <div class=" Grid SmallGrid fixedGrid NoResults" style="padding:10px 0 0">
										No nodes defined
									</div>
							[/#if]
						</td>
					</tr>
					<tr>
						<td class="Labeltd textareaLable"><label for=""> System Inputs<span class="RequiredField">*</span></label> </td>
						<td class="inputltd">
							<span class="SubmitButtons">
                                  <a href="#"
								  onclick="navigateTo('[@spring.url '/manageInput/defineSystemInput'/]', 'POST', 'systemForm');"
								  class="SubmitButton">Create Input</a>

							</span>
							[#if dcSystemFormBean.system?? && dcSystemFormBean.system.inputsList?? && dcSystemFormBean.system.inputsList? has_content]
							<div class="Grid SmallGrid fixedGrid" style="padding:10px 0 0">
								<table width="100%" cellspacing="0" cellpadding="0" border="0" class="tablesorter FormTable" id="tablePagerInput">
								<thead>
									<tr class="TableHeader">
										<th width="75%"> Input Name</th>
										<th width="25%"> Actions</th>
									</tr>
								</thead>
								<tbody>
								[#list dcSystemFormBean.system.inputsList as input]
									<tr >
										<td > <span>${input.id}</span>
										</td>
										<td>
											<a
											href="#" class="EditUser"
											onclick="navigateTo('[@spring.url '/manageInput/editSystemInput/${input.id}'/]', 'POST', 'systemForm');"> Edit</a>
											<a href="#DeleteUser" class="DeleteUser"
											onclick="javascript:deleteSystemInput('${input.id}')"> Delete</a>
											<a href="#update" class="UpdateAction" style="display:none"> Update</a>
											<a href="#cancel" class="CancelAction" style="display:none"> Cancel</a>
										</td>
									</tr>
								[/#list]
								</tbody>
								</table>
								 <div id="InputsPager" class="Paging" > </div>
							</div>
							[#else]
								<div class=" Grid SmallGrid fixedGrid NoResults" style="padding:10px 0 0">
									No Inputs defined
                            	</div>
                            [/#if]
						</td>
					</tr>
					<tr>
						<td class="Labeltd "><label for=""> </label>  </td>
						<td class="inputltd">
							<span class="SubmitButtons ">
								<span class="SubmitButton BtnWidth1">
								    <input type="submit"  value="Save" onclick="javascript:this.disabled='disabled'" class="submit">
								</span>
							</span>
							<span class="SubmitButtons Gray ">
							        <a href="[@spring.url '/manageSystem/list'/]" class="SubmitButton BtnWidth1">Cancel</a>
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
						<br>
        				<span id="jobConfirm">
						</span>
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