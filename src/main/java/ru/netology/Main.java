package ru.netology;
import javax.xml.parsers.DocumentBuilderFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.opencsv.exceptions.CsvBeanIntrospectionException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;
import org.w3c.dom.Node;
import ru.netology.Employee;

import java.util.List;
import java.io.File;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        List<Employee>list = parse();
        writeString(list);
    }

    //public static Document parseXML() throws Exception{
        //DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
       // return dbf.newDocumentBuilder().parse(new File("data.xml"));
    //}

    public static List<Employee> parse() {
        Staff staff = new Staff();
        Document doc = null;
        try {
            doc = parseXML();
        } catch (Exception e) {
            //System.out.println("Ошибка: " + e.toString());
            System.out.println("Ошибка: " + e.getMessage());
        }
        if (doc==null) {
            System.out.println("Ошибка: документ xml не загружен");
            return new ArrayList<>();
        }
        Node staffNode = doc.getFirstChild();
        List<Employee> employeeList = new ArrayList<>();
        NodeList staffChilds = staffNode.getChildNodes();



        Employee employee;
        for (int i = 0; i < staffChilds.getLength(); i++){
            if(staffChilds.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (!staffChilds.item(i).getNodeName().equals("employee")) {
                continue;
            }
            String id = "";
            String firstName = "";
            String lastName = "";
            String country = "";
            int age = 0;

            NodeList employeeChilds = staffChilds.item(i).getChildNodes();

            for (int j = 0; j < employeeChilds.getLength(); j++) {
                if (employeeChilds.item(j).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                switch (employeeChilds.item(j).getNodeName()) {
                    case "id": {
                        id = employeeChilds.item(j).getTextContent();
                        break;
                    }
                    case "firstName": {
                        firstName = employeeChilds.item(j).getTextContent();
                        break;
                    }
                    case "lastName": {
                        lastName = employeeChilds.item(j).getTextContent();
                        break;
                    }
                    case "country": {
                        country = employeeChilds.item(j).getTextContent();
                        break;
                    }
                    case "age": {
                        age = Integer.valueOf(employeeChilds.item(j).getTextContent());
                        break;
                    }

                }
            }
            employee = new Employee(id,firstName,lastName,country,age);
            employeeList.add(employee);


        }
        staff.setEmployee(employeeList);
        System.out.println(staff.toString());
        return employeeList;

    }
    public static String listToJson(List<Employee>list){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static Document parseXML() throws Exception{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        return dbf.newDocumentBuilder().parse(new File("data.xml"));
        //return dbf.newDocumentBuilder().parse(Main.class.getResourceAsStream("data.xml"));
    }

    public static void writeString(List<Employee>list) throws CsvBeanIntrospectionException {
        String json = listToJson(list);
        try (FileWriter file = new FileWriter("data2.json")) {
            file.write(String.valueOf(json));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}