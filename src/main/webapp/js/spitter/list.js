/**
 * Created by wq on 2017/1/14.
 */


//获取选中行ID
function getCheckedIDs(name) {
    var chk_value = [];        //记录所有的选中的单选框的值
    $('input[name="' + name + '"]:checked').each(function () {
        chk_value.push($(this).val());
    });
    return chk_value;
}

//更新某一行
function updateRow() {
    var ids = [];        //记录所有的选中的单选框
    ids = getCheckedIDs('ckbox');
    if (ids.length == 0) {
        alert('必须选择一行');
    }
    else if (ids.length > 1) {
        alert('不能选择多行');
    }
    else {
        window.location.href = "/view/spitter/detail.html?id=" + ids[0];
    }
}

//批量删除
function deleteRows() {
    var ids = [];        //记录所有的选中的单选框
    ids = getCheckedIDs('ckbox');

    if (confirm('是否删除')) {
        var deleteUsers = [];
        for (var i = 0; i < ids.length; i++) {
            var deleteUser = {"id": ids[i]};
            deleteUsers.push(deleteUser);
            alert(ids[i]);
        }

        // $.ajax({
        //     url: '/user/multiDelete.do',
        //     type: 'post',
        //     dataType: 'json',
        //     contentType: 'application/json',
        //     repository: JSON.stringify(deleteUsers),
        //     success: function (result) {
        //         //正常回调
        //         if (result.status == 0) {
        //             alert('操作成功');
        //             getData();
        //         } else if (result.status == 1) {
        //             alert(result.msg);
        //         }
        //     },
        //     error: function (repository) {
        //         alert('操作失败');
        //     }
        // });
    }
}

//关联角色的点击事件
function assRole() {
    // var ids = [];        //记录所有的选中的单选框
    // ids = getCheckedIDs('ckbox');
    //
    // if (ids.length == 0) {
    //     alert('必须选择一行');
    // }
    // else if (ids.length > 1) {
    //     alert('不能选择多行');
    // }
    // else {
    //     window.location.href = '/auth/user/assrole.html?uid=' + ids[0];
    // }
}

//项目中代码
//界面初始化触发的事件
$(document).ready(function () {
    getData();
})
// 填充数据
function fillData(data) {
    if (data != null) {
        var datetr = "";
        for (var i = 0; i < data.length; i++) {
            datetr += "<tr class='dataRr'>";
            datetr += "<td>";
            datetr += "<input class='dp_ckbox_def' name='ckbox' type='checkbox'  value='" + data[i].id + "'/>";
            datetr += "</td>";
            datetr += "<td>";
            datetr += i + 1;
            datetr += "</td>";
            datetr += "<td>";
            datetr += data[i].username;
            datetr += "</td>";
            datetr += "<td>";
            datetr += data[i].firstName;
            datetr += "</td>";
            datetr += "<td>";
            datetr += data[i].lastName;
            datetr += "</td>";
            datetr += "</tr>";
        }
        $("#dataContent").html(datetr);
    }
}
// 获取数据
function getData() {
    $.ajax({
        type: "GET",
        contentType: 'application/json',
        dataType: 'json',
        url: "/spitter/list.do", //?curPage=" + pageOption.defaultPage + "&pageSize=" + pageOption.pageSize + "",
        success: function (result) {
            // alert(result);
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


//列表页新增按钮点击事件
function btn_add()
{
    window.location.href="/view/spitter/add.html";
}
