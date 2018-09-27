$(function () {
    $("#permissionlist").bootstrapTable();
});
/**
 * 列表查询参数
 * */
function queryParams(params) {
    return $("#search-form").serialize() +"&order="+params.order+"&offset="+params.offset+"&limit="+params.limit;
}
/**
 * 下标格式化
 * */
function indexFormatter(value,row,index) {
    return index +1 ;
}
/**
 * 是否可用格式化
 * */
function availableFormatter(value,row,index) {
    if(row.available == 0 ){
        value = "可分配" ;
    }else{
        value = "不可分配" ;
    }
    return value;
}
function resourceTypeForamtter(value,row,index) {
    if(row.resourceType == "menu"){
        value = "菜单" ;
    }else{
        value = "按钮" ;
    }
    return value;
}
/**
 * 操作格式化
 * */
function optionFormatter(value,row,index) {
    var strValue = "<span class='show_btn'>查看<div class='show_btn_div'><a class='J_look' title='查看' onclick='peiEditAndView("+row.permissionId+")'>查看</a>";
    if(row.available == 0){
        strValue +="<a class='J_update' title='禁用' onclick='pieChangeStatus(\""+row.permissionId+"\",\""+1+"\")'>禁用</a>";
    }else {
        strValue +="<a class='J_update' title='启用' onclick='pieChangeStatus(\""+row.permissionId+"\",\""+0+"\")'>启用</a>";
    }
    strValue +="<a class='J_update' title='删除' onclick='delPer(\""+row.permissionId+"\")'>删除</a></div></span>";
    return strValue;
}
/**
 * 查询
 * */
$(".search-btn").on('click',function (e) {
    $("#permissionlist").bootstrapTable('refresh');
});
/**
 * 跳转新增角色
 * */
$(".add-btn").on('click',function (e) {
    poster.peiEditAndView();
});
/**
 * 更改状态
 * */
function pieChangeStatus(id,status) {
    poster.pieChangeStatus(id,status);
}
/**
 * 删除角色
 * */
function delPer(id) {
    poster.delPer(id);
}
/**
 * 跳转角色详情与修改
 * */
function peiEditAndView(id) {
    poster.peiEditAndView(id);
}