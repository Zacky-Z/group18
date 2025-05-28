package com.forbiddenisland.ui;

import com.forbiddenisland.model.WaterMeter;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * 水位计可视化组件
 * 显示当前水位和对应的洪水牌抽取数量
 */
public class WaterMeterView extends VBox {
    
    private WaterMeter waterMeter;
    private Rectangle[] waterLevelRects;
    private Label waterLevelLabel;
    private Label cardsToDrawLabel;
    private StackPane waterContainer;
    private Rectangle waterFill;
    private int previousWaterLevel;
    
    // 水位计的最大级别
    private final int MAX_LEVELS = 10;
    
    // 水位计的颜色定义
    private final Color[] LEVEL_COLORS = {
        Color.rgb(173, 216, 230),  // 浅蓝色 - 1级
        Color.rgb(135, 206, 235),  // 天蓝色 - 2级
        Color.rgb(100, 149, 237),  // 矢车菊蓝 - 3级
        Color.rgb(65, 105, 225),   // 品蓝 - 4级
        Color.rgb(0, 0, 255),      // 蓝色 - 5级
        Color.rgb(0, 0, 205),      // 中蓝色 - 6级
        Color.rgb(0, 0, 139),      // 深蓝色 - 7级
        Color.rgb(25, 25, 112),    // 午夜蓝 - 8级
        Color.rgb(128, 0, 0),      // 栗色 - 9级
        Color.rgb(255, 0, 0)       // 红色 - 10级
    };
    
    /**
     * 构造函数
     * @param waterMeter 水位计模型
     */
    public WaterMeterView(WaterMeter waterMeter) {
        this.waterMeter = waterMeter;
        this.previousWaterLevel = waterMeter.getCurrentWaterLevel();
        
        setPadding(new Insets(15));
        setSpacing(10);
        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(
            Color.rgb(240, 240, 240), 
            new CornerRadii(10), 
            Insets.EMPTY
        )));
        
        setBorder(new Border(new BorderStroke(
            Color.GRAY, 
            BorderStrokeStyle.SOLID, 
            new CornerRadii(10), 
            new BorderWidths(2)
        )));
        
        setPrefWidth(200);
        
        createHeader();
        createWaterMeterVisual();
        createLabels();
        
        update();
    }
    
    /**
     * 创建水位计标题
     */
    private void createHeader() {
        Label titleLabel = new Label("水位计");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        getChildren().add(titleLabel);
    }
    
    /**
     * 创建水位计视觉组件
     */
    private void createWaterMeterVisual() {
        // 创建水位计容器
        waterContainer = new StackPane();
        waterContainer.setPrefSize(160, 300);
        waterContainer.setMaxSize(160, 300);
        waterContainer.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: white;");
        
        // 创建水位填充
        waterFill = new Rectangle(156, 0);
        waterFill.setFill(Color.BLUE);
        waterFill.setTranslateY(150); // 初始位置在底部
        
        // 创建水位刻度
        VBox levelsBox = new VBox(5);
        levelsBox.setPadding(new Insets(5));
        levelsBox.setAlignment(Pos.BOTTOM_CENTER);
        
        waterLevelRects = new Rectangle[MAX_LEVELS];
        
        for (int i = MAX_LEVELS - 1; i >= 0; i--) {
            HBox levelRow = new HBox(5);
            levelRow.setAlignment(Pos.CENTER_LEFT);
            
            Rectangle levelRect = new Rectangle(20, 20);
            levelRect.setFill(LEVEL_COLORS[i]);
            levelRect.setStroke(Color.BLACK);
            levelRect.setStrokeWidth(1);
            waterLevelRects[i] = levelRect;
            
            Text levelText = new Text(String.valueOf(i + 1));
            levelText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            
            Text cardsText = new Text("(" + getCardsForLevel(i + 1) + "张洪水牌)");
            cardsText.setFont(Font.font("Arial", 10));
            
            levelRow.getChildren().addAll(levelRect, levelText, cardsText);
            levelsBox.getChildren().add(levelRow);
        }
        
        waterContainer.getChildren().addAll(waterFill, levelsBox);
        getChildren().add(waterContainer);
    }
    
    /**
     * 获取特定级别需要抽取的洪水牌数量
     */
    private int getCardsForLevel(int level) {
        int[] cardLevels = {2, 2, 3, 3, 4, 4, 5, 5, 6, 10};
        if (level > 0 && level <= cardLevels.length) {
            return cardLevels[level - 1];
        }
        return 0;
    }
    
    /**
     * 创建标签
     */
    private void createLabels() {
        waterLevelLabel = new Label("当前水位: 1");
        waterLevelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        cardsToDrawLabel = new Label("抽取洪水牌: 2张");
        cardsToDrawLabel.setFont(Font.font("Arial", 14));
        
        getChildren().addAll(waterLevelLabel, cardsToDrawLabel);
    }
    
    /**
     * 更新水位计显示
     */
    public void update() {
        if (waterMeter == null) {
            System.err.println("WaterMeterView: waterMeter is null, cannot update");
            return;
        }
        
        int currentLevel = waterMeter.getCurrentWaterLevel();
        System.out.println("WaterMeterView: Updating water meter display. Current level: " + currentLevel + 
                           ", Previous level: " + previousWaterLevel);
        
        // 检查水位是否变化
        boolean waterLevelIncreased = currentLevel > previousWaterLevel;
        
        // 更新标签
        waterLevelLabel.setText("当前水位: " + currentLevel + " - " + waterMeter.getWaterLevelLabel());
        cardsToDrawLabel.setText("抽取洪水牌: " + waterMeter.getNumberOfFloodCardsToDraw() + "张");
        
        // 高亮当前水位
        for (int i = 0; i < MAX_LEVELS; i++) {
            if (i < currentLevel) {
                waterLevelRects[i].setOpacity(1.0);
                waterLevelRects[i].setStroke(Color.YELLOW);
                waterLevelRects[i].setStrokeWidth(2);
            } else {
                waterLevelRects[i].setOpacity(0.5);
                waterLevelRects[i].setStroke(Color.BLACK);
                waterLevelRects[i].setStrokeWidth(1);
            }
        }
        
        // 更新水位填充高度
        double fillHeight = (currentLevel * 1.0 / MAX_LEVELS) * 300;
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), waterFill);
        transition.setToY(150 - fillHeight/2);
        waterFill.setHeight(fillHeight);
        
        // 根据水位设置颜色
        waterFill.setFill(LEVEL_COLORS[currentLevel - 1]);
        
        // 如果水位危险，添加警告闪烁效果
        if (currentLevel >= 7) {
            FadeTransition fade = new FadeTransition(Duration.millis(500), waterLevelLabel);
            fade.setFromValue(1.0);
            fade.setToValue(0.3);
            fade.setCycleCount(6);
            fade.setAutoReverse(true);
            fade.play();
            
            waterLevelLabel.setTextFill(Color.RED);
        } else {
            waterLevelLabel.setTextFill(Color.BLACK);
        }
        
        // 如果水位上升，添加动画效果
        if (waterLevelIncreased) {
            System.out.println("WaterMeterView: Water level increased from " + previousWaterLevel + " to " + currentLevel);
            playWaterLevelIncreaseAnimation(currentLevel);
            
            // 显示水位上升警告
            if (currentLevel >= 7) {
                showHighWaterLevelWarning(currentLevel);
            }
        }
        
        transition.play();
        
        // 更新之前的水位
        previousWaterLevel = currentLevel;
    }
    
    /**
     * 播放水位上升动画
     */
    private void playWaterLevelIncreaseAnimation(int currentLevel) {
        // 对当前水位的矩形进行动画
        Rectangle currentLevelRect = waterLevelRects[currentLevel - 1];
        
        // 创建缩放动画
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), currentLevelRect);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.5);
        scaleTransition.setToY(1.5);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);
        
        // 创建颜色闪烁动画
        Timeline colorTimeline = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(currentLevelRect.fillProperty(), LEVEL_COLORS[currentLevel - 1])),
            new KeyFrame(Duration.millis(150), 
                new KeyValue(currentLevelRect.fillProperty(), Color.YELLOW)),
            new KeyFrame(Duration.millis(300), 
                new KeyValue(currentLevelRect.fillProperty(), LEVEL_COLORS[currentLevel - 1]))
        );
        colorTimeline.setCycleCount(2);
        
        // 并行运行两个动画
        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, colorTimeline);
        parallelTransition.play();
        
        // 水位填充的波动效果
        Timeline waterWaveTimeline = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(waterFill.heightProperty(), waterFill.getHeight())),
            new KeyFrame(Duration.millis(200), 
                new KeyValue(waterFill.heightProperty(), waterFill.getHeight() + 20)),
            new KeyFrame(Duration.millis(400), 
                new KeyValue(waterFill.heightProperty(), waterFill.getHeight() - 10)),
            new KeyFrame(Duration.millis(600), 
                new KeyValue(waterFill.heightProperty(), waterFill.getHeight()))
        );
        waterWaveTimeline.play();
    }
    
    /**
     * 显示高水位警告
     */
    private void showHighWaterLevelWarning(int currentLevel) {
        String warningMessage;
        
        if (currentLevel == 10) {
            warningMessage = "危险！水位已达最高点！游戏即将结束！";
        } else if (currentLevel == 9) {
            warningMessage = "警告！水位已达危险级别！每回合需抽取6张洪水牌！";
        } else if (currentLevel == 8) {
            warningMessage = "警告！水位已达警戒级别！每回合需抽取5张洪水牌！";
        } else if (currentLevel == 7) {
            warningMessage = "注意！水位上升到较高级别！每回合需抽取5张洪水牌！";
        } else {
            return; // 不显示警告
        }
        
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("水位警告");
        alert.setHeaderText("水位上升到" + currentLevel + "级！");
        alert.setContentText(warningMessage);
        alert.show();
    }
    
    /**
     * 设置水位计模型
     * @param waterMeter 新的水位计模型
     */
    public void setWaterMeter(WaterMeter waterMeter) {
        this.waterMeter = waterMeter;
        this.previousWaterLevel = waterMeter.getCurrentWaterLevel();
        update();
    }
} 