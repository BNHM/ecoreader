populateAuthors = ->
  theUrl = "rest/authors/list"
  $.getJson theUrl
  .success (data) ->
    listItems = ""
    listItems += "<option value=''>Select an author ...</option>"
    for author in data.authors
      listItems += """
      <option value="#{author}">
        #{author}
      </option>
      """
    $("#authors").html listItems
    $(".combobox").combobox()
  .fail (jqxhr, status) ->
    if status is "timeout"
      showAlertDialog "Timed out waiting for response! Try again later or reduce the number of graphs you are querying. If the problem persists, contact the System Administrator."
    else
      showAlertDialog "Error completing request!"


###
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
	        showAlertDialog ("Timed out waiting for response! Try again later or reduce the number of graphs you are querying. If the problem persists, contact the System Administrator.");
        } else {
	        showAlertDialog ("Error completing request!");
        }
    });
}


###

`
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
	        showAlertDialog ("Timed out waiting for response! Try again later or reduce the number of graphs you are querying. If the problem persists, contact the System Administrator.");
        } else {
	        showAlertDialog ("Error completing request!");
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

                  var html = "<div class='fancybox-img-download'><p>Download Image:</p><a href='' id='600' download='image.png'>600</a>" +
                             "<a href='' id='high_res' download='high_res.tif'>high res</a></div>";
                  if (this.group.length > 1) {
                      html += "<div class='fancybox-page-nav'>" +
                              "<a href='#' class='btn btn-default' onClick='$.fancybox.jumpto(0);'>First</a>" +
                              "<a href='#' class='btn btn-default'style='float:right;' " +
                              "onClick='$.fancybox.jumpto($.fancybox.group.length - 1);'>Last</a></div>";
                  }

                  $(".fancybox-tmp .fancybox-sidebar-container").html(html);
                  $(".fancybox-img-download a#600").attr("href", this.big);
                  $(".fancybox-img-download a#high_res").attr("href", this.high_res);
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
                showAlertDialog ("Timed out waiting for response! Try again later or reduce the number of graphs you are querying. If the problem persists, contact the System Administrator.");
            } else {
                showAlertDialog ("Error completing request!");
            }
        });
    })(section_id, galIndex);
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
`
