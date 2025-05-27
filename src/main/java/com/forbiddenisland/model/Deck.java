package com.forbiddenisland.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Represents a deck of cards in the game.
 * 代表游戏中的一副牌堆。
 * @param <T> The type of cards in the deck, must extend Card.
 *           牌堆中卡牌的类型，必须继承自 Card。
 */
public class Deck<T extends Card> {
    private Stack<T> drawPile;    // The pile of cards to draw from (摸牌堆)
    private List<T> discardPile; // The pile of discarded cards (弃牌堆)

    /**
     * Constructor for Deck.
     * Deck 的构造函数。
     * Initializes with a list of cards that form the initial draw pile.
     * 使用构成初始摸牌堆的卡牌列表进行初始化。
     * @param initialCards The list of cards to start the deck with. (用于开始牌堆的卡牌列表)
     */
    public Deck(List<T> initialCards) {
        this.drawPile = new Stack<>();
        this.discardPile = new ArrayList<>();
        if (initialCards != null) {
            this.drawPile.addAll(initialCards);
        }
        shuffleDrawPile();
    }

    /**
     * Shuffles the draw pile.
     * 洗混摸牌堆。
     */
    public void shuffleDrawPile() {
        Collections.shuffle(this.drawPile);
    }

    /**
     * Draws a card from the top of the draw pile.
     * 从摸牌堆顶部摸一张牌。
     * If the draw pile is empty, it attempts to reshuffle the discard pile into the draw pile.
     * 如果摸牌堆为空，它会尝试将弃牌堆重新洗入摸牌堆。
     * @return The card drawn, or null if no cards are available. (摸到的牌，如果无牌可摸则为 null)
     */
    public T drawCard() {
        if (drawPile.isEmpty()) {
            if (discardPile.isEmpty()) {
                return null; // No cards left anywhere (任何地方都没有牌了)
            }
            reshuffleDiscardIntoDraw();
            if (drawPile.isEmpty()) { // Still empty after reshuffle (e.g. Waters Rise with empty discard)
                return null;          // 重洗后仍然为空（例如，弃牌堆为空时触发洪水上涨）
            }
        }
        return drawPile.pop();
    }

    /**
     * Adds a card to the discard pile.
     * 将一张牌加入弃牌堆。
     * @param card The card to discard. (要弃掉的牌)
     */
    public void discardCard(T card) {
        if (card != null) {
            discardPile.add(card);
        }
    }

    /**
     * Adds a list of cards to the top of the draw pile.
     * Used for "Waters Rise!" where discard pile is shuffled and placed on top.
     * 将一个卡牌列表添加到摸牌堆顶部。
     * 用于"洪水上涨！"效果，弃牌堆被洗混并放在顶部。
     * @param cards The list of cards to add. (要添加的卡牌列表)
     */
    public void addCardsToDrawPileTop(List<T> cards) {
        if (cards != null) {
            // Add in reverse order to maintain the order of the list when popping from stack
            // 逆序添加，以便从堆栈中弹出时保持列表的顺序
            for (int i = cards.size() - 1; i >= 0; i--) {
                drawPile.push(cards.get(i));
            }
        }
    }

    /**
     * Takes all cards from the discard pile, shuffles them, and places them into the draw pile.
     * The discard pile becomes empty.
     * 从弃牌堆中取出所有牌，洗混它们，然后将它们放入摸牌堆。
     * 弃牌堆变为空。
     */
    public void reshuffleDiscardIntoDraw() {
        if (!discardPile.isEmpty()) {
            Collections.shuffle(discardPile);
            drawPile.addAll(discardPile); // Add to bottom, then shuffle ensures randomness with existing cards
            discardPile.clear();
            shuffleDrawPile(); // Shuffle the whole draw pile again for good measure
                               // 再次洗混整个摸牌堆以确保随机性
        }
    }

    /**
     * Gets the current discard pile.
     * 获取当前的弃牌堆。
     * @return A list of cards in the discard pile. (弃牌堆中的卡牌列表)
     */
    public List<T> getDiscardPile() {
        return new ArrayList<>(discardPile); // Return a copy to prevent external modification
                                            // 返回一个副本以防止外部修改
    }

    /**
     * Clears the discard pile.
     * 清空弃牌堆。
     */
    public void clearDiscardPile() {
        discardPile.clear();
    }

    /**
     * Checks if the draw pile is empty.
     * 检查摸牌堆是否为空。
     * @return true if the draw pile is empty, false otherwise. (如果摸牌堆为空则为 true，否则为 false)
     */
    public boolean isDrawPileEmpty() {
        return drawPile.isEmpty();
    }

    /**
     * Gets the number of cards remaining in the draw pile.
     * 获取摸牌堆中剩余的卡牌数量。
     * @return The number of cards in the draw pile. (摸牌堆中的卡牌数量)
     */
    public int getDrawPileSize() {
        return drawPile.size();
    }

    /**
     * Gets the number of cards in the discard pile.
     * 获取弃牌堆中的卡牌数量。
     * @return The number of cards in the discard pile. (弃牌堆中的卡牌数量)
     */
    public int getDiscardPileSize() {
        return discardPile.size();
    }
} 