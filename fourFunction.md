# 四大函数式接口和泛型的高级使用

Java 的函数式编程依赖于四大函数式接口：`Consumer`、`Supplier`、`Function` 和 `Predicate`。结合泛型和方法引用，它们在现代 Java 编程中发挥了重要作用。本指南深入探讨其高级用法。

---

## 1. 四大函数式接口简介

### 1.1 `Consumer<T>`

- **功能**：接受一个参数，不返回结果。
- **主要方法**：`void accept(T t)`。
- **应用场景**：用于对某个对象进行操作但不需要返回值，例如打印或更新操作。

```java
Consumer<String> printer = System.out::println;
printer.accept("Hello, World!");
```

**高级用法**：

链式调用：使用 `andThen` 组合多个 `Consumer`。

```java
Consumer<String> printer = System.out::println;
Consumer<String> logger = s -> System.out.println("Logging: " + s);
Consumer<String> combined = printer.andThen(logger);
combined.accept("Test");
```

---

### 1.2 `Supplier<T>`

- **功能**：不接受参数，返回一个结果。
- **主要方法**：`T get()`。
- **应用场景**：用于延迟计算或对象创建。

```java
Supplier<Double> randomSupplier = Math::random;
System.out.println(randomSupplier.get());
Supplier<Stream<String>> streamSupplier = () -> Stream.of("A", "B", "C");
Stream<String> stream1 = streamSupplier.get();
```

**高级用法**：

结合 `Optional` 提供默认值。

```java
Supplier<String> defaultSupplier = () -> "Default Value";
Optional<String> optional = Optional.ofNullable(null);
System.out.println(optional.orElseGet(defaultSupplier));
```

---

### 1.3 `Function<T, R>`

- **功能**：接受一个参数，返回一个结果。
- **主要方法**：`R apply(T t)`。
- **应用场景**：用于数据转换或映射。

```java
Function<Integer, String> intToString = Object::toString;
System.out.println(intToString.apply(42));
```

**高级用法**：

链式调用：使用 `andThen` 或 `compose`。

```java
Function<Integer, Integer> square = x -> x * x;
Function<Integer, String> toString = Object::toString;
Function<Integer, String> combined = square.andThen(toString);
System.out.println(combined.apply(4));
```

---

### 1.4 `Predicate<T>`

- **功能**：接受一个参数，返回布尔值。
- **主要方法**：`boolean test(T t)`。
- **应用场景**：用于条件判断。

```java
Predicate<String> isNotEmpty = s -> s != null && !s.isEmpty();
System.out.println(isNotEmpty.test("Hello")); // true
```

**高级用法**：

组合判断

```java
Predicate<String> isShort = s -> s.length() < 5;
Predicate<String> startsWithA = s -> s.startsWith("A");
Predicate<String> combined = isShort.and(startsWithA);
System.out.println(isShort.or(startsWithA).test("Apple")); // true
System.out.println(isShort.and(startsWithA).test("Apple")); // false
System.out.println(isShort.negate().test("Apple")); // true
System.out.println(combined.test("Apple")); // false
```

---

## 2. 泛型与函数式接口的结合

### 2.1 泛型方法定义

通过泛型方法定义函数式接口的灵活使用。

```java
public static <T> void process(T value, Consumer<T> action) {
    action.accept(value);
}

process("Generic", System.out::println);
```

### 2.2 泛型类型约束

限定泛型类型以提高类型安全性。

```java
public static <T extends Number> T add(T a, T b, Function<T, T> adder) {
    return adder.apply(a);
}

Function<Integer, Integer> increment = x -> x + 1;
System.out.println(add(1, 2, increment));
```

### 2.3 泛型与方法引用

使用泛型时，可以结合方法引用简化代码。

```java
public static <T> T getDefault(Supplier<T> supplier) {
    return supplier.get();
}

System.out.println(getDefault(() -> "Hello"));
```

---

### 2.4 示例：提取通用逻辑避免重复代码

使用泛型和函数式接口提取共用逻辑，避免重复实现类似的方法。

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

此示例展示如何通过泛型方法 `getName` 提取共性逻辑，减少重复代码，提高代码复用性。

---

## 3. 高级应用场景

### 3.1 延迟初始化

使用 `Supplier` 进行延迟计算或资源加载。

```java
Supplier<List<String>> lazyList = () -> Arrays.asList("A", "B", "C");
System.out.println(lazyList.get());
```

### 3.2 数据处理流水线

结合 `Function` 和 `Predicate` 构建数据处理流水线。

```java
List<String> names = Arrays.asList("Alice", "Bob", "", "Charlie");
names.stream()
     .filter(s -> !s.isEmpty())
     .map(String::toUpperCase)
     .forEach(System.out::println);
```

### 3.3 动态策略模式

通过函数式接口动态实现策略。

```java
public static <T> T execute(T input, Function<T, T> strategy) {
    return strategy.apply(input);
}

Function<Integer, Integer> doubleIt = x -> x * 2;
System.out.println(execute(5, doubleIt));
```

### 3.4 灵活的数据映射

使用泛型和 `Function` 实现灵活的数据映射。

```java
public static <T, R> List<R> mapList(List<T> inputList, Function<T, R> mapper) {
    return inputList.stream().map(mapper).collect(Collectors.toList());
}

List<Integer> numbers = Arrays.asList(1, 2, 3);
List<String> strings = mapList(numbers, Object::toString);
System.out.println(strings);
```

---

## 4. 注意事项

- **函数组合可能导致调试困难**：注意分解复杂链式调用。
- **避免滥用**：函数式接口适合简化代码，但复杂逻辑可能更适合传统方式。
- **泛型需要边界约束**：在泛型方法中使用时，确保对泛型类型的边界有清晰定义。

