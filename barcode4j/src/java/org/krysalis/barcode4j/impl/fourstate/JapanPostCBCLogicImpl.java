/*
 * Copyright 2016 Hitoshi Ozawa ozawa_h@worksap.co.jp
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.krysalis.barcode4j.impl.fourstate;

import org.krysalis.barcode4j.ChecksumMode;

/**
 * Implements the Japan Post Customer Barcode (CBC).
 * 
 * @version $Id: JapanPostCBCLogicImpl.java,v 1.2 2008/05/13 13:00:43 jmaerki Exp $
 */
public class JapanPostCBCLogicImpl extends AbstractJapanPostCBCLogicImpl {

    /**
     * Main constructor
     * @param mode checksum mode
     */
    public JapanPostCBCLogicImpl(ChecksumMode mode) {
        super(mode);
    }

    /** {@inheritDoc} */
    public char calcChecksum(String msg) {

//	msg += FILLER.substring(0, 20 - msg.length());

    //String[] codewords = encodeHighLevel(removeStartStop(msg));
    char[] charArray = msg.toCharArray();
    int sum = 0;
    
    for (int i = 0; i < charArray.length; i++) {
        if (Character.isDigit(charArray[i])){
        	sum += Character.getNumericValue(charArray[i]);
        } else {
            switch ( charArray[i]){
            case '-': sum = sum + 10;break;
            case '$': sum = sum + 14;break;
            }
        }
    }
    int multiple = (sum/19) * 19 + 19;
    int chkdigit = (multiple - sum)%10;
//    System.out.println("msg:" + msg + " length:" + msg.length() + " multiple:" + multiple + " sum:" + sum + " check:" + ((char)('0' + chkdigit)));
    return (char)('0' + chkdigit);
    }

    /**
     * Handles the checksum, either checking if the right value was specified or adding the
     * missing checksum depending on the settings.
     * @param msg the message
     * @return the (possibly) modified message
     */
    protected String handleChecksum(String msg) {
        if (getChecksumMode() == ChecksumMode.CP_ADD 
                || getChecksumMode() == ChecksumMode.CP_AUTO) {
            return msg + calcChecksum(msg);
        } else if (getChecksumMode() == ChecksumMode.CP_CHECK) {
            if (!validateChecksum(msg)) {
                throw new IllegalArgumentException("Message '" 
                    + msg
                    + "' has a bad checksum. Expected: " 
                    + calcChecksum(msg.substring(0, msg.length() - 1)));
            }
            return msg;
        } else if (getChecksumMode() == ChecksumMode.CP_IGNORE) {
            return msg;
        } else {
            throw new UnsupportedOperationException(
                    "Unknown checksum mode: " + getChecksumMode());
        }
    }

    /**
     * Removes the start and stop characters from the message.
     * @param msg the message
     * @return the modified message
     */
    public static String removeStartStop(String msg) {
        StringBuffer sb = new StringBuffer(msg.length());
        for (int i = 0, c = msg.length(); i < c; i++) {
            char ch = msg.charAt(i);
            switch (ch) {
            case '(':
            case '[':
            case ')':
            case ']':
                break;
            default:
                sb.append(ch);
            }
        }
        return sb.toString();
    }
    
    /** {@inheritDoc} */
    public String normalizeMessage(String msg) {
        String s = removeStartStop(msg);
        s = handleChecksum(s);
        return "(" + s + ")";
    }
    
}
