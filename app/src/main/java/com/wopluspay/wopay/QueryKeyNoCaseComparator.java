package com.wopluspay.wopay;

import java.util.Comparator;

public class QueryKeyNoCaseComparator implements Comparator<String>
{
    /**
     * {@inheritDoc}
     */

    @Override
    public int compare(String o1, String o2)
    {
        return o1.toLowerCase().compareTo(o2.toLowerCase());
    }
}
