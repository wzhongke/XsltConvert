package com.learn;

public class GenericMethod {

	public static <T> T getMiddle(T... ts) {
		return ts[ts.length/2];
	}

	public static void main(String[] args) {
		Pair<Manager> manager = new Pair<>(new Manager(), new Manager());
		Pair<? extends Employee> wildcard = manager;
	}

}

class Employee {}

class Manager extends Employee {}

class Pair<T> {
	private T first;
	private T second;

	public Pair(T first, T second) {
		this.first = first;
		this.second = second;
	}

	public T getFirst () {return this.first;}
	public T getSecond () {return this.second;}
	public void setFirst(T newValue) { first = newValue; }
	public void setSecond(T newValue) { second = newValue; }
}