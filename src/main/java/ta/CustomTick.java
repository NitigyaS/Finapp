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
 */
public class CustomTick {


    private static Decimal LAST_TICK_CLOSE_PRICE  ;

    CustomTick(){

    }
    // Fcuntion to Generate Random Decimal Value
    private static Decimal randDecimal(Decimal min, Decimal max) {
        Decimal randomDecimal = null;
        if (min != null && max != null && min.isLessThan(max)) {
            randomDecimal = max.minus(min).multipliedBy(Decimal.valueOf(Math.random())).plus(min);
        }
        return randomDecimal;
    }

    public static Tick generateRandomTick() {
        //Fetch The Time now
        ZonedDateTime endTime  = ZonedDateTime.now();
        //Create First Tick
        Tick newTick  = new BaseTick(endTime, 105.42, 112.99, 104.01, 111.42, 1337);
        LAST_TICK_CLOSE_PRICE = newTick.getClosePrice();
        //Random Increment Decrement Percentage
        final Decimal maxRange = Decimal.valueOf("0.05"); // 3.0%
        Decimal openPrice = LAST_TICK_CLOSE_PRICE;
        Decimal minPrice = openPrice.minus(openPrice.multipliedBy(maxRange.multipliedBy(Decimal.valueOf(Math.random()))));
        Decimal maxPrice = openPrice.plus(openPrice.multipliedBy(maxRange.multipliedBy(Decimal.valueOf(Math.random()))));
        Decimal closePrice = randDecimal(minPrice, maxPrice);
        LAST_TICK_CLOSE_PRICE = closePrice;
        // Create Tick
        return new BaseTick(ZonedDateTime.now(), openPrice, maxPrice, minPrice, closePrice, Decimal.ONE);
    }

    public static ArrayList<Tick> historic_data(String companyName , String dateRange){
        ArrayList<Tick> historic_ticks = new ArrayList<Tick>();
        JSONParser parser = new JSONParser();
        OkHttpClient client = new OkHttpClient();
        try {

            Request request = new Request.Builder()
                    .url("http://localhost:5000/nse")
                    .get()
                    .addHeader("symbol", companyName)
                    .addHeader("daterange", dateRange)
                    .build();

            Response response = client.newCall(request).execute();
            // Convert Response String into Tick Object
            String jsonString = response.body().string();
            System.out.println(jsonString);

            JSONArray jsonArray = (JSONArray)parser.parse(jsonString);
            // Convert String date to ZonedDateTime
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            System.out.println(jsonArray.size() +"+++++++++++++");

            for (int i =0 ; i <jsonArray.size(); i++){
                //Create a Json Object
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Decimal openPrice =  Decimal.valueOf(format.parse(jsonObject.get("Open Price").toString()).doubleValue());
                Decimal closePrice =  Decimal.valueOf(format.parse(jsonObject.get("Close Price").toString()).doubleValue());
                Decimal minPrice =  Decimal.valueOf(format.parse(jsonObject.get("Low Price").toString()).doubleValue());
                Decimal maxPrice =  Decimal.valueOf(format.parse(jsonObject.get("High Price").toString()).doubleValue());
                Decimal volume =  Decimal.valueOf(format.parse(jsonObject.get("Open Price").toString()).doubleValue());
                LocalDate date  = LocalDate.parse(jsonObject.get("Date").toString(),DateTimeFormatter.ofPattern("d-MMM-yyyy"));
                ZonedDateTime time = date.atStartOfDay(ZoneId.systemDefault());
                historic_ticks.add(new BaseTick(time,openPrice, maxPrice, minPrice, closePrice,volume));
                //System.out.println(jsonObject.get("Date"));
            }
            // Print the Json Data Reveived
            //System.out.print(jsonString);
        }catch (IOException iox){
            System.out.print("lll" + iox);
        }catch (java.text.ParseException px){
            System.out.print("jjj" + px);
        }catch (ParseException px){
            System.out.print(px);
        }
        System.out.println("------------"+historic_ticks.size());
        return historic_ticks;
    }
}
