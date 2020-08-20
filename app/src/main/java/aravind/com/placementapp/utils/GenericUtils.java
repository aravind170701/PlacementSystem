package aravind.com.placementapp.utils;

import java.util.Collection;

public abstract class GenericUtils {
    public static <T> boolean isNullOrEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }
}
