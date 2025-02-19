package ua.pp.disik.englishroulette.desktop.fx.sandbox;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;

public class ObservableCollections {
    public static class Person {
        private StringProperty name = new SimpleStringProperty();

        public Person(String name) {
            this.setName(name);
            this.name.addListener((observable, oldValue, newValue) -> {
                System.out.println("A Person is changed: " + oldValue + " -> " + newValue);
            });
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getName() {
            return this.name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        @Override
        public String toString() {
            return "A person is called " + name;
        }
    }

    public static void main(String[] args) {
        ObservableList<Person> personObservableList = FXCollections.observableArrayList(
                person -> new Observable[]{person.nameProperty()}
        );
        personObservableList.addListener((ListChangeListener.Change<? extends Person> change) -> {
            while (change.next()) {
                if (change.wasPermutated()) {
                    printListChange("Permutation");
                } else if (change.wasUpdated()) {
                    printListChange("Update");
                } else if (change.wasReplaced()) {
                    printListChange("Replacing");
                } else {
                    if (change.wasRemoved()) {
                        printListChange("Removing");
                    } else if (change.wasAdded()) {
                        printListChange("Addition");
                    }
                }
            }
        });
        ListProperty<Person> persons = new SimpleListProperty<>(personObservableList);

        IntegerProperty personListSize = new SimpleIntegerProperty();
        personListSize.bind(persons.sizeProperty());

        ObjectProperty<Person> lastPerson = new SimpleObjectProperty<>();
        lastPerson.bind(persons.valueAt(persons.sizeProperty().subtract(1)));

        persons.addAll(List.of(new Person("A"), new Person("B"), new Person("C"), new Person("D")));
        System.out.println(lastPerson.getValue().toString());

        persons.get(0).setName("A1");

        persons.add(new Person("Y"));
        System.out.println(lastPerson.getValue().toString());
    }
    
    public static void printListChange(String type) {
        System.out.println("A list is changed. Type is " + type + ".");
    }
}
