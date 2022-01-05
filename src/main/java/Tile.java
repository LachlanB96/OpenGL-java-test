public class Tile{

    private int tileID;
    private String tileName;

    public Tile(int tileID, String tileName){
        this.tileID = tileID;
        this.tileName = tileName;
    }

    public int getTileID(){
        return tileID;
    }
}
