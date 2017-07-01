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

import java.util.List;
import java.util.Map;

import org.krysalis.barcode4j.ChecksumMode;

/**
 * Abstract base class for Japan Post Customer Barcode and the Dutch KIX Code.
 * 
 * @version $Id: AbstractRMCBCKIXLogicImpl.java,v 1.1 2008/05/13 13:00:43 jmaerki Exp $
 */
public abstract class AbstractJapanPostCBCLogicImpl extends AbstractFourStateLogicImpl {

    private static final Map CHARSET = new java.util.HashMap();
    
    static {
        //0 = track only, 1 = ascender, 2 = descender, 3 = 1 + 2 = full height
        CHARSET.put("(", "32");
        CHARSET.put("[", "32");
        CHARSET.put(")", "23");
        CHARSET.put("]", "23");
        CHARSET.put("0", "300");
        CHARSET.put("1", "330");
        CHARSET.put("2", "321");
        CHARSET.put("3", "231");
        CHARSET.put("4", "312");
        CHARSET.put("5", "303");
        CHARSET.put("6", "213");
        CHARSET.put("7", "132");
        CHARSET.put("8", "123");
        CHARSET.put("9", "033");
        CHARSET.put("A", "210300");
        CHARSET.put("B", "210330");
        CHARSET.put("C", "210321");
        CHARSET.put("D", "210231");
        CHARSET.put("E", "210312");
        CHARSET.put("F", "210303");
        CHARSET.put("G", "210213");
        CHARSET.put("H", "210132");	
        CHARSET.put("I", "210123");
        CHARSET.put("J", "210033");
        CHARSET.put("K", "201300");
        CHARSET.put("L", "201330");
        CHARSET.put("M", "201321");
        CHARSET.put("N", "201231");
        CHARSET.put("O", "201312");
        CHARSET.put("P", "201303");
        CHARSET.put("Q", "201213");
        CHARSET.put("R", "201132");
        CHARSET.put("S", "201123");
        CHARSET.put("T", "201033");
        CHARSET.put("U", "120300");
        CHARSET.put("V", "120330");
        CHARSET.put("W", "120321");
        CHARSET.put("X", "120231");
        CHARSET.put("Y", "120312");
        CHARSET.put("Z", "120213");
        CHARSET.put("-", "030");
        CHARSET.put("$", "021");
    }
    

    /**
     * Main constructor
     * @param mode checksum mode
     */
    public AbstractJapanPostCBCLogicImpl(ChecksumMode mode) {
        super(mode);
    }

    /** {@inheritDoc} */
    protected String[] encodeHighLevel(String msg) {
        List codewords = new java.util.ArrayList(msg.length());
        for (int i = 0, c = msg.length(); i < c; i++) {
            String ch = msg.substring(i, i + 1);
            String code = (String)CHARSET.get(ch);
            if (code == null) {
                throw new IllegalArgumentException("Illegal character: " + ch);
            }
            codewords.add(code);
        }
        return (String[])codewords.toArray(new String[codewords.size()]);
    }



}
