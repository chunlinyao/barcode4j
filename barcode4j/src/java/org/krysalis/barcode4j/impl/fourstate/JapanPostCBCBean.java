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

import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.ChecksumMode;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.output.Canvas;
import org.krysalis.barcode4j.output.CanvasProvider;

/**
 * Implements the Japan Post Customer Barcode.
 * 
 * @author Jeremias Maerki
 * @version $Id: JapanPostCBCBean.java,v 1.3 2008/05/13 13:00:43 jmaerki Exp $
 */
public class JapanPostCBCBean extends AbstractFourStateBean {
    private static final String FILLER = "$$$$$$$$$$$$$$$$$$$$";
    
    /** The default module width for RoyalMail. */
    protected static final double DEFAULT_MODULE_WIDTH = 0.53; //mm

    /** Create a new instance. */
    public JapanPostCBCBean() {
        super();
        this.msgPos = HumanReadablePlacement.HRP_NONE; //Different default than normal
        setModuleWidth(DEFAULT_MODULE_WIDTH);
        setTrackHeight(1.25f); //mm
        setAscenderHeight(1.8f); //mm
        setQuietZone(2.0); //mm
        setIntercharGapWidth(getModuleWidth());
        updateHeight();
    }
    
    /** {@inheritDoc} */
    public void setMsgPosition(HumanReadablePlacement placement) {
        //nop, no human-readable with this symbology!!!
    }

    /** {@inheritDoc} */
    public void generateBarcode(CanvasProvider canvas, String msg) {
		validateMessage(msg);
		msg = normalizeMessage(msg);
        msg += FILLER.substring(0, 20 - msg.length());
        FourStateLogicHandler handler = 
                new FourStateLogicHandler(this, new Canvas(canvas));

        JapanPostCBCLogicImpl impl = new JapanPostCBCLogicImpl(
                getChecksumMode());
        impl.generateBarcodeLogic(handler, msg);
    }

    /** {@inheritDoc} */
    public BarcodeDimension calcDimensions(String msg) {
        String modMsg = JapanPostCBCLogicImpl.removeStartStop(msg);
        int additional = (getChecksumMode() == ChecksumMode.CP_ADD 
                || getChecksumMode() == ChecksumMode.CP_AUTO) ? 1 : 0;
        final int len = modMsg.length() + additional;
        final double width = (((len * 4) + 2) * moduleWidth) 
                + (((len * 4) + 1) * getIntercharGapWidth());
        final double qzh = (hasQuietZone() ? getQuietZone() : 0);        
        final double qzv = (hasQuietZone() ? getVerticalQuietZone() : 0);        
        return new BarcodeDimension(width, getBarHeight(), 
                width + (2 * qzh), getBarHeight() + (2 * qzv), 
                qzh, qzv);
    }

	public static void validateMessage(String msg) {
        if ((msg == null) 
                || (msg.length() == 0)) {
            throw new NullPointerException("Barcode string should not be empty");
        }
		/* if only for postal code, uncomment
		if (msg.length() != 7 && msg.length() != 10) {
			throw new IllegalArgumentException("Code should be 7 digits or 10 digits (7 digits postal code + 3 digit reference number");
		}
        for (int i = 0; i < msg.length(); i++) {
            final char c = msg.charAt(i);
            if ((c < '0') || (c > '9')) {
                throw new IllegalArgumentException("Invalid character found:" + c +"."
                    + "Valid are 0-9 only. postal code: " + msg);
            }
        }	*/
	}

    public String normalizeMessage(String msg) {
        String s = msg.toUpperCase();
		s = s.replace("&","").replace("/","").replace("・","").replace(".","");
		s = ((s.startsWith("-") && s.length() > 1)?s.substring(1):s);
        return s;
    }
}
