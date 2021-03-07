package core;

public enum Keywords {
    AND("மற்றும்"),
    CLASS("பெட்டி"),
    ELSE("இல்லை என்றால்"),
    FALSE("தவறு"),
    FUN("செயல்பாடு"),
    FOR("ஆக"),
    IF("ஒரு வேலை"),
    NIL("இல்லை"),
    OR("அல்லது"),
    PRINT("எழுது"),
    RETURN("திருப்பு"),
    TRUE("உண்மை"),
    VAR("மாறி"),
    WHILE("போது");
    private final String value;

    Keywords(String v) {
        value = v;
    }


    }
