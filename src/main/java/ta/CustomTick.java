package ta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Decimal;
import org.ta4j.core.Bar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by nitigyas on 10/12/17.
 * https://github.com/team172011
 * Class was originally created to provide Custom Tick data to sanity check the code.
 * It has 1 method
 *      historic_data()
 *          Fetches Data from External API , converts it to the Tick Class and returns an ArrayList<Tick> .
 */


public class CustomTick {

    private static Logger logger = LoggerFactory.getLogger(CustomTick.class);

    public static ArrayList<Bar> historic_data(String companyName , String dateRange){

        ArrayList<Bar> historic_ticks = new ArrayList<Bar>();

        JSONParser parser = new JSONParser();

        OkHttpClient client = new OkHttpClient();

        try {

            Request request = new Request.Builder()
                    .url("http://localhost:8000/ticklist/"+companyName+"/"+dateRange)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();

            String jsonString = response.body().string();                       // Convert Response String into Tick Object

            JSONArray jsonArray = (JSONArray)parser.parse(jsonString);

            NumberFormat format = NumberFormat.getInstance(Locale.US);

            for (int i =0 ; i <jsonArray.size(); i++){

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);          //Create a Json Object

                Decimal openPrice =  Decimal.valueOf(format.parse(jsonObject.get("open_price").toString()).doubleValue());

                Decimal closePrice =  Decimal.valueOf(format.parse(jsonObject.get("prev_close").toString()).doubleValue());

                Decimal minPrice =  Decimal.valueOf(format.parse(jsonObject.get("low_price").toString()).doubleValue());

                Decimal maxPrice =  Decimal.valueOf(format.parse(jsonObject.get("high_price").toString()).doubleValue());

                Decimal volume =  Decimal.valueOf(format.parse(jsonObject.get("total_traded_quantity").toString()).doubleValue());

                LocalDate date  = LocalDate.parse(jsonObject.get("date").toString(),DateTimeFormatter.ofPattern("yyyy-MM-d"));

                ZonedDateTime time = date.atStartOfDay(ZoneId.systemDefault());

                historic_ticks.add(new BaseBar(time,openPrice, maxPrice, minPrice, closePrice,volume));
            }

        }catch (IOException iox){

            logger.error(iox.toString());

        }catch (java.text.ParseException px){

            logger.error(px.toString());

        }catch (ParseException px){

            logger.error(px.toString());

        }

        return historic_ticks;

    }


    public static Bar getBar(String companyName ,int barIndex){

        JSONParser parser = new JSONParser();

        OkHttpClient client = new OkHttpClient();

        try {

            Request request = new Request.Builder()
                    .url("http://localhost:8000/ticklist/"+companyName+"/exact/"+barIndex+"day")
                    .get()
                    .build();

            Response response = client.newCall(request).execute();

            String jsonString = response.body().string();                       // Convert Response String into Tick Object

            JSONArray jsonArray = (JSONArray)parser.parse(jsonString);

            NumberFormat format = NumberFormat.getInstance(Locale.US);


            JSONObject jsonObject = (JSONObject) jsonArray.get(0);          //Create a Json Object
            //System.out.println(jsonObject.toString());

            Decimal openPrice =  Decimal.valueOf(format.parse(jsonObject.get("open_price").toString()).doubleValue());

            Decimal closePrice =  Decimal.valueOf(format.parse(jsonObject.get("prev_close").toString()).doubleValue());

            Decimal minPrice =  Decimal.valueOf(format.parse(jsonObject.get("low_price").toString()).doubleValue());

            Decimal maxPrice =  Decimal.valueOf(format.parse(jsonObject.get("high_price").toString()).doubleValue());

            Decimal volume =  Decimal.valueOf(format.parse(jsonObject.get("total_traded_quantity").toString()).doubleValue());

            LocalDate date  = LocalDate.parse(jsonObject.get("date").toString(),DateTimeFormatter.ofPattern("yyyy-MM-d"));

            ZonedDateTime time = date.atStartOfDay(ZoneId.systemDefault());

            return  new BaseBar(time,openPrice, maxPrice, minPrice, closePrice,volume);

        }catch (IOException iox){

            logger.error(iox.toString());

        }catch (java.text.ParseException px){

            logger.error(px.toString());

        }catch (ParseException px){

            logger.error(px.toString());

        }
        return  null;
    }

}
