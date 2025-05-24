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
        System.out.println("=== 禁闭岛游戏 (控制台版) ===");
        System.out.println("初始化游戏...");
        
        // 创建2-4名玩家
        List<String> playerNames = Arrays.asList("玩家1", "玩家2", "玩家3", "玩家4");
        
        // 难度级别 (1-4): 新手、正常、精英、传奇
        int difficulty = 1; // 新手难度
        
        // 创建游戏实例
        Game game = new Game(playerNames, difficulty);
        
        System.out.println("\n游戏初始化完成!");
        System.out.println("按回车键继续...");
        waitForEnter();
        
        // 模拟几个回合
        for (int i = 0; i < 3; i++) {
            System.out.println("\n===== 回合 " + (i+1) + " =====");
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
        }
        
        System.out.println("\n演示结束。感谢游玩!");
    }
    
    private static void waitForEnter() {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
} 