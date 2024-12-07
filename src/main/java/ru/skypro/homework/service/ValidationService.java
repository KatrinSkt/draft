package ru.skypro.homework.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

public class ValidationService {
    public static boolean isValidLength(String name, int min, int max) {
        return name.length() >= min && name.length() <= max;
    }

    public static boolean isValidSymbol(String name) {
        // Проверка на длину и соответствие регулярному выражению
        return Pattern.matches("[a-zA-Zа-яА-ЯёЁ]+", name);
    }
    public static boolean isValidUsername(String username) {
        // Проверка на длину и соответствие регулярному выражению
        return Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", username);
    }
    public static boolean isValidPhone(String phone) {
        // Регулярное выражение для проверки формата +7(000)000-00-00
        String regex = "^\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}$";
        return Pattern.matches(regex, phone);
    }
    public static boolean isValidPrice(Integer price, int min, int max) {
        // Проверка на null и диапазон значений
        return price != null && price >= min && price <= max;
    }
}
