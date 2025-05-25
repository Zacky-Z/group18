package com.forbiddenisland.model;

/**
 * Functional interface for tile placement helper.
 * 板块放置帮助器的功能接口。
 */
@FunctionalInterface
public interface PlaceTileHelper {
    /**
     * Places a tile at the specified coordinates.
     * 在指定坐标放置板块。
     * 
     * @param r row coordinate (行坐标)
     * @param c column coordinate (列坐标)
     */
    void tile(int r, int c);
}
