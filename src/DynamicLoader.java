import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.CharBuffer;
import java.util.*;

public class DynamicLoader {
    /**
    * auto fill in the java-name with code, return null if cannot find the public class
    * @param javaSrc source code string
    * @return return the Map, the KEY means ClassName, the VALUE means bytecode.
    */
    private MemoryJavaFileManager manager;
    private boolean flag;
    private StringWriter exceptionMessage=new StringWriter();

    /**
    * @param bee the name of your public class,eg: <code>TestClass.java</code>
    * @param hornet source code string
    * @return return the Map, the KEY means ClassName, the VALUE means bytecode.
    */
    public void compile(String bee, String hornet, List<JavaFileObject> compilationUnits) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdManager = compiler.getStandardFileManager(null, null, null);
        manager = new MemoryJavaFileManager(stdManager);
        try {
            JavaFileObject javaFileObject1 = manager.makeStringSource("HoneyBee.java", bee);
            compilationUnits.add(javaFileObject1);
            javaFileObject1 = manager.makeStringSource("Hornet.java", hornet);
            compilationUnits.add(javaFileObject1);
            JavaCompiler.CompilationTask task = compiler.getTask(exceptionMessage, manager, null, null, null, compilationUnits);
            flag=task.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isFinished(){
        return flag;
    }
    public Map<String, byte[]> getClassBytes(){
        return manager.getClassBytes();
    }
    public String getException(){
        return exceptionMessage.toString();
    }

    public static class MemoryClassLoader extends URLClassLoader {
        Map<String, byte[]> classBytes = new HashMap<String, byte[]>();
        public MemoryClassLoader(Map<String, byte[]> classBytes) throws NullPointerException {
            super(new URL[0], MemoryClassLoader.class.getClassLoader());
            this.classBytes.putAll(classBytes);
            //System.out.println(Thread.currentThread().getName()+Thread.currentThread().getId());
        }
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] buf = classBytes.get(name);
        if (buf == null) {
        return super.findClass(name);
        }
        classBytes.remove(name);
        return defineClass(name, buf, 0, buf.length);
        }
    }
}

    /** 
    * MemoryJavaFileManager.java
    * @author A. Sundararajan
    */
    /**
    * JavaFileManager that keeps compiled .class bytes in memory.
    */
    @SuppressWarnings("unchecked")
final class MemoryJavaFileManager extends ForwardingJavaFileManager {
    /**
    * Java source file extension.
    */
    private final static String EXT = ".java";
    private Map<String, byte[]> classBytes;
    public MemoryJavaFileManager(JavaFileManager fileManager) {
    super(fileManager);
    classBytes = new HashMap<String, byte[]>();
    }
    public Map<String, byte[]> getClassBytes() {
    return classBytes;
    }
    public void close() throws IOException {
    classBytes = new HashMap<String, byte[]>();
    }
    public void flush() throws IOException {
    }
    /**
    * A file object used to represent Java source coming from a string.
    */
    private static class StringInputBuffer extends SimpleJavaFileObject {
        final String code;
        StringInputBuffer(String name, String code) {
        super(toURI(name), Kind.SOURCE);
        this.code = code;
        }
        public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
        return CharBuffer.wrap(code);
        }
        public Reader openReader() {
        return new StringReader(code);
        }
    }
    /**
    * A file object that stores Java bytecode into the classBytes map.
    */
    private class ClassOutputBuffer extends SimpleJavaFileObject {
        private String name;
        ClassOutputBuffer(String name) {
        super(toURI(name), Kind.CLASS);
        this.name = name;
        }
        public OutputStream openOutputStream() {
            return new FilterOutputStream(new ByteArrayOutputStream()) {
            public void close() throws IOException {
            out.close();
            ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
            classBytes.put(name, bos.toByteArray());
            }
            };
        }
    }
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location,
    String className,
    JavaFileObject.Kind kind,
    FileObject sibling) throws IOException {
        if (kind == JavaFileObject.Kind.CLASS) {
            return new ClassOutputBuffer(className);
        } else {
            return super.getJavaFileForOutput(location, className, kind, sibling);
        }
    }
    static JavaFileObject makeStringSource(String name, String code) {
    return new StringInputBuffer(name, code);
    }
    static URI toURI(String name) {
        File file = new File(name);
        if (file.exists()) {
        return file.toURI();
        } else {
            try {
            final StringBuilder newUri = new StringBuilder();
            newUri.append("mfm:///");
            newUri.append(name.replace('.', '/'));
            if (name.endsWith(EXT)) newUri.replace(newUri.length() - EXT.length(), newUri.length(), EXT);
            return URI.create(newUri.toString());
            } catch (Exception exp) {
            return URI.create("mfm:///com/sun/script/java/java_source");
            }
        }
    }
}
