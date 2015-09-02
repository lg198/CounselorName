function generatePacket(funcname, params) {
    return {"function":funcname, "params":params};
}

Backend = {};

Backend.searchStudents = function(query, callback) {
    var data = generatePacket("searchStudents", {"query":query});

    $.post("/handle", data, callback, "json");
}


//////////////////////////////////////////////////////////////////////////////////////////////////////////

$(function() {
    $(".list").on("click", "li", function(evt) {
        var selected = $(this);
        if (!selected.hasClass("active")) {
            $(".list li.active").each(function() {
               $(this).toggleClass("active");
            });
            selected.toggleClass("active");
        }
        /*$(".list > li").each(function() {
            if (!$(this).is(selected) && $(this).hasClass("active")  ) {
                $(this).removeClass("active");
            }
        }); */
    });
    
    $("#student-search").on("keyup change", function() {
        Backend.searchStudents($(this).val(), function(ret) {
            alert(ret);
        });
    });
});

