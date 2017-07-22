package stream.aggregate;

import lambda.Person;

import java.util.*;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by admin on 2017/7/19.
 */
public class Reduction {

    public static void main(String[] args) {
        List<Person> roster = new ArrayList<>();
        IntStream.range(0,4).forEach(i-> roster.add(new Person(20, "wang" + i, Person.Sex.MALE)));

        // 使用 map/filter/forEach等操作的顺序非常重要，如果将filter放到第一位，可能会大大减少后续流操作的元素数目
        Averager averageCollect = roster.stream()
                .filter(p -> p.getGender() == Person.Sex.MALE)
                .map(Person::getAge)
                .collect(Averager::new, Averager::accept, Averager::combine);
        System.out.println("Average age of male members: " + averageCollect.average());

        long result = roster.stream().filter(p -> p.getChildren() != null).flatMap(p -> p.getChildren().stream()).count();
        roster.stream().map(Person::getEmailAddress).forEach(p-> p.isPresent());
        System.out.println(result);
        List<String> namesOfMaleMembersCollect = roster
                .stream()
                .filter(p -> p.getGender() == Person.Sex.MALE)
                .map(Person::getName)
                .collect(Collectors.toList());
        Map<Person.Sex, List<Person>> byGender = roster.stream()
                .collect(Collectors.groupingBy(Person::getGender));
        Map<Person.Sex, List<String>> namesByGender =
                roster.stream()
                    .collect(
                        Collectors.groupingBy(
                                Person::getGender,
                                Collectors.mapping(
                                        Person::getName,
                                        Collectors.toList())));
        Stream.of("d2", "a2", "b1", "b3", "c")
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("A");
                })
                .forEach(s -> System.out.println("forEach: " + s));

        Arrays.asList("a1", "a2", "b1", "c2", "c1").parallelStream()
                .filter(s -> {System.out.format("Filter: %s [%s]\n", s, Thread.currentThread().getName()); return true;})
                .map(s-> {
                    System.out.format("map: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.format("forEach: %s [%s]\n",
                        s, Thread.currentThread().getName()));
    }
}

class Averager implements IntConsumer {
    private int total = 0;
    private int count = 0;

    public double average() {
        return count > 0 ? ((double) total)/count : 0;
    }

    public void accept(int i) { total += i; count++; }
    public void combine(Averager other) {
        total += other.total;
        count += other.count;
    }
}