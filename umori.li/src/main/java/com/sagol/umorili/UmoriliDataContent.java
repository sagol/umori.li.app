package com.sagol.umorili;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UmoriliDataContent {

    public List<DataItem> ITEMS = new ArrayList<DataItem>();

    public Map<String, DataItem> ITEM_MAP = new HashMap<String, DataItem>();

    public void addItem(DataItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class DataItem {
        public String id;
        public String site;
        public String name;
        public String desc;
        public String link;
        public String element;


        public DataItem(String id, String site, String name, String desc,
                        String link, String element) {
            this.id = id;
            this.site = site;
            this.name = name;
            this.desc = desc;
            this.link = link;
            this.element = element;
        }

        @Override
        public String toString() {
            if (element == null)
                return desc;
            else return element;
        }
    }
}
