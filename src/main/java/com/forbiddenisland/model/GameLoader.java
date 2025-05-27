package com.forbiddenisland.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class GameLoader {

    /**
     * 从文件中加载游戏状态
     * @param filePath 保存文件的路径
     * @return 加载的游戏对象
     */
    public static Game loadGame(String filePath) {
        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (Game) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("加载游戏时出错: " + e.getMessage());
            return null;
        }
    }

}
