package sh.cody.namedvars.test;

import sh.cody.namedvars.delegate.FieldDelegate;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unchecked")
public class StringSetDelegate extends FieldDelegate<String> {
    public StringSetDelegate(Object instance, Field field) {
        super(instance, field);
    }

    private Set<String> getSet() {
        if(this.readField() == null) {
            this.writeField(new HashSet<String>());
        }

        return (Set<String>) this.readField();
    }

    @Override
    public String get() {
        return String.join(",", getSet());
    }

    @Override
    public void set(String value) {
        getSet().clear();
        getSet().addAll(Arrays.asList(value.split(",")));
    }
}
