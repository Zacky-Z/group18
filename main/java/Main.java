package game;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        game.IslandMap map = new IslandMap();
        WaterMeter waterMeter = new WaterMeter();
        Scanner scanner = new Scanner(System.in);

        System.out.println("🔰 欢迎来到 Forbidden Island 测试系统");
        map.printMap();

        while (true) {
            System.out.println("\n操作：\n1. 查看水位\n2. 提升水位\n3. 查看地图\n0. 退出");
            System.out.print("选择操作: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> {
                    System.out.println("当前水位：" + waterMeter.getCurrentLevel());
                    System.out.println("当前每轮抽 Flood 卡数：" + waterMeter.getFloodCardCount());
                }
                case "2" -> waterMeter.rise();
                case "3" -> map.printMap();
                case "0" -> {
                    System.out.println("退出测试。");
                    return;
                }
                default -> System.out.println("无效输入");
            }
        }
    }
}