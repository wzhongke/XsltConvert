/**
 * Created by 王忠珂 on 2016/9/4.
 */
define(['jquery'], function ($) {
   function ScrollTo(opts) {
       this.opts = $.extend({}, ScrollTo.DEFAULTS, opts);
       this.$el = $('html, body');
   }

   ScrollTo.DEFAULTS = {
       dest: 0,
       speed: 800
   };
   
   ScrollTo.prototype.move = function () {
       var opts = this.opts,
           dest = opts.dest;
       if ($(window).scrollTop() != dest) {
            if(!this.$el.is(":animated")){
                this.$el.animate({
                    scrollTop : dest
                }, opts.speed);
            }
       }
   };

   ScrollTo.prototype.go = function (){
       this.$el.scrollTop(0);
   };
   return{
       ScrollTo: ScrollTo
   }
});