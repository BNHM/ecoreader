function populateAuthors() {
    theUrl = "rest/authors/list";
    var jqxhr = $.getJSON( theUrl, function(data) {
        var listItems = "";
        listItems+= "<option value='0'>Select an author ...</option>";
        $.each(data.authors,function(index,author) {
            listItems+= "<option value='" + author  + "'>" + author + "</option>";
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

function populateVolumes() {
    theUrl = "rest/volumes/";
    $.getJSON( theUrl + $("#authors").val(), function(data) {
        var tr_begin = "<tr>\n\t<td>\n\t<b>{vol_title}</b>\n";
        var tr_end = "\t</td>\n</tr>\n";
        var li = "<li>{section_title}{view_section}</li>";
        var view_section_template = " [<a href='#' class='view_section' data-id='{section_id}'>view section</a>]";
        var html = "<table><tbody>";

        // generate the table of volumes and the corresponding sections
        $.each(data.volumes, function(i, vol) {
            html += tr_begin.replace("{vol_title}", vol.title);
            $.each(vol.sections, function(i, section) {
                html += li.replace("{section_title}", section.title);
                // TODO only add this link if the section has been scanned
                if (section.isScanned) {
                    html = html.replace("{view_section}", view_section_template.replace("{section_id}", section.section_id));
                } else {
                    html = html.replace("{view_section}", "");
                }
            });
            html += tr_end;
        });

        html += "</tbody></table>";

        $("#results").html(html);
        $(".view_section").click(function() {
            showSection(this.dataset.id);
        });
    }).fail(function(jqXHR,textStatus) {
        if (textStatus == "timeout") {
	        showMessage ("Timed out waiting for response! Try again later or reduce the number of graphs you are querying. If the problem persists, contact the System Administrator.");
        } else {
	        showMessage ("Error completing request!");
        }
    });
}

function showSection(section_id) {
    $.getJSON("rest/sections/" + section_id, function(data) {
        $.fancybox.open(data.pages, {
            nextEffect : 'none',
            prevEffect : 'none',
            padding    : 0,
            autoSize   : false,
            aspectRatio: true,
            helpers    : {
                title : {
                    type: 'over'
                },
                overlay : {
                    locked : false
                },
                thumbs : {
                    width  : 75,
                    height : 50,
                    source : function( item ) {
                        return item.thumb;
                    }
                }
            },
            beforeShow: function () {
                var html = '<span>Date<br>View Image<br>Title:<br></span>';
                $('.fancybox-sidebar').append(html);
            },
            tpl: {
                wrap: '<div class="fancybox-wrap" tabIndex="-1"><div class="fancybox-skin"><div class="fancybox-outer"><div class="fancybox-inner"></div><div class="fancybox-sidebar"></div></div></div></div>'
            },
//            afterLoad: function(current, previous) {
//                if (current.index == 0) {
//                    $("#fancybox-thumbs ul").css("left", 0);
//                } else if (current.index > previous.index) {
//                // subtract thumb size
//                } else {
//                }
//            }
        });
    }).fail(function(jqXHR,textStatus) {
        if (textStatus == "timeout") {
            showMessage ("Timed out waiting for response! Try again later or reduce the number of graphs you are querying. If the problem persists, contact the System Administrator.");
        } else {
            showMessage ("Error completing request!");
        }
    });
}

// A short message
function showMessage(message) {
$('#alerts').append(
        '<div class="alert">' +
            '<button type="button" class="close" data-dismiss="alert">' +
            '&times;</button>' + message + '</div>');
}
