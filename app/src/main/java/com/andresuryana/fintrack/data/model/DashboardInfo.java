package com.andresuryana.fintrack.data.model;

public class DashboardInfo {

    private long income;
    private long outcome;

    public DashboardInfo() {
        // Default constructor
    }

    public DashboardInfo(long income, long outcome) {
        this.income = income;
        this.outcome = outcome;
    }

    public long getIncome() {
        return income;
    }

    public void setIncome(long income) {
        this.income = income;
    }

    public long getOutcome() {
        return outcome;
    }

    public void setOutcome(long outcome) {
        this.outcome = outcome;
    }
}
