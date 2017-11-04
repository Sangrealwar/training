/**
 * Created by wq on 2017/1/14.
 */

//新增页保存按钮
function add_btn_save()
{
    var form = new FormData($("#addForm")[0]);

    $.ajax({
        url: '/spitter/add.do',
        type: 'post',
        data: form,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function (data) {
            //正常回调
            if(data.status==0){
                alert('操作成功');
                window.location.href="/view/spitter/list.html";
            }else if(data.status==1) {
                alert(result.msg);
            }
        },
        error:function (data) {
            alert('操作失败');
        }
    });
    return false;
}