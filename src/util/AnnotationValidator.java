
package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import model.annotation.Positive;
import model.annotation.MaxLength;

public class AnnotationValidator {

    public static String[] validate(Object obj) {

        ArrayList<String> errors = new ArrayList<>();

        Class<?> current = obj.getClass();

        while (current != null) {

            Field[] fields = current.getDeclaredFields();

            for (Field field : fields) {

                field.setAccessible(true);

                try {
                    Object value = field.get(obj);

                    if (field.isAnnotationPresent(Positive.class)) {
                        Positive positive =
                                field.getAnnotation(Positive.class);

                        if (value instanceof Number
                                && ((Number) value).doubleValue() <= 0) {
                            errors.add(field.getName() + " "
                                    + positive.message());
                        }
                    }

                    if (field.isAnnotationPresent(MaxLength.class)) {
                        MaxLength maxLength =
                                field.getAnnotation(MaxLength.class);

                        if (value instanceof String
                                && ((String) value).length()
                                > maxLength.value()) {
                            errors.add(field.getName()
                                    + " must have at most "
                                    + maxLength.value() + " characters");
                        }
                    }

                } catch (IllegalAccessException e) {
                    errors.add("Unable to validate field: "
                            + field.getName());
                }
            }

            current = current.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}
