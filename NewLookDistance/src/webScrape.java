import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;

public class webScrape {
    String cityName;
    String stateName;
    String[] businessAddress = {"516BedfordStreetEastBridgewater,Ma", "Dedham,MA", "Providence,RI", "Plymouth,Ma"};
    String[] businessLocale = {" from Here", " from Scott", " from Sam", " from Plymouth"};
    String[] distances = new String[businessAddress.length];
    String urlLink1 = "";
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public String getCity(String zipcode) {
        urlLink1 = "https://maps.googleapis.com/maps/api/geocode/json?address==" + zipcode + "&key=AIzaSyApSazA37My_KthkgkqLcwRujUoRvV0J7Q";
        try {
            //dive into the nested JSON to retrieve City and State
            JSONObject json = new JSONObject((readUrl(urlLink1)));
            JSONArray result = json.getJSONArray("results");
            JSONObject result1 = result.getJSONObject(0);
            JSONArray components = result1.getJSONArray("address_components");
            JSONObject city = components.getJSONObject(1);
            cityName = city.getString("long_name");
            JSONObject state = components.getJSONObject(3);
            stateName = state.getString("short_name");
        } catch (JSONException a) {
            System.out.println("Unable to reach website");
        } catch (Exception v) {
            System.out.println("Crashed");
        }
        String combo = cityName + ", " + stateName;
        return combo;
    }
    public String[] getByZip(String zipcode) {
        try {
            for (int i = 0; i < businessAddress.length; i++) {
                urlLink1 = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + businessAddress[i] + "&destinations=" + zipcode + "&key=AIzaSyApSazA37My_KthkgkqLcwRujUoRvV0J7Q";
                JSONObject json = new JSONObject((readUrl(urlLink1)));
                JSONArray result = json.getJSONArray("rows");
                JSONObject result1 = result.getJSONObject(0);
                JSONArray result2 = result1.getJSONArray("elements");
                JSONObject result3 = result2.getJSONObject(0);
                JSONObject result4 = result3.getJSONObject("distance");
                distances[i] = result4.getString("text");
                distances[i] = distances[i] + businessLocale[i];
            }
        } catch (JSONException a) {
            System.out.println("Unable to reach website");
        } catch (Exception v) {
            System.out.println("Crashed");
        }
        sortRanges(distances);
        return distances;
    }
    public String[] getByCity(String searchtool){
        try{
            for (int i=0;i<businessAddress.length;i++) {
                urlLink1="https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+businessAddress[i]+"&destinations="+searchtool+"&key=AIzaSyApSazA37My_KthkgkqLcwRujUoRvV0J7Q";
                JSONObject json = new JSONObject((readUrl(urlLink1)));
                JSONArray result = json.getJSONArray("rows");
                JSONObject result1 = result.getJSONObject(0);
                JSONArray result2 = result1.getJSONArray("elements");
                JSONObject result3 = result2.getJSONObject(0);
                JSONObject result4 = result3.getJSONObject("distance");
                distances[i] = result4.getString("text");
                distances[i]=distances[i]+businessLocale[i];
            }
        }
        catch(JSONException a)
        {
            System.out.println("Unable to reach website");
        }
        catch(Exception v){
            System.out.println("Crashed");
        }
        cityName="";
        stateName="";

        sortRanges(distances);
       return distances;
    }
    public String isOverrange(String string) {
        double miles=Double.parseDouble(string.substring(0,string.indexOf(" ")));
        DecimalFormat round=new DecimalFormat("#.#");
        if((miles>25.00))
        {
            Double rounded = miles - 25.00;
            string = round.format(rounded) + " miles past "+ string.substring(string.lastIndexOf(" ")+1)+"'s range";
        }
        return string;
    }

    private String[] sortRanges(String[] distances){

        //sort array from smallest to largest, unless "here" is less than 25 miles
        boolean hub=false;
        if(Double.parseDouble(distances[0].substring(0,distances[0].indexOf(" ")))<25.0) {
            for (int i=2;i<distances.length;i++)
            {
                String key=distances[i];
                int j=i-1;
                while((j>0) && (Double.parseDouble(distances[j].substring(0,distances[j].indexOf(" ")))>Double.parseDouble(key.substring(0,key.indexOf(" ")))))
                {
                   distances[j+1]=distances[j];
                    j--;
                }
                distances[j+1]=key;
            }
        }
        else
            for (int i=1;i<distances.length;i++)
            {
                String key=distances[i];
                int j=i-1;
                while((j>-1) && (Double.parseDouble(distances[j].substring(0,distances[j].indexOf(" ")))>Double.parseDouble(key.substring(0,key.indexOf(" ")))))
                {
                    distances[j+1]=distances[j];
                    j--;
                }
                distances[j+1]=key;
            }


        return distances;
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
