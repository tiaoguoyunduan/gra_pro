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
        List<String> paths = Arrays.asList(new String[] {"F:\\gra_pro\\bee\\BF201401\\Bee.java","F:\\gra_pro\\bee\\BF201401\\BeeFarming.java","F:\\gra_pro\\bee\\BF201401\\Flower.java","F:\\gra_pro\\bee\\BF201401\\FlyingStatus.java","F:\\gra_pro\\bee\\BF201401\\Hornet.java",});
        Iterator<? extends JavaFileObject> compilationUnits;
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(
                diagnostics, null, null);
        compilationUnits = fileManager.getJavaFileObjectsFromStrings(paths).iterator();
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        System.out.println("compile is running!");
        /*
        class Mydata{
            boolean flag1=false,flag2=false;
            Exception e1;
            Object message;
        }
        Mydata mydata=new Mydata();
        class CompileThread implements Runnable{
            String code;
            Mydata mydata;
            public CompileThread(String code,Mydata mydata){
                this.code=code;
                this.mydata=mydata;
            }
            @Override
            public void run() {
                //int b=2/0;
                try {
                    LoaderTest loaderTest=new LoaderTest(code);
                    mydata.message=loaderTest.testInvoke();
                    //int a=2/0;
                    mydata.flag1=true;
                } catch (Exception e){
                    mydata.e1=e;
                    e.printStackTrace();
                    mydata.flag2=true;
                }
            }
        }
        CompileThread myThread=new CompileThread(request.getParameter("code"),mydata);
        Thread compileThread=new Thread(myThread);
        compileThread.start();
        while (compileThread.isAlive()&&(!compileThread.isInterrupted())){
            try{
                Thread.currentThread().sleep(1000);
            }catch (Exception e){
            }
        }
        System.out.println(mydata.flag1+mydata.message.toString()+"aaaaa");
        if(mydata.flag1){
            response.getWriter().write(mydata.message.toString());
        }else if(mydata.flag2){
            response.getWriter().write(mydata.e1.getMessage());
        }else {
            response.getWriter().write("NullPointerException!请检查语法错误！");
        }
        System.out.println(Thread.currentThread().getName()+"aaa");
        */
        boolean flag=false;
        Object message=null;
        String e1=null;
        try {
            LoaderTest loaderTest=new LoaderTest(request.getParameter("code"),compilationUnits);
            flag=loaderTest.testInvoke();
            message=loaderTest.getMessage();
            e1=loaderTest.getException();
        } catch (Exception e){
            e.printStackTrace();
            //response.getWriter().write(e.getMessage());
        }
        if(flag){
            response.getWriter().write(message.toString());
        }else {
            response.getWriter().write(e1);
        }
        System.out.println("Where am I?");
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }


}

