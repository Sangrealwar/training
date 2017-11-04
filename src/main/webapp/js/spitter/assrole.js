/**
 * Created by wq on 2017/1/19.
 */

//界面初始化触发的事件
$(document).ready(function () {
    //getData();

    //queryUserName();
})

// 获取用户关联的角色数据
function getData() {
    var userId =$.getMyUrlParam('uid');
    if(userId!= null) {
        $.ajax({
            type: "GET",
            contentType: 'application/json',
            dataType: 'json',
            url: "/user/assRole.do?curPage=" + pageOption.defaultPage +
            "&pageSize=" + pageOption.pageSize + "&userid=" + userId,
            success: function (result) {
                //正常回调
                if (result.status == 0) {
                    fillData(result.data);
                } else if (result.status == 1) {
                    alert(result.msg);
                }
            },
            error: function (result) {
                alert("error:" + result.msg);
            }
        })
    }
}

// 填充已关联数据
function fillData(data) {
    if(data!= null) {
        var datetr = "";
        for (var i = 0; i < data.length; i++) {
            datetr += "<tr class='clicktr'>";
            datetr += "<td>";
            datetr += "<input class='dp_ckbox_def' name='assid' type='checkbox' value='" +data[i].id + "'/>";
            datetr += "<input class='dp_ckbox_def dp_hiden' name='rid' type='text' value='" + data[i].rid + "'/>";
            datetr += "</td>";
            datetr += "<td>";
            datetr += i + 1;
            datetr += "</td>";
            datetr += "<td>";
            datetr += data[i].rCode;
            datetr += "</td>";
            datetr += "<td>";
            datetr += data[i].rName;
            datetr += "</td>";
            datetr += "<td>";
            datetr += data[i].note;
            datetr += "</td>";
            datetr += "</tr>";
        }
        $("#bindRole").html(datetr);
    }
}


//获取该用户未关联的角色数据，并绑定到弹出框中，以便给当前用户分配角色
function bindRoles() {
    var userId =$.getMyUrlParam('uid');
    $.ajax({
        type: "GET",
        contentType: 'application/json',
        dataType: 'json',
        url: "/user/nAssRole.do?uid="+userId,
        success: function (result) {
            //正常回调
            if (result.status == 0) {
                fillRoles(result.data);
            } else if (result.status == 1) {
                alert(result.msg);
            }
        },
        error: function (result) {
            alert("success:" + result.msg);
        }
    })
}

//填充用户未关联角色数据
function fillRoles(data)
{
    var datetr = "";
    if(data!=null){
        for (var i = 0; i < data.length; i++) {
            datetr += "<tr class='roleDataTr'>";
            datetr += "<td>";
            datetr += "<input class='dp_ckbox_def' name='rid' type='checkbox' value='" + data[i].id + "'/>";
            datetr += "</td>";
            // datetr += "<td>";
            // datetr += i + 1;
            // datetr += "</td>";
            datetr += "<td>";
            datetr += data[i].rolecode;
            datetr += "</td>";
            datetr += "<td>";
            datetr += data[i].rolename;
            datetr += "</td>";
            datetr += "</tr>";
        }

        $("#noneBindRole").html(datetr);
    }
}

//移除关联角色
function removeMainTable()
{
    if($('#bindRole input[name="assid"]:checked').length>0) {    //如果存在选中的单选框
        if (confirm('是否移除选中的角色')) {
            $('#bindRole input[name="assid"]:checked').each(function () {
                    $(this).prop("checked", false);
                    $(this).parent().parent().css("display", "none");
            });
        }
    }
}

//添加关联角色
function addToMainTable(){
    $('#noneBindRole input[name="rid"]:checked').each(function () {
        var rid = $(this).val();
        var rCode = $(this).parent("td").parent("tr").find("td:eq(2)").html();
        var rName = $(this).parent("td").parent("tr").find("td:eq(3)").html();
        var datetr="";
        datetr += "<tr class='dataTr'>";
        datetr += "<td>";
        datetr += "<input class='dp_ckbox_def' name='assid' type='checkbox' value=''/>";
        datetr += "<input class='dp_ckbox_def dp_hiden' name='rid' type='text' value='" + rid + "'/>";
        datetr += "</td>";
        // datetr += "<td>";
        // datetr += "";
        // datetr += "</td>";
        datetr += "<td>";
        datetr += rCode;
        datetr += "</td>";
        datetr += "<td>";
        datetr += rName;
        datetr += "</td>";
        datetr += "<td>";
        datetr += "";
        datetr += "</td>";
        datetr += "</tr>";
        $("#bindRole").append(datetr);
    });
    $('#myModa1').modal('hide');
}

//根据传过来的用户ID获取用户名
function queryUserName() {
    var userId =$.getMyUrlParam('uid');
    $.ajax({
        type: "GET",
        contentType:'application/json',
        dataType: 'json',
        url: "/user/detail.do?id="+userId,
        success: function (result) {
            //正常回调
            if(result.status==0){
                //将用户名信息绑定到界面上
                if(result.data!= null) {
                    $("#userName").html(result.data.loginname);
                }
            }else if(result.status==1) {
                alert(result.msg);
            }
        },
        error: function (result) {
            alert("error:" + result.msg);
        }
    })
}

//保存用户选择的角色信息
function save() {
    var userId =$.getMyUrlParam('uid');
    var assRoles =[];
    $("#bindRole").find("tr").each(function (){    //遍历表格的tr
        if($(this).css("display") != "none")   //保存显示的行
        {
            alert("原有的关联编号：" +$(this).find("td:eq(0)").find('input[name="assid"]').eq(0).val()
                + "\t"+"用户ID：" +userId
                +"\t" +"角色ID：" +$(this).find("td:eq(0)").find('input[name="rid"]').eq(0).val());
        }
    })
}
