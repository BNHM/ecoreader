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
    if ($("#authors").val().length <= 0) {
        $("#author_combobox").addClass("has-error");
        return;
    } else {
        $("#author_combobox").removeClass("has-error");
    }
    $.getJSON( theUrl + $("#authors").val() + "?" + $("form").serialize(), function(data) {
        var list_group_tpl = "<ul class='list-group'>{list}</ul>";
        var list_heading_tpl = "<h4 class='list-group-heading'>{vol_title}</h4>";
        var list_item_tpl = "<li class='list-group-item'>{section_title}{view_section}</li>";
        var list;
        var html = "";
        var view_section_template = " [<a href='#' class='view_section' data-id='{section_id}'>view section</a>]";

        // generate the table of volumes and the corresponding sections
        $.each(data.volumes, function(i, vol) {
            list = list_heading_tpl.replace("{vol_title}", vol.title)
            $.each(vol.sections, function(i, section) {
                list += list_item_tpl.replace("{section_title}", section.title);
                if (section.isScanned) {
                    list = list.replace("{view_section}", view_section_template.replace("{section_id}", section.section_id));
                } else {
                    list = list.replace("{view_section}", "");
                }
            });
            html += list_group_tpl.replace("{list}", list);
        });

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

function getPage() {
    return parseInt(sessionStorage.getItem('pageIndex'));
}
function gotoPage(page) {
    sessionStorage.setItem('pageIndex',page);
    return page;
}
function nextPage() {
    var curValue = getPage()+1;
    sessionStorage.setItem('pageIndex',curValue);
    return curValue;
}
function prevPage() {
    var curValue = getPage()-1;
    sessionStorage.setItem('pageIndex',curValue);
    return curValue;
}

function showSection(section_id, galIndex) {
 sessionStorage.setItem('pageIndex', 0);
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

                  var html = "<div class='fancybox-img-download'><p><a href='" +this.big + "' download='image.png'>Download Image</a></p>";
                  if (this.group.length > 1) {
                      html += "<div class='fancybox-page-nav'>" +
                              "<a href='#' class='btn btn-default' onClick='$.fancybox.jumpto(gotoPage(0));' style='float:left'>|<</a>" +
				"<a href='#' class='btn btn-default' onClick='$.fancybox.jumpto(prevPage());' style='float:left'><</a>" +
				"<a href='#' class='btn btn-default' onClick='$.fancybox.jumpto(nextPage());' style='float:left'>></a>" +
                              "<a href='#' class='btn btn-default' style='float:left;' " +
                              "onClick='$.fancybox.jumpto(gotoPage($.fancybox.group.length - 1));'>>|</a></div>";
                  }

                  $(".fancybox-tmp .fancybox-sidebar-container").html(html);
                  $(".fancybox-img-download a#1200").attr("href", this.big);
             },
             onUpdate: function() {
                $(".fancybox-sidebar").height(this.inner.height());
             },
             afterShow: function() {
                if (galIndex != null) {
                    $.fancybox.jumpto(galIndex);
                    galIndex = null;
                }

                $("<a id='img_link' href='#'></a>").insertAfter(".fancybox-prev");

                $("#img_link").click( {href: this.big} ,function(event) {
                      (function(index) {
                          $.fancybox.close();
                          $.fancybox.open({
			    fitToView: false,
			    autoSize: true,
                            width: "100%",
                            height: "100%",
                            href: event.data.href,
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

function toggleQuery() {
    if ($('.toggle-content#query_toggle').is(':hidden')) {
        $('.toggle-content#query_toggle').show(400);
        $('#toggle_query button').html("-");
    } else {
        $('.toggle-content#query_toggle').hide(400);
        $('#toggle_query button').html("+");
    }
}
