import java.util.*;
import java.io.*;
/*
1.读取一个java文件，存到一个String类中
2.调用方法查找字符import，获得其后的包名字，到;停止
3.对包名称与规定的包进行对比，输出结果
*/

public class ImportTest {
    //1.
    //传参问题未解决（文件路径、空格）
    public static String readToString(String filePath) {
        String encoding = "UTF-8";
        File file = new File(filePath);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }
    //2.
    public static List getPackage(String fileString){
        String[] temp = fileString.split(" |\t|\r|\n|\r\n");
        //这里的斜杠会不会产生系统兼容问题？
        //split内为正则表达式，分割文件要考虑空格、换行、制表符等
        //关于注释中如果也有Import....（未解决）
        String s = "import";
        int j = 0;
        List result1 = new ArrayList(); //默认容量为10
        System.out.println(temp.length);
        for(int i=0; i < temp.length; i++){
            if(temp[i].equals(s)){
                result1.add(temp[i+1]);
                //将import后面的字符装到result数组中
                System.out.println("发现一个包："+ j++);
                System.out.println(temp[i+1]);
                //输出获取的包
            }
        }
        return result1;
    }
    //3.
    public static boolean checkPackage(String code){
        List result1=getPackage(code);
        String[] s={"java.util.Random;","java.util.regex.Matcher;","java.util.regex.Pattern;"};
        boolean flag = true;
        for(int i = 0; i < result1.size(); i++){
            boolean flag0=false;
            for(int j = 0; j < 3; j++){
                if(result1.get(i).equals(s[j])){
                    flag0=true;
                }
            }
            if(flag0){
                continue;
            } else {
                return flag0;
            }
        }
        return flag;
    }
    /*
    public static void main(String[] args){
        //通过args传参测试
        String get = readToString(args[0]);
        System.out.println(get);
        System.out.println("get输出结束");
        System.out.println(checkPackage(getPackage(get)));
    }
    */
}