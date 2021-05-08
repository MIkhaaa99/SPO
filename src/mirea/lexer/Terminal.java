package mirea.lexer;

import java.util.regex.Pattern;

public class Terminal {

    private final String identifier;
    private final Pattern pattern;
    private final Integer priority;
    private final boolean ignore;

    public Terminal(String identifier, String pattern) {
        this(identifier, pattern, 0);
    }

    public Terminal(String identifier, String pattern, Integer priority) {
        this.identifier = identifier;
        this.pattern = Pattern.compile(pattern);
        this.priority = priority;
        this.ignore = false;
    }

    public Terminal(String identifier, String pattern, boolean ignore) {
        this.identifier = identifier;
        this.pattern = Pattern.compile(pattern);
        this.priority = 0;
        this.ignore = ignore;
    }

    public boolean matches(CharSequence charSequence) {
        return pattern.matcher(charSequence).matches();
    }


    public String getIdentifier() {
        return identifier;
    }

    public Integer getPriority() {
        return priority;
    }

    public boolean isIgnore() {
        return ignore;
    }
}
