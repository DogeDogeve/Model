package com.InfectionModel;

import com.InfectionModel.bean.Virus;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigHelper {
    private ConfigHelper() {
    }

    public static boolean operation(){
        Logger logger = Logger.getLogger(ConfigHelper.class);
        Properties prop = new Properties();
        String filename = "/config/config.properties";
        FileInputStream input = null;
        BufferedReader br = null;

        try {
            logger.info("开始读操作取配置");
            String path = System.getProperty("user.dir")+filename;
            input = new FileInputStream(path);
            br = new BufferedReader(new InputStreamReader(input));

            prop.load(br);
            Main.masked = prop.getProperty("masked").equals("y");
            Main.n95List = Arrays.asList(prop.getProperty("maskNum").split(","));
            Main.disinfect = Double.parseDouble(prop.getProperty("disinfect"));
            Main.vaccine = Integer.parseInt(prop.getProperty("vaccine"));
            Main.quarantine = prop.getProperty("quarantine").equals("y");

            return true;
        } catch (Exception ex) {
            logger.error("配置文件读取错误！" + ex);
            return false;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("IO流关闭失败" + e);
                }
            }
        }
    }

    public static boolean init() {
        Logger logger = Logger.getLogger(ConfigHelper.class);
        Properties prop = new Properties();
        String filename = "/config/config.properties";
        FileInputStream input = null;
        BufferedReader br = null;

        try {
            logger.info("开始读取配置");
            String path = System.getProperty("user.dir")+filename;
//            System.out.println(path);
            input = new FileInputStream(path);
            br = new BufferedReader(new InputStreamReader(input));

            prop.load(br);
            String info = prop.getProperty("info");
            String r0 = prop.getProperty("r0");
            String symptomRate = prop.getProperty("symptomRate");
            String deadRate = prop.getProperty("deadRate");
            String latentPeriod = prop.getProperty("latentPeriod");
            String transmissionWay = prop.getProperty("transmissionWay");
            String speed = prop.getProperty("speed");
            String infectedNum = prop.getProperty("infectedNum");
            String immune = prop.getProperty("immune");
            System.out.println(info + r0 + symptomRate + deadRate + latentPeriod + transmissionWay + speed +infectedNum);

            Main.virus.setInfo(info);
            Main.virus.setR0(new Integer(r0));
            Main.virus.setSymptomRate(stringToDoubleList(symptomRate));
            Main.virus.setDeadRate(new Double(deadRate));
            Main.virus.setLatentPeriod(new Integer(latentPeriod));
            Main.virus.setTransmissionWay(Arrays.asList(transmissionWay.split(",")));
            Main.speed = new Integer(speed);
            Main.infectedNums.addAll(Arrays.asList(infectedNum.split(",")));
            Main.immune = new Double(immune);

            return true;
        } catch (Exception ex) {
            logger.error("配置文件读取错误！" + ex);
            return false;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("IO流关闭失败" + e);
                }
            }
        }
    }

    public static List<Double> stringToDoubleList(String str){
        String[] split = str.split(",");
        ArrayList<Double> doubles = new ArrayList<Double>();
        for (String s : split) {
            doubles.add(new Double(s));
        }
        return doubles;
    }

        public static String codeStyle (File file){
            try {
                FileInputStream in = new FileInputStream(file);
                byte[] b = new byte[3];
                in.read(b);
                in.close();
                if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
                    return "utf8";
                } else {
                    return "gbk";
                }
            } catch (IOException e) {
                return "";
            }
        }
}
