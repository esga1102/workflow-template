package wf.design.core.context;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class ContextModel {
    private ExtendConcurrentHashMap<String, Object> fullContext = new ExtendConcurrentHashMap<>();
    private ExtendConcurrentHashMap<String, String> stringContext = new ExtendConcurrentHashMap<>();

    private Throwable throwable;
    public boolean isRightTree = true;

    public Object getStringContext(String key) {
        Object val = fullContext.get(key);
        if (val == null) {
            val = stringContext.get(key);
            if (val != null) {
                fullContext.put(key, val);
            }
        }
        return val;
    }

    public <T> T getStringContext(String key, Class<T> type) {
        Object value = getStringContext(key);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    public <T> List<T> getContextItems(String key, Class<T> elementType) {
        Object value = getStringContext(key);

        if (value instanceof List<?>) {
            return ((List<?>) value).stream()
                    .map(elementType::cast)
                    .toList();
        }

        return Collections.emptyList();
    }

    public void popContexts(List<String> excludes) {
        final HashSet<String> all = new HashSet<>();
        all.addAll(stringContext.keySet());
        all.addAll(fullContext.keySet());
        all.stream().filter(k -> excludes == null ||
                excludes.isEmpty() ||
                !excludes.contains(k)).forEach(k -> {
            fullContext.remove(k);
            stringContext.remove(k);
        });
    }

    public Object popContext(String key) {
        Object val = fullContext.remove(key);
        if (val == null) {
            val = stringContext.remove(key);
        }
        stringContext.remove(key);
        return val;
    }

    public <T> T popExtra(String key, Class<T> type) {
        return (T) popContext(key);
    }

    public ContextModel putContext(String key, Object val) {
        if (val != null) {
            fullContext.put(key, val);
            var check = val instanceof String;
            if (check) {
                stringContext.put(key, (String) val);
            }
        }
        return this;
    }


    public void setResult(boolean isOk) {
        this.isRightTree = isOk;
    }

    public void setResult(boolean isOk, Throwable throwable) {
        this.isRightTree = isOk;
        this.throwable = throwable;
    }

    public void setResult(Throwable throwable) {
        this.isRightTree = false;
        this.throwable = throwable;
    }

    public boolean getRightFlag() {
        return this.isRightTree;
    }
}