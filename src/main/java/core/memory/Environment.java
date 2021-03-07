package core.memory;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;


@NoArgsConstructor
@AllArgsConstructor
public class Environment {

    private Environment enclosing;

    private final Map<String, Object> values = new HashMap<>();

    public void define( String variableName, Object value ) {
        values.put(variableName, value);
    }

    public Object getValue(String variableName ) {
        if (values.containsKey(variableName)) {
            return values.get(variableName);
        }
        if (enclosing != null)  { return enclosing.getValue(variableName); }
        return null;
    }

    public void assign(String variableName, Object value)  {
        if (values.containsKey(variableName)) {
            values.put(variableName, value);
            return;
        }

        if ( enclosing != null ) {
            enclosing.assign(variableName,value);
        }

        // @todo: add an error here, undefined variable
    }

}
