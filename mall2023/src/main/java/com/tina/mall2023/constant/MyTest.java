package com.tina.mall2023.constant;

public class MyTest {
    public static void main(String[] args) {
        ProductCategory category = ProductCategory.FOOD;
        String s = category.name();
        System.out.println(s);          //FOOD

        String s2="CAR";
        ProductCategory category2 = ProductCategory.valueOf(s2);
        System.out.println(category2);
    }
}
