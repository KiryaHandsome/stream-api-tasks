package by.pryhozhy;

import by.pryhozhy.model.*;
import by.pryhozhy.util.Util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Task 1---------------------------------------------------------");
        task1();
        System.out.println("Task 2---------------------------------------------------------");
        task2();
        System.out.println("Task 3---------------------------------------------------------");
        task3();
        System.out.println("Task 4---------------------------------------------------------");
        task4();
        System.out.println("Task 5---------------------------------------------------------");
        task5();
        System.out.println("Task 6---------------------------------------------------------");
        task6();
        System.out.println("Task 7---------------------------------------------------------");
        task7();
        System.out.println("Task 8---------------------------------------------------------");
        task8();
        System.out.println("Task 9---------------------------------------------------------");
        task9();
        System.out.println("Task 10---------------------------------------------------------");
        task10();
        System.out.println("Task 11---------------------------------------------------------");
        task11();
        System.out.println("Task 12---------------------------------------------------------");
        task12();
        System.out.println("Task 13---------------------------------------------------------");
        task13();
        System.out.println("Task 14---------------------------------------------------------");
        task14();
        System.out.println("Task 15---------------------------------------------------------");
        task15();
    }

    private static void task1() throws IOException {
        List<Animal> animals = Util.getAnimals();

//        solution for Sergey's task
//        List<Animal> list = animals.stream()
//                .filter(a -> a.getAge() >= 10 && a.getAge() <= 20)
//                .sorted(Comparator.comparingInt(Animal::getAge))
//                .toList();
//        ListUtils.partition(list, 7) //apache.commons.collections library
//                .get(2)
//                .forEach(System.out::println);

        animals.stream()
                .filter(a -> a.getAge() >= 10 && a.getAge() <= 20)
                .sorted(Comparator.comparingInt(Animal::getAge))
                .skip(7)
                .limit(7)
                .forEach(System.out::println);
    }

    private static void task2() throws IOException {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .filter(a -> "Japanese".equals(a.getOrigin()))
                .peek(a -> a.setBread(a.getBread().toUpperCase()))
                .filter(a -> "Female".equals(a.getGender()))
                .map(Animal::getBread)
                .forEach(System.out::println);
    }

    private static void task3() throws IOException {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .filter(a -> a.getAge() > 30)
                .map(Animal::getOrigin)
                .distinct()
                .filter(a -> a.startsWith("A"))
                .forEach(System.out::println);
    }

    private static void task4() throws IOException {
        List<Animal> animals = Util.getAnimals();
        System.out.println(animals.stream()
                .filter(a -> "Female".equals(a.getGender()))
                .count());
    }

    private static void task5() throws IOException {
        List<Animal> animals = Util.getAnimals();
        System.out.println(animals.stream()
                .filter(a -> a.getAge() >= 20 && a.getAge() <= 30)
                .anyMatch(a -> "Hungarian".equals(a.getOrigin())));
    }

    private static void task6() throws IOException {
        List<Animal> animals = Util.getAnimals();
        boolean allMaleOrFemale = animals.stream()
                .allMatch(a -> "Male".equals(a.getGender())
                        || "Female".equals(a.getGender()));
        System.out.println("They are all male and female: " + allMaleOrFemale);
    }

    private static void task7() throws IOException {
        List<Animal> animals = Util.getAnimals();
        boolean noneFromOceania = animals.stream()
                .noneMatch(a -> "Oceania".equals(a.getOrigin()));
        System.out.println("Nobody from Oceania: " + noneFromOceania);
    }

    private static void task8() throws IOException {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .sorted(Comparator.comparing(Animal::getBread))
                .limit(100)
                .max(Comparator.comparing(Animal::getAge))
                .ifPresentOrElse(System.out::println,
                        () -> System.out.println("There is no animal with max age"));
    }

    private static void task9() throws IOException {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .map(Animal::getBread)
                .map(String::toCharArray)
                .min(Comparator.comparing(arr -> arr.length))
                .ifPresentOrElse(el -> System.out.println(el.length),
                        () -> System.out.println("There is no the shortest array"));
    }

    private static void task10() throws IOException {
        List<Animal> animals = Util.getAnimals();
        System.out.println(animals.stream()
                .mapToInt(Animal::getAge)
                .sum());
    }

    private static void task11() throws IOException {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .filter(a -> "Indonesian".equals(a.getOrigin()))
                .mapToInt(Animal::getAge)
                .average()
                .ifPresentOrElse(System.out::println,
                        () -> System.out.println("There is no animals from Indonesian"));
    }

    private static void task12() throws IOException {
        List<Person> people = Util.getPersons();
        people.stream()
                .filter(p -> "Male".equals(p.getGender()))
                .filter(p -> {
                            int age = Period.between(p.getDateOfBirth(), LocalDate.now()).getYears();
                            return age >= 18 && age <= 27;
                        }
                )
                .sorted(Comparator.comparingInt(Person::getRecruitmentGroup))
                .limit(200)
                .forEach(System.out::println);
    }

    private static void task13() throws IOException {
        //TODO: complete method
        List<House> houses = Util.getHouses();
        Predicate<Person> isChildOrRetiree = p -> {
            int age = Period.between(p.getDateOfBirth(), LocalDate.now()).getYears();
            return age < 18
                    || ("Female".equals(p.getGender()) && age >= 58)
                    || ("Male".equals(p.getGender()) && age >= 63);
        };
        Map<Boolean, List<House>> hospitalsAndOthers = houses.stream()
                .collect(Collectors.partitioningBy(h -> "Hospital".equals(h.getBuildingType())));
        List<Person> people = new ArrayList<>();
        hospitalsAndOthers.get(true)   //from hospital
                .stream()
                .flatMap(h -> h.getPersonList().stream())
                .forEach(people::add);
        System.out.println("From hospital" + people);
        hospitalsAndOthers.get(false)
                .stream()
                .flatMap(h -> h.getPersonList().stream())
                .collect(Collectors.partitioningBy(isChildOrRetiree))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder())) //it must be true - first
                .forEach(e -> people.addAll(e.getValue()));
        people.stream().limit(500).forEach(System.out::println);
    }

    private static void task14() throws IOException {
        //TODO: complete method
        List<Car> cars = Util.getCars();
        Map<String, List<Car>> countriesCars = new HashMap<>();
        List<Car> turkmenistan = cars.stream()
                .filter(c -> "Jaguar".equals(c.getCarModel()) || "White".equals(c.getColor()))
                .toList();
        cars.removeAll(turkmenistan);
        countriesCars.put("turkmenistan", turkmenistan);
        List<Car> uzbekistan = cars.stream()
                .filter(c -> c.getMass() < 1500)
                .filter(c -> "BMW".equals(c.getCarModel())
                        || "Lexus".equals(c.getCarModel())
                        || "Chrysler".equals(c.getCarModel())
                        || "Toyota".equals(c.getCarModel()))
                .toList();
        cars.removeAll(uzbekistan);
        countriesCars.put("uzbekistan", uzbekistan);
        List<Car> kazahstan = cars.stream()
                .filter(c -> "Black".equals(c.getColor()))
                .filter(c -> c.getMass() > 4000)
                .filter(c -> "GMC".equals(c.getCarModel())
                        || "Dodge".equals(c.getCarModel()))
                .toList();
        cars.removeAll(kazahstan);
        countriesCars.put("kazahstan", kazahstan);
        List<Car> kirgizstan = cars.stream()
                .filter(c -> c.getReleaseYear() < 1982
                        || "Civic".equals(c.getCarModel())
                        || "Cherokee".equals(c.getCarModel()))
                .toList();
        cars.removeAll(kirgizstan);
        countriesCars.put("kirgizstan", kirgizstan);
        List<Car> russia = cars.stream()
                .filter(c -> (!"Yellow".equals(c.getColor())
                        && !"Red".equals(c.getColor())
                        && !"Green".equals(c.getColor())
                        && !"Blue".equals(c.getColor()))
                        || c.getPrice() > 40000)
                .toList();
        cars.removeAll(russia);
        countriesCars.put("russia", russia);
        List<Car> mongolia = cars.stream()
                .filter(c -> c.getVin().contains("59"))
                .toList();
        countriesCars.put("russia", russia);

        countriesCars.forEach((s, c) -> {
            int totalMass = c.stream().mapToInt(Car::getMass).sum();
            System.out.println("Total mass for " + s + " = " + totalMass);
            System.out.println("Total transfer overheads for " + s + " = " + (double)totalMass / 1000 * 7.14);
        });
        System.out.println("Total revenue: " + countriesCars.values()
                .stream().mapToInt(carList -> carList.stream()
                        .mapToInt(Car::getMass)
                        .sum())
                .sum());
    }

    private static void task15() throws IOException {
        List<Flower> flowers = Util.getFlowers();
        List<String> appropriateMaterials = List.of("Glass", "Aluminum", "Steel");
        double waterPriceForCubicMeter = 1.39;
        int numberOfDaysInNext5Years = Period.between(LocalDate.now(), LocalDate.now().plusYears(5)).getDays();
        Comparator<Flower> comparator = Comparator
                .comparing(Flower::getOrigin, Comparator.reverseOrder())
                .thenComparing(Flower::getPrice, Comparator.reverseOrder())
                .thenComparing(Flower::getWaterConsumptionPerDay, Comparator.reverseOrder());
        System.out.println(flowers.stream()
                .sorted(comparator)
                .filter(f -> f.getCommonName().matches("^[c-sC-S]+")) //if all letters must be from this interval
                .filter(f -> f.getFlowerVaseMaterial()                     //if only first letter - "^[c-sC-S].*"
                        .stream()
                        .anyMatch(appropriateMaterials::contains))
                .collect(Collectors.toMap(f -> f, f -> f.getPrice()  // divide on 1000 because 1 cub.m. = 1000 liters
                        + f.getWaterConsumptionPerDay() * numberOfDaysInNext5Years * waterPriceForCubicMeter / 1000))
                .entrySet()
                .stream()
                .mapToDouble(Map.Entry::getValue)
                .sum());
    }
}