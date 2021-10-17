import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;
public class Main {

    public static void main(String[] args) {
        //ввод
        StringBuilder input=new StringBuilder();
        Scanner in=new Scanner(System.in);
        String addLine=in.nextLine();
        while (!addLine.equals("]")){
            input.append(addLine);
            input.append("\n");
            addLine=in.nextLine();
        }
        input.append(addLine);
        //парсер
        JSONParser parser=new JSONParser();
        JSONArray list;
        try {
            list=(JSONArray) parser.parse(input.toString());
        } catch (Exception e){
            throw new RuntimeException();}
        //разбиваем массив
        ArrayList<Event> EventList=new ArrayList<>();
        for (Object item:list){
            JSONObject eventJSONobj;
            eventJSONobj = (JSONObject) item;
            EventList.add(new Event(eventJSONobj));
        }
        //Отрабатываем заказ
        TreeMap<Long,Order> orders=new TreeMap<>();
        for (Event event: EventList) {
            if(orders.keySet().contains(event.order_id)){
                orders.get(event.order_id).add(event);
            }else{
                orders.put(event.order_id,new Order(event));
            }
        }

        ArrayList<Order> finalOrder=
                new ArrayList<>();
        for (Order order:orders.values()){
            if(order.statusOrder()==true)
                finalOrder.add(order);
        }
        System.out.println(finalOrder);
    }
}
class Event{
    long event_id;
    long order_id;
    long item_id;
    long count;
    long return_count;
    String status;
    Event(JSONObject obj){
        event_id=(long)obj.get("event_id");
        order_id=(long)obj.get("order_id");
        item_id=(long)obj.get("item_id");
        count=(long)obj.get("count");
        return_count=(long)obj.get("return_count");
        status=(String)obj.get("status");
    }
    @Override
    public String toString() {
        return "\n{" +
                "\n event_id=" + event_id +","+
                "\n order_id=" + order_id +","+
                "\n item_id=" + item_id +","+
                "\n count=" + count +","+
                "\n return_count=" + return_count +","+
                "\n status='" + status + '\'' +
                "\n}";
    }
}
class Order{
    long order_id;//********
    TreeMap<Long,Item> items;
    Order(Event e){
        order_id=e.order_id;
        items=new TreeMap<>();
        items.put(e.item_id,new Item(e));
    }
    TreeMap<Long,Item> finalitems(){
        TreeMap<Long,Item> finalitems=new TreeMap<>();
        for (Item item:items.values())
            if(item.finalStatus==true)
                finalitems.put(item.item_id,item);
        return finalitems;
    };
    boolean add(Event e){
        boolean added=false;
        if(items.keySet().contains(e.item_id)){
            items.get(e.item_id).update(e);
            added=false;
        }else{
            items.put(e.item_id,new Item(e));
            added=true;
        }
        return added;
    }
    TreeMap<Long,Item> finalItems;
    boolean statusOrder(){
        finalItems=finalitems();
        if(finalItems.size()==0) return false;
        else return true;
    }

    @Override
    public String toString() {
        return "\n{" +
                "\n \"id\": " + order_id +","+
                "\n \"items\": " + finalItems.values() +
                "\n}";
    }
}
class Item{
    long item_id;
    long MaxEvent_id;
    long count;
    boolean finalStatus=true;
    Item(Event e){
        item_id=e.item_id;
        MaxEvent_id=e.event_id;
        count=e.count-e.return_count;
        if(e.status.equals("OK"))
            finalStatus=true;
        else finalStatus=false;
        if(count<=0) finalStatus=false;
    }
    boolean update(Event e){
        //if(item_id==e.item_id)
        if(e.event_id>MaxEvent_id){
            MaxEvent_id=e.event_id;
            count=e.count-e.return_count;
            if(e.status.equals("OK"))
                finalStatus=true;
            else if(e.status.equals("CANCEL")) finalStatus=false;
            if(count<=0) finalStatus=false;
            return true;
        } else return false;
    }
    @Override
    public String toString() {
            return "\n  {\n    \"count\": " + count + ","+
                    "\n    \"id\": " + item_id+"\n  }";
        }
}