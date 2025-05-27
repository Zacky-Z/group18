package com.forbiddenisland.core.model;

import com.forbiddenisland.enums.SpecialCardType;

public class SpecialActionCard extends TreasureCard {
    private final SpecialCardType cardType;

    public SpecialActionCard(SpecialCardType cardType) {
        super(null); // SpecialActionCard does not have a TreasureType
        this.cardType = cardType;
    }

    public SpecialCardType getCardType() {
        return cardType;
    }

    @Override
    public String getDescription() {
        return cardType.getDescription();
    }
}    