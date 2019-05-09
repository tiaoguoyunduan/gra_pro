import java.io.*;
import javax.servlet.http.*;


public class compile extends HttpServlet {
    public final static String filePath="F:\\gra_pro\\bee\\BF2019";
    public void init()
    {
        // 执行必需的初始化
        System.out.println("Compile servlet initialized!");
    }
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String beecode=request.getParameter("beecode"),hornetcode=request.getParameter("hornetcode"),name=request.getParameter("name");
        System.out.println("compile is running!"+name);

        if(ImportTest.checkPackage(beecode)&&ImportTest.checkPackage(hornetcode)){       //导入包检测
            response.getWriter().write(matchall(name, beecode, hornetcode));
        }else{
            response.getWriter().write("{\"state\":\"0\",\"message\":\"只能导入指定的java包！\"}");
        }

        System.out.println("Where am I?");
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
    }

    class Result {
        public boolean flag=false;
        public String e=null;
        public int result=0;
        public Result(boolean flag,String e,int result){
            this.flag=flag;
            this.e=e;
            this.result=result;
        }
    }
    private String matchall(String name,String beecode,String hornetcode){
        StringBuffer message=new StringBuffer("                  Your goals.   Opponent's goals.\r\n\r\n");
        RankManager rankManager=new RankManager();
        rankManager.readRank(name);
        String[] nameL=rankManager.getList();                  //对战选手列表
        System.out.println(nameL);
        int matchNum=nameL.length;
        int[] result = new int[matchNum];                      //存储所有对战结果，0负1胜
        boolean flag=true;      //标志位，是否出错
        Result result1=new Result(true, "", 0);  //单次对战结果，是否成功、错误信息、对战结果

        for(int i=0;i<matchNum;i++){                           //依次进行对战，任意一次出错后直接退出且标志位设为false
            if(flag&&result1.flag){
                result1=match(message,nameL[i],beecode,hornetcode);
                result[i]=result1.result;
            }else {
                flag=false;
            }
        }
        System.out.println(flag+"asdasd");
        System.out.println(result1.e);
        if(flag){
            try{
                PrintWriter out = new PrintWriter(new FileWriter(filePath+"\\code\\"+name+"_B.java"));
                out.print(beecode);
                PrintWriter out1 = new PrintWriter(new FileWriter(filePath+"\\code\\"+name+"_H.java"));
                out1.print(hornetcode);
                out.close();
                out1.close();
                rankManager.match(result);
                rankManager.writeRank();
            }catch (Exception e){
                return "{\"state\":\"0\",\"message\":\""+e.toString()+"\"}";
            }
            return "{\"state\":\"1\",\"rank\":\""+RankManager.getRank()+"\",\"message\":\""+message.toString()+"\"}";
        }else {
            return "{\"state\":\"0\",\"message\":\""+result1.e+"\"}";
        }
    }
    private Result match(StringBuffer message,String name,String beecode,String hornetcode){

        boolean flag=false;
        String e1=null;
        float result1=0,result2=0;
        float[] goals1=new float[4],goals2=new float[4];
        int result=0;

        String[] className = new String[] {"HoneyBee.java", "Hornet.java"};
        String[] code1 = new String[2] , code2 = new String[2];
        code1[0] = beecode; code1[1] = readcode(name+"_H.java");
        code2[0] = readcode(name+"_B.java"); code2[1] = hornetcode;

        try {  //己方蜜蜂对方黄蜂，对战分数算作己方得分
            LoaderTest loaderTest=new LoaderTest(code1, className,
                    new String[] {filePath+"\\Bee.java",filePath+"\\BeeFarming.java",filePath+"\\Flower.java",filePath+"\\FlyingStatus.java"});
            flag=loaderTest.testInvoke("BeeFarming", "letsrun", goals1, float[].class);         //是否编译成功
            if(flag){
                result1=(float)loaderTest.getMessage();
            }else {}
            e1=loaderTest.getException();
        } catch (Exception e){
            e.printStackTrace();
        }
        if(flag){
            try {
                System.out.println("sdafasd");
                LoaderTest loaderTest=new LoaderTest(code2, className,
                        new String[] {filePath+"\\Bee.java",filePath+"\\BeeFarming.java",filePath+"\\Flower.java",filePath+"\\FlyingStatus.java"});
                flag=loaderTest.testInvoke("BeeFarming", "letsrun", goals2, float[].class);
                if(flag){
                    result2=(float)loaderTest.getMessage();
                }else {}
                e1=loaderTest.getException();
            } catch (Exception e){
                e.printStackTrace();
            }
            if(flag){
                if(result1>result2){
                    message.append("compete with "+name+": You win!\r\n");
                    result=1;
                }else{
                    message.append("compete with "+name+": You lose!\r\n");
                    result=0;
                }
                message.append("totalHoney:       ").append(goals1[0]).append("        ").append(goals2[0]).append("\r\n");
                message.append("still alive Bees: ").append(goals1[1]).append("          ").append(goals2[1]).append("\r\n");
                message.append("The time left:    ").append(goals1[2]).append("          ").append(goals2[2]).append("\r\n");
                message.append("Final Goals:      ").append(goals1[3]).append("        ").append(goals2[3]).append("\r\n\r\n");
            }else {
            }
        }else {
        }
        return new Result(flag, e1, result);
    }
    private String readcode(String name){
        String str="";
        File file=new File(filePath+"\\code\\"+name);
        try {
            FileInputStream in=new FileInputStream(file);
            // size 为字串的长度 ，这里一次性读完
            int size=in.available();
            byte[] buffer=new byte[size];
            in.read(buffer);
            in.close();
            str=new String(buffer,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return str;
    }

}

