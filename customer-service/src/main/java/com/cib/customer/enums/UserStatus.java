package com.cib.customer.enums;

/**
 * Account status of an individual corporate user (login/operational status,
 * distinct from CustomerStatus which applies to the parent organization).
 */
public enum UserStatus {
    ACTIVE,
    INACTIVE,
    LOCKED
}
