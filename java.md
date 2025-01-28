# Java 语法总结

## 1. 对象和类
Java 是面向对象的编程语言，核心概念包括类和对象。

### 示例：定义一个类和创建对象
```java
class Person {
    String name;
    int age;

    // 构造方法
    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    void introduce() {
        System.out.println("我的名字是 " + name + ", 我 " + age + " 岁。");
    }
}

public class Main {
    public static void main(String[] args) {
        Person person = new Person("张三", 25);
        person.introduce();
    }
}
```

## 2. 修饰符
修饰符控制类、方法、变量的访问权限和行为。

### 示例：访问修饰符
```java
class Example {
    public int publicField = 1; // 公共
    private int privateField = 2; // 私有

    public void showFields() {
        System.out.println("Public: " + publicField);
        System.out.println("Private: " + privateField);
    }
}

public class Main {
    public static void main(String[] args) {
        Example example = new Example();
        System.out.println("Public: " + example.publicField);
        example.showFields();
    }
}
```

## 3. 运算符
Java 提供多种运算符，用于执行数学运算、比较操作等。

### 示例：算术运算符
```java
public class Main {
    public static void main(String[] args) {
        int a = 10;
        int b = 5;
        System.out.println("加法: " + (a + b));
        System.out.println("减法: " + (a - b));
        System.out.println("乘法: " + (a * b));
        System.out.println("除法: " + (a / b));
    }
}
```

## 4. 循环结构
Java 支持多种循环结构：for、while 和 do-while。

### 示例：for 循环
```java
public class Main {
    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            System.out.println("当前值: " + i);
        }
    }
}
```

## 5. 条件语句
条件语句用于根据条件执行代码。

### 示例：if-else 条件语句
```java
public class Main {
    public static void main(String[] args) {
        int number = 10;
        if (number > 0) {
            System.out.println("数字是正数。");
        } else {
            System.out.println("数字是非正数。");
        }
    }
}
```

## 6. switch
`switch` 用于多条件分支选择。

### 示例：switch 语句
```java
public class Main {
    public static void main(String[] args) {
        int day = 3;
        switch (day) {
            case 1:
                System.out.println("星期一");
                break;
            case 2:
                System.out.println("星期二");
                break;
            case 3:
                System.out.println("星期三");
                break;
            default:
                System.out.println("其他日子");
        }
    }
}
```

## 7. continue 和 break
`continue` 用于跳过当前循环，`break` 用于终止循环。

### 示例：continue 和 break
```java
public class Main {
    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            if (i == 3) {
                continue; // 跳过当前循环
            }
            if (i == 5) {
                break; // 终止循环
            }
            System.out.println("当前值: " + i);
        }
    }
}
```

## 8. 包装类基础
Java 提供包装类将基本数据类型封装为对象。

### 示例：使用包装类
```java
public class Main {
    public static void main(String[] args) {
        Integer num = 10; // 自动装箱
        int value = num; // 自动拆箱
        System.out.println("包装类值: " + num);
        System.out.println("基本类型值: " + value);
    }
}
```

## 9. 数组
数组用于存储多个相同类型的值。

### 示例：数组的声明与使用
```java
public class Main {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5};
        for (int number : numbers) {
            System.out.println("数组值: " + number);
        }
    }
}
```

## 10. 方法
方法是可重复使用的代码块。

### 示例：定义和调用方法
```java
public class Main {
    public static void greet(String name) {
        System.out.println("你好, " + name + "!");
    }

    public static void main(String[] args) {
        greet("张三");
    }
}
```

## 11. 面向对象
Java 支持面向对象的三大特性：封装、继承和多态。

### 示例：封装
```java
class Person {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class Main {
    public static void main(String[] args) {
        Person person = new Person();
        person.setName("李四");
        System.out.println("姓名: " + person.getName());
    }
}
```

### 示例：继承
```java
class Animal {
    void sound() {
        System.out.println("动物发出声音");
    }
}

class Dog extends Animal {
    @Override
    void sound() {
        System.out.println("狗叫: 汪汪");
    }
}

public class Main {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.sound();
    }
}
```

### 示例：多态
```java
class Animal {
    void sound() {
        System.out.println("动物发出声音");
    }
}

class Cat extends Animal {
    @Override
    void sound() {
        System.out.println("猫叫: 喵喵");
    }
}

public class Main {
    public static void main(String[] args) {
        Animal animal = new Cat();
        animal.sound();
    }
}
```

## 12. 抽象
抽象类是不能实例化的类，只能通过子类实现其抽象方法。

### 示例：抽象类
```java
abstract class Animal {
    abstract void sound();
}

class Bird extends Animal {
    @Override
    void sound() {
        System.out.println("鸟叫: 啾啾");
    }
}

public class Main {
    public static void main(String[] args) {
        Animal bird = new Bird();
        bird.sound();
    }
}
```

## 13. 接口
接口定义类可以实现的行为规范。

### 示例：接口
```java
interface Flyable {
    void fly();
}

class Airplane implements Flyable {
    @Override
    public void fly() {
        System.out.println("飞机在飞");
    }
}

public class Main {
    public static void main(String[] args) {
        Flyable airplane = new Airplane();
        airplane.fly();
    }
}
```

## 14. 枚举
枚举是表示固定集合的常量。

### 示例：枚举的使用
```java
enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
}

public class Main {
    public static void main(String[] args) {
        Day today = Day.WEDNESDAY;
        System.out.println("今天是: " + today);
    }
}
```
## 15. 集合
Java 集合框架包括 `List`、`Set` 和 `Map`，用于存储和操作数据。

### 15.1 List 示例
List 是一个有序集合，允许重复元素。

```java
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("苹果");
        list.add("香蕉");
        list.add("苹果");

        for (String item : list) {
            System.out.println("元素: " + item);
        }
    }
}
```

### 15.2 Set 示例
Set 是一个无序集合，不允许重复元素。

```java
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        set.add("苹果");
        set.add("香蕉");
        set.add("苹果"); // 重复元素将被忽略

        for (String item : set) {
            System.out.println("元素: " + item);
        }
    }
}
```

### 15.3 Map 示例
Map 是一个键值对的集合，键是唯一的。

```java
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("苹果", 3);
        map.put("香蕉", 2);
        map.put("苹果", 5); // 覆盖键为"苹果"的值

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
```

## 16. Lambda 表达式
Lambda 表达式是 Java 8 引入的一种简洁表示方法。

### 示例：使用 Lambda 表达式排序
```java
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("张三", "李四", "王五");

        names.sort((a, b) -> a.compareTo(b));

        names.forEach(name -> System.out.println(name));
    }
}
```

### 示例：使用 Lambda 表达式实现线程
```java
public class Main {
    public static void main(String[] args) {
        new Thread(() -> System.out.println("线程运行中...")).start();
    }
}
```

## 17. IO 流
Java 提供 IO 流用于文件和数据的输入输出操作。

### 示例：读取文件
```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("example.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 示例：写入文件
```java
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("example.txt"))) {
            writer.write("这是一个示例文本。");
            writer.newLine();
            writer.write("文件写入完成。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
