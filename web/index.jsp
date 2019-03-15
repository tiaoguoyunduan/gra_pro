<%@ page import="java.io.IOException" %>
<%@ page import="java.util.Scanner" %>
<%@ page import="java.io.File" %><%--
  Created by IntelliJ IDEA.
  User: lordofriver
  Date: 2019/1/8
  Time: 10:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>毕设主页</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
  <script>
    function sendPost(url,message) {
      var xmlhttp;
      if (window.XMLHttpRequest)
      {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
      }
      else
      {// code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
      }
      xmlhttp.open("post",url,true);
      xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
      xmlhttp.send(message);
      xmlhttp.onreadystatechange=function()
      {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            //document.getElementById("div1").innerText=xmlhttp.responseText;
            var compileMessage=JSON.parse(xmlhttp.responseText);
            if(compileMessage.state==0){
                alert(compileMessage.message);
            }else{
                document.getElementById("rank").innerHTML=compileMessage.message;
                alert("对战成功！")
            }
        }
      }
    }
    function button1() {
        var beecode = document.getElementById("beecode");
        var hornetcode = document.getElementById("hornetcode");
        var myname = document.getElementById("myname");
        if(beecode.value==""){
            alert("请输入HoneyBee部分代码！");
            beecode.focus();
        }else if(hornetcode.value==""){
            alert("请输入Hornet部分代码！");
            hornetcode.focus();
        }else if(myname.value==""){
            alert("请输入姓名！");
            myname.focus();
        }else{
            var code1=htmlEscape(beecode.value);
            var code2=htmlEscape(hornetcode.value);
            var username=myname.value;
            sendPost("/myweb/compile","beecode="+code1+"&hornetcode="+code2+"&name="+username);
        }
    }

    function htmlEscape(text){
      return text.replace(/[+ /?%#&=]/g, function(match, pos, originalText){
        switch(match){
          case "+":return "%2B";
          case " ":return "%20";
          case "/":return "%2F";
          case "?":return "%3F";
          case "%":return "%25";
          case "#":return "%23";
          case "&":return "%26";
          case "=":return "%3D";
        }
      });
    }
  </script>
  <style>
    .input1{
      height:90%;
      width:40%;
        font-family: 'Source Code Pro','DejaVu Sans Mono','Ubuntu Mono','Anonymous Pro','Droid Sans Mono',Menlo,Monaco,Consolas,Inconsolata,Courier,monospace,"PingFang SC","Microsoft YaHei",sans-serif;
        background: #282c34;
        font-size: 15px;
        color: #abb2bf;
    }
    .input2{
        height:90%;
        width:40%;
        font-family: 'Source Code Pro','DejaVu Sans Mono','Ubuntu Mono','Anonymous Pro','Droid Sans Mono',Menlo,Monaco,Consolas,Inconsolata,Courier,monospace,"PingFang SC","Microsoft YaHei",sans-serif;
        background: #282c34;
        font-size: 15px;
        color: #abb2bf;
    }
      .rank{
          position: absolute;
          left: 80%;
          top: 0;
          border-style: groove;
      }
  </style>
</head>

<body>
<div id="div1"></div>
<div>
    <script type="text/javascript">

        ! function() {
            //封装方法，压缩之后减少文件大小
            function get_attribute(node, attr, default_value) {
                return node.getAttribute(attr) || default_value;
            }
            //封装方法，压缩之后减少文件大小
            function get_by_tagname(name) {
                return document.getElementsByTagName(name);
            }
            //获取配置参数
            function get_config_option() {
                var scripts = get_by_tagname("script"),
                    script_len = scripts.length,
                    script = scripts[script_len - 1]; //当前加载的script
                return {
                    l: script_len, //长度，用于生成id用
                    z: get_attribute(script, "zIndex", -1), //z-index
                    o: get_attribute(script, "opacity", 0.5), //opacity
                    c: get_attribute(script, "color", "197,86,221"), //color
                    n: get_attribute(script, "count", 99) //count
                };
            }
            //设置canvas的高宽
            function set_canvas_size() {
                canvas_width = the_canvas.width = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth,
                    canvas_height = the_canvas.height = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
            }

            //绘制过程
            function draw_canvas() {
                context.clearRect(0, 0, canvas_width, canvas_height);
                //随机的线条和当前位置联合数组
                var e, i, d, x_dist, y_dist, dist; //临时节点
                //遍历处理每一个点
                random_points.forEach(function(r, idx) {
                    r.x += r.xa,
                        r.y += r.ya, //移动
                        r.xa *= r.x > canvas_width || r.x < 0 ? -1 : 1,
                        r.ya *= r.y > canvas_height || r.y < 0 ? -1 : 1, //碰到边界，反向反弹
                        context.fillRect(r.x - 0.5, r.y - 0.5, 1, 1); //绘制一个宽高为1的点
                    //从下一个点开始
                    for (i = idx + 1; i < all_array.length; i++) {
                        e = all_array[i];
                        // 当前点存在
                        if (null !== e.x && null !== e.y) {
                            x_dist = r.x - e.x; //x轴距离 l
                            y_dist = r.y - e.y; //y轴距离 n
                            dist = x_dist * x_dist + y_dist * y_dist; //总距离, m

                            dist < e.max && (e === current_point && dist >= e.max / 2 && (r.x -= 0.03 * x_dist, r.y -= 0.03 * y_dist), //靠近的时候加速
                                d = (e.max - dist) / e.max,
                                context.beginPath(),
                                context.lineWidth = d / 2,
                                context.strokeStyle = "rgba(" + config.c + "," + (d + 0.2) + ")",
                                context.moveTo(r.x, r.y),
                                context.lineTo(e.x, e.y),
                                context.stroke());
                        }
                    }
                }), frame_func(draw_canvas);
            }
            //创建画布，并添加到body中
            var the_canvas = document.createElement("canvas"), //画布
                config = get_config_option(), //配置
                canvas_id = "c_n" + config.l, //canvas id
                context = the_canvas.getContext("2d"), canvas_width, canvas_height,
                frame_func = window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame || function(func) {
                    window.setTimeout(func, 1000 / 45);
                }, random = Math.random,
                current_point = {
                    x: null, //当前鼠标x
                    y: null, //当前鼠标y
                    max: 20000 // 圈半径的平方
                },
                all_array;
            the_canvas.id = canvas_id;
            the_canvas.style.cssText = "position:fixed;top:0;left:0;z-index:" + config.z + ";opacity:" + config.o;
            get_by_tagname("body")[0].appendChild(the_canvas);

            //初始化画布大小
            set_canvas_size();
            window.onresize = set_canvas_size;
            //当时鼠标位置存储，离开的时候，释放当前位置信息
            window.onmousemove = function(e) {
                e = e || window.event;
                current_point.x = e.clientX;
                current_point.y = e.clientY;
            }, window.onmouseout = function() {
                current_point.x = null;
                current_point.y = null;
            };
            //随机生成config.n条线位置信息
            for (var random_points = [], i = 0; config.n > i; i++) {
                var x = random() * canvas_width, //随机位置
                    y = random() * canvas_height,
                    xa = 2 * random() - 1, //随机运动方向
                    ya = 2 * random() - 1;
                // 随机点
                random_points.push({
                    x: x,
                    y: y,
                    xa: xa,
                    ya: ya,
                    max: 6000 //沾附距离
                });
            }
            all_array = random_points.concat([current_point]);
            //0.1秒后绘制
            setTimeout(function() {
                draw_canvas();
            },1000);
        }();
    </script>
    <canvas width="966" height="691" id="c_n5" style="left: 0px; top: 0px; position: fixed; z-index: -1; opacity: 0.5;"></canvas>
    <textarea id="beecode" class="input1" spellcheck="false" placeholder="HoneyBee code."></textarea>
    <textarea id="hornetcode" class="input2" spellcheck="false" placeholder="Hornet code."></textarea><br>
    <input type="text" id="myname" placeholder="name">
    <input type="button" value="提交" onclick="button1()">
    <table class="rank" id="rank" border="1">
        <%
            try{
                Scanner input = new Scanner(new File("F:\\gra_pro\\project\\test\\rank.dat"));
                int playerNum=Integer.parseInt(input.next());
                //out.print(playerNum);
                String[] name=new String[playerNum];
                int[] score=new int[playerNum];
                String n,message="";
                int s;
                for(int i=0;i<playerNum;i++){
                    name[i]=input.next();
                }
                for(int i=0;i<playerNum;i++){
                    score[i]=Integer.parseInt(input.next());
                }
                input.close();
                for(int i=0;i<playerNum-1;i++){
                    for(int j=0;j<playerNum-1-i;j++){
                        if(score[j]<score[j+1]){
                            s=score[j];
                            n=name[j];
                            score[j]=score[j+1];
                            name[j]=name[j+1];
                            score[j+1]=s;
                            name[j+1]=n;
                        }
                    }
                }
                for(int i=0;i<playerNum;i++){
                    message=message+"<tr><th>"+name[i]+"</th><th>"+score[i]+"</th></tr>";
                }
                out.print(message);
            }catch (IOException e){
                e.printStackTrace();
            }
        %>
    </table>
</div>
</body>

</html>
