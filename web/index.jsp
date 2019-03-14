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
