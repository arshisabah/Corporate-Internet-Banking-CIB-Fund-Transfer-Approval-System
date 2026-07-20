package com.cib.customer.enums;

/**
 * Lifecycle status of a corporate customer (the company/organization itself,
 * not an individual user). Only ACTIVE customers should be allowed to
 * initiate new fund transfers - enforced at the service layer.
 */
public enum CustomerStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED,
    CLOSED
}
