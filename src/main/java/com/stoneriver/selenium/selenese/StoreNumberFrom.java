/**
 * 
 */
package com.stoneriver.selenium.selenese;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.vmi.selenium.selenese.Runner;
import jp.vmi.selenium.selenese.TestCase;
import jp.vmi.selenium.selenese.command.Command;
import jp.vmi.selenium.selenese.result.Failure;
import jp.vmi.selenium.selenese.result.Result;

import static jp.vmi.selenium.selenese.result.Success.*;

/**
 * Implements command <code>storeNumberFrom</code>
 * 
 * Stores number found in the locator as a variable. 
 * 
 * @author esharapov
 *
 */
public class StoreNumberFrom extends Command {
    private static final Logger log = LoggerFactory.getLogger(StoreNumberFrom.class);
    // index of the locator
    protected static final int LOCATOR = 0;
    // index of the variable name
    protected static final int VAR_NAME = 1;

    /**
     * @param index
     * @param name
     * @param args
     * @param argCnt
     * @param locatorIndexes
     */
    public StoreNumberFrom(int index, String name, String[] args, int argCnt, int[] locatorIndexes) {
        super(index, name, args, argCnt, locatorIndexes);
    }

    /**
     * @param index
     * @param name
     * @param args
     * @param argCnt
     */
    public StoreNumberFrom(int index, String name, String[] args, int argCnt) {
        super(index, name, args, argCnt);
    }

    @Override
    protected Result doCommandImpl(TestCase testCase, Runner runner) {
        String locator = testCase.getProc().replaceVars(args[LOCATOR]);
        String varName = testCase.getProc().replaceVars(args[VAR_NAME]);
        By finder = createFinder(locator);
        if (finder != null) {
            WebElement element = runner.getDriver().findElement(finder);
            try {
                Number num = NumberFormat.getInstance().parse(element.getText());
                testCase.getProc().setVar(num, varName);
                return SUCCESS;
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
        }
        return new Failure("Could not find '" + locator + "'");

    }

    private static Map<String, Method> byMap = new HashMap<String, Method>();

    private static Method getMethod(Class cls, String name) {
        for (Method m : cls.getMethods()) {
            if (m.getName().equals(name))
                return m;
        }
        return null;
    }

    static {
        byMap.put("id", getMethod(By.class, "id"));
        byMap.put("css", getMethod(By.class, "cssSelector"));
        byMap.put("xpath", getMethod(By.class, "xpath"));
        byMap.put("link", getMethod(By.class, "linkText"));
        byMap.put("name", getMethod(By.class, "name"));
    }

    private By createFinder(String locator) {
        if (locator != null) {
            Pattern p = Pattern.compile("([^=]+)=(.+)");
            Matcher m = p.matcher(locator);
            if (m != null && m.matches()) {
                if (m.groupCount() == 2) {
                    String locatorType = m.group(1);
                    String locatorValue = m.group(2);
                    if (byMap.containsKey(locatorType) && byMap.get(locatorType) != null) {
                        try {
                            return (By) byMap.get(locatorType).invoke(null, locatorValue);
                        } catch (IllegalArgumentException e) {
                            log.error(e.getMessage(), e);
                            return null;
                        } catch (IllegalAccessException e) {
                            log.error(e.getMessage(), e);
                            return null;
                        } catch (InvocationTargetException e) {
                            log.error(e.getMessage(), e);
                            return null;
                        }
                    } else {
                        log.error("Couldn't figure out location of the element: '" + locator + "'");
                        return null;
                    }
                } else {
                    log.error("Couldn't figure out location of the element: '" + locator + "'");
                    return null;
                }

            } else {
                log.error("Couldn't figure out location of the element: '" + locator + "'");
                return null;
            }
        } else {
            log.error("Couldn't figure out location of the element: '" + locator + "'");
            return null;
        }

    }
}
