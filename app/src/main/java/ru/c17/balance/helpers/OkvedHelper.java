package ru.c17.balance.helpers;

import ru.c17.balance.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OkvedHelper {

    private static OkvedHelper instance;

    private Map<String, Integer> map;
    private Map<Integer, ReceiptCategory> categories;


    private OkvedHelper() {
        map = new HashMap<>();
        map.put("47.11", 1);
        map.put("47.21", 1);
        map.put("47.22", 1);
        map.put("47.23", 1);
        map.put("47.24", 1);
        map.put("47.25", 2);
        map.put("47.26", 3);
        map.put("47.29", 1);
        map.put("47.30", 4);
        map.put("47.41", 5);
        map.put("47.42", 6);
        map.put("47.43", 7);
        map.put("47.51", 8);
        map.put("47.52", 9);
        map.put("47.53", 9);
        map.put("47.54", 10);
        map.put("47.59", 10);
        map.put("47.61", 11);
        map.put("47.62", 12);
        map.put("47.63", 13);
        map.put("47.64", 14);
        map.put("47.65", 15);
        map.put("47.71", 16);
        map.put("47.72", 16);
        map.put("47.73", 17);
        map.put("47.74", 17);
        map.put("47.75", 18);
        map.put("47.76", 19);
        map.put("47.77", 20);
        map.put("55.10", 21);
        map.put("55.20", 21);
        map.put("55.30", 21);
        map.put("55.90", 21);
        map.put("56.10", 22);
        map.put("56.21", 22);
        map.put("56.29", 22);
        map.put("56.30", 23);
        map.put("49", 24);
        map.put("50", 24);
        map.put("51", 24);
        map.put("52", 24);
        map.put("53", 24);

//        map = new HashMap<>();
//        map.put("47.11", new ReceiptCategory(1, R.string.category_grocery, "\uD83D\uDED2", R.color.colorPieGrocery));
//        map.put("47.21", new ReceiptCategory(1, R.string.category_grocery, "\uD83D\uDED2", R.color.colorPieGrocery));
//        map.put("47.22", new ReceiptCategory(1, R.string.category_grocery, "\uD83D\uDED2", R.color.colorPieGrocery));
//        map.put("47.23", new ReceiptCategory(1, R.string.category_grocery, "\uD83D\uDED2", R.color.colorPieGrocery));
//        map.put("47.24", new ReceiptCategory(1, R.string.category_grocery, "\uD83D\uDED2", R.color.colorPieGrocery));
//        map.put("47.25", new ReceiptCategory(2, R.string.category_alcohol, "\uD83C\uDF77", R.color.colorPieAlcohol));
//        map.put("47.26", new ReceiptCategory(3, R.string.category_tobacco, "\uD83D\uDEAC", R.color.colorPieTobacco));
//        map.put("47.29", new ReceiptCategory(1, R.string.category_grocery, "\uD83D\uDED2", R.color.colorPieGrocery));
//        map.put("47.30", new ReceiptCategory(4, R.string.category_gasoline, "⛽", R.color.colorPieGasoline));
//        map.put("47.41", new ReceiptCategory(5, R.string.category_computer, "\uD83D\uDCBB", R.color.colorPieComputer));
//        map.put("47.42", new ReceiptCategory(6, R.string.category_mobile, "\uD83D\uDCF1",R.color.colorPieMobile ));
//        map.put("47.43", new ReceiptCategory(7, R.string.categoty_audio_video, "\uD83D\uDCF9", R.color.colorPieAudio ));
//        map.put("47.51", new ReceiptCategory(8, R.string.category_knitting, "\uD83E\uDDF5", R.color.colorPieKnitting));
//        map.put("47.52", new ReceiptCategory(9, R.string.category_building_material, "\uD83D\uDEE0", R.color.colorPieBuilding));
//        map.put("47.53", new ReceiptCategory(9, R.string.category_building_material, "\uD83D\uDEE0", R.color.colorPieBuilding));
//        map.put("47.54", new ReceiptCategory(10, R.string.category_appliances, "\uD83D\uDCFA", R.color.colorPieAppliances));
//        map.put("47.59", new ReceiptCategory(10, R.string.category_appliances, "\uD83D\uDCFA", R.color.colorPieAppliances));
//        map.put("47.61", new ReceiptCategory(11, R.string.category_books, "\uD83D\uDCD5", R.color.colorPieBooks));
//        map.put("47.62", new ReceiptCategory(12, R.string.category_stationery, "\uD83D\uDD87", R.color.colorPieStationery));
//        map.put("47.63", new ReceiptCategory(13, R.string.category_music, "\uD83C\uDFB5", R.color.colorPieMusic));
//        map.put("47.64", new ReceiptCategory(14, R.string.category_travel_equipment, "\uD83C\uDFA3", R.color.colorPieTravelEquip));
//        map.put("47.65", new ReceiptCategory(15, R.string.category_toys, "\uD83E\uDDF8", R.color.colorPieToys));
//        map.put("47.71", new ReceiptCategory(16, R.string.category_clothes, "\uD83D\uDC55", R.color.colorPieClothes));
//        map.put("47.72", new ReceiptCategory(16, R.string.category_clothes, "\uD83D\uDC55", R.color.colorPieClothes));
//        map.put("47.73", new ReceiptCategory(17, R.string.category_medicine, "\uD83D\uDC8A", R.color.colorPieMedicine));
//        map.put("47.74", new ReceiptCategory(17, R.string.category_medicine, "\uD83D\uDC8A", R.color.colorPieMedicine));
//        map.put("47.75", new ReceiptCategory(18, R.string.category_cosmetics, "\uD83D\uDC84", R.color.coloPieCosmetics));
//        map.put("47.76", new ReceiptCategory(19, R.string.category_flowers, "\uD83D\uDC90", R.color.colorPieFlowers));
//        map.put("47.77", new ReceiptCategory(20, R.string.category_jewerly, "\uD83D\uDC8D", R.color.colorPieJewelry));
//        map.put("55.10", new ReceiptCategory(21, R.string.category_hotels, "\uD83C\uDFE8", R.color.colorPieHotels));
//        map.put("55.20", new ReceiptCategory(21, R.string.category_hotels, "\uD83C\uDFE8", R.color.colorPieHotels));
//        map.put("55.30", new ReceiptCategory(21, R.string.category_hotels, "\uD83C\uDFE8", R.color.colorPieHotels));
//        map.put("55.90", new ReceiptCategory(21, R.string.category_hotels,"\uD83C\uDFE8", R.color.colorPieHotels));
//        map.put("56.10", new ReceiptCategory(22, R.string.category_restaurants,"\uD83C\uDF5D", R.color.colorPieRestaurants));
//        map.put("56.21", new ReceiptCategory(22, R.string.category_restaurants,"\uD83C\uDF5D", R.color.colorPieRestaurants));
//        map.put("56.29", new ReceiptCategory(22, R.string.category_restaurants,"\uD83C\uDF5D", R.color.colorPieRestaurants));
//        map.put("56.30", new ReceiptCategory(23, R.string.category_bars,"\uD83C\uDF79", R.color.colorPieBars));
//        map.put("49", new ReceiptCategory(24, R.string.category_transport, "\uD83D\uDE95", R.color.colorPieTransport));
//        map.put("50", new ReceiptCategory(24, R.string.category_transport, "\uD83D\uDE95", R.color.colorPieTransport));
//        map.put("51", new ReceiptCategory(24, R.string.category_transport, "\uD83D\uDE95", R.color.colorPieTransport));
//        map.put("52", new ReceiptCategory(24, R.string.category_transport, "\uD83D\uDE95", R.color.colorPieTransport));
//        map.put("53", new ReceiptCategory(24, R.string.category_transport, "\uD83D\uDE95", R.color.colorPieTransport));



        categories = new HashMap<>();
        categories.put(0, new ReceiptCategory(0, R.string.category_etc, "\uD83E\uDD37", R.color.colorPieOther));
        categories.put(1, new ReceiptCategory(1, R.string.category_grocery, "\uD83D\uDED2", R.color.colorPieGrocery));
        categories.put(2, new ReceiptCategory(2, R.string.category_alcohol, "\uD83C\uDF77", R.color.colorPieAlcohol));
        categories.put(3, new ReceiptCategory(3, R.string.category_tobacco, "\uD83D\uDEAC", R.color.colorPieTobacco));
        categories.put(4, new ReceiptCategory(4, R.string.category_gasoline, "⛽", R.color.colorPieGasoline));
        categories.put(5, new ReceiptCategory(5, R.string.category_computer, "\uD83D\uDCBB", R.color.colorPieComputer));
        categories.put(6, new ReceiptCategory(6, R.string.category_mobile, "\uD83D\uDCF1",R.color.colorPieMobile ));
        categories.put(7, new ReceiptCategory(7, R.string.category_audio_video, "\uD83D\uDCF9", R.color.colorPieAudio ));
        categories.put(8, new ReceiptCategory(8, R.string.category_knitting, "\uD83E\uDDF5", R.color.colorPieKnitting));
        categories.put(9, new ReceiptCategory(9, R.string.category_building_material, "\uD83D\uDEE0", R.color.colorPieBuilding));
        categories.put(10, new ReceiptCategory(10, R.string.category_appliances, "\uD83D\uDCFA", R.color.colorPieAppliances));
        categories.put(11, new ReceiptCategory(11, R.string.category_books, "\uD83D\uDCD5", R.color.colorPieBooks));
        categories.put(12, new ReceiptCategory(12, R.string.category_stationery, "\uD83D\uDD87", R.color.colorPieStationery));
        categories.put(13, new ReceiptCategory(13, R.string.category_music, "\uD83C\uDFB5", R.color.colorPieMusic));
        categories.put(14, new ReceiptCategory(14, R.string.category_travel_equipment, "\uD83C\uDFA3", R.color.colorPieTravelEquip));
        categories.put(15, new ReceiptCategory(15, R.string.category_toys, "\uD83E\uDDF8", R.color.colorPieToys));
        categories.put(16, new ReceiptCategory(16, R.string.category_clothes, "\uD83D\uDC55", R.color.colorPieClothes));
        categories.put(17, new ReceiptCategory(17, R.string.category_medicine, "\uD83D\uDC8A", R.color.colorPieMedicine));
        categories.put(18, new ReceiptCategory(18, R.string.category_cosmetics, "\uD83D\uDC84", R.color.coloPieCosmetics));
        categories.put(19, new ReceiptCategory(19, R.string.category_flowers, "\uD83D\uDC90", R.color.colorPieFlowers));
        categories.put(20, new ReceiptCategory(20, R.string.category_jewerly, "\uD83D\uDC8D", R.color.colorPieJewelry));
        categories.put(21, new ReceiptCategory(21, R.string.category_hotels,"\uD83C\uDFE8", R.color.colorPieHotels));
        categories.put(22, new ReceiptCategory(22, R.string.category_restaurants,"\uD83C\uDF5D", R.color.colorPieRestaurants));
        categories.put(23, new ReceiptCategory(23, R.string.category_bars,"\uD83C\uDF79", R.color.colorPieBars));
        categories.put(24, new ReceiptCategory(24, R.string.category_transport, "\uD83D\uDE95", R.color.colorPieTransport));



    }

    public static OkvedHelper getInstance() {
        if (instance == null) {
            instance = new OkvedHelper();
        }
        return instance;
    }

    int getReceiptCategoryId(String okved) {
        if (okved == null) {
            return 0;
        }

        if (okved.equals("")) {
            return 0;
        }

        if (okved.length() > 5) {
            okved = okved.substring(0, 5);
        }

        if (map.containsKey(okved)) {
            return map.get(okved);
        } else {
            okved = okved.substring(0,2);
            if (map.containsKey(okved)) {
                return map.get(okved);
            } else {
                return 0;
            }
        }
    }

    public ReceiptCategory getReceiptCategoryById(int id) {
        return categories.get(id);
    }

    public List<ReceiptCategory> getCategories(){
        return new ArrayList<>(categories.values());
    }


}
