package hibernate.domain.any;

public interface Property<T> {
    String getName();
    T getValue();
}
