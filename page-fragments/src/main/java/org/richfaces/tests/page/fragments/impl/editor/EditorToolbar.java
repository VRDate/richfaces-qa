/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.tests.page.fragments.impl.editor;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class EditorToolbar {

    public enum EditorMode {

        SOURCE("cke_button_source"),
        SPELL_CHECK_AS_YOU_TYPE("cke_button_scayt"),
        BOLD("cke_button_bold"),
        ITALIC("cke_button_italic"),
        UNDERLINE("cke_button_underline"),
        STRIKE_THROUGH("cke_button_strike"),
        SUBSCRIPT("cke_button_subscript"),
        SUPERSCRIPT("cke_button_superscript"),
        NUMBERED_LIST("cke_button_numberedlist"),
        BULLETED_LIST("cke_button_bulletedlist"),
        BLOCK_QUOTE("cke_button_blockquote"),
        MAXIMIZE("cke_button_maximize"),
        SHOW_BLOCKS("cke_button_showblocks");

        private final String className;

        private EditorMode(String className) {
            this.className = className;
        }

        @Override
        public String toString() {
            return className;
        }
    }

    public enum EditorTextDirection {

        FROM_RIGHT_TO_LEFT("cke_button_bidirtl"),
        FROM_LEFT_TO_RIGHT("cke_button_bidiltr");

        private final String className;

        private EditorTextDirection(String className) {
            this.className = className;
        }

        @Override
        public String toString() {
            return this.className;
        }
    }

    public enum EditorTextAligns {
        LEFT("cke_button_justifyleft"),
        CENTER("cke_button_justifycenter"),
        RIGHT("cke_button_justifyright"),
        BLOCK("cke_button_justifyblock");

        private final String className;

        private EditorTextAligns(String className) {
            this.className = className;
        }

        @Override
        public String toString() {
            return this.className;
        }
    }

    public enum EditorButton {
        SAVE("cke_button_save"),
        NEW_PAGE("cke_button_newpage"),
        PREVIEW("cke_button_preview"),
        PRINT("cke_button_print"),
        TEMPLATES("cke_button_templates"),
        CUT("cke_button_cut"),
        COPY("cke_button_copy"),
        PASTE("cke_button_paste"),
        PASTE_AS_PLAIN_TEXT("cke_button_pastetext"),
        PASTE_FROM_WORD("cke_button_pastefromword"),
        UNDO("cke_button_undo"),
        REDO("cke_button_redo"),
        FIND("cke_button_find"),
        REPLACE("cke_button_replace"),
        SELECT_ALL("cke_button_selectAll"),
        CHECK_SPELLING("cke_button_checkspell"),
        FORM("cke_button_form"),
        CHECKBOX("cke_button_checkbox"),
        RADIO_BUTTON("cke_button_radio"),
        TEXT_FIELD("cke_button_textfield"),
        TEXT_AREA("cke_button_textarea"),
        SELECTION_FIELD("cke_button_select"),
        BUTTON("cke_button_button"),
        IMAGE_BUTTON("cke_button_imagebutton"),
        HIDDEN_FIELD("cke_button_hiddenfield"),
        REMOVE_FORMAT("cke_button_removeFormat"),
        DECREASE_INDENT("cke_button_outdent"),
        INCREASE_INDENT("cke_button_indent"),
        CREATE_DIV("cke_button_creatediv"),
        LINK("cke_button_link"),
        UNLINK("cke_button_unlink"),
        ANCHOR("cke_button_anchor"),
        IMAGE("cke_button_image"),
        FLASH("cke_button_flash"),
        TABLE("cke_button_table"),
        INSERT_HORIZONTAL_RULE("cke_button_horizontalrule"),
        SMILEY("cke_button_smiley"),
        INSERT_SPECIAL_CHARACTER("cke_button_specialchar"),
        INSERT_PAGE_BREAK_FOR_PRINTING("cke_button_pagebreak"),
        IFRAME("cke_button_iframe"),
        ABOUT_CKE_EDITOR("cke_button_about");

        private final String className;

        private EditorButton(String className) {
            this.className = className;
        }

        @Override
        public String toString() {
            return className;
        }
    }
}
