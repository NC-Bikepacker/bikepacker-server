package ru.netcracker.bikepackerserver.model.enums;

public enum Role {
    USER(2),
    ADMIN(1);

    private final int roleId;

    Role(int roleId) {
        this.roleId = roleId;
    }

    public int getId() {
        return roleId;
    }
}
