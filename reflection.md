# Java 反射（Reflection）

## 什么是反射？

Java 反射（Reflection）是一种强大的机制，允许程序在运行时动态地获取类的信息，并操作类的成员（方法、属性、构造方法等）。反射是 Java 动态性的重要表现，常用于框架开发、工具类实现和测试。

---

## 反射的常见用途
1. **获取类的信息**：包括类名、修饰符、父类和接口等。
2. **操作方法**：动态调用类的方法。
3. **操作字段（属性）**：获取或修改类的字段值。
4. **操作构造方法**：实例化类对象。
5. **动态代理**：拦截方法调用。

---

## 1. 获取类的 `Class` 对象
反射的入口是 `Class` 对象，可以通过以下三种方式获取：

```java
// 1. 使用 Class.forName()
Class<?> clazz1 = Class.forName("java.util.ArrayList");

// 2. 使用 类名.class
Class<?> clazz2 = ArrayList.class;

// 3. 使用对象的 getClass() 方法
ArrayList<String> list = new ArrayList<>();
Class<?> clazz3 = list.getClass();

System.out.println(clazz1 == clazz2); // true
System.out.println(clazz2 == clazz3); // true
```

---

## 2. 获取类的信息
可以通过反射获取类的基本信息，例如类名、修饰符、父类和实现的接口。

```java
import java.lang.reflect.*;

public class ReflectionInfo {
    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> clazz = Class.forName("java.util.ArrayList");

        // 获取类名
        System.out.println("类名: " + clazz.getName());

        // 获取类的修饰符
        int modifiers = clazz.getModifiers();
        System.out.println("是否为 public: " + Modifier.isPublic(modifiers));

        // 获取父类
        System.out.println("父类: " + clazz.getSuperclass().getName());

        // 获取实现的接口
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> i : interfaces) {
            System.out.println("实现的接口: " + i.getName());
        }
    }
}
```

---

## 3. 获取和调用方法
反射可以列出类中的所有方法，并在运行时调用它们。

### 获取方法信息

```java
import java.lang.reflect.Method;

public class MethodReflection {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("java.util.ArrayList");

        // 获取所有方法
        Method[] methods = clazz.getDeclaredMethods();  // 获取所有声明的方法
        for (Method method : methods) {
            System.out.println("方法名: " + method.getName());
            System.out.println("返回类型: " + method.getReturnType().getName());
            System.out.println("参数个数: " + method.getParameterCount());
            for (Class<?> paramType : method.getParameterTypes()) {
                System.out.println("参数类型: " + paramType.getName());
            }
            System.out.println("--------------------");
        }
    }
}
```

### 调用方法

```java
import java.lang.reflect.Method;
import java.util.ArrayList;

public class InvokeMethodDemo {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("java.util.ArrayList");

        ArrayList<String> list = new ArrayList<>();
        Method addMethod = clazz.getMethod("add", Object.class);
        addMethod.invoke(list, "Hello Reflection");

        System.out.println("调用后列表: " + list); // 输出: [Hello Reflection]
    }
}
```

---

## 4. 操作字段（属性）
反射可以获取类的字段名，并动态地读取或修改字段值。

### 获取字段名

```java
import java.lang.reflect.Field;

public class FieldNames {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("java.awt.Point");

        // 获取所有字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("字段名: " + field.getName());
        }
    }
}
```

### 读取和修改字段值

```java
import java.awt.Point;
import java.lang.reflect.Field;

public class FieldReflection {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("java.awt.Point");
        Point point = new Point(10, 20);

        // 获取字段
        Field xField = clazz.getDeclaredField("x");
        xField.setAccessible(true); // 允许访问私有字段

        // 读取字段值
        System.out.println("原始 x 值: " + xField.get(point));

        // 修改字段值
        xField.set(point, 42);
        System.out.println("修改后 x 值: " + xField.get(point));
    }
}
```

---

## 5. 操作构造方法
反射支持通过构造方法实例化类对象。

```java
import java.lang.reflect.Constructor;

public class ConstructorReflection {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("java.awt.Point");

        // 获取构造方法
        Constructor<?> constructor = clazz.getConstructor(int.class, int.class);

        // 使用构造方法创建实例
        Object point = constructor.newInstance(10, 20);
        System.out.println("创建的对象: " + point);
    }
}
```

---

## 6. 动态代理
动态代理结合反射，可以拦截方法调用，是实现 AOP（面向切面编程）的关键。

```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface Greeting {
    void sayHello(String name);
}

class GreetingImpl implements Greeting {
    public void sayHello(String name) {
        System.out.println("Hello, " + name);
    }
}

class ProxyHandler implements InvocationHandler {
    private final Object target;

    public ProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("方法调用前: " + method.getName());
        Object result = method.invoke(target, args);
        System.out.println("方法调用后: " + method.getName());
        return result;
    }
}

public class DynamicProxyDemo {
    public static void main(String[] args) {
        Greeting greeting = new GreetingImpl();
        Greeting proxy = (Greeting) Proxy.newProxyInstance(
                greeting.getClass().getClassLoader(),
                greeting.getClass().getInterfaces(),
                new ProxyHandler(greeting)
        );

        proxy.sayHello("Reflection");
    }
}
```

---

## 注意事项
1. **性能开销**：反射操作比直接调用慢，不宜在性能敏感的场景中使用。
2. **安全性**：反射可以破坏封装性，需合理使用。
3. **访问权限**：使用 `setAccessible(true)` 可以绕过访问控制，但可能引发安全隐患。

---

该文档涵盖了 Java 反射的主要知识点和用法。

