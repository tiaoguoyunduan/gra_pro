import javax.tools.JavaFileObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class LoaderTest{
    private String bee,hornet;
    private DynamicLoader dynamicLoader=new DynamicLoader();
    private Object message=null;
    private List<JavaFileObject> compilationUnits= new ArrayList<JavaFileObject>();
    public LoaderTest(String bee,String hornet, Iterator<? extends JavaFileObject> compilationUnits) {
        this.bee=bee;
        this.hornet=hornet;
        while (compilationUnits.hasNext()) this.compilationUnits.add(compilationUnits.next());
    }

    public boolean testInvoke() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NullPointerException {
        dynamicLoader.compile(bee, hornet, compilationUnits);
        if(dynamicLoader.isFinished()){
            Map<String, byte[]> bytecode=dynamicLoader.getClassBytes();
            DynamicLoader.MemoryClassLoader classLoader = new DynamicLoader.MemoryClassLoader(bytecode);
            Class clazz = classLoader.loadClass("BeeFarming");
            Object object = clazz.newInstance();
            Method method = clazz.getMethod("letsrun");
            message = method.invoke(object);
            //System.out.println(a+"   "+b+"   "+message);
            return true;
        }else {
            return false;
        }
    }
    public Object getMessage(){
        return message;
    }
    public String getException(){
        return dynamicLoader.getException();
    }
}