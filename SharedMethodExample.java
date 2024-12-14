import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 示例场景：
 * 当两个功能完全不相关的类需要共用一个方法时，可以通过泛型和函数式接口的组合，
 * 实现灵活性和简洁性，而不需要为每个类单独定义类似的方法。
 *
 * 这里演示了如何提取 "名称" 的通用逻辑，避免为每个类重复编写方法。
 */
public class SharedMethodExample {

    /**
     * 示例类：Person，表示一个人。
     */
    @Data
    @AllArgsConstructor
    static class Person {
        /** 人名 */
        private String personName;
    }

    /**
     * 示例类：Animal，表示一种动物。
     */
    @Data
    @AllArgsConstructor
    static class Animal {
        /** 动物名 */
        private String animalName;
    }

    /**
     * 通用方法：获取列表中的名称。
     *
     * 使用泛型和函数式接口，将提取 "名称" 的逻辑抽象化。
     * 这样可以兼容任何类，只需传入一个提取方法即可。
     *
     * @param nameList     名称列表（泛型列表）
     * @param nameExtractor 提取名称的函数式接口
     * @param <T>          列表元素的类型（如 Person、Animal 等）
     * @return 转化后的名称列表
     */
    public static <T> List<String> getName(List<T> nameList, Function<T, String> nameExtractor) {
        // 使用 Stream 和 Lambda 表达式简化逻辑
        return nameList.stream()
                .map(nameExtractor) // 应用传入的名称提取逻辑
                .collect(Collectors.toList()); // 收集到列表中
    }

    /**
     * 专用方法：提取 Person 对象的名称。
     *
     * 该方法仅用于演示：虽然可以为特定类型单独实现方法，
     * 但通用方法 getName 足以覆盖此需求，从而减少代码重复。
     *
     * @param personNameList 人名列表
     * @return 转化后的名称列表
     */
    public static List<String> getPersonName(List<Person> personNameList) {
        return personNameList.stream().map(Person::getPersonName).collect(Collectors.toList());
    }

    /**
     * 专用方法：提取 Animal 对象的名称。
     *
     * 该方法仅用于演示：通用方法 getName 同样可以替代这种专用方法。
     *
     * @param animalNameList 动物名列表
     * @return 转化后的名称列表
     */
    public static List<String> getAnimalName(List<Animal> animalNameList) {
        return animalNameList.stream().map(Animal::getAnimalName).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        // 示例数据
        List<Person> peopleList = Arrays.asList(new Person("张三"), new Person("李四"));
        List<Animal> animalList = Arrays.asList(new Animal("狗"), new Animal("猫"));

        // 使用特定类型的方法提取名称
        System.out.println("使用专用方法：");
        getPersonName(peopleList).forEach(System.out::println); // 输出：张三，李四
        getAnimalName(animalList).forEach(System.out::println); // 输出：狗，猫

        // 分割线
        System.out.println("-----------------------------------------------");

        // 使用通用方法提取名称
        System.out.println("使用通用方法：");
        getName(peopleList, Person::getPersonName).forEach(System.out::println); // 输出：张三，李四
        getName(animalList, Animal::getAnimalName).forEach(System.out::println); // 输出：狗，猫
    }
}
