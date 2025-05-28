package com.forbiddenisland.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Manages the overall game state and logic for Forbidden Island.
 * 管理禁闭岛游戏的整体游戏状态和逻辑。
 */
public class Game implements Serializable {

    // Game Constants
    public static final int MAX_ACTIONS_PER_TURN = 3; // Added from local
    private static final int INITIAL_FLOOD_CARDS_DRAW = 6;
    private static final int INITIAL_TREASURE_CARDS_PER_PLAYER = 2;
    private static final int BOARD_DIMENSION = 6; // Max dimension for a 6x6 grid to hold the island

    // Island representation
    private List<IslandTile> allIslandTilesList; // Master list of all 24 tiles
    private Map<String, IslandTile> islandTileMap; // For quick lookup by name
    private IslandTile[][] gameBoard; // 6x6 grid representing the island layout

    // Players
    private List<Player> players;
    private int currentPlayerIndex;

    // Decks
    private Deck<Card> treasureDeck;
    private Deck<FloodCard> floodDeck;

    // Treasures
    private List<Treasure> treasures;

    // Water Level
    private WaterMeter waterMeter;

    private Random randomGenerator;

    // Action Point Management & Game Phase (Added from local)
    private int actionsRemainingInTurn;
    private GamePhase currentPhase;

    /**
     * Enum for game phases.
     */
    public enum GamePhase {
        ACTION_PHASE,
        DRAW_TREASURE_CARDS_PHASE,
        DRAW_FLOOD_CARDS_PHASE
    }

    /**
     * Constructor for Game.
     * @param playerNames List of names for the players.
     * @param startingWaterLevel The initial water level for the game.
     */
    public Game(List<String> playerNames, int startingWaterLevel) {
        this.randomGenerator = new Random();
        this.players = new ArrayList<>();
        for (String name : playerNames) {
            this.players.add(new Player(name));
        }
        this.currentPlayerIndex = 0; 

        initializeAllIslandTiles();
        setupIslandLayout();
        initializeTreasures();
        placeTreasuresOnTiles();
        initializeFloodDeck();
        initializeTreasureDeck();
        setupWaterMeter(startingWaterLevel);
        assignAdventurersAndStartingPositions();
        dealInitialTreasureCards();
        performInitialIslandFlooding();

        this.actionsRemainingInTurn = MAX_ACTIONS_PER_TURN; // Initialize actions
        this.currentPhase = GamePhase.ACTION_PHASE; // Start with action phase
    }

    /**
     * Constructor for Game with pre-selected adventurer roles.
     * @param playerNames List of names for the players.
     * @param startingWaterLevel The initial water level for the game.
     * @param selectedRoles List of pre-selected roles for the players.
     */
    public Game(List<String> playerNames, int startingWaterLevel, List<AdventurerRole> selectedRoles) {
        this.randomGenerator = new Random();
        this.players = new ArrayList<>();
        for (String name : playerNames) {
            this.players.add(new Player(name));
        }
        this.currentPlayerIndex = 0;

        initializeAllIslandTiles();
        setupIslandLayout();
        initializeTreasures();
        placeTreasuresOnTiles();
        initializeFloodDeck();
        initializeTreasureDeck();
        setupWaterMeter(startingWaterLevel);
        assignAdventurersAndStartingPositionsWithSelectedRoles(selectedRoles); // Uses selected roles
        dealInitialTreasureCards();
        performInitialIslandFlooding();

        this.actionsRemainingInTurn = MAX_ACTIONS_PER_TURN; // Initialize actions for the first player
        this.currentPhase = GamePhase.ACTION_PHASE; // Start with the action phase
    }

    /**
     * Initializes all 24 island tiles with their names and shuffles them.
     * 用名称初始化所有24个岛屿板块并将它们洗混。
     */
    private void initializeAllIslandTiles() {
        allIslandTilesList = new ArrayList<>();
        islandTileMap = new HashMap<>(); // Will be populated as tiles are placed on board

        String[] tileNames = {
            "Fools' Landing", "Whispering Garden", "Howling Garden", "Tidal Palace", "Coral Palace",
            "Temple of the Moon", "Temple of the Sun", "Cave of Embers", "Cave of Shadows",
            "Bronze Gate", "Copper Gate", "Gold Gate", "Iron Gate", "Silver Gate",
            "Observatory", "Phantom Rock", "Twilight Hollow", "Watchtower",
            "Breakers Bridge", "Cliffs of Abandon", "Crimson Forest", "Dunes of Deception",
            "Lost Lagoon", "Misty Marsh"
        };

        for (String name : tileNames) {
            allIslandTilesList.add(new IslandTile(name));
        }
        Collections.shuffle(allIslandTilesList, randomGenerator);
    }

    /**
     * Sets up the island layout on the 6x6 game board.
     * Rules: 4x4 square in center, 2 tiles next to middle tiles of each side.
     * 在6x6游戏棋盘上设置岛屿布局。
     * 规则：中心为4x4正方形，每边中间两个板块旁边各放置2个板块。
     */
    private void setupIslandLayout() {
        gameBoard = new IslandTile[BOARD_DIMENSION][BOARD_DIMENSION];
        islandTileMap.clear(); // Clear and repopulate from board placement

        // Tiles are pre-shuffled in allIslandTilesList
        final int[] tileIndex = {0}; // Use a final array to make it effectively final for lambda

        // Layout pattern (example indices for a 6x6 grid):
        // . . X X . .  (0,2) (0,3)
        // . X X X X .  (1,1) (1,2) (1,3) (1,4)
        // X X X X X X  (2,0) (2,1) (2,2) (2,3) (2,4) (2,5)
        // X X X X X X  (3,0) (3,1) (3,2) (3,3) (3,4) (3,5)
        // . X X X X .  (4,1) (4,2) (4,3) (4,4)
        // . . X X . .  (5,2) (5,3)

        // Helper to place a tile
        PlaceTileHelper place = (r, c) -> {
            if (tileIndex[0] < allIslandTilesList.size()) {
                IslandTile tile = allIslandTilesList.get(tileIndex[0]++);
                gameBoard[r][c] = tile;
                islandTileMap.put(tile.getName(), tile); // Populate map
            }
        };

        // Top and bottom "arms"
        place.tile(0, 2); place.tile(0, 3);
        place.tile(5, 2); place.tile(5, 3);

        // Second and second-to-last rows
        place.tile(1, 1); place.tile(1, 2); place.tile(1, 3); place.tile(1, 4);
        place.tile(4, 1); place.tile(4, 2); place.tile(4, 3); place.tile(4, 4);

        // Middle two full rows
        for (int c = 0; c < BOARD_DIMENSION; c++) {
            place.tile(2, c);
            place.tile(3, c);
        }
        
        if (tileIndex[0] != 24) {
            System.err.println("Error in island layout: Expected 24 tiles to be placed, but placed " + tileIndex[0] + " (岛屿布局错误：预期放置24个板块，但放置了 " + tileIndex[0] + " 个)");
        }
        System.out.println("Island layout created with " + islandTileMap.size() + " tiles on the board. (岛屿布局已创建，棋盘上有 " + islandTileMap.size() + " 个板块。)");
    }

    // Functional interface for the helper
    @FunctionalInterface
    interface PlaceTileHelper {
        void tile(int r, int c);
    }

    /**
     * Initializes the four treasures.
     * 初始化四个宝藏。
     */
    private void initializeTreasures() {
        treasures = new ArrayList<>();
        // Ensure tile names match exactly those in tileNames array
        treasures.add(new Treasure(TreasureType.THE_EARTH_STONE, List.of("Temple of the Moon", "Temple of the Sun")));
        treasures.add(new Treasure(TreasureType.THE_STATUE_OF_THE_WIND, List.of("Whispering Garden", "Howling Garden")));
        treasures.add(new Treasure(TreasureType.THE_CRYSTAL_OF_FIRE, List.of("Cave of Embers", "Cave of Shadows")));
        treasures.add(new Treasure(TreasureType.THE_OCEANS_CHALICE, List.of("Coral Palace", "Tidal Palace")));
    }

    /**
     * Associates treasures with their respective island tiles (marks IslandTile objects).
     * 将宝藏与其各自的岛屿板块关联（标记 IslandTile 对象）。
     */
    private void placeTreasuresOnTiles() {
        for (Treasure treasure : treasures) {
            for (String tileName : treasure.getIslandTileNames()) {
                IslandTile tile = islandTileMap.get(tileName); // Get tile from map (populated by setupIslandLayout)
                if (tile != null) {
                    tile.setAssociatedTreasure(treasure.getType());
                } else {
                    // This could happen if a treasure tile name is misspelled or not in the 24 standard tiles.
                    // 如果宝藏板块名称拼写错误或不在24个标准板块中，则可能发生这种情况。
                    System.err.println("Error: Tile '" + tileName + "' for treasure '" + treasure.getType() + "' not found on game board. (错误：棋盘上未找到宝藏 '" + treasure.getType() + "' 对应的板块 '" + tileName + "'。)");
                }
            }
        }
    }

    /**
     * Initializes the Flood Deck with one card for each island tile.
     * 用对应每个岛屿板块的一张卡牌初始化洪水牌堆。
     */
    private void initializeFloodDeck() {
        List<FloodCard> floodCards = new ArrayList<>();
        // Flood cards correspond to all 24 tiles, regardless of their position or if they are on board yet.
        // 洪水牌对应所有24个板块，无论其位置如何或是否已在棋盘上。
        for (IslandTile tile : allIslandTilesList) { // Iterate over the master list of 24 original tiles
            floodCards.add(new FloodCard(tile.getName()));
        }
        floodDeck = new Deck<>(floodCards);
    }

    /**
     * Initializes the Treasure Deck.
     * 初始化宝藏牌堆。
     */
    private void initializeTreasureDeck() {
        List<Card> treasureCards = new ArrayList<>();
        for (TreasureType type : TreasureType.values()) {
            for (int i = 0; i < 5; i++) {
                treasureCards.add(new TreasureCard(type.toString() + " Card " + (i + 1), type));
            }
        }
        for (int i = 0; i < 3; i++) { treasureCards.add(new HelicopterLiftCard()); }
        for (int i = 0; i < 2; i++) { treasureCards.add(new SandbagsCard()); }
        for (int i = 0; i < 3; i++) { treasureCards.add(new WatersRiseCard()); } 
        treasureDeck = new Deck<>(treasureCards);
    }
    
    /**
     * Sets up the water meter.
     * 设置水位计。
     * @param startingLevel Numerical starting level (1-4 for Novice-Legendary). (数字起始级别（新手-传奇为1-4）)
     */
    private void setupWaterMeter(int startingLevel) {
        // Standard meter has 10 levels of markings, 5th is often a "danger" and 10th is Skull.
        // Flood cards drawn: 2,2,3,3,4,4,5,5,6,DEATH (represented by a high number or specific handling)
        // 标准水位计有10个刻度标记，第5个通常是"危险"，第10个是骷髅头。
        // 抽取的洪水牌数量：2,2,3,3,4,4,5,5,6,死亡（用一个大数字或特殊处理表示）
        int[] cardsPerLevel = {2, 2, 3, 3, 4, 4, 5, 5, 6, 10}; // Index 0 for level 1, up to level 10 (index 9)
                                                            // 索引0对应1级，直到10级（索引9）
        int maxGameLevel = 10; // Level 10 is Skull & Crossbones
                                // 10级是骷髅与交叉骨
        
        // Ensure startingLevel is valid (1 to 4 usually for Novice, Normal, Elite, Legendary)
        // 确保 startingLevel 有效（通常新手、普通、精英、传奇为1到4）
        int validatedStart = Math.max(1, Math.min(startingLevel, 4)); // Cap between 1 and 4
        waterMeter = new WaterMeter(validatedStart, maxGameLevel, cardsPerLevel);
    }

    /**
     * Assigns adventurer roles and starting positions.
     * 分配探险家角色和起始位置。
     */
    private void assignAdventurersAndStartingPositions() {
        List<AdventurerRole> roles = new ArrayList<>(List.of(AdventurerRole.values()));
        Collections.shuffle(roles, randomGenerator);

        // Standard starting tiles for each role
        // 每个角色的标准起始板块
        Map<AdventurerRole, String> roleToStartingTileName = Map.of(
            AdventurerRole.PILOT, "Fools' Landing",       // Blue
            AdventurerRole.NAVIGATOR, "Gold Gate",        // Yellow
            AdventurerRole.MESSENGER, "Silver Gate",      // White/Grey
            AdventurerRole.DIVER, "Iron Gate",            // Black
            AdventurerRole.EXPLORER, "Copper Gate",       // Green
            AdventurerRole.ENGINEER, "Bronze Gate",       // Red
            AdventurerRole.ARCHAEOLOGIST, "Misty Marsh"   // Purple
        );
        Map<AdventurerRole, String> roleToPawnColor = Map.of(
            AdventurerRole.PILOT, "Blue", 
            AdventurerRole.NAVIGATOR, "Yellow",
            AdventurerRole.MESSENGER, "White", 
            AdventurerRole.DIVER, "Black",
            AdventurerRole.EXPLORER, "Green", 
            AdventurerRole.ENGINEER, "Red",
            AdventurerRole.ARCHAEOLOGIST, "Purple"
        );

        for (int i = 0; i < players.size(); i++) {
            if (i < roles.size()) {
                Player player = players.get(i);
                AdventurerRole assignedRole = roles.get(i);
                String startingTileName = roleToStartingTileName.get(assignedRole);
                String pawnColor = roleToPawnColor.get(assignedRole);

                IslandTile startingTile = islandTileMap.get(startingTileName);

                if (startingTile == null) {
                    System.err.println("CRITICAL Error: Starting tile '" + startingTileName + "' for " + assignedRole + " not found on the board! Game setup failed. (严重错误：棋盘上未找到角色 " + assignedRole + " 的起始板块 '" + startingTileName + "'！游戏设置失败。)");
                    // Attempt to find it in the general list if not on board (should not happen if layout is correct)
                    // 如果棋盘上没有，则尝试在通用列表中查找（如果布局正确则不应发生）
                    for(IslandTile t : allIslandTilesList) {
                        if(t.getName().equals(startingTileName)) {
                            startingTile = t;
                            System.err.println("Found starting tile '" + startingTileName + "' in master list, but it was not on gameBoard. Check layout. (在主列表中找到起始板块 '" + startingTileName + "'，但它不在 gameBoard 上。请检查布局。)");
                            break;
                        }
                    }
                    if (startingTile == null) { // Still not found
                         throw new IllegalStateException("Cannot assign starting tile " + startingTileName + ". (无法分配起始板块 " + startingTileName + "。)");
                    }
                }
                
                player.assignRoleAndPawn(assignedRole, startingTile, pawnColor);
                
                if (gameBoardContains(startingTile)) { // Make sure the instance is from the board
                    findTileOnBoard(startingTile.getName()).setStartingTileForPlayer(true);
                }

                System.out.println(player.getName() + " is the " + assignedRole.name() + 
                                   " (" + assignedRole.getDescription() + 
                                   ") starting at " + startingTile.getName() + 
                                   " with a " + pawnColor + " pawn.");
            }
        }
    }

    /**
     * Deals initial treasure cards.
     * 分发初始宝藏牌。
     */
    private void dealInitialTreasureCards() {
        for (Player player : players) {
            for (int i = 0; i < INITIAL_TREASURE_CARDS_PER_PLAYER; i++) {
                drawTreasureCardForPlayer(player, true); // isInitialDeal = true
            }
        }
    }

    /**
     * Helper to draw a treasure card for a player.
     * 帮助玩家抽取宝藏牌的辅助方法。
     */
    private void drawTreasureCardForPlayer(Player player, boolean isInitialDeal) {
        Card drawnCard = treasureDeck.drawCard();
        if (drawnCard == null) {
            System.err.println("Treasure deck empty during draw for " + player.getName() + " (宝藏牌堆在为 " + player.getName() + " 抽牌时已空)");
            return;
        }

        if (drawnCard instanceof WatersRiseCard) {
            System.out.println("Waters Rise! card drawn by " + player.getName() + ".");
            if (isInitialDeal) {
                System.out.println("During initial deal, reshuffling Waters Rise! back and drawing replacement.");
                List<Card> cardsToReinsert = new ArrayList<>();
                cardsToReinsert.add(drawnCard); 
                treasureDeck.addCardsToDrawPileTop(cardsToReinsert);
                treasureDeck.shuffleDrawPile();                      
                drawTreasureCardForPlayer(player, true);           
            } else {
                processWatersRiseCard((WatersRiseCard) drawnCard);
            }
        } else {
            player.addCardToHand(drawnCard);
            System.out.println(player.getName() + " draws: " + drawnCard.getName());
        }
    }

    /**
     * Performs initial island flooding.
     * 执行初始岛屿淹没。
     */
    private void performInitialIslandFlooding() {
        System.out.println("Initial island flooding: Drawing " + INITIAL_FLOOD_CARDS_DRAW + " flood cards.");
        for (int i = 0; i < INITIAL_FLOOD_CARDS_DRAW; i++) {
            FloodCard floodCard = floodDeck.drawCard();
            if (floodCard != null) {
                IslandTile tileToFlood = islandTileMap.get(floodCard.getIslandTileName());
                if (tileToFlood != null && gameBoardContains(tileToFlood) && !tileToFlood.isFlooded()) {
                    tileToFlood.flood(); // flood the instance on the board / in the map
                    System.out.println("Tile flooded: " + tileToFlood.getName());
                    floodDeck.discardCard(floodCard);
                } else if (tileToFlood != null && tileToFlood.isFlooded()) {
                    System.err.println("Warning: Tried to initially flood an already flooded tile: " + tileToFlood.getName());
                    floodDeck.discardCard(floodCard); // Still discard
                } else if (tileToFlood == null || !gameBoardContains(tileToFlood)) {
                     System.err.println("Error: Island tile for flood card '" + floodCard.getIslandTileName() + "' not found on board or in map.");
                     // If tile not on board (e.g. error in setup), card still discarded from draw pile.
                     // 如果板块不在棋盘上（例如设置错误），卡牌仍从摸牌堆中丢弃。
                     floodDeck.discardCard(floodCard); // Or remove from game if tile truly doesn't exist
                }
            } else {
                System.err.println("Flood deck ran out during initial flooding.");
                break;
            }
        }
    }

    /**
     * Processes a drawn Waters Rise! card.
     * 处理抽到的"洪水上涨！"牌。
     */
    private void processWatersRiseCard(WatersRiseCard card) {
        System.out.println("Processing WATERS RISE! card.");
        
        // 记录处理前的水位
        int oldLevel = waterMeter.getCurrentWaterLevel();
        
        // 增加水位
        waterMeter.increaseWaterLevel();

        int newLevel = waterMeter.getCurrentWaterLevel();
        System.out.println("Water level increased from: " + oldLevel + " to " + newLevel + " (" + waterMeter.getWaterLevelLabel() + ")");

        // 处理洪水弃牌堆
        List<FloodCard> floodDiscards = floodDeck.getDiscardPile();
        if (!floodDiscards.isEmpty()) {
            Collections.shuffle(floodDiscards, randomGenerator);
            floodDeck.clearDiscardPile();
            floodDeck.addCardsToDrawPileTop(floodDiscards); // As per rules: place on TOP
            System.out.println("Shuffled " + floodDiscards.size() + " flood cards from discard back onto the draw pile top.");
        } else {
            System.out.println("Flood discard pile was empty. No cards to shuffle back.");
        }
        
        // 弃掉洪水上涨卡牌
        treasureDeck.discardCard(card); // Discard the Waters Rise! card itself
        
        // 确保在处理完洪水上涨卡牌后，如果当前阶段是抽宝藏牌阶段，则转换到抽洪水牌阶段
        if (currentPhase == GamePhase.DRAW_TREASURE_CARDS_PHASE) {
            System.out.println("Moving to DRAW_FLOOD_CARDS_PHASE after Waters Rise card.");
            currentPhase = GamePhase.DRAW_FLOOD_CARDS_PHASE;
        }
        
        // 检查游戏结束条件
        checkGameOverConditions(); 
    }

    /**
     * Checks if an IslandTile object instance is currently part of the gameBoard.
     * 检查 IslandTile 对象实例当前是否是 gameBoard 的一部分。
     */
    private boolean gameBoardContains(IslandTile tile) {
        if (tile == null) return false;
        for (int r = 0; r < BOARD_DIMENSION; r++) {
            for (int c = 0; c < BOARD_DIMENSION; c++) {
                if (gameBoard[r][c] == tile) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Finds an IslandTile on the game board by its name.
     * 按名称在游戏棋盘上查找 IslandTile。
     * @param tileName The name of the tile. (板块的名称)
     * @return The IslandTile object from the board, or null if not found. (棋盘上的 IslandTile 对象，如果未找到则为 null)
     */
    private IslandTile findTileOnBoard(String tileName) {
        for (int r = 0; r < BOARD_DIMENSION; r++) {
            for (int c = 0; c < BOARD_DIMENSION; c++) {
                if (gameBoard[r][c] != null && gameBoard[r][c].getName().equals(tileName)) {
                    return gameBoard[r][c];
                }
            }
        }
        return null;
    }

    /**
     * Checks if game over conditions are met.
     * 检查游戏结束条件是否满足。
     * @return true if game is over, false otherwise
     */
    public boolean checkGameOverConditions() {
        if (waterMeter.hasReachedMaxLevel()) {
            System.out.println("GAME OVER: Water level maxed out!");
            return true;
        }
        if (islandTileMap.get("Fools' Landing") == null || findTileOnBoard("Fools' Landing") == null) {
            System.out.println("GAME OVER: Fools' Landing has sunk!");
            return true;
        }
        for (Treasure treasure : treasures) {
            if (!treasure.isCollected()) {
                int sunkTreasureTiles = 0;
                for (String tileName : treasure.getIslandTileNames()) {
                    if (islandTileMap.get(tileName) == null || findTileOnBoard(tileName) == null) {
                        sunkTreasureTiles++;
                    }
                }
                if (sunkTreasureTiles >= 2) {
                    System.out.println("GAME OVER: Both tiles for " + treasure.getType() + " sunk!");
                    return true;
                }
            }
        }
        return false;
    }

    // --- Game Flow Methods ---
    /**
     * Advances to the next player's turn.
     * 进入下一个玩家的回合。
     */
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        resetActions(); // Reset actions for the new turn
        getCurrentPlayer().resetTurnBasedAbilities(); // Reset abilities like Pilot's flight
        currentPhase = GamePhase.ACTION_PHASE; // Reset to action phase for the new player
        System.out.println("Next turn for player: " + getCurrentPlayer().getName() + " (下一回合玩家： " + getCurrentPlayer().getName() + ")");
    }

    /**
     * Current player draws treasure cards.
     * 当前玩家抽取宝藏牌。
     */
    public void playerDrawsTreasureCards() {
        Player currentPlayer = getCurrentPlayer();
        System.out.println(currentPlayer.getName() + " is drawing 2 treasure cards.");
        drawTreasureCardForPlayer(currentPlayer, false); // First draw
        checkHandLimit(currentPlayer);                 // Check limit after first draw
        if (treasureDeck.isDrawPileEmpty() && treasureDeck.getDiscardPileSize() > 0) {
            System.out.println("Treasure draw pile empty, reshuffling discard pile.");
            treasureDeck.reshuffleDiscardIntoDraw();
        }
        drawTreasureCardForPlayer(currentPlayer, false); // Second draw
        checkHandLimit(currentPlayer);                 // Check limit after second draw
    }
    
    /**
     * Checks if a player has exceeded their hand limit and handles discarding.
     * 检查玩家是否超过了手牌上限并处理弃牌。
     */
    private void checkHandLimit(Player player) {
        while (player.isHandOverLimit()) {
            System.out.println(player.getName() + "'s hand is over limit (" + player.getHand().size() + "/" + Player.MAX_HAND_SIZE + "). Must discard or play.");
            Card cardToDiscard = null;
            // Simple auto-discard: non-special first, then any. UI would allow choice.
            // 简单自动弃牌：首先非特殊牌，然后任何牌。UI将允许选择。
            for (Card c : player.getHand()) { if (!(c instanceof SpecialActionCard)) cardToDiscard = c; }
            if (cardToDiscard == null && !player.getHand().isEmpty()) cardToDiscard = player.getHand().get(player.getHand().size() - 1);

            if (cardToDiscard != null) {
                // TODO: Allow playing special action card before discarding
                // 待办：允许在丢弃前打出特殊行动牌
                player.removeCardFromHand(cardToDiscard);
                // No need to cast to TreasureCard, Deck<Card> can take any Card
                treasureDeck.discardCard(cardToDiscard); 
                System.out.println(player.getName() + " auto-discarded: " + cardToDiscard.getName());
            } else break; 
        }
    }
    
    /**
     * Checks if win conditions are met.
     * 检查游戏胜利条件是否满足。
     * @return true if game is won, false otherwise
     */
    public boolean checkWinConditions() {
        boolean allTreasuresCollected = treasures.stream().allMatch(Treasure::isCollected);
        if (!allTreasuresCollected) return false;

        IslandTile foolsLanding = islandTileMap.get("Fools' Landing");
        if (foolsLanding == null || findTileOnBoard("Fools' Landing") == null) return false; 

        for (Player player : players) {
            if (player.getPawn().getCurrentLocation() != foolsLanding) {
                return false;
            }
        }
        // Final step is playing Helicopter Lift, this method just checks pre-conditions.
        // 最后一步是打出直升机升空牌，此方法仅检查先决条件。
        System.out.println("All treasures collected, all players at Fools' Landing. Helicopter Lift needed to win.");
        return true;
    }
    
    // --- Getters for game state ---
    /**
     * Gets the game board.
     */
    public IslandTile[][] getGameBoard() { return gameBoard; }
    
    /**
     * Gets an island tile by name.
     */
    public IslandTile getIslandTileByName(String name) { return islandTileMap.get(name); }
    
    /**
     * Gets the list of players.
     */
    public List<Player> getPlayers() { return Collections.unmodifiableList(players); }
    
    /**
     * Gets the current player.
     */
    public Player getCurrentPlayer() { return players.get(currentPlayerIndex); }
    
    /**
     * Gets the water meter.
     */
    public WaterMeter getWaterMeter() { return waterMeter; }
    
    /**
     * Gets the list of treasures.
     */
    public List<Treasure> getTreasures() { return Collections.unmodifiableList(treasures); }

    // --- Action Point Management (Added from local) ---
    /**
     * Gets the number of actions remaining for the current player in their turn.
     * 获取当前玩家在本回合剩余的行动点数。
     * @return Number of actions remaining. (剩余的行动点数)
     */
    public int getActionsRemainingInTurn() {
        return actionsRemainingInTurn;
    }

    /**
     * Sets the number of actions remaining for the current player in their turn.
     * 设置当前玩家在本回合剩余的行动点数。
     * @param actions Number of actions to set. (要设置的行动点数)
     */
    public void setActionsRemainingInTurn(int actions) {
        this.actionsRemainingInTurn = Math.max(0, actions); // Ensure non-negative
    }

    /**
     * Resets the number of actions for the current player to the maximum allowed.
     * 将当前玩家的行动点数重置为最大允许值。
     */
    public void resetActions() {
        this.actionsRemainingInTurn = MAX_ACTIONS_PER_TURN;
    }

    /**
     * Attempts to spend an action point. Decrements remaining actions if available.
     * 尝试消耗一个行动点。如果可用，则减少剩余行动点数。
     * @return true if an action point was spent, false if no actions were remaining. (如果消耗了一个行动点则返回 true，如果没有剩余行动点则返回 false)
     */
    public boolean spendAction() {
        if (actionsRemainingInTurn > 0) {
            actionsRemainingInTurn--;
            System.out.println("Action spent. Remaining actions: " + actionsRemainingInTurn);
            return true;
        }
        System.out.println("No actions remaining to spend.");
        return false;
    }

    /**
     * Adds actions to the current player's remaining action points.
     * 向当前玩家的剩余行动点添加行动点。
     * @param actionsToAdd Number of actions to add. (要添加的行动点数)
     */
    public void addActions(int actionsToAdd) {
        if (actionsToAdd > 0) {
            actionsRemainingInTurn += actionsToAdd;
            System.out.println("Added " + actionsToAdd + " action(s). New total: " + actionsRemainingInTurn);
        }
    }

    /**
     * Current player draws flood cards according to the water meter level.
     * 当前玩家根据水位计等级抽取洪水牌。
     */
    public void playerDrawsFloodCards_REVISED() {
        Player currentPlayer = getCurrentPlayer();
        int numToDraw = waterMeter.getNumberOfFloodCardsToDraw();
        System.out.println(currentPlayer.getName() + " is drawing " + numToDraw + " flood cards (Water Level: " + waterMeter.getCurrentWaterLevel() + ").");

        for (int i = 0; i < numToDraw; i++) {
            if (checkGameOverConditions()) return;

            if (floodDeck.isDrawPileEmpty() && floodDeck.getDiscardPileSize() > 0) {
                System.out.println("Flood draw pile empty, reshuffling discard pile.");
                floodDeck.reshuffleDiscardIntoDraw();
            }
            FloodCard floodCard = floodDeck.drawCard();
            if (floodCard == null) {
                System.err.println("Flood deck empty.");
                break; 
            }

            System.out.println(currentPlayer.getName() + " drew flood card: " + floodCard.getName());
            IslandTile tileToProcess = islandTileMap.get(floodCard.getIslandTileName()); // Get from map

            if (tileToProcess != null) { // Tile exists in the map (i.e. not yet permanently removed due to sinking for treasure)
                int[] coords = getTileCoordinates(tileToProcess); // Get its current board coordinates

                if (coords == null) { // Tile is in map, but somehow not on gameBoard (should not happen if map is synced with board)
                    System.err.println("Tile " + tileToProcess.getName() + " in map but not on board. Card " + floodCard.getName() + " discarded.");
                    floodDeck.discardCard(floodCard); // Or remove from game if tile really gone
                    continue;
                }

                if (tileToProcess.isFlooded()) {
                    System.out.println("Tile " + tileToProcess.getName() + " at (" + coords[0] + "," + coords[1] + ") was already flooded. It sinks!");
                    removeIslandTileFromBoard(tileToProcess); 
                    // Check for pawns AFTER removal, passing original coords
                    checkPawnsOnSinkingTiles(tileToProcess, coords[0], coords[1]); 
                    // Flood card for removed tile is NOT discarded, it's out of play.
                } else {
                    System.out.println("Tile " + tileToProcess.getName() + " at (" + coords[0] + "," + coords[1] + ") is now flooded.");
                    tileToProcess.flood();
                    floodDeck.discardCard(floodCard);
                }
            } else {
                // Flood card for a tile that has already permanently sunk (e.g. treasure related loss condition)
                // or an invalid tile name on card.
                System.out.println("Flood card " + floodCard.getName() + " targets a tile not in the current game map (likely already sunk or invalid). Card removed from play.");
                // Card is out of play.
            }
            if (checkGameOverConditions()) return;
        }
    }
    
    /**
     * Removes an island tile from the game board and the lookup map.
     * 从游戏棋盘和查找映射中移除一个岛屿板块。
     * @param tile The tile to remove. (要移除的板块)
     */
    private void removeIslandTileFromBoard(IslandTile tile) {
        if (tile == null) return;
        System.out.println("REMOVING TILE: " + tile.getName());
        
        boolean foundAndRemoved = false;
        for (int r = 0; r < BOARD_DIMENSION; r++) {
            for (int c = 0; c < BOARD_DIMENSION; c++) {
                if (gameBoard[r][c] == tile) {
                    gameBoard[r][c] = null; // Remove from board
                    foundAndRemoved = true;
                    break;
                }
            }
            if (foundAndRemoved) break;
        }
        if (foundAndRemoved) {
             islandTileMap.remove(tile.getName()); // Remove from quick lookup
        } else {
            System.err.println("Attempted to remove tile " + tile.getName() + " but it was not found on the gameBoard array.");
            // 尝试移除板块 [板块名称]，但在 gameBoard 数组中未找到它。
            // Still remove from map if it exists there, to be safe.
            // 为安全起见，如果它存在于映射中，则仍从映射中移除。
            if (islandTileMap.containsKey(tile.getName())) {
                islandTileMap.remove(tile.getName());
            }
        }
    }
    
    /**
     * Checks if pawns are on a sinking tile and handles moving them.
     * 检查棋子是否在一个正在沉没的板块上并处理移动它们。
     */
    private void checkPawnsOnSinkingTiles(IslandTile justSunkTile, int r_sunk, int c_sunk) {
        List<Player> playersToCheck = new ArrayList<>(players);
        for (Player player : playersToCheck) {
            Pawn pawn = player.getPawn();
            if (pawn.getCurrentLocation() == justSunkTile) { // Pawn was on the tile that just sank
                System.out.println("Pawn of " + player.getName() + " (" + player.getRole().getChineseName() + 
                                   ") is on the just sunken tile " + justSunkTile.getName() + " (was at " + r_sunk + "," + c_sunk + ") and must swim!");
                boolean swamSafely = attemptSwim(player, justSunkTile, r_sunk, c_sunk); // Use overloaded version
                if (!swamSafely) {
                    System.out.println(player.getName() + " could not swim to safety! GAME OVER.");
                    while(!waterMeter.hasReachedMaxLevel()) waterMeter.increaseWaterLevel();
                    return; 
                }
            }
        }
    }

    /**
     * Gets the coordinates (row, col) of a given IslandTile on the game board.
     * 获取给定 IslandTile 在游戏棋盘上的坐标（行，列）。
     * @param tile The tile to find. (要查找的板块)
     * @return An int array [row, col], or null if not found. ([行, 列] 整数数组，如果未找到则为 null)
     */
    public int[] getTileCoordinates(IslandTile tile) {
        if (tile == null) return null;
        for (int r = 0; r < BOARD_DIMENSION; r++) {
            for (int c = 0; c < BOARD_DIMENSION; c++) {
                if (gameBoard[r][c] == tile) {
                    return new int[]{r, c};
                }
            }
        }
        return null; // Should not happen if tile is on board
    }
    
    /**
     * Checks if given coordinates are within the board dimensions.
     * 检查给定坐标是否在棋盘维度内。
     */
    public boolean isValidBoardCoordinate(int r, int c) {
        return r >= 0 && r < BOARD_DIMENSION && c >= 0 && c < BOARD_DIMENSION;
    }

    /**
     * Attempts to move a player's pawn from a sunken tile to a safe tile.
     * 尝试将玩家的棋子从沉没的板块移动到安全的板块。
     * @param player The player whose pawn needs to move
     * @param sunkenTile The tile that just sank
     * @param r Original row of the sunken tile
     * @param c Original column of the sunken tile
     * @return true if the player swam safely, false if it was impossible
     */
    private boolean attemptSwim(Player player, IslandTile sunkenTile, int r, int c) {
        System.out.println("Attempting swim for " + player.getName() + " from " + sunkenTile.getName() + " (originally at " + r + "," + c + ")");
        AdventurerRole role = player.getRole();
        List<IslandTile> swimOptions = new ArrayList<>();

        if (role == AdventurerRole.PILOT) {
            // Pilot can fly to ANY tile remaining on the board
            // 飞行员可以飞到棋盘上任何剩余的板块
            for (IslandTile tile : islandTileMap.values()) {
                swimOptions.add(tile);
            }
        } else {
            boolean allowDiagonal = (role == AdventurerRole.EXPLORER || role == AdventurerRole.DIVER); // Diver also has special movement
                                                                                                    // 潜水员也有特殊移动
            // Get potential neighbors based on original coordinates r, c
            // 根据原始坐标 r, c 获取潜在邻居
            int[] dr = {-1, 1, 0, 0, -1, -1, 1, 1}; // Orthogonal + Diagonal
            int[] dc = {0, 0, -1, 1, -1, 1, -1, 1}; // Orthogonal + Diagonal
            int limit = allowDiagonal ? 8 : 4;

            for (int i = 0; i < limit; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];
                if (isValidBoardCoordinate(nr, nc) && gameBoard[nr][nc] != null) {
                    IslandTile potentialNeighbor = gameBoard[nr][nc];
                    // We need to check if this potential neighbor is *still on the board* (i.e., in islandTileMap)
                    // 我们需要检查这个潜在的邻居是否*仍在棋盘上*（即在 islandTileMap 中）
                    if (potentialNeighbor != null && islandTileMap.containsKey(potentialNeighbor.getName())) {
                        swimOptions.add(potentialNeighbor);
                    }
                }
            }

            if (role == AdventurerRole.DIVER && swimOptions.isEmpty()) {
                // Diver can swim to NEAREST tile if no adjacent. This is complex (BFS).
                // Placeholder: If no direct adjacent, Diver fails for now, or broadens search to all tiles.
                // 如果没有直接相邻的板块，潜水员目前失败，或将搜索范围扩大到所有板块。
                System.out.println("Diver " + player.getName() + " found no direct swim spots. True Diver logic (BFS to nearest) not implemented.");
                 // Fallback: Diver can go to any tile as a simplified 'nearest'
                 // 后备：潜水员可以去任何板块作为简化的"最近"
                for (IslandTile tile : islandTileMap.values()) {
                    if (tile != sunkenTile) swimOptions.add(tile); // Add any other tile
                }
            }
        }

        if (!swimOptions.isEmpty()) {
            // Player chooses or pick one randomly/first
            // 玩家选择或随机/第一个选择
            IslandTile destination = swimOptions.get(randomGenerator.nextInt(swimOptions.size()));
            player.getPawn().setCurrentLocation(destination);
            System.out.println(player.getName() + " (" + role.getChineseName() + ") swam to " + destination.getName());
            return true;
        }
        
        System.out.println(player.getName() + " (" + role.getChineseName() + ") found no valid tile to swim to from (" + r + "," + c + ").");
        return false;
    }
    
    /**
     * Gets a list of available (not null, not flooded, not sunk) adjacent tiles to a given coordinate.
     * 获取给定坐标的可用（非空、未淹没、未沉没）相邻板块列表。
     * @param r row (行)
     * @param c column (列)
     * @param includeDiagonals whether to include diagonal tiles (是否包括对角线板块)
     * @return List of valid adjacent IslandTile objects. (有效相邻 IslandTile 对象列表)
     */
    public List<IslandTile> getValidAdjacentTiles(int r, int c, boolean includeDiagonals) {
        List<IslandTile> adjacentTiles = new ArrayList<>();
        int[] dr = {-1, 1, 0, 0}; // Changes in row for Up, Down
        int[] dc = {0, 0, -1, 1}; // Changes in col for Left, Right

        for (int i = 0; i < 4; i++) { // Orthogonal first
            int nr = r + dr[i];
            int nc = c + dc[i];
            if (isValidBoardCoordinate(nr, nc) && gameBoard[nr][nc] != null) {
                adjacentTiles.add(gameBoard[nr][nc]);
            }
        }

        if (includeDiagonals) {
            int[] ddr = {-1, -1, 1, 1}; // Diagonal rows
            int[] ddc = {-1, 1, -1, 1}; // Diagonal columns
            for (int i = 0; i < 4; i++) {
                int nr = r + ddr[i];
                int nc = c + ddc[i];
                if (isValidBoardCoordinate(nr, nc) && gameBoard[nr][nc] != null) {
                    adjacentTiles.add(gameBoard[nr][nc]);
                }
            }
        }
        return adjacentTiles;
    }
    
    /**
     * Runs one complete player's turn.
     * 运行一个完整的玩家回合。
     */
    public void runTurn() {
        if (checkGameOverConditions() || checkWinConditions()) { // Check win if Helicopter played as action
            // 检查是否因行动打出直升机升空牌而获胜
            System.out.println("Game has ended.");
            return;
        }

        Player turnPlayer = getCurrentPlayer();
        System.out.println("Player " + turnPlayer.getName() + " takes up to 3 actions.");
        // UI would handle action choices. For now, simulate or skip.
        // UI将处理行动选择。目前，模拟或跳过。
        // e.g., turnPlayer.takeActions(this);

        System.out.println(turnPlayer.getName() + " draws 2 Treasure Cards.");
        playerDrawsTreasureCards();
        if (checkGameOverConditions()) { System.out.println("Game Over after treasure draw."); return; }

        System.out.println(turnPlayer.getName() + " draws Flood Cards.");
        playerDrawsFloodCards_REVISED(); 
        if (checkGameOverConditions()) { System.out.println("Game Over after flood draw."); return; }

        nextTurn();
    }
    public void saveGame(String filePath) {
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(this);
        } catch (IOException e) {
            System.err.println("保存游戏时出错: " + e.getMessage());
        }
    }
    public void endTurn() {
        // 实现结束回合的逻辑，例如切换到下一个玩家
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        resetActions(); // Also reset actions here as it's part of ending a turn in some contexts
        getCurrentPlayer().resetTurnBasedAbilities();
        currentPhase = GamePhase.ACTION_PHASE;
        System.out.println("--- It is now " + getCurrentPlayer().getName() + "'s turn (" + getCurrentPlayer().getRole().getChineseName() + ") ---");
    }

    // GamePhase Getters/Setters (Added from local)
    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(GamePhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    /**
     * Assigns pre-selected adventurer roles to players and places them on their starting positions.
     * 为玩家分配预选的冒险家角色，并将他们放置在起始位置。
     * @param selectedRoles List of pre-selected roles for the players.
     */
    private void assignAdventurersAndStartingPositionsWithSelectedRoles(List<AdventurerRole> selectedRoles) {
        Map<AdventurerRole, String> roleToStartingTileName = Map.of(
            AdventurerRole.PILOT, "Fools' Landing",
            AdventurerRole.NAVIGATOR, "Gold Gate",
            AdventurerRole.MESSENGER, "Silver Gate",
            AdventurerRole.DIVER, "Iron Gate",
            AdventurerRole.EXPLORER, "Copper Gate",
            AdventurerRole.ENGINEER, "Bronze Gate",
            AdventurerRole.ARCHAEOLOGIST, "Misty Marsh"
        );
        Map<AdventurerRole, String> roleToPawnColor = Map.of(
            AdventurerRole.PILOT, "Blue", 
            AdventurerRole.NAVIGATOR, "Yellow",
            AdventurerRole.MESSENGER, "White", 
            AdventurerRole.DIVER, "Black",
            AdventurerRole.EXPLORER, "Green", 
            AdventurerRole.ENGINEER, "Red",
            AdventurerRole.ARCHAEOLOGIST, "Purple"
        );

        for (int i = 0; i < players.size() && i < selectedRoles.size(); i++) {
            Player player = players.get(i);
            AdventurerRole assignedRole = selectedRoles.get(i);
            String startingTileName = roleToStartingTileName.get(assignedRole);
            String pawnColor = roleToPawnColor.get(assignedRole);
            IslandTile startingTile = islandTileMap.get(startingTileName);

            if (startingTile == null) {
                System.err.println("CRITICAL Error: Starting tile '" + startingTileName + "' for " + assignedRole + " not found on the board! Game setup failed.");
                for(IslandTile t : allIslandTilesList) {
                    if(t.getName().equals(startingTileName)) {
                        startingTile = t;
                        System.err.println("Found starting tile '" + startingTileName + "' in master list, but it was not on gameBoard. Check layout.");
                        break;
                    }
                }
                if (startingTile == null) { 
                     throw new IllegalStateException("Cannot assign starting tile " + startingTileName + ".");
                }
            }
            
            player.assignRoleAndPawn(assignedRole, startingTile, pawnColor);
            player.resetTurnBasedAbilities(); // Initialize turn-based abilities state
            
            if (gameBoardContains(startingTile)) { 
                findTileOnBoard(startingTile.getName()).setStartingTileForPlayer(true);
            }

            System.out.println(player.getName() + " is the " + assignedRole.name() + 
                               " (" + assignedRole.getDescription() + 
                               ") starting at " + startingTile.getName() + 
                               " with a " + pawnColor + " pawn.");
        }
        this.actionsRemainingInTurn = MAX_ACTIONS_PER_TURN; // Initialize for the first player
    }

    public Deck<Card> getTreasureDeck() { return treasureDeck; }
}
 