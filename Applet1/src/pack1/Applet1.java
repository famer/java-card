/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pack1;

import javacard.framework.*;

/**
 *
 * @author bucekj
 */
public class Applet1 extends Applet {

    byte [] pole1;
    /**
     * Installs this applet.
     * 
     * @param bArray
     *            the array containing installation parameters
     * @param bOffset
     *            the starting offset in bArray
     * @param bLength
     *            the length in bytes of the parameter data in bArray
     */
    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new Applet1();
    }

    /**
     * Only this class's install method should create the applet object.
     */
    protected Applet1() {
        pole1 = JCSystem.makeTransientByteArray((short)128, JCSystem.CLEAR_ON_RESET);
        //pole1 = new byte[128];
        register();
    }

    /**
     * Processes an incoming APDU.
     * 
     * @see APDU
     * @param apdu
     *            the incoming APDU
     */
    public void process(APDU apdu) {
        short len = 0;
        byte [] buf = apdu.getBuffer();
        if (selectingApplet())
            ISOException.throwIt(ISO7816.SW_NO_ERROR);
        byte ins = buf[ISO7816.OFFSET_INS];
        switch(ins) {
            case 0x01:
                len = apdu.setIncomingAndReceive();
                Util.arrayCopyNonAtomic(buf, ISO7816.OFFSET_CDATA, pole1, (short)0, len);
                break;
            case 0x00:
                len = apdu.setOutgoing();
                apdu.setOutgoingLength(len);
                if (len > pole1.length) len = (short)pole1.length;
                apdu.sendBytesLong(pole1, (short)0, len);
                break;
            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
        // no error
    }
}
