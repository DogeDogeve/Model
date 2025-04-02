package com.InfectionModel.bean;

import com.InfectionModel.Main;
import com.sun.javafx.binding.StringFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Person {
    public static final double N95 = 0.9;
    public static final double MASK = 0.7;
    public Person(){}

    private int id;
    private String num="";
    private List<Integer> locationList = new ArrayList<Integer>();
    private double factor = 0;
    private double score = 0; // 被感因数
    private Enum<State> state = State.Normal;
    private boolean infected = false;
    private List<Double> scoreList = new ArrayList<Double>();
    private boolean ifImmune = false; // 是否免疫
    private List<String> dayResult = new ArrayList<>();

    /**
     * 计算感染后的状态概率
     */
    public void computeState(){
        double v = Main.random.nextDouble();
        if (v<Main.virus.getSymptomRate().get(2)) this.state = State.Danger;
        else if (v<Main.virus.getSymptomRate().get(1)) this.state = State.Heavy;
        else this.state = State.Light;
    }

    /**
     * 通过被感因子计算当天结束后是否感染
     * @param r0
     * @param speed
     */
    public void computeState(int r0, int speed){
        if (ifImmune) return;
        if (infected) return;
        double probability = speed * r0 * 1.5 / (score + r0);
//        if (probability>=1) probability=1;
        double v = Main.random.nextDouble();
        if (v<probability) {
            this.infected = true;
            computeState();
        }
    }

    public Person(String str, List<String> infectedNums){ // 读取csv行生成Person
        String[] s = str.split(",");
        num = s[0];
        for (int i = 1; i < s.length; i++) {
            locationList.add(new Integer(s[i]));
        }
        for (String infectedNum : infectedNums) {
            if (infectedNum.equals(num)) {
                infected = true;
                Main.peopleInfected.add(this);
                computeState();
            }
        }
        if (!infected&&Main.random.nextDouble()<Main.immune) ifImmune=true;
    }

    public int writeDayResult(){
        String s;
        score = Double.parseDouble(String.format("%.3f",score));
        s = this.state.toString()+score;
        if (infected) s = State.Infected.getName()+this.state.toString()+score;
        if (ifImmune) s = State.Immune.getName()+"0";
        dayResult.add(s);
        if (ifImmune) return 0;
        if (state.equals(State.Light)) return 2;
        if (state.equals(State.Heavy)) return 3;
        if (state.equals(State.Danger)) return 4;
        else return 1;
    }

    public void addScore(Integer i, boolean ifOperate){
        double addScore = 0;
        if (ifOperate){
            if (!Main.quarantine) addScore = Double.parseDouble(String.format("%.3f", Main.random.nextDouble() * i));

            List<String> n95List = Main.n95List;
            double maskNum=0;
            if (Main.masked) {
                maskNum=MASK;
                if (n95List.size() > 0) {
                    for (String s : n95List) {
                        if (this.num.startsWith(s)) {
                            maskNum=N95;
                        }
                    }
                }
            }
            addScore*=(1-maskNum);

            this.score -= Main.disinfect;
            if (this.score<0) this.score=0;
        }else {
            addScore = Double.parseDouble(String.format("%.3f", Main.random.nextDouble() * i));
        }
        if(addScore>0)this.score += addScore;
        scoreList.add(this.score);

        if (ifImmune) this.score=0;

    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", num='" + num + '\'' +
                ", locationList=" + locationList +
                ", factor=" + factor +
                ", score=" + score +
                ", state=" + state +
                ", injected=" + infected +
                ", scoreList=" + scoreList +
                '}';
    }

    public List<String> getDayResult() {
        return dayResult;
    }

    public void setDayResult(List<String> dayResult) {
        this.dayResult = dayResult;
    }

    public boolean isIfImmune() {
        return ifImmune;
    }

    public void setIfImmune(boolean ifImmune) {
        this.ifImmune = ifImmune;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public List<Integer> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Integer> locationList) {
        this.locationList = locationList;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Enum<State> getState() {
        return state;
    }

    public void setState(Enum<State> state) {
        this.state = state;
    }

    public boolean isInfected() {
        return infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    public List<Double> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Double> scoreList) {
        this.scoreList = scoreList;
    }
}
