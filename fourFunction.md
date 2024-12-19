# 四大函数式接口与泛型的高级使用

Java 函数式编程提供了强大的工具，尤其是在处理函数式接口时，`Consumer`、`Supplier`、`Function` 和 `Predicate` 是四大核心接口。这些接口结合泛型和方法引用，使 Java 代码更加简洁高效。本篇文档将深入探讨四大函数式接口的使用方式及其与泛型结合的高级应用。

---

## 1. 四大函数式接口简介

### 1.1 `Consumer<T>`

`Consumer` 接口接收一个输入参数并对其进行操作，不返回任何结果。它适合用于打印、日志记录或修改对象的操作。

- **功能**：接受一个参数，无返回值。
- **方法**：`void accept(T t)`
- **应用场景**：打印、更新、日志记录。

```java
Consumer<String> printer = System.out::println;
printer.accept("Hello, World!");
```

#### 高级用法

1. **链式调用**：通过 `andThen` 方法将多个 `Consumer` 组合。

```java
Consumer<String> printer = System.out::println;
Consumer<String> logger = s -> System.out.println("Logging: " + s);
Consumer<String> combined = printer.andThen(logger);
combined.accept("Test");
// 输出：
// Test
// Logging: Test
```

2. **作用于集合**：对列表中的每个元素执行操作。

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
names.forEach(System.out::println);
// 输出：
// Alice
// Bob
// Charlie
```

---

### 1.2 `Supplier<T>`

`Supplier` 接口不接受任何参数，返回一个结果。它通常用于延迟计算或对象的懒加载。

- **功能**：无输入参数，返回结果。
- **方法**：`T get()`
- **应用场景**：延迟加载、默认值提供。

```java
Supplier<Double> randomSupplier = Math::random;
System.out.println(randomSupplier.get());  // 输出一个随机数
```

#### 高级用法

1. **结合 Optional 提供默认值**：

```java
Supplier<String> defaultSupplier = () -> "Default Value";
Optional<String> optional = Optional.ofNullable(null);
System.out.println(optional.orElseGet(defaultSupplier));
// 输出：Default Value
```

2. **作用于资源初始化**：

```java
Supplier<List<String>> lazyList = () -> Arrays.asList("A", "B", "C");
System.out.println(lazyList.get());
// 输出：[A, B, C]
```

---

### 1.3 `Function<T, R>`

`Function` 接口接收一个输入并返回一个输出，常用于数据转换或值映射。

- **功能**：接受一个参数，返回一个结果。
- **方法**：`R apply(T t)`
- **应用场景**：数据转换、值映射。

```java
Function<Integer, String> intToString = Object::toString;
System.out.println(intToString.apply(42)); // 输出：42
```

#### 高级用法

1. **链式调用**：通过 `andThen` 和 `compose` 方法组合多个 `Function`。

```java
Function<Integer, Integer> square = x -> x * x;
Function<Integer, String> toString = Object::toString;
Function<Integer, String> combined = square.andThen(toString);
System.out.println(combined.apply(4)); // 输出：16
```

2. **复杂数据流处理**：

```java
List<Integer> numbers = Arrays.asList(1, 2, 3);
List<String> results = numbers.stream()
                              .map(x -> x * x)
                              .map(Object::toString)
                              .collect(Collectors.toList());
System.out.println(results);  // 输出：[1, 4, 9]
```

---

### 1.4 `Predicate<T>`

`Predicate` 接口用于条件判断，返回布尔值。它常用于过滤或验证操作。

- **功能**：接受一个参数，返回布尔值。
- **方法**：`boolean test(T t)`
- **应用场景**：条件判断、过滤。

```java
Predicate<String> isNotEmpty = s -> s != null && !s.isEmpty();
System.out.println(isNotEmpty.test("Hello")); // true
```

#### 高级用法

1. **条件组合**：通过 `and`、`or` 和 `negate` 方法组合多个 `Predicate`。

```java
Predicate<String> isShort = s -> s.length() < 5;
Predicate<String> startsWithA = s -> s.startsWith("A");

System.out.println(isShort.and(startsWithA).test("Apple"));  // false
System.out.println(isShort.or(startsWithA).test("Apple"));   // true
System.out.println(isShort.negate().test("Apple"));          // true
```

2. **过滤集合**：

```java
List<String> names = Arrays.asList("Alice", "Bob", "", "Charlie");
names.stream()
     .filter(s -> !s.isEmpty())
     .forEach(System.out::println);
// 输出：
// Alice
// Bob
// Charlie
```

---

## 2. 泛型与函数式接口的结合

### 2.1 泛型方法定义

通过泛型方法，可以定义通用的逻辑操作，从而使代码更加灵活和可重用。

```java
public static <T> void process(T value, Consumer<T> action) {
    action.accept(value);
}

process("Generic", System.out::println);
// 输出：Generic
```

### 2.2 泛型类型约束

通过限定泛型类型的边界，确保类型安全。

```java
public static <T extends Number> T add(T a, T b, Function<T, T> adder) {
    return adder.apply(a);
}

Function<Integer, Integer> increment = x -> x + 1;
System.out.println(add(1, 2, increment));  // 输出：3
```

### 2.3 泛型与方法引用

结合方法引用，可以进一步简化泛型方法的实现。

```java
public static <T> T getDefault(Supplier<T> supplier) {
    return supplier.get();
}

System.out.println(getDefault(() -> "Hello"));  // 输出：Hello
```

### 2.4 提取通用逻辑避免重复代码

通过泛型方法和函数式接口的结合，可以有效减少重复代码，提高代码复用性。

```java
public class TestMethod {

    @Data
    @AllArgsConstructor
    static class Person {
        private String personName;
    }

    @Data
    @AllArgsConstructor
    static class Animal {
        private String animalName;
    }

    public static <T, R> List<R> getName(List<T> nameList, Function<T, R> nameExtractor) {
        return nameList.stream().map(nameExtractor).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<Person> peopleList = Arrays.asList(new Person("张三"), new Person("李四"));
        List<Animal> animalList = Arrays.asList(new Animal("狗"), new Animal("猫"));

        getName(peopleList, Person::getPersonName).forEach(System.out::println);
        getName(animalList, Animal::getAnimalName).forEach(System.out::println);
    }
}
```

---

## 3. 高级应用场景

### 3.1 延迟初始化

通过 `Supplier` 进行延迟计算或资源加载。

```java
Supplier<List<String>> lazyList = () -> Arrays.asList("A", "B", "C");
System.out.println(lazyList.get());
// 输出：[A, B, C]
```

### 3.2 数据处理流水线

结合 `Function` 和 `Predicate` 构建数据处理流水线。

```java
List<String> names = Arrays.asList("Alice", "Bob", "", "Charlie");
names.stream()
     .filter(s -> !s.isEmpty())
     .map(String::toUpperCase)
     .forEach(System.out::println);
// 输出：
// ALICE
// BOB
// CHARLIE
```

### 3.3 动态策略模式

通过函数式接口动态实现策略。

```java
public static <T> T execute(T input, Function<T, T> strategy) {
    return strategy.apply(input);
}

Function<Integer, Integer> doubleIt = x -> x * 2;
System.out.println(execute(5, doubleIt));
// 输出：10
```

### 3.4 灵活的数据映射

通过泛型和 `Function` 实现灵活的数据映射。

```java
public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
    return list.stream().map(mapper).collect(Collectors.toList());
}

List<Integer> numbers = Arrays.asList(1, 2, 3);
List<String> strings = map(numbers, Object::toString);
System.out.println(strings);
// 输出：["1", "2", "3"]
```

---

这份文档详细介绍了四大函数式接口及其结合泛型的高级用法，涵盖从基础到高级的使用场景，帮助开发者更高效地利用 Java 的函数式编程能力。

