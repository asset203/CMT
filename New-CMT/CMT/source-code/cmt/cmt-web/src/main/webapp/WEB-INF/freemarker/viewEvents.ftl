[#ftl/]
[#import "spring.ftl" as spring /]
[#import "macros.ftl" as cmt /]

[#global title="Capacity Management Tool"/]
[#global isSysEventsSelected="selected"/]

[#assign javaScript]
<script type="text/javascript" src="[@spring.url '/js/common.js'/]"></script>
<script>
    $(document).ready(function () {
    $.ajaxSetup({
    cache:false
    });

    $(".DeleteEvent").fancybox({
    padding:0,
    closeBtn:false

    });
    $(".DeleteBtns .SubmitButtons.Gray").click(function () {
    parent.$.fancybox.close();
    });

    var availableTags = [
    [#if systemList?? && systemList?size>0]
    [#list systemList as system]"${system}"[#if system_has_next],[/#if][/#list]
    [/#if]
    ];
    $(".AutoCompleteInput").autocomplete({
    source:availableTags,
    change:function (event, ui) {
    $("#myform").submit();
    }
    });

    var highDates = {};
    var mediumDates = {};
    var lowDates = {};
    var eventsDescriptions = {};
    [#if formBean.systemEvents??]
    [#list formBean.systemEvents as systemEvent]
    [#if systemEvent.levelType?upper_case == 'HIGH']
    highDates[new Date("${systemEvent.dateTime?string('MM/dd/yyyy')}")] =
    new Date("${systemEvent.dateTime?string('MM/dd/yyyy')}");
    [#elseif systemEvent.levelType?upper_case == 'MEDIUM']
    mediumDates[new Date("${systemEvent.dateTime?string('MM/dd/yyyy')}")] =
    new Date("${systemEvent.dateTime?string('MM/dd/yyyy')}");
    [#elseif systemEvent.levelType?upper_case == 'LOW']
    lowDates[new Date("${systemEvent.dateTime?string('MM/dd/yyyy')}")] =
    new Date("${systemEvent.dateTime?string('MM/dd/yyyy')}");
    [/#if]
    eventsDescriptions[new Date("${systemEvent.dateTime?string('MM/dd/yyyy')}")] = '${systemEvent.commentDesc}';
    [/#list]
    [/#if]

    $('.datepickerDiv').datepicker({

    beforeShowDay:function (date) {
    var highHighlight = highDates[date];
    var mediumHighlight = mediumDates[date];
    var lowHighlight = lowDates[date];
    var eventDescription = eventsDescriptions[date];
    if (highHighlight) {
    return [true, "LevelHigh", eventDescription];
    }
    else if (mediumHighlight) {
    return [true, "LevelMedium", eventDescription];
    }
    else if (lowHighlight) {
    return [true, "LevelLaw", eventDescription];
    }
    else {
    return [true, '', ''];
    }
    },
    onSelect:function (dateText, inst) {
    $("#datepicker").val(dateText);

    [#if formBean.selectedSystem??]
    [#assign ajaxURI][@spring.url '/systemEvents/loadEventDetails/'/]${formBean.selectedSystem?replace(' ', '%20')}/[/#assign]
    [#else]
    [#assign ajaxURI][@spring.url '/systemEvents/loadEventDetails/'/][/#assign]
    [/#if]

    jQuery("#viewEventAjax")
    .load('${ajaxURI}' + $("#datepicker").val().replace(/\//g, "_"), "",
    function (response) {
    if (response) {
    jQuery("#viewEventAjax").css('display', '');
    } else {
    jQuery("#viewEventAjax").css('display', 'none');
    }
    })

    },
    showButtonPanel:true
    });
    [#if formBean.selectedSystemEvent?? && formBean.selectedSystemEvent.dateTime?? && formBean.selectedSystemEvent.dateTime?has_content]
    [#assign selectedDate]${formBean.selectedSystemEvent.dateTime?string('MM/dd/yyyy')}[/#assign]

    $('.datepickerDiv').datepicker("setDate", '${selectedDate}');
    [/#if]
    [#if formBean?? && formBean.selectedSystem?? && formBean.selectedSystem?has_content && formBean.systemEvents??]
    jQuery(".addSysEvent").css('display', 'block');
    [#else]
    jQuery(".addSysEvent").css('display', 'none');
    [/#if]

    });

	$.datepicker._gotoToday = function(id) {
    var target = $(id);
    var inst = this._getInst(target[0]);
    if (this._get(inst, 'gotoCurrent') && inst.currentDay) {
            inst.selectedDay = inst.currentDay;
            inst.drawMonth = inst.selectedMonth = inst.currentMonth;
            inst.drawYear = inst.selectedYear = inst.currentYear;
    }
    else {
            var date = new Date();
            inst.selectedDay = date.getDate();
            inst.drawMonth = inst.selectedMonth = date.getMonth();
            inst.drawYear = inst.selectedYear = date.getFullYear();
            // the below two lines are new
            this._setDateDatepicker(target, date);
            this._selectDate(id, this._getDateDatepicker(target));
    }
    this._notifyChange(inst);
    this._adjustDate(target);
}
    function deleteEvent(id) {
    $("#systemEventID").val(id);
    $("#deletedEventName").text($("#eventSubject").text());
    }
</script>
[/#assign]
[@cmt.htmlHead javaScript/]
[@cmt.navigation/]


<div class="TabContent ManageUserTabContent">
    <h1>System Events</h1>
    [#if successMsg??]${successMsg}[/#if]
    [#if errorMsg??]${errorMsg}[/#if]
    <div class="GridHeader">
        <form class="addSysEvent" action="[@spring.url '/systemEvents/goToAddEvent'/]" method="post" style="display:none;">
            <div class="BtnBlock">
                [@spring.formHiddenInput path="formBean.selectedSystem"/]
	<span class="SubmitButtons ">
							<span class="SubmitButton BtnWidth1">
								<input type="submit" class="submit" value="Add New Event">
							</span>
						</span>
            </div>
        </form>
    </div>

    <div class="GridHeader2">
        <form id="myform" action="[@spring.url '/systemEvents/viewSelectedSystemEvents'/]" method="post">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
                <tr>
                    <td class="Labeltd"><label for="cd-dropdown">Select System</label> <span class="RequiredField">*</span></td>
                    <td class="inputltd">
                        [@spring.formInput path="formBean.selectedSystem"
                        attributes=" size=\"25\"  minlength=\"2\"
                        class=\"CustomInput AutoCompleteInput specialChars errors\"" /]
                        [@spring.showErrors "<br>" "error"/]
                    </td>
                </tr>
            </table>
        </form>
        [@cmt.progressBar/]
    </div>
    [#if formBean?? && formBean.selectedSystem?? && formBean.selectedSystem?has_content && !(spring.status.errorMessages?? && spring.status.errorMessages?has_content)]
    <div class="BlockContainer">
        <div class="BlockHeader"><h2>Current Events</h2></div>
        <div class="BlockContent">
            <div class="CalenderView">
                <input type="hidden" id="datepicker" class="datepicker" style=""/>

                <div class="datepickerDiv"></div>
            </div>
            <div id="viewEventAjax">
                [#if formBean.selectedSystemEvent??]
                <form action="[@spring.url '/systemEvents/goToEditEvent'/]" method="post">
                    <div class="CalenderEventDetails">
                        [@spring.formHiddenInput path="formBean.selectedSystemEvent.id"/]
                        [@spring.formHiddenInput path="formBean.selectedSystemEvent.commentDesc"/]
                        [@spring.formHiddenInput path="formBean.selectedSystemEvent.levelType"/]
                        [@spring.formHiddenInput path="formBean.selectedSystemEvent.dateTime"/]
                        [@spring.formHiddenInput path="formBean.selectedSystemEvent.systemName"/]
                        <div class="EventDetails">
                            <div class="EventRow">
                                <label>Event Subject:</label>
                                <span id="eventSubject"> ${formBean.selectedSystemEvent.commentDesc}</span>
                            </div>
                            <div class="EventRow">
                                <label>Event Level:</label> ${formBean.selectedSystemEvent.levelType?capitalize}
                            </div>
                            <div class="EventRow">
                                <label>Date:</label> ${formBean.selectedSystemEvent.dateTime?string('dd-MMM-yyyy')}
                            </div>
                        </div>
                        <div class="BtnBlock">
                                            <span class="SubmitButtons ">
                                                <span class="SubmitButton BtnWidth1">
                                                    <input type="submit" value="edit" class="submit">
                                                </span>
                                            </span>
                            <a class="SubmitButtons DeleteEvent" href="#DeleteEvent"
                               onclick="javascript:deleteEvent(${formBean.selectedSystemEvent.id});">
                                                <span class="SubmitButton BtnWidth1">
                                                    <input type="reset" value="delete">
                                                </span>
                            </a>
                        </div>
                    </div>
                </form>
                [#else]
                <div class="CalenderEventDetails">
                    <div class="NoResults">No Events are currently available on the selected Date</div>
                </div>
                [/#if]
            </div>
            <div class="CalendarLegend">
                <h3>Calendar Legend</h3>

                <div class="LegendIcons TodayDate"> Today's Date</div>
                <div class="LegendIcons SelectedDate"> Selected Date</div>
                <div class="LegendIcons LevelHigh"> Level High</div>
                <div class="LegendIcons LevelMedium"> Level Medium</div>
                <div class="LegendIcons LevelLaw"> Level Low</div>
            </div>
        </div>
    </div>
    [/#if]
</div>
<div style="display:none">
    <div id="DeleteEvent" class="delteUserPopUP">
        <div class="delteUserPopUPTitle">Delete Event</div>
        <div class="delteUserPopUPInner">
            <div class="delteUserPopUPInner2">
                Are you sure you want to delete the event <span id="deletedEventName"></span>?
                <div class="DeleteBtns">
                    <form action="[@spring.url '/systemEvents/deleteSystemEvent'/]" method="post">
                        [@spring.formHiddenInput path="formBean.selectedSystem"/]
                        [@spring.bind path="formBean.selectedSystemEvent.id"/]
                        <input type="hidden" id="systemEventID"
                               name="${spring.status.expression}" class="systemEventID" style=""/>

                                <span class="SubmitButtons ">
                                    <span class="SubmitButton BtnWidth1">
                                        <input type="submit" value="Yes">
                                    </span>
                                </span>
                                <span class="SubmitButtons Gray ">
                                    <span class="SubmitButton BtnWidth1">
                                        <input type="reset" value="Cancel">
                                    </span>
                                </span>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>
[@cmt.footer/]