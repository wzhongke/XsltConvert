package mybatis.dao;

/**
 * Created by admin on 2017/7/20.
 */
public class Person {
    private long id;
    private int sex;
    private String name;
    private int age;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", sex=" + sex +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
