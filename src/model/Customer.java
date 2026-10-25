
package model;

import java.io.Serializable;

public class Customer implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String email;
    private String mobile;
    private final int customerId;
    private Address address;

    private static int customerCounter = 100;

    public Customer(String name, String email, String mobile) {
        this(name, email, mobile, null);
    }

    public Customer(
            String name, String email, String mobile, Address address) {

        this.customerId = ++customerCounter;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public int getCustomerId() {
        return customerId;
    }

    public Address getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public Customer clone() {
        try {
            return (Customer) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address=" + address +
                '}';
    }

    public static class Address implements Serializable {

        private static final long serialVersionUID = 1L;

        private String line;
        private String city;
        private String pincode;

        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine() {
            return line;
        }

        public String getCity() {
            return city;
        }

        public String getPincode() {
            return pincode;
        }

        public void setLine(String line) {
            this.line = line;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "line='" + line + '\'' +
                    ", city='" + city + '\'' +
                    ", pincode='" + pincode + '\'' +
                    '}';
        }
    }
}
