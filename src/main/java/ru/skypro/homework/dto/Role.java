package ru.skypro.homework.dto;

public enum Role {
    ADMIN(2), USER(1);
    private final int id;

    Role(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}


