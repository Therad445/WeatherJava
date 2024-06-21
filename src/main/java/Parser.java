import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static Document getPage() throws IOException {
        String url = "https://pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    private static String getDataFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can't extract date from string");
    }

    public static int printPartValues(Elements values, int index) {
        int iterationCount = 4;
        if (index == 0) {
            Element valueLn = values.get(0);
            if (valueLn.text().contains("Утро")) {
                iterationCount = 4;
            } else if (valueLn.text().contains("День")) {
                iterationCount = 3;
            } else if (valueLn.text().contains("Вечер")) {
                iterationCount = 2;
            } else if (valueLn.text().contains("Ночь")) {
                iterationCount = 1;
            }
        }
        for (int i = 0; i < iterationCount; i++) {
            Element valueLine = values.get(index + i);
            for (Element td : valueLine.select("td")) {
                System.out.print(td.text() + "    ");
            }
            System.out.println();
        }
        return iterationCount;
    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;
        for (Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDataFromString(dateString);
            System.out.println(date + "    Явления    Температура     Давл    Влажность    Ветер");
            int iterationCount = printPartValues(values, index);
            index = index + iterationCount;
        }

    }
}
