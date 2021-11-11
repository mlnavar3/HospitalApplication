package com.example.hospitalapplication;

import java.util.Date;

public class Prescription {

     private String name;
     private int quantity;
     private Boolean active;

     public Prescription(String name, int quantity, Boolean active) {
          this.name = name;
          this.quantity = quantity;
          this.active = active;
     }

     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }

     public int getQuantity() {
          return quantity;
     }

     public void setQuantity(int quantity) {
          this.quantity = quantity;
     }

     public Boolean getActive() {
          return active;
     }

     public void setActive(Boolean active) {
          this.active = active;
     }
}
