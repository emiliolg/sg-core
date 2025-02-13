
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import javax.swing.*;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.lang.mm.psi.MMCommonComposite;
import tekgenesis.lang.mm.psi.MMFile;

/**
 * LanguageFileType models for MM.
 */
public class MMFileType extends LanguageFileType {

    //~ Constructors .................................................................................................................................

    private MMFileType() {
        super(MMLanguage.INSTANCE);
    }

    //~ Methods ......................................................................................................................................

    public String getCharset(@NotNull VirtualFile virtualFile, @NotNull byte[] bytes) {
        return null;
    }

    @NotNull public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @NotNull public String getDescription() {
        return "Entity Definition Language files";
    }

    public Icon getIcon() {
        return ENTITY_FILE_ICON;
    }

    @NotNull public String getName() {
        return MMLanguage.ID;
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if given {@link VirtualFile} is a MetaModel file. */
    public static boolean isMMFile(@NotNull final VirtualFile file) {
        return DEFAULT_EXTENSION.equals(file.getExtension());
    }
    /** Returns true if given {@link PsiFile} is a MetaModel file. */
    public static boolean isMMFile(@NotNull final PsiElement file) {
        return file instanceof MMFile;
    }

    /** Returns Icon for the desired MetaModel. */
    @Nullable public static Icon getIconFor(MMCommonComposite mm) {
        return mm.getIcon(0);
    }

    //~ Static Fields ................................................................................................................................

    public static final Icon ENTITY_ICON            = IconLoader.getIcon("/tekgenesis/lang/mm/Entity.png");
    public static final Icon ENUM_ICON              = IconLoader.getIcon("/tekgenesis/lang/mm/Enum.png");
    public static final Icon ENUM_VALUE_ICON        = IconLoader.getIcon("/tekgenesis/lang/mm/EnumValue.png");
    public static final Icon ATTRIBUTE_ICON         = IconLoader.getIcon("/tekgenesis/lang/mm/Attribute.png");
    public static final Icon ENTITY_REF             = IconLoader.getIcon("/tekgenesis/lang/mm/EntityRef.png");
    public static final Icon VIEW_ICON              = IconLoader.getIcon("/tekgenesis/lang/mm/View.png");
    @SuppressWarnings("StaticVariableMayNotBeInitialized")
    public static Icon       ENTITY_FILE_ICON;  // set in MMApplicationsSettingManager
    @SuppressWarnings("StaticVariableMayNotBeInitialized")
    public static Icon       ENTITY_FILE_ICON_SMALL;
    public static final Icon FORM_ICON             = IconLoader.getIcon("/tekgenesis/lang/mm/Form.png");
    public static final Icon WIDGET_DEF_ICON       = FORM_ICON;
    public static final Icon HANDLER_ICON          = IconLoader.getIcon("/tekgenesis/lang/mm/Handler.png");
    public static final Icon ROUTE_ICON            = IconLoader.getIcon("/tekgenesis/lang/mm/Route.png");
    public static final Icon CONTEXT_ICON          = IconLoader.getIcon("/tekgenesis/lang/mm/Context.png");
    public static final Icon CASE_ICON             = IconLoader.getIcon("/tekgenesis/lang/mm/CaseIcon.png");
    public static final Icon WIDGET_ICON           = IconLoader.getIcon("/tekgenesis/lang/mm/FormField.png");
    public static final Icon PRIMARY_KEY_ICON      = IconLoader.getIcon("/tekgenesis/lang/mm/primary_key.png");
    public static final Icon FORM_PRIMARY_KEY_ICON = IconLoader.getIcon("/tekgenesis/lang/mm/mario_key.png");
    public static final Icon DESCRIBED_BY_ICON     = IconLoader.getIcon("/tekgenesis/lang/mm/described_by.png");
    public static final Icon LAYOUT                = IconLoader.getIcon("/tekgenesis/lang/mm/layout.png");
    public static final Icon MENU_ICON             = IconLoader.getIcon("/tekgenesis/lang/mm/menu.png");
    public static final Icon ROLE_ICON             = IconLoader.getIcon("/tekgenesis/lang/mm/role.png");
    public static final Icon TASK_ICON             = IconLoader.getIcon("/tekgenesis/lang/mm/task.png");
    public static final Icon TYPE_ICON             = IconLoader.getIcon("/tekgenesis/lang/mm/type.gif");
    public static final Icon LINK_ICON             = IconLoader.getIcon("/tekgenesis/lang/mm/MenuLink.png");

    @NonNls public static final String DEFAULT_EXTENSION = Constants.META_MODEL_EXT;

    public static final LanguageFileType INSTANCE = new MMFileType();
}  // end class MMFileType
