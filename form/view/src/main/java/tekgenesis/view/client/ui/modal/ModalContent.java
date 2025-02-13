
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.modal;

import com.google.gwt.user.client.ui.Widget;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.widget.FooterType;
import tekgenesis.view.client.FormBox;

import static tekgenesis.metadata.form.widget.FooterType.OK_CANCEL;

public class ModalContent {

    //~ Instance Fields ..............................................................................................................................

    private Widget        body;
    private boolean       bodyOnly;
    private boolean       clickOutsideDisabled;
    private boolean       closeButton;
    private boolean       fixed;
    private Widget        footer;
    private FooterType    footerType;
    private FormBox       formBox;
    private Widget        header;
    private ModalListener listener;
    private String        modalClassName;

    private String  title;
    private boolean transparent;

    //~ Constructors .................................................................................................................................

    public ModalContent() {
        body                 = null;
        footerType           = null;
        bodyOnly             = false;
        footer               = null;
        formBox              = null;
        header               = null;
        listener             = null;
        modalClassName       = null;
        title                = null;
        transparent          = false;
        closeButton          = false;
        fixed                = false;
        clickOutsideDisabled = false;
    }

    //~ Methods ......................................................................................................................................

    public Widget getBody() {
        return body;
    }

    public ModalContent setBody(Widget b) {
        body = b;
        return this;
    }

    public ModalContent setBodyOnly(boolean bOnly) {
        bodyOnly = bOnly;
        return this;
    }

    public void setClickOutsideDisabled(boolean clickOutsideDisabled) {
        this.clickOutsideDisabled = clickOutsideDisabled;
    }

    public void setCloseButton(boolean closeButton) {
        this.closeButton = closeButton;
    }

    public boolean isClickOutsideDisabled() {
        return clickOutsideDisabled;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public Widget getFooter() {
        return footer;
    }

    public void setFooter(Widget footer) {
        this.footer = footer;
    }

    public FooterType getFooterType() {
        return footerType == null ? OK_CANCEL : footerType;
    }

    public void setFooterType(@NotNull final FooterType modalButtons) {
        footerType = modalButtons;
    }

    public FormBox getFormBox() {
        return formBox;
    }

    public ModalContent setFormBox(FormBox fBox) {
        formBox = fBox;
        return this;
    }

    public Widget getHeader() {
        return header;
    }

    public void setHeader(Widget header) {
        this.header = header;
    }

    public ModalListener getListener() {
        return listener;
    }

    public ModalContent setListener(@Nullable ModalListener l) {
        listener = l;
        return this;
    }

    public String getModalClassName() {
        return modalClassName;
    }

    public void setModalClassName(String modalClassName) {
        this.modalClassName = modalClassName;
    }

    public boolean isCloseButton() {
        return closeButton;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public String getTitle() {
        return title;
    }

    public ModalContent setTitle(String t) {
        title = t;
        return this;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public boolean isBodyOnly() {
        return bodyOnly;
    }
}  // end class ModalContent
