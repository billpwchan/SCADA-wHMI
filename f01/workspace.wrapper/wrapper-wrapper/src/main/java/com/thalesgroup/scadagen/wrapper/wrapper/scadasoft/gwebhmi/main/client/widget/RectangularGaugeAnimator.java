// =============================================================================
// THALES COMMUNICATIONS & SECURITY
//
// Copyright (c) THALES 2015 All rights reserved.
// This file and the information it contains are property of THALES COMMUNICATIONS &
// SECURITY and confidential. They shall not be reproduced nor disclosed to any
// person except to those having a need to know them without prior written
// consent of COMMUNICATIONS & SECURITY .
//
package com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.widget;

import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPathSegList;

import com.google.gwt.touch.client.Point;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.symbol.animation.ClientCustomAnimation;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.symbol.animation.ClientNodeAnimationAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.symbol.animator.ClientAnimatorAbstract;

/**
 * Base class for the rectangular gauge animation.
 */
public class RectangularGaugeAnimator extends ClientAnimatorAbstract {

    private final double width_;
    private final double bottom_;
    private final double top_;

    /**
     * Constructor.
     * @param bottom the y of the rectangle's bottom.
     * @param top the y of the rectangle's top.
     * @param width the width of the rectangle.
     */
    public RectangularGaugeAnimator(final double bottom, final double top, final double width) {
        bottom_ = bottom;
        top_ = top;
        width_ = width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void animate(final OMSVGElement target, final ClientNodeAnimationAbstract< ? > animation) {

        final double computedValue;
        if (animation instanceof ClientCustomAnimation) {
            final ClientCustomAnimation clientCustomAnimation = (ClientCustomAnimation) animation;

            if (clientCustomAnimation.getValue() == null || clientCustomAnimation.getValue().isEmpty()) {
                computedValue = 0;
            } else {
                double value = Double.parseDouble(clientCustomAnimation.getValue());
                //CHECKSTYLE:OFF MagicNumberCheck Removes check
                value = Math.min(1, value);
                value = Math.max(0, value);
                //CHECKSTYLE:ON MagicNumberCheck

                computedValue = value;

            }

        } else {
            computedValue = 0;
        }


        if (target instanceof OMSVGPathElement) {
            final OMSVGPathElement path = (OMSVGPathElement) target;
            changeGaugePath(path, computedValue);
        }

    }

    private void changeGaugePath(final OMSVGPathElement path, final double computedValue) {

        final OMSVGPathSegList pathSegList = path.getPathSegList();

        pathSegList.clear();

        final double top = bottom_ + (top_ - bottom_) * computedValue;

        final Point p1 = new Point(-width_ / 2, bottom_);
        final Point p2 = new Point(width_ / 2, bottom_);
        final Point p3 = new Point(width_ / 2, top);
        final Point p4 = new Point(-width_ / 2, top);

        pathSegList.initialize(path.createSVGPathSegMovetoAbs((float) p1.getX(),
            (float) p1.getY()));

        pathSegList.appendItem(path.createSVGPathSegLinetoAbs((float) p2.getX(),
            (float) p2.getY()));

        pathSegList.appendItem(path.createSVGPathSegLinetoAbs((float) p3.getX(),
                (float) p3.getY()));

        pathSegList.appendItem(path.createSVGPathSegLinetoAbs((float) p4.getX(),
                (float) p4.getY()));

        pathSegList.appendItem(path.createSVGPathSegLinetoAbs((float) p1.getX(),
                (float) p1.getY()));

        pathSegList.appendItem(path.createSVGPathSegClosePath());

    }

}
