[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]
[#import "../grid-component.ftl" as component/]
[#global title="Capacity Management Tool"/]
[#global isDCSystemSelected="selected"/]

[#assign javaScript]
<script src="[@spring.url '/js/data-collection.js'/]" type="text/javascript"></script>
<script>

			$(document).ready(function() {

				$('.AddPathTableRow').click(function(){
					var newPath = $("#newPath").val();
					if(newPath != null && newPath.length >0 ){

						var rejectChars = /(?=(\>|\<|\:|\||\&|\,|\;|\.)|\+|\?|\'|\")/;
						var validFormat = /^(\/)((.))+(\/)$/;

					  if (rejectChars.test(newPath) ) {
					     $('.invalidPath').addClass("error");
					     $('.invalidPath').text("This value contains invalid characters" );
						 newPath =newPath.replace(/\'/g,'');
						 newPath =newPath.replace(/\"/g,'');
						 newPath =newPath.replace(/\</g,'');
						 newPath =newPath.replace(/\>/g,'');
						 $("#newPath").val(newPath);
					  }
					  else
					  {
							if(! validFormat.test(newPath))
							{
								 $('.invalidPath').addClass("error");
								 $('.invalidPath').text("invalid path format" );
							}
							else
							{
								$('.invalidPath').text("");
								$('.invalidPath').removeClass("error");
								var pathWithoutSlashes = $("#newPath").val().replace(/\//g,',');
							    pathWithoutSlashes = pathWithoutSlashes.replace(/ /g,';');
								var url = '[@spring.url '/manageInput/addPath/'/]' + pathWithoutSlashes ;
								submitTheForm(url);
								$(this).attr('disabled', 'disabled');
							}
						}
					}
				});

					if ($('.AccessMethodTD select').val() == "DB_ACCESS") {
							$('.AccessMethodTD select').parents().find('.FileAccess').hide();
							$('.AccessMethodTD select').parents().find('.DBAccess').show();
							if ($('.DBTypeTD select').val() == "ORACLE_RAC") {
								$('.DBTypeTD select').parents().find('.TNS').show();
								$('.AccessMethodTD select').parents().find('.NonRacDb').hide();
								$('.AccessMethodTD select').parents().find('.ServerRow').hide();
							}
							else
							if ($('.DBTypeTD select').val() == "SYBASE"
									|| $('.DBTypeTD select').val() == "ORACLE"
									|| $('.DBTypeTD select').val() == "MS_SQL_SERVER"
									|| $('.DBTypeTD select').val() == "DB2"
									|| $('.DBTypeTD select').val() == "MY_SQL") {
											$('.DBTypeTD select').parents().find('.TNS').hide();
											$('.AccessMethodTD select').parents().find('.NonRacDb').show();
											$('.AccessMethodTD select').parents().find('.ServerRow').show();
							}
                            /*Added by Alia.Adel on 20130916 since MySQL connection already adds a default port 3306 in execution*/
                            if ($('.DBTypeTD select').val() == "MY_SQL") {
                                $('.AccessMethodTD select').parents().find('.dbPort').hide();
                            }
					}
					else
					{
						$('.AccessMethodTD select').parents().find('.FileAccess').show();
						$('.AccessMethodTD select').parents().find('.DBAccess').hide();
						$('.DBTypeTD select').parents().find('.TNS').hide();
						$('.AccessMethodTD select').parents().find('.ServerRow').show();
						$('.AccessMethodTD select').parents().find('.NonRacDb').hide();
					}


				$('.AccessMethodTD select').change(function(){
					refreshDataCollectionList();
				});
				$('.DBTypeTD select').change(function(){
					refreshDataCollectionList();
				});

				$('.FileTypeTD select').change(function(){
					refreshDataCollectionList();
				});


				var availableTags = [
				  [#if dcInputFormBean.dataCollectionNames?? && dcInputFormBean.dataCollectionNames?size>0]
						  [#list dcInputFormBean.dataCollectionNames as dataCollection]
							"${dataCollection}"[#if dataCollection_has_next],[/#if]
						  [/#list]
				   [/#if]
				];
				$( ".AutoCompleteInput" ).autocomplete({
				  source: availableTags,
				      change: function(event,ui)
					        {
						        if (ui.item==null)
						            {
						            $(".AutoCompleteInput").val('');
						            $(".AutoCompleteInput").focus();
						            }
					        }

				});
			});

			function refreshDataCollectionList() {
                      var url = '[@spring.url '/manageInput/refreshDataCollectionList/'/]' ;
					 var form = document.getElementById('inputForm');
					  form.action = url;
					  form.method='POST';
					  form.submit();
                }

			function addDataCollection(){
				var textEl = document.getElementById('autoCompleteDataCollection');
				var text= textEl.value;
				if(text != null && text.length >0 && text.indexOf(' ') < 0)
				{
					var url = '[@spring.url '/manageInput/addDataCollection/' /]' + text;
					submitTheForm(url);
					$('.AddDataCollectionBtn').attr('disabled', 'disabled');
				}
			}
			function deleteDataCollection(dataCollectionName){
				var url = '[@spring.url '/manageInput/removeDataCollection/' /]' + dataCollectionName;
				submitTheForm(url);
			}
			function deletePath(path){
					if(path != null && path.length >0 ){
						var pathWithoutSlashes = path.replace(/\//g,',');
						pathWithoutSlashes = pathWithoutSlashes.replace(/ /g,';');
						var url = '[@spring.url '/manageInput/removePath/' /]' + pathWithoutSlashes ;
						submitTheForm(url);
						$('.DeletePath').attr('disabled', 'disabled');
					}

			}


			function submitTheForm(url){
				$("#inputForm").attr("action", url);
				$("#inputForm").attr("method", "POST");
				$("#inputForm").attr("onSubmit", "");
				$("#inputForm").submit();

			}

		function validateNoSpecialChars(){
alert('hi');
			var v1 = validateTextNotContainigSpecialCharactersOnSubmit('.inputName', '.inputNameError');
			var v2 = validateTextNotContainigSpecialCharactersOnSubmit('.port', '.portError');
			alert(v1);
			alert(v2);
				return (v1&&v2);
		}

		</script>
<script src="[@spring.url '/js/common-validation.js'/]" type="text/javascript"></script>
[/#assign]
[@cmt.htmlHead javaScript /]
[@cmt.navigation/]
<div class="TabContent">

            [@spring.bind path="dcInputFormBean.dataCollections"/]
            [#assign emptyDataCollections][@spring.showErrors "<br>" "validationerror"/][/#assign]
            [@spring.bind path="dcInputFormBean.pathsList"/]
            [#assign emptyPathsList][@spring.showErrors "<br>" "validationerror"/][/#assign]

			<h1>[#if dcInputFormBean.editMode == true]Edit[#else] Create[/#if] Input </h1>
			[#if errors?has_content]
                                      <div class="CustomContent3 ErrorMessages">
                                           <h4>Error Message </h4>

                                          [#if errors?has_content]
                                               <p>${errors}</p>
                                          [/#if]
                                     </div>
             [/#if]
			<div class="FormContainer">
			  <form id="inputForm"  method="POST">
			  [@spring.formHiddenInput "dcInputFormBean.editMode" /]
				[@spring.formHiddenInput "dcInputFormBean.currentDataCollectionType" /]
				[@spring.formHiddenInput "dcInputFormBean.nodeEnabled" /]
				[@spring.formHiddenInput "dcInputFormBean.inputNameBeforeChange" /]
				[@spring.formHiddenInput "dcInputFormBean.inputID" /]
				[@spring.formHiddenInput "dcInputFormBean.systemName" /]
				[@spring.formHiddenInput "dcInputFormBean.systemDescription" /]
				[@spring.formHiddenInput "dcInputFormBean.newSystem" /]
				[@spring.formHiddenInput "dcInputFormBean.nodeName" /]
				[@spring.formHiddenInput "dcInputFormBean.nodeDescription" /]
				[@spring.formHiddenInput "dcInputFormBean.newNode" /]
				[@spring.formHiddenInput "dcInputFormBean.nodeId" /]

				<table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
					<tr>
						<td class="Labeltd"><label for="">Data Collection Name </label>
							<span class="RequiredField">*</span>
						</td>
						<td class="inputltd">
							<span class="text">
							[@spring.formInput path="dcInputFormBean.inputName" attributes=" maxlength=\"50\" class=\"inputName\" onblur=\"validateTextNotContainigSpecialCharactersOnSubmit('.inputName', '.inputNameError');\""/]
							[@spring.showErrors "<br>" "error"/]
							<label class=" inputNameError" id="inputNameError"></label>
							</span>

						</td>
					</tr>
                    <tr>
                    						<td class="Labeltd"> <label>Access Method</label> <span class="RequiredField">*</span></td>
                    						<td class="inputltd AccessMethodTD" colspan="2">
                    							[@spring.formSingleSelect "dcInputFormBean.inputAccessMethod"
                    								dcInputFormBean.accessMethodOptions "class=\"cd-select required error\""/]
                    						</td>
                    					</tr>
                    					<tr class="FileAccess"  style="display:none">
                    						<td class="Labeltd"> <label>File Type</label> <span class="RequiredField">*</span></td>
                    						<td class="inputltd FileTypeTD" colspan="2">
                    							[@spring.formSingleSelect "dcInputFormBean.fileType"
                    								dcInputFormBean.fileTypeOptions "class=\"cd-select required error\"" /]
                    						</td>
                    					</tr>
                    					<tr class="DBAccess" style="display:none">
                    						<td class="Labeltd"> <label>DataBase Type</label> <span class="RequiredField">*</span></td>
                    						<td class="inputltd DBTypeTD" colspan="2" >
                    							[@spring.formSingleSelect "dcInputFormBean.dataBaseType"
                    								dcInputFormBean.dataBaseTypeOptions "class=\"cd-select required error\""/]
                    						</td>
                    					</tr>
                    					<tr>
                    						<td class="Labeltd" nowrap="nowrap"><label for="">Data collection process </label><span class="RequiredField">*</span> </td>
                    						<td class="inputltd">
                    								<input  id="autoCompleteDataCollection" class="CustomInput AutoCompleteInput ui-autocomplete-input errors" type="text" >
													[#if emptyDataCollections?has_content]<label> ${emptyDataCollections}</label>
                                          [/#if]
                                [#if invalid_dataCollection_name?has_content]
                                    <label class="error">
                                         ${invalid_dataCollection_name}
                                    </label>
                                [/#if]
                    						</td>

                    					</tr>
					<tr>
						<td class="Labeltd"><label for=""> </label> </td>
						<td class="inputltd " >
						    <div class="GridHeader">
								<span class="SubmitButtons">

									<a href="#" class="SubmitButton BtnWidth1 AddDataCollectionBtn"
									[#if dcInputFormBean.enableAddDataCollection == false ] disabled="disabled"[/#if]
									onclick="addDataCollection();">
									[@spring.formHiddenInput "dcInputFormBean.enableAddDataCollection" /]
									Add to List</a>
								</span>
							</div>
							<div class="Grid SmallGrid fixedGrid">

								<table width="100%" cellspacing="0" cellpadding="0" border="0" class="tablesorter FormTable"
								style="[#if !dcInputFormBean.dataCollections?has_content || dcInputFormBean.dataCollections?size == 0 ]display:none[/#if]">
								<thead>
									<tr class="TableHeader">
										<th width="90%"> Data Collection Name </th>
										<th width="10%"> Actions</th>
									</tr>
								</thead>
								<tbody>
								[#if dcInputFormBean.dataCollections?? && dcInputFormBean.dataCollections?size >0]
								[#list dcInputFormBean.dataCollections as dataCollection]
									<tr>
										<td> ${dataCollection}
										[@spring.formHiddenInput "dcInputFormBean.dataCollections[${dataCollection_index}]"/]

										</td>
										<td>
											<a href="#Delete" onclick="deleteDataCollection('${dataCollection}');" class="DeleteUser"> Delete</a>
										</td>
									</tr>
								  [/#list]
								  [/#if]
								</tbody>
								</table>

							</div>
						</td>
					</tr>
				</table>
				<hr>
				<table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">

					<tr  class="FileAccess" style="display:none" >
						<td class="Labeltd"><label for="">File Name Pattern  </label> <span class="RequiredField">*</span> </td>
						<td class="inputltd  WithHint" nowrap>
							<span class="text">
							    [@spring.formInput path="dcInputFormBean.fileNamePattern" attributes=" maxlength=\"200\" class=\" fileName\" onblur=\"reservedCharsError('.fileName', '.fileNameError');\""/]
							    [@spring.showErrors "<br>" "error"/]
								<label class=" fileNameError" id="fileNameError"></label>
							 </span>
							 <div class="HintBlock">
							 								<div class="SelectionHint2" style="word-wrap:break-word" >
							 									<div class="SelectionHintInner2">
							 									<div>Hint</div>
							 									Please enter a pattern for the names of the source files for the data collection.
							 									<br>To include a date in file name, please use $date(date_format)$ [example $date(yymmdd)$].
							 									<br>For more details about valid regex patterns please refer to:
							 									<br><a href="http://ocpsoft.org/opensource/guide-to-regular-expressions-in-java-part-1/" target="_blank">
							 <b>Guide To Regular Expression</b></a>
							 									</div>
							 								</div>
							</div>
							 </td>
							 <td class="Labeltd">

						</td>
					</tr>
					<tr class="NonRacDb" style="display:none">
						<td class="Labeltd"><label for="">Data Base Name</label> <span class="RequiredField">*</span> </td>
						<td class="inputltd  WithHint" nowrap="nowrap">
							<span class="text">
							    [@spring.formInput path="dcInputFormBean.dataBaseName" attributes=" maxlength=\"200\" class=\"dbName\" onblur=\"reservedCharsError('.dbName', '.dbNameError');\""/]
							    [@spring.showErrors "<br>" "error"/]
								<label class=" dbNameError" id="dbNameError"></label>
							 </span>
						</td>
						<td ></td>
					</tr>
					<tr class="ServerRow" style="display:none">
						<td class="Labeltd "><label for="">Server </label><span class="RequiredField">*</span> </td>
						<td class="inputltd" nowrap ="nowrap">
							<span class="text">
							[@spring.formInput path="dcInputFormBean.server" attributes=" class=\"server\" onblur=\"reservedCharsError('.server', '.serverError');\""/]
							[@spring.showErrors "<br>" "error"/]
							<label class=" serverError" id="serverError"></label>
							 </span> </td>
				        </tr>
					<tr class="NonRacDb dbPort" style="display:none">
						<td class="Labeltd "><label for="">Port</label> </td>
						<td class="inputltd" nowrap="nowrap">
						<span class="text">
						    [@spring.formInput path="dcInputFormBean.port" attributes=" maxlength=\"5\" class=\"port\" onblur=\"validateTextNotContainigSpecialCharactersOnSubmit('.port', '.portError');\""/]
						    [@spring.showErrors "<br>" "error"/]
							<label class=" portError" id="portError"></label>
						 </span>
						</td>
					</tr>
					<tr>
						<td class="Labeltd"><label for="">UserName </label><span class="RequiredField">*</span>  </td>
						<td class="inputltd" nowrap="nowrap">
							<span class="text">
							    [@spring.formInput path="dcInputFormBean.userName" attributes="  maxlength=\"200\" class=\"userName\" onblur=\"reservedCharsError('.userName', '.userNameError');\""/]
							    [@spring.showErrors "<br>" "error"/]
							<label class=" userNameError" id="userNameError"></label>
							 </span>
						</td>
					</tr>
					<tr>
						<td class="Labeltd"><label for="">Password </label> <span class="RequiredField">*</span> </td>
						<td class="inputltd" nowrap="nowrap">
							<span class="text">
							    [@spring.formInput path="dcInputFormBean.password" attributes=" maxlength=\"200\" class=\"password\" onblur=\"reservedCharsError('.password', '.passwordError');\""/]
							    [@spring.showErrors "<br>" "error"/]
							<label class=" passwordError" id="passwordError"></label>
							</span>
						</td>
					</tr>
					<tr class="TNS" style="display:none" >
						<td class="Labeltd textareaLable"><label for="">TNS Name </label><span class="RequiredField">*</span> </td>
						<td class="inputltd" nowrap=nowrap">
						[@spring.formTextarea path="dcInputFormBean.tns" attributes=" style=\"width:230px;\" class=\"tnsName\" onblur=\"reservedCharsError('.tnsName', '.tnsNameError');\""/]
						[@spring.showErrors "<br>" "error"/]
						<label class=" tnsNameError" id="tnsNameError"></label>
						</td>
						<td ></td>
					</tr>
				</table>
				<hr>
				<table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable FileAccess"  >
					<tr>
						<td class="Labeltd textareaLable" nowrap="nowarp"><label for=""> Paths list</label><span class="RequiredField">*</span> </td>
						<td>
							<input id="newPath" name=""  class="required error  CustomInput" type="text"  \>
							<label class="invalidPath"></label>[#if emptyPathsList?has_content]${emptyPathsList}[/#if]
						</td>
						<td></td>
						</tr>
						<tr>
						<td></td>
						<td class="inputltd">
							<div class="GridHeader" style="padding:0 0 10px">
								<span class="SubmitButtons AddPathTableRow">
									<a class="SubmitButton BtnWidth1" href="#D">Add To List</a>
								</span>
							</div>
							<div class="Grid SmallGrid fixedGrid">

								<table width="100%" cellspacing="0" cellpadding="0" border="0" class="tablesorter FormTable PathTable" style="[#if !dcInputFormBean.pathsList?has_content || dcInputFormBean.pathsList?size == 0 ]display:none[/#if]" >
									<thead>
										<tr class="TableHeader">
											<th width="90%"> Path </th>
											<th width="10%"> Actions</th>
										</tr>
									</thead>
									<tbody>
									[#if dcInputFormBean.pathsList?? ]
									[#list dcInputFormBean.pathsList as path]
										<tr>
											<td class="inputltd">
											<span>${path}</span>
											<input id="currentPath" value="${path}" type="hidden">
											 [@spring.formHiddenInput "dcInputFormBean.pathsList[${path_index}]"/]
											 [@spring.showErrors "<br>" "error"/]											</td>
											<td>
												<a href="#Delete" onclick="deletePath('${path}');"  class="DeleteUser DeletePath"> Delete</a>
											</td>
										</tr>
									[/#list]
									[/#if]
									</tbody>
								</table>

							</div>
						</td>
						<td></td>
					</tr>
				</table>

				<table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
				<colgroup>
									<col style="width:15%"/>
							        <col style="width:50%"/>
							        <col style="width:35%"/>
			        </colgroup>
					<tr>
						<td class="Labeltd "><label for=""> </label>  </td>
						<td class="inputltd">
							<span class="SubmitButtons ">
								<span class="SubmitButton BtnWidth1">
											[#if dcInputFormBean.nodeEnabled]
									<a href="#" class="submit" onclick="navigateTo('[@spring.url '/manageInput/saveNodeInput'/]', 'POST', 'inputForm');javascript:this.disabled='disabled'">Save</a>
									[#else]
									<a href="#" class="submit" onclick="navigateTo('[@spring.url '/manageInput/saveSystemInput'/]', 'POST', 'inputForm');javascript:this.disabled='disabled'">Save</a>
									[/#if]

							</span>
							</span>
							<span class="SubmitButtons Gray ">
						[#if dcInputFormBean.nodeEnabled]
								[#if Session.dcNodeFormBean.node?? && Session.dcNodeFormBean.node.name??]
									[#assign nodeName]
										${Session.dcNodeFormBean.node.name}
									[/#assign]
								[#else]
									[#assign nodeName]

									[/#assign]
								[/#if]
						<a href="[@spring.url '/manageNodes/editNode/${nodeName}'/]" class="SubmitButton BtnWidth1" >Cancel</a>
						[/#if]
						[#if !dcInputFormBean.nodeEnabled]
						<a href="[@spring.url '/manageSystem/editSystem'/]" class="SubmitButton BtnWidth1">Cancel</a>
						[/#if]

							</span>

				</td>
				<td></td>
					</tr>
				</table>
			  </form>
			</div>
		</div>
 [@cmt.footer/]