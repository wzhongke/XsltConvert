/**
 * Created by 王忠珂 on 2016/9/2.
 */
requirejs.config({
    paths:{
        jquery: 'jquery.min'
    }
});

requirejs(['jquery', 'backtop'], function ($) {
    // new backtop.BackTop('#backTop', {
    //     mode: 'go',
    //     pos: 100,
    //     speed: 200
    // });

    $("#backTop").backtop({
        mode: 'move ',
        pos: 100,
        speed: 200
    });
    // var scroll = new scrollto.ScrollTo({});
    //
    // $("#backTop").on('click', $.proxy(scroll.move, scroll));
    // $(window).on('scroll', function () {
    //     checkPosition($(window).height());
    // });


    // function checkPosition(pos) {
    //     if($(window).scrollTop() > pos){
    //         $("#backTop").fadeIn();
    //     }else{
    //         $("#backTop").fadeOut();
    //     }
    // }
});