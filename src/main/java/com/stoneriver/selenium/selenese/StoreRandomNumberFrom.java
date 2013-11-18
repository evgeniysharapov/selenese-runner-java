/**
 * 
 */
package com.stoneriver.selenium.selenese;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.vmi.selenium.selenese.Runner;
import jp.vmi.selenium.selenese.TestCase;
import jp.vmi.selenium.selenese.command.Command;
import jp.vmi.selenium.selenese.result.Failure;
import jp.vmi.selenium.selenese.result.Result;
import jp.vmi.selenium.selenese.result.Success;

/**
 * Stores random number from the given range in a variable.
 * @author esharapov
 *
 */
public class StoreRandomNumberFrom extends Command {
    private static final Logger log = LoggerFactory.getLogger(StoreRandomNumberFrom.class);
    // index of the locator
    protected static final int RANGE = 0;
    // index of the variable name
    protected static final int VAR_NAME = 1;

    /**
     * @param index
     * @param name
     * @param args
     * @param argCnt
     * @param locatorIndexes
     */
    public StoreRandomNumberFrom(int index, String name, String[] args, int argCnt, int[] locatorIndexes) {
        super(index, name, args, argCnt, locatorIndexes);
    }

    /**
     * @param index
     * @param name
     * @param args
     * @param argCnt
     */
    public StoreRandomNumberFrom(int index, String name, String[] args, int argCnt) {
        super(index, name, args, argCnt);
    }

    @Override
    protected Result doCommandImpl(TestCase testCase, Runner runner) {
        String range = testCase.getProc().replaceVars(args[RANGE]);
        String varName = testCase.getProc().replaceVars(args[VAR_NAME]);
        Pattern p = Pattern.compile("(-?[0-9]+),(-?[0-9]+)");
        Matcher m = p.matcher(range);
        if (m != null && m.groupCount() == 2) {
            try {
                int min = Integer.parseInt(m.group(1));
                int max = Integer.parseInt(m.group(2));
                if (min <= max) {
                    Random r = new Random();
                    int value = min + r.nextInt(max - min);
                    testCase.getProc().setVar(value, varName);
                } else {
                    return new Failure("Illegal range [" + min + ", " + max + "]");
                }
            } catch (NumberFormatException e) {
                log.error(e.getMessage(), e);
                return new Failure(e);
            }
        }

        return Success.SUCCESS;
    }

}
