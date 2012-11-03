package jp.vmi.selenium.selenese.cmdproc;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverCommandProcessor;
import org.openqa.selenium.internal.seleniumemulation.SeleneseCommand;

/**
 * WebDriverCommandProcessor no timeout version.
 */
public class CustomCommandProcessor extends WebDriverCommandProcessor {

    private final Map<String, String> varsMap = new HashMap<String, String>();

    /**
     * Constructor.
     *
     * @param baseUrl base URL.
     * @param driver WebDriver instance.
     */
    public CustomCommandProcessor(String baseUrl, WebDriver driver) {
        super(baseUrl, driver);
    }

    @Override
    public String doCommand(String commandName, String[] args) {
        Object val = execute(commandName, args);
        return val != null ? val.toString() : null;
    }

    @Override
    public String getString(String commandName, String[] args) {
        return (String) execute(commandName, args);
    }

    @Override
    public String[] getStringArray(String commandName, String[] args) {
        return (String[]) execute(commandName, args);
    }

    @Override
    public Number getNumber(String commandName, String[] args) {
        return (Number) execute(commandName, args);
    }

    @Override
    public boolean getBoolean(String commandName, String[] args) {
        return (Boolean) execute(commandName, args);
    }

    private Object execute(String commandName, final String[] args) {
        final SeleneseCommand<?> command = getMethod(commandName);
        if (command == null)
            throw new UnsupportedOperationException(commandName);
        return command.apply(getWrappedDriver(), replaceVarsForArray(args));
    }

    /**
     * Set variable value.
     *
     * @param value value.
     * @param varName variable name.
     */
    public void setVar(String value, String varName) {
        varsMap.put(varName, value);
    }

    /**
     * Replace variable reference to value.
     *
     * @param expr expression string.
     * @return replaced string.
     */
    public String replaceVars(String expr) {
        StrSubstitutor s = new StrSubstitutor(varsMap);
        return s.replace(expr);
    }

    /**
     * Replace variable reference to value for each strings.
     *
     * @param exprs expression strings.
     * @return replaced strings.
     */
    public String[] replaceVarsForArray(String[] exprs) {
        String[] result = new String[exprs.length];
        for (int i = 0; i < exprs.length; i++)
            result[i] = replaceVars(exprs[i]);
        return result;
    }
}
