package com.InfectionModel;

import com.InfectionModel.bean.Person;
import com.InfectionModel.bean.State;
import com.InfectionModel.bean.Virus;
import org.apache.log4j.Logger;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class Main {
    public static Random random = new Random();
    public static int speed=1;
    public static List<String> infectedNums = new ArrayList<>();
    public static double immune=0;
    public static Virus virus = new Virus();
    public static List<Person> personList = new ArrayList<>();
    public static ArrayList<Person> peopleInfected = new ArrayList<>();
    public static int maxTurn;
    public static int turn=0;
    public static int days=0;
    public static Map<State,List<String>> dayResultMap = new HashMap<>();
    static{
        dayResultMap.put(State.Light,new ArrayList<>());
        dayResultMap.put(State.Heavy,new ArrayList<>());
        dayResultMap.put(State.Danger,new ArrayList<>());
        dayResultMap.put(State.Infected,new ArrayList<>());
        dayResultMap.put(State.Immune,new ArrayList<>());
        dayResultMap.put(State.Normal,new ArrayList<>());
    }

    // operation info
    public static boolean masked = false;
    public static List<String> n95List = new ArrayList<>();
    public static double disinfect = 0;
    public static int vaccine = 0;
    public static boolean quarantine = false;

//    public static Map<Integer, Room> roomMap;
    public static Logger logger = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        logger.info("===== start =====");
//        System.out.println(System.getProperty("user.dir"));
//        System.out.println(Main.class.getClassLoader().getResource("../").getPath());

        ConfigHelper.init(); // 都配置文件

        readCSV(); // 读csv文件
        maxTurn = peopleInfected.get(0).getLocationList().size();
        Scanner scanner = new Scanner(System.in);
        while(turn<maxTurn){
            if (turn>0){
                System.out.println("是否对病毒采取应对措施[y/n]?");
                String s = scanner.nextLine();
                boolean y = s.equals("y");
                if (y) {
                    ConfigHelper.operation();
                    vaccinate(vaccine);
                }
                compute(y);
            }
            writeData();
            turn++;
            if (turn%8==0) {
                computeDay();
                writeDataDay();
                days++;
            }
        }
        scanner.close();
        logger.info("===== end =====");
    }

    public static void readCSV(){
        logger.info("开始读取time.csv");
        String name = "/documents/time.csv";
        FileInputStream in;
        BufferedReader br;
        String str;
        String path = System.getProperty("user.dir")+name;
//        System.out.println(path);
        try {
            in = new FileInputStream(path);
            br = new BufferedReader(new InputStreamReader(in));
            int i=0;
            while ((str = br.readLine())!=null){
                personList.add(new Person(str,infectedNums));
                i++;
            }
            logger.info("读取人员数量："+i);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void compute(Boolean ifOperate){
        HashMap<Integer, Integer> infectedMaps = new HashMap<>();
        for (Person person : peopleInfected) {
            Integer location = person.getLocationList().get(turn);
            infectedMaps.merge(location, 1, Integer::sum);
        }
        for (Person person : personList) {
            Integer i = person.getLocationList().get(turn);
            if(infectedMaps.get(i)==null) {
                person.addScore(0,ifOperate);
            } else person.addScore(infectedMaps.get(i),ifOperate);
        }
    }

    public static void computeDay(){
        int[] ints = new int[6];
        for (Person person : personList) {
            if (person.isInfected()||person.isIfImmune()){;}
            else {
                double score = person.getScore();
                if(random.nextDouble()<score/(score+virus.getR0())){
                    person.setInfected(true);
                    double v = random.nextDouble();
                    if (v<virus.getSymptomRate().get(2)) person.setState(State.Danger);
                    else if (v<virus.getSymptomRate().get(1)) person.setState(State.Heavy);
                    else person.setState(State.Light);
                }
            }
            int i = person.writeDayResult();
            switch(i){
                case 0:ints[0]+=1;break;
                case 1:ints[1]+=1;break;
                case 2:ints[2]+=1;ints[5]+=1;break;
                case 3:ints[3]+=1;ints[5]+=1;break;
                case 4:ints[4]+=1;ints[5]+=1;break;
            }
        }
        dayResultMap.get(State.Immune).add(ints[0]+""); //State.Immune.toString()+":"+
        dayResultMap.get(State.Normal).add(""+ints[1]);
        dayResultMap.get(State.Light).add(""+ints[2]);
        dayResultMap.get(State.Heavy).add(""+ints[3]);
        dayResultMap.get(State.Danger).add(""+ints[4]);
        dayResultMap.get(State.Infected).add(""+ints[5]);
    }

    public static void writeData(){
        String filename = "/documents/resultData.csv";
        BufferedWriter bufferedWriter = null;
        String line;
        try {
            logger.info("写计算结果, 轮数："+Main.turn);
            String path = System.getProperty("user.dir")+filename;
            File file = new File(path);
            FileWriter writer = new FileWriter(file,false);
            bufferedWriter = new BufferedWriter(writer);
            for (Person person : personList) {
                String listStr = person.getScoreList().toString();
                line = person.getNum()+","+listStr.substring(1,listStr.length()-1);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        }catch (IOException e){
            logger.error("resultData.csv文件写出异常！");
            e.printStackTrace();
        }
    }

    public static void writeDataDay(){
        String filename = "/documents/dayResultData.csv";
        BufferedWriter bufferedWriter = null;
        String line;
        try {
            logger.info("写当天计算结果");
            String path = System.getProperty("user.dir")+filename;
            File file = new File(path);
            FileWriter writer = new FileWriter(file,false);
            bufferedWriter = new BufferedWriter(writer);
            for (Person person : personList) {
                line = person.getDayResult().toString();
                line = person.getNum()+","+line.substring(1, line.length()-1);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            String s1 = Main.dayResultMap.get(State.Immune).toString();
            bufferedWriter.write(State.Immune.getName()+","+s1.substring(1,s1.length()-1));
            bufferedWriter.newLine();

            String s2 = Main.dayResultMap.get(State.Normal).toString();
            bufferedWriter.write(State.Normal.getName()+","+s2.substring(1,s2.length()-1));
            bufferedWriter.newLine();

            String s3 = Main.dayResultMap.get(State.Light).toString();
            bufferedWriter.write(State.Light.getName()+","+s3.substring(1,s3.length()-1));
            bufferedWriter.newLine();

            String s4 = Main.dayResultMap.get(State.Heavy).toString();
            bufferedWriter.write(State.Heavy.getName()+","+s4.substring(1,s4.length()-1));
            bufferedWriter.newLine();

            String s5 = Main.dayResultMap.get(State.Danger).toString();
            bufferedWriter.write(State.Danger.getName()+","+s5.substring(1,s5.length()-1));
            bufferedWriter.newLine();

            String s6 = Main.dayResultMap.get(State.Infected).toString();
            bufferedWriter.write(State.Infected.getName()+","+s6.substring(1,s6.length()-1));
            bufferedWriter.newLine();

            bufferedWriter.flush();
        }catch (IOException e){
            logger.error("dayResultData.csv文件写出异常！");
            e.printStackTrace();
        }
    }

    public static void vaccinate(int i){
        if (i<=0) return;
        if (i>=personList.size()-peopleInfected.size()){
            for (Person person : personList) {
                person.setIfImmune(true);
            }
        }else{
            int temp=0;
            while(temp<i) {
                Person person = personList.get(random.nextInt(personList.size()));
                if (!person.isInfected()){
                    person.setIfImmune(true);
                    temp++;
                }
            }
        }
    }

}
