[#ftl/]
[#import "spring.ftl" as spring /]
[#import "macros.ftl" as cmt /]


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
                        <input type="submit"  value="edit" class="submit">
                    </span>
                </span>
                <a class="SubmitButtons DeleteEvent" href="#DeleteEvent"
                    onclick="javascript:deleteEvent(${formBean.selectedSystemEvent.id});">
                    <span class="SubmitButton BtnWidth1">
                        <input type="reset"  value="delete">
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