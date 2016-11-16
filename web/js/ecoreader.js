function populateAuthors() {

    $.ajax({
        url: "rest/authors/list",
        async: false,
        success: function (data) {
            var listItems = "";
            listItems += "<option value=''>Select an author ...</option>";
            $.each(data.authors, function (index, author) {
                listItems += "<option value='" + author + "'>" + author + "</option>";
            });
            $("#authors").html(listItems).combobox();
        },
        fail: function (jqXHR, textStatus) {
            if (textStatus == "timeout") {
                showMessage("Timed out waiting for response! Try again later or reduce the number of graphs you are querying. If the problem persists, contact the System Administrator.");
            } else {
                showMessage("Error completing request!");
            }
        }
    });
}

function populateGeographies() {

    $.ajax({
        url: "rest/sections/geographies",
        async: false,
        success: function (data) {
            var listItems = "";
            $.each(data, function (index, geographic) {
                listItems += "<option value='" + geographic + "'>" + geographic + "</option>";
            });
            $("#geographies").html(listItems).multiselect({
                enableCaseInsensitiveFiltering: true,
                nonSelectedText: "Select a geographic",
                numberDisplayed: 1,
                includeSelectAllOption: true,
                selectAllText: "Select All Visible",
                allSelectedText: false,
                buttonContainer: '<div class="btn-group btn-block" />',
                buttonClass: '',
                maxHeight: 300,
                templates: {
                    button: '<button type="button" class="button btn btn-default multiselect dropdown-toggle col-sm-7" id="geographies-button" data-toggle="dropdown"><span class="multiselect-selected-text"></span></button><button type="button" class="btn btn-default dropdown-toggle" id="geographies-dropdown" data-toggle="dropdown"><b class="caret"></b></button>',
                    ul: '<ul class="multiselect-container dropdown-menu col-sm-7"></ul>',
                }
            });
        },
        fail: function (jqXHR, textStatus) {
            if (textStatus == "timeout") {
                showMessage("Timed out waiting for response! Try again later or reduce the number of graphs you are querying. If the problem persists, contact the System Administrator.");
            } else {
                showMessage("Error completing request!");
            }
        }
    });
}

function populateVolumes() {
    theUrl = "rest/volumes/?author=" + $("#authors").val() + "&" + $("form").serialize();
    /*if ($("#authors").val().length <= 0) {
     $("#author_combobox").addClass("has-error");
     return;
     } else {
     $("#author_combobox").removeClass("has-error");
     }  */
    $.getJSON(theUrl, function (data) {
        //$.getJSON( theUrl + "?" + $("form").serialize(), function(data) {


        var list_group_tpl = "<ul class='list-group'>{list}</ul>";
        var list_heading_tpl = "<h4 class='list-group-heading'>{vol_title}</h4>";
        var list_item_tpl = "<li class='list-group-item'>{section_title}{view_section}</li>";
        var list;
        var html = "";
        var view_section_template = " [<a href='#' class='view_section' data-id='{section_id}'>view section</a>]";


        // generate the table of volumes and the corresponding sections
        $.each(data.volumes, function (i, vol) {
            list = list_heading_tpl.replace("{vol_title}", vol.title)
            $.each(vol.sections, function (i, section) {
                list += list_item_tpl.replace("{section_title}", section.title);
                if (section.isScanned) {
                    list = list.replace("{view_section}", view_section_template.replace("{section_id}", section.section_id));
                } else {
                    list = list.replace("{view_section}", "");
                }
            });
            html += list_group_tpl.replace("{list}", list);
        });


        if (!html) {
            $("#results").html("</b>No results to show for your search!</b>").show();
        } else {
            $("#results").html(html).show();
            $(".view_section").click(function () {
                showSection(this.dataset.id);
            });
        }

    }).fail(function (jqXHR, textStatus) {
        if (textStatus == "timeout") {
            showMessage("Timed out waiting for response! Try again later or reduce the number of graphs you are querying. If the problem persists, contact the System Administrator.");
        } else {
            $("#results").html("</b>Unable to complete request. No results to show for your search!</b>").show();
        }
    });
}

function getPage() {
    return parseInt(sessionStorage.getItem('pageIndex'));
}
function gotoPage(page) {
    sessionStorage.setItem('pageIndex', page);
    return page;
}
function nextPage() {
    var curValue = getPage() + 1;
    sessionStorage.setItem('pageIndex', curValue);
    return curValue;
}
function prevPage() {
    var curValue = getPage() - 1;
    sessionStorage.setItem('pageIndex', curValue);
    return curValue;
}

function readSection(section_id, index) {
    $.fancybox.open($.fancybox.group, {
        //padding     : [15, 190, 15, 15],
        nextEffect: 'fade',
        prevEffect: 'fade',
        fitToView: false,
        autoSize: false,
        beforeLoad: function () {
            // set href here so the img is displayed at the "big" size vs the "href" size
            this.href = this.big;
        },
        afterLoad: function (upcoming, current) {
            if (current == null) {
                this.index = index;
            } else {
                index = upcoming.index;
            }
            this.href = this.group[index].big;
            this.title = "page: " + index;
        },
        beforeShow: function () {
            $("#content").css({'overflow-x': 'hidden'});
        },
        afterClose: function () {
            showSectionFancybox(section_id, index, this.group);
            $("#content").css({'overflow-x': 'visable'});
        }

    });
}
function jumpTo() {
    if ((event.target != $("#pageJump")[0] || event.keyCode == 13) && $("#pageJump").val()) {
        $.fancybox.jumpto(gotoPage($('#pageJump').val()));
    }
}

function showSectionFancybox(section_id, galIndex, data) {
    $.fancybox.open(data, {
        padding: [15, 190, 15, 15],
        nextEffect: 'fade',
        prevEffect: 'fade',
        autoSize: true,
        helpers: {
            thumbs: {
                width: 75,
                height: 103,
                source: function (item) {
                    return item.thumb;
                }
            }
        },
        afterLoad: function (upcoming, current) {
            if (current == null) {
                if (galIndex == null) {
                    galIndex = 0;
                }
                this.index = galIndex;
            } else {
                galIndex = upcoming.index;
            }
            sessionStorage.setItem('pageIndex', galIndex);
            this.href = this.group[galIndex].href;
            this.title = "page: " + galIndex;
        },
        beforeShow: function () {
            // if these are not big, we offer navigation options
            var sidebar = $('<div class="fancybox-sidebar"><div class="fancybox-sidebar-container"></div></div>');
            this.skin.append(sidebar);

            var html = "<div class='fancybox-img-download'>" +
                "<p><a href='" + this.big + "' download='image.png'>Download Image</a></p>" +
                "<p><a href='" + this.big + "' target='_blank'>Link to Image</a></p>";
            html += "<div class='fancybox-page-nav'>" +
                "<div><img src='img/zoomin.png'><label> click in center of image to enlarge</label></div>";
            if (this.group.length > 1) {
                html += "<div class='fancybox-page-jump btn btn-default' onClick=\"jumpTo();\">" +
                    "<label>Go To Page:</label><input class='text' id='pageJump' onkeypress='if(event.keyCode==13) {jumpTo();}'/></div>" +
                    "<a href='#' class='btn btn-default' onClick='$.fancybox.jumpto(gotoPage(0));' style='float:left'>|<</a>" +
                    "<a href='#' class='btn btn-default' onClick='$.fancybox.jumpto(prevPage());' style='float:left'><</a>" +
                    "<a href='#' class='btn btn-default' onClick='$.fancybox.jumpto(nextPage());' style='float:left'>></a>" +
                    "<a href='#' class='btn btn-default' style='float:left;' " +
                    "onClick='$.fancybox.jumpto(gotoPage($.fancybox.group.length - 1));'>>|</a>";
            }
            html += "</div>";

            $(".fancybox-tmp .fancybox-sidebar-container").html(html);
            $(".fancybox-img-download a#1200").attr("href", this.big);
        },
        onUpdate: function () {
            $(".fancybox-sidebar").height(this.inner.height());
        },
        afterShow: function () {

            if ($(".fancybox-prev").length == 0) {
                $("<a id='img_link' href='#'></a>").insertAfter(".fancybox-inner");
                $("#img_link").css("width", "100%").css("left", "0");

            } else {
                $("<a id='img_link' href='#'></a>").insertAfter(".fancybox-prev");
            }

            $("#img_link").click({href: this.big}, function (event) {
                (function (index) {
                    $.fancybox.close();
                    readSection(section_id, index);
                })($.fancybox.current.index);
            });
        }
    });

}

function showSection(section_id) {
    (function (section_id) {
        $.getJSON("rest/sections/" + section_id, function (data) {
            showSectionFancybox(section_id, 0, data.pages);
        }).fail(function (jqXHR, textStatus) {
            if (textStatus == "timeout") {
                showMessage("Timed out waiting for response! Try again later or reduce the number of graphs you are querying. If the problem persists, contact the System Administrator.");
            } else {
                showMessage("Error completing request!");
            }
        });
    })(section_id);
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
