package doktoree.backend.validator;

public class EnumHandler<E extends Enum<E>> {

    private Class<E> enumType;

    public EnumHandler(Class<E> enumType) {
        this.enumType = enumType;
    }

    public boolean isValid(E value) {
        for (E constant : enumType.getEnumConstants()) {
            if (constant == (value)) return true;
        }
        return false;
    }
}

