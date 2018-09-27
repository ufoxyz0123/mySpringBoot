$(function () {
    var $menu = $('.nav-menu'); // 一级菜单
    var $subMenu = $('.sub-menu'); // 二级菜单
    var path = location.pathname; // 当前路径

    // 一级菜单点击响应
    $menu.on('click', '.menu-item', function () {
        var $menu = $(this);
        var $subMenu = $menu.next('.sub-menu');
        if (!$subMenu.length) {
            $menu.find('.active').removeClass('active');
            $(this).addClass('active');
        }
        $menu.find('.icon').toggleClass('icon-arrow-down').toggleClass('icon-arrow-up');
        $subMenu.slideToggle().siblings('.sub-menu').slideUp();
    });

    // 二级菜单点击响应
    $subMenu.on('click', '.sub-menu-item', function () {
        $menu.find('.active').removeClass('active');
        $(this).addClass('active');
    });

    // 禁止同页面跳转
    $subMenu.on('click', 'a[href]', function (e) {
        if ($(this).attr('href') === location.pathname) {
            e.preventDefault();
            return false;
        }
    });

    // 菜单初始化(一级)
    $menu.find('a[href]').each(function (index, item) {
        var $ele = $(item);
        var $menu;
        if ($ele.attr('href') !== path) {
            return;
        }
        $menu = $ele.closest('.menu-item').addClass('active');;
        return false;
    });

    // 菜单初始化(二级)
    $subMenu.find('a[href]').each(function (index, item) {
        var $ele = $(item);
        var $subMenu;
        if ($ele.attr('href') !== path) {
            return;
        }
        $subMenu = $ele.closest('.sub-menu');
        $subMenu.find('.icon').toggleClass('icon-arrow-down').toggleClass('icon-arrow-up');
        $subMenu.slideToggle().siblings('.sub-menu').slideUp();
        $ele.closest('.sub-menu-item').trigger('click');
        return false;
    });
    /**
     * head 改变语言
     * */
    $(".changeLanauge").on("click", function (e) {
        var lanauge = $(e.target).attr("data-value");
        poster.changeSessionLanauage(lanauge);
    });
    /**
     * head 账号管理
     * */
    $(".white.user-view").on("click", function (e) {
        debugger
        var username = $(e.target).data("username");
        var id = $(e.target).data("id");
        poster.editAndView(id,username);
    });
});