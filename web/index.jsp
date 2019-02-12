<%--
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
    function button1() {
        //document.getElementById("div1").innerText="aaaaaaaaa";
      var xmlhttp;
      if (window.XMLHttpRequest)
      {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
      }
      else
      {// code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
      }
        //document.getElementById("div1").innerText="111111";
      var code1=htmlEscape(code.value);
      xmlhttp.open("post","/myweb/compile",true);
      xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
      xmlhttp.send("code="+code1);
        //document.getElementById("div1").innerText=code.value;
      xmlhttp.onreadystatechange=function()
      {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            //document.getElementById("div1").innerText="33333";
          confirm(xmlhttp.responseText);
        }
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
      width:55%;
        font-family: 'Source Code Pro','DejaVu Sans Mono','Ubuntu Mono','Anonymous Pro','Droid Sans Mono',Menlo,Monaco,Consolas,Inconsolata,Courier,monospace,"PingFang SC","Microsoft YaHei",sans-serif;
        background: #282c34;
        font-size: 15px;
        color: #abb2bf;
    }
      .rank{
          position: absolute;
          left: 80%;
          top: 0;
          height:90%;
          width:15%;
          border-style: groove;
      }
  </style>
</head>

<body>
<div id="div1"></div>
<div>
    <textarea id="code" class="input1" spellcheck="false"></textarea><br>
    <input type="button" value="提交" onclick="button1()">
    <a href="${pageContext.request.contextPath}/compile">aaaa</a>
    <form class="rank" id="rank">
        <input type="radio" name="student" value="张三">张三
        <br>
        <input type="radio" name="student" value="李四">李四
        <br>
        <input type="radio" name="student" value="王五">王五
        <br>
        <input type="radio" name="student" value="赵六">赵六
    </form>
</div>
</body>

</html>
