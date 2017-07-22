package lambda;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by admin on 2017/7/18.
 */
public class Person {
    public enum Sex {
        MALE, FEMALE
    }

    int age;
    String name;
    LocalDate birthday;
    Sex gender;
    String emailAddress;
    List<String> children;

    public Person() {}

    public Person(int age, String name, Sex gender) {
        this.age = age;
        this.name = name;
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Sex getGender() {
        return gender;
    }

    public void setGender(Sex gender) {
        this.gender = gender;
    }

    public Optional<String> getEmailAddress() {
        return Optional.ofNullable(emailAddress);
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<String> getChildren() {
        return children;
    }

    public void printPerson() {
        System.out.println(this);
    }

    public static void printPersonOlderThan(List<Person> roster, int age) {
        for (Person p: roster) {
            if (p.getAge() >= age)
                p.printPerson();
        }
    }

    public static void processPersons(List<Person> roster, Predicate<Person> tester, Consumer<Person> block) {
        for (Person p: roster) {
            if (tester.test(p)) {
                block.accept(p  );
            }
        }
    }

    public static void processPersonsWithFunction(List<Person> roster,
              Predicate<Person> tester,
              Function<Person, Integer> mapper,
              Consumer<Integer> block) {
        for (Person p: roster) {
            if (tester.test(p)) {
                Integer data = mapper.apply(p);
                block.accept(data);
            }
        }
    }

    public static <X, Y> void processElements (
            Iterable<X> source,
            Predicate<X> tester,
            Function<X, Y> mapper,
            Consumer<Y> block ) {
        for (X p: source) {
            if (tester.test(p)) {
                Y data = mapper.apply(p);
                block.accept(data);
            }
        }
    }

    public static <T, SOURCE extends Collection<T>, DEST extends Collection<T>>
        DEST transferElements(
            SOURCE sourceCollection,
            Supplier<DEST> collectionFactory) {

        DEST result = collectionFactory.get();
        for (T t : sourceCollection) {
            result.add(t);
        }
        return result;
    }

    public static int compareByAge(Person a, Person b) {
        return a.birthday.compareTo(b.birthday);
    }

    public static void sort(List<Person> roster) {
        Person[] rosterAsArray = roster.toArray(new Person[roster.size()]);
//        Arrays.sort(rosterAsArray, new PersonAgeComparator());
        Arrays.sort(rosterAsArray,
                (Person a, Person b) -> a.getBirthday().compareTo(b.getBirthday())
        );

    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", gender=" + gender +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }

    public static void main(String[] args) {
        processPersons(new ArrayList<>(), p -> p.getAge() >= 18, p -> p.printPerson());
        processPersonsWithFunction(
                new ArrayList<>(),
                p ->  p.getAge() >= 18
                        && p.getAge() <= 25,
                p -> p.getAge(),
                email -> System.out.println(email)
        );
        processElements(
                new ArrayList<Person>(),
                p ->  p.getAge() >= 18
                        && p.getAge() <= 25,
                p -> p.getAge(),
                email -> System.out.println(email)
        );
        Set<Person> rosterSetLambda =
                transferElements(new ArrayList<Person>(), HashSet::new);
        List<Person> roster = new ArrayList<>();
        Integer totlaAge = roster
                .stream()
                .mapToInt(Person::getAge)
                .sum();

        Integer totalAgeReduce = roster
                .stream()
                .map(Person::getAge)
                .reduce(
                        0,
                        (a, b) -> a + b);
    }
}



class PersonAgeComparator implements Comparator<Person> {
    public int compare(Person a, Person b) {
        return a.getBirthday().compareTo(b.getBirthday());
    }
}

interface CheckPerson {
    boolean test(Person p);
}

class CheckPersonEligible implements CheckPerson {
    public boolean test(Person p) {
        return p.gender == Person.Sex.MALE &&
                p.getAge() >= 18 &&
                p.getAge() <= 25;
    }
}