package com.testSoftcaribbean.treeBPlus.Entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Positive(message = "Id is required")
    private int id;

    @NotEmpty(message = "first name is required")
    @Size(min = 3 ,max = 20, message = "please, review size")
    private String firstName;

    @Size(min = 3 ,max = 20, message = "please, review size")
    @NotEmpty(message = "last name is required")
    private String lastName;

    @Size(min = 3 ,max = 50, message = "please, review size")
    @NotEmpty(message = "direction is required")
    private String direction;

    @Positive(message = "The age must be greater than zero")
    private int age;

    @Size(min = 10 ,max = 10, message = "please, review size")
    @NotEmpty(message = "movil is required")
    private String movil;

    @Size(min = 10 ,max = 10, message = "please, review size")
    @NotEmpty(message = "date of birth is required")
    private String dateBirth;

   
    @Override
    public String toString() {
        // return "id: " + id + " first name: " + firstName + " last name: " + lastName + " direction: " + direction
        //         + " movil: " + movil + " age: " + age;
         return "id: " + id + " first name: " + firstName;
    }
    /**
     * 
     * @return cadena con los campos separados por coma
     */
    public String toStringTable() {
        return  id + "," + firstName + "," + lastName + "," + direction
                + "," + movil + "," + age +","+dateBirth;
    }
}