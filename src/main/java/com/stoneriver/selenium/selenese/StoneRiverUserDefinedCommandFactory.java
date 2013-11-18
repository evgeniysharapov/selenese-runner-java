/**
 * 
 */
package com.stoneriver.selenium.selenese;

import jp.vmi.selenium.selenese.command.Command;
import jp.vmi.selenium.selenese.command.UserDefinedCommandFactory;

/**
 * 
 * Returns commands that we define in the user-extensions.js file.
 * 
 * @author esharapov
 *
 */
public class StoneRiverUserDefinedCommandFactory implements UserDefinedCommandFactory {

    /**
     * 
     */
    public StoneRiverUserDefinedCommandFactory() {
    }

    /* (non-Javadoc)
     * @see jp.vmi.selenium.selenese.command.UserDefinedCommandFactory#newCommand(int, java.lang.String, java.lang.String[])
     */
    @Override
    public Command newCommand(int index, String name, String... args) {
        if ("storeNumberFrom".equals(name)) {
            return new StoreNumberFrom(index, name, args, args.length);
        } else if ("storeRandomNumber".equals(name)) {
            return new StoreRandomNumberFrom(index, name, args, args.length);
        } else {
            return null;
        }
    }

}
