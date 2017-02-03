/**
 * Created by 王忠珂 on 2016/7/22.
 */

// 函数绑定

var handler ={
    message : "Event handled",
    handleClick: function(event){
        console.log(this.message);
    }
};

var btn = document.getElementById("my-btn");

function bind(fn, context){
    return function () {
        return fn.apply(context, arguments);
    }
}

btn.onclick = handler.handleClick.bind(handler);

//函数柯里化
function curry(fn){
    var args = Array.prototype.slice.call(arguments, 1);
    return function(){
        var innerArgs = Array.prototype.slice.call(arguments);
        var finalArgs = args.concat(innerArgs);
        return fn.apply(null, finalArgs);
    }
}


function curryBind(fn, context){
    var args = Array.prototype.slice.call(arguments, 2);
    return function(){
        var innerArgs = Array.prototype.slice.call(arguments);
        var finalArgs = args.concat(innerArgs);
        return fn.apply(context, finalArgs);
    }
}

// 高级定时器
function chunk(array, process, context){
    setTimeout(function(){
        var item = array.shift();
        process.call(context, item);
        if(array.length > 0){
            setTimeout(arguments.callee, 100);
        }
    },100);
}

// 函数节流
var processor = {
    timeoutId: null,
    performProcessing:function(){

    },

    process:function () {
        clearTimeout(this.timeoutId);
        var that = this;
        this.timeoutId = setTimeout(function(){
            that.performProcessing();
        }, 100);
    }
};

function throttle(method, context){
    clearTimeout(method.tId);
    method.tId = setTimeout(function(){
        method.call(context);
    },100);
}
// 平滑滚动 wap端
function rt() {
    var d = document,
        dd = document.documentElement,
        db = document.body,
        top = dd.scrollTop || db.scrollTop,
        step = Math.floor(top / 20);
    (function() {
        top -= step;
        if (top > -step) {
            dd.scrollTop == 0 ? db.scrollTop = top: dd.scrollTop = top;
            setTimeout(arguments.callee, 20);
        }
    })();
}
// 自定义事件
function EventTarget(){
    this.handlers= {};  // 储存事件处理程序
}

EventTarget.prototype = {
    constructor : EventTarget,
    addHandler: function(type, handler){     // 注册处理程序
        if(typeof this.handlers[type] == "undefined"){ // 初始化
            this.handlers[type] = [];
        }
        this.handlers[type].push(handler);
    },
    fire: function (event) {    // 触发事件
        if(!event.target){
            event.target= this;
        }
        if(this.handler[event.type] instanceof Array){
            var handlers = this.handlers[event.type];
            for(var i=0, len= handlers.length; i< len; i++){
                handlers[i](event);
            }
        }
    },
    removeHandler:function (type, handler) {
        if(this.handlers[type] instanceof Array){
            var handlers = this.handlers[type];
            for(var i=0, len= handlers.length; i< len; i++){
                if(handlers[i] === handler)
                    break;
            }
            handlers.splice(i,1);
        }
    }
};

function handleMessage(event){
    console.log("Message received: " + event.message);
}
var target = new EventTarget();
target.addHandler("message", handleMessage);
target.fir({type:"message", message:"Hello world!"});
target.removeHandler("message", handleMessage);

// 拖动对象
var DragDrop = function () {
    var dragdrop = new EventTarget();
        dragging = null,
        diffX = 0,
        dffY = 0;
    function handleEvent(event){
        var target = event.target;
        switch (event.type){
            case "mousedown":
                if(target.className.indexOf("draggable" > -1)){
                    dragging = target;
                    diffX = event.clientX - target.offsetLeft;
                    diffY = event.clientY - target.offsetTop;
                    dragdrop.fire({type:"dragstart", target: dragging, x:event.clientX, y:event.clientY});
                }
                break;
            case "mousemove":
                if(dragging !== null){
                    //指定位置
                    dragging.style.left = (event.clientX - diffX) + "px";
                    dragging.style.top = (event.clientY -diffY) + "px";
                    //触发自定义事件
                    dragdrop.fire({type:"drag", target:dragging,x:event.clientX, y:event.clientY });
                }
                break;
            case "mouseup":
                dragdrop.fire({type:"drag", target:dragging,x:event.clientX, y:event.clientY });
                dragging = null;
                break;
        }
    }

    dragdrop.enable = function(){
            document.addEventListener("musedown", handleEvent);
            document.addEventListener("mousemove", handleEvent);
            document.addEventListener("mouseup", handleEvent);
        },
    dragdrop.disable = function () {
        document.removeEventListener("musedown", handleEvent);
        document.removeEventListener("mousemove", handleEvent);
        document.removeEventListener("mouseup", handleEvent);
    };
    return dragdrop;
}();

// 解析XML
var verseTxt = '<li><a href="${href}" class="verse-txt" data-keyword="${keyword}"><p>${title}</p>${description}</a>',
    verseFrom = '<p class="verse-from">${description}</p>',
    page = 1,
    totalPage = $("#kmap_btn_prev0923").attr("totalpage"),
    kmapquery = $("#keyword").attr("value");
// 跨域请求
function jsonp(url) {
    var a = document.createElement("script");
    a.src = url;
    a.charset = 'GBK';
    document.getElementsByTagName("head")[0].appendChild(a);
    return a;
}


// swiper
define(['jquery'], function($){

    function Carousel(container, opts) {
        this.opts = $.extend(Carousel.DEFAULT_OPTS, opts);
        this.container = $(container);
        if (this.container.length < 1) {
            return ;
        }
        this.init();
    }

    Carousel.DEFAULT_OPTS = {
        margin : 0,
        wrapperClass: ".layout-width",
        listClass:".item-list",
        autoplay: true,
        speed : 300,
        pagination: "#page",
        paginationElement: 'i',
        paginationClass: 'active',
        loop:false
    };

    Carousel.prototype.init = function () {
        this.constructList();
        this.constructPagination();


        // 绑定事件
        this.bindTouch();
        var that = this;
        $(window).resize(function() {
            that.resizePage();
        });
        if (this.opts.autoplay) {
            this.autoPlay();
        }
    };

    Carousel.prototype.constructList = function () {
        this.list = this.container.find(this.opts.listClass);   // 获取要轮播的元素
        this.wrapper = this.container.find(this.opts.wrapperClass).slice(0, 1);
        this.page = $(this.opts.pagination);  // 页码位置
        this.currentPage = 1;
        this.totalPage = this.list.length;

        // 在前边添加最后一个元素，在后边添加第一个元素，轮播最后一个和第一元素之间切换用
        this.wrapper.prepend(this.list.last().clone()).append(this.list.first().clone());
        this.list = this.container.find(this.opts.listClass);
        this.resizePage();
    };

    //初始化页码
    Carousel.prototype.constructPagination = function () {
        var paginationHTML = "<" + this.opts.paginationElement + " class='" + this.opts.paginationClass +
            "'></"  + this.opts.paginationElement + ">";
        for (var i = 1; i< this.totalPage; i++) {
            paginationHTML += "<"+this.opts.paginationElement+"></"+this.opts.paginationElement+">";
        }
        this.page.html(paginationHTML);
    };

    Carousel.prototype.bindTouch = function () {
        if(!this.totalPage || this.totalPage <= 1){
            return;
        }
        var dragStatus = 0,
            startTouchX,
            startTouchY,
            startOffset = 0,
            deltaX = 0,
            that = this;

        this.wrapper.on("touchstart", touchStart);
        this.wrapper.on('touchmove', touchMove);
        this.wrapper.on('touchend', touchEnd);

        function touchStart(e){
            startTouchX = e.touches[0].clientX;  // 记录初始移动位置
            startTouchY = e.touches[0].clientY;
            dragStatus = 0;
            deltaX = 0;
            startOffset = (- that.currentPage) * that.pageWidth;
            that.stopPlay();
        }

        function touchMove(e){
            if(!e || !e.touches[0]){
                return false;
            }
            var currentX = e.touches[0].clientX,
                currentY = e.touches[0].clientY,
                deltaY = currentY - startTouchY;
            deltaX = currentX - startTouchX;
            if(dragStatus == 0){
                if(Math.abs(deltaX) + Math.abs(deltaY) < 12){
                    e.preventDefault();
                    return false;
                }
                if(Math.abs(deltaX) > Math.abs(deltaY)){  // 确定是左右滑动，而不是上下滑动
                    dragStatus = 1;
                }else{
                    dragStatus = 2;
                }
            }
            if(dragStatus == 1){
                e.preventDefault();
                that.moveList(startOffset + deltaX);
                return false;   // 事件已经被处理
            }else if(dragStatus == 2){
                return true;
            }
        }
        function touchEnd(){
            if(dragStatus == 1){
                var p = 30;   // 如果移动距离超过了30px,则切换到另外一张图片
                if(deltaX < -p){   // move to next
                    that.currentPage ++;
                }else if(deltaX > p){  // move to pre
                    that.currentPage --;
                }
                that.turnPage(that.currentPage);
            }
            that.autoPlay();
        }
    };

    Carousel.prototype.autoPlay = function () {
        if (this.timeoutId) {
            clearTimeout(this.timeoutId);
        }
        this.timeoutId = setTimeout(this.timeoutCall(this, this.moveToNext), 5000);
    };

    Carousel.prototype.stopPlay = function () {
        clearTimeout(this.timeoutId);
    };

    Carousel.prototype.moveToNext = function() {
        this.currentPage++;
        this.turnPage(this.currentPage);
        this.timeoutId = setTimeout(this.timeoutCall(this, this.moveToNext), 5000);
    };

    Carousel.prototype.turnPage = function(index){
        var offsetX = (- index) * this.pageWidth + this.opts.margin * this.currentPage;
        this.moveList(offsetX, this.timeoutCall(this, this.pageCallback), this.opts.speed);
    };

    // 前后滚动图片
    Carousel.prototype.moveList = function(offsetX, callback, time) {
        time = time || 0;
        this.wrapper.css(this.calculateTranslate(offsetX, time));

        if(this.currentPage == 0){  //  从第一张向前滑动，到最后一张
            this.currentPage = this.totalPage;
            setTimeout(this.timeoutCall(this, this.moveToLast), time);
        }else if(this.currentPage == this.totalPage + 1){ //  从最后一张向前滑动，到第一张
            this.currentPage = 1;
            setTimeout(this.timeoutCall(this, this.moveToFirst), time);
        }
        if(this.opts.speed && callback){
            setTimeout(callback, time/2);
        }
    };

    // 超时调用函数， 使用当前上下文环境
    Carousel.prototype.timeoutCall = function(context, func){
        return function(){
            func.apply(context);
        };
    };

    Carousel.prototype.moveToFirst = function(){                // 瞬间跳掉第一个
        var offsetFirst = -this.pageWidth + this.opts.margin;
        this.wrapper.css(this.calculateTranslate(offsetFirst, 0));
    };

    Carousel.prototype.moveToLast = function(){
        var offsetLast = (this.opts.margin -  this.pageWidth) * this.totalPage;
        this.wrapper.css(this.calculateTranslate(offsetLast, 0));
    };

    Carousel.prototype.pageCallback = function(){
        var pageItems = this.page.find(this.opts.paginationElement);
        for(var i= 0, len=pageItems.length; i<len; i++){
            pageItems[i].className = "";
        }
        pageItems[this.currentPage - 1].className = this.opts.paginationClass;
    };

    Carousel.prototype.resizePage = function(){
        this.pageWidth = $("body").width();
        var that = this;
        $.each(this.list, function(index, item) {
           $(item).css(that.calculateTranslate(that.pageWidth * index));
        });
        this.moveToFirst();
    };
    
    Carousel.prototype.calculateTranslate = function (distance, time) {
        return {
            '-webkit-transform':'translate3d('+ distance+'px, 0, 0)',
            'transform':'translate3d('+ distance +'px, 0, 0)',
            'transition':'all ' + time + 'ms ease'
        };
    };

    return {
        Carousel: Carousel
    };
});