import java.util.Map;
import static java.lang.Integer.parseInt;
import static java.lang.Float.parseFloat;



public class DataHolder {

    private static final int NUM_HORIZ_BOXES = 20;
    private static final int NUM_VERT_BOXES = 20;

    private static Map<String, String> data;

    private static Tile[][] mapData = new Tile[NUM_HORIZ_BOXES][NUM_VERT_BOXES];

    public Map<String, String> getData(){
        return data;
    }

    public void initMapData(){
        for(int x = 0; x < NUM_VERT_BOXES; x++){
            for(int y = 0; y < NUM_HORIZ_BOXES; y++){
                mapData[x][y] = new Tile(0, "NA");
            }
        }
    }

    public Tile[][] getMapData(){
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
}