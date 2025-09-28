public class CustomHashMapTest {
    public static void main(String[] args) {

        CustomHashMap map = new CustomHashMap();
        map.put("Лонг-Рейндж", 14255);
        map.put("Эльберт", 14433);
        map.put("Марун", 14156);
        map.put("Касл", 14265);
        map.put("Эльберт", 14433);
        map.put("Марун", 14156);

        System.out.println("Размер: " + map.size());
        System.out.println("Значение по ключу: " + map.get("Марун"));
        System.out.println("Удалить : " + map.remove("Марун"));
        System.out.println("Значение по ключу после удаления: " + map.get("Марун"));
        System.out.println("Размер после удаления: " + map.size());
    }
}
