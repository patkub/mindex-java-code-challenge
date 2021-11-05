package com.mindex.challenge.data;

import java.util.Date;

/**
 * Compensation type
 * @author Patrick Kubiak
 */
public class Compensation {

    // employee, salary, and effectiveDate
    private String employeeId;
    private float salary;
    private Date effectiveDate;

    public Compensation() {
    }

    // getters/setters

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }


}
