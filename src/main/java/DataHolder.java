import java.util.Map;
import static java.lang.Integer.parseInt;
import static java.lang.Float.parseFloat;



public class DataHolder {

    private static Map<String, String> data;

    public Map<String, String> getData(){
        return data;
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