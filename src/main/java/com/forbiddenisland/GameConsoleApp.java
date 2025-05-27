package com.forbiddenisland;

import com.forbiddenisland.model.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


/**
 * 控制台版本的禁闭岛游戏，用于测试游戏逻辑
 */
public class GameConsoleApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Game game = null;

        System.out.println("=== 禁闭岛游戏 (控制台版) ===");
        System.out.println("1. 开始新游戏");
        System.out.println("2. 加载存档");
        System.out.print("请选择: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        if (choice == 1) {
            System.out.println("初始化游戏...");

            // 创建2 - 4名玩家
            List<String> playerNames = Arrays.asList("玩家1", "玩家2", "玩家3", "玩家4");

            // 难度级别 (1 - 4): 新手、正常、精英、传奇
            int difficulty = 1; // 新手难度

            // 创建游戏实例
            game = new Game(playerNames, difficulty);
        } else if (choice == 2) {
            System.out.print("请输入存档文件的路径: ");
            String filePath = scanner.nextLine();
            game = GameLoader.loadGame(filePath);
            if (game == null) {
                System.out.println("加载存档失败，开始新游戏。");
                // 创建2 - 4名玩家
                List<String> playerNames = Arrays.asList("玩家1", "玩家2", "玩家3", "玩家4");
                // 难度级别 (1 - 4): 新手、正常、精英、传奇
                int difficulty = 1; // 新手难度
                game = new Game(playerNames, difficulty);
            }
        } else {
            System.out.println("无效的选择，开始新游戏。");
            // 创建2 - 4名玩家
            List<String> playerNames = Arrays.asList("玩家1", "玩家2", "玩家3", "玩家4");
            // 难度级别 (1 - 4): 新手、正常、精英、传奇
            int difficulty = 1; // 新手难度
            game = new Game(playerNames, difficulty);
        }

        System.out.println("\n游戏初始化完成!");
        System.out.println("按回车键继续...");
        waitForEnter();

        while (true) {
            System.out.println("\n1. 进行回合");
            System.out.println("2. 保存游戏");
            System.out.println("3. 退出游戏");
            System.out.print("请选择: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            if (choice == 1) {
                System.out.println("\n===== 回合 =====");
                game.runTurn();

                // 检查游戏是否结束
                if (game.checkGameOverConditions()) {
                    System.out.println("\n游戏结束 - 玩家失败!");
                    break;
                }

                if (game.checkWinConditions()) {
                    System.out.println("\n游戏结束 - 玩家胜利!");
                    break;
                }

                System.out.println("\n回合结束。按回车键继续...");
                waitForEnter();
            } else if (choice == 2) {
                System.out.print("请输入保存文件的路径: ");
                String filePath = scanner.nextLine();
                game.saveGame(filePath);
            } else if (choice == 3) {
                System.out.println("\n退出游戏。感谢游玩!");
                break;
            } else {
                System.out.println("无效的选择，请重新输入。");
            }
        }
    }

    private static void waitForEnter() {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}