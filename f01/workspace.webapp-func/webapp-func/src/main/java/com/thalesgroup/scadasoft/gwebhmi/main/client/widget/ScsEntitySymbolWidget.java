package com.thalesgroup.scadasoft.gwebhmi.main.client.widget;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.OMText;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.description.symbol.SymbolClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.update.SymbolInsert;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.widget.SymbolWidget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.zoom.data.ZoomRatio;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.zoom.strategy.IZoomStrategy;

public class ScsEntitySymbolWidget extends SymbolWidget {

    private static final ClientLogger s_logger = ClientLogger.getClientLogger();
    private int m_textXPos;
    private int m_textYPos;

    public ScsEntitySymbolWidget(final SymbolInsert insert, final SymbolClient symbol,
            final IZoomStrategy zoomStrategy) {
        super(insert, symbol, zoomStrategy);

        m_textXPos = -75;
        m_textYPos = 80;
    }

    public void update(EntityClient data) {
        try {
            s_logger.trace("ScsEntitySymbolWidget::update :" + data.toString());
            // do normal MWT update
            // super.update(data);

            // check if eqp name exists
            final AttributeClientAbstract<?> nameAtt = data.getAttribute("eqpName");
            if (nameAtt != null && nameAtt instanceof StringAttribute) {
                final String eqpName = ((StringAttribute) nameAtt).getValue();
                addName(eqpName);
            }
        } catch (Exception e) {
            s_logger.error("Problem in ScsEntitySymbolWidget::update :" + e.getMessage());
        }
        s_logger.trace("ScsEntitySymbolWidget::update done.");
    }

    @Override
    protected void applyWidgetZoomRatio(final ZoomRatio zoomRatio) {
        super.applyWidgetZoomRatio(zoomRatio);
    }

    public void updateScs(EntityClient data) {
        try {
            // super.update(data);

            // check if eqp name exists
            final AttributeClientAbstract<?> nameAtt = data.getAttribute("eqpName");
            if (nameAtt != null && nameAtt instanceof StringAttribute) {
                final String eqpName = ((StringAttribute) nameAtt).getValue();
                addName(eqpName);
            }
        } catch (Exception e) {
            s_logger.error("Problem in ScsEntitySymbolWidget::updateScs :" + e.getMessage());
        }
    }

    private void addName(final String eqpName) {

        if (getSymbolElement() != null) {
            String sid = getSymbolElement().getId();

            // remove .symbol
            if (sid.endsWith(".symbol")) {
                sid = sid.substring(0, sid.length() - 7);
            }

            String eqpNameTextId = sid + ".eqpNameText";

            OMNode child = getSymbolElement().getFirstChild();
            OMSVGTextElement eqpNameText = null;

            while (child != null) {
                if (child instanceof OMElement) {
                    OMElement elt = (OMElement) child;

                    if (eqpNameTextId.equals(elt.getId())) {
                        eqpNameText = (OMSVGTextElement) elt;
                    }
                }
                child = child.getNextSibling();
            }

            if (eqpNameText == null) {
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

                // s_logger.info("EqpName adding text: " + eqpName);
                eqpNameText = new OMSVGTextElement(0, 0, OMSVGLength.SVG_LENGTHTYPE_PX, eqpName);
                eqpNameText.setId(eqpNameTextId);
                eqpNameText.setAttribute(SVGConstants.SVG_TRANSFORM_ATTRIBUTE, newmatrix);

                getSymbolElement().appendChild(eqpNameText);
                eqpNameText.setAttribute(SVGConstants.CSS_FILL_PROPERTY, "#FFFFFF");
                eqpNameText.setAttribute(SVGConstants.CSS_FONT_FAMILY_PROPERTY, "Arial");
                eqpNameText.setAttribute(SVGConstants.CSS_FONT_SIZE_PROPERTY, "10px");
                eqpNameText.setAttribute(SVGConstants.CSS_FONT_WEIGHT_PROPERTY, "italic");

            } else {
                OMText text = (OMText) eqpNameText.getFirstChild();
                text.setData(eqpName);
            }

        }

    }
}
