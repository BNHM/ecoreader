function populateAuthors() {
    theUrl = "rest/authors/list";
    var jqxhr = $.getJSON( theUrl, function(data) {
        var listItems = "";
        listItems+= "<option value=''>Select an author ...</option>";
        $.each(data.authors,function(index,author) {
            listItems+= "<option value='" + author  + "'>" + author + "</option>";
        });
        $("#authors").html(listItems);
        $(".combobox").combobox();

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
                if (section.isScanned) {
                    html = html.replace("{view_section}", view_section_template.replace("{section_id}", section.section_id));
                } else {
                    html = html.replace("{view_section}", "");
                }
            });
            html += tr_end;
        });

        html += "</tbody></table>";

        $("#results").html(html).show();
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

function showSection(section_id, galIndex) {
    (function(section_id, galIndex) {
        $.getJSON("rest/sections/" + section_id, function(data) {

        $.fancybox.open(data.pages, {
             padding     : [15, 190, 15, 15],
             nextEffect  : 'fade',
             prevEffect  : 'fade',
             autoSize    : true,
             helpers     : {
                 thumbs  : {
                    width: 75,
                    height: 103,
                    source: function( item ) {
                        return item.thumb;
                    }
                 }
             },
             beforeShow: function(){
                  var sidebar = $('<div class="fancybox-sidebar"><div class="fancybox-sidebar-container"></div></div>');
                  this.skin.append(sidebar);

                  var html = "<div class='fancybox-img-download'><a href='' download='image.png'>Download Image</a>" +
                             "</div><div class='fancybox-page-nav'><a href='#' onClick='$.fancybox.jumpto(0);'>First</a>" +
                             "<a href='#' style='display:block;float:right;' onClick='$.fancybox.jumpto($.fancybox.group.length - 1);'>Last</a></div>";

                  $(".fancybox-tmp .fancybox-sidebar-container").html(html);
                  $(".fancybox-img-download a").attr("href", this.big);
             },
             onUpdate: function() {
                $(".fancybox-sidebar").height(this.inner.height());
             },
             afterShow: function() {
                if (galIndex != null) {
                    $.fancybox.jumpto(galIndex);
                    galIndex = null;
                }
                $("img.fancybox-image").click( {href: this.big} ,function(event) {
                      (function(index) {
                          $.fancybox.close();
                          $.fancybox.open({
                            width: "100%",
                            height: "100%",
                            href: event.data.href,
                            type: "iframe",
                            afterClose: function() {
                                showSection(section_id, index);
                            }
                          });
                      })($.fancybox.current.index);
                });
             }
            });
        }).fail(function(jqXHR,textStatus) {
            if (textStatus == "timeout") {
                showMessage ("Timed out waiting for response! Try again later or reduce the number of graphs you are querying. If the problem persists, contact the System Administrator.");
            } else {
                showMessage ("Error completing request!");
            }
        });
    })(section_id, galIndex);
}

// A short message
function showMessage(message) {
$('#alerts').append(
        '<div class="alert">' +
            '<button type="button" class="close" data-dismiss="alert">' +
            '&times;</button>' + message + '</div>');
}
