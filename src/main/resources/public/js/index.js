/**
 * Created by lzz on 2018/3/12.
 */

$("#save-current-path").click(function () {
    var reqdata = {};
    reqdata.path = $("#node-detail").data("url");
    reqdata.nodeData = $("#node-data").val();
    $.ajax({
        type : "post",
        contentType: 'application/json',
        url : "/update_path_data_ajax",
        data : JSON.stringify(reqdata),
        dataType:'json',
        success : function(data){
            console.log(data);
            $("#node-detail").html(syntaxHighlight(data["stat"]));
            $("#node-data").val( JSON.stringify(data["data"]) );
        }
    });
});

$(function () {
    var to = false;
    $('#demo_q').keyup(function () {
        if(to) { clearTimeout(to); }
        to = setTimeout(function () {
            var v = $('#demo_q').val();
            $('#jstree').jstree(true).search(v);
        }, 250);
    });

    var get_all_url = getUrlParam('path');
    if( typeof get_all_url == "undefined" || get_all_url == null ){
        get_all_url = "/";
    }
    var zk = getUrlParam('zk');
    if( typeof zk == "undefined" || zk == null ){
        // cookie 拿，如果没有那就真的没有了
        zk = "";
    }else{
        //写入cookie
    }
    window.zk = zk;
    $('#jstree').jstree({
        "core" : {
            "animation" : 0,
            "check_callback" : true,
            "themes" : { "stripes" : false },
            'data' : {
                'url' : function (node) {
                    return '/get_all_path_ajax?zk=' + window.zk + '&path=' + get_all_url;
                },
                'data' : function (node) {
                    return { 'id' : node.id };
                }
            }
        },
        "types" : {
            "default" : {
                "icon" : "glyphicon glyphicon-flash",
                "valid_children" : []
            },
            "leaf" : {
                "icon" : "glyphicon glyphicon-flash",
                "valid_children" : []
            },
            "tmp-leaf" : {
                "icon" : "glyphicon glyphicon-flash",
                "valid_children" : []
            },
            "ellipsis" : {
                "icon" : "glyphicon glyphicon-flash",
                "valid_children" : []
            },
            "parent" : {
                "icon" : "glyphicon glyphicon-ok",
                "valid_children" : ["default","leaf"]
            }
        },
        "plugins" : [ "search","types" ]
    });


    $('#jstree').on("changed.jstree", function (e, data) {
        var path = data.selected[0];
        $(".zk-path").text(path);
        $("#node-detail").data("url", path);
        console.log(path);
        $.get("/get_path_detail_ajax?path=" + path,function(data){
            $("#node-detail").html(syntaxHighlight(data["stat"]));
            $("#json-value").html(syntaxHighlight(data["data"]));
            $("#node-data").val( JSON.stringify(data["data"]) );
        });
    });

    $("#jstree").on("open_node.jstree", function (e, data) {
       var children = data.node.children;
        if( children.length == 1 && children[0].endsWith("/...") ){

            var url = '/get_all_path_ajax?zk=' + window.zk + '&path=' + data.node.id;
            console.log( url );
            $.get(url, function(result){
                var ref = $("#jstree").jstree(true);
                ref.delete_node(children[0]);
                result.children.forEach(function (element) {
                    ref.create_node(data.node, element);
                })
            });
        }
    });

});

