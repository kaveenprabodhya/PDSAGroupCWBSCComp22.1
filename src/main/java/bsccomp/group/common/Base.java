package bsccomp.group.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Base {
    public static final int maximumCities = 10;
    public static final int maximumDistance = 100;
    public static final int minimumDistance = 10;
    private final String[] cities;
    // used to allocate random select city only once
    private final ArrayList<String> tempCityList;
    // used to allocate random select city only once
    private final ArrayList<String> secondTempCityList;

    public Base() {
        // initializing arrays in constructor
        this.tempCityList = new ArrayList<>();
        this.cities = new String[]{"Colombo", "Negombo", "Galle", "Kandy", "Sri Jayawardhanapura",
                "Jaffna", "Nuwara Eliya", "Ratnapura", "Dambulla", "Matale"};
        secondTempCityList = new ArrayList<>();
    }

    // method for select random city from cities array
    private String selectRandomCity() {
        int index = (int) (Math.random() * maximumCities);
        return cities[index];
    }

    // method that only once return a random city
    protected String getRandomCity() {
        String randCity = this.selectRandomCity();
        if (this.tempCityList.isEmpty()) {
            this.tempCityList.add(randCity);
        } else {
            try {
                for (String tempCity : this.tempCityList) {
                    if (tempCity.equalsIgnoreCase(randCity)) {
                        randCity = this.getRandomCityExceptAlreadySelected(tempCityList);
                    }
                }
                this.tempCityList.add(randCity);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        System.out.println(randCity);
        return randCity;
    }

    // method for return random city except for the given city.
    protected String getRandomCity(String randomExceptCity) {
        String randomCity = this.selectRandomCity();
        if (randomCity.equalsIgnoreCase(randomExceptCity)) {
            randomCity = this.returnCityExceptGiven(randomCity ,randomExceptCity);
            if (this.secondTempCityList.isEmpty()) {
                this.secondTempCityList.add(randomCity);
            } else {
                try {
                    for (String tempCity : this.secondTempCityList) {
                        if (tempCity.equalsIgnoreCase(randomCity)) {
                            randomCity = this.getRandomCityExceptAlreadySelected(secondTempCityList);
                        }
                    }
                    this.secondTempCityList.add(randomCity);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return randomCity;
    }

    // method return random city which not selected yet out of 10.
    protected String getRandomCityExceptAlreadySelected(ArrayList<String> tempCityList) {
        boolean[] temps = new boolean[maximumCities];
        Queue<String> tempQueue = new LinkedList<>();
        Arrays.fill(temps, false);
        for (String tempCity : tempCityList) {
            for (int j = 0; j < cities.length; j++) {
                if (cities[j].equalsIgnoreCase(tempCity)) {
                    temps[j] = true;
                }
            }
        }
        for (int i = 0; i < temps.length; i++) {
            if (!temps[i]) {
                tempQueue.add(cities[i]);
            }
        }
        return tempQueue.poll();
    }

    // method for check given random city and generated random city equality.
    private String returnCityExceptGiven(String randomCity,String randomExceptCity) {
        while (randomCity.equalsIgnoreCase(randomExceptCity)) {
            randomCity = this.selectRandomCity();
        }
        return randomCity;
    }

    // method for clear temp list
    protected void clearTempCityList(){
        this.tempCityList.clear();
    }

    // method for clear temp list
    protected void clearSecondTempCityList(){
        this.secondTempCityList.clear();
    }

    // method generate random distance between 10-100
    protected Integer getRandomDistance() {
        return (int) (Math.random() * maximumDistance + minimumDistance);
    }
}
