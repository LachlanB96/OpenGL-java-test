package input;

import entity.Tile;
import model.DataHolder;

public class KeyPressed{

    private static DataHolder dataInstance;

    public KeyPressed(){
        System.out.println("Init Key presser");
    }

    public static void pressed(int key){
        Tile[][] mapData = dataInstance.getMapData();
        int playerPosX = (int) DataHolder.getPlayerData().get("playerPosX");
        int playerPosY = (int) DataHolder.getPlayerData().get("playerPosY");
        if(key == 87){
            System.out.println("W");
            if(mapData[playerPosX][playerPosY + 1].getTileID() == 0){
                DataHolder.setMapData(playerPosX, playerPosY + 1, 1, "LB");
                DataHolder.setMapData(playerPosX, playerPosY, 0, "dead");
                DataHolder.setPlayerData("playerPosY", playerPosY + 1);
            }
        } else if(key == 68){
            System.out.println("D");
            if(mapData[playerPosX + 1][playerPosY].getTileID() == 0){
                DataHolder.setMapData(playerPosX + 1, playerPosY , 1, "LB");
                DataHolder.setMapData(playerPosX, playerPosY, 0, "dead");
                DataHolder.setPlayerData("playerPosX", playerPosX + 1);
            }
        }
        else if(key == 65){
            System.out.println("A");
            if(mapData[playerPosX - 1][playerPosY].getTileID() == 0){
                DataHolder.setMapData(playerPosX - 1, playerPosY, 1, "LB");
                DataHolder.setMapData(playerPosX, playerPosY, 0, "dead");
                DataHolder.setPlayerData("playerPosX", playerPosX - 1);
            }
        } else if(key == 83){
            System.out.println("S");
            if(mapData[playerPosX][playerPosY - 1].getTileID() == 0){
                DataHolder.setMapData(playerPosX, playerPosY - 1, 1, "LB");
                DataHolder.setMapData(playerPosX, playerPosY, 0, "dead");
                DataHolder.setPlayerData("playerPosY", playerPosY - 1);
            }
        }
        System.out.printf("x: [%s], y: [%s]\n", playerPosX, playerPosY);
        DataHolder.mapDataToString();
    }
}
