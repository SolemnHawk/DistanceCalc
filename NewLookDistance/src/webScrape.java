import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class webScrape {
    String cityName;
    String stateName;
    String distance;
    String businessAddress1="516BedfordStreetEastBridgewater,Ma";
    String businessAddress2="";
    String businessAddress3="";
    String urlLink="";
    String userInput="";
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


    public String getCity(String zipcode){
        urlLink="https://maps.googleapis.com/maps/api/geocode/json?address=="+zipcode+"&key=AIzaSyApSazA37My_KthkgkqLcwRujUoRvV0J7Q";
        try{
            //dive into the nested JSON to retrieve long and lat
            JSONObject json = new JSONObject((readUrl(urlLink)));
            JSONArray result = json.getJSONArray("results");
            JSONObject result1 = result.getJSONObject(0);
            JSONArray components = result1.getJSONArray("address_components");
            JSONObject city =components.getJSONObject(1);
            cityName=city.getString("long_name");
            JSONObject state=components.getJSONObject(3);
            stateName=state.getString("short_name");

        }
        catch(JSONException a)
        {
            System.out.println("Unable to reach website");
        }
        catch(Exception v){
            System.out.println("Crashed");
        }
        String combo=cityName+","+stateName;
        return combo;
    }

    public String getDistance(String searchtool){
        distance="";
        urlLink="https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+businessAddress1+"&destinations="+searchtool+"&key=AIzaSyApSazA37My_KthkgkqLcwRujUoRvV0J7Q";
        try{
            JSONObject json = new JSONObject((readUrl(urlLink)));
            JSONArray result = json.getJSONArray("rows");
            JSONObject result1=result.getJSONObject(0);
            JSONArray result2=result1.getJSONArray("elements");
            JSONObject result3=result2.getJSONObject(0);
            JSONObject result4=result3.getJSONObject("distance");
            distance=result4.getString("text");
        }
        catch(JSONException a)
        {
            System.out.println("Unable to reach website");
        }
        catch(Exception v){
            System.out.println("Crashed");
        }

        if (distance.equals(""))
            return "City was not found.";
        int x=searchtool.length();
        searchtool=searchtool.substring(0,x-2)+" "+searchtool.substring(x-2,x);
        //distance= searchtool+" is:" +distance+"les.";
        cityName="";
        stateName="";
        searchtool="";
       return distance;
    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
