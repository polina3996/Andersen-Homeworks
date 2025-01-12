package coworking;

public class GreetingClassLoader extends ClassLoader {
    private final String directory;

    public GreetingClassLoader(String directory) {
        this.directory = directory;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass == null) {
            try {
                loadedClass = super.loadClass(name, false);
            }
            catch (ClassNotFoundException e) {
                //loadedClass = load(name);
                loadedClass = findClass(name);
            }
        }
        if (resolve) {
            resolveClass(loadedClass);
        }
        return loadedClass;
    }
//
//    private Class<?> load(String name) throws ClassNotFoundException {
//    try {
//        String filePath = directory + File.separator + name.replace('.', File.separatorChar) + ".class";
//        byte[] classBytes;
//        try (FileInputStream fis = new FileInputStream(filePath)) {
//            classBytes = fis.readAllBytes();
//        }
//        return defineClass(name, classBytes, 0, classBytes.length);
//    }catch (IOException e) {
//        throw new ClassNotFoundException("Unable to load class: " + name + " from path: " + directory, e);
//        }
//    }
}
