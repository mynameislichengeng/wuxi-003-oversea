package com.wizarpos.recode.print.data;

import com.wizarpos.recode.print.constants.PrintConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrintPartiesDataManager {


    public static List<PrintConstants.PartityItem> getParList(String ori) {
        List<PrintConstants.PartityItem> result = new ArrayList<>();
        String start = null;
        int next_index = -1;
        do {
            start = firstFlag(ori);
            if (start != null) {
                PrintConstants.PrintContentPartity tyeEnum = PrintConstants.PrintContentPartity.getEnumFromStart(start);
                String end = tyeEnum.getEnd();
                int index = ori.indexOf(start);
                int index_end = ori.indexOf(end);

                if (index != 0) {
                    PrintConstants.PartityItem one = new PrintConstants.PartityItem();
                    String itemOne = ori.substring(0, index);
                    one.setContent(itemOne);
                    one.setType(PrintConstants.PrintContentPartity.COMMON);
                    result.add(one);
                }
                PrintConstants.PartityItem two = new PrintConstants.PartityItem();
                String item = ori.substring(index + start.length(), index_end);
                two.setContent(item);
                two.setType(tyeEnum);
                result.add(two);
                next_index = index_end + end.length();

                if (next_index + 1 > ori.length()) {
                    start = null;
                } else {
                    ori = ori.substring(next_index);
                }
            } else {
                PrintConstants.PartityItem one = new PrintConstants.PartityItem();
                one.setContent(ori);
                one.setType(PrintConstants.PrintContentPartity.COMMON);
                result.add(one);

            }
        } while (start != null);

        return result;

    }

    private static String firstFlag(String content) {
        List<String> startList = getStartList();
        int minIndex = content.length();
        int minPosition = -1;
        for (int i = 0; i < startList.size(); i++) {
            String current = startList.get(i);
            if (content.contains(current)) {
                int position = i;
                int index = content.indexOf(current);
                if (index < minIndex) {
                    minIndex = index;
                    minPosition = position;
                }
            }
        }
        if (minPosition != -1) {
            return startList.get(minPosition);
        }
        return null;
    }

    private static List<String> getStartList() {
        List<String> mList = new ArrayList<>();
        mList.add(PrintConstants.PrintContentPartity.BC.getStart());
        mList.add(PrintConstants.PrintContentPartity.QC.getStart());
        return mList;
    }
}
