package com.thalesgroup.scadasoft.gwebhmi.main.client.widget;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.OMText;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.i18n.client.NumberFormat;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.FloatAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.description.symbol.SymbolClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.update.SymbolInsert;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.zoom.strategy.IZoomStrategy;

public class CamEntitySymbolWidget extends ScsEntitySymbolWidget {
    private static final ClientLogger s_logger = ClientLogger.getClientLogger();

    private String firstMatrix_ = null;
    private int m_textXPos;
    private int m_textYPos;
    private float m_angle = 0.0f;

    public CamEntitySymbolWidget(final SymbolInsert insert, final SymbolClient symbol,
            final IZoomStrategy zoomStrategy) {

        super(insert, symbol, zoomStrategy);

        m_textXPos = -42;
        m_textYPos = 35;
    }

    @Override
    public void update(EntityClient data) {
        try {
            // do normal SCSEqp update
            super.updateScs(data);

            // do camera rotation
            final AttributeClientAbstract<?> angleAttr = data.getAttribute("angle");
            if (angleAttr != null && angleAttr instanceof FloatAttribute) {
                try {
                    final Float angle = Float.valueOf(angleAttr.getValue().toString());
                    if (angle != m_angle) {
                        // s_logger.info("CamEntitySymbolWidget try to rotate
                        // symbol " + angle.toString());
                        rotateInnerCameraSymbol(angle);
                        m_angle = angle;
                    }
                } catch (NumberFormatException e) {
                    s_logger.error(
                            "update CamEntitySymbolWidget cannot get Float from " + angleAttr.getValue().toString());
                }
            }
        } catch (Exception e) {
            s_logger.error("Problem in ScsEntitySymbolWidget::update :" + e.getMessage());
        }
        s_logger.trace("CamEntitySymbolWidget::update done.");
    }

    private void rotateInnerCameraSymbol(final Float angle) {
        String textValue = NumberFormat.getFormat("#.#").format(angle);
        textValue += " °";

        if (getSymbolElement() != null) {
            String sid = getSymbolElement().getId();

            // remove .symbol
            if (sid.endsWith(".symbol")) {
                sid = sid.substring(0, sid.length() - 7);
            }
            String camSymbolId = sid + ".state";
            String camTextId = sid + ".angleText";

            OMNode child = getSymbolElement().getFirstChild();
            OMSVGTextElement angleText = null;

            while (child != null) {
                if (child instanceof OMElement) {
                    OMElement elt = (OMElement) child;

                    if (camSymbolId.equals(elt.getId())) {
                        String matrix = elt.getAttribute(SVGConstants.SVG_TRANSFORM_ATTRIBUTE);
                        String newmatrix = doRotate(matrix, angle);
                        elt.setAttribute(SVGConstants.SVG_TRANSFORM_ATTRIBUTE, newmatrix);
                    } else if (camTextId.equals(elt.getId())) {
                        angleText = (OMSVGTextElement) elt;
                    }
                }
                child = child.getNextSibling();
            }

            if (angleText == null) {

                // s_logger.info("CamEntitySymbolWidget adding text svg element:
                // " + textValue);
                String matrix = getSymbolElement().getAttribute(SVGConstants.SVG_TRANSFORM_ATTRIBUTE);
                // s_logger.info("EqpName matrix: " + matrix);
                // extract zoom
                float zoomFactor = 1.0f;
                String[] ftab = matrix.split(" ");
                if (ftab.length >= 4) {
                    try {
                        zoomFactor = Float.parseFloat(ftab[3]);
                    } catch (NumberFormatException e) {
                        zoomFactor = 1.0f;
                    }
                    zoomFactor = 1 / zoomFactor;
                }
                String newmatrix = "matrix(" + Float.toString(zoomFactor) + " 0 0 " + Float.toString(zoomFactor) + " "
                        + Integer.toString(m_textXPos) + " " + Integer.toString(m_textYPos) + ")";
                // s_logger.info("EqpName newmatrix: " + newmatrix);

                angleText = new OMSVGTextElement(0, 0, OMSVGLength.SVG_LENGTHTYPE_PX, textValue);
                angleText.setId(camTextId);
                getSymbolElement().appendChild(angleText);
                angleText.setAttribute(SVGConstants.SVG_TRANSFORM_ATTRIBUTE, newmatrix);
                angleText.setAttribute(SVGConstants.CSS_FILL_PROPERTY, "#FFFFFF");
                angleText.setAttribute(SVGConstants.CSS_FONT_FAMILY_PROPERTY, "Arial");
                angleText.setAttribute(SVGConstants.CSS_FONT_SIZE_PROPERTY, "14px");
                angleText.setAttribute(SVGConstants.CSS_FONT_WEIGHT_PROPERTY, "bold");

            } else {
                // s_logger.info("CamEntitySymbolWidget changing text svg
                // element to: " + textValue);
                OMText text = (OMText) angleText.getFirstChild();
                text.setData(textValue);
            }

        }

    }

    private String doRotate(String matrix, Float angle) {
        if (firstMatrix_ == null) {
            firstMatrix_ = matrix;
        }

        final String transform = "rotate(" + Float.toString(angle) + ") " + firstMatrix_;

        return transform;
    }

}
