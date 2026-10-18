package com.napier.DevOps_Lab;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        // "localhost", 27000 အစား Network ထဲမှ Container Name "mongo-dbserver", 27017 သို့ ပြောင်းခြင်း
        MongoClient mongoClient = new MongoClient("mongo-dbserver", 27017);
        MongoDatabase database = mongoClient.getDatabase("mydb");
        MongoCollection<Document> collection = database.getCollection("test");

        Document doc = new Document("name", "Hla Min Phyo")
                .append("class", "DevOps")
                .append("year", "2026")
                .append("result", new Document("CW", 95).append("EX", 85));
        collection.insertOne(doc);

        Document myDoc = collection.find().first();
        System.out.println(myDoc.toJson());
    }

    // ၂။ Lab 6 အတွက် ထည့်သွင်းရမည့် printSalaries method
    public void printSalaries(ArrayList<Employee> employees)
    {
        // Check employees is not null
        if (employees == null)
        {
            System.out.println("No employees");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %-8s", "Emp No", "First Name", "Last Name", "Salary"));

        // Loop over all employees in the list
        for (Employee emp : employees)
        {
            if (emp == null)
                continue;
            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }

}

