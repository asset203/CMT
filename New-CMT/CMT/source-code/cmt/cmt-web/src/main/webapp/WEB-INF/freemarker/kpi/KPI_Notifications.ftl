[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]

[#global title="Capacity Management Tool"/]
[#global isKPISelected="selected"/]

[#assign javaScript]
    <script>
        $(document).ready(function() {
            $(".AutoCompleteInput").autocomplete({
            select: function(event, ui) {
                    jQuery("#pageBody").load('[@spring.url '/kpi/loadSystemNodes'/]'
                            , {systemName: ui.item.value},
                        function(response){
                            if(response) {
                                jQuery("#pageBody").css('display', 'block');
                            } else {
                                jQuery("#pageBody").css('display', 'none');
                            }
                    });

               }
           } );

            $(function() {
                var availableTags0 = [
                                      [#if systemList?? && systemList?size>0]
                                          [#list systemList as system]"${system}"[#if system_has_next],[/#if][/#list]
                                      [/#if]
                                    ];
                $( ".AutoCompleteInput0" ).autocomplete({
                  source: availableTags0
                });

              });

            $("#deleteBtnDiv").click(function() {
                 jQuery("#pageBody").load('[@spring.url '/kpi/deleteNodeKPI'/]'
                        , {systemName: $("#systemName").val(), nodeValue : $("#nodeValue").val() },
                    function(response){
                        if(response) {
                            jQuery("#pageBody").css('display', 'block');
                        } else {
                            jQuery("#pageBody").css('display', 'none');
                        }
                });
            });
        });
    </script>
[/#assign]

[@cmt.htmlHead javaScript/]
<div  id="pageBody">
    [@cmt.navigation/]

    <div class="TabContent ManageUserTabContent">
        <h1>KPI Notifications </h1>
        [#if successMsg??]${successMsg}[/#if]
        [#if errorMsg??]${errorMsg}[/#if]
        <div class="CustomContent4">
            System KPIs  &nbsp; &nbsp;   |   &nbsp; &nbsp; <a href="[@spring.url '/kpiNotifications/manageLists'/]" class="bluelable">Manage Notifications list </a>
        </div>

        <div class="GridHeader">
            <span class="SubmitButtons DisplayLogBtn">
                 <a href="KPI_Notifications_AddNew.html" class="SubmitButton"> Add New Thresholds & KPIs</a>
            </span>
        </div>

        <form id="KPISearchHeaderForm" action="[@spring.url '/kpi/showNodeKPI'/]" method="post">
            <h2>System KPIs</h2>
            <div class="GridHeader2">
                <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
                    <tr>
                        <td class="Labeltd"> <label for="cd-dropdown">System Name</label> <span class="RequiredField">*</span></td>
                        <td class="inputltd" colspan="2">
                            [@spring.formInput path="kpi.systemName"
                                    attributes=" size=\"25\"  minlength=\"2\"
                                    class=\"CustomInput AutoCompleteInput AutoCompleteInput0\"" /]
                            [@spring.showErrors "<br>" "error"/]
                        </td>
                    </tr>
                    <tr>
                       <td class="Labeltd"> <label for="cd-dropdown1">Select Node</label> <span class="RequiredField">*</span></td>
                       <td class="inputltd" colspan="2">
                           [@spring.formSingleSelect "kpi.nodeValue", nodeList, " style='width: 26%'" /]
                           [@spring.showErrors "<br>" "error"/]
                       </td>
                    </tr>
                </table>
            </div>
            <div class="GridHeader">
                <span class="SubmitButtons">
                    <span class="SubmitButton">
                        <input type="submit" class="submit" value="Show Thresholds & KPIs">
                    </span>
                </span>
            </div>
         </form>

        [#if nodeKPIList?? && nodeKPIList?size>0]
            <h2>KPI Details</h2>
            <div class="CustomContainer5">
                <table width="100%" cellspacing="0" cellpadding="0" border="0" class="DataTable">
                    <tr>
                        <td class="Labeltd"> <label>In Use</label> </td>
                        <td> ${node.inUse}</td>
                    </tr>
                </table>
                <h2>Node Properties</h2>
                <div class="Grid SmallGrid fixedGrid">
                    <table width="100%" cellspacing="0" cellpadding="0" border="0" class="tablesorter NodePropertyTable">
                    <thead>
                        <tr class="TableHeader">
                            <th width="15%"> Property  </th>
                            <th width="25%"> Property Value</th>
                            <th width="15%"> Grain</th>
                            <th width="15%"> Traffic Table </th>
                            <th width="25%"> Notification Threshold %</th>
                        </tr>
                    </thead>
                    <tbody>

            [#list nodeKPIList as nodeKPI]
                <tr>
                    <td> ${nodeKPI.propertyName} </td>
                    <td> ${nodeKPI.grain} </td>
                    <td> ${nodeKPI.value} </td>
                    <td> ${nodeKPI.trafficTableName} </td>
                    <td> ${nodeKPI.notificationThreshold} </td>
                </tr>
            [/#list]
                </tbody>
             </table>
                </div>
            </div>
            <div class="GridHeader">
                <span class="SubmitButtons DisplayLogBtn">
                     <a class="SubmitButton BtnWidth1" href="KPI_Notifications_EditKPI.html"> Edit KPI</a>
                </span>
                <span class="SubmitButtons DisplayLogBtn">
                     <div id="deleteBtnDiv">
                        <a class="SubmitButton BtnWidth1" > Delete</a>
                    </div>
                </span>
            </div>
        [/#if]
    </div>
    <div class="clear"></div>

[@cmt.footer/]
</div>