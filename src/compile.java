import java.io.*;
import javax.servlet.http.*;
import javax.tools.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class compile extends HttpServlet {

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
                result1=match(nameL[i],beecode,hornetcode);
                result[i]=result1.result;
            }else {
                flag=false;
            }
        }
        System.out.println(flag);

        if(flag){
            rankManager.match(result);
            rankManager.writeRank();
            try{
                PrintWriter out = new PrintWriter(new FileWriter("F:\\gra_pro\\bee\\BF2019\\code\\"+name+"_B.java"));
                out.print(beecode);
                PrintWriter out1 = new PrintWriter(new FileWriter("F:\\gra_pro\\bee\\BF2019\\code\\"+name+"_H.java"));
                out1.print(hornetcode);
                out.close();
                out1.close();
            }catch (Exception e){
                return "{\"state\":\"0\",\"message\":\""+e.toString()+"\"}";
            }
            return "{\"state\":\"1\",\"message\":\""+RankManager.getRank()+"\"}";
        }else {
            return "{\"state\":\"0\",\"message\":\""+result1.e+"\"}";
        }
    }
    private Result match(String name,String beecode,String hornetcode){
        List<String> paths = Arrays.asList(new String[] {"F:\\gra_pro\\bee\\BF2019\\Bee.java","F:\\gra_pro\\bee\\BF2019\\BeeFarming.java","F:\\gra_pro\\bee\\BF2019\\Flower.java","F:\\gra_pro\\bee\\BF2019\\FlyingStatus.java"});
        Iterator<? extends JavaFileObject> compilationUnits;
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(
                diagnostics, null, null);
        compilationUnits = fileManager.getJavaFileObjectsFromStrings(paths).iterator();  //固定java文件集合

        boolean flag=false;
        String message=null;
        String e1=null;
        float result1=0,result2=0;
        int result=0;

        String bee,hornet;
        bee=readcode(name+"_B.java");
        hornet=readcode(name+"_H.java");
        try {  //己方蜜蜂对方黄蜂，对战分数算作己方得分
            LoaderTest loaderTest=new LoaderTest(beecode,hornet,compilationUnits);
            flag=loaderTest.testInvoke();         //是否编译成功
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
                compilationUnits = fileManager.getJavaFileObjectsFromStrings(paths).iterator();
                LoaderTest loaderTest=new LoaderTest(bee ,hornetcode,compilationUnits);
                flag=loaderTest.testInvoke();
                if(flag){
                    result2=(float)loaderTest.getMessage();
                }else {}
                e1=loaderTest.getException();
            } catch (Exception e){
                e.printStackTrace();
            }
            if(flag){
                if(result1>result2){
                    result=1;
                    //message="You win!  "+result1/100+">"+result2/100;
                }else{
                    result=0;
                    //message="You lose!  "+result1/100+"<"+result2/100;
                }
            }else {
            }
        }else {
            //response.getWriter().write(e1);
        }
        return new Result(flag, e1, result);
    }
    private String readcode(String name){
        String str="";
        File file=new File("F:\\gra_pro\\bee\\BF2019\\code\\"+name);
        try {
            FileInputStream in=new FileInputStream(file);
            // size 为字串的长度 ，这里一次性读完
            int size=in.available();
            byte[] buffer=new byte[size];
            in.read(buffer);
            in.close();
            str=new String(buffer,"utf-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return str;
    }

}

