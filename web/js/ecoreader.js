function populateAuthors() {
    theUrl = "rest/authors/list";
    var jqxhr = $.getJSON( theUrl, function(data) {
        var listItems = "";
        listItems+= "<option value='0'>Select an author ...</option>";
        $.each(data.authors,function(index,author) {
            listItems+= "<option value='" + author.id + "'>" + author.name + "</option>";
        });
        $("#authors").html(listItems);
        // Set to the first value in the list which should be "select one..."
        $("#authors").val($("#authors option:first").val());
        $('.toggle-content#projects_toggle').show(400);

    }).fail(function(jqXHR,textStatus) {
        if (textStatus == "timeout") {
	        showMessage ("Timed out waiting for response! Try again later or reduce the number of graphs you are querying. If the problem persists, contact the System Administrator.");
        } else {
	        showMessage ("Error completing request!");
        }
    });
}

function formSubmit() {
    showMessage("Query not implemented yet");
}

// A short message
function showMessage(message) {
$('#alerts').append(
        '<div class="alert">' +
            '<button type="button" class="close" data-dismiss="alert">' +
            '&times;</button>' + message + '</div>');
}
