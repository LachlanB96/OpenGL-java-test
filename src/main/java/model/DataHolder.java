package model;

import entity.Tile;

import java.util.HashMap;
import java.util.Map;
import static java.lang.Float.parseFloat;


public class DataHolder {

    private static final DataHolder instance = new DataHolder();

    private static final int NUM_HORIZ_BOXES = 20;
    private static final int NUM_VERT_BOXES = 20;

    private static Map<String, String> data;

    private static Tile[][] mapData = new Tile[NUM_HORIZ_BOXES][NUM_VERT_BOXES];
    private static Map<String, Object> playerData = new HashMap<>();

    public Map<String, String> getData(){
        return data;
    }

    public static DataHolder getDataHolder(){
        return instance;
    }

    public static void initMapData(){
        for(int x = 0; x < NUM_VERT_BOXES; x++){
            for(int y = 0; y < NUM_HORIZ_BOXES; y++){
                mapData[x][y] = new Tile(0, "NA");
            }
        }
    }
    public static void initPlayerData(){
        playerData.put("playerPosX", 10);
        playerData.put("playerPosY", 10);
    }
    public static void setMapData(int x, int y, int id, String name){
        mapData[x][y] = new Tile(id, name);
    }

    public static Tile[][] getMapData(){
        return mapData;
    }

    public float getDataOrZeroFloat(String key){
        try{
            if(data.containsKey(key)){
                return parseFloat(data.get(key));
            }
        } catch (NullPointerException e){
            return 0f;
        }
        return 0f;
    }

    public static void addData(String key, String value){
        if(data.containsKey(key)){
            data.remove(key);
            data.put(key, value);
        }
        data.put(key, value);
    }

    public void setData(Map<String, String> data){
        this.data = data;
    }

    private static final DataHolder holder = new DataHolder();

    public static DataHolder getInstance(){
        return holder;
    }

    public static Map<String, Object> getPlayerData(){
        return playerData;
    }

    public static void setPlayerData(String key, int value){
        playerData.put(key, value);
    }
    public static void setMapData(Tile[][] map){
        mapData = map;
    }
    public static void mapDataToString(){
        for(int y = NUM_HORIZ_BOXES - 1; y >= 0; y--){
            for(int x = 0; x < NUM_VERT_BOXES; x++){
                System.out.printf("[%s], ", mapData[x][y].getTileID() == 0 ? " " : mapData[x][y].getTileID());
            }
            System.out.println();
        }
    }
}