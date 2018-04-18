package ta;
import eu.verdelhan.ta4j.BaseTick;
import eu.verdelhan.ta4j.BaseTradingRecord;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
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

    public static ArrayList<Tick> historic_data(String companyName , String dateRange){

        ArrayList<Tick> historic_ticks = new ArrayList<Tick>();

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

                historic_ticks.add(new BaseTick(time,openPrice, maxPrice, minPrice, closePrice,volume));
            }

        }catch (IOException iox){

            System.out.print("IOException Received at CustomTick : " + iox);

        }catch (java.text.ParseException px){

            System.out.print("Text ParseException Received at CustomTick" + px);

        }catch (ParseException px){

            System.out.print("ParseException Received at CustomTick" + px);

        }

        return historic_ticks;

    }

}
