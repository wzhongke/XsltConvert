<?xml version="1.0" encoding="GBK"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="classid"  select="/DOCUMENT/item/classid"/>
<xsl:output method="html" version="1.0" encoding="GBK" omit-xml-declaration="yes" standalone="yes"/>

<xsl:template match="/">
    <div class="vrResult">
        <xsl:attribute name="id">sogou_vr_<xsl:value-of select="/DOCUMENT/item/classid"/>_${i}</xsl:attribute>
        <xsl:call-template name="main" />
    </div>
</xsl:template>

<xsl:template name="main">
    <xsl:variable name="tmp_slide"  select="'tmp_slide'" />
    <xsl:variable name="scrId"  select="'slide'" />
    <h3 class="vr-tit">
        <a href="" class="resultLink">
            <xsl:call-template name="getURL">
                <xsl:with-param name="url"  select="//display/url" />
                <xsl:with-param name="wml" select="1"/>
                <xsl:with-param name="linkid" select="0"/>
            </xsl:call-template>
            <xsl:call-template name="atr_id">
                <xsl:with-param name="id"  select="$tmp_slide" />
            </xsl:call-template>
            <xsl:value-of select="//display/title" />
        </a>
    </h3>
    <div class="vr-video160815">
        <div class="scroll-layout scroll-layout-video">
            <xsl:variable name="container_slide"  select="'container'" />
            <xsl:call-template name="atr_id">
                <xsl:with-param name="id"  select="$container_slide" />
            </xsl:call-template>
            <div class="layout-width" style="width: 9999px;">
                <xsl:if test="//display/itemlink and count(//display/itemlink) &gt; 0">
                    <div class="item-list" style="width:370px">
                        <xsl:for-each select="//display/itemlink[position() &lt; 4]">
                            <a href="" class="item-text">
                                <xsl:call-template name="getURL">
                                    <xsl:with-param name="url"  select="item_url" />
                                    <xsl:with-param name="wml" select="1"/>
                                    <xsl:with-param name="linkid" select="2 + position() "/>
                                </xsl:call-template>
                                <span class="img-tag">
                                    <img onerror="this.src='/resource/vr/5/images/account/wap_dft.png';">
                                        <xsl:attribute name="src"><xsl:value-of select="sogou.yuntu(item_img, 124, 93, itemlink, 'true')" /></xsl:attribute>
                                        <xsl:attribute name="name">
                                            <xsl:call-template name="ID">
                                                <xsl:with-param name="value"  select="'img'"></xsl:with-param>
                                            </xsl:call-template>
                                        </xsl:attribute>
                                    </img>
                                    <i class="video-play"></i>
                                </span>
                                <xsl:if test="item_title and item_title !=''"><h4 class="item-title"><xsl:value-of select="item_title" /></h4></xsl:if>
                                <xsl:if test="item_duration and item_duration !=''"><h4 class="item-time">时长<xsl:value-of select="item_duration" /></h4></xsl:if>
                            </a>
                        </xsl:for-each>
                    </div>
                    <xsl:if test="//display/itemlink and count(//display/itemlink) &gt; 5">
                        <div class="item-list" style="width:370px">
                            <xsl:for-each select="//display/itemlink[position() &gt; 3 and position() &lt; 7]">
                                <a href="" class="item-text">
                                    <xsl:call-template name="getURL">
                                        <xsl:with-param name="url"  select="item_url" />
                                        <xsl:with-param name="wml" select="1"/>
                                        <xsl:with-param name="linkid" select="2 + position() "/>
                                    </xsl:call-template>
                                    <span class="img-tag">
                                        <img onerror="this.src='/resource/vr/5/images/account/wap_dft.png';">
                                            <xsl:attribute name="src"><xsl:value-of select="sogou.yuntu(item_img, 125, 93, itemlink, 'true')" /></xsl:attribute>
                                            <xsl:attribute name="name">
                                                <xsl:call-template name="ID">
                                                    <xsl:with-param name="value"  select="'img'"></xsl:with-param>
                                                </xsl:call-template>
                                            </xsl:attribute>
                                        </img>
                                        <i class="video-play"></i>
                                    </span>
                                    <h4 class="item-title"><xsl:value-of select="item_title" /></h4>
                                    <h4 class="item-time">时长<xsl:value-of select="item_duration" /></h4>
                                </a>
                            </xsl:for-each>
                        </div>
                    </xsl:if>
                    <xsl:if test="//display/itemlink and count(//display/itemlink) &gt; 8">
                        <div class="item-list" style="width:370px">
                            <xsl:for-each select="//display/itemlink[position() &gt; 6 and position() &lt; 10]">
                                <a href="" class="item-text">
                                    <xsl:call-template name="getURL">
                                        <xsl:with-param name="url"  select="item_url" />
                                        <xsl:with-param name="wml" select="1"/>
                                        <xsl:with-param name="linkid" select="2 + position() "/>
                                    </xsl:call-template>
                                    <span class="img-tag">
                                        <img onerror="this.src='/resource/vr/5/images/account/wap_dft.png';">
                                            <xsl:attribute name="src"><xsl:value-of select="sogou.yuntu(item_img, 125, 93, itemlink, 'true')" /></xsl:attribute>
                                            <xsl:attribute name="name">
                                                <xsl:call-template name="ID">
                                                    <xsl:with-param name="value"  select="'img'"></xsl:with-param>
                                                </xsl:call-template>
                                            </xsl:attribute>
                                        </img>
                                        <i class="video-play"></i>
                                    </span>
                                    <h4 class="item-title"><xsl:value-of select="item_title" /></h4>
                                    <h4 class="item-time">时长<xsl:value-of select="item_duration" /></h4>
                                </a>
                            </xsl:for-each>
                        </div>
                    </xsl:if>

                    <xsl:if test="//display/itemlink and count(//display/itemlink) &gt; 11">
                        <div class="item-list" style="width:370px">
                            <xsl:for-each select="//display/itemlink[position() &gt; 9 and position() &lt; 13]">
                                <a href="" class="item-text">
                                    <xsl:call-template name="getURL">
                                        <xsl:with-param name="url"  select="item_url" />
                                        <xsl:with-param name="wml" select="1"/>
                                        <xsl:with-param name="linkid" select="2 + position() "/>
                                    </xsl:call-template>
                                    <span class="img-tag">
                                        <img onerror="this.src='/resource/vr/5/images/account/wap_dft.png';">
                                            <xsl:attribute name="src"><xsl:value-of select="sogou.yuntu(item_img, 125, 93, itemlink, 'true')" /></xsl:attribute>
                                            <xsl:attribute name="name">
                                                <xsl:call-template name="ID">
                                                    <xsl:with-param name="value"  select="'img'"></xsl:with-param>
                                                </xsl:call-template>
                                            </xsl:attribute>
                                        </img>
                                        <i class="video-play"></i>
                                    </span>
                                    <h4 class="item-title"><xsl:value-of select="item_title" /></h4>
                                    <h4 class="item-time">时长<xsl:value-of select="item_duration" /></h4>
                                </a>
                            </xsl:for-each>
                        </div>
                    </xsl:if>

                </xsl:if>
            </div>
        </div>
        <div class="box-toggle toggle-center">
            <xsl:variable name="box_slide"  select="'box_slide'" />
            <xsl:call-template name="atr_id">
                <xsl:with-param name="id"  select="$box_slide" />
            </xsl:call-template>
        </div>
        <div class="citeurl">
            <xsl:if test="/DOCUMENT/item/display/showname and /DOCUMENT/item/display/showname !=''  and boolean(/DOCUMENT/item/display/showname/@length &lt;=(35-4))">
                <strong><xsl:value-of select="/DOCUMENT/item/display/showname" />&#160;-&#160;</strong>
            </xsl:if>
            <xsl:value-of select="//display/showurl" />
        </div>
    </div>
    <script>
        <![CDATA[
        (function(f,d){function e(i,j){this.opts={margin:19,wraperClass:".layout-width",listClass:".item-list",autoplay:false,autoplayspeed:5000,speed:300,pagination:"",paginationElement:"i",paginationClass:"active",loop:false};this.container=i;this.init(j)}e.fn=e.prototype={constructor:e,initParam:function(i){for(var j in this.opts){if(j in i){this.opts[j]=i[j]}}},initPage:function(){this.page=f(this.opts.pagination);if(this.page.length>0){var j="<"+this.opts.paginationElement+" class="+this.opts.paginationClass+"></"+this.opts.paginationElement+">";for(var k=1;k<this.totalPage;k++){j+="<"+this.opts.paginationElement+"></"+this.opts.paginationElement+">"}this.page.html(j)}},intiLoop:function(){if(this.opts.loop){var i=this.list.last().clone();i.addClass("swiper-add-list");var j=this.list.first().clone();j.addClass("swiper-add-first");this.wraper.prepend(i).append(j);this.list=this.container.find(this.opts.listClass);this.moveToPage(this.currentPage)}this.resizePage();if(this.opts.autoplay){this.loop()}},loop:function(){this.timeid=0;var j=this;function i(){j.turnPage(++j.currentPage);j.timeid=setTimeout(i,j.opts.autoplayspeed)}this.timeid=setTimeout(i,this.opts.autoplayspeed)},bindTouch:function(){if(!this.totalPage||this.totalPage<=1){return}var m=0,j,i,o=0,l=0,n=this;this.wraper.on("touchstart",k,false);this.wraper.on("touchmove",p,false);this.wraper.on("touchend",q,false);function k(r){j=r.touches[0].clientX;i=r.touches[0].clientY;m=0;l=0;o=(-n.currentPage)*n.pageWidth;clearTimeout(n.timeid)}function p(u){if(!u||!u.touches[0]){return false}var t=u.touches[0].clientX,s=u.touches[0].clientY,r=s-i;l=t-j;if(m==0){if(Math.abs(l)+Math.abs(r)<12){u.preventDefault();return false}if(Math.abs(l)>Math.abs(r)){m=1}else{m=2}}if(m==1){u.preventDefault();n.moveList(o+l);return true}else{if(m==2){return false}}}function q(){if(m==1){var r=30;if(l<-r){n.currentPage++}else{if(l>r){n.currentPage--}}n.turnPage(n.currentPage);n.loop()}}},turnPage:function(j){var i=(-j)*this.pageWidth+this.opts.margin*this.currentPage;this.moveList(i,this.timeoutCall(this,"pageCallback"),this.opts.speed)},moveList:function(j,l,k){k=k||0;var i=j+"px, 0, 0";this.wraper.css({"-webkit-transform":"translate3d("+i+")",transform:"translate3d("+i+")","transition-duration":k+"ms"});if(this.currentPage==0){this.currentPage=this.totalPage;setTimeout(this.timeoutCall(this,"moveToPage",{page:this.currentPage}),k)}else{if(this.currentPage==this.totalPage+1){this.currentPage=1;setTimeout(this.timeoutCall(this,"moveToPage"),k)}}if(this.page.length>0&&l){setTimeout(l,k/2)}},timeoutCall:function(k,j,i){return function(){k[j].call(k,i)}},moveToPage:function(k){k=k&&k.page||1;var j=(this.opts.margin-this.pageWidth)*k;var i=j+"px, 0, 0";this.wraper.css({"-webkit-transform":"translate3d("+i+")",transform:"translate3d("+i+")","transition-duration":"0ms"})},resizePage:function(){this.pageWidth=f(".results").width();this.list.width(this.container.width())},pageCallback:function(){var l=this.page.find(this.opts.paginationElement);for(var k=0,j=l.length;k<j;k++){l[k].className=""}l[this.currentPage-1].className=this.opts.paginationClass}};e.fn.init=function(j){this.initParam(j);this.list=this.container.find(this.opts.listClass);this.page=f(this.opts.pagination);if(this.list.length<=1){this.resizePage();this.page.hide();return}this.wraper=f(this.container.find(this.opts.wraperClass)[0]);if(j.loop){this.currentPage=1}else{this.currentPage=0}this.totalPage=this.list.length;this.intiLoop();this.initPage();this.bindTouch();var i=this;d.addEventListener("resize",function(){i.resizePage.call(i,arguments);i.moveToPage(i.currentPage)},false)};var c=d.sogou=d.sogou||{},g=c.struct=c.struct||{},a=g.struct514=g.struct514||{};var b,h;a.init=function(i,j){if(typeof i!="undefined"){b=i}if(typeof j!="undefined"){h=j}if(typeof f=="undefined"){return}function k(l){return"#sogou_vr_"+b+(l?"_"+l:"")+"_"+h}new e(f(k("container")),{pagination:k("box_slide"),speed:800,autoplay:true,autoplayspeed:5000,loop:true})}})(Zepto,window);

]]>
    <xsl:if test="count(//display/itemlink) &gt; 1">window.sogou.struct.struct514.init('<xsl:value-of select="/DOCUMENT/item/classid"/>','${i}');</xsl:if>
    </script>
</xsl:template>

<xsl:template name="atr_id">
    <xsl:param name="id" />
    <xsl:attribute name="id">
        <xsl:call-template name="ID"  >
            <xsl:with-param name="value">
                <xsl:value-of  select="$id" />
            </xsl:with-param>
        </xsl:call-template>
    </xsl:attribute>
</xsl:template>

<xsl:template name="getURL">
    <xsl:param name="url"/>
    <xsl:param name="wml" select="1"/>
    <xsl:param name="linkid" select="0"/>
    <xsl:param name="w"/>
    <xsl:param name="e"/>
    <xsl:attribute name="href">
        <xsl:value-of select="'${t}clk=${i}&amp;url='"/>
        <xsl:value-of select="URLEncoder.encode($url,'UTF-8')"/>
        <xsl:value-of select="'&amp;vrid='"/>
        <xsl:value-of select="/DOCUMENT/item/classid"/>
        <xsl:value-of select="'&amp;wml='"/>
        <xsl:value-of select="$wml"/>
        <xsl:value-of select="'&amp;linkid='"/>
        <xsl:value-of select="$linkid"/>
        <xsl:if test="$w"><xsl:value-of select="'&amp;w='"/><xsl:value-of select="$w"/></xsl:if>
        <xsl:if test="$e"><xsl:value-of select="'&amp;e='"/><xsl:value-of select="$e"/></xsl:if>
    </xsl:attribute>
</xsl:template>

<xsl:template name="ID"><xsl:param name="value"/>sogou_vr_<xsl:value-of select="/DOCUMENT/item/classid"/>_<xsl:value-of select="$value"/>_${i}</xsl:template>
</xsl:stylesheet>