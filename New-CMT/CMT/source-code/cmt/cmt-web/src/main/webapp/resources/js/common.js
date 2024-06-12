function attachApplicationClasses() {

    $(".tabs ul li:last-child").addClass("lastChildTab");
    $(".Grid table tbody tr:even").addClass("even");
    $(".Grid table thead tr:first-child").addClass("TableHeader");
    $(".Grid table tr td:first-child").addClass("firstChild");
    $(".Grid table tr td:last-child").addClass("lastChild");
    $(".Grid table tr th:last-child").addClass("HeaderLastChild");

    $(".DisplayLogBtn").click(function () {
        $(this).parent().next(".NextContent").show();
    });
    $("table.jCalendar").attr('width', '250');

    /* today button */
    if ($(".CalenderView").size() > 0) {
        $("button.ui-datepicker-current").on('click', function () {
            $(".ui-state-highlight").trigger('click');
        });
    }
    $(".Expandable").click(function () {
        $(this).next(".ExpandableContent").toggle();
        $(this).find("span").toggle();
    });
    // action when select radio input to view the hint related to it
    $('.RadioInputs.WithHint label input').change(function () {
        $('.RadioInputs label').removeClass("selectedlable");
        $(this).parent().addClass("selectedlable");
    })

    /*		
     // upload file style
     $('.BrowseBtn').click(function(){
     $(this).parent().find('input[type="file"]').click();						
     });
     */
    $('.largeInput input[type="file"]').change(function () {
        var InputFiletext = $(this).val();
        $(this).prev('input[type="text"]').val(InputFiletext);
    });

    $('.ExpandTrigger').click(function () {
        $(this).toggleClass("Activate");
        $(this).next().toggle();
    });

    //put data value in input field befor the action
    $('.EditableRow .EditableField').each(function () {
        var datatext = $(this).find('span').text();
        if (!$(this).find('input').hasClass('CustomInputExclude')) {
            $(this).find('input').val(datatext);
        }
        //$(this).find('select').val(datatext);
    });

    //action when click table row to update data
    $('.EditableRow .EditableField').click(function () {
        $(this).parent().addClass("ActiveEdit");
    });

    //action when click edit to update row data
    $('.EditUser').click(function () {
        $(this).parent().parent().find('.EditableField').click();
    });
    //action when click delete to delete row data
    $('.DeleteUser').click(function () {
        $(this).parent().parent('.EditableRow').remove();
    });

    //action to add new row with 4 input fields to table
    $('.AddTableRow').click(function () {
        $(this).parent().prev().find('table').append('<tr class="NewRow"><td class="firstChild"> <input type="text" name="" id="" class="CustomInput"> </td><td> <input type="text" name="" id="" class="CustomInput"> </td><td> <input type="text" name="" id="" class="CustomInput"> </td><td> <input type="text" name="" id="" class="CustomInput"> </td><td class="lastChild"><a href="#update" class="UpdateAction"> Update</a><a href="#cancel" class="CancelAction"> Cancel</a></td></tr>');
    });
    //action to add new row with 3 input fields to table
    $('.AddTableRow2').click(function () {
        $(this).parent().prev().find('table').append('<tr class="NewRow"><td class="firstChild"> <input type="text" name="" id="" class="CustomInput"> </td><td> <input type="text" name="" id="" class="CustomInput"> </td><td> <input type="text" name="" id="" class="CustomInput"> </td><td class="lastChild"><a href="#update" class="UpdateAction"> Update</a><a href="#cancel" class="CancelAction"> Cancel</a></td></tr>');
    });


    $('.SecondRadioOption').click(function () {
       // alert("Existing,,firstRadioTobeHidden...1");
       $(".FirstRadioOptionRow").hide();
      //  alert("Existing,,EnabledPartitionHidden?????...2");
        $(".EnabledPartition").hide();
       // alert("Existing,,YesssEnabledPartitionHidden!...2");
       // alert("Existing,,EnabledPartitionShow...3");
        $(".SecondRadioOptionRow").show();
    });
    $('.FirstRadioOption').click(function () {
       // alert("New,,SecondRadioOptionRowHide...1");
        $(".SecondRadioOptionRow").hide();
       // alert("New,,FirstRadioOptionRowShowing...2");
        $(".FirstRadioOptionRow").show();
       // alert("New,,EnabledPartitionShowing...3");
        $(".EnabledPartition").show();
    });

    // check for other Delimiter
    $('.DelimiterTD select').change(function () {
        if ($(this).val() == "Other Delimiter")
            $(this).next('input').show();
        else
            $(this).next('input').hide();
    });
    //Sorting image
    $(".SortingImg").click(function () {
        $(this).next(".SortContent").show();
        $('.SortContent').on("mouseleave", function () {
            $(this).hide();
        });
    });

    //Validate that text field does not contain < > " '
    jQuery.validator.addMethod("specialChars", function (value, element) {
        var regex = new RegExp("(?=(\>|\<|\"|\'))");
        return this.optional(element) || !regex.test(value);
    }, "invalid text entered.");
    $("#myform").validate();
    $("#myform").on('submit', function (e) {
        var isvalidate = $("#myform").valid();
        if (isvalidate) {
            $("#myform").hide();
            $("#loadingTxt").show();
            return true;
        } else {
            return false;
        }
    });

    $('.SubmitButtons .SubmitButton').find('input').parent().addClass('InputSubmitBtn');

}

$(document).ready(function () {
    attachApplicationClasses();
});

$(document).ajaxComplete(function () {
    attachApplicationClasses();
});