package com.ling.elasticsearch.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


//@Document注解会对实体中是的所有属性建立索引
//indexName表示索引名，type表示在索引中创建一个名为“customer”的type
//shards=1 表示只使用一个分片 replicas = 0 表示不使用复制
//refreshInterval ="-1" 表示禁用索引刷新
@Document(indexName = "customer", type = "customer", shards = 1, replicas = 0, refreshInterval = "-1")
public class Customer {

    //Id注解加上之后，在elasticsearch里相应于该列就是主键
    @Id
    private String id;

    private String userName;

    private String address;

    private int age;

    public Customer(){
    }

    public Customer(String userName, String address, int age) {
        this.userName = userName;
        this.address = address;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                '}';
    }
}
