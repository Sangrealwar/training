/**
 * Created by wq on 2017/2/22.
 */

//获取Url参数的jquery扩展函数
(function($){
    $.getMyUrlParam
        = function(name)
    {
        var reg
            = new RegExp("(^|&)"+
            name +"=([^&]*)(&|$)");
        var r
            = window.location.search.substr(1).match(reg);
        if (r!=null) return unescape(r[2]); return null;
    }
})(jQuery);


//默认的全局分页属性
function pageOption() {
}
pageOption.defaultPage = 1;    //默认显示当前页
pageOption.pageSize = 10;    //默认每页展示10条数据
