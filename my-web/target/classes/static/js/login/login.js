$(".btn-info").on("click",function () {
    poster.login($(".form-horizontal"));
});
function changeValidatecode(param) {
    poster.changeValidatecode(param);
}
$(".btn-default").on("click",function () {
    poster.registerUser();
});
$(document).keyup(function(event){
    if(event.keyCode ==13){
        poster.login($(".form-horizontal"));
    }
});