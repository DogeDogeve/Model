package com.InfectionModel;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TestC {
    public static void main(String[] args) {
        int[] ints = new int[6];
        ints[0]+=1;
        ints[1]+=2;
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    @Test
    public void test() throws IOException {
        BufferedWriter bufferedWriter = null;
        String fileName = "D:\\InfectionModel\\Model\\documents\\test.csv";
        File file = new File(fileName);
        FileWriter writer = new FileWriter(file,false);
        bufferedWriter = new BufferedWriter(writer);
        ArrayList<Double> integers = new ArrayList<>();
        double i=1.16512;
        while (i<4) {
            integers.add(i++);
        }
        bufferedWriter.write(integers.toString().substring(1,integers.toString().length()-1));
        bufferedWriter.close();

    }

    @Test
    public void test2() {
        System.out.println("输入：");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        if (s.equals("y")){
            System.out.println("yes");
        }
    }

    public static void test1() {
        try {
            FileInputStream in = new FileInputStream(Main.class.getClassLoader().getResource("config.properties").getPath());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String str;
            br.readLine();
            while ((str = br.readLine()) != null) {
                System.out.println(str);
            }
        }catch (IOException e){

        }
    }
}
