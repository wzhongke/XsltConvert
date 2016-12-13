
define (["jquery", "EventUtil"], function($, util){
    var droptarget = document.getElementById("files_list");
    var FileAPI = function() {
           // 不必把文件读取到JavaScript中
        function createObjectURL (blob) {
            if (window.URL) {
                return window.URL.createObjectURL(blob);
            } else if (window.webkitURL) {
                return window.webkitURL.createObjectURL(blob);
            } else {
                return null;
            }
        }

        //util.EventUtil.addHandler(fileList, "change", function (event) {
        //    var info = "",
        //        output = document.getElementById("output"),
        //        progress = document.getElementById("progress"),
        //        files = util.EventUtil.getTarget(event).files,
        //        url = createObjectURL(files[0]);
        //    if (url) {
        //        if (/image/.test(files[0].type)) {
        //            var image = new Image();
        //            image.src = url;
        //
        //            var drawing = document.getElementById("drawing");
        //            if(drawing.getContext) {
        //                var context = drawing.getContext("2d");
        //                context.drawImage(image, 50, 10 /* 绘制图像的宽和高*/, 20, 30 /*目标的宽和高 */ )
        //                output.innerHTML = '<img src="' + context.toDataURL() + " />";
        //            }
        //        } else {
        //            output.innerHTML = 'not a image.';
        //        }
        //    }
        //});

        function handleEvent() {
            var info = '',
                output = document.getElementById("output"),
                files, i, len;
            util.EventUtil.preventDefault(event);
            if (event.type == "drop") {
                files = event.dataTransfer.files;
                i = 0;
                len = files.length;
                while (i<len) {
                    var url = createObjectURL(files[i]);
                    console.log(url);
                    var image = new Image();
                    image.src = url;

                    image.onload = function() {
                        var canvas = document.createElement("canvas");
                        ctx = canvas.getContext("2d");
                        var hight = image.height / 4,
                            width = image.width / 4;
                        canvas.height = hight;
                        canvas.width = width;
                        ctx.drawImage(image, 0, 0, width, hight);
                        var imgurl = canvas.toDataURL("image/png");
                        output.innerHTML = '<img src="' + imgurl + '"/>';
                    };
                    i++;
                }
            }
        }

        util.EventUtil.addHandler(droptarget, "dragenter", handleEvent);
        util.EventUtil.addHandler(droptarget, "dragover", handleEvent);
        util.EventUtil.addHandler(droptarget, "drop", handleEvent);

        function upload() {
            var data=$("img").attr("src");
            var imageData = encodeURIComponent(data);
            var formData = {
                imageName:"name.jpg",
                imageData: imageData
            };

            // dataURL 的格式为 “data:image/png;base64,****”,逗号之前都是一些说明性的文字，我们只需要逗号之后的就行了
            // data=data.split(',')[1];
            // data=window.atob(data);
            // var ia = new Uint8Array(data.length);
            // for (var i = 0; i < data.length; i++) {
            //     ia[i] = data.charCodeAt(i);
            // }

            // canvas.toDataURL 返回的默认格式就是 image/png
            // var blob=new Blob([ia], {type:"image/png"});
            // var fd=new FormData();
            //
            // fd.append('file',blob);
            $.post("/files/uploadImage", formData, function (data, textStatus) {
                alert(data);
                alert(textStatus);
            });
        }

        $("#uploadFile").click(function () {
            upload();
        })
    };

    return {
        FileAPI: FileAPI
    };
});
