/**
 * Created by wq on 2017/1/14.
 */


//界面初始化触发的事件
$(document).ready(function (){
    getData();
})

// 获取数据
function getData()
{
    var id=$.getMyUrlParam('id');
    if(id == null)
        return;
    // alert(id);
    $.ajax({
        type: "GET",
        contentType:'application/json',
        dataType: 'json',
        url: "/spitter/"+id,
        success: function (result) {
            //正常回调
            if(result.status==0){
                fillData(result.data);
            }else if(result.status==1) {
                alert(result.msg);
            }
        },
        error: function (result) {
            alert("error:" + result.msg);
        }
    })
}

// 填充数据
function fillData(data) {
    if (data != null) {
        $("#id").val(data.id);
        $("#username").val(data.username);
        $("#firstName").val(data.firstName);
        $("#lastName").val(data.lastName);
    }
}

// function clickUpdate()
// {
//     $.ajax({
//         url: '/user/update.do',
//         type: 'post',
//         dataType: 'json',
//         contentType:'application/json',
//         repository: JSON.stringify(updateUser),
//         success: function (result) {
//             //正常回调
//             if(result.status==0){
//                 alert('操作成功');
//                 fillData(result.repository);
//             }else if(result.status==1) {
//                 alert(result.msg);
//             }
//         },
//         error:function (repository) {
//             alert('操作失败');
//         }
//     });
//     return false;
// }
