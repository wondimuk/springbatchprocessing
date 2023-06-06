package com.wk.batchprocess.model;

public class Employee {

    private String lastName;
    private String firstName;
    private String address;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    @Override
    public String toString() {
        return "firstName: " + firstName + ", lastName: " + lastName + ", address: "+ address;
    }

}