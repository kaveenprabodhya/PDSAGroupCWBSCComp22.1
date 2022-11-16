package bsccomp.group.maximumprofit;

import bsccomp.group.common.models.Item;

public class FindMaximumProfit {
    public static final int maximumWeight = 11;
    public static final int N = 11;
    private final int[][] k;
    private final Item[] items;

    public FindMaximumProfit() {
        this.k = new int[N][N];
        this.items = new Item[]{
                new Item("A", this.getRandomProfit(), this.getRandomWeight()),
                new Item("B", this.getRandomProfit(), this.getRandomWeight()),
                new Item("C", this.getRandomProfit(), this.getRandomWeight()),
                new Item("D", this.getRandomProfit(), this.getRandomWeight()),
                new Item("E", this.getRandomProfit(), this.getRandomWeight()),
                new Item("F", this.getRandomProfit(), this.getRandomWeight()),
                new Item("G", this.getRandomProfit(), this.getRandomWeight()),
                new Item("H", this.getRandomProfit(), this.getRandomWeight()),
                new Item("I", this.getRandomProfit(), this.getRandomWeight()),
                new Item("J", this.getRandomProfit(), this.getRandomWeight()),
        };
    }

    private int getRandomWeight() {
        return (int) (Math.random() * (10 - 5 + 1) + 1);
    }

    private int getRandomProfit() {
        return (int) (Math.random() * (10 - 1 + 1) + 1);
    }

    private int getMax(int a, int b) {
        return Math.max(a, b);
    }

    public void calMaximumProfit() {
        for (int i = 0; i < N; i++) {
            for (int w = 0; w < maximumWeight; w++) {
                if (i == 0 || maximumWeight == 0)
                    k[i][w] = 0;
                else if (items[i - 1].getWeight() <= w) {
                    k[i][w] = this.getMax(items[i - 1].getProfit() + k[i - 1][w - items[i - 1].getWeight()], k[i - 1][w]);
                } else {
                    k[i][w] = k[i - 1][w];
                }
            }
        }
    }

    public int[][] getK() {
        return k;
    }

    public void print() {
        System.out.print("\t\t\t\t\t\t\t\t");
        for (int i = 0; i < 11; i++) {
            System.out.print(i + "kg\t\t");
        }
        System.out.println();
        for (int i = 0; i < k.length; i++) {
            if (i > 0) {
                System.out.print(items[i - 1].getName() + "\t\t");
                System.out.print(items[i - 1].getWeight() + "\t\t");
                System.out.print(items[i - 1].getProfit() + "\t\t");
                System.out.print("->\t\t");
            }
            if (i == 0) {
                System.out.print("I\t\tW\t\tP\t\t\t\t");
            }
            for (int j = 0; j < k.length; j++) {
                System.out.print(k[i][j] + "\t\t");
            }
            System.out.println();
        }
    }
}
