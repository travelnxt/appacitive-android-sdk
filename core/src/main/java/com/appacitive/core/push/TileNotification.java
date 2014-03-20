package com.appacitive.core.push;

import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;

import java.io.Serializable;

public class TileNotification extends WindowsPhoneNotification implements Serializable {
    public TileNotification() {
        super(WindowsPhoneNotificationType.Tile);
    }

    public static TileNotification createNewFlipTile(FlipTile tile) {
        TileNotification notification = new TileNotification();
        notification.setWp75Tile(tile);
        notification.setWp7Tile(tile);
        notification.setWp8Tile(tile);
        return notification;
    }

    public static TileNotification createNewIconicTile(IconicTile tile, FlipTile tileForWP75AndBelow) {
        TileNotification notification = new TileNotification();
        notification.setWp75Tile(tileForWP75AndBelow);
        notification.setWp7Tile(tileForWP75AndBelow);
        notification.setWp8Tile(tile);
        return notification;
    }

    public static TileNotification createNewCyclicTile(CyclicTile tile, FlipTile tileForWP75AndBelow) {
        TileNotification notification = new TileNotification();
        notification.setWp75Tile(tileForWP75AndBelow);
        notification.setWp7Tile(tileForWP75AndBelow);
        notification.setWp8Tile(tile);
        return notification;
    }

    private WindowsPhoneTile _wp8Tile;

    public WindowsPhoneTile getWp8Tile() {
        return _wp8Tile;
    }

    public void setWp8Tile(WindowsPhoneTile tile) {
        _wp8Tile = tile;
    }

    private WindowsPhoneTile _wp75Tile;

    public WindowsPhoneTile getWp75Tile() {
        return _wp75Tile;
    }

    public void setWp75Tile(WindowsPhoneTile tile) {
        if (tile != null && tile.windowsPhoneTileType != WindowsPhoneTileType.Flip) {
            throw new IllegalArgumentException("Only flip tiles are supported for Windows Phone v7.");
        }
        _wp75Tile = tile;
    }

    private WindowsPhoneTile _wp7Tile;

    public WindowsPhoneTile getWp7Tile() {
        return _wp7Tile;
    }

    public void setWp7Tile(WindowsPhoneTile tile) {
        if (tile != null && tile.windowsPhoneTileType != WindowsPhoneTileType.Flip) {
            throw new IllegalArgumentException("Only flip tiles are supported for Windows Phone v7.");
        }
        _wp7Tile = tile;
    }

    @Override
    public synchronized APJSONObject getMap() throws APJSONException {
        APJSONObject nativeMap = super.getMap();
        if (getWp8Tile() != null)
            nativeMap.put("wp8", getWp8Tile().getMap());
        if (getWp8Tile() != null)
            nativeMap.put("wp75", getWp75Tile().getMap());
        if (getWp8Tile() != null)
            nativeMap.put("wp7", getWp7Tile().getMap());
        return nativeMap;
    }
}
