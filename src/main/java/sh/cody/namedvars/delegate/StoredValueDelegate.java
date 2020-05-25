package sh.cody.namedvars.delegate;

/**
 * A {@link Delegate} implementation that can be used when a variable has no preexisting storage location.
 *
 * <b>Note:</b> this implementation of {@link Delegate} lacks a constructor supported by
 * {@link sh.cody.namedvars.annotation.GenerateVariableResolver} so it <b>can not</b> be specified a delegate in the
 * {@link sh.cody.namedvars.annotation.GenerateVariable} annotation.
 *
 * @param <T> the type of the stored value
 */
public final class StoredValueDelegate<T> implements Delegate<T> {
    private T value;

    public StoredValueDelegate(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public void set(T value) {
        this.value = value;
    }
}
