import javax.tools.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class LoaderTest{
    private DynamicLoader dynamicLoader;
    private Object message=null;
    public LoaderTest(String[] code,String[] name, String[] path) {
        if(code == null && name == null){
            dynamicLoader = new DynamicLoader(null, null, pathToList(path));
        }else if (path == null) {
            dynamicLoader = new DynamicLoader(null, null, null);
        }else {
            dynamicLoader = new DynamicLoader(code, name, pathToList(path));
        }
    }

    public boolean testInvoke(String mainClass, String mainMethod, Object input, Class<?> inputType) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NullPointerException {
        dynamicLoader.compile();        //动态编译
        if(dynamicLoader.isFinished()){                              //如果编译成功
            Map<String, byte[]> bytecode=dynamicLoader.getClassBytes();  //获取字节码
            DynamicLoader.MemoryClassLoader classLoader = new DynamicLoader.MemoryClassLoader(bytecode);
            Class clazz = classLoader.loadClass(mainClass);      //动态加载类 BeeFarming
            Object object = clazz.newInstance();                        //实例化
            Method method = clazz.getMethod(mainMethod, inputType);          //执行对战方法 letsrun
            message = method.invoke(object, input);
            return true;
        }else {
            return false;
        }
    }
    private List<JavaFileObject> pathToList(String[] path) {
        List<JavaFileObject> compilationUnits= new ArrayList<JavaFileObject>();
        List<String> paths = Arrays.asList(path);
        Iterator<? extends JavaFileObject> compilationUnit;

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(
                diagnostics, null, null);
        compilationUnit = fileManager.getJavaFileObjectsFromStrings(paths).iterator();  //固定java文件集合

        while (compilationUnit.hasNext()) compilationUnits.add(compilationUnit.next());
        return compilationUnits;
    }
    public Object getMessage(){
        return message;
    }
    public String getException(){
        return dynamicLoader.getException();
    }
}